package cn.youngkbt.hdsecurity.exception;

/**
 * Hd Security Http Basic 认证异常，Http Basic 认证失败时抛出
 * @author Tianke
 * @date 2024/12/14 01:59:34
 * @since 1.0.0
 */
public class HdSecurityHttpBasicAuthException extends HdSecurityException {
    public HdSecurityHttpBasicAuthException(String message) {
        super(message);
    }
}
