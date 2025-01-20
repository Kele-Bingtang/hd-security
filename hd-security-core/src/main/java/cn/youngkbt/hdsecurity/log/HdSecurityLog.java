package cn.youngkbt.hdsecurity.log;

/**
 * Hd Security 日志输出接口
 *
 * @author Tianke
 * @date 2024/11/25 01:29:08
 * @since 1.0.0
 */
public interface HdSecurityLog {

    /**
     * 初始化，在加载到 Hd Security 时执行
     */
    default void init() {
    }

    /**
     * 输出 trace 日志
     *
     * @param content 日志内容
     * @param args    参数列表
     */
    void trace(String content, Object... args);

    /**
     * 输出 debug 日志
     *
     * @param content 日志内容
     * @param args    参数列表
     */
    void debug(String content, Object... args);

    /**
     * 输出 info 日志
     *
     * @param content 日志内容
     * @param args    参数列表
     */
    void info(String content, Object... args);

    /**
     * 输出 warn 日志
     *
     * @param content 日志内容
     * @param args    参数列表
     */
    void warn(String content, Object... args);

    /**
     * 输出 error 日志
     *
     * @param content 日志内容
     * @param args    参数列表
     */
    void error(String content, Object... args);

    /**
     * 输出 fatal 日志
     *
     * @param content 日志内容
     * @param args    参数列表
     */
    void fatal(String content, Object... args);
}
