package cn.youngkbt.hdsecurity.repository;

import java.util.List;

/**
 * <p>Hd Security 持久层</p>
 * <p>实现此接口可以拓展自己的持久层，将数据存储到自定义的位置，如数据库、Redis 缓存等</p>
 *
 * @author Tianke
 * @date 2024/11/25 01:16:06
 * @since 1.0.0
 */
public interface HdSecurityRepositoryKV<K, V> {
    /**
     * 常量，表示一个 key 永不过期 （在一个 key 被标注为永远不过期时返回此值）
     */
    long NEVER_EXPIRE = -1;

    /**
     * 常量，表示持久层中不存在数据（key 过期或者过期时间为空时返回此值）
     */
    long NOT_VALUE_EXPIRE = -2;

    /**
     * 通过 key 查询持久层中的数据
     *
     * @param key 键名称
     * @return 持久层中的数据或 null
     */
    V query(K key);

    /**
     * 添加数据到持久层中，并指定过期时间
     *
     * @param key        键名称
     * @param value      值
     * @param expireTime 过期时间，单位：秒
     */
    void add(K key, V value, long expireTime);

    /**
     * 修改持久层中的数据，不会更新过期时间
     *
     * @param key   键名称
     * @param value 新值
     */
    void edit(K key, V value);

    /**
     * 删除持久层中的数据
     *
     * @param key 键名称
     */
    void remove(K key);

    /**
     * 清空持久层的所有数据
     */
    void clear();

    /**
     * 获取 key 的过期时间，单位：秒
     *
     * @param key 键名称
     * @return 过期时间，单位：秒，如果永久不过期，则返回 {@link #NEVER_EXPIRE}，如果 key 不存在，则返回 {@link #NOT_VALUE_EXPIRE}
     */
    long getExpireTime(K key);

    /**
     * 更新 key 的过期时间，单位：秒
     *
     * @param key        键名称
     * @param expireTime 过期时间，单位：秒
     */
    void updateExpireTime(K key, long expireTime);

    /**
     * 获取指定 prefix 且包含 keyword 的 Key 列表
     *
     * @param prefix   前缀
     * @param keyword  关键词，在 Key 列表里进行模糊匹配
     * @param start    开始处索引
     * @param size     获取数量  (-1 代表从 start 处一直取到末尾)
     * @param sortType 排序类型（true 正序，false 反序）
     * @return Key 列表
     */
    List<String> searchKeyList(String prefix, String keyword, int start, int size, boolean sortType);

    /**
     * 当仓库实例被装载时触发
     */
    default void init() {
    }

    /**
     * 当仓库实例被卸载时触发
     */
    default void destroy() {
    }
}
