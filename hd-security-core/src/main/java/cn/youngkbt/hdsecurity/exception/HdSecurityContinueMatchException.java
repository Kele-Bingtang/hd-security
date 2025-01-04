package cn.youngkbt.hdsecurity.exception;

/**
 * Hd Security 停止匹配异常，停止路由匹配函数时抛出
 *
 * @author Tianke
 * @date 2024/12/18 22:49:31
 * @since 1.0.0
 */
public class HdSecurityContinueMatchException extends HdSecurityException {
    public HdSecurityContinueMatchException(String message) {
        super(message);
    }
}
