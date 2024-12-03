package cn.youngkbt.hdsecurity.context.model;

import cn.youngkbt.hdsecurity.model.cookie.HdCookie;

/**
 * @author Tianke
 * @date 2024/11/30 15:24:56
 * @since 1.0.0
 */
public interface HdSecurityResponse {
    /**
     * 获取底层被包装的源对象
     *
     * @return 被包装的源对象
     */
    Object getSource();

    /**
     * 在响应头里添加一个值
     *
     * @param name  名字
     * @param value 值
     * @return 对象自身
     */
    HdSecurityResponse addHeader(String name, String value);

    /**
     * 重定向
     *
     * @param url 重定向地址
     * @return 任意值
     */
    Object redirect(String url);

    /**
     * 删除指定 Cookie
     *
     * @param name Cookie名称
     */
    default void deleteCookie(String name) {
        addCookie(name, null, null, null, 0);
    }

    /**
     * 写入指定 Cookie
     *
     * @param name    Cookie名称
     * @param value   Cookie值
     * @param path    Cookie路径
     * @param domain  Cookie的作用域
     * @param timeout 过期时间 （秒）
     */
    default void addCookie(String name, String value, String path, String domain, int timeout) {
        this.addCookie(new HdCookie().setName(name).setValue(value).setPath(path).setDomain(domain).setMaxAge(timeout));
    }

    /**
     * 写入指定 Cookie
     * @param cookie Cookie
     */
    default void addCookie(HdCookie cookie) {
        addHeader(HdCookie.HEADER_NAME, cookie.buildCookieStr());
    }
}
