package cn.youngkbt.hdsecurity.hd;

import cn.youngkbt.hdsecurity.HdSecurityManager;

/**
 * Hd Security 持久层 Key 模块
 *
 * @author Tianke
 * @date 2024/11/28 01:44:37
 * @since 1.0.0
 */
public class RepositoryKeyHelper {

    private RepositoryKeyHelper() {
    }

    /**
     * 获取 AccountSession 的 Key
     *
     * @param loginId     登录ID
     * @param accountType 账号类型
     * @return 账号SessionKey
     */
    public static String getAccountSessionKey(Object loginId, String accountType) {
        return HdSecurityManager.getConfig(accountType).getSecurityPrefixKey() + ":" + accountType + ":accountSession:" + loginId;
    }

    /**
     * 获取 TokenSession 的 Key
     *
     * @param token       Token
     * @param accountType 账号类型
     * @return TokenSessionKey
     */
    public static String getTokenSessionKey(String token, String accountType) {
        return HdSecurityManager.getConfig(accountType).getSecurityPrefixKey() + ":" + accountType + ":tokenSession:" + token;
    }

    /**
     * 获取 Token 登录 ID 映射的 Key
     *
     * @param token       Token
     * @param accountType 账号类型
     * @return Token 登录ID映射的 Key
     */
    public static String getTokenLoginIdMappingKey(String token, String accountType) {
        return HdSecurityManager.getConfig(accountType).getSecurityPrefixKey() + ":" + accountType + ":tokenLoginMapping:" + token;
    }

    /**
     * 获取 Token 最后活跃时间的 Key
     *
     * @param token       Token
     * @param accountType 账号类型
     * @return Token 最后活跃时间
     */
    public static String getLastActiveKey(String token, String accountType) {
        return HdSecurityManager.getConfig(accountType).getSecurityPrefixKey() + ":" + accountType + ":lastActive:" + token;
    }

    /**
     * 获取全局应用的 Key
     *
     * @param key 应用变量的 Key
     * @return 应用变量的 Key
     */
    public static String getApplicationKey(String key) {
        return HdSecurityManager.getConfig().getSecurityPrefixKey() + ":var:" + key;
    }

    /**
     * 获取禁用账号的 Key
     *
     * @param accountType 账号类型
     * @param loginId     登录ID
     * @param realm       域
     * @return 禁用账号的 Key
     */
    public static String getDisableAccountKey(String accountType, Object loginId, String realm) {
        return HdSecurityManager.getConfig(accountType).getSecurityPrefixKey() + ":" + accountType + ":disable:" + realm + ":" + loginId;
    }

    /**
     * 获取切换账号的 Key
     *
     * @param accountType 账号类型
     * @return 切换账号的 Key
     */
    public static String getSwitchLoginIdKey(String accountType) {
        return HdSecurityManager.getConfig().getSecurityPrefixKey() + ":" + accountType + ":var:switch";
    }

    /**
     * 获取二次认证的 Key
     *
     * @param accountType 账号类型
     * @param webToken    Web Token
     * @param realm       域
     * @return 二次认证的 Key
     */
    public static String getSecondAuthKey(String accountType, String webToken, String realm) {
        return HdSecurityManager.getConfig(accountType).getSecurityPrefixKey() + ":" + accountType + ":secondAuth:" + realm + ":" + webToken;
    }

    /**
     * 获取临时 Token 的 Key
     *
     * @param accountType 账号类型
     * @param realm       域
     * @param value       值
     * @return 临时 Token 的 Key
     */
    public static String getTempTokenKey(String accountType, String realm, Object value) {
        return HdSecurityManager.getConfig(accountType).getSecurityPrefixKey() + ":" + accountType + ":tempToken:" + realm + ":" + value;
    }

    /**
     * 获取同源 Token 的 Key
     *
     * @return 同源 Token 的 Key
     */
    public static String getSameOriginTokenKey() {
        return HdSecurityManager.getConfig().getSecurityPrefixKey() + ":var:sameOriginToken";
    }

    /**
     * 获取同源二次 Token 的 Key
     *
     * @return 同源 Token 的 Key
     */
    public static String getSameOriginSecondTokenKey() {
        return HdSecurityManager.getConfig().getSecurityPrefixKey() + ":var:sameOriginSecondToken";
    }
}
