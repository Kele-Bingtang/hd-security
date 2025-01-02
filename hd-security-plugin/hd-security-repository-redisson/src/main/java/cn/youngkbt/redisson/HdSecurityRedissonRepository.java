package cn.youngkbt.redisson;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.repository.HdSecurityRepository;
import cn.youngkbt.hdsecurity.repository.HdSecurityRepositoryKV;
import cn.youngkbt.hdsecurity.utils.HdStringUtil;
import org.redisson.api.*;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

/**
 * Hd Security Redisson 持久层 API 实现类
 *
 * @author Tianke
 * @date 2025/1/3 00:33:19
 * @since 1.0.0
 */
public class HdSecurityRedissonRepository implements HdSecurityRepository {
    /**
     * redisson 客户端
     */
    public RedissonClient redissonClient;

    private boolean isInit = false;

    public HdSecurityRedissonRepository(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
        this.isInit = true;
    }

    public boolean isInit() {
        return isInit;
    }

    @Override
    public Object query(String key) {
        return redissonClient.getBucket(key).get();
    }

    @Override
    public void add(String key, Object value, long expireTime) {
        if (expireTime == 0 || expireTime <= HdSecurityRepositoryKV.NOT_VALUE_EXPIRE) {
            return;
        }

        if (expireTime == HdSecurityRepositoryKV.NEVER_EXPIRE) {
            redissonClient.getBucket(key).set(value);
        } else {
            RBatch batch = redissonClient.createBatch();
            RBucketAsync<Object> bucket = batch.getBucket(key);
            bucket.setAsync(value);
            bucket.expireAsync(Duration.ofSeconds(expireTime));
            batch.execute();
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
        redissonClient.getBucket(key).delete();
    }

    @Override
    public void clear() {
        RKeys keys = redissonClient.getKeys();
        Iterable<String> keysByPattern = keys.getKeysByPattern(HdSecurityManager.getConfig().getSecurityPrefixKey() + "*");
        for (String s : keysByPattern) {
            redissonClient.getBucket(s).delete();
        }
    }

    @Override
    public long getExpireTime(String key) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        long remainTimeToLive = bucket.remainTimeToLive();
        return 0 < remainTimeToLive ? remainTimeToLive : remainTimeToLive / 1000;
    }

    @Override
    public void updateExpireTime(String key, long expireTime) {
        // 如果过期时间设置为永久，且当前 key 不是永久有效，则更新为永久有效
        if (expireTime == HdSecurityRepositoryKV.NEVER_EXPIRE && getExpireTime(key) != HdSecurityRepositoryKV.NEVER_EXPIRE) {
            add(key, query(key), expireTime);
            return;
        }

        redissonClient.getBucket(key).expire(Duration.ofSeconds(expireTime));
    }

    @Override
    public Set<String> keys(String keyword) {
        RKeys keys = redissonClient.getKeys();
        if (HdStringUtil.hasEmpty(keyword)) {
            keyword = "*";
        }

        Iterable<String> keySet = keys.getKeysByPattern(keyword);
        Set<String> set = new HashSet<>();
        for (String k : keySet) {
            set.add(k);
        }

        return set;
    }
}
