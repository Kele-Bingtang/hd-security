package cn.youngkbt.hdsecurity.hd;

import cn.youngkbt.hdsecurity.HdSecurityManager;

/**
 * @author Tianke
 * @date 2024/11/28 01:44:37
 * @since 1.0.0
 */
public class RepositoryKeyHelper {

    private RepositoryKeyHelper() {
    }

    public static String getAccountSessionKey(Object loginId, String accountType) {
        return HdSecurityManager.getConfig().getSecurityPrefixKey() + ":" + accountType + ":accountSession:" + loginId;
    }

    public static String getTokenSessionKey(Object loginId, String accountType) {
        return HdSecurityManager.getConfig().getSecurityPrefixKey() + ":" + accountType + ":tokenSession:" + loginId;
    }

    public static String getTokenLoginIdMappingKey(Object loginId, String accountType) {
        return HdSecurityManager.getConfig().getSecurityPrefixKey() + ":" + accountType + ":tokenLoginMapping:" + loginId;
    }

    public static String getLastActiveKey(String token, String accountType) {
        return HdSecurityManager.getConfig().getSecurityPrefixKey() + ":" + accountType + ":lastActive" + token;
    }

    public static String getApplicationKey(String key) {
        return HdSecurityManager.getConfig().getSecurityPrefixKey() + ":var:" + key;
    }
}
