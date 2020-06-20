package core.mvc.support.resolvers;

import core.annotation.web.CookieValue;
import core.mvc.support.MethodParameter;
import core.mvc.utils.TypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

public class CookieValueHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger log = LoggerFactory.getLogger(CookieValueHandlerMethodArgumentResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return Optional
                .ofNullable(methodParameter.getParameterAnnotation(CookieValue.class))
                .isPresent();
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request) {
        final CookieValue cookieValue = methodParameter.getParameterAnnotation(CookieValue.class);
        final String cookieName = cookieValue.value().isEmpty() ? methodParameter.getParameterName() : cookieValue.value();
        final Optional<String> maybeCookieValue = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue);

        if (maybeCookieValue.isPresent()) {
            final Object converted = TypeConverter.convert(maybeCookieValue.get(), methodParameter.getParameterType());
            log.debug("converted: {}", converted);
            return converted;
        }
        return null;
    }
}
