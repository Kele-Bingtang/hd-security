package cn.youngkbt.security.redis.enums;

import cn.youngkbt.security.redis.HdSecurityRedisRepository;
import cn.youngkbt.security.redis.support.HdSecurityRedisRepositoryForFastjson;
import cn.youngkbt.security.redis.support.HdSecurityRedisRepositoryForFastjson2;
import cn.youngkbt.security.redis.support.HdSecurityRedisRepositoryForJackson;
import cn.youngkbt.security.redis.support.HdSecurityRedisRepositoryForJdk;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.function.Function;

/**
 * Hd Security Redis 持久层支持序列化器的枚举类
 *
 * @author Tianke
 * @date 2024/12/28 02:59:35
 * @since 1.0.0
 */
public enum HdSecuritySerializerType {

    /**
     * 因为在 application 文件配置时，语法提示读取的内容是 name()，所以改为小写阅读比较好
     */
    jdk(HdSecurityRedisRepositoryForJdk::new),
    jackson(HdSecurityRedisRepositoryForJackson::new),
    fastjson(HdSecurityRedisRepositoryForFastjson::new),
    fastjson2(HdSecurityRedisRepositoryForFastjson2::new),

    ;

    private final Function<RedisConnectionFactory, HdSecurityRedisRepository> repositoryFunction;

    HdSecuritySerializerType(Function<RedisConnectionFactory, HdSecurityRedisRepository> repositoryFunction) {
        this.repositoryFunction = repositoryFunction;
    }

    public Function<RedisConnectionFactory, HdSecurityRedisRepository> getRepositoryFunction() {
        return repositoryFunction;
    }
}
