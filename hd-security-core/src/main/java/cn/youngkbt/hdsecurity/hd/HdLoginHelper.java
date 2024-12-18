package cn.youngkbt.hdsecurity.hd;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.config.HdSecurityConfigProvider;
import cn.youngkbt.hdsecurity.constants.DefaultConstant;
import cn.youngkbt.hdsecurity.error.HdSecurityErrorCode;
import cn.youngkbt.hdsecurity.exception.HdSecurityLoginException;
import cn.youngkbt.hdsecurity.exception.HdSecuritySecondAuthException;
import cn.youngkbt.hdsecurity.listener.HdSecurityEventCenter;
import cn.youngkbt.hdsecurity.model.login.HdLoginModel;
import cn.youngkbt.hdsecurity.model.login.HdLoginModelOperator;
import cn.youngkbt.hdsecurity.model.session.HdSession;
import cn.youngkbt.hdsecurity.model.session.HdTokenDevice;
import cn.youngkbt.hdsecurity.utils.HdCollectionUtil;
import cn.youngkbt.hdsecurity.utils.HdStringUtil;
import cn.youngkbt.hdsecurity.utils.ObjectUtil;

import java.util.List;
import java.util.Objects;

/**
 * Hd Security 登录模块
 * 包含：登录、注销、踢人下线、注销他人、获取登录信息
 *
 * @author Tianke
 * @date 2024/11/25 01:02:21
 * @since 1.0.0
 */
public class HdLoginHelper {

    private final String accountType;

    public HdLoginHelper(String accountType) {
        this.accountType = accountType;
    }

    // ---------- 登录相关操作方法 ----------

    /**
     * 执行登录操作
     *
     * @param loginId 登录 ID
     * @return Token
     */
    public String login(Object loginId) {
        return login(HdLoginModelOperator.build().setLoginId(loginId));
    }

    /**
     * 执行登录操作
     *
     * @param loginId 登录 ID
     * @param device  设备
     * @return Token
     */
    public String login(Object loginId, String device) {
        return login(HdLoginModelOperator.build().setLoginId(loginId).setDevice(device));
    }

    /**
     * 执行登录操作
     *
     * @param loginId         登录 ID
     * @param tokenExpireTime Token 过期时间
     * @return Token
     */
    public String login(Object loginId, long tokenExpireTime) {
        return login(HdLoginModelOperator.build().setLoginId(loginId).setTokenExpireTime(tokenExpireTime));
    }

    /**
     * 执行登录操作
     *
     * @param loginId    登录 ID
     * @param rememberMe 是否记住我
     * @return Token
     */
    public String login(Object loginId, boolean rememberMe) {
        return login(HdLoginModelOperator.build().setLoginId(loginId).setRememberMe(rememberMe));
    }

    /**
     * 执行登录操作
     *
     * @param loginModel 登录参数
     * @return Token
     */
    public String login(HdLoginModel loginModel) {
        // 发布登录开始事件
        HdSecurityEventCenter.publishBeforeLogin(accountType, loginModel.getLoginId());

        // 创建账号会话，并获取账号会话绑定的 Token
        String token = HdHelper.sessionHelper(accountType).createAccountSession(loginModel);

        // 发布登录结束事件：账号 xxx 登录成功
        HdSecurityEventCenter.publishAfterLogin(accountType, loginModel.getLoginId(), token, loginModel);

        // 将 Token 写入到 Cookie、响应体等
        HdHelper.tokenHelper(accountType).writeTokenToWeb(token, loginModel);

        return token;
    }

    /**
     * 登录模型检查
     *
     * @param loginModel 登录模型
     */
    public void checkLoginModel(HdLoginModel loginModel) {
        Object loginId = loginModel.getLoginId();
        // 账号 ID 不能为空
        if (null == loginId) {
            throw new HdSecurityLoginException("loginId 不能为空").setCode(HdSecurityErrorCode.LOGIN_ID_IS_NULL);
        }

        // 账号 id 不能是复杂类型
        if (!ObjectUtil.isBasicType(loginId.getClass())) {
            HdSecurityManager.getLog().warn("loginId 应该为简单类型，例如：String | int | long，不推荐使用复杂类型：" + loginId.getClass());
        }

        // 如果全局配置未启动动态 activeTimeout 功能，但是此次登录却传入了 activeTimeout 参数，那么就打印警告信息
        if (Boolean.FALSE.equals(HdSecurityManager.getConfig(accountType).getDynamicActiveExpireTime()) && null != loginModel.getTokenActiveExpireTime()) {
            HdSecurityManager.getLog().warn("当前全局配置未开启动态 activeTimeout 功能，传入的 activeTimeout 参数将被忽略");
        }
    }

