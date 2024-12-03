package cn.youngkbt.hdsecurity.model.cookie;

import cn.youngkbt.hdsecurity.utils.HdStringUtil;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author Tianke
 * @date 2024/11/26 23:45:57
 * @since 1.0.0
 */
public class HdCookie {
    /**
     * 写入响应头时使用的 key
     */
    public static final String HEADER_NAME = "Set-Cookie";

    /**
     * 名称
     */
    private String name;

    /**
     * 值
     */
    private String value;

    /**
     * 有效时长 （单位：秒），-1 代表为临时 Cookie 浏览器关闭后自动删除
     */
    private int maxAge = -1;

    /**
     * 域
     */
    private String domain;

    /**
     * 路径
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

    public String getName() {
        return name;
    }

    public HdCookie setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public HdCookie setValue(String value) {
        this.value = value;
        return this;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public HdCookie setMaxAge(int maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    public String getDomain() {
        return domain;
    }

    public HdCookie setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public String getPath() {
        return path;
    }

    public HdCookie setPath(String path) {
        this.path = path;
        return this;
    }

    public Boolean getSecure() {
        return secure;
    }

    public HdCookie setSecure(Boolean secure) {
        this.secure = secure;
        return this;
    }

    public Boolean getHttpOnly() {
        return httpOnly;
    }

    public HdCookie setHttpOnly(Boolean httpOnly) {
        this.httpOnly = httpOnly;
        return this;
    }

    public String getSameSite() {
        return sameSite;
    }

    public HdCookie setSameSite(String sameSite) {
        this.sameSite = sameSite;
        return this;
    }

    /**
     * 构建 cookie 字符串的方法
     * @return cookie
     */
    public String buildCookieStr() {
        StringBuilder cookieStringBuilder = new StringBuilder();

        cookieStringBuilder.append(name).append("=").append(value);

        appendIfPresent(cookieStringBuilder, "Domain", domain);
        appendIfPresent(cookieStringBuilder, "Path", path);
        appendIfPresent(cookieStringBuilder, "Max-Age", maxAge >= 0 ? String.valueOf(maxAge) : null);
        appendIfPresent(cookieStringBuilder, "Expires", calculateExpires(maxAge));
        appendIfTrue(cookieStringBuilder, "Secure", secure);
        appendIfTrue(cookieStringBuilder, "HttpOnly", httpOnly);
        appendIfPresent(cookieStringBuilder, "SameSite", sameSite);

        return cookieStringBuilder.toString();
    }

    /**
     * 判断是否需要添加到 StringBuilder
     *
     * @param sb    构建 cookie 字符串
     * @param key   key
     * @param value value
     */
    private void appendIfPresent(StringBuilder sb, String key, String value) {
        if (HdStringUtil.hasText(value)) {
            sb.append("; ").append(key).append("=").append(value);
        }
    }

    /**
     * 判断是否需要添加到 StringBuilder
     *
     * @param sb        构建 cookie 字符串
     * @param key       key
     * @param condition boolean 条件
     */
    private void appendIfTrue(StringBuilder sb, String key, boolean condition) {
        if (condition) {
            sb.append("; ").append(key);
        }
    }

    /**
     * 计算过期时间
     *
     * @param maxAge 有效时长（单位：秒）
     * @return String
     */
    private String calculateExpires(int maxAge) {
        if (maxAge < 0) {
            return null;
        }
        if (maxAge == 0) {
            return Instant.EPOCH.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.RFC_1123_DATE_TIME);
        }
        return OffsetDateTime.now().plusSeconds(maxAge).format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }
}
