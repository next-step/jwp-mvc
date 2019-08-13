package core.mvc.tobe.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ForwardView implements View {

    private static final Logger logger = LoggerFactory.getLogger(ForwardView.class);

    private String viewName;

    public ForwardView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("forward view : {}", viewName);
        request.getRequestDispatcher(viewName).forward(request, response);
    }
}
