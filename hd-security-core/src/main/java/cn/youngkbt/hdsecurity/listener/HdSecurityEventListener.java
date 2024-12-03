package cn.youngkbt.hdsecurity.listener;

/**
 * @author Tianke
 * @date 2024/11/25 22:14:33
 * @since 1.0.0
 */
public interface HdSecurityEventListener extends HdSecurityEventBeforeListener, HdSecurityEventAfterListener {
    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;
    int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

    /**
     * 监听器执行优先级
     * @return int
     */
    default int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
