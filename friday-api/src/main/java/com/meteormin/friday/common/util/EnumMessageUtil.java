package com.meteormin.friday.common.util;

/**
 * EnumMessageUtil
 *
 * @author seongminyoo
 * @since 2023/10/23
 */
public class EnumMessageUtil {

    private EnumMessageUtil() {
    }

    /**
     * enum 값을 spring message resource를 이용하여 언어 변활을 수행 한다.
     *
     * @param enumClass 변환할 enum class
     * @return 변환된 메시지
     */
    public static String getMessage(Enum<?> enumClass) {
        if (enumClass instanceof HasText) {
            return MessageUtil.getMessage(((HasText) enumClass).text());
        } else {
            return MessageUtil.getMessage(enumClass.name());
        }
    }
}
