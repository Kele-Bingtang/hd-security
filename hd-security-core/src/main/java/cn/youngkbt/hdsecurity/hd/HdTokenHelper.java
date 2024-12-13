package cn.youngkbt.hdsecurity.hd;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.config.HdCookieConfig;
import cn.youngkbt.hdsecurity.config.HdSecurityConfig;
import cn.youngkbt.hdsecurity.config.HdSecurityConfigProvider;
import cn.youngkbt.hdsecurity.constants.DefaultConstant;
import cn.youngkbt.hdsecurity.context.HdSecurityContext;
import cn.youngkbt.hdsecurity.error.HdSecurityErrorCode;
import cn.youngkbt.hdsecurity.exception.HdSecurityTokenException;
import cn.youngkbt.hdsecurity.listener.HdSecurityEventCenter;
import cn.youngkbt.hdsecurity.model.cookie.HdCookie;
import cn.youngkbt.hdsecurity.model.login.HdLoginModel;
import cn.youngkbt.hdsecurity.model.login.HdLoginModelOperator;
import cn.youngkbt.hdsecurity.model.session.HdAccountSession;
import cn.youngkbt.hdsecurity.model.session.HdSession;
import cn.youngkbt.hdsecurity.model.session.HdTokenDevice;
import cn.youngkbt.hdsecurity.repository.HdSecurityRepository;
import cn.youngkbt.hdsecurity.repository.HdSecurityRepositoryKV;
import cn.youngkbt.hdsecurity.strategy.TokenGenerateStrategy;
import cn.youngkbt.hdsecurity.utils.HdStringUtil;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Hd Security Token 模块
 *
 * @author Tianke
 * @date 2024/11/26 21:55:21
 * @since 1.0.0
 */
public class HdTokenHelper {
    private final String accountType;

    public HdTokenHelper(String accountType) {
        this.accountType = accountType;
    }

    /**
     * 创建 UUID 式 Token
     *
     * @return Token
     */
    public static String createUuidToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * 创建简单 UUID 式 Token（不带 -）
     *
     * @return Token
     */
    public static String createSimpleUuidToken() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 创建 TLK 式 Token
     *
     * @return Token
     */
    public static String createTlkToken() {
        return createRandomToken(2) + "_" + createRandomToken(14) + "_" + createRandomToken(16) + "__";
    }

    /**
     * 创建 32 位随机 Token
     *
     * @return Token
     */
    public static String createRandom32Token() {
        return HdStringUtil.getRandomString(32);
    }

    /**
     * 创建 64 位随机 Token
     *
     * @return Token
     */
    public static String createRandom64Token() {
        return HdStringUtil.getRandomString(64);
    }

    /**
     * 创建 128 位随机 Token
     *
     * @return Token
     */
    public static String createRandom128Token() {
        return HdStringUtil.getRandomString(128);
    }

    /**
     * 创建指定长度的随机 Token
     *
     * @param length Token 长度
     * @return Token
     */
    public static String createRandomToken(int length) {
        return HdStringUtil.getRandomString(length);
    }

    /**
     * 创建 Token
     *
     * @param loginModel 登录参数
     * @return Token
     */
    public String createToken(HdLoginModel loginModel) {
        // 如果存在自定义 token，则直接返回
        if (HdStringUtil.hasText(loginModel.getToken())) {
            return loginModel.getToken();
        }

        HdSecurityConfig config = HdSecurityManager.getConfig(accountType);
        Object loginId = loginModel.getLoginId();
        // 如果配置了是否允许同一账号多地同时登录
        if (Boolean.TRUE.equals(config.getConcurrent()) && Boolean.TRUE.equals(config.getShare())) {
            // 获取上次登录的 Token
            String token = getLastTokenByLoginId(loginId, loginModel.getDevice());
            if (HdStringUtil.hasText(token)) {
                return token;
            }
        }

        // 如果不存在旧 Token，则创建新 Token
        return TokenGenerateStrategy.instance.generateUniqueElement.generate(
                "Token",
                // 最大尝试次数
                config.getMaxTryTimes(),
                // 创建 Token
                () -> createToken(loginId),
                // 验证 Token 唯一性，这里从持久层获取根据创建的 Token 获取登录 ID，获取成功代表有用户在用，则不唯一
                newToken -> getLoginIdByToken(newToken) == null,
                // 捕获异常
                e -> {
                    throw e;
                }
        );
    }

