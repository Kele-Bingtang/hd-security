package cn.youngkbt.security.redis.serializer;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * FastJson2 的 Redis 序列化器封装
 *
 * @author Tianke
 * @date 2024/12/28 02:32:54
 * @since 1.0.0
 */
public class FastJson2RedisSerializer<T> implements RedisSerializer<T> {

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private final Class<T> clazz;

    private FastJsonConfig fastJsonConfig = new FastJsonConfig();

    public FastJson2RedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
        // 配置日期格式
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        // 配置是否包含类型信息
        fastJsonConfig.setWriterFeatures(JSONWriter.Feature.WriteClassName);
    }

    public FastJsonConfig getFastJsonConfig() {
        return fastJsonConfig;
    }

    public FastJson2RedisSerializer<T> setFastJsonConfig(FastJsonConfig fastJsonConfig) {
        this.fastJsonConfig = fastJsonConfig;
        return this;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        return JSON.toJSONString(t, fastJsonConfig.getWriterFeatures())
                .getBytes(DEFAULT_CHARSET);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);
        return JSON.parseObject(str, clazz, fastJsonConfig.getReaderFeatures());
    }
}