package core.mvc;


import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Created by hspark on 2019-08-16.
 */
public interface RequestMappingHandler {
    void initialize();
    Optional<HandlerExecutor> getHandlerExecutor(HttpServletRequest httpServletRequest);
}
