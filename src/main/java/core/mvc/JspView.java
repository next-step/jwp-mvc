package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JspView implements View {

    private final String viewName;

    JspView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(final Map<String, ?> model,
                       final HttpServletRequest request,
                       final HttpServletResponse response) throws Exception {
        request.getRequestDispatcher(viewName)
                .forward(request, response);
    }
}
