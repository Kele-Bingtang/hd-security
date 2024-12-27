package cn.youngkbt.security.redis.support;

import cn.youngkbt.security.redis.HdSecurityRedisRepository;
import cn.youngkbt.security.redis.serializer.FastJson2RedisSerializer;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Hd Security Redis 持久层，Fastjson2 序列化
 *
 * @author Tianke
 * @date 2024/12/28 02:46:14
 * @since 1.0.0
 */
public class HdSecurityRedisRepositoryForFastjson2 extends HdSecurityRedisRepository {

    public HdSecurityRedisRepositoryForFastjson2(RedisConnectionFactory redisConnectionFactory) {
        super(redisConnectionFactory);
    }

    @Override
    public RedisTemplate<String, Object> init(RedisConnectionFactory redisConnectionFactory) {
        // 如果已经初始化，则不再重复初始化
        if (super.isInit()) {
            return super.getRedisTemplate();
        }

        FastJson2RedisSerializer<Object> fastJson2RedisSerializer = new FastJson2RedisSerializer<>(Object.class);

        // 构建RedisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        // 指定相应的序列化方案 
        StringRedisSerializer keySerializer = new StringRedisSerializer();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setHashKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(fastJson2RedisSerializer);
        redisTemplate.setHashValueSerializer(fastJson2RedisSerializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}