    /**
     * 获取当前会话的 LoginId
     *
     * @return LoginId
     */
    public Object getLoginId() {
        // 根据 Token 获取 LoginId
        return getLoginIdByToken(HdHelper.tokenHelper(accountType).getWebToken());
    }

    /**
     * 根据 Token 获取 LoginId
     *
     * @param token Token
     * @return LoginId
     */
    public Object getLoginIdByToken(String token) {
        if (HdStringUtil.hasEmpty(token)) {
            return null;
        }
        // 检查 Token 是否被冻结
        HdHelper.tokenHelper(accountType).checkTokenActiveTime(token);

        // 如果开启 Token 冻结功能和续签功能，则更新 Token 的最活跃时间为现在
        if (HdSecurityConfigProvider.isUseActiveExpireTime() && Boolean.TRUE.equals(HdSecurityManager.getConfig(accountType).getAutoRenew())) {
            HdHelper.tokenHelper(accountType).updateTokenLastActiveTimeToNow(token);
        }

        // 先判断一下当前会话是否正在临时身份切换, 如果是则返回临时身份
        if (isSwitch()) {
            return getSwitchLoginId();
        }

        // 查找 token 对应的 loginId
        Object loginId = HdHelper.tokenHelper(accountType).getLoginIdByToken(token);
        // 如果 loginId 不存在，则返回 null
        if (HdStringUtil.hasEmpty(loginId)) {
            return null;
        }

        // 如果 loginId 为 Hd Security 内部使用的关键词，则返回 null
        if (HdSecurityLoginException.KEYWORD_LIST.contains(String.valueOf(loginId))) {
            return null;
        }
        return loginId;
    }

    /**
     * 判断当前会话是否已经登录
     *
     * @return 已登录返回 true，未登录返回 false
     */
    public boolean isLogin() {
        HdTokenHelper tokenHelper = HdHelper.tokenHelper(accountType);
        // 根据 Token 获取 LoginId
        String webToken = tokenHelper.getWebToken();
        // 如果 Token 不存在或已被冻结，则返回 null
        if (HdStringUtil.hasEmpty(webToken) || null == tokenHelper.getTokenRemainActiveTime(webToken)) {
            return false;
        }
        return null != tokenHelper.getLoginIdByToken();
    }

    /**
     * 判断指定账号 ID 是否已经登录
     *
     * @param loginId 账号 ID
     * @return 已登录返回 true，未登录返回 false
     */
    public boolean isLogin(Object loginId) {
        List<HdTokenDevice> tokenDeviceList = HdHelper.sessionHelper(accountType).getTokenDeviceList(loginId);
        return HdCollectionUtil.isNotEmpty(tokenDeviceList);
    }

    /**
     * 检查当前会话是否已经登录，没有登录则抛出异常
     */
    public void checkLogin() {
        checkLogin(HdHelper.tokenHelper(accountType).getWebToken());
    }

