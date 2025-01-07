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
     * 从集合里查询数据
     *
     * @param dataList 数据集合
     * @param prefix   前缀
     * @param keyword  关键字
     * @param start    起始位置 (-1 代表查询所有)
     * @param size     获取条数
     * @param sortType 排序类型（true 正序，false 反序）
     * @return 符合条件的新数据集合
     */
    public static List<String> searchList(Collection<String> dataList, String prefix, String keyword, int start, int size, boolean sortType) {
        if (prefix == null) {
            prefix = "";
        }
        if (keyword == null) {
            keyword = "";
        }
        // 挑选出所有符合条件的
        List<String> list = new ArrayList<>();
        for (String key : dataList) {
            if (key.startsWith(prefix) && key.contains(keyword)) {
                list.add(key);
            }
        }
        // 取指定段数据
        return substrList(list, start, size, sortType);
    }

    /**
     * 从集合里查询数据
     *
     * @param list     数据集合
     * @param start    起始位置
     * @param size     获取条数 (-1 代表从 start 处一直取到末尾)
     * @param sortType 排序类型（true 正序，false 反序）
     * @return 符合条件的新数据集合
     */
    public static List<String> substrList(List<String> list, int start, int size, boolean sortType) {
        // 如果是反序的话
        if (!sortType) {
            Collections.reverse(list);
        }
        // start 至少为 0
        if (start < 0) {
            start = 0;
        }
        // size 为 -1 时，代表一直取到末尾，否则取到 start + size
        int end = size == -1 ? list.size() : start + size;
        // 取出的数据放到新集合中
        List<String> list2 = new ArrayList<>();
        for (int i = start; i < end; i++) {
            // 如果已经取到 list 的末尾，则直接退出
            if (i >= list.size()) {
                return list2;
            }
            list2.add(list.get(i));
        }
        return list2;
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
