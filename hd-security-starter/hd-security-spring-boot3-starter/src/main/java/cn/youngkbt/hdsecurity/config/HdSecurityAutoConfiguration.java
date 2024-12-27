package cn.youngkbt.hdsecurity.config;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.annotation.handler.HdAnnotationHandler;
import cn.youngkbt.hdsecurity.authorize.HdSecurityAuthorize;
import cn.youngkbt.hdsecurity.context.HdSecurityContext;
import cn.youngkbt.hdsecurity.context.HdSecurityContextForSpring;
import cn.youngkbt.hdsecurity.hd.HdHelper;
import cn.youngkbt.hdsecurity.listener.HdSecurityEventListener;
import cn.youngkbt.hdsecurity.log.HdSecurityLog;
import cn.youngkbt.hdsecurity.repository.HdSecurityRepository;
import cn.youngkbt.hdsecurity.utils.SpringBootVersionChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author Tianke
 * @date 2024/12/23 23:49:32
 * @since 1.0.0
 */
@AutoConfiguration
public class HdSecurityAutoConfiguration {

    public HdSecurityAutoConfiguration() {
        // 检测 SpringBoot 版本兼容性问题
        new SpringBootVersionChecker();
    }

    @Bean
    @ConfigurationProperties(prefix = "hd-security")
    public HdSecurityConfig hdSecurityConfig() {
        return new HdSecurityConfig();
    }

    @Bean
    public HdSecurityContextForSpring hdSecurityContextForSpring() {
        return new HdSecurityContextForSpring();
    }

    @Autowired(required = false)
    public void setLog(HdSecurityLog log) {
        if (null == log) {
            return;
        }
        HdSecurityManager.setLog(log);
    }

    @Autowired(required = false)
    public void setHdSecurityConfig(HdSecurityConfig config) {
        if (null == config) {
            return;
        }
        HdSecurityManager.setConfig(config);
    }

    @Autowired(required = false)
    public void setHdSecurityRepository(HdSecurityRepository repository) {
        if (null == repository) {
            return;
        }
        HdSecurityManager.setRepository(repository);
    }

    @Autowired(required = false)
    public void setHdSecurityContext(HdSecurityContext context) {
        if (null == context) {
            return;
        }
        HdSecurityManager.setContext(context);
    }

    @Autowired(required = false)
    public void setHdSecurityAuthorize(HdSecurityAuthorize authorize) {
        if (null == authorize) {
            return;
        }
        HdSecurityManager.setAuthorize(authorize);
    }

    @Autowired(required = false)
    public void setHdSecurityListener(List<HdSecurityEventListener> listener) {
        HdSecurityManager.setEventListener(listener);
    }

    @Autowired(required = false)
    public void setHdSecurityAnnotationHandler(List<HdAnnotationHandler<? extends Annotation>> annotationHandlerList) {
        for (HdAnnotationHandler<? extends Annotation> handler : annotationHandlerList) {
            HdHelper.annotationHelper().addAnnotationHandler(handler);
        }
    }
}