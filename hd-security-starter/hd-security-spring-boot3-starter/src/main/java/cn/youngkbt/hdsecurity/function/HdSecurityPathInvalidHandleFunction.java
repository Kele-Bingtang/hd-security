package cn.youngkbt.hdsecurity.function;


import cn.youngkbt.hdsecurity.exception.HdSecurityPathInvalidException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Hd Security 路径无效时处理函数
 *
 * @author Tianke
 * @date 2024/12/31 00:23:44
 * @since 1.0.0
 */
@FunctionalInterface
public interface HdSecurityPathInvalidHandleFunction {

    /**
     * 路径无效时处理函数
     *
     * @param e        路径无效异常
     * @param request  请求类
     * @param response 响应类
     */
    void handle(HdSecurityPathInvalidException e, HttpServletRequest request, HttpServletResponse response) throws IOException;
}
