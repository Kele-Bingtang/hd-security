package cn.youngkbt.hdsecurity.constants;

/**
 * @author Tianke
 * @date 2025/1/1 17:30:19
 * @since 1.0.0
 */
public interface WebConstant {

    String CONTENT_TYPE = "Content-Type";

    /**
     * 违法字符，在 Web 框架校验请求的 Path 用到
     */
    String[] INVALID_CHARACTER = {
            "//", "\\",
            "%2e", "%2E",    // .
            "%2f", "%2F",    // /
            "%5c", "%5C",    // \
            "%25"    // 空格
    };
}
