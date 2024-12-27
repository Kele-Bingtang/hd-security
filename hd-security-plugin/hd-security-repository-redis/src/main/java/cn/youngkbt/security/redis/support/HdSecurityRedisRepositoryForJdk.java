package cn.youngkbt.security.redis.support;

import cn.youngkbt.security.redis.HdSecurityRedisRepository;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Hd Security Redis 持久层，JDK 序列化
 *
 * @author Tianke
 * @date 2024/12/28 00:24:50
 * @since 1.0.0
 */
public class HdSecurityRedisRepositoryForJdk extends HdSecurityRedisRepository {

    public HdSecurityRedisRepositoryForJdk(RedisConnectionFactory redisConnectionFactory) {
        super(redisConnectionFactory);
    }

    @Override
    public RedisTemplate<String, Object> init(RedisConnectionFactory redisConnectionFactory) {
        // 如果已经初始化，则不再重复初始化
        if (super.isInit()) {
            return super.getRedisTemplate();
        }

        // 构建 RedisTemplate
        return getRedisTemplate(redisConnectionFactory);
    }

    private RedisTemplate<String, Object> getRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplateNew = new RedisTemplate<>();

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();

        // 设置 value 的序列化规则和 key 的序列化规则
        redisTemplateNew.setConnectionFactory(redisConnectionFactory);
        redisTemplateNew.setKeySerializer(stringRedisSerializer);
        redisTemplateNew.setHashKeySerializer(stringRedisSerializer);
        redisTemplateNew.setValueSerializer(jdkSerializationRedisSerializer);
        redisTemplateNew.setHashValueSerializer(jdkSerializationRedisSerializer);
        redisTemplateNew.afterPropertiesSet();

        return redisTemplateNew;
    }

}
