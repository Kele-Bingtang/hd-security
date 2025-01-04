package cn.youngkbt.hdsecurity.exception;

/**
 * Hd Security 事件异常，校验监听器失败时抛出
 *
 * @author Tianke
 * @date 2024/11/25 22:24:15
 * @since 1.0.0
 */
public class HdSecurityEventException extends HdSecurityException {
    public HdSecurityEventException(String message) {
        super(message);
    }
}
