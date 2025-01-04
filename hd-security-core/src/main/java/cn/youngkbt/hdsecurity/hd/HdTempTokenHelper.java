package cn.youngkbt.hdsecurity.hd;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.constants.DefaultConstant;
import cn.youngkbt.hdsecurity.strategy.HdSecurityTokenGenerateStrategy;
import cn.youngkbt.hdsecurity.utils.HdObjectUtil;

import java.util.Collections;
import java.util.Map;

/**
 * Hd Security 临时 Token 模块
 * <p>有效期很短的 token，一般用于一次性接口防盗用、短时间资源访问等业务场景</p>
 *
 * @author Tianke
 * @date 2025/1/5 01:32:29
 * @since 1.0.0
 */
public class HdTempTokenHelper {
    /**
     * 创建指定领域的临时 Token，存储 Value
     *
     * @param value      指定值
     * @param expireTime 有效时间，单位：秒，-1 代表永久有效
     * @return 临时 Token
     */
    public String createToken(Object value, long expireTime) {
        return createToken(DefaultConstant.DEFAULT_TEMP_TOKEN_REALM, value, expireTime, null);
    }

    /**
     * 创建指定领域的临时 Token，存储 Value
     *
     * @param realm      领域
     * @param value      指定值
     * @param expireTime 有效时间，单位：秒，-1 代表永久有效
     * @param extra      额外的拓展信息，给 JWT 模块使用，这里不用到
     * @return 临时 Token
     */
    public String createToken(String realm, Object value, long expireTime, Map<String, Object> extra) {
        // 创建新临时 Token
        String token = HdSecurityTokenGenerateStrategy.instance.createToken.create(null, null);

        String tempTokenKey = RepositoryKeyHelper.getTempTokenKey(realm, token);
        HdSecurityManager.getRepository().add(tempTokenKey, value, expireTime);

        // 返回临时 Token
        return token;
    }

    /**
     * 解析临时 Token 获取 Value
     *
     * @param token 临时 Token
     * @return 获取 Value
     */
    public Object parseToken(String token) {
        return parseToken(DefaultConstant.DEFAULT_TEMP_TOKEN_REALM, token);
    }

    /**
     * 解析指定领域的临时 Token 获取 Value
     *
     * @param realm 领域
     * @param token 临时 Token
     * @return 指定领域的 Value
     */
    public Object parseToken(String realm, String token) {
        return HdSecurityManager.getRepository().query(RepositoryKeyHelper.getTempTokenKey(realm, token));
    }

    /**
     * 解析临时 Token 获取 Value，并转换为指定类型
     *
     * @param token 临时 Token
     * @param cs    指定类型
     * @param <T>   指定类型
     * @return 获取 Value，并转换为指定类型
     */
    public <T> T parseToken(String token, Class<T> cs) {
        return parseToken(DefaultConstant.DEFAULT_TEMP_TOKEN_REALM, token, cs);
    }

    /**
     * 解析指定领域的临时 Token 获取 Value，并转换为指定类型
     *
     * @param realm 领域
     * @param token 临时 Token
     * @param cs    指定类型
     * @param <T>   指定类型
     * @return 指定领域的 Value，并转换为指定类型
     */
    public <T> T parseToken(String realm, String token, Class<T> cs) {
        return HdObjectUtil.convertObject(parseToken(realm, token), cs);
    }

    /**
     * 移除临时 Token
     *
     * @param token 临时 Token
     */
    public void removeToken(String token) {
        removeToken(DefaultConstant.DEFAULT_TEMP_TOKEN_REALM, token);
    }

    /**
     * 移除指定领域下的临时 Token
     *
     * @param realm 领域
     * @param token 临时 Token
     */
    public void removeToken(String realm, String token) {
        HdSecurityManager.getRepository().remove(RepositoryKeyHelper.getTempTokenKey(realm, token));
    }

    /**
     * 获取临时 Token 的有效时间
     *
     * @param token 临时 Token
     * @return 有效时间
     */
    public long getTokenExpireTime(String token) {
        return getTokenExpireTime(DefaultConstant.DEFAULT_TEMP_TOKEN_REALM, token);
    }

    /**
     * 获取指定领域下的临时 Token 的有效时间
     *
     * @param realm 领域
     * @param token 临时 Token
     * @return 有效时间
     */
    public long getTokenExpireTime(String realm, String token) {
        return HdSecurityManager.getRepository().getExpireTime(RepositoryKeyHelper.getTempTokenKey(realm, token));
    }

    /**
     * 获取当前 Token 的扩展信息（此函数只在 JWT 模式下生效，即引入 hd-security-jwt 依赖）
     *
     * @param token 指定的 Token 值
     * @return 对应的扩展数据
     */
    public Map<String, Object> getExtraMap(String token) {
        return Collections.emptyMap();
    }

    /**
     * 获取指定 Token 的扩展信息（此函数只在 JWT 模式下生效，即引入 hd-security-jwt 依赖）
     *
     * @param token 指定的 Token 值
     * @param key   键值
     * @return 对应的扩展数据
     */
    public Object getExtra(String token, String key) {
        return getExtraMap(token).get(key);
    }
}
