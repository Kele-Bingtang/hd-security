package cn.youngkbt.hdsecurity.jwt.utils;

import cn.youngkbt.hdsecurity.exception.HdSecurityJwtException;
import cn.youngkbt.hdsecurity.hd.HdTokenHelper;
import cn.youngkbt.hdsecurity.jwt.error.HdSecurityJwtErrorCode;
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
 * JWT 工具类，包含创建、解析、刷新、验证 JWT 方法
 *
 * @author Tianke
 * @date 2025/1/3 23:22:35
 * @since 1.0.0
 */
public class HdJwtTokenUtil {

    /**
     * 使用 public 修饰为了支持修改属性值
     */
    public static String accountTypeKey = "accountType";
    public static String loginIdKey = "loginId";
    public static String deviceKey = "device";
    public static String issuerValue = "hd-security";
    public static String realmKey = "realm-";

    private HdJwtTokenUtil() {
    }

    /**
     * 生成令牌，给临时 Token 创建使用
     *
     * @param realm      领域
     * @param value      存储的值
     * @param expireTime 过期时间
     * @param secretKey  密钥
     * @param extra      额外参数
     * @return 令牌
     */
    public static String createToken(String realm, Object value, long expireTime, String secretKey, Map<String, Object> extra) {
        if (null == extra || extra.isEmpty()) {
            extra = new HashMap<>();
        }

        extra.put(realmKey + realm, value);
        return createToken(extra, expireTime, secretKey);
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
    public static String createToken(String accountType, Object loginId, String device, long expireTime, String secretKey, Map<String, Object> extra) {
        if (null == extra || extra.isEmpty()) {
            extra = new HashMap<>();
        }

        extra.put(accountTypeKey, accountType);
        extra.put(loginIdKey, loginId);
        extra.put(deviceKey, device);

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
    private static String createToken(Map<String, Object> claim, long expireTime, String secretKey) {
        return Jwts.builder()
                .header()
                .add("type", "JWT")
                .and()
                // 随机生成 32 位 字符串，防止同账号下每次生成的 token 都一样的
                .id(HdTokenHelper.createRandom32Token())
                // 设置过期时间，如果为 -1 代表永不过期
                .expiration(expireTime != HdSecurityRepositoryKV.NEVER_EXPIRE ? new Date(System.currentTimeMillis() + expireTime * 1000) : null)
                .claims(claim)
                .issuer(issuerValue)
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
    public static Claims getClaims(String token, String secretKey) {
        return parseToken(token, secretKey).getPayload();
    }

    /**
     * 解析 Token
     *
     * @param token     令牌
     * @param secretKey 密钥
     * @return 数据声明
     */
    public static Jws<Claims> parseToken(String token, String secretKey) {
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
    public static Jws<Claims> parseToken(String token, String secretKey, boolean isCheckExpire) {
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
    public static SecretKey generateKey(String secretKey) {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
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
    public static boolean validateToken(String token, String loginId, String device, String secretKey) {
        Claims claims = getClaims(token, secretKey);
        Object validLoginId = claims.get(loginIdKey);
        Object validDevice = claims.get(deviceKey);

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
    public static String refreshToken(String token, Long expireTime, String secretKey) {
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
     * 从令牌中获取数据头
     *
     * @param token     令牌
     * @param secretKey 密钥
     * @return 数据声明
     */
    public static JwsHeader getHeader(String token, String secretKey) {
        return parseToken(token, secretKey).getHeader();
    }

    /**
     * 从令牌中获取值
     *
     * @param realm     域
     * @param token     令牌
     * @param secretKey 密钥
     * @return Value
     */
    public static Object getRealmValue(String realm, String token, String secretKey) {
        return getClaims(token, secretKey).get(realmKey + realm);
    }

    /**
     * 从令牌中获取登录 ID
     *
     * @param token     令牌
     * @param secretKey 密钥
     * @return 用户名
     */
    public static Object getLoginId(String token, String secretKey) {
        return getClaims(token, secretKey).get(loginIdKey);
    }

    /**
     * 从令牌中获取设备
     *
     * @param token     令牌
     * @param secretKey 密钥
     * @return 用户名
     */
    public static String getDevice(String token, String secretKey) {
        return String.valueOf(getClaims(token, secretKey).get(deviceKey));
    }

    /**
     * 获取 Token 有效期
     *
     * @param token     令牌
     * @param secretKey 密钥
     * @return Token 有效期
     */
    public static Date getExpireTime(String token, String secretKey) {
        return getClaims(token, secretKey).getExpiration();
    }

    /**
     * 获取 Token 有效期，单位秒。如果为 null 代表永不过期
     *
     * @param token     令牌
     * @param secretKey 密钥
     * @return Token 有效期
     */
    public static Long getExpireTimeout(String token, String secretKey) {
        Date expireTime = getExpireTime(token, secretKey);
        // 如果没有设置过期时间，则默认为永久有效
        if (null == expireTime) {
            return null;
        }

        return expireTime.getTime() - System.currentTimeMillis() / 1000;
    }

    /**
     * 判断令牌是否过期
     *
     * @param token     令牌
     * @param secretKey 密钥
     * @return 是否过期
     */
    public static boolean isExpire(String token, String secretKey) {
        Date expireTime = getExpireTime(token, secretKey);
        // 如果没有设置过期时间，则默认为永久有效
        if (null == expireTime) {
            return false;
        }
        return expireTime.before(new Date());
    }
}
