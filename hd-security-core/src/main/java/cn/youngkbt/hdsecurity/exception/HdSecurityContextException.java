package cn.youngkbt.hdsecurity.exception;

/**
 * Hd Security 上下文异常，不支持上下文环境时抛出
 * @author Tianke
 * @date 2024/12/2 23:03:54
 * @since 1.0.0
 */
public class HdSecurityContextException extends HdSecurityException {
    public HdSecurityContextException(String message) {
        super(message);
    }
}
