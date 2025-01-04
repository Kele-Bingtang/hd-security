package cn.youngkbt.hdsecurity.jwt.temp;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.exception.HdSecurityJwtException;
import cn.youngkbt.hdsecurity.hd.HdTempTokenHelper;
import cn.youngkbt.hdsecurity.hd.RepositoryKeyHelper;
import cn.youngkbt.hdsecurity.jwt.HdJwtTokenUtil;

import java.util.Collections;
import java.util.Map;

/**
 * JWT Replace TempTokenHelper（临时令牌）模块：Token 替换模式，仅仅将核心模块自带的 Token 创建功能替换为 JWT 创建
 *
 * @author Tianke
 * @date 2025/1/4 14:30:14
 * @since 1.0.0
 */
public class HdSecurityTempTokenHelperForReplace extends HdTempTokenHelper {

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
        // 创建新临时 Token
        String token = HdJwtTokenUtil.createToken(realm, value, expireTime, getSecretKey(), extra);

        String tempTokenKey = RepositoryKeyHelper.getTempTokenKey(realm, token);
        HdSecurityManager.getRepository().add(tempTokenKey, value, expireTime);

        // 返回临时 Token
        return token;
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
