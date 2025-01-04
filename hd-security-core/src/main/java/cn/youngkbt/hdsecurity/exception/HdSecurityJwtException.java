package cn.youngkbt.hdsecurity.exception;

/**
 * Hd Security JWT 异常，检查 JWT 时抛出
 *
 * @author Tianke
 * @date 2025/1/1 23:09:40
 * @since 1.0.0
 */
public class HdSecurityJwtException extends HdSecurityException {
    public HdSecurityJwtException(String message) {
        super(message);
    }
}
