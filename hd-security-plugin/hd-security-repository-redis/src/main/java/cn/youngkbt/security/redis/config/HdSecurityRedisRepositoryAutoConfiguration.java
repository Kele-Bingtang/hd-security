package cn.youngkbt.security.redis.config;

import cn.youngkbt.hdsecurity.exception.HdSecurityException;
import cn.youngkbt.security.redis.HdSecurityRedisRepository;
import cn.youngkbt.security.redis.enums.HdSecuritySerializerType;
import cn.youngkbt.security.redis.error.HdSecurityRedisErrorCode;
import cn.youngkbt.security.redis.properties.HdSecurityRedisProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * @author Tianke
 * @date 2024/12/28 00:22:30
 * @since 1.0.0
 */
@AutoConfiguration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(HdSecurityRedisProperties.class)
public class HdSecurityRedisRepositoryAutoConfiguration {

    @Bean
    public HdSecurityRedisRepository hdSecurityRedisRepository(RedisConnectionFactory redisConnectionFactory, HdSecurityRedisProperties hdSecurityRedisProperties) {
        HdSecuritySerializerType serializer = hdSecurityRedisProperties.getSerializer();

        if (null == serializer) {
            throw new HdSecurityException("不支持该 Redis 序列化器：").setCode(HdSecurityRedisErrorCode.REDIS_SERIALIZER_NOT_SUPPORT);
        }

        return serializer.getRepositoryFunction().apply(redisConnectionFactory);
    }
}
