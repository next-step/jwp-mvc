package core.mvc;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping<T> {
    T getHandler(HttpServletRequest request);
}
