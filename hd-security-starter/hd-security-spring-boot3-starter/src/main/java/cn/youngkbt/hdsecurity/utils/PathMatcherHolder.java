package cn.youngkbt.hdsecurity.utils;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * 路径匹配工具类
 *
 * @author Tianke
 * @date 2024/12/24 00:42:55
 * @since 1.0.0
 */
public class PathMatcherHolder {

    private PathMatcherHolder() {
    }

    private static final PathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    public static PathMatcher getAntPathMatcher() {
        return ANT_PATH_MATCHER;
    }

    /**
     * 判断：指定路由匹配符是否可以匹配成功指定路径
     *
     * @param pattern 路由匹配符
     * @param path    要匹配的路径
     * @return 是否匹配成功
     */
    public static boolean match(String pattern, String path) {
        return ANT_PATH_MATCHER.match(pattern, path);
    }
}
