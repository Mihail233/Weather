package org.weather.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.weather.common.service.CookieService;
import org.weather.common.service.SessionService;
import org.weather.data.entity.Session;
import org.weather.exception.UnauthenticatedException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CookieServiceImpl implements CookieService {
    public static final String USER_SESSION_COOKIE = "user_session";
    private final SessionService sessionService;

    public void setCookie(HttpServletResponse response, String name, String value, int expiresAt) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(expiresAt);

        setHeaders(response);
        response.addCookie(cookie);
    }

    private void setHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET,HEAD,OPTIONS,POST,PUT");
        response.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
    }

    public Session getAndValidateSession(HttpServletRequest request) {
        Cookie[] cookies = getCookies(request)
                .orElseThrow(() -> new UnauthenticatedException("Failed to authentication the user, please sign in"));

        String userSessionId = getUserSessionIdFrom(cookies)
                .orElseThrow(() -> new UnauthenticatedException("Failed to authentication the user, please sign in"));
        UUID sessionId = parse(userSessionId);

        return sessionService.findById(sessionId)
                .orElseThrow(() -> new UnauthenticatedException("Failed to authentication the user, please sign in"));
    }


    private Optional<Cookie[]> getCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Optional.of(cookies);
    }

    private Optional<String> getUserSessionIdFrom(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(USER_SESSION_COOKIE)) {
                return Optional.of(cookie.getValue());
            }
        }
        return Optional.empty();
    }

    private UUID parse(String sessionId) {
        try {
            return UUID.fromString(sessionId);
        } catch (NumberFormatException e) {
            throw new UnauthenticatedException("Failed to authentication the user, please sign in");
        }
    }

    public void deleteSessionCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(USER_SESSION_COOKIE, null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }
}
