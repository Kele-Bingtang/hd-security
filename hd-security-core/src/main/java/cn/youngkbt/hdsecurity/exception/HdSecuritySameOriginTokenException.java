package cn.youngkbt.hdsecurity.exception;

/**
 * @author Tianke
 * @date 2024/12/23 21:49:43
 * @since 1.0.0
 */
public class HdSecuritySameOriginTokenException extends HdSecurityException {
    public HdSecuritySameOriginTokenException(String message) {
        super(message);
    }
}
