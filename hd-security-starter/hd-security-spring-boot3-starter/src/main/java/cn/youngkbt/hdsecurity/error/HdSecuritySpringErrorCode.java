package cn.youngkbt.hdsecurity.error;

/**
 * @author Tianke
 * @date 2024/12/24 00:39:08
 * @since 1.0.0
 */
public interface HdSecuritySpringErrorCode {
    /**
     * 企图在非 Web 上下文获取 Request、Response 等对象
     */
    int NOT_WEB_CONTEXT = 20101;
}
