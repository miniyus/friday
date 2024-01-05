package com.meteormin.friday.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigUtil {
    private static String profile;

    private ConfigUtil() {
    }

    @Value("${spring.profiles.active}")
    public static void setProfile(String profile) {
        ConfigUtil.profile = profile;
    }

    public static String getProfile() {
        return ConfigUtil.profile;
    }
}
