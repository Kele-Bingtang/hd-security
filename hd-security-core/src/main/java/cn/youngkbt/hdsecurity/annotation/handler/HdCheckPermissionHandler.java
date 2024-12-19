package cn.youngkbt.hdsecurity.annotation.handler;

import cn.youngkbt.hdsecurity.annotation.HdCheckPermission;
import cn.youngkbt.hdsecurity.annotation.HdMode;
import cn.youngkbt.hdsecurity.exception.HdSecurityAuthorizeException;
import cn.youngkbt.hdsecurity.hd.HdAuthorizeHelper;
import cn.youngkbt.hdsecurity.hd.HdHelper;

import java.lang.reflect.Method;

/**
 * HdCheckPermission 处理器，执行「权限码」校验功能
 *
 * @author Tianke
 * @date 2024/12/20 00:47:51
 * @since 1.0.0
 */
public class HdCheckPermissionHandler implements HdAnnotationHandler<HdCheckPermission> {
    @Override
    public Class<HdCheckPermission> getHandlerAnnotationClass() {
        return HdCheckPermission.class;
    }

    @Override
    public void handle(HdCheckPermission annotation, Method method) {
        HdAuthorizeHelper hdAuthorizeHelper = HdHelper.authorizeHelper(annotation.accountType());
        HdMode mode = annotation.mode();
        String[] permissions = annotation.value();

        try {
            if (mode == HdMode.AND) {
                hdAuthorizeHelper.checkPermissionAnd(permissions);
            } else if (mode == HdMode.OR) {
                hdAuthorizeHelper.checkPermissionOr(permissions);
            }
        } catch (HdSecurityAuthorizeException e) {
            for (String role : annotation.orRole()) {
                // 任意一个角色通过，则通过
                if (hdAuthorizeHelper.hasRole(role)) {
                    return;
                }
            }
            // 权限和角色校验不通过，则抛出异常
            throw e;
        }
    }
}
