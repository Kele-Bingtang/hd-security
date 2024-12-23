package cn.youngkbt.hdsecurity.utils;

import cn.youngkbt.hdsecurity.exception.HdSecurityException;
import org.springframework.boot.SpringBootVersion;

/**
 * SpringBoot 版本与 Hd Security 版本兼容检查器
 *
 * @author Tianke
 * @date 2024/12/24 01:09:50
 * @since 1.0.0
 */
public class SpringBootVersionChecker {

    public SpringBootVersionChecker() {
        String version = SpringBootVersion.getVersion();
        if (HdStringUtil.hasEmpty(version) || version.startsWith("3.")) {
            return;
        }
        String str = "当前 SpringBoot 版本（" + version + "）与 Hd Security 依赖不兼容，" +
                "请将依赖 hd-security-spring-boot3-starter 修改为：hd-security-spring-boot2-starter";
        System.err.println(str);
        throw new HdSecurityException(str);
    }
}
