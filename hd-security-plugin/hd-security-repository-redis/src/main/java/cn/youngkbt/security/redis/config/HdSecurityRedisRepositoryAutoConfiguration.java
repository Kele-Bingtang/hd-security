package cn.youngkbt.security.redis.config;

import cn.youngkbt.hdsecurity.exception.HdSecurityException;
import cn.youngkbt.security.redis.HdSecurityRedisRepository;
import cn.youngkbt.security.redis.enums.HdSecuritySerializerType;
import cn.youngkbt.security.redis.error.HdSecurityRedisErrorCode;
import cn.youngkbt.security.redis.properties.HdSecurityRedisProperties;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Hd Security Redis 自动装配类
 *
 * @author Tianke
 * @date 2024/12/28 00:22:30
 * @since 1.0.0
 */
@AutoConfiguration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(HdSecurityRedisProperties.class)
public class HdSecurityRedisRepositoryAutoConfiguration {

    /**
     * 配置信息的前缀
     */
    public static final String REDIS_PREFIX = "hd-security.redis";

    @Bean
    public HdSecurityRedisRepository hdSecurityRedisRepository(RedisConnectionFactory redisConnectionFactory, HdSecurityRedisProperties hdSecurityRedisProperties, Environment environment) {
        HdSecuritySerializerType serializer = hdSecurityRedisProperties.getSerializer();

        if (null == serializer) {
            throw new HdSecurityException("不支持该 Redis 序列化器：").setCode(HdSecurityRedisErrorCode.REDIS_SERIALIZER_NOT_SUPPORT);
        }

        RedisConnectionFactory connectionFactory = getHdSecurityRedisConnectionFactory(environment);

        return serializer.getRepositoryFunction().apply(Optional.ofNullable(connectionFactory).orElse(redisConnectionFactory));
    }

    /**
     * 获取在配置文件 hd-security.redis 的 Redis 信息来创建 Redis 连接工厂（业务数据和 Hd Security 认证数据分离时需要进行配置）
     *
     * @param environment 环境变量
     * @return Redis 连接工厂
     */
    public RedisConnectionFactory getHdSecurityRedisConnectionFactory(Environment environment) {
        // 使用 Binder 获取 Redis 的配置信息（和 spring.redis 一样的配置在 hd-security.redis）
        RedisProperties redisProperties = Binder.get(environment).bind(REDIS_PREFIX, RedisProperties.class).get();
        // 获取 Redis 模式，默认为单例模式
        String pattern = environment.getProperty(REDIS_PREFIX + ".pattern", "single");

        switch (pattern) {
            case "single" -> {
                return getLettuceConnectionFactory(getSingleRedisConfiguration(redisProperties), redisProperties);
            }
            case "cluster" -> {
                return getLettuceConnectionFactory(getClusterRedisConfiguration(redisProperties), redisProperties);
            }
            case "sentinel" -> {
                return getLettuceConnectionFactory(getSentinelRedisConfiguration(redisProperties), redisProperties);
            }
            case "socket" -> {
                String socket = environment.getProperty(REDIS_PREFIX + ".socket", "");
                return getLettuceConnectionFactory(getSocketRedisConfiguration(redisProperties, socket), redisProperties);
            }
        }

        return null;
    }

    /**
     * 获取 Redis 单体模式的配置信息
     *
     * @param redisProperties 基础配置信息
     * @return Redis 单体模式的配置信息
     */
    public RedisConfiguration getSingleRedisConfiguration(RedisProperties redisProperties) {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(redisProperties.getHost());
        redisConfig.setPort(redisProperties.getPort());
        redisConfig.setDatabase(redisProperties.getDatabase());
        redisConfig.setPassword(RedisPassword.of(redisProperties.getPassword()));
        redisConfig.setDatabase(redisProperties.getDatabase());
        // 低版本没有 username 属性，捕获异常给个提示即可，无需退出程序
        try {
            redisConfig.setUsername(redisProperties.getUsername());
        } catch (NoSuchMethodError e) {
            System.err.println(e.getMessage());
        }
        return redisConfig;
    }

