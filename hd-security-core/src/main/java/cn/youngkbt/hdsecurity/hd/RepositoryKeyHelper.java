package cn.youngkbt.hdsecurity.hd;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.constants.DefaultConstant;

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

    public static String getAccountSessionKey(Object loginId, String accountType) {
        return HdSecurityManager.getConfig(accountType).getSecurityPrefixKey() + ":" + accountType + ":accountSession:" + loginId;
    }

    public static String getTokenSessionKey(String token, String accountType) {
        return HdSecurityManager.getConfig(accountType).getSecurityPrefixKey() + ":" + accountType + ":tokenSession:" + token;
    }

    public static String getTokenLoginIdMappingKey(String token, String accountType) {
        return HdSecurityManager.getConfig(accountType).getSecurityPrefixKey() + ":" + accountType + ":tokenLoginMapping:" + token;
    }

    public static String getLastActiveKey(String token, String accountType) {
        return HdSecurityManager.getConfig(accountType).getSecurityPrefixKey() + ":" + accountType + ":lastActive:" + token;
    }

    public static String getApplicationKey(String key) {
        return HdSecurityManager.getConfig().getSecurityPrefixKey() + ":var:" + key;
    }

    public static String getDisableAccountKey(String accountType, Object loginId, String realm) {
        return HdSecurityManager.getConfig(accountType).getSecurityPrefixKey() + ":" + accountType + ":disable:" + realm + ":" + loginId;
    }

    public static String getSwitchLoginIdKey(String accountType) {
        return DefaultConstant.SWITCH_TO_SAVE_KEY + "accountType";
    }

    public static String getSecondAuthKey(String accountType, String webToken, String realm) {
        return HdSecurityManager.getConfig(accountType).getSecurityPrefixKey() + ":" + accountType + ":secondAuth:" + realm + ":" + webToken;
    }
    
    public static String getTempTokenKey(String accountType, String realm, Object value) {
        return HdSecurityManager.getConfig(accountType).getSecurityPrefixKey() + ":" + accountType + ":tempToken:" + realm + ":" + value;
    }
}
