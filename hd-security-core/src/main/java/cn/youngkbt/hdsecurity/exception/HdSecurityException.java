package cn.youngkbt.hdsecurity.exception;

import cn.youngkbt.hdsecurity.error.HdBaseErrorStatusEnum;
import cn.youngkbt.hdsecurity.error.HdResponseErrorStatusEnum;

import java.io.Serial;

/**
 * 基础异常类，可以继承该类定义自己的异常类
 *
 * @author Tianke
 * @date 2024/11/25 00:43:57
 * @since 1.0.0
 */
public class HdSecurityException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer code = HdResponseErrorStatusEnum.FAIL.getCode();
    private String status = HdResponseErrorStatusEnum.FAIL.getStatus();
    private String message = HdResponseErrorStatusEnum.FAIL.getMessage();

    public HdSecurityException() {
    }

    public HdSecurityException(Integer code) {
        this.code = code;
    }

    public HdSecurityException(String message) {
        this.message = message;
    }

    public HdSecurityException(Integer code, String status) {
        this.code = code;
        this.status = status;
    }

    public HdSecurityException(Integer code, String status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public HdSecurityException(HdBaseErrorStatusEnum hdBaseErrorStatusEnum) {
        this.code = hdBaseErrorStatusEnum.getCode();
        this.status = hdBaseErrorStatusEnum.getStatus();
        this.message = hdBaseErrorStatusEnum.getMessage();
    }

    public HdSecurityException(Throwable cause) {
        super(cause);
    }

    public HdSecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    public Integer getCode() {
        return code;
    }

    public HdSecurityException setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public HdSecurityException setStatus(String status) {
        this.status = status;
        return this;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HdSecurityException setMessage(String message) {
        this.message = message;
        return this;
    }
}
