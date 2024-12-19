package cn.youngkbt.hdsecurity.annotation.handler;

import cn.youngkbt.hdsecurity.annotation.HdCheckLogin;
import cn.youngkbt.hdsecurity.hd.HdHelper;

import java.lang.reflect.Method;

/**
 * HdCheckLogin 处理器，执行「登录」校验功能
 *
 * @author Tianke
 * @date 2024/12/20 00:13:18
 * @since 1.0.0
 */
public class HdCheckLoginHandler implements HdAnnotationHandler<HdCheckLogin> {

    @Override
    public Class<HdCheckLogin> getHandlerAnnotationClass() {
        return HdCheckLogin.class;
    }

    public void handle(HdCheckLogin checkLogin, Method method) {
        HdHelper.loginHelper(checkLogin.accountType()).checkLogin();
    }

}
