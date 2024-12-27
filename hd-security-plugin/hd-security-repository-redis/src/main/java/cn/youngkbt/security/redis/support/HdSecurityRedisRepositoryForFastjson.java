package cn.youngkbt.security.redis.support;

import cn.youngkbt.security.redis.HdSecurityRedisRepository;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Hd Security Redis 持久层，Fastjson 序列化
 *
 * @author Tianke
 * @date 2024/12/28 02:04:56
 * @since 1.0.0
 */
public class HdSecurityRedisRepositoryForFastjson extends HdSecurityRedisRepository {

    public HdSecurityRedisRepositoryForFastjson(RedisConnectionFactory redisConnectionFactory) {
        super(redisConnectionFactory);
    }

    @Override
    public RedisTemplate<String, Object> init(RedisConnectionFactory redisConnectionFactory) {
        // 如果已经初始化，则不再重复初始化
        if (super.isInit()) {
            return super.getRedisTemplate();
        }

        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);

        // 构建RedisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        // 指定相应的序列化方案 
        StringRedisSerializer keySerializer = new StringRedisSerializer();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setHashKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(fastJsonRedisSerializer);
        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}
