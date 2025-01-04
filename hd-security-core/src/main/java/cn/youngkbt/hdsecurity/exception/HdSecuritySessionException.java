package cn.youngkbt.hdsecurity.exception;

/**
 * Hd Security Session 异常，获取 Session 前校验不合法时抛出
 *
 * @author Tianke
 * @date 2024/11/28 01:40:30
 * @since 1.0.0
 */
public class HdSecuritySessionException extends HdSecurityException {
    public HdSecuritySessionException(String message) {
        super(message);
    }
}
