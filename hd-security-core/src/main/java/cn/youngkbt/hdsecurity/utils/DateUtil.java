package cn.youngkbt.hdsecurity.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Tianke
 * @date 2024/11/26 01:26:49
 * @since 1.0.0
 */
public class DateUtil {
    public static String getCurrentTime() {
        return System.currentTimeMillis() + "";
    }
    public static String now() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
