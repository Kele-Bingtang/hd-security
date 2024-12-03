package cn.youngkbt.hdsecurity.hd;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.config.HdCookieConfig;
import cn.youngkbt.hdsecurity.config.HdSecurityConfig;
import cn.youngkbt.hdsecurity.context.HdSecurityContext;
import cn.youngkbt.hdsecurity.error.HdSecurityErrorCode;
import cn.youngkbt.hdsecurity.exception.HdSecurityTokenException;
import cn.youngkbt.hdsecurity.model.cookie.HdCookie;
import cn.youngkbt.hdsecurity.model.login.HdLoginModel;
import cn.youngkbt.hdsecurity.model.session.HdSession;
import cn.youngkbt.hdsecurity.strategy.TokenGenerateStrategy;
import cn.youngkbt.hdsecurity.utils.HdStringUtil;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Token 工具类
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

        HdSecurityConfig config = HdSecurityManager.getConfig();
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

    // ---------- Token 最后活跃时间相关操作方法 ----------

    /**
     * 添加 Token 的最后活跃时间
     *
     * @param token            Token
     * @param activeExpireTime 最后活跃时间
     * @param tokenExpireTime  Token 过期时间
     */
    public void addTokenActiveTime(String token, Long activeExpireTime, Long tokenExpireTime) {
        HdSecurityConfig config = HdSecurityManager.getConfig();
        if (null == tokenExpireTime) {
            tokenExpireTime = config.getTokenExpireTime();
        }

        String key = RepositoryKeyHelper.getLastActiveKey(token, accountType);
        String value = System.currentTimeMillis() + "," + (Boolean.TRUE.equals(config.getDynamicActiveTimeout()) && null != activeExpireTime ? activeExpireTime : "");

        HdSecurityManager.getRepository().add(key, value, tokenExpireTime);
    }

    /**
     * 删除 Token 的最后活跃时间
     *
     * @param token Token
     */
    public void removeTokenActiveTime(String token) {
        HdSecurityManager.getRepository().remove(RepositoryKeyHelper.getLastActiveKey(token, accountType));
    }

    // ---------- Token 从持久层获取相关操作方法 ----------

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

    // --------- Token 和 LoginId 的映射关系相关操作方法 ---------

    /**
     * 根据 Token 获取 LoginId
     *
     * @param token Token
     * @return LoginId
     */
    public String getLoginIdByToken(String token) {
        return (String) HdSecurityManager.getRepository().query(RepositoryKeyHelper.getTokenLoginIdMappingKey(token, accountType));
    }

    /**
     * 添加 Token 和 LoginId 的映射关系
     *
     * @param loginId         登录 ID
     * @param token           Token
     * @param tokenExpireTime Token 过期时间
     */
    public void addTokenAndLoginIdMapping(Object loginId, String token, Long tokenExpireTime) {
        HdSecurityManager.getRepository().add(RepositoryKeyHelper.getTokenLoginIdMappingKey(loginId, accountType), token, tokenExpireTime);
    }

    /**
     * 删除 Token 和 LoginId 的映射关系
     *
     * @param token Token
     */
    public void removeTokenAndLoginIdMapping(String token) {
        HdSecurityManager.getRepository().remove(RepositoryKeyHelper.getTokenLoginIdMappingKey(token, accountType));
    }

    // --------- Token 在 Web 读取和写入相关操作方法 ---------

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
        HdSecurityConfig config = HdSecurityManager.getConfig();
        String tokenPrefix = config.getTokenPrefix();
        HdSecurityManager.getContext().getStorage().set("storageToken", HdStringUtil.hasText(tokenPrefix) ? tokenPrefix : " " + token);
    }

    /**
     * 写入 Token 到 Header
     *
     * @param token Token
     */
    public void writeTokenToHeader(String token) {
        HdSecurityConfig config = HdSecurityManager.getConfig();

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
        HdSecurityConfig config = HdSecurityManager.getConfig();

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
     * 获取 Token，不做任何检查
     *
     * @return Token
     */
    public String getToken() {
        return getToken(false, false);
    }

    /**
     * 检查 Token 如果未按照指定前缀提交，则抛出异常，否则返回 Token
     *
     * @return Token
     */
    public String checkTokenPrefixThenGet() {
        return getToken(false, true);
    }

    /**
     * 检查 Token 为空，则抛出异常，否则返回 Token
     *
     * @return Token
     */
    public String checkTokenNonNullThenGet() {
        return getToken(true, true);
    }

    /**
     * 获取 Token
     *
     * @param tokeNonNull     是否必须存在
     * @param prefixMustMatch 是否必须以指定的前缀开头
     * @return Token
     */
    public String getToken(boolean tokeNonNull, boolean prefixMustMatch) {
        String token = getTokenFromWeb();

        if (HdStringUtil.hasEmpty(token)) {
            if (tokeNonNull) {
                throw new HdSecurityTokenException("未能读取到有效 Token").setCode(HdSecurityErrorCode.TOKEN_IS_NULL);
            }
            return null;
        }

        String tokenPrefix = HdSecurityManager.getConfig().getTokenPrefix();
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
        HdSecurityConfig config = HdSecurityManager.getConfig();
        HdSecurityContext context = HdSecurityManager.getContext();
        String securityPrefixKey = HdSecurityManager.getConfig().getSecurityPrefixKey();
        // 先尝试从 Storage 中获取
        String token = String.valueOf(context.getStorage().get("storageToken"));

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
}
