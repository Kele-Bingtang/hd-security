package cn.youngkbt.hdsecurity.jwt.temp;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.exception.HdSecurityJwtException;
import cn.youngkbt.hdsecurity.hd.HdTempTokenHelper;
import cn.youngkbt.hdsecurity.jwt.utils.HdJwtTokenUtil;
import cn.youngkbt.hdsecurity.repository.HdSecurityRepositoryKV;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * JWT Stateless TempTokenHelper（临时令牌）模块：无状态模式，JWT 不会缓存到持久层，一旦 JWT 丢失后将无法找回，并且该 JWT 无法注销，只能等待自身的过期时间到期
 *
 * <p>
 * 如果使用该类替换核心包的自带的临时 Token 功能，一行代码解决：
 * <pre>
 *     HdSecurityHelperCreateStrategy.instance.setCreateTempTokenHelper(HdSecurityTempTokenHelperForStateless::new);
 * </pre>
 * </p>
 *
 * @author Tianke
 * @date 2025/1/5 02:30:01
 * @since 1.0.0
 */
public class HdSecurityTempTokenHelperForStateless extends HdTempTokenHelper {

    /**
     * 获取 JWT 秘钥
     *
     * @return JWT 秘钥
     */
    public String getSecretKey() {
        String jwtSecretKey = HdSecurityManager.getConfig().getJwtSecretKey();
        if (null == jwtSecretKey || jwtSecretKey.isEmpty()) {
            throw new HdSecurityJwtException("请配置 JWT 秘钥");
        }
        return jwtSecretKey;
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
    @Override
    public String createToken(String realm, Object value, long expireTime, Map<String, Object> extra) {
        return HdJwtTokenUtil.createToken(realm, value, expireTime, getSecretKey(), extra);
    }

    /**
     * 解析指定领域的临时 Token 获取 Value
     *
     * @param realm 领域
     * @param token 临时 Token
     * @return 指定领域的 Value
     */
    @Override
    public Object parseToken(String realm, String token) {
        try {
            return HdJwtTokenUtil.getRealmValue(realm, token, getSecretKey());
        } catch (HdSecurityJwtException e) {
            return null;
        }
    }

    /**
     * 移除指定领域下的临时 Token
     *
     * @param realm 领域
     * @param token 临时 Token
     */
    @Override
    public void removeToken(String realm, String token) {
        throw new HdSecurityJwtException("JWT 无状态模式不支持删除 Token");
    }

    /**
     * 获取指定领域下的临时 Token 的有效时间
     *
     * @param realm 领域
     * @param token 临时 Token
     * @return 有效时间
     */
    @Override
    public long getTokenExpireTime(String realm, String token) {
        try {
            return Optional.ofNullable(HdJwtTokenUtil.getExpireTimeout(token, getSecretKey())).orElse(HdSecurityRepositoryKV.NEVER_EXPIRE);
        } catch (HdSecurityJwtException e) {
            return HdSecurityRepositoryKV.NOT_VALUE_EXPIRE;
        }
    }

    /**
     * 获取当前 Token 的扩展信息（此函数只在 JWT 模式下生效，即引入 hd-security-jwt 依赖）
     *
     * @param token 指定的 Token 值
     * @return 对应的扩展数据
     */
    @Override
    public Map<String, Object> getExtraMap(String token) {
        try {
            return HdJwtTokenUtil.getClaims(token, getSecretKey());
        } catch (HdSecurityJwtException e) {
            return Collections.emptyMap();
        }
    }
}
