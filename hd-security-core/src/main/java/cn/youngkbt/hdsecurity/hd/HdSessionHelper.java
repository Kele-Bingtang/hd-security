package cn.youngkbt.hdsecurity.hd;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.config.HdSecurityConfig;
import cn.youngkbt.hdsecurity.config.HdSecurityConfigProvider;
import cn.youngkbt.hdsecurity.constants.DefaultConstant;
import cn.youngkbt.hdsecurity.error.HdSecurityErrorCode;
import cn.youngkbt.hdsecurity.exception.HdSecurityLoginException;
import cn.youngkbt.hdsecurity.exception.HdSecuritySessionException;
import cn.youngkbt.hdsecurity.exception.HdSecurityTokenException;
import cn.youngkbt.hdsecurity.model.login.HdLoginModel;
import cn.youngkbt.hdsecurity.model.login.HdLoginModelOperator;
import cn.youngkbt.hdsecurity.model.session.HdAccountSession;
import cn.youngkbt.hdsecurity.model.session.HdSession;
import cn.youngkbt.hdsecurity.model.session.HdTokenDevice;
import cn.youngkbt.hdsecurity.model.session.HdTokenSession;
import cn.youngkbt.hdsecurity.strategy.HdSecuritySessionCreateStrategy;
import cn.youngkbt.hdsecurity.strategy.HdSecurityTokenGenerateStrategy;
import cn.youngkbt.hdsecurity.utils.HdStringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Hd Security Session 模块
 *
 * @author Tianke
 * @date 2024/11/28 01:26:59
 * @since 1.0.0
 */
public class HdSessionHelper {
    private final String accountType;

    public HdSessionHelper() {
        this(DefaultConstant.DEFAULT_ACCOUNT_TYPE);
    }

