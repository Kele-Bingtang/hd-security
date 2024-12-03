package cn.youngkbt.hdsecurity.repository;

import java.util.Set;

/**
 * Hd Security 持久层
 * 实现此接口可以拓展自己的持久层，将数据存储到自定义的位置，如数据库、Redis 缓存等
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
     * 常量，表示缓存中不存在数据（获取剩余存活时间为空时返回此值）
     */
    long NOT_VALUE_EXPIRE = -2;

    V query(K key);

    void add(K key, V value, long expireTime);

    void edit(K key, V value);

    void remove(K key);
    
    void clear();

    long getExpireTime(K key);

    void updateExpireTime(K key, long expireTime);

    Set<K> keys(String keyword);
    
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