    /**
     * 使用策略创建 Token
     *
     * @param loginId 登录 ID
     * @return Token
     */
    public String createToken(Object loginId) {
        return TokenGenerateStrategy.instance.createToken.create(loginId, accountType);
    }

    // ---------- Token 允许的活跃时间相关操作方法 ----------

    /**
     * 获取 Token 允许的活跃时间（秒），如果指定 Token 允许的活跃时间不存在，则返回全局配置的 Token 允许的活跃时间（秒）
     *
     * @param token Token
     * @return 允许的活跃时间（秒）
     */
    public long getTokenActiveTimeOrGlobalConfig(String token) {
        Long tokenActiveTime = getTokenActiveTime(token);
        return null == tokenActiveTime ? HdSecurityConfigProvider.getHdSecurityConfig().getTokenActiveExpireTime() : tokenActiveTime;
    }

    /**
     * 获取 Token 允许的活跃时间（秒）
     *
     * @return 允许的活跃时间（秒）
     */
    public Long getTokenActiveTime() {
        return getTokenActiveTime(getWebToken());
    }

    /**
     * 获取 Token 允许的活跃时间（秒）
     *
     * @param token Token
     * @return 允许的活跃时间（秒）
     */
    public Long getTokenActiveTime(String token) {
        if (HdStringUtil.hasEmpty(token)) {
            return null;
        }

        // 如果全局未启用动态 activeTimeout 功能，则直接返回 null
        if (Boolean.FALSE.equals(HdSecurityManager.getConfig(accountType).getDynamicActiveExpireTime())) {
            return null;
        }

        // 获取活跃时间配置
        String activeTime = (String) HdSecurityManager.getRepository().query(RepositoryKeyHelper.getLastActiveKey(token, accountType));
        if (HdStringUtil.hasEmpty(activeTime)) {
            return null;
        }

        return Long.parseLong(activeTime.split(",")[1]);
    }

    /**
     * 添加 Token 活跃时间配置，格式：最后的活跃时间戳,允许的活跃时间（秒）
     *
     * @param token            Token
     * @param activeExpireTime 允许的活跃时间（秒），为 null 代表使用全局配置的 activeTimeout 值，虽然方法里没有设置，但是在获取的时候会被设置为全局配置的值，@See HdTokenHelper#getTokenActiveTimeOrGlobalConfig(String)
     * @param tokenExpireTime  Token 过期时间，为 null 代表使用全局配置的 tokenExpireTime 值
     */
    public void addTokenActiveTime(String token, Long activeExpireTime, Long tokenExpireTime) {
        HdSecurityConfig config = HdSecurityManager.getConfig(accountType);
        if (null == tokenExpireTime) {
            tokenExpireTime = config.getTokenExpireTime();
        }

        String key = RepositoryKeyHelper.getLastActiveKey(token, accountType);
        String value = System.currentTimeMillis() + "," + (Boolean.TRUE.equals(config.getDynamicActiveExpireTime()) && null != activeExpireTime ? activeExpireTime : "");

        HdSecurityManager.getRepository().add(key, value, tokenExpireTime);
    }

    /**
     * 删除 Token 活跃时间配置
     *
     * @param token Token
     */
    public void removeTokenActiveTime(String token) {
        HdSecurityManager.getRepository().remove(RepositoryKeyHelper.getLastActiveKey(token, accountType));
    }

    // ---------- Token 最后活跃时间相关操作方法 ----------

    /**
     * 获取 Token 的最后活跃时间
     *
     * @return 最后活跃时间
     */
    public Long getTokenLastActiveTime() {
        return getTokenLastActiveTime(getWebToken());
    }

