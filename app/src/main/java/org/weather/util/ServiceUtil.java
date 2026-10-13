package org.weather.util;

import lombok.experimental.UtilityClass;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.NestedRuntimeException;

@UtilityClass
public class ServiceUtil {
    public static boolean isConstraintViolationException(NestedRuntimeException e) {
        Throwable cause = e.getCause();
        return ConstraintViolationException.class.isAssignableFrom(cause.getClass());
    }
}
