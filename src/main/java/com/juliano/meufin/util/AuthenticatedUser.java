package com.juliano.meufin.util;

import com.juliano.meufin.domain.user.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticatedUser {
    public static User get() {
        var auth =  SecurityContextHolder.getContext().getAuthentication();
        return  ((User) auth.getPrincipal());

    }
}