    /**
     * 获取 Token 的最后活跃时间（13 位时间戳）
     *
     * @param token Token
     * @return 最后活跃时间（13 位时间戳）
     */
    public Long getTokenLastActiveTime(String token) {
        if (HdStringUtil.hasEmpty(token)) {
            return null;
        }

        // 获取最活跃时间
        String activeTime = (String) HdSecurityManager.getRepository().query(RepositoryKeyHelper.getLastActiveKey(token, accountType));
        if (HdStringUtil.hasEmpty(activeTime)) {
            return null;
        }

        return Long.parseLong(activeTime.split(",")[0]);
    }

    /**
     * 续签当前 Token 为当前时间戳，如果 Token 被冻结，则也会续签成功
     */
    public void updateTokenLastActiveTimeToNow() {
        updateTokenLastActiveTimeToNow(getWebToken());
    }

    /**
     * 续签当前 Token 为当前时间戳，如果 Token 被冻结，则也会续签成功
     *
     * @param token Token
     */
    public void updateTokenLastActiveTimeToNow(String token) {
        String key = RepositoryKeyHelper.getLastActiveKey(token, accountType);
        String value = System.currentTimeMillis() + "," + getTokenActiveTime(token);
        HdSecurityManager.getRepository().edit(key, value);
    }

    /**
     * 获取 Token 剩余的活跃时间，剩余活跃时间 = (当前时间戳 - 最后一次活跃时间戳) / 1000 - 允许的活跃时间（秒）
     *
     * @return 单位: 秒，返回 -1 代表永不冻结，null 代表 Token 已被冻结了或找不到 Token 剩余的活跃时间
     */
    public Long getTokenRemainActiveTime() {
        return getTokenRemainActiveTime(getWebToken());
    }

    /**
     * 获取 Token 剩余的活跃时间，剩余活跃时间 = (当前时间戳 - 最后一次活跃时间戳) / 1000 - 允许的活跃时间（秒）
     *
     * @param token Token
     * @return 单位: 秒，返回 -1 代表永不冻结，null 代表 Token 已被冻结了或找不到 Token 剩余的活跃时间
     */
    public Long getTokenRemainActiveTime(String token) {
        // 如果全局配置 Token 永不冻结, 则直接返回 null
        if (!HdSecurityConfigProvider.isUseActiveExpireTime()) {
            return null;
        }
        // 获取 Token 的最后活跃时间
        Long lastActiveTime = getTokenLastActiveTime(token);
        if (null == lastActiveTime) {
            return null;
        }
        // 计算已经活跃的时间（秒），格式：(当前时间戳 - 最后一次活跃时间戳) / 1000
        long activeTimeDiff = (System.currentTimeMillis() - lastActiveTime) / 1000;

        // 获取 Token 允许的活跃时间（秒）
        long activeTime = getTokenActiveTimeOrGlobalConfig(token);

        // 如果允许的活跃时间为 -1 ，则代表永不冻结，这里直接返回 -1
        if (activeTime == HdSecurityRepositoryKV.NEVER_EXPIRE) {
            return HdSecurityRepositoryKV.NEVER_EXPIRE;
        }

        // 计算剩余活跃时间（秒）
        long remainActiveTime = activeTime - activeTimeDiff;
        if (remainActiveTime <= 0) {
            return null;
        }

        return remainActiveTime;
    }


    /**
     * 检查指定 token 是否已被冻结，如果是则抛出异常
     *
     * @param token token
     */
    public void checkTokenActiveTime(String token) {
        // 如果全局配置 Token 永不冻结, 则直接返回，需要校验
        if (!HdSecurityConfigProvider.isUseActiveExpireTime()) {
            return;
        }
        // 获取这个 token 剩余的活跃时间
        Long tokenRemainActiveTime = getTokenRemainActiveTime(token);

        // 如果剩余活跃时间为 null，代表 Token 已被冻结或找不到 Token 剩余的活跃时间，则抛出异常
        if (null == tokenRemainActiveTime) {
            throw new HdSecurityTokenException("Token 已被冻结或找不到 Token 剩余的活跃时间").setCode(HdSecurityErrorCode.TOKEN_FREEZE);
        }
    }

