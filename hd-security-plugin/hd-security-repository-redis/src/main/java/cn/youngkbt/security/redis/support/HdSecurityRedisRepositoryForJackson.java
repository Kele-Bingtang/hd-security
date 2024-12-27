package cn.youngkbt.security.redis.support;

import cn.youngkbt.hdsecurity.utils.DatePattern;
import cn.youngkbt.security.redis.HdSecurityRedisRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Hd Security Redis 持久层，Jackson 序列化
 *
 * @author Tianke
 * @date 2024/12/28 01:12:28
 * @since 1.0.0
 */
public class HdSecurityRedisRepositoryForJackson extends HdSecurityRedisRepository {

    public HdSecurityRedisRepositoryForJackson(RedisConnectionFactory redisConnectionFactory) {
        super(redisConnectionFactory);
    }

    @Override
    public RedisTemplate<String, Object> init(RedisConnectionFactory redisConnectionFactory) {
        // 如果已经初始化，则不再重复初始化
        if (super.isInit()) {
            return super.getRedisTemplate();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        // 配置忽略未知字段
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 解决 Redis 无法存入 LocalDateTime 等 JDK8 的时间类
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        // 新增 LocalDateTime 序列化、反序列化规则
        javaTimeModule
                // LocalDateTime 序列化与反序列化：yyyy-MM-dd HH:mm:ss
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DatePattern.NORM_DATETIME_FORMATTER))
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DatePattern.NORM_DATETIME_FORMATTER))
                // LocalDate 序列化与反序列化：yyyy-MM-dd
                .addSerializer(new LocalDateSerializer(DatePattern.NORM_DATE_FORMATTER))
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(DatePattern.NORM_DATE_FORMATTER))
                // LocalTime 序列化与反序列化：HH:mm:ss
                .addSerializer(LocalTime.class, new LocalTimeSerializer(DatePattern.NORM_TIME_FORMATTER))
                .addDeserializer(LocalTime.class, new LocalTimeDeserializer(DatePattern.NORM_TIME_FORMATTER))
                // Instant 序列化与反序列化
                .addSerializer(Instant.class, InstantSerializer.INSTANCE)
                .addDeserializer(Instant.class, InstantDeserializer.INSTANT);

        objectMapper.registerModules(javaTimeModule);

        // 使用 Jackson2JsonRedisSerialize 序列化
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 设置 value 的序列化规则和 key 的序列化规则
        return getStringObjectRedisTemplate(redisConnectionFactory, jackson2JsonRedisSerializer);
    }

    private RedisTemplate<String, Object> getStringObjectRedisTemplate(RedisConnectionFactory redisConnectionFactory, Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
