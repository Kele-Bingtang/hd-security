package cn.youngkbt.hdsecurity.exception;

/**
 * Hd Security Token 异常，Token 校验不合法时抛出
 *
 * @author Tianke
 * @date 2024/11/28 01:15:42
 * @since 1.0.0
 */
public class HdSecurityTokenException extends HdSecurityException {
    public HdSecurityTokenException(String message) {
        super(message);
    }
}
