package cn.youngkbt.hdsecurity.function;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Hd Security 路径检查函数
 *
 * @author Tianke
 * @date 2024/12/31 00:15:33
 * @since 1.0.0
 */
@FunctionalInterface
public interface HdSecurityPathCheckFunction {
    /**
     * 路径检查
     *
     * @param path     请求路径
     * @param request  请求类
     * @param response 响应类
     */
    void check(String path, HttpServletRequest request, HttpServletResponse response);
}
