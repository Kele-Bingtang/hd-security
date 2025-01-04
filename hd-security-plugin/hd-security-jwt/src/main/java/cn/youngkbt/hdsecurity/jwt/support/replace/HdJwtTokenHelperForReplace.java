package cn.youngkbt.hdsecurity.jwt.support.replace;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.exception.HdSecurityJwtException;
import cn.youngkbt.hdsecurity.hd.HdTokenHelper;
import cn.youngkbt.hdsecurity.jwt.utils.HdJwtTokenUtil;
import cn.youngkbt.hdsecurity.model.login.HdLoginModel;

import java.util.Collections;
import java.util.Map;

/**
 * JWT Replace TokenHelper 模块：Token 替换模式，仅仅将核心包自带的 Token 创建功能替换为 JWT 创建
 *
 * @author Tianke
 * @date 2025/1/4 02:02:53
 * @since 1.0.0
 */
public class HdJwtTokenHelperForReplace extends HdTokenHelper {

    public HdJwtTokenHelperForReplace(String accountType) {
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
