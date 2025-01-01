package cn.youngkbt.security.redis;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.repository.HdSecurityRepository;
import cn.youngkbt.hdsecurity.repository.HdSecurityRepositoryKV;
import cn.youngkbt.hdsecurity.utils.HdStringUtil;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Hd Security Redis 持久层 API 基本实现类，该类没有实现 Redis 序列化器，需要子类实现序列化器
 *
 * @author Tianke
 * @date 2024/12/28 01:12:58
 * @since 1.0.0
 */
public abstract class HdSecurityRedisRepository implements HdSecurityRepository {
    
    private final RedisTemplate<String, Object> redisTemplate;

    private boolean isInit = false;

    public abstract RedisTemplate<String, Object> init(RedisConnectionFactory redisConnectionFactory);

    public HdSecurityRedisRepository(RedisConnectionFactory redisConnectionFactory) {
        redisTemplate = init(redisConnectionFactory);
        isInit = true;
    }

    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public boolean isInit() {
        return isInit;
    }

    @Override
    public Object query(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void add(String key, Object value, long expireTime) {
        // 验证 expireTime
        if (0 == expireTime || expireTime < HdSecurityRepositoryKV.NOT_VALUE_EXPIRE) {
            return;
        }
        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
        if (expireTime == HdSecurityRepositoryKV.NEVER_EXPIRE) {
            opsForValue.set(key, value);
        } else {
            opsForValue.set(key, value, expireTime, TimeUnit.SECONDS);
        }
    }

    @Override
    public void edit(String key, Object value) {
        long expireTime = getExpireTime(key);
        if (expireTime == HdSecurityRepositoryKV.NOT_VALUE_EXPIRE) {
            return;
        }

        add(key, value, expireTime);
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void clear() {
        redisTemplate.delete(keys(HdSecurityManager.getConfig().getSecurityPrefixKey() + "*"));
    }

    @Override
    public long getExpireTime(String key) {
        if (null != redisTemplate) {
            return Optional.ofNullable(redisTemplate.getExpire(key)).orElse(HdSecurityRepositoryKV.NOT_VALUE_EXPIRE);
        }
        return HdSecurityRepositoryKV.NOT_VALUE_EXPIRE;
    }

    @Override
    public void updateExpireTime(String key, long expireTime) {
        // 如果过期时间设置为永久，且当前 key 不是永久有效，则更新为永久有效
        if (expireTime == HdSecurityRepositoryKV.NEVER_EXPIRE && getExpireTime(key) != HdSecurityRepositoryKV.NEVER_EXPIRE) {
            add(key, query(key), expireTime);
            return;
        }

        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }

    @Override
    public Set<String> keys(String keyword) {
        if (HdStringUtil.hasEmpty(keyword)) {
            return redisTemplate.keys("*");
        }

        return redisTemplate.keys(keyword);
    }
}
