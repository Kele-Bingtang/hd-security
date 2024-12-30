package cn.youngkbt.hdsecurity.filter;

import java.util.List;
import java.util.function.Function;

/**
 * Hd Security 过滤器接口
 *
 * @author Tianke
 * @date 2024/12/31 00:50:24
 * @since 1.0.0
 */
public interface HdSecurityFilter {
    /**
     * 添加需要拦截的路由
     *
     * @param paths 路由
     * @return 对象自身
     */
    HdSecurityFilter addInclude(String... paths);

    /**
     * 添加不需要拦截的路由
     *
     * @param paths 路由
     * @return 对象自身
     */
    HdSecurityFilter addExclude(String... paths);

    /**
     * 写入需要拦截的路由集合
     *
     * @param pathList 路由集合
     * @return 对象自身
     */
    HdSecurityFilter setIncludeList(List<String> pathList);

    /**
     * 写入不需要拦截的路由集合
     *
     * @param pathList 路由集合
     * @return 对象自身
     */
    HdSecurityFilter setExcludeList(List<String> pathList);


    /**
     * 写入认证前置函数，每次执行认证函数前执行该函数
     * <p>
     * <b>注意点：前置认证函数将不受 includeList 与 excludeList 的限制，所有路由的请求都会进入 beforeAuth</b>
     * </p>
     *
     * @param beforeAuth 认证前置函数
     * @return 对象自身
     */
    HdSecurityFilter setBeforeAuth(Runnable beforeAuth);

    /**
     * 写入认证函数：每次请求执行
     *
     * @param auth see note
     * @return 对象自身
     */
    HdSecurityFilter setAuth(Runnable auth);

    /**
     * 写入异常处理函数：每次认证函数发生异常时执行此函数
     *
     * @param error 异常处理函数
     * @return 对象自身
     */
    HdSecurityFilter setError(Function<Throwable, Object> error);

}
