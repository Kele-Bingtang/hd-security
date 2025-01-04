package cn.youngkbt.hdsecurity.context.model;

import cn.youngkbt.hdsecurity.utils.HdStringUtil;

import java.util.Map;

/**
 * Hd Security Request 请求对象包装类
 * @author Tianke
 * @date 2024/11/30 15:24:45
 * @since 1.0.0
 */
public interface HdSecurityRequest {
    /**
     * 获取底层被包装的源对象
     *
     * @return 被包装的源对象
     */
    Object getSource();

    /**
     * 在请求体里获取一个参数值
     *
     * @param name 键
     * @return 值
     */
    String getParam(String name);

    /**
     * 获取请求体提交的所有参数
     *
     * @return 参数列表
     */
    Map<String, String[]> getParamsMap();

    /**
     * 获取请求体提交的所有参数（key 重复取第一个）
     *
     * @return 参数列表
     */
    Map<String, String> getParamMap();

    /**
     * 在请求头里获取一个值
     *
     * @param name 键
     * @return 值
     */
    String getHeader(String name);

    /**
     * 在 Cookie 作用域里获取一个值（key 重复则取最后一个值）
     *
     * @param name 键
     * @return 值
     */
    String getCookieValue(String name);

    /**
     * 返回当前请求 path (不包括上下文名称)
     *
     * @return path
     */
    String getRequestPath();

    /**
     * 返回当前请求的 url，不带 query 参数，例：http://xxx.com/test
     *
     * @return 当前请求的 url
     */
    String getUrl();

    /**
     * 返回当前请求的类型
     *
     * @return /
     */
    String getMethod();

    /**
     * 请求转发
     *
     * @param path 转发路径
     * @return /
     */
    Object forward(String path);

    /**
     * 在请求体里检测是否存在某个参数
     *
     * @param name 键
     * @return 是否存在
     */
    default boolean hasParam(String name) {
        return HdStringUtil.hasText(getParam(name));
    }

    /**
     * 在 请求体里检测提供的参数是否为指定值
     *
     * @param name  键
     * @param value 值
     * @return 是否相等
     */
    default boolean hasParam(String name, String value) {
        String paramValue = getParam(name);
        return HdStringUtil.hasText(paramValue) && paramValue.equals(value);
    }

    /**
     * 在请求头里获取一个值
     *
     * @param name         键
     * @param defaultValue 值为空时的默认值
     * @return 值
     */
    default String getHeader(String name, String defaultValue) {
        String value = getHeader(name);
        if (HdStringUtil.hasEmpty(value)) {
            return defaultValue;
        }
        return value;
    }

    /**
     * 返回当前请求 path 是否为指定值
     *
     * @param path path
     * @return 当前请求 path 是否为指定值
     */
    default boolean isPath(String path) {
        return getRequestPath().equals(path);
    }

}
