package cn.youngkbt.hdsecurity.strategy;

import cn.youngkbt.hdsecurity.constants.DefaultConstant;
import cn.youngkbt.hdsecurity.exception.HdSecurityPathInvalidException;
import cn.youngkbt.hdsecurity.function.HdSecurityPathCheckFunction;
import cn.youngkbt.hdsecurity.function.HdSecurityPathInvalidHandleFunction;

import java.io.PrintWriter;

/**
 * @author Tianke
 * @date 2024/12/31 00:14:25
 * @since 1.0.0
 */
public class HdSecurityPathCheckStrategy {

    public static HdSecurityPathCheckStrategy instance = new HdSecurityPathCheckStrategy();

    /**
     * 请求 path 不允许出现的字符
     */
    public static String[] INVALID_CHARACTER = DefaultConstant.INVALID_CHARACTER;

    public HdSecurityPathCheckFunction pathCheckFunction = (path, request, response) -> {
        // 请求地址不允许为 Null
        if (null == path) {
            throw new HdSecurityPathInvalidException("请求路径不能为 Null").setPath(path);
        }

        // 不允许包含非法字符
        for (String s : INVALID_CHARACTER) {
            if (path.contains(s)) {
                throw new HdSecurityPathInvalidException("请求路径带有非法字符").setPath(path);
            }
        }

        // 不允许出现跨目录
        if (path.contains("/.") || path.contains("\\.")) {
            throw new HdSecurityPathInvalidException("请求路径出现跨目录字符").setPath(path);
        }
    };

    public HdSecurityPathInvalidHandleFunction pathInvalidHandleFunction = (e, request, response) -> {
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print("请求地址：" + e.getPath() + "，异常信息：" + e.getMessage());
        writer.flush();
    };
}
