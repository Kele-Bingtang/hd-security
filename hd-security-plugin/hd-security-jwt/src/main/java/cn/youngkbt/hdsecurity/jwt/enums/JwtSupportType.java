package cn.youngkbt.hdsecurity.jwt.enums;

/**
 * JWT 集成 Hd Security 的支持类型
 *
 * @author Tianke
 * @date 2025/1/5 03:05:08
 * @since 1.0.0
 */
public enum JwtSupportType {
    /**
     * 替换模式：生成 Token 的方式改为 JWT，其他功能不变
     */
    REPLACE,
    /**
     * 缓存模式：生成 Token 的方式改为 JWT，且仅仅缓存 JWT，但是不支持踢人下线、顶人下线和部分会话查询，认证信息完全从 JWT 解析出来
     */
    CACHE,
    /**
     * 无状态模式：生成 Token 的方式改为 JWT，但是不缓存 JWT，也就是关闭缓存功能，认证信息完全从 JWT 解析出来
     */
    STATELESS,

    ;
}
