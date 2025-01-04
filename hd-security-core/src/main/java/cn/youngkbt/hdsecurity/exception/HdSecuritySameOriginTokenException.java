package cn.youngkbt.hdsecurity.exception;

/**
 * Hd Security 同源 Token 相关异常，检查同源 Token 不合法时抛出
 *
 * @author Tianke
 * @date 2024/12/23 21:49:43
 * @since 1.0.0
 */
public class HdSecuritySameOriginTokenException extends HdSecurityException {
    public HdSecuritySameOriginTokenException(String message) {
        super(message);
    }
}
