package cn.youngkbt.hdsecurity.utils;

/**
 * @author Tianke
 * @date 2024/11/27 23:25:23
 * @since 1.0.0
 */
public class ObjectUtil {
    /**
     * 判断类型是否为 8 大包装类型
     *
     * @param cs 类型
     * @return 是否为 8 大包装类型
     */
    public static boolean isWrapperType(Class<?> cs) {
        return cs == Integer.class || cs == Short.class || cs == Long.class || cs == Byte.class
                || cs == Float.class || cs == Double.class || cs == Boolean.class || cs == Character.class;
    }

    /**
     * 判断类型是否为基础类型：8大基本数据类型、8大包装类、String
     *
     * @param cs 类型
     * @return 是否为基础类型
     */
    public static boolean isBasicType(Class<?> cs) {
        return cs.isPrimitive() || isWrapperType(cs) || cs == String.class;
    }
    
}