    /**
     * 获取 Redis 普通集群的配置信息
     *
     * @param redisProperties 基础配置信息
     * @return Redis 普通集群的配置信息
     */
    public RedisConfiguration getClusterRedisConfiguration(RedisProperties redisProperties) {
        RedisClusterConfiguration redisClusterConfig = new RedisClusterConfiguration();
        // 低版本没有 username 属性，捕获异常给个提示即可，无需退出程序
        try {
            redisClusterConfig.setUsername(redisProperties.getUsername());
        } catch (NoSuchMethodError e) {
            System.err.println(e.getMessage());
        }
        redisClusterConfig.setPassword(RedisPassword.of(redisProperties.getPassword()));

        RedisProperties.Cluster cluster = redisProperties.getCluster();
        List<RedisNode> serverList = cluster.getNodes().stream().map(node -> {
            String[] ipAndPort = node.split(":");
            return new RedisNode(ipAndPort[0].trim(), Integer.parseInt(ipAndPort[1]));
        }).collect(Collectors.toList());
        redisClusterConfig.setClusterNodes(serverList);
        redisClusterConfig.setMaxRedirects(cluster.getMaxRedirects());
        return redisClusterConfig;
    }

    /**
     * 获取 Redis 哨兵模式的配置信息
     *
     * @param redisProperties 基础配置信息
     * @return Redis 哨兵模式的配置信息
     */
    public RedisConfiguration getSentinelRedisConfiguration(RedisProperties redisProperties) {
        // 哨兵集群模式
        RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration();
        redisSentinelConfiguration.setDatabase(redisProperties.getDatabase());
        // 低版本没有 username 属性，捕获异常给个提示即可，无需退出程序
        try {
            redisSentinelConfiguration.setUsername(redisProperties.getUsername());
        } catch (NoSuchMethodError e) {
            System.err.println(e.getMessage());
        }
        redisSentinelConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));

        RedisProperties.Sentinel sentinel = redisProperties.getSentinel();
        redisSentinelConfiguration.setMaster(sentinel.getMaster());
        redisSentinelConfiguration.setSentinelPassword(sentinel.getPassword());
        List<RedisNode> serverList = sentinel.getNodes().stream().map(node -> {
            String[] ipAndPort = node.split(":");
            return new RedisNode(ipAndPort[0].trim(), Integer.parseInt(ipAndPort[1]));
        }).collect(Collectors.toList());
        redisSentinelConfiguration.setSentinels(serverList);

        return redisSentinelConfiguration;
    }

    /**
     * 获取 Redis Socket 连接模式的配置信息
     *
     * @param redisProperties 基础配置信息
     * @return Redis Socket 连接模式的配置信息
     */
    public RedisConfiguration getSocketRedisConfiguration(RedisProperties redisProperties, String socket) {
        // socket 连接单体 Redis
        RedisSocketConfiguration redisSocketConfiguration = new RedisSocketConfiguration();
        redisSocketConfiguration.setDatabase(redisProperties.getDatabase());
        // 低版本没有 username 属性，捕获异常给个提示即可，无需退出程序
        try {
            redisSocketConfiguration.setUsername(redisProperties.getUsername());
        } catch (NoSuchMethodError e) {
            System.err.println(e.getMessage());
        }
        redisSocketConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
        redisSocketConfiguration.setSocket(socket);

        return redisSocketConfiguration;
    }

    /**
     * 获取 Lettuce 连接工厂
     *
     * @param redisConfiguration Redis 配置信息
     * @param redisProperties    Redis 基础配置信息
     * @return Lettuce 连接工厂
     */
    public LettuceConnectionFactory getLettuceConnectionFactory(RedisConfiguration redisConfiguration, RedisProperties redisProperties) {
        GenericObjectPoolConfig<RedisConfiguration> poolConfig = new GenericObjectPoolConfig<>();
        // pool 配置 
        RedisProperties.Lettuce lettuce = redisProperties.getLettuce();
        if (null != lettuce.getPool()) {
            RedisProperties.Pool pool = redisProperties.getLettuce().getPool();
            // 连接池最大连接数
            poolConfig.setMaxTotal(pool.getMaxActive());
            // 连接池中的最大空闲连接 
            poolConfig.setMaxIdle(pool.getMaxIdle());
            // 连接池中的最小空闲连接
            poolConfig.setMinIdle(pool.getMinIdle());
            // 连接池最大阻塞等待时间（使用负值表示没有限制）
            poolConfig.setMaxWait(pool.getMaxWait());
        }
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration.builder();

        // timeout 
        if (redisProperties.getTimeout() != null) {
            builder.commandTimeout(redisProperties.getTimeout());
        }

        // shutdownTimeout 
        if (lettuce.getShutdownTimeout() != null) {
            builder.shutdownTimeout(lettuce.getShutdownTimeout());
        }

        // 创建 Factory 对象
        LettuceClientConfiguration clientConfig = builder.poolConfig(poolConfig).build();
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisConfiguration, clientConfig);
        factory.afterPropertiesSet();

        return factory;
    }

}
