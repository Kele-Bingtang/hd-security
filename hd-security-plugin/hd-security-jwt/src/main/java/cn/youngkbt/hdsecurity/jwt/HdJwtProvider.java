package cn.youngkbt.hdsecurity.jwt;

import cn.youngkbt.hdsecurity.exception.HdSecurityJwtException;
import cn.youngkbt.hdsecurity.jwt.enums.JwtSupportType;
import cn.youngkbt.hdsecurity.jwt.support.cache.HdJwtLoginHelperForCache;
import cn.youngkbt.hdsecurity.jwt.support.cache.HdJwtTokenHelperForCache;
import cn.youngkbt.hdsecurity.jwt.support.replace.HdJwtTokenHelperForReplace;
import cn.youngkbt.hdsecurity.jwt.support.stateless.HdJwtLoginHelperForStateless;
import cn.youngkbt.hdsecurity.jwt.support.stateless.HdJwtSessionHelperForStateless;
import cn.youngkbt.hdsecurity.jwt.support.stateless.HdJwtTokenHelperForStateless;
import cn.youngkbt.hdsecurity.jwt.temp.HdSecurityTempTokenHelperForReplace;
import cn.youngkbt.hdsecurity.jwt.temp.HdSecurityTempTokenHelperForStateless;
import cn.youngkbt.hdsecurity.strategy.HdSecurityHelperCreateStrategy;

/**
 * Hd Security JWT 支持的类型提供者，使用该类通过指定 JWT 模式来替换核心包自带的 Token 相关功能
 *
 * @author Tianke
 * @date 2025/1/5 03:03:31
 * @since 1.0.0
 */
public class HdJwtProvider {

    private HdJwtProvider() {
    }

    /**
     * 使用指定 JWT 模式替换核心包自带的 Token 相关功能，包括 TokenHelper、LoginHelper、SessionHelper
     *
     * @param jwtSupportType JWT 支持的模式
     */
    public static void use(JwtSupportType jwtSupportType) {
        if (jwtSupportType.equals(JwtSupportType.REPLACE)) {
            HdSecurityHelperCreateStrategy.instance.setCreateTokenHelper(HdJwtTokenHelperForReplace::new);
            return;
        }

        if (jwtSupportType.equals(JwtSupportType.CACHE)) {
            HdSecurityHelperCreateStrategy.instance.setCreateTokenHelper(HdJwtTokenHelperForCache::new)
                    .setCreateLoginHelper(HdJwtLoginHelperForCache::new);
            return;
        }

        if (jwtSupportType.equals(JwtSupportType.STATELESS)) {
            HdSecurityHelperCreateStrategy.instance.setCreateTokenHelper(HdJwtTokenHelperForStateless::new)
                    .setCreateSessionHelper(HdJwtSessionHelperForStateless::new)
                    .setCreateLoginHelper(HdJwtLoginHelperForStateless::new);
            return;
        }

        throw new HdSecurityJwtException("JWT 模式暂不支持该类型：" + jwtSupportType.name());
    }

    /**
     * 使用指定 JWT 模式替换核心包自带的临时 Token 功能，即替换 TempTokenHelper 类
     *
     * @param jwtSupportType JWT 支持的模式
     */
    public static void useTemp(JwtSupportType jwtSupportType) {
        if (jwtSupportType.equals(JwtSupportType.REPLACE)) {
            HdSecurityHelperCreateStrategy.instance.setCreateTempTokenHelper(HdSecurityTempTokenHelperForReplace::new);
            return;
        }

        if (jwtSupportType.equals(JwtSupportType.STATELESS)) {
            HdSecurityHelperCreateStrategy.instance.setCreateTempTokenHelper(HdSecurityTempTokenHelperForStateless::new);
            return;
        }

        throw new HdSecurityJwtException("临时 Token 模式暂不支持该类型：" + jwtSupportType.name());
    }
}
