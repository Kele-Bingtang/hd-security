package cn.youngkbt.security.redis.error;

/**
 * Hd Security Redis 持久层错误码
 *
 * @author Tianke
 * @date 2024/12/28 03:03:37
 * @since 1.0.0
 */
public interface HdSecurityRedisErrorCode {
    /**
     * Redis 序列化器不支持
     */
    int REDIS_SERIALIZER_NOT_SUPPORT = 30001;
}
