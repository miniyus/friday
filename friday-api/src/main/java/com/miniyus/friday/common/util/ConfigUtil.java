package com.miniyus.friday.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConfigUtil {
    private static String profile;

    @Value("${spring.profiles.active}")
    public static void setProfile(String profile) {
        ConfigUtil.profile = profile;
    }

    public static String getProfile() {
        return ConfigUtil.profile;
    }
}
