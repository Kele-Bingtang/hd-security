package cn.youngkbt.hdsecurity.error;

/**
 * 基础异常状态码枚举接口，通过实现该接口，自定义异常状态码
 *
 * @author Tianke
 * @date 2024/11/25 00:38:27
 * @since 1.0.0
 */
public interface HdBaseErrorStatusEnum {
    Integer getCode();

    String getStatus();

    String getMessage();
}
