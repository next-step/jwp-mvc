package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by hspark on 2019-08-16.
 */
public interface HandlerExecutor {
    void execute(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception;
}
