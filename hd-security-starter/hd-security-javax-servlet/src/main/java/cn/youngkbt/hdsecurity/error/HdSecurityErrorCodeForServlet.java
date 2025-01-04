package cn.youngkbt.hdsecurity.error;

/**
 * Hd Security Javax Servlet 错误码
 *
 * @author Tianke
 * @date 2024/12/24 01:03:27
 * @since 1.0.0
 */
public interface HdSecurityErrorCodeForServlet {
    /**
     * 转发失败
     */
    int FORWARD_FAILURE = 20001;

    /**
     * 重定向失败
     */
    int REDIRECT_FAILURE = 20002;
}
