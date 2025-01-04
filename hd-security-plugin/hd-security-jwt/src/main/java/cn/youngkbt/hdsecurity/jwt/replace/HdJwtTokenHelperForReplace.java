package cn.youngkbt.hdsecurity.jwt.replace;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.exception.HdSecurityJwtException;
import cn.youngkbt.hdsecurity.hd.HdTokenHelper;
import cn.youngkbt.hdsecurity.jwt.HdJwtTokenUtil;
import cn.youngkbt.hdsecurity.model.login.HdLoginModel;

import java.util.Collections;
import java.util.Map;

/**
 * JWT Replace TokenHelper 模块：Token 替换模式，仅仅将核心模块自带的 Token 创建功能替换为 JWT 创建
 *
 * @author Tianke
 * @date 2025/1/4 02:02:53
 * @since 1.0.0
 */
public class HdJwtTokenHelperForReplace extends HdTokenHelper {

    public HdJwtTokenHelperForReplace(String accountType) {
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
    public Map<String, Object> getExtraMap(String token) {
        try {
            return HdJwtTokenUtil.getClaims(token, getSecretKey());
        } catch (HdSecurityJwtException e) {
            return Collections.emptyMap();
        }
    }
}
