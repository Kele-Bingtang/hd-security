package cn.youngkbt.hdsecurity.error;

/**
 * @author Tianke
 * @date 2024/12/24 00:39:08
 * @since 1.0.0
 */
public interface HdSecurityJwtErrorCode {
    /**
     * JWT 签名无效
     */
    int JWT_SIGNATURE_INVALID = 30001;
    int JWT_EXPIRED = 30002;
}
