package cn.youngkbt.hdsecurity.utils;

import java.util.*;
import java.util.function.Function;

/**
 * Hd Security Collection 工具类
 *
 * @author Tianke
 * @date 2024/11/25 21:19:59
 * @since 1.0.0
 */
public class HdCollectionUtil {

    private HdCollectionUtil() {
    }

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static <T> boolean isEmpty(Collection<T> list) {
        return list == null || list.isEmpty();
    }

    public static <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }

    public static <T> boolean isNotEmpty(Collection<T> list) {
        return !isEmpty(list);
    }

    public static <T> boolean isEmptyWithElement(Collection<T> list) {
        if (isEmpty(list)) {
            return true;
        } else {
            return elementIsEmpty(list);
        }
    }

    public static <T> boolean elementIsEmpty(Collection<T> list) {
        return list.stream().allMatch(Objects::isNull);
    }

    public static <T> boolean elementIsNotEmpty(Collection<T> list) {
        return !elementIsEmpty(list);
    }

    public static boolean isArray(Object obj) {
        return null != obj && obj.getClass().isArray();
    }

    public static <T> List<T> newArrayList() {
        return new ArrayList<>();
    }

    public static <T> List<T> newArrayList(T... objects) {
        return Arrays.asList(objects);
    }

    public static <T> List<T> newArrayList(List<T> useList, Function<T, T> function) {
        List<T> list = newArrayList();
        useList.forEach(l -> {
            T apply = function.apply(l);
            if (Objects.nonNull(apply)) {
                list.add(apply);
            }
        });
        return list;
    }

    public static <T, V> List<V> newArrayList(List<T> useList, Function<T, V> function, Class<V> clazz) {
        List<V> list = newArrayList();
        useList.forEach(l -> list.add(function.apply(l)));
        return list;
    }

    /**
     * 判断集合中是否包含指定元素（模糊匹配，支持 * 匹配）
     *
     * @param strList 集合
     * @param element 元素
     * @return 集合中是否包含指定元素
     */
    public static boolean vagueMatchElement(Collection<String> strList, String element) {
        // 空集合直接返回 false
        if (isEmpty(strList)) {
            return false;
        }

        // 先尝试一下简单匹配，如果可以匹配成功则无需继续模糊匹配
        if (strList.contains(element)) {
            return true;
        }

        // 开始模糊匹配
        for (String pattern : strList) {
            if (HdStringUtil.vagueMatch(pattern, element)) {
                return true;
            }
        }

        // 走出 for 循环说明没有一个元素可以匹配成功
        return false;
    }
}
