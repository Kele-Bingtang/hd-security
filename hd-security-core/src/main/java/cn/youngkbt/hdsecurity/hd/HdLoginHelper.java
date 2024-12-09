package cn.youngkbt.hdsecurity.hd;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.config.HdSecurityConfigProvider;
import cn.youngkbt.hdsecurity.error.HdSecurityErrorCode;
import cn.youngkbt.hdsecurity.exception.HdSecurityLoginException;
import cn.youngkbt.hdsecurity.listener.HdSecurityEventCenter;
import cn.youngkbt.hdsecurity.model.login.HdLoginModel;
import cn.youngkbt.hdsecurity.model.login.HdLoginModelOperator;
import cn.youngkbt.hdsecurity.model.session.HdSession;
import cn.youngkbt.hdsecurity.model.session.HdTokenDevice;
import cn.youngkbt.hdsecurity.utils.HdCollectionUtil;
import cn.youngkbt.hdsecurity.utils.ObjectUtil;

import java.util.List;

/**
 * 登录工具类
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
        if (!Boolean.TRUE.equals(HdSecurityManager.getConfig().getDynamicActiveTimeout()) && null == loginModel.getTokenActiveExpireTime()) {
            HdSecurityManager.getLog().warn("当前全局配置未开启动态 activeTimeout 功能，传入的 activeTimeout 参数将被忽略");
        }
    }

    // ---------- 注销方法 ----------

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
     * 通过 Token 注销
     *
     * @param token          Token
     * @param accountSession 账号会话
     */
    public void logoutByToken(String token, HdSession accountSession) {
        HdSessionHelper hdSessionHelper = HdHelper.sessionHelper(accountType);
        HdTokenHelper tokenHelper = HdHelper.tokenHelper(accountType);

        Object loginId = tokenHelper.getLoginIdByToken(token);
        // 发布注销开始事件
        HdSecurityEventCenter.publishBeforeLogout(accountType, loginId);

        // 清除 Token 的最后活跃时间
        if (HdSecurityConfigProvider.isUseActiveTimeout()) {
            tokenHelper.removeTokenActiveTime(token);
        }

        // 清除 Token Session
        hdSessionHelper.removeTokenSession(token, accountType);

        // 清除 Token -> id 的映射关系
        tokenHelper.removeTokenAndLoginIdMapping(token);

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

}
