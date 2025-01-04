package cn.youngkbt.hdsecurity.utils;

/**
 * Hd Security 对象工具类
 *
 * @author Tianke
 * @date 2024/11/27 23:25:23
 * @since 1.0.0
 */
public class HdObjectUtil {
    private HdObjectUtil() {
    }

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

    /**
     * 将指定值转化为指定类型
     *
     * @param <T> 泛型
     * @param obj 值
     * @param cls 类型
     * @return 转换后的值
     */
    public static <T> T convertObject(Object obj, Class<T> cls) {
        // 如果 obj 为 null 或者本来就是 cls 类型
        if (obj == null || obj.getClass().equals(cls)) {
            return (T) obj;
        }
        // 开始转换
        String obj2 = String.valueOf(obj);
        Object obj3;
        if (cls.equals(String.class)) {
            obj3 = obj2;
        } else if (cls.equals(int.class) || cls.equals(Integer.class)) {
            obj3 = Integer.valueOf(obj2);
        } else if (cls.equals(long.class) || cls.equals(Long.class)) {
            obj3 = Long.valueOf(obj2);
        } else if (cls.equals(short.class) || cls.equals(Short.class)) {
            obj3 = Short.valueOf(obj2);
        } else if (cls.equals(byte.class) || cls.equals(Byte.class)) {
            obj3 = Byte.valueOf(obj2);
        } else if (cls.equals(float.class) || cls.equals(Float.class)) {
            obj3 = Float.valueOf(obj2);
        } else if (cls.equals(double.class) || cls.equals(Double.class)) {
            obj3 = Double.valueOf(obj2);
        } else if (cls.equals(boolean.class) || cls.equals(Boolean.class)) {
            obj3 = Boolean.valueOf(obj2);
        } else if (cls.equals(char.class) || cls.equals(Character.class)) {
            obj3 = obj2.charAt(0);
        } else {
            obj3 = obj;
        }
        return (T) obj3;
    }
}