    // ---------- Token 从持久层获取相关操作方法 ----------

    /**
     * 从持久层获取 Token，如果 Token 为空，则登录
     *
     * @param loginId 账号 ID
     * @return Token
     */
    public String getTokenOrLogin(Object loginId) {
        return getTokenOrLogin(loginId, DefaultConstant.DEFAULT_LOGIN_DEVICE);
    }

    /**
     * 从持久层获取 Token，如果 Token 为空，则登录
     *
     * @param loginId 账号 ID
     * @param device  设备
     * @return Token
     */
    public String getTokenOrLogin(Object loginId, String device) {
        String lastToken = getLastTokenByLoginId(loginId);
        if (HdStringUtil.hasText(lastToken)) {
            return lastToken;
        }
        return HdHelper.loginHelper(accountType).login(loginId, device);
    }

    /**
     * 根据账号从持久层获取注册的 Token
     *
     * @param loginId 账号
     * @return Token
     */
    public String getCacheToken(Object loginId) {
        return getLastTokenByLoginId(loginId);
    }

    /**
     * 根据账号获取注册的最后一个 Token
     *
     * @param loginId 账号
     * @return Token
     */
    public String getLastTokenByLoginId(Object loginId) {
        return getLastTokenByLoginId(loginId, DefaultConstant.DEFAULT_LOGIN_DEVICE);
    }

    /**
     * 根据账号和设备获取注册的最后一个 Token
     *
     * @param loginId 账号
     * @return Token
     */
    public String getLastTokenByLoginId(Object loginId, String device) {
        List<String> tokenList = getTokenListByLoginId(loginId, device);
        return tokenList.isEmpty() ? null : tokenList.get(tokenList.size() - 1);
    }

    /**
     * 根据账号获取所有与设备下注册的 Token 列表
     *
     * @param loginId 账号
     * @return Token 列表
     */
    public List<String> getTokenListByLoginId(Object loginId) {
        return getTokenListByLoginId(loginId, null);
    }

    /**
     * 根据账号和设备获取注册的 Token 列表
     *
     * @param loginId 账号
     * @param device  设备
     * @return Token 列表
     */
    public List<String> getTokenListByLoginId(Object loginId, String device) {
        HdSession session = HdHelper.sessionHelper(accountType).getAccountSessionByLoginId(loginId);
        if (null == session) {
            return Collections.emptyList();
        }

        // 获取指定设备下的 Token 列表
        return session.getTokenListByDevice(device);
    }

    // --------- 通过 Token 获取设备相关操作方法 ---------

    /**
     * 获取 Token 对应的设备
     *
     * @return 设备
     */
    public String getDevice() {
        return getDeviceByToken(getWebToken());
    }

    /**
     * 获取 Token 对应的设备
     *
     * @param token Token
     * @return 设备
     */
    public String getDeviceByToken(String token) {
        if (HdStringUtil.hasEmpty(token)) {
            return null;
        }

        // 检查 Token 是否存活
        if (null == getTokenRemainActiveTime(token)) {
            return null;
        }

        // 通过 Token 获取 LoginId
        Object loginId = getLoginIdByToken(token);
        if (null == loginId) {
            return null;
        }

        // 获取 Account Session
        HdSession session = HdHelper.sessionHelper(accountType).getAccountSessionByLoginId(loginId);
        if (null == session) {
            return null;
        }

        // 通过 Token 获取 TokenDevice
        HdTokenDevice tokenDevice = session.getTokenDeviceByToken(token);
        if (null == tokenDevice) {
            return null;
        }
        return tokenDevice.getDevice();
    }

    // --------- Token 和 LoginId 的映射关系相关操作方法 ---------

