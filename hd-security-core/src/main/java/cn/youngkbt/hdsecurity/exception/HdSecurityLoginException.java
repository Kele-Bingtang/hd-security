package cn.youngkbt.hdsecurity.exception;

import java.util.Arrays;
import java.util.List;

/**
 * Hd Security 登录异常，检查登录信息、踢人下线、顶人下线时抛出
 *
 * @author Tianke
 * @date 2024/11/27 23:21:29
 * @since 1.0.0
 */
public class HdSecurityLoginException extends HdSecurityException {
    /**
     * 账号被踢下线
     */
    public static final String KICK_OUT = "HD_SECURITY_KICK_OUT";
    /**
     * 账号被顶下线
     */
    public static final String REPLACED = "HD_SECURITY_REPLACED";


    /**
     * Hd Security 关键词的集合
     */
    public static final List<String> KEYWORD_LIST = Arrays.asList(KICK_OUT, REPLACED);

    public HdSecurityLoginException(String message) {
        super(message);
    }
}
