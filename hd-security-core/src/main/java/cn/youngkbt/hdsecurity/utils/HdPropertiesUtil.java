package cn.youngkbt.hdsecurity.utils;

import cn.youngkbt.hdsecurity.jwt.error.HdSecurityErrorCode;
import cn.youngkbt.hdsecurity.exception.HdSecurityConfigException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 从 resource 目录下的 properties 文件中读取配置信息
 *
 * @author Tianke
 * @date 2024/11/25 23:27:29
 * @since 1.0.0
 */
public class HdPropertiesUtil {
    // 私有构造函数，防止实例化
    private HdPropertiesUtil() {
    }

    /**
     * 从指定路径读取配置文件
     *
     * @param path 配置文件路径
     * @return 配置文件对象
     */
    public static Properties readerProperties(String path) {
        Properties properties = new Properties();
        try (InputStream inputStream = HdPropertiesUtil.class.getClassLoader().getResourceAsStream(path)) {

            if (inputStream == null) {
                throw new IOException();
            }
            properties.load(inputStream);
        } catch (IOException e) {
            return null;
        }
        return properties;
    }

    /**
     * 从指定路径读取配置文件，然后转换为指定的类对象
     *
     * @param path  资源路径
     * @param clazz 目标类
     * @param <T>   目标类的泛型
     * @return 转换后的对象
     */
    public static <T> T readerThenConvert(String path, Class<T> clazz) {
        Properties properties = readerProperties(path);
        if (null == properties) {
            return null;
        }
        return readerThenConvert(properties, clazz);
    }

    /**
     * 将 Properties 转换为指定的类对象
     *
     * @param properties 配置属性
     * @param clazz      目标类
     * @param <T>        目标类的泛型
     * @return 转换后的对象
     */
    public static <T> T readerThenConvert(Properties properties, Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                String key = field.getName();
                String value = properties.getProperty(key);
                if (null != value) {
                    Object convertedValue = convertValue(value, field.getType());
                    field.set(instance, convertedValue);
                }
            }
            return instance;
        } catch (Exception e) {
            throw new HdSecurityConfigException("类实例化或属性赋值出错", e).setCode(HdSecurityErrorCode.CONFIG_PROPERTY_READ_FAIL);
        }
    }

    /**
     * 将指定路径的 Properties 文件转换为 Map
     *
     * @param path 资源路径
     * @return 转换后的 Map
     */
    public static Map<String, Object> readerThenConvertToMap(String path) {
        Properties properties = readerProperties(path);
        if (null == properties) {
            return Collections.emptyMap();
        }
        return readerThenConvertToMap(properties);
    }

    /**
     * 将 Properties 转换为 Map
     *
     * @param properties 配置属性
     * @return 转换后的 Map
     */
    public static Map<String, Object> readerThenConvertToMap(Properties properties) {
        Map<String, Object> map = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            // 默认转换为 String
            Object convertedValue = convertValue(value, String.class);
            map.put(key, convertedValue);
        }
        return map;
    }

    /**
     * 将字符串值转换为目标类型
     *
     * @param value 字符串值
     * @param type  目标类型
     * @return 转换后的值
     */
    private static Object convertValue(String value, Class<?> type) {
        if (type.equals(String.class)) {
            return value;
        } else if (type.equals(int.class) || type.equals(Integer.class)) {
            return Integer.parseInt(value);
        } else if (type.equals(long.class) || type.equals(Long.class)) {
            return Long.parseLong(value);
        } else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            return Boolean.parseBoolean(value);
        } else if (type.equals(double.class) || type.equals(Double.class)) {
            return Double.parseDouble(value);
        } else if (type.equals(float.class) || type.equals(Float.class)) {
            return Float.parseFloat(value);
        } else if (type.equals(short.class) || type.equals(Short.class)) {
            return Short.parseShort(value);
        } else if (type.equals(char.class) || type.equals(Character.class)) {
            if (value.length() != 1) {
                throw new IllegalArgumentException("违法的参数值: " + value);
            }
            return value.charAt(0);
        } else {
            throw new IllegalArgumentException("不支持的类型：" + type.getName());
        }
    }
}