    /**
     * 根据 Token 获取 LoginId
     *
     * @return LoginId
     */
    public Object getLoginIdByToken() {
        return getLoginIdByToken(getWebToken());
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
        return HdSecurityManager.getRepository().query(RepositoryKeyHelper.getTokenLoginIdMappingKey(token, accountType));
    }

    /**
     * 添加 Token 和 LoginId 的映射关系
     *
     * @param token           Token
     * @param loginId         登录 ID
     * @param tokenExpireTime Token 过期时间
     */
    public void addTokenAndLoginIdMapping(String token, Object loginId, Long tokenExpireTime) {
        HdSecurityManager.getRepository().add(RepositoryKeyHelper.getTokenLoginIdMappingKey(token, accountType), loginId, tokenExpireTime);
    }

    /**
     * 编辑 Token 和 LoginId 的映射关系
     *
     * @param token   Token
     * @param loginId 登录 ID
     */
    public void editTokenAndLoginIdMapping(String token, Object loginId) {
        HdSecurityManager.getRepository().edit(RepositoryKeyHelper.getTokenLoginIdMappingKey(token, accountType), loginId);
    }

    /**
     * 删除 Token 和 LoginId 的映射关系
     *
     * @param token Token
     */
    public void removeTokenAndLoginIdMapping(String token) {
        HdSecurityManager.getRepository().remove(RepositoryKeyHelper.getTokenLoginIdMappingKey(token, accountType));
    }

    // ---------- Token 和 LoginId 的映射关系的 ExpireTime 获取操作方法 ---------

    /**
     * 通过 Token 获取 Token 和 LoginId 映射关系的过期时间（单位: 秒，返回 -1 代表永久有效，-2 代表没有这个值）
     *
     * @return Token 和 LoginId 映射关系的过期时间（单位: 秒，返回 -1 代表永久有效，-2 代表没有这个值）
     */
    public long getTokenAndLoginIdExpireTime() {
        return getTokenAndLoginIdExpireTime(getWebToken());
    }

    /**
     * 通过 Token 获取 Token 和 LoginId 映射关系的过期时间（单位: 秒，返回 -1 代表永久有效，-2 代表没有这个值）
     *
     * @param token Token
     * @return Token 和 LoginId 映射关系的过期时间（单位: 秒，返回 -1 代表永久有效，-2 代表没有这个值）
     */
    public long getTokenAndLoginIdExpireTime(String token) {
        return HdSecurityManager.getRepository().getExpireTime(RepositoryKeyHelper.getTokenLoginIdMappingKey(token, accountType));
    }

    /**
     * 通过 LoginId 获取 Token 和 LoginId 映射关系的过期时间（单位: 秒，返回 -1 代表永久有效，-2 代表没有这个值）
     *
     * @param loginId 登录 ID
     * @return Token 和 LoginId 映射关系的过期时间（单位: 秒，返回 -1 代表永久有效，-2 代表没有这个值）
     */
    public long getTokenAndLoginIdExpireTimeByLoginId(Object loginId) {
        String token = getLastTokenByLoginId(loginId);
        return getTokenAndLoginIdExpireTime(token);
    }

    // --------- Token 在 Web 读取和写入相关操作方法 ---------

    /**
     * 写入 Token 到 Web
     *
     * @param token Token
     */
    public void writeTokenToWeb(String token) {
        writeTokenToWeb(token, HdLoginModelOperator.build());
    }


    /**
     * 写入 Token 到 Web
     *
     * @param token           Token
     * @param tokenExpireTime Token 过期时间，如果开启了 Cookie，也是 Cookie 过期时间
     */
    public void writeTokenToWeb(String token, long tokenExpireTime) {
        writeTokenToWeb(token, HdLoginModelOperator.create().setTokenExpireTime(tokenExpireTime));
    }

    /**
     * 写入 Token 到 Web
     *
     * @param token      Token
     * @param loginModel 登录参数
     */
    public void writeTokenToWeb(String token, HdLoginModel loginModel) {
        if (HdStringUtil.hasEmpty(token)) {
            return;
        }
        // 存储到 Storage
        writeTokenToStorage(token);

        // 存储到 Header
        if (Boolean.TRUE.equals(loginModel.getWriteHeader())) {
            writeTokenToHeader(token);
        }

        // 存储到 Cookie
        writeTokenToCookie(token, loginModel.getCookieExpireTime());
    }

