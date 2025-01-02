package cn.youngkbt.hdsecurity.strategy;

import cn.youngkbt.hdsecurity.constants.WebConstant;
import cn.youngkbt.hdsecurity.exception.HdSecurityPathInvalidException;
import cn.youngkbt.hdsecurity.function.HdSecurityPathCheckFunction;
import cn.youngkbt.hdsecurity.function.HdSecurityPathInvalidHandleFunction;
import cn.youngkbt.hdsecurity.utils.SpringMVCHolder;

/**
 * @author Tianke
 * @date 2024/12/31 00:14:25
 * @since 1.0.0
 */
public class HdSecurityPathCheckStrategy {

    public static HdSecurityPathCheckStrategy instance = new HdSecurityPathCheckStrategy();

    /**
     * 请求 path 黑名单
     */
    private String[] blackPaths = {};

    /**
     * 请求 path 白名单
     */
    private String[] whitePaths = {};

    /**
     * 请求 path 不允许出现的字符
     */
    private String[] invalidCharacter = WebConstant.INVALID_CHARACTER;

    public String[] getBlackPaths() {
        return blackPaths;
    }

    public HdSecurityPathCheckStrategy setBlackPaths(String[] blackPaths) {
        this.blackPaths = blackPaths;
        return this;
    }

    public String[] getWhitePaths() {
        return whitePaths;
    }

    public HdSecurityPathCheckStrategy setWhitePaths(String[] whitePaths) {
        this.whitePaths = whitePaths;
        return this;
    }

    public String[] getInvalidCharacter() {
        return invalidCharacter;
    }

    public HdSecurityPathCheckStrategy setInvalidCharacter(String[] invalidCharacter) {
        this.invalidCharacter = invalidCharacter;
        return this;
    }

    public HdSecurityPathCheckFunction pathCheckFunction = (path, request, response) -> {
        // 请求地址不允许为 Null
        if (null == path) {
            throw new HdSecurityPathInvalidException("请求路径不能为 Null").setPath(path);
        }

        // 如果在白名单里，则直接放行
        for (String item : whitePaths) {
            if (path.equals(item)) {
                return;
            }
        }

        // 如果在黑名单里，则抛出异常
        for (String item : blackPaths) {
            if (path.equals(item)) {
                throw new HdSecurityPathInvalidException("非法请求：" + path).setPath(path);
            }
        }

        // 不允许包含非法字符
        for (String s : invalidCharacter) {
            if (path.contains(s)) {
                throw new HdSecurityPathInvalidException("请求路径带有非法字符").setPath(path);
            }
        }

        // 不允许出现跨目录
        if (path.contains("/.") || path.contains("\\.")) {
            throw new HdSecurityPathInvalidException("请求路径出现跨目录字符").setPath(path);
        }
    };

    public HdSecurityPathInvalidHandleFunction pathInvalidHandleFunction = (e, request, response) ->
            SpringMVCHolder.responseWrite(response, "请求地址：" + e.getPath() + "，异常信息：" + e.getMessage());
}