    /**
     * 检查当前会话是否已经登录，没有登录则抛出异常
     *
     * @param token Token
     */
    public void checkLogin(String token) {
        // 如果 Token 为空，则抛出异常
        if (HdStringUtil.hasEmpty(token)) {
            throw new HdSecurityLoginException("未能读取到有效 Token").setCode(HdSecurityErrorCode.TOKEN_IS_NULL);
        }
        // 如果 Token 被冻结（过期），则抛出异常
        HdHelper.tokenHelper(accountType).checkTokenActiveTime(token);

        // 查找 token 对应的 loginId
        Object loginId = HdHelper.tokenHelper(accountType).getLoginIdByToken(token);

        // 如果开启 Token 冻结功能和续签功能，则更新 Token 的最活跃时间为现在
        if (HdSecurityConfigProvider.isUseActiveExpireTime() && Boolean.TRUE.equals(HdSecurityManager.getConfig(accountType).getAutoRenew())) {
            HdHelper.tokenHelper(accountType).updateTokenLastActiveTimeToNow(token);
        }

        // 如果 loginId 不存在，则抛出异常
        if (HdStringUtil.hasEmpty(loginId)) {
            throw new HdSecurityLoginException("Token 无效或者登录过期").setCode(HdSecurityErrorCode.TOKEN_INVALID);
        }

        // 如果 loginId 被标注为踢人下线，则抛出异常
        if (Objects.equals(loginId, HdSecurityLoginException.KICK_OUT)) {
            throw new HdSecurityLoginException("Token 已被踢下线").setCode(HdSecurityErrorCode.TOKEN_KICK_OUT);
        }

        // 如果 loginId 被标注为顶人下线，则抛出异常
        if (Objects.equals(loginId, HdSecurityLoginException.REPLACED)) {
            throw new HdSecurityLoginException("Token 已被顶下线").setCode(HdSecurityErrorCode.TOKEN_REPLACED);
        }

        // 如果 loginId 为 Hd Security 内部使用的关键词中的值，则返回 null
        if (HdSecurityLoginException.KEYWORD_LIST.contains(String.valueOf(loginId))) {
            throw new HdSecurityLoginException("LoginId 为内部使用的关键词").setCode(HdSecurityErrorCode.LOGIN_ID_IS_KEYWORD);
        }
    }

    /**
     * 检查当前会话是否已经登录，没有登录则抛出异常，登录则返回 LoginId
     *
     * @return LoginId
     */
    public Object checkLoginThenGet() {
        return checkLoginThenGet(HdHelper.tokenHelper(accountType).getWebToken());
    }

    /**
     * 检查当前会话是否已经登录，没有登录则抛出异常，登录则返回 LoginId
     *
     * @param token Token
     * @return LoginId
     */
    public Object checkLoginThenGet(String token) {
        checkLogin(token);
        return getLoginIdByToken(token);
    }

    // ---------- 注销相关操作方法 ----------

    /**
     * 注销
     *
     * @param loginId 登录 ID
     */
    public void logout(Object loginId) {
        logout(loginId, null);
    }

    /**
     * 注销
     *
     * @param loginId 登录 ID
     * @param device  设备
     */
    public void logout(Object loginId, String device) {
        HdSession accountSession = HdHelper.sessionHelper(accountType).getAccountSessionByLoginId(loginId);
        if (null == accountSession) {
            return;
        }

        List<HdTokenDevice> tokenDeviceList = accountSession.getTokenDeviceListByDevice(device);

        for (HdTokenDevice tokenDevice : tokenDeviceList) {
            logoutByToken(tokenDevice.getToken(), accountSession);
        }
    }

    /**
     * 通过 Token 注销
     *
     * @param token Token
     */
    public void logoutByToken(String token) {
        logoutByToken(token, null);
    }

    /**
     * 通过 Token 注销，如果账号会话为空，则根据 token 获取对应的账号会话
     *
     * @param token          Token
     * @param accountSession 账号会话
     */
    public void logoutByToken(String token, HdSession accountSession) {
        exitLoginByToken(token, accountSession, () -> {
            // 清除 Token Session
            HdHelper.sessionHelper(accountType).removeTokenSession(token);

            // 清除 Token -> id 的映射关系
            HdHelper.tokenHelper(accountType).removeTokenAndLoginIdMapping(token);
        });
    }

    /**
     * 账号会话数量超出最大值，则按照登录时间顺序进行注销，保留 maxLoginCount 的登录数量
     *
     * @param loginId        登录 ID
     * @param accountSession Account Session
     * @param device         设备
     * @param maxLoginCount  保留的登录数量
     */
    public void logoutByMaxLoginCount(Object loginId, HdSession accountSession, String device, int maxLoginCount) {
        // 如果 Account Session 为空，则根据 loginId 获取对应的 session
        if (null == accountSession) {
            accountSession = HdHelper.sessionHelper(accountType).getAccountSessionByLoginId(loginId);
            if (null == accountSession) {
                return;
            }
        }

        List<HdTokenDevice> tokenDeviceList = accountSession.getTokenDeviceListByDevice(device);
        // 从前面注销超过 maxLoginCount 的登录数量
        for (int i = 0; i < tokenDeviceList.size() - maxLoginCount; i++) {
            logoutByToken(tokenDeviceList.get(i).getToken(), accountSession);
        }
    }


