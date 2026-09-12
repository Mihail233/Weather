package org.weather.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.weather.exception.InvalidSessionIdException;

import java.util.Optional;

@UtilityClass
public class CookieUtil {
    public static final String USER_SESSION_COOKIE = "user_session";

    public void setCookie(HttpServletResponse response, String name, String value, int expiresAt) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(expiresAt);

        response.addCookie(cookie);
    }

    public Optional<Cookie[]> getCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Optional.of(cookies);
    }

    public Optional<String> getUserSessionIdFrom(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(USER_SESSION_COOKIE)) {
                return Optional.of(cookie.getValue());
            }
        }
        return Optional.empty();
    }

    public Long parse(String sessionId) {
        try {
            return Long.valueOf(sessionId);
        } catch (NumberFormatException e) {
            throw new InvalidSessionIdException("Session Id not correct");
        }
    }
}
