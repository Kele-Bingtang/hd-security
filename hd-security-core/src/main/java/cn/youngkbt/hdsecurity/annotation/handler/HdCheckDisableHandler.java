package cn.youngkbt.hdsecurity.annotation.handler;

import cn.youngkbt.hdsecurity.annotation.HdCheckDisable;
import cn.youngkbt.hdsecurity.hd.HdBanAccountHelper;
import cn.youngkbt.hdsecurity.hd.HdHelper;
import cn.youngkbt.hdsecurity.hd.HdLoginHelper;

import java.lang.reflect.Method;

/**
 * HdCheckDisable 处理器，执行「账号禁用」校验功能
 *
 * @author Tianke
 * @date 2024/12/20 00:55:55
 * @since 1.0.0
 */
public class HdCheckDisableHandler implements HdAnnotationHandler<HdCheckDisable> {
    @Override
    public Class<HdCheckDisable> getHandlerAnnotationClass() {
        return HdCheckDisable.class;
    }

    @Override
    public void handle(HdCheckDisable annotation, Method method) {
        HdLoginHelper loginHelper = HdHelper.loginHelper(annotation.accountType());
        HdBanAccountHelper banAccountHelper = HdHelper.banAccountHelper(annotation.accountType());

        for (String realm : annotation.value()) {
            banAccountHelper.checkDisable(loginHelper.getLoginId(), realm, annotation.level());
        }
    }
}
