package cn.youngkbt.hdsecurity.function;

/**
 * 函数式接口：创建 token 的策略
 *
 * @author Tianke
 * @date 2024/11/28 01:17:32
 * @since 1.0.0
 */
@FunctionalInterface
public interface HdCreateTokenFunction {

    /**
     * 创建 Token
     *
     * @param loginId     登录 ID
     * @param accountType 账号类型
     * @return Token
     */
    String create(Object loginId, String accountType);

}