package org.weather.util;

import jakarta.persistence.PersistenceException;
import lombok.experimental.UtilityClass;
import org.springframework.core.NestedRuntimeException;

@UtilityClass
public class ServiceUtil {
    public static boolean isPersistenceException(NestedRuntimeException e) {
        Throwable cause = e.getCause();
        return PersistenceException.class.isAssignableFrom(cause.getClass());
    }

}
