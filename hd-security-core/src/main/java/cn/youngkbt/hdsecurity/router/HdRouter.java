package cn.youngkbt.hdsecurity.router;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Hd Security 路由匹配操作门户
 *
 * @author Tianke
 * @date 2024/12/18 23:26:49
 * @since 1.0.0
 */
public class HdRouter {
    /**
     * 路径匹配
     *
     * @param patterns 匹配符数组
     * @return this
     */
    public static HdRouteMatcher match(String... patterns) {
        return new HdRouteMatcher().match(patterns);
    }

    /**
     * 路径匹配
     *
     * @param patternList 匹配符集合
     * @return this
     */
    public static HdRouteMatcher match(List<String> patternList) {
        return new HdRouteMatcher().match(patternList);
    }

    /**
     * 路径匹配
     *
     * @param bool 匹配结果
     * @return this
     */
    public static HdRouteMatcher match(boolean bool) {
        return new HdRouteMatcher().match(bool);
    }

    /**
     * 路径匹配
     *
     * @param predicate 断言函数式接口：匹配结果
     * @return this
     */
    public static HdRouteMatcher match(Predicate<HdRouteMatcher> predicate) {
        return new HdRouteMatcher().match(predicate);
    }

    /**
     * 请求方法匹配
     *
     * @param hdHttpMethod 请求方法
     * @return this
     */
    public static HdRouteMatcher matchMethod(HdHttpMethod hdHttpMethod) {
        return new HdRouteMatcher().matchMethod(hdHttpMethod);
    }

    /**
     * 请求方法匹配
     *
     * @param hdHttpMethod 请求方法数组
     * @return this
     */
    public static HdRouteMatcher matchMethod(String... hdHttpMethod) {
        return new HdRouteMatcher().matchMethod(hdHttpMethod);
    }

    /**
     * 路径匹配排除
     *
     * @param patterns 匹配符数组
     * @return this
     */
    public static HdRouteMatcher notMatch(String... patterns) {
        return new HdRouteMatcher().notMatch(patterns);
    }

    /**
     * 路径匹配排除
     *
     * @param patternList 匹配符集合
     * @return this
     */
    public static HdRouteMatcher notMatch(List<String> patternList) {
        return new HdRouteMatcher().notMatch(patternList);
    }

    /**
     * 路径匹配排除
     *
     * @param bool 匹配结果
     * @return this
     */
    public static HdRouteMatcher notMatch(boolean bool) {
        return new HdRouteMatcher().notMatch(bool);
    }

    /**
     * 请求方法匹配排除
     *
     * @param predicate 断言函数式接口：匹配结果
     * @return this
     */
    public static HdRouteMatcher notMatch(Predicate<HdRouteMatcher> predicate) {
        return new HdRouteMatcher().notMatch(predicate);
    }

    /**
     * 请求方法匹配排除
     *
     * @param hdHttpMethod 请求方法
     * @return this
     */
    public static HdRouteMatcher notMatchMethod(HdHttpMethod hdHttpMethod) {
        return new HdRouteMatcher().notMatchMethod(hdHttpMethod);
    }

    /**
     * 请求方法匹配排除
     *
     * @param hdHttpMethod 请求方法数组
     * @return this
     */
    public static HdRouteMatcher notMatchMethod(String... hdHttpMethod) {
        return new HdRouteMatcher().notMatchMethod(hdHttpMethod);
    }

    /**
     * 执行校验函数（不带参数）
     *
     * @param runnable 要执行的函数
     * @return this
     */
    public static HdRouteMatcher check(Runnable runnable) {
        return new HdRouteMatcher().check(runnable);
    }

    /**
     * 执行校验函数（带 this 参数）
     *
     * @param consumer 要执行的函数
     * @return this
     */
    public static HdRouteMatcher check(Consumer<HdRouteMatcher> consumer) {
        return new HdRouteMatcher().check(consumer);
    }

    /**
     * 登录校验
     *
     * @return this
     */
    public static HdRouteMatcher checkLogin() {
        return new HdRouteMatcher().checkLogin();
    }

    /**
     * 指定账号类型执行登录校验
     *
     * @param accountType 账号类型
     * @return this
     */
    public static HdRouteMatcher checkLogin(String accountType) {
        return new HdRouteMatcher().checkLogin(accountType);
    }

    /**
     * 自由匹配代码块（在 free 作用域里执行 stop()不会跳出整个匹配作用域，而是仅仅跳出 free 代码块）
     *
     * @param consumer 自由匹配代码块
     * @return this
     */
    public static HdRouteMatcher free(Consumer<HdRouteMatcher> consumer) {
        return new HdRouteMatcher().free(consumer);
    }

    /**
     * 停止匹配，跳出当前匹配函数
     */
    public void stopMatch() {
        new HdRouteMatcher().stopMatch();
    }

    /**
     * 停止匹配，结束匹配链
     */
    public void continueMatch() {
        new HdRouteMatcher().continueMatch();
    }

    /**
     * 停止匹配，结束匹配链，并返回结果
     *
     * @param result 结果
     */
    public void continueMatch(String result) {
        new HdRouteMatcher().continueMatch(result);
    }
}
