package cn.youngkbt.hdsecurity.constants;

/**
 * Hd Security 常量
 *
 * @author Tianke
 * @date 2024/11/27 22:53:04
 * @since 1.0.0
 */
public interface DefaultConstant {
    /**
     * 默认设备
     */
    String DEFAULT_LOGIN_DEVICE = "default-device";
    /**
     * 默认账号类型
     */
    String DEFAULT_ACCOUNT_TYPE = "default-account";
    /**
     * 获取和存储刚创建 Token 的标识
     */
    String CREATED_TOKEN = "CREATED_TOKEN";
    /**
     * 获取和存储刚创建且带有前置 Token 的标识
     */
    String CREATED_TOKEN_PREFIX = "CREATED_TOKEN_PREFIX";
    /**
     * 获取和存储刚创建且带有前置 Token 的标识
     */
    String TOKEN_ACTIVE_TIME_CHECK = "TOKEN_ACTIVE_TIME_CHECK";
    /**
     * 默认临时 Token 前缀
     */
    String DEFAULT_TEMP_TOKEN_REALM = "default-temp-token-realm";

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

    // ---------- 二次认证 ----------
    /**
     * 在进行 Token 二级认证时，往持久层写入的 value 值
     */
    String SECOND_AUTH_OPEN_TAG = "SECOND_AUTH_OPEN_TAG";
    /**
     * 二次认证默认领域
     */
    String DEFAULT_SECOND_AUTH_REALM = "default-second-auth-realm";

    /**
     * 同源 Token 标识
     */
    String SAME_ORIGIN_TOKEN_TAG = "SAME_ORIGIN_TOKEN_TAG";

}
