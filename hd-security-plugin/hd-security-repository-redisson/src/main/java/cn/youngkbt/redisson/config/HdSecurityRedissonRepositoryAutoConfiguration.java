package cn.youngkbt.redisson.config;

import cn.youngkbt.redisson.HdSecurityRedissonRepository;
import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;

/**
 * Hd Security Redisson 自动装配类
 * 
 * @author Tianke
 * @date 2025/1/3 01:01:30
 * @since 1.0.0
 */
@AutoConfiguration
@AutoConfigureAfter(RedissonAutoConfiguration.class)
public class HdSecurityRedissonRepositoryAutoConfiguration {
    
    @Bean
    public HdSecurityRedissonRepository hdSecurityRedissonRepository(RedissonClient redissonClient) {
        return new HdSecurityRedissonRepository(redissonClient);
    }
}
