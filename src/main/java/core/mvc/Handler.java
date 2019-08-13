package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Handler<T> {
    T handle(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
