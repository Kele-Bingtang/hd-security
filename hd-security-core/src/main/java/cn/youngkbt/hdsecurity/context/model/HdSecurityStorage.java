package cn.youngkbt.hdsecurity.context.model;

/**
 * Hd Security Storage 请求作用域对象包装类
 * @author Tianke
 * @date 2024/11/30 15:25:10
 * @since 1.0.0
 */
public interface HdSecurityStorage {
    /**
     * 获取底层被包装的源对象
     *
     * @return 被包装的源对象
     */
    Object getSource();

    Object get(String key);

    HdSecurityStorage set(String key, Object value);

    HdSecurityStorage remove(String key);
}
