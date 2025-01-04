package cn.youngkbt.hdsecurity.exception;

/**
 * Hd Security 配置异常，用于在读取配置文件错误时抛出
 * @author Tianke
 * @date 2024/11/25 23:29:31
 * @since 1.0.0
 */
public class HdSecurityConfigException extends HdSecurityException {
    public HdSecurityConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
