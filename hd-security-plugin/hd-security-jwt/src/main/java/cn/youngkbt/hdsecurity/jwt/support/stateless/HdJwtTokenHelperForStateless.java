package cn.youngkbt.hdsecurity.jwt.support.stateless;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.exception.HdSecurityJwtException;
import cn.youngkbt.hdsecurity.hd.HdTokenHelper;
import cn.youngkbt.hdsecurity.jwt.utils.HdJwtTokenUtil;
import cn.youngkbt.hdsecurity.model.login.HdLoginModel;
import cn.youngkbt.hdsecurity.repository.HdSecurityRepositoryKV;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * JWT Stateless TokenHelper 模块：无状态模式，JWT 不会缓存到持久层，一旦 JWT 丢失后将无法找回，并且该 JWT 无法注销，只能等待自身的过期时间到期
 *
 * @author Tianke
 * @date 2025/1/4 01:21:57
 * @since 1.0.0
 */
public class HdJwtTokenHelperForStateless extends HdTokenHelper {

    public HdJwtTokenHelperForStateless(String accountType) {
        super(accountType);
    }

    /**
     * 获取 JWT 秘钥
     *
     * @return JWT 秘钥
     */
    public String getSecretKey() {
        String jwtSecretKey = HdSecurityManager.getConfig(getAccountType()).getJwtSecretKey();
        if (null == jwtSecretKey || jwtSecretKey.isEmpty()) {
            throw new HdSecurityJwtException("请配置 JWT 秘钥");
        }
        return jwtSecretKey;
    }

    /**
     * 使用策略创建 Token
     *
     * @param loginModel 登录模型
     * @return Token
     */
    @Override
    public String createToken(HdLoginModel loginModel) {
        return HdJwtTokenUtil.createToken(getAccountType(), loginModel.getLoginId(), loginModel.getDevice(), loginModel.getTokenExpireTime(), getSecretKey(), loginModel.getExtraData());
    }

    /**
     * 根据 Token 获取 LoginId
     *
     * @param token Token
     * @return LoginId
     */
    @Override
    public Object getLoginIdByToken(String token) {
        try {
            return HdJwtTokenUtil.getLoginId(token, getSecretKey());
        } catch (HdSecurityJwtException e) {
            return null;
        }
    }

    /**
     * 获取 Token 对应的设备
     *
     * @param token Token
     * @return 设备
     */
    @Override
    public String getDeviceByToken(String token) {
        try {
            return HdJwtTokenUtil.getDevice(token, getSecretKey());
        } catch (HdSecurityJwtException e) {
            return null;
        }
    }

    /**
     * 通过 Token 获取 Token 和 LoginId 映射关系的过期时间（单位: 秒，返回 -1 代表永久有效，-2 代表没有这个值）
     *
     * @param token Token
     * @return Token 和 LoginId 映射关系的过期时间（单位: 秒，返回 -1 代表永久有效，-2 代表没有这个值）
     */
    @Override
    public long getTokenAndLoginIdExpireTime(String token) {
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
