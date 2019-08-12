package core.mvc.tobe.view;

import core.mvc.tobe.AnnotationHandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class RedirectView implements View {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private String viewName;

    public RedirectView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("redirect view : {}", viewName);
        response.sendRedirect(viewName);
    }
}
