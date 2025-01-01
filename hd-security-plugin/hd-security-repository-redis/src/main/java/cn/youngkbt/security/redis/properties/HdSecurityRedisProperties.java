package cn.youngkbt.security.redis.properties;

import cn.youngkbt.security.redis.config.HdSecurityRedisRepositoryAutoConfiguration;
import cn.youngkbt.security.redis.enums.HdSecuritySerializerType;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Hd Security Redis 持久层的配置文件
 *
 * @author Tianke
 * @date 2024/12/28 02:52:36
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = HdSecurityRedisRepositoryAutoConfiguration.REDIS_PREFIX)
public class HdSecurityRedisProperties {
    /**
     * Redis 序列化器
     */
    private HdSecuritySerializerType serializer = HdSecuritySerializerType.jdk;

    public HdSecuritySerializerType getSerializer() {
        return serializer;
    }

    public HdSecurityRedisProperties setSerializer(HdSecuritySerializerType serializer) {
        this.serializer = serializer;
        return this;
    }
}
