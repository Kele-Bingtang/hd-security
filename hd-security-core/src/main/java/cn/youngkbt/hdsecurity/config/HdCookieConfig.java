package cn.youngkbt.hdsecurity.config;

/**
 * Cookie 配置类
 *
 * @author Tianke
 * @date 2024/11/26 23:41:22
 * @since 1.0.0
 */
public class HdCookieConfig {
    /**
     * 作用域
     * <p>
     * 写入 Cookie 时显式指定的作用域, 常用于单点登录二级域名共享 Cookie 的场景,一般情况下你不需要设置此值，因为浏览器默认会把 Cookie 写到当前域名下
     * </p>
     */
    private String domain;

    /**
     * 路径 （一般只有当在一个域名下部署多个项目时才会用到此值）
     */
    private String path;

    /**
     * 是否只在 https 协议下有效
     */
    private Boolean secure = false;

    /**
     * 是否禁止 js 操作 Cookie
     */
    private Boolean httpOnly = false;

    /**
     * 第三方限制级别（Strict=完全禁止，Lax=部分允许，None=不限制）
     */
    private String sameSite;

    public String getDomain() {
        return domain;
    }

    public HdCookieConfig setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public String getPath() {
        return path;
    }

    public HdCookieConfig setPath(String path) {
        this.path = path;
        return this;
    }

    public Boolean getSecure() {
        return secure;
    }

    public HdCookieConfig setSecure(Boolean secure) {
        this.secure = secure;
        return this;
    }

    public Boolean getHttpOnly() {
        return httpOnly;
    }

    public HdCookieConfig setHttpOnly(Boolean httpOnly) {
        this.httpOnly = httpOnly;
        return this;
    }

    public String getSameSite() {
        return sameSite;
    }

    public HdCookieConfig setSameSite(String sameSite) {
        this.sameSite = sameSite;
        return this;
    }

}
