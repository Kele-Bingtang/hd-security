package cn.youngkbt.hdsecurity.annotation.handler;

import cn.youngkbt.hdsecurity.annotation.HdCheckRole;
import cn.youngkbt.hdsecurity.annotation.HdMode;
import cn.youngkbt.hdsecurity.exception.HdSecurityAuthorizeException;
import cn.youngkbt.hdsecurity.hd.HdAuthorizeHelper;
import cn.youngkbt.hdsecurity.hd.HdHelper;

import java.lang.reflect.Method;

/**
 * HdCheckRole 处理器，执行「角色码」校验功能
 * 
 * @author Tianke
 * @date 2024/12/20 00:44:44
 * @since 1.0.0
 */
public class HdCheckRoleHandler implements HdAnnotationHandler<HdCheckRole> {
    @Override
    public Class<HdCheckRole> getHandlerAnnotationClass() {
        return HdCheckRole.class;
    }

    @Override
    public void handle(HdCheckRole annotation, Method method) {
        HdAuthorizeHelper hdAuthorizeHelper = HdHelper.authorizeHelper(annotation.accountType());
        HdMode mode = annotation.mode();
        String[] roles = annotation.value();

        try {
            if (mode == HdMode.AND) {
                hdAuthorizeHelper.checkRoleAnd(roles);
            } else if (mode == HdMode.OR) {
                hdAuthorizeHelper.checkRoleOr(roles);
            }
        } catch (HdSecurityAuthorizeException e) {
            for (String permission : annotation.orPermission()) {
                // 任意一个权限验证通过，则通过
                if (hdAuthorizeHelper.hasPermission(permission)) {
                    return;
                }
            }
            // 角色和权限校验不通过，则抛出异常
            throw e;
        }
    }
}
