package com.miniyus.friday.common.util;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;

/**
 * 언어 변환 util
 *
 * @author miniyus
 * @since 2023/10/19
 */
public class MessageUtil {
    private static MessageSourceAccessor messageSourceAccessor;

    private MessageUtil() {
    }

    /**
     * Retrieves a message from the message source based on the provided key.
     *
     * @param key the key used to retrieve the message
     * @return the retrieved message
     */
    public static String getMessage(String key) {
        return messageSourceAccessor.getMessage(key, key, LocaleContextHolder.getLocale());
    }

    /**
     * Retrieves the message for the given key from the message source accessor.
     *
     * @param key    the key for the message
     * @param params the parameters to be substituted in the message
     * @return the retrieved message, or the key if the message is not found
     */
    public static String getMessage(String key, Object... params) {
        return messageSourceAccessor.getMessage(key, params, key, LocaleContextHolder.getLocale());
    }

    /**
     * Translates the given message code into a localized message.
     *
     * @param messageCode the code of the message to be translated
     * @return the translated message
     */
    public static String getMessage(String messageCode, String defaultMessage) {
        return messageSourceAccessor.getMessage(
            messageCode,
            null,
            defaultMessage,
            LocaleContextHolder.getLocale());
    }

    /**
     * Retrieves a message based on the provided message code, default message, and arguments.
     *
     * @param messageCode    the code of the message to retrieve
     * @param defaultMessage the default message to use if the message code is not found
     * @param args           the arguments to be used in formatting the message
     * @return the retrieved message
     */
    public static String getMessage(String messageCode,
        String defaultMessage,
        Object... args) {
        return messageSourceAccessor.getMessage(
            messageCode,
            args,
            defaultMessage,
            LocaleContextHolder.getLocale());
    }

    /**
     * Sets the message source accessor for the Java function.
     *
     * @param messageSourceAccessor the message source accessor to be set
     */
    public static void setMessageSourceAccessor(MessageSourceAccessor messageSourceAccessor) {
        MessageUtil.messageSourceAccessor = messageSourceAccessor;
    }
}
