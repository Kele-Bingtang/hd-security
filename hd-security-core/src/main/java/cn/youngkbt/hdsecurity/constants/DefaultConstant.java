package cn.youngkbt.hdsecurity.constants;

/**
 * @author Tianke
 * @date 2024/11/27 22:53:04
 * @since 1.0.0
 */
public interface DefaultConstant {
    String DEFAULT_LOGIN_DEVICE = "default-device";
    String DEFAULT_ACCOUNT_TYPE = "default-account";
    String CREATED_TOKEN = "CREATED_TOKEN";
    String CREATED_TOKEN_PREFIX = "CREATED_TOKEN_PREFIX";
    
    // ---------- 账号封禁 ----------
    /**
     * 默认封禁领域
     */
    String DEFAULT_BAN_REALM = "default-ban-realm";
    /**
     * 默认封禁等级
     */
    int DEFAULT_BAN_LEVEL = 1;
    /**
     * 封禁最小等级
     */
    int MIN_BAN_LIMIT_LEVEL = 1;
    /**
     * 未封禁标记
     */
    int NOT_BAN_TAG = -2;
}
