package cn.youngkbt.hdsecurity.authorize;

/**
 * 认证模式枚举
 *
 * @author Tianke
 * @date 2024/12/13 00:16:05
 * @since 1.0.0
 */
public enum AuthorizeType {

    /**
     * 角色
     */
    ROLE("role"),
    /**
     * 权限
     */
    PERMISSION("permission"),

    ;

    private final String type;

    AuthorizeType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
