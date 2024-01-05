package com.meteormin.friday.api.auth.resource;

import com.meteormin.friday.auth.domain.Auth;

public record AuthUserResource(
    Long id,
    String email,
    String name,
    String role,
    String snsId,
    String provider
) {
    public static AuthUserResource fromDomain(Auth user) {
        String provider;
        if (user.getProvider() == null) {
            provider = null;
        } else if (user.getProvider().isBlank()) {
            provider = null;
        } else {
            provider = user.getProvider();
        }

        return new AuthUserResource(
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getRole().value(),
            user.getSnsId(),
            provider
        );
    }
}