    // --------- 踢人下线相关操作方法 ----------

    /**
     * 根据登录 ID 踢人下线
     *
     * @param loginId 登录 ID
     */
    public void kickout(Object loginId) {
        kickout(loginId, null);
    }

    /**
     * 在指定的设备下踢人下线
     *
     * @param loginId 登录 ID
     * @param device  设备
     */
    public void kickout(Object loginId, String device) {
        HdSession accountSession = HdHelper.sessionHelper(accountType).getAccountSessionByLoginId(loginId);
        if (null == accountSession) {
            return;
        }

        List<HdTokenDevice> tokenDeviceList = accountSession.getTokenDeviceListByDevice(device);

        for (HdTokenDevice tokenDevice : tokenDeviceList) {
            kickoutByToken(tokenDevice.getToken(), accountSession);
        }
    }

    /**
     * 通过 Token 踢人下线
     *
     * @param token Token
     */
    public void kickoutByToken(String token) {
        kickoutByToken(token, null);
    }

    /**
     * 通过 Token 踢人下线，如果账号会话为空，则根据 token 获取对应的账号会话
     *
     * @param token          Token
     * @param accountSession 账号会话
     */
    public void kickoutByToken(String token, HdSession accountSession) {
        // 相比较注销，没有清除 Token Session，因为此时不是完全注销，Token Session 还有用
        exitLoginByToken(token, accountSession, () -> HdHelper.tokenHelper(accountType).editTokenAndLoginIdMapping(token, HdSecurityLoginException.KICK_OUT));
    }

    // --------- 顶人下线相关操作方法 ----------

    /**
     * 根据登录 ID 顶人下线
     *
     * @param loginId 登录 ID
     */
    public void replaced(Object loginId) {
        replaced(loginId, null);
    }

    /**
     * 在指定的设备下顶人下线
     *
     * @param loginId 登录 ID
     * @param device  设备
     */
    public void replaced(Object loginId, String device) {
        HdSession accountSession = HdHelper.sessionHelper(accountType).getAccountSessionByLoginId(loginId);
        if (null == accountSession) {
            return;
        }

        List<HdTokenDevice> tokenDeviceList = accountSession.getTokenDeviceListByDevice(device);

        for (HdTokenDevice tokenDevice : tokenDeviceList) {
            replacedByToken(tokenDevice.getToken(), accountSession);
        }
    }

    /**
     * 通过 Token 顶人下线
     *
     * @param token Token
     */
    public void replacedByToken(String token) {
        replacedByToken(token, null);
    }

    /**
     * 通过 Token 顶人下线，如果账号会话为空，则根据 token 获取对应的账号会话
     *
     * @param token          Token
     * @param accountSession 账号会话
     */
    public void replacedByToken(String token, HdSession accountSession) {
        // 相比较注销，没有清除 Token Session，因为此时不是完全注销，Token Session 还有用
        exitLoginByToken(token, accountSession, () -> HdHelper.tokenHelper(accountType).editTokenAndLoginIdMapping(token, HdSecurityLoginException.REPLACED));
    }

    /**
     * 退出登录，如果账号会话为空，则根据 token 获取对应的账号会话
     * 注销、踢人下线、顶人下线都用到该方法
     *
     * @param token             Token
     * @param accountSession    账号会话
     * @param exitExtraRunnable 退出登录的额外逻辑，给注销、踢人下线、顶人下线分别传入对应的逻辑
     */
    private void exitLoginByToken(String token, HdSession accountSession, Runnable exitExtraRunnable) {
        HdSessionHelper hdSessionHelper = HdHelper.sessionHelper(accountType);
        HdTokenHelper tokenHelper = HdHelper.tokenHelper(accountType);

        Object loginId = tokenHelper.getLoginIdByToken(token);
        // 发布注销开始事件
        HdSecurityEventCenter.publishBeforeLogout(accountType, loginId);

        // 清除 Token 的最后活跃时间
        if (HdSecurityConfigProvider.isUseActiveExpireTime()) {
            tokenHelper.removeTokenActiveTime(token);
        }
        // 退出登录的额外逻辑
        exitExtraRunnable.run();

        // 发布注销结束事件：xx 账号的 xx 客户端注销了
        HdSecurityEventCenter.publishAfterLogout(accountType, loginId, token);

        // 如果 Account Session 为空，则根据 loginId 获取对应的 session
        if (null == accountSession) {
            accountSession = hdSessionHelper.getAccountSessionByLoginId(loginId);
            // 如果根据 loginId 获取的 Account Session 为空，则不做任何处理
            if (null == accountSession) {
                return;
            }
        }

        // 清理这个账号的 Account Session 上的 Token Device，并且尝试注销掉 Account Session
        accountSession.removeTokenDevice(token);
        // 如果 Account Session 的 TokenDeviceList 为空，代表所有 Device 已经全部注销，直接注销掉这个 Account Session
        if (HdCollectionUtil.isEmpty(accountSession.getTokenDeviceList())) {
            accountSession.removeFromRepository();
        }
    }

