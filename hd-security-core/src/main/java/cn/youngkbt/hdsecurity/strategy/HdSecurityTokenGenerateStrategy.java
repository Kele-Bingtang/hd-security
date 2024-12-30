package cn.youngkbt.hdsecurity.strategy;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.exception.HdSecurityException;
import cn.youngkbt.hdsecurity.function.HdCreateTokenFunction;
import cn.youngkbt.hdsecurity.function.HdGenerateUniqueElementFunction;
import cn.youngkbt.hdsecurity.utils.HdStringUtil;

/**
 * Token 生成策略
 *
 * @author Tianke
 * @date 2024/11/28 00:08:15
 * @since 1.0.0
 */
public class HdSecurityTokenGenerateStrategy {

    public static HdSecurityTokenGenerateStrategy instance = new HdSecurityTokenGenerateStrategy();

    public HdGenerateUniqueElementFunction generateUniqueElement = ((elementName, maxTryTimes, createElementFunction, checkUniquePredicate, exceptionConsumer) -> {
        // 循环生成
        for (int i = 1; ; i++) {
            // 生成唯一元素
            String element = createElementFunction.get();

            // 如果 maxTryTimes == -1，表示不做唯一性验证，直接返回
            if (maxTryTimes == -1) {
                return element;
            }

            // 如果生成的元素校验唯一成功，直接可以返回
            if (checkUniquePredicate.test(element)) {
                return element;
            }

            // 如果已经循环了 maxTryTimes 次，仍然没有创建出可用的元素，那么抛出异常
            if (i >= maxTryTimes) {
                exceptionConsumer.accept(new HdSecurityException(" 生成唯一" + element + "失败，已尝试 " + i + " 次，生成算法过于简单或资源池已耗尽"));
            }
        }
    });

    public HdCreateTokenFunction createToken = ((loginId, accountType) -> {
        String tokenStyle = HdSecurityManager.getConfig(accountType).getTokenStyle();

        String tokenByStyle = HdSecurityTokenGenerateEnums.getTokenByStyle(tokenStyle);
        if (HdStringUtil.hasText(tokenByStyle)) {
            return tokenByStyle;
        }
        // 默认返回 uuid
        return HdSecurityTokenGenerateEnums.UUID.getGenerator().get();
    });


    public HdSecurityTokenGenerateStrategy setGenerateUniqueElement(HdGenerateUniqueElementFunction generateUniqueElement) {
        this.generateUniqueElement = generateUniqueElement;
        return this;
    }

    public HdSecurityTokenGenerateStrategy setCreateToken(HdCreateTokenFunction createToken) {
        this.createToken = createToken;
        return this;
    }
}
