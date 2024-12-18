package cn.youngkbt.hdsecurity.router;

import cn.youngkbt.hdsecurity.utils.HdStringUtil;

import java.util.Arrays;

/**
 * Http 请求各种请求类型
 *
 * @author Tianke
 * @date 2024/12/18 23:00:50
 * @since 1.0.0
 */
public enum HdHttpMethod {
    ALL, GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE, CONNECT,

    ;

    public static HdHttpMethod[] convert(String... method) {
        if (HdStringUtil.hasEmpty(method)) {
            return new HdHttpMethod[0];
        }
        return Arrays.stream(method).map(m -> HdHttpMethod.valueOf(m.toUpperCase())).toArray(HdHttpMethod[]::new);
    }
}
