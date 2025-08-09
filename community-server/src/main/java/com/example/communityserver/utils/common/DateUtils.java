package com.example.communityserver.utils.common;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * <p>
 * 时间工具
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-05
 **/


public class DateUtils {
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static Date parse(String dateString) throws ParseException {
        return new SimpleDateFormat(DEFAULT_PATTERN).parse(dateString);
    }

    public static Date parseSafe(String dateString) {
        try {
            return parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException("日期解析失败: " + dateString, e);
        }
    }
}
