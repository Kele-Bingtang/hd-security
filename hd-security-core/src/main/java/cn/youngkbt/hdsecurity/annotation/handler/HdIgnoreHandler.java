package cn.youngkbt.hdsecurity.annotation.handler;

import cn.youngkbt.hdsecurity.annotation.HdIgnore;
import cn.youngkbt.hdsecurity.router.HdRouter;

import java.lang.reflect.Method;

/**
 * HdIgnore 处理器，不执行任何校验功能（优先级高于其他的鉴权注解）
 *
 * @author Tianke
 * @date 2024/12/20 00:30:46
 * @since 1.0.0
 */
public class HdIgnoreHandler implements HdAnnotationHandler<HdIgnore> {
    @Override
    public Class<HdIgnore> getHandlerAnnotationClass() {
        return null;
    }

    @Override
    public void handle(HdIgnore annotation, Method method) {
        HdRouter.stopMatch();
    }
}