    // ---------- 临时身份切换 ----------

    /**
     * 临时切换身份为指定账号 ID
     *
     * @param loginId 账号 ID
     */
    public void switchTo(Object loginId) {
        HdSecurityManager.getContext().getStorage().set(RepositoryKeyHelper.getSwitchLoginIdKey(accountType), loginId);
    }

    /**
     * 结束临时切换身份
     */
    public void endSwitch() {
        HdSecurityManager.getContext().getStorage().remove(RepositoryKeyHelper.getSwitchLoginIdKey(accountType));
    }

    /**
     * 判断当前请求是否正处于身份临时切换中
     *
     * @return 是否正处于身份临时切换中
     */
    public boolean isSwitch() {
        return HdSecurityManager.getContext().getStorage().get(RepositoryKeyHelper.getSwitchLoginIdKey(accountType)) != null;
    }

    /**
     * 返回身份临时切换的 loginId
     *
     * @return loginId
     */
    public Object getSwitchLoginId() {
        return HdSecurityManager.getContext().getStorage().get(RepositoryKeyHelper.getSwitchLoginIdKey(accountType));
    }

    /**
     * 在一个 lambda 代码段里，临时切换身份为指定账号 ID，lambda 结束后自动恢复
     *
     * @param loginId  指定账号id
     * @param runnable 要执行的方法
     */
    public void switchTo(Object loginId, Runnable runnable) {
        try {
            switchTo(loginId);
            runnable.run();
        } finally {
            endSwitch();
        }
    }

    // ---------- 二级认证 ----------

    /**
     * 获取当前登录账号的二级认证过期时间（单位: 秒, 返回 null 代表尚未通过二级认证）
     */
    public Long getSecondAuthTime() {
        return getSecondAuthTime(HdHelper.tokenHelper(accountType).getWebToken(), DefaultConstant.DEFAULT_SECOND_AUTH_REALM);
    }

    /**
     * 获取当前登录账号指定领域的二级认证过期时间（单位: 秒, 返回 null 代表尚未通过二级认证）
     */
    public Long getSecondAuthTime(String realm) {
        return getSecondAuthTime(HdHelper.tokenHelper(accountType).getWebToken(), realm);
    }

    /**
     * 获取指定登录账号指定领域的二级认证过期时间（单位: 秒, 返回 null 代表尚未通过二级认证）
     *
     * @param token Token
     * @param realm 领域
     * @return 二级认证过期时间（单位: 秒, 返回 null 代表尚未通过二级认证）
     */
    public Long getSecondAuthTime(String token, String realm) {
        if (HdStringUtil.hasEmpty(token)) {
            return null;
        }

        // 登录后才能开启二级认证，因此这里获取当前 Token 对应的 loginId 来判断
        Object loginId = getLoginIdByToken(token);
        if (HdStringUtil.hasEmpty(loginId)) {
            return null;
        }

        // 从持久层获取二级认证的过期时间
        return HdSecurityManager.getRepository().getExpireTime(getSecondAuthKey(token, realm));
    }

    /**
     * 开启二级认证
     *
     * @param secondAuthTime 二级认证过期时间（单位: 秒）
     */
    public void openSecondAuth(long secondAuthTime) {
        openSecondAuth(DefaultConstant.DEFAULT_SECOND_AUTH_REALM, secondAuthTime);
    }

