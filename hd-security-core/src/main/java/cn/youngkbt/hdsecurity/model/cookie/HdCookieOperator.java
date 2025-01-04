package cn.youngkbt.hdsecurity.model.cookie;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.config.HdCookieConfig;

/**
 * @author Tianke
 * @date 2025/1/4 01:41:36
 * @since 1.0.0
 */
public class HdCookieOperator {

    private HdCookieOperator() {
    }

    public static HdCookie createCookie(String cookieName, String value, int cookieExpireTime, HdCookieConfig cookieConfig) {
        return new HdCookie()
                .setName(cookieName)
                .setValue(value)
                .setMaxAge(cookieExpireTime)
                .setDomain(cookieConfig.getDomain())
                .setPath(cookieConfig.getPath())
                .setHttpOnly(cookieConfig.getHttpOnly())
                .setSecure(cookieConfig.getSecure())
                .setSameSite(cookieConfig.getSameSite());
    }

    public static void removeCookie(String cookieName, HdCookieConfig cookieConfig) {
        HdCookie cookie = new HdCookie()
                .setName(cookieName)
                .setValue(null)
                // 有效期指定为 0，覆盖浏览器缓存
                .setMaxAge(0)
                .setDomain(cookieConfig.getDomain())
                .setPath(cookieConfig.getPath())
                .setHttpOnly(cookieConfig.getHttpOnly())
                .setSecure(cookieConfig.getSecure())
                .setSameSite(cookieConfig.getSameSite());

        HdSecurityManager.getContext().getResponse().addCookie(cookie);
    }

    public static void removeCookie(String cookieName) {
        HdSecurityManager.getContext().getResponse().deleteCookie(cookieName);
    }
}
