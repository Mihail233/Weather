package org.weather.web.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.weather.common.service.SessionService;
import org.weather.data.entity.Session;
import org.weather.exception.CookieNotFoundException;
import org.weather.exception.InvalidSessionException;
import org.weather.exception.InvalidSessionIdException;
import org.weather.util.CookieUtil;

import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class PrivateEndpointInterceptor implements HandlerInterceptor {
    public static final String USER_ID_ATTRIBUTE = "user_id";
    public static final String USER_NAME_ATTRIBUTE = "user_name";
    public static final String SESSION_ID_ATTRIBUTE = "session_id";

    private static final String GET = "GET";
    private static final String POST = "POST";

    private static final Map<String, Set<String>> PRIVATE_ENDPOINTS = Map.of(
            "/weather", Set.of(GET),
            "/location/search", Set.of(GET, POST),
            "/location/add", Set.of(POST),
            "/location/delete", Set.of(POST),
            "/sign-out", Set.of(POST)
    );

    private final SessionService sessionService;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        if (isPrivateEndpoint(request)) {
            Cookie[] cookies = CookieUtil.getCookies(request)
                    .orElseThrow(() -> new CookieNotFoundException("Cookies were not found, please sign in"));

            String userSessionId = CookieUtil.getUserSessionIdFrom(cookies)
                    .orElseThrow(() -> new InvalidSessionIdException("Invalid session ID, please sign in"));
            Long sessionId = CookieUtil.parse(userSessionId);

            Session session = sessionService.findById(sessionId)
                    .orElseThrow(() -> new InvalidSessionException("Session not found, please sign in"));

            sessionService.deleteExpired(session);

            request.setAttribute(USER_ID_ATTRIBUTE, session.getUser().getId());
            request.setAttribute(USER_NAME_ATTRIBUTE, session.getUser().getLogin());
            request.setAttribute(SESSION_ID_ATTRIBUTE, userSessionId);
        }
        return true;
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler,
                           @Nullable ModelAndView modelAndView) {
        if (isPrivateEndpoint(request)) {
            assert modelAndView != null;
            modelAndView.addObject(USER_NAME_ATTRIBUTE, request.getAttribute(USER_NAME_ATTRIBUTE));
        }
    }

    private boolean isPrivateEndpoint(HttpServletRequest request) {
        String path = request.getServletPath();
        String method = request.getMethod();

        return PRIVATE_ENDPOINTS.containsKey(path) && PRIVATE_ENDPOINTS.get(path).contains(method);
    }
}
