package org.weather.common.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.weather.data.entity.Session;

public interface CookieService {
    void setCookie(HttpServletResponse response, String name, String value, int expiresAt);

    Session getAndValidateSession(HttpServletRequest request);

    void deleteSessionCookie(HttpServletResponse response);

}
