package cn.youngkbt.hdsecurity.annotation.handler;

import cn.youngkbt.hdsecurity.annotation.HdCheckHttpBasic;
import cn.youngkbt.hdsecurity.hd.HdBasicAuthHelper;
import cn.youngkbt.hdsecurity.hd.HdHelper;

import java.lang.reflect.Method;

/**
 * HdCheckHttpBasic 处理器，执行「HttpBasic」校验功能
 *
 * @author Tianke
 * @date 2024/12/20 00:50:33
 * @since 1.0.0
 */
public class HdCheckHttpBasicHandler implements HdAnnotationHandler<HdCheckHttpBasic> {
    @Override
    public Class<HdCheckHttpBasic> getHandlerAnnotationClass() {
        return HdCheckHttpBasic.class;
    }

    @Override
    public void handle(HdCheckHttpBasic annotation, Method method) {
        HdBasicAuthHelper hdBasicAuthHelper = HdHelper.basicAuthHelper();
        hdBasicAuthHelper.checkBasicAuth(annotation.realm(), annotation.account());
    }
}
