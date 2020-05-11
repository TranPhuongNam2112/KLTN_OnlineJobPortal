package com.datn.onlinejobportal.security;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

public class MyAnonymousAuthenticationFilter extends AnonymousAuthenticationFilter {
    private static final String USER_SESSION_KEY = "user";
    private final String key;

    public MyAnonymousAuthenticationFilter(String key) {
        super(key);
        this.key = key;
    }

    @Override
    protected Authentication createAuthentication(HttpServletRequest req) {
        HttpSession httpSession = req.getSession();
        UserPrincipal user = Optional.ofNullable((UserPrincipal) httpSession.getAttribute(USER_SESSION_KEY))
                .orElseGet(() -> {
                    UserPrincipal anon = new UserPrincipal();
                    anon.setFullname("annonymousUser");
                    httpSession.setAttribute(USER_SESSION_KEY, anon);
                    return anon;
                });
        return new AnonymousAuthenticationToken(key, user, AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
    }
}