    /**
     * 写入 Token 到 Storage
     *
     * @param token Token
     */
    public void writeTokenToStorage(String token) {
        HdSecurityConfig config = HdSecurityManager.getConfig(accountType);
        String tokenPrefix = config.getTokenPrefix();
        // 将 Token 写入到 Storage
        HdSecurityManager.getContext().getStorage().set(DefaultConstant.CREATED_TOKEN, token);
        // 将有前缀的 Token 写入到 Storage
        HdSecurityManager.getContext().getStorage().set(DefaultConstant.CREATED_TOKEN_PREFIX, (HdStringUtil.hasText(tokenPrefix) ? tokenPrefix : " ") + token);
    }

    /**
     * 写入 Token 到 Header
     *
     * @param token Token
     */
    public void writeTokenToHeader(String token) {
        HdSecurityConfig config = HdSecurityManager.getConfig(accountType);

        if (Boolean.TRUE.equals(config.getWriteHeader())) {
            String securityPrefixKey = config.getSecurityPrefixKey();
            HdSecurityManager.getContext().getResponse().addHeader(securityPrefixKey, token);
        }
    }

    /**
     * 写入 Token 到 Cookie
     *
     * @param token            Token
     * @param cookieExpireTime Cookie 过期时间
     */
    public void writeTokenToCookie(String token, int cookieExpireTime) {
        HdSecurityConfig config = HdSecurityManager.getConfig(accountType);

        if (Boolean.TRUE.equals(config.getReadCookie())) {
            HdCookieConfig cookieConfig = config.getCookie();
            HdCookie cookie = new HdCookie()
                    .setName(config.getSecurityPrefixKey())
                    .setValue(token)
                    .setMaxAge(cookieExpireTime)
                    .setDomain(cookieConfig.getDomain())
                    .setPath(cookieConfig.getPath())
                    .setHttpOnly(cookieConfig.getHttpOnly())
                    .setSecure(cookieConfig.getSecure())
                    .setSameSite(cookieConfig.getSameSite());

            HdSecurityManager.getContext().getResponse().addCookie(cookie);
        }
    }

    /**
     * 从 Web 获取 Token，不做任何检查
     *
     * @return Token
     */
    public String getWebToken() {
        return getWebToken(false, false);
    }

    /**
     * 检查 Token 如果未按照指定前缀提交，则抛出异常，否则返回 Token
     *
     * @return Token
     */
    public String checkWebTokenPrefixThenGet() {
        return getWebToken(false, true);
    }

    /**
     * 检查 Token 为空，则抛出异常，否则返回 Token
     *
     * @return Token
     */
    public String checkWebTokenNonNullThenGet() {
        return getWebToken(true, true);
    }

    /**
     * 从 Web 获取 Token
     *
     * @param tokeNonNull     是否必须存在
     * @param prefixMustMatch 是否必须以指定的前缀开头
     * @return Token
     */
    public String getWebToken(boolean tokeNonNull, boolean prefixMustMatch) {
        String token = getTokenFromWeb();

        if (HdStringUtil.hasEmpty(token)) {
            if (tokeNonNull) {
                throw new HdSecurityTokenException("未能读取到有效 Token").setCode(HdSecurityErrorCode.TOKEN_IS_NULL);
            }
            return null;
        }

        String tokenPrefix = HdSecurityManager.getConfig(accountType).getTokenPrefix();
        // 如果有前缀，则需要处理
        if (HdStringUtil.hasText(tokenPrefix)) {
            if (!token.startsWith(tokenPrefix)) {
                if (prefixMustMatch) {
                    throw new HdSecurityTokenException("未按照指定前缀提交 Token").setCode(HdSecurityErrorCode.TOKEN_NO_MATCH_PREFIX);
                }
                return null;
            }
            // 此时匹配到前缀，则去掉前缀
            return token.substring(tokenPrefix.length());
        }
        // 如果没有设置前缀，则直接返回
        return token;
    }