    public HdSessionHelper(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountType() {
        return accountType;
    }

    // ---------- Account Session 相关操作方法 ---------

    /**
     * 创建账号会话
     *
     * @param hdLoginModel 登录模型
     * @return token
     */
    public String createAccountSession(HdLoginModel hdLoginModel) {
        // 检查登录模型
        HdHelper.loginHelper(accountType).checkLoginModel(hdLoginModel);

        // 初始化登录模型
        HdLoginModel loginModel = HdLoginModelOperator.mutate(hdLoginModel);

        Object loginId = loginModel.getLoginId();
        Long tokenExpireTime = loginModel.getTokenExpireTime();

        HdSecurityConfig config = HdSecurityManager.getConfig(accountType);
        // 如果不允许一个账号多地同时登录，则需要先将这个账号的历史登录会话标记为 被顶下线
        if (Boolean.FALSE.equals(config.getConcurrent())) {
            HdHelper.loginHelper(accountType).replaced(loginId, loginModel.getDevice());
        }

        // 获取 Account Session 会话，如果获取失败，则代表第一次登录，需要创建新的会话
        HdSession accountSession = getAccountSessionByLoginIdOrCreate(loginId, tokenExpireTime);

        // 创建 Token
        HdTokenHelper tokenHelper = HdHelper.tokenHelper(accountType);
        String token = tokenHelper.createLoginToken(loginModel);

        // 创建设备，一个设备持有一个 Token
        HdTokenDevice tokenDevice = new HdTokenDevice(token, loginModel.getDevice(), loginModel.getTokenDeviceData());
        // 在 Account Session 记录登录的设备
        accountSession.addDevice(tokenDevice);
        
        /*
          保存 token -> loginId 的映射关系，方便日后根据 token 找 loginId
          场景 1：通过 token 查找所在的 Account Session，需要遍历所有 Account Session 下的 TokenDevice 集合。比较麻烦，所以创建映射关系，通过 token 先找到 loginId，接着根据 loginId 可以直接遍历出匹配的 Account Session
          场景 2：校验 Token 是否已经被创建使用时，只需要通过 Token 找到 loginId，找不到则代表没有登录过，token 也就没有被创建，找到就代表 token 已经被使用，不再是唯一
          @see  HdTokenHelper#createToken(HdLoginModel)
         */
        tokenHelper.addTokenAndLoginIdMapping(token, loginId, loginModel.getTokenExpireTime());

        // 更新 Token 最后活跃时间
        if (HdSecurityConfigProvider.isUseActiveExpireTime()) {
            tokenHelper.addTokenActiveTime(token, loginModel.getTokenActiveExpireTime(), tokenExpireTime);
        }

        // 如果该 token 对应的 Token Session 已经存在，则续期
        HdSession tokenSession = getTokenSessionByToken(token);
        if (null != tokenSession) {
            tokenSession.updateExpireTimeWhenCondition(loginModel.getTokenExpireTime(), true);
        }

        // 检查此账号会话数量是否超出最大值，如果超过，则按照登录时间顺序，把最开始登录的给注销掉
        if (config.getMaxLoginCount() != -1) {
            HdHelper.loginHelper(accountType).logoutByMaxLoginCount(loginId, accountSession, null, config.getMaxLoginCount());
        }

        return token;
    }

    /**
     * 获取账号会话
     *
     * @return 账号会话
     */
    public HdAccountSession getAccountSession() {
        return getAccountSessionByLoginId(HdHelper.loginHelper(accountType).getLoginId());
    }

    /**
     * 获取账号会话，如果不存在账号会话，则创建一个
     *
     * @return 账号会话
     */
    public HdAccountSession getAccountSessionOrCreate() {
        return getAccountSessionOrCreate(null);
    }

    /**
     * 获取账号会话，如果不存在账号会话，则创建一个
     *
     * @param expireTime 过期时间
     * @return 账号会话
     */
    public HdAccountSession getAccountSessionOrCreate(Long expireTime) {
        return getAccountSessionByLoginIdOrCreate(HdHelper.loginHelper(accountType).getLoginId(), expireTime);
    }

    /**
     * 获取账号会话，如果不存在账号会话，则创建一个
     *
     * @param loginId 登录 ID
     * @return 账号会话
     */
    public HdAccountSession getAccountSessionByLoginIdOrCreate(Object loginId) {
        return getAccountSessionByLoginIdOrCreate(loginId, null);
    }

    /**
     * 获取账号会话，如果不存在账号会话，则创建一个
     *
     * @param loginId    登录 ID
     * @param expireTime 过期时间
     * @return 账号会话
     */
    public HdAccountSession getAccountSessionByLoginIdOrCreate(Object loginId, Long expireTime) {
        // 获取账号会话
        HdAccountSession accountSession = getAccountSessionByLoginId(loginId);

        if (null == accountSession) {
            // 策略模式创建 Account Session
            accountSession = HdSecuritySessionCreateStrategy.instance.createAccountSession.apply(String.valueOf(loginId), accountType);
            Long sessionExpireTime = Optional.ofNullable(expireTime).orElse(HdSecurityManager.getConfig(accountType).getTokenExpireTime());

            // 存储到持久层
            HdSecurityManager.getRepository().addSession(accountSession, sessionExpireTime);
        } else {
            // 在持久层更新缓存的时间
            accountSession.updateExpireTimeWhenCondition(expireTime, true);
        }
        return accountSession;
    }

    /**
     * 根据登录 ID 获取账号会话
     *
     * @param loginId 登录 ID
     * @return 账号会话
     */
    public HdAccountSession getAccountSessionByLoginId(Object loginId) {
        if (HdStringUtil.hasEmpty(loginId)) {
            throw new HdSecuritySessionException("Account-Session 获取失败：loginId 不能为空").setCode(HdSecurityErrorCode.LOGIN_ID_IS_NULL);
        }

        return (HdAccountSession) HdSecurityManager.getRepository().querySession(RepositoryKeyHelper.getAccountSessionKey(accountType, loginId));
    }

    /**
     * 搜索 Account SessionId 列表
     *
     * @param keyword  关键词
     * @param start    开始位置
     * @param size     要获取的数据条数 （值为 -1 代表一直获取到末尾）
     * @param sortType 是否排序
     * @return Token 列表
     */
    public List<String> searchAccountSessionIdList(String keyword, int start, int size, boolean sortType) {
        return HdSecurityManager.getRepository().searchKeyList(RepositoryKeyHelper.getAccountSessionKey(accountType, ""), keyword, start, size, sortType);
    }

    // ---------- Account Session ExpireTime 获取操作方法 ---------

    /**
     * 获取当前账号会话过期时间
     *
     * @return 账号会话过期时间
     */
    public long getAccountSessionExpireTime() {
        return getAccountSessionExpireTime(HdHelper.loginHelper(accountType).getLoginId());
    }

    /**
     * 获取指定账号会话过期时间
     *
     * @param loginId 登录 ID
     * @return 账号会话过期时间
     */
    public long getAccountSessionExpireTime(Object loginId) {
        return HdSecurityManager.getRepository().getSessionTimeout(RepositoryKeyHelper.getAccountSessionKey(accountType, loginId));
    }

    /**
     * 获取指定 Token 的账号会话过期时间
     *
     * @param token Token
     * @return 账号会话过期时间
     */
    public long getAccountSessionExpireTimeByToken(String token) {
        Object loginId = HdHelper.loginHelper(accountType).getLoginIdByToken(token);
        return getAccountSessionExpireTime(loginId);
    }

    // ---------- Token Session 相关操作方法 ---------

    /**
     * 创建 Token Session 会话
     * <p>专门针对【不使用 LoginId 登录，使用随机 Token 登录】使用</p>
     * <p>如果已经登录则使用 HdSessionHelper#getTokenSessionOrCreate() 获取 Token Session 会话</p>
     *
     * @return Token Session 会话
     */
    public HdTokenSession createTokenSession() {
        HdTokenHelper tokenHelper = HdHelper.tokenHelper(accountType);

        // 创建一个 Token
        String token = HdSecurityTokenGenerateStrategy.instance.generateUniqueElement.generate(
                "Token",
                // 最大尝试次数
                tokenHelper.getMaxTryTimes(),
                // 创建 Token
                () -> tokenHelper.createToken(null),
                // 验证 Token 唯一性，这里从持久层获取根据创建的 Token 获取登录 ID，获取成功代表有用户在用，则不唯一
                newToken -> tokenHelper.getLoginIdByToken(newToken) == null,
                // 捕获异常
                e -> {
                    throw e;
                }
        );

        // 如果开启活跃时间功能，则添加 token 的最后活跃时间
        if (HdSecurityConfigProvider.isUseActiveExpireTime()) {
            tokenHelper.addTokenActiveTime(token, null, null);
        }

        // 将 Token 写到 Web 响应里
        tokenHelper.writeTokenToWeb(token);

        // 创建 Token Session 对象并返回
        return getTokenSessionByTokenOrCreate(token);
    }

    /**
     * 获取 Token-Session
     *
     * @return Token-Session
     */
    public HdTokenSession getTokenSession() {
        // 如果配置了 tokenSessionCheckLogin == true，则需要先校验当前是否登录，未登录情况下不允许拿到 Token-Session
        if (Boolean.TRUE.equals(HdSecurityManager.getConfig(accountType).getTokenSessionCheckLogin())) {
            HdHelper.loginHelper(accountType).checkLogin();
        }

        // 根据 Web 提供的 Token 获取对应的 Token-Session 对象
        return getTokenSessionByToken(HdHelper.tokenHelper(accountType).getWebToken());
    }

    /**
     * 获取 Token-Session，如果不存在 Token-Session，则创建一个并返回
     *
     * @return Token-Session
     */
    public HdTokenSession getTokenSessionOrCreate() {
        // 如果配置了 tokenSessionCheckLogin == true，则需要先校验当前是否登录，未登录情况下不允许拿到 Token-Session
        if (Boolean.TRUE.equals(HdSecurityManager.getConfig(accountType).getTokenSessionCheckLogin())) {
            HdHelper.loginHelper(accountType).checkLogin();
        }

        // 根据 Web 提供的 Token 获取对应或者创建 Token-Session 对象
        return getTokenSessionByTokenOrCreate(HdHelper.tokenHelper(accountType).getWebToken());
    }

    /**
     * 获取 Token-Session，如果不存在 Token-Session，则创建一个并返回
     *
     * @return Token-Session
     */
    public HdTokenSession getTokenSessionByTokenOrCreate(String token) {
        HdTokenSession tokenSession = getTokenSessionByToken(token);

        if (null == tokenSession) {
            // 策略模式创建 Account Session
            tokenSession = HdSecuritySessionCreateStrategy.instance.createTokenSession.apply(String.valueOf(token), accountType);
            // 默认 Token Session 的过期时间与 Token 和 LoginId 映射的过期时间一致，因此这里尝试获取 Token 和 LoginId 映射的过期时间，如果没有则使用全局的过期时间配置
            long expireTime = HdHelper.tokenHelper(accountType).getTokenAndLoginIdExpireTime(token);

            if (expireTime == -2) {
                expireTime = HdSecurityManager.getConfig(accountType).getTokenExpireTime();
            }

            // 存储到持久层
            HdSecurityManager.getRepository().addSession(tokenSession, expireTime);
        }
        return tokenSession;
    }

    /**
     * 根据 Token 获取 Token-Session
     *
     * @param token Token
     * @return Token-Session
     */
    public HdTokenSession getTokenSessionByToken(String token) {
        if (HdStringUtil.hasEmpty(token)) {
            throw new HdSecurityTokenException("Token-Session 获取失败：token 不能为空").setCode(HdSecurityErrorCode.TOKEN_IS_NULL);
        }

        return (HdTokenSession) HdSecurityManager.getRepository().querySession(RepositoryKeyHelper.getTokenSessionKey(accountType, token));
    }

    /**
     * 根据 Token 删除 Token 会话
     *
     * @param token Token
     */
    public void removeTokenSession(String token) {
        HdSecurityManager.getRepository().removeSession(RepositoryKeyHelper.getTokenSessionKey(accountType, token));
    }

    /**
     * 搜索 Token SessionId 列表
     *
     * @param keyword  关键词
     * @param start    开始位置
     * @param size     要获取的数据条数 （值为 -1 代表一直获取到末尾）
     * @param sortType 是否排序
     * @return Token 列表
     */
    public List<String> searchTokenSessionIdList(String keyword, int start, int size, boolean sortType) {
        return HdSecurityManager.getRepository().searchKeyList(RepositoryKeyHelper.getTokenSessionKey(accountType, ""), keyword, start, size, sortType);
    }

    // ---------- Token Session ExpireTime 获取操作方法 ---------

    /**
     * 获取当前 Token-Session 的过期时间
     *
     * @return 过期时间
     */
    public long getTokenSessionExpireTime() {
        return getTokenSessionExpireTime(HdHelper.tokenHelper().getWebToken());
    }

    /**
     * 获取指定 Token 的过期时间
     *
     * @param token Token
     * @return 过期时间
     */
    public long getTokenSessionExpireTime(String token) {
        return HdSecurityManager.getRepository().getSessionTimeout(RepositoryKeyHelper.getTokenSessionKey(accountType, token));
    }

    // --------- TokenDevice 相关操作方法 ---------

    /**
     * 根据登录 Id 获取账号会话中的 TokenDevice 列表
     *
     * @param loginId 登录 ID
     * @return Token 列表
     */
    public List<HdTokenDevice> getTokenDeviceList(Object loginId) {
        return getTokenDeviceList(loginId, null);
    }

    /**
     * 根据登录 Id 和指定设备获取账号会话中的 TokenDevice 列表
     *
     * @param loginId 登录 ID
     * @param device  设备
     * @return Token 列表
     */
    public List<HdTokenDevice> getTokenDeviceList(Object loginId, String device) {
        HdSession session = getAccountSessionByLoginId(loginId);
        if (session == null) {
            return new ArrayList<>();
        }

        return session.getTokenDeviceListByDevice(device);
    }

    /**
     * 获取账号会话中的 TokenDevice
     *
     * @return TokenDevice
     */
    public HdTokenDevice getTokenDeviceByToken() {
        return getTokenDeviceByToken(HdHelper.tokenHelper().getWebToken());
    }

    /**
     * 根据 Token 获取账号会话中的 TokenDevice
     *
     * @param token Token
     * @return TokenDevice
     */
    public HdTokenDevice getTokenDeviceByToken(String token) {
        // 如果 token 为 null，直接提前返回
        if (HdStringUtil.hasEmpty(token)) {
            return null;
        }

        // 根据 Token 获取登录 ID，如果获取不到或者登录 ID 是框架使用的关键词，则返回 null
        Object loginId = HdHelper.loginHelper(accountType).getLoginIdByToken(token);
        if (HdStringUtil.hasEmpty(loginId) || HdSecurityLoginException.KEYWORD_LIST.contains(String.valueOf(loginId))) {
            return null;
        }

        if (null == HdHelper.tokenHelper(accountType).getTokenActiveTime(token)) {
            return null;
        }

        // 获取这个账号的 Account Session
        HdAccountSession accountSession = getAccountSessionByLoginId(loginId);

        // 为 null 说明尚未登录，当然也就不存在什么设备类型，直接返回 null
        if (null == accountSession) {
            return null;
        }

        // 从 Account Session 获取 TokenDevice
        return accountSession.getTokenDeviceByToken(token);
    }

    /**
     * 获取指定账号会话中的 Token 列表
     *
     * @param loginId 登录 ID
     * @return Token 列表
     */
    public List<String> getTokenList(Object loginId) {
        return getTokenList(loginId, null);
    }

    /**
     * 获取指定账号会话中指定设备的 Token 列表
     *
     * @param loginId 登录 ID
     * @param device  设备
     * @return Token 列表
     */
    public List<String> getTokenList(Object loginId, String device) {
        HdSession session = getAccountSessionByLoginId(loginId);
        return session.getTokenListByDevice(device);
    }

    /**
     * 获取账号会话中的最后一个 Token
     *
     * @param loginId 登录 ID
     * @return Token
     */
    public String getLastToken(Object loginId) {
        return getLastToken(loginId, null);
    }

    /**
     * 获取账号会话中指定设备的最后一个 Token
     *
     * @param loginId 登录 ID
     * @param device  设备
     * @return Token
     */
    public String getLastToken(Object loginId, String device) {
        HdSession session = getAccountSessionByLoginId(loginId);
        List<String> tokenList = session.getTokenListByDevice(device);
        if (tokenList.isEmpty()) {
            return null;
        }
        return tokenList.get(tokenList.size() - 1);
    }
}
