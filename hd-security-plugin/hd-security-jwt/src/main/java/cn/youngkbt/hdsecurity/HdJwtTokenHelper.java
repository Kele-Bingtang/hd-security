package cn.youngkbt.hdsecurity;

import cn.youngkbt.hdsecurity.constants.DefaultConstant;
import cn.youngkbt.hdsecurity.error.HdSecurityJwtErrorCode;
import cn.youngkbt.hdsecurity.exception.HdSecurityJwtException;
import cn.youngkbt.hdsecurity.hd.HdTokenHelper;
import cn.youngkbt.hdsecurity.repository.HdSecurityRepositoryKV;
import cn.youngkbt.hdsecurity.utils.HdStringUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * JWT 令牌模块
 *
 * @author Tianke
 * @date 2025/1/1 21:28:15
 * @since 1.0.0
 */
public class HdJwtTokenHelper {
    private String accountType = DefaultConstant.DEFAULT_ACCOUNT_TYPE;
    private String loginId = "loginId";
    private String device = DefaultConstant.DEFAULT_LOGIN_DEVICE;
    private String issuer = "hd-security";

    public String getAccountType() {
        return accountType;
    }

    public HdJwtTokenHelper setAccountType(String accountType) {
        this.accountType = accountType;
        return this;
    }

    public String getLoginId() {
        return loginId;
    }

    public HdJwtTokenHelper setLoginId(String loginId) {
        this.loginId = loginId;
        return this;
    }

    public String getDevice() {
        return device;
    }

    public HdJwtTokenHelper setDevice(String device) {
        this.device = device;
        return this;
    }

    public String getIssuer() {
        return issuer;
    }

    public HdJwtTokenHelper setIssuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    /**
     * 生成令牌
     *
     * @param loginId    登录 ID
     * @param device     设备
     * @param expireTime 过期时间
     * @param secretKey  密钥
     * @param extra      额外参数
     * @return 令牌
     */
    public String createToken(Object loginId, String device, long expireTime, String secretKey, Map<String, Object> extra) {
        if (null == extra || extra.isEmpty()) {
            extra = new HashMap<>();
        }

        extra.put(accountType, accountType);
        extra.put(this.loginId, loginId);
        extra.put(this.device, device);

        return createToken(extra, expireTime, secretKey);
    }

    /**
     * 生成令牌
     *
     * @param claim      参数
     * @param expireTime 过期时间
     * @param secretKey  密钥
     * @return 令牌
     */
    private String createToken(Map<String, Object> claim, long expireTime, String secretKey) {
        return Jwts.builder()
                .header()
                .add("type", "JWT")
                .and()
                // 随机生成 32 位 字符串，防止同账号下每次生成的 token 都一样的
                .id(HdTokenHelper.createRandom32Token())
                // 设置过期时间，如果为 -1 代表永不过期
                .expiration(expireTime != HdSecurityRepositoryKV.NEVER_EXPIRE ? new Date(System.currentTimeMillis() + expireTime * 1000) : null)
                .claims(claim)
                .issuer(issuer)
                .issuedAt(new Date())
                .signWith(generateKey(secretKey), Jwts.SIG.HS256)
                .compact();
    }


    /**
     * 从令牌中获取数据声明
     *
     * @param token     令牌
     * @param secretKey 密钥
     * @return 数据声明
     */
    private Claims getClaims(String token, String secretKey) {
        return parseToken(token, secretKey).getPayload();
    }

    /**
     * 解析 Token
     *
     * @param token     令牌
     * @param secretKey 密钥
     * @return 数据声明
     */
    private Jws<Claims> parseToken(String token, String secretKey) {
        return parseToken(token, secretKey, true);
    }


    /**
     * 解析 Token
     *
     * @param token         令牌
     * @param secretKey     密钥
     * @param isCheckExpire 是否校验 Token 是否过期
     * @return 数据声明
     */
    private Jws<Claims> parseToken(String token, String secretKey, boolean isCheckExpire) {
        if (HdStringUtil.hasEmpty(token)) {
            throw new HdSecurityJwtException("Token 不能为空");
        }

        if (HdStringUtil.hasEmpty(secretKey)) {
            throw new HdSecurityJwtException("JWT 密钥不能为空");
        }

        Jws<Claims> claimsJws;
        try {
            claimsJws = Jwts.parser().verifyWith(generateKey(secretKey)).build().parseSignedClaims(token);
        } catch (JwtException | IllegalArgumentException e) {
            throw new HdSecurityJwtException("JWT 签名无效").setCode(HdSecurityJwtErrorCode.JWT_SIGNATURE_INVALID);
        }

        if (isCheckExpire) {
            Date expiration = claimsJws.getPayload().getExpiration();
            if (Objects.nonNull(expiration) && expiration.before(new Date())) {
                throw new HdSecurityJwtException("JWT 已过期").setCode(HdSecurityJwtErrorCode.JWT_EXPIRED);
            }
        }

        return claimsJws;
    }

    /**
     * 加密明文密钥
     *
     * @param secretKey 密钥
     */
    public SecretKey generateKey(String secretKey) {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 从令牌中获取数据头
     *
     * @param token     令牌
     * @param secretKey 密钥
     * @return 数据声明
     */
    public JwsHeader getHeader(String token, String secretKey) {
        return parseToken(token, secretKey).getHeader();
    }


    /**
     * 从令牌中获取登录 ID
     *
     * @param token     令牌
     * @param secretKey 密钥
     * @return 用户名
     */
    public Object getLoginId(String token, String secretKey) {
        return getClaims(token, secretKey).get(loginId);
    }

    /**
     * 从令牌中获取设备
     *
     * @param token     令牌
     * @param secretKey 密钥
     * @return 用户名
     */
    public String getDevice(String token, String secretKey) {
        return String.valueOf(getClaims(token, secretKey).get(device));
    }

    /**
     * 验证令牌
     *
     * @param token     令牌
     * @param loginId   登录 ID
     * @param device    设备
     * @param secretKey 密钥
     * @return 令牌是否正确
     */
    public boolean validateToken(String token, String loginId, String device, String secretKey) {
        Claims claims = getClaims(token, secretKey);
        Object validLoginId = claims.get(this.loginId);
        Object validDevice = claims.get(this.device);

        return (Objects.equals(validLoginId, loginId) && Objects.equals(validDevice, device) && !isExpire(token, secretKey));
    }

    /**
     * 刷新令牌
     *
     * @param token      令牌
     * @param expireTime 过期时间
     * @param secretKey  密钥
     * @return 新的令牌
     */
    public String refreshToken(String token, Long expireTime, String secretKey) {
        String refreshedToken;
        try {
            Claims claims = getClaims(token, secretKey);
            refreshedToken = createToken(claims, expireTime, secretKey);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 获取 Token 有效期
     *
     * @param token     令牌
     * @param secretKey 密钥
     * @return Token 有效期
     */
    public Date getExpireTime(String token, String secretKey) {
        return getClaims(token, secretKey).getExpiration();
    }

    /**
     * 判断令牌是否过期
     *
     * @param token     令牌
     * @param secretKey 密钥
     * @return 是否过期
     */
    public boolean isExpire(String token, String secretKey) {
        return getExpireTime(token, secretKey).before(new Date());
    }
}
