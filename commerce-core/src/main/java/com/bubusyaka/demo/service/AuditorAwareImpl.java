package com.bubusyaka.demo.service;

import org.springframework.data.domain.AuditorAware;
import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    // Returning empty instead of user since we're tracking just dates not users
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.empty();
    }

    // Use the following to get the actual connected user
        /*
    public User getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        return ((MyUserDetails) authentication.getPrincipal()).getUser();
    }
    */
}