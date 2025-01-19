package cn.youngkbt.hdsecurity.annotation.handler;

import cn.youngkbt.hdsecurity.annotation.HdCheckSecondAuth;
import cn.youngkbt.hdsecurity.hd.HdHelper;

import java.lang.reflect.Method;

/**
 * HdCheckSecondAuth 处理器，执行「二次验证」校验功能
 *
 * @author Tianke
 * @date 2024/12/20 00:40:45
 * @since 1.0.0
 */
public class HdCheckSecondAuthHandler implements HdAnnotationHandler<HdCheckSecondAuth> {
    @Override
    public Class<HdCheckSecondAuth> getHandlerAnnotationClass() {
        return HdCheckSecondAuth.class;
    }

    @Override
    public void handle(HdCheckSecondAuth annotation, Method method) {
        HdHelper.secondAuthHelper(annotation.accountType()).checkSecondAuth(annotation.realm());
    }
}
