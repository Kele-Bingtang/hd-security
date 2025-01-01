package cn.youngkbt.hdsecurity;

/**
 * Jwt 配置文件
 *
 * @author Tianke
 * @date 2025/1/1 21:36:32
 * @since 1.0.0
 */
public class HdJwtProperties {
    /**
     * Token 密钥，自定义，根据密钥生成 token，或还原 token
     */
    private String secretKey;

    /**
     * Token 有效期
     */
    private long expireTime = 12 * 60 * 60 * 1000;

    /**
     * Token 刷新时间
     */
    private long refreshTime = 2 * 60 * 60 * 1000;


    public String getSecretKey() {
        return secretKey;
    }

    public HdJwtProperties setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public HdJwtProperties setExpireTime(long expireTime) {
        this.expireTime = expireTime;
        return this;
    }

    public long getRefreshTime() {
        return refreshTime;
    }

    public HdJwtProperties setRefreshTime(long refreshTime) {
        this.refreshTime = refreshTime;
        return this;
    }
}
