package cn.youngkbt.hdsecurity.exception;

/**
 * Hd Security 非 Web 环境异常，检查 Web 环境不符合时抛出
 *
 * @author Tianke
 * @date 2024/12/24 00:38:14
 * @since 1.0.0
 */
public class HdSecurityNotWebContextException extends HdSecurityException {
    public HdSecurityNotWebContextException(String message) {
        super(message);
    }
}
