package com.miniyus.friday.infrastructure.config;

import com.miniyus.friday.common.util.MessageUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

/**
 * Locale Configuration
 * - get locale from Accept-Language header
 * - default KOREA
 * 
 * @author miniyus
 * @since 2023/08/28
 */
@Configuration
public class LocaleConfig implements WebMvcConfigurer {
    /**
     * Creates and returns a LocaleResolver bean.
     *
     * @return the LocaleResolver bean
     */
    @Bean
    public LocaleResolver localeResolver() {
        // Create a new instance of AcceptHeaderLocaleResolver
        var localeResolver = new AcceptHeaderLocaleResolver();

        // Set the default locale to Locale.KOREA
        localeResolver.setDefaultLocale(Locale.KOREA);

        // Return the LocaleResolver bean
        return localeResolver;
    }

    @Bean
    public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource) {
        MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(messageSource);
        MessageUtil.setMessageSourceAccessor(messageSourceAccessor); // ⚡️ 아래의 MessageUtil 클래스에 세팅
        return messageSourceAccessor;
    }
}
