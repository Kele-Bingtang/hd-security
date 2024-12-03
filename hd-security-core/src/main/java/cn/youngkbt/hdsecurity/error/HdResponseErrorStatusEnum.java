package cn.youngkbt.hdsecurity.error;

/**
 * Hd Security 响应状态码
 *
 * @author Tianke
 * @date 2024/11/25 00:39:54
 * @since 1.0.0
 */
public enum HdResponseErrorStatusEnum implements HdBaseErrorStatusEnum {

    /**
     * 操作成功
     */
    SUCCESS(200, "success", "操作成功"),
    /**
     * 操作失败
     */
    FAIL(500, "fail", "操作失败"),
    /**
     * 操作错误
     */
    ERROR(500, "error", "操作错误"),

    ;

    private Integer code;

    private String status;

    private String message;

    HdResponseErrorStatusEnum(Integer code, String status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    void setCode(Integer code) {
        this.code = code;
    }

    void setStatus(String status) {
        this.status = status;
    }

    void setMessage(String message) {
        this.message = message;
    }
}
