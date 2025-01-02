package cn.youngkbt.hdsecurity.model.http;

import cn.youngkbt.hdsecurity.jwt.error.HdBaseErrorStatusEnum;
import cn.youngkbt.hdsecurity.jwt.error.HdResponseErrorStatusEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * 响应结果封装
 *
 * @author Tianke
 * @date 2024/11/25 00:36:19
 * @since 1.0.0
 */
public class HdResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 自定义状态码
     **/
    private Integer code;
    /**
     * 状态码信息
     **/
    protected String status;
    /**
     * 消息
     **/
    private String message;
    /**
     * 时间戳
     **/
    private Long timestamp;
    /**
     * 数据
     **/
    protected T data;

    public static <T> HdResponse<T> instance() {
        return new HdResponse<>();
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    // ---------- 响应结果常用方法封装 ---------

    public static <T> HdResponse<T> response(T data, HdBaseErrorStatusEnum hdBaseErrorStatusEnum) {
        HdResponse<T> hdResponse = HdResponse.instance();
        hdResponse.setData(data);
        hdResponse.setCode(hdBaseErrorStatusEnum.getCode());
        hdResponse.setStatus(hdBaseErrorStatusEnum.getStatus());
        hdResponse.setMessage(hdBaseErrorStatusEnum.getMessage());
        hdResponse.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return hdResponse;
    }

    public static <T> HdResponse<T> response(T data, HdBaseErrorStatusEnum hdBaseErrorStatusEnum, String message) {
        HdResponse<T> hdResponse = HdResponse.instance();
        hdResponse.setData(data);
        hdResponse.setCode(hdBaseErrorStatusEnum.getCode());
        hdResponse.setStatus(hdBaseErrorStatusEnum.getStatus());
        hdResponse.setMessage(message);
        hdResponse.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return hdResponse;
    }

    public static <T> HdResponse<Map<String, T>> response(String key, T data, HdBaseErrorStatusEnum status) {
        Map<String, T> map = new HashMap<>(16);
        HdResponse<Map<String, T>> hdResponse = HdResponse.instance();
        map.put(key, data);
        hdResponse.setData(map);
        hdResponse.setCode(status.getCode());
        hdResponse.setStatus(status.getStatus());
        hdResponse.setMessage(status.getMessage());
        hdResponse.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return hdResponse;
    }

    public static <T> HdResponse<T> response(T data, Integer code, String status, String message) {
        HdResponse<T> hdResponse = HdResponse.instance();
        hdResponse.setData(data);
        hdResponse.setCode(code);
        hdResponse.setStatus(status);
        hdResponse.setMessage(message);
        hdResponse.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return hdResponse;
    }

    public static <T> HdResponse<Map<String, T>> response(String key, T data, Integer code, String status, String message) {
        Map<String, T> map = new HashMap<>(16);
        HdResponse<Map<String, T>> hdResponse = HdResponse.instance();
        map.put(key, data);
        hdResponse.setData(map);
        hdResponse.setCode(code);
        hdResponse.setStatus(status);
        hdResponse.setMessage(message);
        hdResponse.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return hdResponse;
    }

    public static <T> HdResponse<T> ok(T data) {
        HdResponse<T> hdResponse = HdResponse.instance();
        hdResponse.setData(data);
        hdResponse.setCode(HdResponseErrorStatusEnum.SUCCESS.getCode());
        hdResponse.setStatus(HdResponseErrorStatusEnum.SUCCESS.getStatus());
        hdResponse.setMessage(HdResponseErrorStatusEnum.SUCCESS.getMessage());
        hdResponse.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return hdResponse;
    }

    public static <T> HdResponse<Map<String, T>> ok(String key, T data) {
        Map<String, T> map = new HashMap<>(16);
        HdResponse<Map<String, T>> hdResponse = HdResponse.instance();
        map.put(key, data);
        hdResponse.setData(map);
        hdResponse.setCode(HdResponseErrorStatusEnum.SUCCESS.getCode());
        hdResponse.setStatus(HdResponseErrorStatusEnum.SUCCESS.getStatus());
        hdResponse.setMessage(HdResponseErrorStatusEnum.SUCCESS.getMessage());
        hdResponse.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return hdResponse;
    }

    public static <T> HdResponse<T> okMessage(String message) {
        HdResponse<T> hdResponse = HdResponse.instance();
        hdResponse.setData(null);
        hdResponse.setCode(HdResponseErrorStatusEnum.SUCCESS.getCode());
        hdResponse.setStatus(HdResponseErrorStatusEnum.SUCCESS.getStatus());
        hdResponse.setMessage(message);
        hdResponse.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return hdResponse;
    }

    public static <T> HdResponse<T> ok(Integer code, String message) {
        return returnOKResponse(code, message);
    }

    public static <T> HdResponse<T> fail(T data) {
        HdResponse<T> hdResponse = HdResponse.instance();
        hdResponse.setData(data);
        hdResponse.setCode(HdResponseErrorStatusEnum.FAIL.getCode());
        hdResponse.setStatus(HdResponseErrorStatusEnum.FAIL.getStatus());
        hdResponse.setMessage(HdResponseErrorStatusEnum.FAIL.getMessage());
        hdResponse.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return hdResponse;
    }

    public static <T> HdResponse<Map<String, T>> fail(String key, T data) {
        Map<String, T> map = new HashMap<>(16);
        HdResponse<Map<String, T>> hdResponse = HdResponse.instance();
        map.put(key, data);
        hdResponse.setData(map);
        hdResponse.setCode(HdResponseErrorStatusEnum.FAIL.getCode());
        hdResponse.setStatus(HdResponseErrorStatusEnum.FAIL.getStatus());
        hdResponse.setMessage(HdResponseErrorStatusEnum.FAIL.getMessage());
        hdResponse.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return hdResponse;
    }

    public static <T> HdResponse<T> fail(HdBaseErrorStatusEnum hdBaseErrorStatusEnum) {
        return returnResponse(hdBaseErrorStatusEnum);
    }

    public static <T> HdResponse<T> fail(HdBaseErrorStatusEnum hdBaseErrorStatusEnum, String message) {
        return returnResponse(hdBaseErrorStatusEnum, message);
    }

    public static <T> HdResponse<T> failMessage(String message) {
        HdResponse<T> hdResponse = HdResponse.instance();
        hdResponse.setData(null);
        hdResponse.setCode(HdResponseErrorStatusEnum.FAIL.getCode());
        hdResponse.setStatus(HdResponseErrorStatusEnum.FAIL.getStatus());
        hdResponse.setMessage(message);
        hdResponse.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return hdResponse;
    }

    public static <T> HdResponse<T> fail(Integer code, String message) {
        return returnFailResponse(code, message);
    }

    public static <T> HdResponse<T> error(T data) {
        HdResponse<T> hdResponse = HdResponse.instance();
        hdResponse.setData(data);
        hdResponse.setCode(HdResponseErrorStatusEnum.ERROR.getCode());
        hdResponse.setStatus(HdResponseErrorStatusEnum.ERROR.getStatus());
        hdResponse.setMessage(HdResponseErrorStatusEnum.ERROR.getMessage());
        hdResponse.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return hdResponse;
    }

    public static <T> HdResponse<Map<String, T>> error(String key, T data) {
        Map<String, T> map = new HashMap<>(16);
        HdResponse<Map<String, T>> hdResponse = HdResponse.instance();
        map.put(key, data);
        hdResponse.setData(map);
        hdResponse.setCode(HdResponseErrorStatusEnum.ERROR.getCode());
        hdResponse.setStatus(HdResponseErrorStatusEnum.ERROR.getStatus());
        hdResponse.setMessage(HdResponseErrorStatusEnum.ERROR.getMessage());
        hdResponse.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return hdResponse;
    }

    public static <T> HdResponse<T> error(HdBaseErrorStatusEnum hdBaseErrorStatusEnum) {
        return returnResponse(hdBaseErrorStatusEnum);
    }

    public static <T> HdResponse<T> error(HdBaseErrorStatusEnum hdBaseErrorStatusEnum, String message) {
        return returnResponse(hdBaseErrorStatusEnum, message);
    }

    public static <T> HdResponse<T> errorMessage(String message) {
        HdResponse<T> hdResponse = HdResponse.instance();
        hdResponse.setData(null);
        hdResponse.setCode(HdResponseErrorStatusEnum.ERROR.getCode());
        hdResponse.setStatus(HdResponseErrorStatusEnum.ERROR.getStatus());
        hdResponse.setMessage(message);
        hdResponse.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return hdResponse;
    }

    public static <T> HdResponse<T> error(Integer code, String message) {
        return returnErrorResponse(code, message);
    }

    public static <T> HdResponse<T> okOrFail(T data, String message) {
        if (null == data) {
            return response(null, HdResponseErrorStatusEnum.FAIL, HdResponseErrorStatusEnum.FAIL.getMessage());
        }
        return response(data, HdResponseErrorStatusEnum.SUCCESS, message);
    }

    public static <T> HdResponse<T> okOrFail(T data) {
        if (null == data) {
            return response(null, HdResponseErrorStatusEnum.FAIL);
        }
        return response(null, HdResponseErrorStatusEnum.SUCCESS);
    }

    public static <T> HdResponse<T> okOrFail(boolean bool) {
        if (!bool) {
            return response(null, HdResponseErrorStatusEnum.FAIL);
        }
        return response(null, HdResponseErrorStatusEnum.SUCCESS);
    }

    private static <T> HdResponse<T> returnResponse(HdBaseErrorStatusEnum hdBaseErrorStatusEnum) {
        HdResponse<T> hdResponse = HdResponse.instance();
        hdResponse.setData(null);
        hdResponse.setCode(hdBaseErrorStatusEnum.getCode());
        hdResponse.setStatus(hdBaseErrorStatusEnum.getStatus());
        hdResponse.setMessage(hdBaseErrorStatusEnum.getMessage());
        hdResponse.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return hdResponse;
    }

    private static <T> HdResponse<T> returnResponse(HdBaseErrorStatusEnum hdBaseErrorStatusEnum, String message) {
        HdResponse<T> hdResponse = HdResponse.instance();
        hdResponse.setData(null);
        hdResponse.setCode(hdBaseErrorStatusEnum.getCode());
        hdResponse.setStatus(hdBaseErrorStatusEnum.getStatus());
        hdResponse.setMessage(message);
        hdResponse.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return hdResponse;
    }

    private static <T> HdResponse<T> returnOKResponse(Integer code, String message) {
        HdResponse<T> hdResponse = HdResponse.instance();
        hdResponse.setData(null);
        hdResponse.setCode(code);
        hdResponse.setStatus(HdResponseErrorStatusEnum.SUCCESS.getStatus());
        hdResponse.setMessage(message);
        hdResponse.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return hdResponse;
    }

    private static <T> HdResponse<T> returnFailResponse(Integer code, String message) {
        HdResponse<T> hdResponse = HdResponse.instance();
        hdResponse.setData(null);
        hdResponse.setCode(code);
        hdResponse.setStatus(HdResponseErrorStatusEnum.FAIL.getStatus());
        hdResponse.setMessage(message);
        hdResponse.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return hdResponse;
    }

    private static <T> HdResponse<T> returnErrorResponse(Integer code, String message) {
        HdResponse<T> hdResponse = HdResponse.instance();
        hdResponse.setData(null);
        hdResponse.setCode(code);
        hdResponse.setStatus(HdResponseErrorStatusEnum.ERROR.getStatus());
        hdResponse.setMessage(message);
        hdResponse.setTimestamp(Instant.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return hdResponse;
    }
}
