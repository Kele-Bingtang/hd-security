package cn.youngkbt.hdsecurity.jwt.cache;

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
 * JWT Cache TokenHelper 模块：缓存模式，JWT 存入持久层，仅用于判断是否过期。需要重写创建 Token 方法，从 Token 获取登录信息方法，不会缓存与 LoginId 相关的信息
 *
 * <p>
 * 常用场景：当用户想注销 JWT，服务器无法注销，该 JWT 只能等待自己过期。因此服务器需要在持久层缓存 JWT，每次请求资源前先判断持久层是否存在前端传来的 JWT，当登出时就可以在持久层清除。不再局限 JWT 本身的过期时间
 * </p>
 *
 * @author Tianke
 * @date 2025/1/4 02:16:41
 * @since 1.0.0
 */
public class HdJwtTokenHelperForCache extends HdTokenHelper {

    public HdJwtTokenHelperForCache(String accountType) {
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

    /**
     * 添加 Token 和 LoginId 的映射关系
     *
     * @param token           Token
     * @param loginId         登录 ID
     * @param tokenExpireTime Token 过期时间
     */
    @Override
    public void addTokenAndLoginIdMapping(String token, Object loginId, Long tokenExpireTime) {
        //
    }

    /**
     * 编辑 Token 和 LoginId 的映射关系
     *
     * @param token   Token
     * @param loginId 登录 ID
     */
    @Override
    public void editTokenAndLoginIdMapping(String token, Object loginId) {
        // 
    }

    /**
     * 删除 Token 和 LoginId 的映射关系
     *
     * @param token Token
     */
    @Override
    public void removeTokenAndLoginIdMapping(String token) {
        //
    }

    /**
     * 返回全局配置对象的 maxTryTimes，该模式因为不根据 Token 存储任何信息，因此不需要 Token 判重功能
     *
     * @return -1，不开启 maxTryTimes 功能
     */
    @Override
    public int getMaxTryTimes() {
        return -1;
    }
}
