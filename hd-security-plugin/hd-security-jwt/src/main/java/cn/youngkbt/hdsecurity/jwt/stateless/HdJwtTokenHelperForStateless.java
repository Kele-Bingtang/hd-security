package cn.youngkbt.hdsecurity.jwt.stateless;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.exception.HdSecurityJwtException;
import cn.youngkbt.hdsecurity.hd.HdTokenHelper;
import cn.youngkbt.hdsecurity.jwt.HdJwtTokenUtil;
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

    public String getSecretKey() {
        String jwtSecretKey = HdSecurityManager.getConfig(getAccountType()).getJwtSecretKey();
        if (null == jwtSecretKey || jwtSecretKey.isEmpty()) {
            throw new HdSecurityJwtException("请配置 JWT 秘钥");
        }
        return jwtSecretKey;
    }


    @Override
    public String createToken(HdLoginModel loginModel) {
        return HdJwtTokenUtil.createToken(getAccountType(), loginModel.getLoginId(), loginModel.getDevice(), loginModel.getTokenExpireTime(), getSecretKey(), loginModel.getExtraData());
    }

    @Override
    public Object getLoginIdByToken(String token) {
        try {
            return HdJwtTokenUtil.getLoginId(token, getSecretKey());
        } catch (HdSecurityJwtException e) {
            return null;
        }
    }

    @Override
    public String getDeviceByToken(String token) {
        try {
            return HdJwtTokenUtil.getDevice(token, getSecretKey());
        } catch (HdSecurityJwtException e) {
            return null;
        }
    }

    @Override
    public long getTokenAndLoginIdExpireTime(String token) {
        try {
            return Optional.ofNullable(HdJwtTokenUtil.getExpireTimeout(token, getSecretKey())).orElse(HdSecurityRepositoryKV.NEVER_EXPIRE);
        } catch (HdSecurityJwtException e) {
            return HdSecurityRepositoryKV.NOT_VALUE_EXPIRE;
        }
    }

    @Override
    public Map<String, Object> getExtraMap(String token) {
        try {
            return HdJwtTokenUtil.getClaims(token, getSecretKey());
        } catch (HdSecurityJwtException e) {
            return Collections.emptyMap();
        }
    }

}