    /**
     * 开启指定领域的二级认证
     *
     * @param realm          领域
     * @param secondAuthTime 二级认证过期时间（单位: 秒）
     */
    public void openSecondAuth(String realm, long secondAuthTime) {
        // 登录后才能开启二级认证
        checkLogin();
        // 获取当前 Web Token，如果不存在，则抛出异常
        String webToken = HdHelper.tokenHelper(accountType).checkWebTokenNonNullThenGet();

        // 发布二级认证开启前置事件
        HdSecurityEventCenter.publishBeforeSecondAuthOpen(accountType, webToken, realm, secondAuthTime);
        // 添加二级认证表示到持久IC
        HdSecurityManager.getRepository().add(getSecondAuthKey(webToken, realm), DefaultConstant.SECOND_AUTH_OPEN_TAG, secondAuthTime);

        // 发布二级认证开启后置事件
        HdSecurityEventCenter.publishAfterSecondAuthOpen(accountType, webToken, realm, secondAuthTime);
    }

    /**
     * 判断当前登录账号是否开启了二级认证
     */
    public boolean isSendAuth() {
        return isSecondAuth(HdHelper.tokenHelper(accountType).getWebToken(), DefaultConstant.DEFAULT_SECOND_AUTH_REALM);
    }

    /**
     * 判断当前登录账号指定领域的二级认证是否开启
     *
     * @param realm 领域
     */
    public boolean isSecondAuth(String realm) {
        return isSecondAuth(HdHelper.tokenHelper(accountType).getWebToken(), realm);
    }

    /**
     * 判断指定登录账号的指定领域是否开启了二级认证
     *
     * @param token Token
     * @return 是否开启了二级认证
     */
    public boolean isSecondAuth(String token, String realm) {
        if (HdStringUtil.hasEmpty(token)) {
            return false;
        }

        // 登录后才能开启二级认证，因此这里获取当前 Token 对应的 loginId 来判断
        Object loginId = getLoginIdByToken(token);
        if (HdStringUtil.hasEmpty(loginId)) {
            return false;
        }

        Object secondAuthTag = HdSecurityManager.getRepository().query(getSecondAuthKey(token, realm));
        return HdStringUtil.hasEmpty(secondAuthTag);
    }

    /**
     * 校验当前登录账号是否开启了二级认证
     */
    public void checkSecondAuth() {
        checkSecondAuth(HdHelper.tokenHelper(accountType).getWebToken(), DefaultConstant.DEFAULT_SECOND_AUTH_REALM);
    }

    /**
     * 校验当前登录账号指定领域的二级认证是否开启
     *
     * @param realm 领域
     */
    public void checkSecondAuth(String realm) {
        checkSecondAuth(HdHelper.tokenHelper(accountType).getWebToken(), realm);
    }

    /**
     * 校验指定登录账号指定领域的二级认证是否开启
     *
     * @param token Token
     * @param realm 领域
     */
    public void checkSecondAuth(String token, String realm) {
        boolean secondAuth = isSecondAuth(token, realm);
        if (!secondAuth) {
            throw new HdSecuritySecondAuthException(accountType, token, realm);
        }
    }

    /**
     * 关闭二级认证
     */
    public void closeSecondAuth() {
        closeSecondAuth(DefaultConstant.DEFAULT_SECOND_AUTH_REALM);
    }

    /**
     * 关闭指定领域的二级认证
     *
     * @param realm 领域
     */
    public void closeSecondAuth(String realm) {
        // 获取 Web 传来的 Token
        String webToken = HdHelper.tokenHelper(accountType).getWebToken();
        if (HdStringUtil.hasEmpty(webToken)) {
            return;
        }
        // 发布二级认证关闭前置事件
        HdSecurityEventCenter.publishBeforeSecondAuthClose(accountType, webToken, realm);
        // 删除 Token 对应的二级认证标识
        HdSecurityManager.getRepository().remove(getSecondAuthKey(webToken, realm));
        // 发布二级认证关闭后置事件
        HdSecurityEventCenter.publishAfterSecondAuthClose(accountType, webToken, realm);
    }

    /**
     * 获取二级认证标识的 Key
     *
     * @param token Token
     * @param realm 领域
     * @return 二级认证标识的 Key
     */
    public String getSecondAuthKey(String token, String realm) {
        return RepositoryKeyHelper.getSecondAuthKey(accountType, token, realm);
    }
}
