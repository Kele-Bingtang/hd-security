package cn.youngkbt.hdsecurity.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Tianke
 * @date 2024/11/26 01:26:49
 * @since 1.0.0
 */
public class DateUtil {
    
    private DateUtil() {
    }

    /**
     * 获取当前时间戳
     *
     * @return 时间戳
     */
    public static String getCurrentTime() {
        return System.currentTimeMillis() + "";
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间
     */
    public static String now() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 指定当前时间戳后的毫秒，将其格式化为 yyyy-MM-dd HH:mm:ss
     *
     * @param millisSecond 当前时间戳后的毫秒
     * @return 格式化后的时间
     */
    public static String formatDateTime(long millisSecond) {
        Instant instant = Instant.ofEpochMilli(System.currentTimeMillis() + millisSecond);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
        return formatDateTime(zonedDateTime);
    }

    /**
     * 将日期格式化为 yyyy-MM-dd HH:mm:ss
     *
     * @param zonedDateTime 日期
     * @return 格式化后的时间
     */
    public static String formatDateTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
