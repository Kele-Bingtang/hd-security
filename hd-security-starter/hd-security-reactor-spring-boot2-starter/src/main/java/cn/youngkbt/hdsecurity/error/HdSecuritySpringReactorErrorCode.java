package cn.youngkbt.hdsecurity.error;

/**
 * Hd Security Spring 错误码
 * @author Tianke
 * @date 2024/12/24 00:39:08
 * @since 1.0.0
 */
public interface HdSecuritySpringReactorErrorCode {
    /**
     * 企图在非 Web 上下文获取 Request、Response 等对象
     */
    int NOT_WEB_CONTEXT = 20101;

    /**
     * 默认 Filter 异常处理码
     */
    int DEFAULT_FILTER_ERROR = 20102;
}
