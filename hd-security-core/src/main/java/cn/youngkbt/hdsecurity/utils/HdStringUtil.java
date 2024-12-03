package cn.youngkbt.hdsecurity.utils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 字符串工具类
 *
 * @author Tianke
 * @date 2024/11/25 01:26:36
 * @since 1.0.0
 */
public class HdStringUtil {

    public static boolean hasText(String str) {
        return str != null && !str.isBlank();
    }

    public static boolean hasText(String... content) {
        for (String s : content) {
            if (!hasText(s)) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasText(Object str) {
        return str != null && !"".equals(str);
    }

    public static boolean hasEmpty(String content) {
        return !hasText(content);
    }

    public static boolean hasEmpty(String... content) {
        for (String s : content) {
            if (!hasEmpty(s)) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasEmpty(Object content) {
        return !hasText(content);
    }

    public static boolean hasAnyText(String... content) {
        for (String s : content) {
            if (hasText(s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasAnyEmpty(String... content) {
        for (String s : content) {
            if (hasEmpty(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 生成指定长度的随机字符串
     *
     * @param length 字符串的长度
     * @return 一个随机字符串
     */
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = ThreadLocalRandom.current().nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 字符串模糊匹配
     * <p>example:
     * <p> user* user-add   --  true
     * <p> user* art-add    --  false
     *
     * @param pattern 表达式
     * @param str     待匹配的字符串
     * @return 是否可以匹配
     */
    public static boolean vagueMatch(String pattern, String str) {
        // 两者均为 null 时，直接返回 true
        if (pattern == null && str == null) {
            return true;
        }
        // 两者其一为 null 时，直接返回 false
        if (pattern == null || str == null) {
            return false;
        }
        // 如果表达式不带有*号，则只需简单equals即可 (这样可以使速度提升200倍左右)
        if (!pattern.contains("*")) {
            return pattern.equals(str);
        }
        // 深入匹配
        return vagueMatchMethod(pattern, str);
    }

    /**
     * 字符串模糊匹配
     *
     * @param pattern 表达式
     * @param str     待匹配的字符串
     * @return 是否可以匹配
     */
    private static boolean vagueMatchMethod(String pattern, String str) {
        int m = str.length();
        int n = pattern.length();
        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[0][0] = true;
        for (int i = 1; i <= n; ++i) {
            if (pattern.charAt(i - 1) == '*') {
                dp[0][i] = true;
            } else {
                break;
            }
        }
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                if (pattern.charAt(j - 1) == '*') {
                    dp[i][j] = dp[i][j - 1] || dp[i - 1][j];
                } else if (str.charAt(i - 1) == pattern.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                }
            }
        }
        return dp[m][n];
    }
}
