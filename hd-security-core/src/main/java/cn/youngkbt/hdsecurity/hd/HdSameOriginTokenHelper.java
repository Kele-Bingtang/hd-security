package cn.youngkbt.hdsecurity.hd;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.constants.DefaultConstant;
import cn.youngkbt.hdsecurity.error.HdSecurityErrorCode;
import cn.youngkbt.hdsecurity.exception.HdSecuritySameOriginTokenException;
import cn.youngkbt.hdsecurity.utils.HdStringUtil;

import java.util.Objects;
import java.util.Optional;

/**
 * Hd Security SameOrigin Token 模块
 *
 * <p>当两个服务之间调用需要认证时使用，比如服务 A 访问服务 B，则 A 的请求携带一个 Token，服务 B 先验证 Token 后再执行逻辑。场景如：微服务网关请求转发鉴权</p>
 *
 * @author Tianke
 * @date 2024/12/23 21:12:44
 * @since 1.0.0
 */
public class HdSameOriginTokenHelper {
    /**
     * 获取 SameOrigin Token
     *
     * @return SameOrigin Token
     */
    public String getToken() {
        // 先从持久层获取 Token，如果获取失败，则创建 Token
        String token = getCacheToken();
        return HdStringUtil.hasText(token) ? token : refreshToken();
    }

    /**
     * 校验 SameOrigin Token
     *
     * @param token SameOrigin Token
     * @return 校验结果
     */
    public boolean validToken(String token) {
        if (HdStringUtil.hasEmpty(token)) {
            return false;
        }

        String cacheToken = getToken();
        String cacheSecondToken = getCacheSecondToken();
        return Objects.equals(token, cacheToken) || Objects.equals(token, cacheSecondToken);
    }

    /**
     * 校验请求头是否带有 SameOrigin Token，如果校验失败则抛出异常
     */
    public void checkToken() {
        String token = HdSecurityManager.getContext().getRequest().getHeader(DefaultConstant.SAME_ORIGIN_TOKEN_TAG);
        checkToken(token);
    }

    /**
     * 校验 SameOrigin Token，如果校验失败则抛出异常
     *
     * @param token SameOrigin Token
     */
    public void checkToken(String token) {
        if (!validToken(token)) {
            throw new HdSecuritySameOriginTokenException("无效 SameOrigin Token：" + Optional.ofNullable(token).orElse("")).setCode(HdSecurityErrorCode.SAME_ORIGIN_TOKEN_INVALID);
        }
    }

    /**
     * 刷新 SameOrigin Token，并将旧 Token（二级 Token）存入持久层
     *
     * @return 新的 SameOrigin Token
     */
    public String refreshToken() {
        String oldToken = getCacheToken();
        if (HdStringUtil.hasText(oldToken)) {
            // 将旧 Token 存入持久层
            addSecondToken(oldToken, HdSecurityManager.getConfig().getSameOriginTokenExpireTime());
        }
        // 刷新 Token 后存入持久层
        String newToken = HdTokenHelper.createRandom64Token();
        addToken(newToken, HdSecurityManager.getConfig().getSameOriginTokenExpireTime());

        return newToken;
    }

    /**
     * 获取缓存 SameOrigin Token
     *
     * @return SameOrigin Token
     */
    private String getCacheToken() {
        return (String) HdSecurityManager.getRepository().query(RepositoryKeyHelper.getSameOriginTokenKey());
    }

    /**
     * 获取缓存二级 SameOrigin Token
     *
     * @return 二级 SameOrigin Token
     */
    private String getCacheSecondToken() {
        return (String) HdSecurityManager.getRepository().query(RepositoryKeyHelper.getSameOriginSecondTokenKey());
    }

    /**
     * 添加 SameOrigin Token 到缓存
     *
     * @param token      SameOrigin Token
     * @param expireTime 过期时间
     */
    private void addToken(String token, long expireTime) {
        HdSecurityManager.getRepository().add(RepositoryKeyHelper.getSameOriginTokenKey(), token, expireTime);
    }

    /**
     * 添加二级 SameOrigin Token 到缓存
     *
     * @param token      二级 SameOrigin Token
     * @param expireTime 过期时间
     */
    private void addSecondToken(String token, long expireTime) {
        HdSecurityManager.getRepository().add(RepositoryKeyHelper.getSameOriginSecondTokenKey(), token, expireTime);
    }
}