    /**
     * 从 Web 获取 Token
     *
     * @return Token
     */
    public String getTokenFromWeb() {
        HdSecurityConfig config = HdSecurityManager.getConfig(accountType);
        HdSecurityContext context = HdSecurityManager.getContext();
        String securityPrefixKey = HdSecurityManager.getConfig(accountType).getSecurityPrefixKey();
        // 先尝试从 Storage 中获取
        String token = String.valueOf(context.getStorage().get(DefaultConstant.CREATED_TOKEN_PREFIX));

        // 再尝试从 Body 中获取，如 URL ? 后面的参数、Form 表单等
        if (HdStringUtil.hasEmpty(token) && Boolean.TRUE.equals(config.getReadBody())) {
            token = context.getRequest().getParam(securityPrefixKey);
        }

        // 再尝试从 Header 中获取
        if (HdStringUtil.hasEmpty(token) && Boolean.TRUE.equals(config.getReadBody())) {
            token = context.getRequest().getHeader(securityPrefixKey);
        }
        // 再尝试从 Cookie 中获取
        if (HdStringUtil.hasEmpty(token) && Boolean.TRUE.equals(config.getReadCookie())) {
            token = context.getRequest().getCookieValue(securityPrefixKey);
        }

        return token;
    }

    // --------- Token 续期相关操作方法 ----------

    /**
     * 对 webToken 进行续期
     *
     * @param expireTime 要续期的时间 (单位: 秒，-1 代表要续为永久有效)
     */
    public void renewTokenExpireTime(long expireTime) {
        String webToken = getWebToken();
        renewTokenExpireTime(webToken, expireTime);
    }

    /**
     * 对 webToken 进行续期
     *
     * @param token      Token
     * @param expireTime 要续期的时间 (单位: 秒，-1 代表要续为永久有效)
     */
    public void renewTokenExpireTime(String token, long expireTime) {
        // 如果 Token 为空，则直接返回
        if (HdStringUtil.hasEmpty(token)) {
            return;
        }
        // 如果 Token 映射的 LoginId 不存在或已经过期，则直接返回
        Object loginId = getLoginIdByToken(token);
        if (null == loginId) {
            return;
        }

        // 发布续期前置事件
        HdSecurityEventCenter.publishBeforeRenewExpireTime(token, loginId, expireTime);

        HdSecurityRepository repository = HdSecurityManager.getRepository();
        // Token 和 LoginId 的映射关系续期
        repository.updateExpireTime(RepositoryKeyHelper.getTokenLoginIdMappingKey(token, accountType), expireTime);

        // Token Session 续期
        repository.updateSessionTimeout(RepositoryKeyHelper.getTokenSessionKey(token, accountType), expireTime);

        // Token 对应的 Account Session 续期
        HdAccountSession accountSession = HdHelper.sessionHelper(accountType).getAccountSessionByLoginId(loginId);
        if (null != accountSession) {
            accountSession.updateExpireTimeWhenCondition(expireTime, true);
        }

        // Token 最后活跃时间续期
        if (HdSecurityConfigProvider.isUseActiveExpireTime()) {
            repository.updateExpireTime(RepositoryKeyHelper.getLastActiveKey(token, accountType), expireTime);
        }

        // 发布续期后置事件：某某 token 被续期了
        HdSecurityEventCenter.publishAfterRenewExpireTime(token, loginId, expireTime);

        // 持久层续期后，Cookie 也需要续期
        if (Boolean.TRUE.equals(HdSecurityManager.getConfig(accountType).getReadCookie())) {
            if (expireTime == HdSecurityRepositoryKV.NEVER_EXPIRE || expireTime > Integer.MAX_VALUE) {
                expireTime = Integer.MAX_VALUE;
            }
            writeTokenToCookie(token, Math.toIntExact(expireTime));
        }
    }
}
