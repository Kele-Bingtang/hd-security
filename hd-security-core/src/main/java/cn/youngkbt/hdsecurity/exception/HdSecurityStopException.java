package cn.youngkbt.hdsecurity.exception;

/**
 * @author Tianke
 * @date 2024/12/18 22:47:03
 * @since 1.0.0
 */
public class HdSecurityStopException extends HdSecurityException {
    public HdSecurityStopException() {
        super("stop match");
    }
}
