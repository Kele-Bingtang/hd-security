package cn.youngkbt.hdsecurity.log;

import cn.youngkbt.hdsecurity.utils.DateUtil;

/**
 * 控制台日志打印
 *
 * @author Tianke
 * @date 2024/11/25 21:28:05
 * @since 1.0.0
 */
public class HdSecurityLogForConsole implements HdSecurityLog {

    /**
     * 日志等级
     */
    public static final int TRACE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int FATAL = 6;

    /**
     * 日志前缀
     */
    public static final String LOG_PREFIX = "[HdSecurity] ";
    public static final String TRACE_PREFIX = "[HdSecurity] TRACE ";
    public static final String DEBUG_PREFIX = "[HdSecurity] DEBUG ";
    public static final String INFO_PREFIX = "[HdSecurity] INFO ";
    public static final String WARN_PREFIX = "[HdSecurity] WARN ";
    public static final String ERROR_PREFIX = "[HdSecurity] ERROR ";
    public static final String FATAL_PREFIX = "[HdSecurity] FATAL ";

    /**
     * 日志输出的颜色
     */
    public static final String TRACE_COLOR = "\033[39m";
    public static final String DEBUG_COLOR = "\033[34m";
    public static final String INFO_COLOR = "\033[32m";
    public static final String WARN_COLOR = "\033[33m";
    public static final String ERROR_COLOR = "\033[31m";
    public static final String FATAL_COLOR = "\033[35m";

    public static final String DEFAULT_COLOR = TRACE_COLOR;

    @Override
    public void trace(String content, Object... args) {
        println(content, TRACE, TRACE_PREFIX, TRACE_COLOR, args);
    }

    @Override
    public void debug(String content, Object... args) {
        println(content, DEBUG, DEBUG_PREFIX, DEBUG_COLOR, args);
    }

    @Override
    public void info(String content, Object... args) {
        println(content, INFO, INFO_PREFIX, INFO_COLOR, args);
    }

    @Override
    public void warn(String content, Object... args) {
        println(content, WARN, WARN_PREFIX, WARN_COLOR, args);
    }

    @Override
    public void error(String content, Object... args) {
        println(content, ERROR, ERROR_PREFIX, ERROR_COLOR, args);
    }

    @Override
    public void fatal(String content, Object... args) {
        println(content, FATAL, FATAL_PREFIX, FATAL_COLOR, args);
    }

    /**
     * 打印日志到控制台
     *
     * @param level  日志等级
     * @param color  颜色编码
     * @param prefix 前缀
     * @param str    字符串
     * @param args   参数列表
     */
    public void println(String str, int level, String prefix, String color, Object... args) {
        System.out.println(color + prefix + DateUtil.now() + " " + StrFormatter.format(str, args) + DEFAULT_COLOR);
    }
}
