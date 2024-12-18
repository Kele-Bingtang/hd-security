package cn.youngkbt.hdsecurity.router;

import cn.youngkbt.hdsecurity.constants.DefaultConstant;
import cn.youngkbt.hdsecurity.exception.HdSecurityContinueMatchException;
import cn.youngkbt.hdsecurity.exception.HdSecurityStopException;
import cn.youngkbt.hdsecurity.hd.HdHelper;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Hd Security 路由匹配器
 *
 * @author Tianke
 * @date 2024/12/18 23:23:19
 * @since 1.0.0
 */
public class HdRouteMatcher {

    /**
     * 是否开启命中
     */
    private boolean useHit = true;

    public boolean isUseHit() {
        return useHit;
    }

    public HdRouteMatcher setUseHit(boolean useHit) {
        this.useHit = useHit;
        return this;
    }

    /**
     * 重置命中标记为 true
     *
     * @return this
     */
    public HdRouteMatcher reset() {
        this.useHit = true;
        return this;
    }

    /**
     * 路径匹配
     *
     * @param patterns 匹配符数组
     * @return this
     */
    public HdRouteMatcher match(String... patterns) {
        if (useHit) {
            useHit = HdRouterHelper.isMatchRequestPath(patterns);
        }
        return this;
    }

    /**
     * 路径匹配
     *
     * @param patternList 匹配符集合
     * @return this
     */
    public HdRouteMatcher match(List<String> patternList) {
        if (useHit) {
            useHit = HdRouterHelper.isMatchRequestPath(patternList);
        }
        return this;
    }

    /**
     * 路径匹配
     *
     * @param bool 匹配结果
     * @return this
     */
    public HdRouteMatcher match(boolean bool) {
        if (useHit) {
            useHit = bool;
        }
        return this;
    }

    /**
     * 路径匹配
     *
     * @param predicate 断言函数式接口：匹配结果
     * @return this
     */
    public HdRouteMatcher match(Predicate<HdRouteMatcher> predicate) {
        if (useHit) {
            useHit = predicate.test(this);
        }
        return this;
    }

    /**
     * 路径匹配和路径匹配排除
     *
     * @param pattern      匹配符
     * @param excludePattern 排除的匹配符
     * @return this
     */
    public HdRouteMatcher match(String pattern, String excludePattern) {
        return this.match(pattern).notMatch(excludePattern);
    }

    /**
     * 请求方法匹配
     *
     * @param hdHttpMethod 请求方法
     * @return this
     */
    public HdRouteMatcher matchMethod(HdHttpMethod hdHttpMethod) {
        if (useHit) {
            useHit = HdRouterHelper.isMatchMethod(hdHttpMethod);
        }
        return this;
    }

    /**
     * 请求方法匹配
     *
     * @param hdHttpMethod 请求方法数组
     * @return this
     */
    public HdRouteMatcher matchMethod(String... hdHttpMethod) {
        if (useHit) {
            useHit = HdRouterHelper.isMatchMethod(hdHttpMethod);
        }
        return this;
    }

    /**
     * 路径匹配排除
     *
     * @param patterns 匹配符数组
     * @return this
     */
    public HdRouteMatcher notMatch(String... patterns) {
        if (useHit) {
            useHit = !HdRouterHelper.isMatchRequestPath(patterns);
        }
        return this;
    }

    /**
     * 路径匹配排除
     *
     * @param patternList 匹配符集合
     * @return this
     */
    public HdRouteMatcher notMatch(List<String> patternList) {
        if (useHit) {
            useHit = !HdRouterHelper.isMatchRequestPath(patternList);
        }
        return this;
    }

    /**
     * 路径匹配排除
     *
     * @param bool 匹配结果
     * @return this
     */
    public HdRouteMatcher notMatch(boolean bool) {
        if (useHit) {
            useHit = !bool;
        }
        return this;
    }

    /**
     * 请求方法匹配排除
     *
     * @param predicate 断言函数式接口：匹配结果
     * @return this
     */
    public HdRouteMatcher notMatch(Predicate<HdRouteMatcher> predicate) {
        if (useHit) {
            useHit = !predicate.test(this);
        }
        return this;
    }

    /**
     * 请求方法匹配排除
     *
     * @param hdHttpMethod 请求方法
     * @return this
     */
    public HdRouteMatcher notMatchMethod(HdHttpMethod hdHttpMethod) {
        if (useHit) {
            useHit = !HdRouterHelper.isMatchMethod(hdHttpMethod);
        }
        return this;
    }

    /**
     * 请求方法匹配排除
     *
     * @param hdHttpMethod 请求方法数组
     * @return this
     */
    public HdRouteMatcher notMatchMethod(String... hdHttpMethod) {
        if (useHit) {
            useHit = !HdRouterHelper.isMatchMethod(hdHttpMethod);
        }
        return this;
    }

    /**
     * 执行校验函数（不带参数）
     *
     * @param runnable 要执行的函数
     * @return this
     */
    public HdRouteMatcher check(Runnable runnable) {
        if (useHit) {
            runnable.run();
        }
        return this;
    }

    /**
     * 执行校验函数（带 this 参数）
     *
     * @param consumer 要执行的函数
     * @return this
     */
    public HdRouteMatcher check(Consumer<HdRouteMatcher> consumer) {
        if (useHit) {
            consumer.accept(this);
        }
        return this;
    }

    /**
     * 登录校验
     *
     * @return this
     */
    public HdRouteMatcher checkLogin() {
        return checkLogin(DefaultConstant.DEFAULT_ACCOUNT_TYPE);
    }

    /**
     * 指定账号类型执行登录校验
     *
     * @param accountType 账号类型
     * @return this
     */
    public HdRouteMatcher checkLogin(String accountType) {
        if (useHit) {
            HdHelper.loginHelper(accountType).checkLogin();
        }
        return this;
    }

    /**
     * 自由匹配代码块（在 free 作用域里执行 stop()不会跳出整个匹配作用域，而是仅仅跳出 free 代码块）
     *
     * @param consumer 自由匹配代码块
     * @return this
     */
    public HdRouteMatcher free(Consumer<HdRouteMatcher> consumer) {
        if (useHit) {
            try {
                consumer.accept(this);
            } catch (HdSecurityStopException e) {
                // 跳出 free 自由匹配代码块 
            }
        }
        return this;
    }

    /**
     * 停止匹配，跳出当前匹配函数
     */
    public void stopMatch() {
        if (useHit) {
            throw new HdSecurityStopException();
        }
    }

    /**
     * 停止匹配，结束匹配链
     */
    public void continueMatch() {
        if (useHit) {
            throw new HdSecurityContinueMatchException("");
        }
    }

    /**
     * 停止匹配，结束匹配链，并返回结果
     *
     * @param result 结果
     */
    public void continueMatch(String result) {
        if (useHit) {
            throw new HdSecurityContinueMatchException(result);
        }
    }
}
