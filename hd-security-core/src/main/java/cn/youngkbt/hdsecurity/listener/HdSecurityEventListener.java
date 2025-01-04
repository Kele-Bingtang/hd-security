package cn.youngkbt.hdsecurity.listener;

/**
 * Hd Security 事件监听器，包括各个前置和后置事件
 *
 * @author Tianke
 * @date 2024/11/25 22:14:33
 * @since 1.0.0
 */
public interface HdSecurityEventListener extends HdSecurityEventBeforeListener, HdSecurityEventAfterListener {
    // 最高优先级
    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;
    // 最低优先级
    int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

    /**
     * 监听器执行优先级
     *
     * @return int
     */
    default int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
