package cn.youngkbt.hdsecurity.exception;

/**
 * Hd Security 停止匹配异常，在停止路由匹配时抛出，代表跳出本次路由匹配规则，不代表跳出所有的路由匹配规则
 *
 * @author Tianke
 * @date 2024/12/18 22:47:03
 * @since 1.0.0
 */
public class HdSecurityStopException extends HdSecurityException {
    public HdSecurityStopException() {
        super("stop match");
    }
}
