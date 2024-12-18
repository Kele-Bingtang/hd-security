package cn.youngkbt.hdsecurity.router;

import cn.youngkbt.hdsecurity.HdSecurityManager;

import java.util.List;
import java.util.Objects;

/**
 * Hd Security 路由 Helper
 *
 * @author Tianke
 * @date 2024/12/18 20:56:03
 * @since 1.0.0
 */
public class HdRouterHelper {

    private HdRouterHelper() {
    }

    /**
     * 路由匹配
     *
     * @param patterns    路由匹配表达式
     * @param requestPath 请求路径
     * @return 是否匹配
     */
    public static boolean isMatch(String patterns, String requestPath) {
        return HdSecurityManager.getContext().matchPath(patterns, requestPath);
    }

    /**
     * 路由匹配
     *
     * @param patternList 路由匹配表达式列表
     * @param requestPath 请求路径
     * @return 是否匹配
     */
    public static boolean isMatch(List<String> patternList, String requestPath) {
        for (String pattern : patternList) {
            if (isMatch(pattern, requestPath)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 路由匹配当前请求的 Path
     *
     * @param pattern 路由匹配表达式
     * @return 是否匹配
     */
    public static boolean isMatchRequestPath(String pattern) {
        return isMatch(pattern, HdSecurityManager.getContext().getRequest().getRequestPath());
    }

    /**
     * 路由匹配当前请求的 Path
     *
     * @param patterns 路由匹配表达式列表
     * @return 是否匹配
     */
    public static boolean isMatchRequestPath(String... patterns) {
        return isMatchRequestPath(List.of(patterns));
    }

    /**
     * 路由匹配当前请求的 Path
     *
     * @param patternList 路由匹配表达式列表
     * @return 是否匹配
     */
    public static boolean isMatchRequestPath(List<String> patternList) {
        return isMatch(patternList, HdSecurityManager.getContext().getRequest().getRequestPath());
    }

    /**
     * 请求方法匹配
     *
     * @param hdHttpMethodList 请求方法集合
     * @param method           要匹配的请求方法
     * @return 是否匹配
     */
    public static boolean isMatchMethod(List<HdHttpMethod> hdHttpMethodList, String method) {
        if (null == method) {
            return false;
        }
        for (HdHttpMethod httpMethod : hdHttpMethodList) {
            if (httpMethod == HdHttpMethod.ALL || Objects.equals(method, httpMethod.name())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 请求方法匹配
     *
     * @param hdHttpMethod 请求方法
     * @return 是否匹配
     */
    public static boolean isMatchMethod(HdHttpMethod hdHttpMethod) {
        return isMatchMethod(List.of(hdHttpMethod), HdSecurityManager.getContext().getRequest().getMethod());
    }

    /**
     * 请求方法匹配
     *
     * @param httpMethod 请求方法数组
     * @return 是否匹配
     */
    public static boolean isMatchMethod(String... httpMethod) {
        HdHttpMethod[] hdHttpMethods = HdHttpMethod.convert(httpMethod);
        return isMatchMethod(List.of(hdHttpMethods), HdSecurityManager.getContext().getRequest().getMethod());
    }

}
