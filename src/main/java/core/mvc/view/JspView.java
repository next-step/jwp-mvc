package core.mvc.view;

import lombok.RequiredArgsConstructor;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RequiredArgsConstructor
public class JspView implements View {

    private static final String JSP_VIEW_SUFFIX = ".jsp";

    private final String viewName;

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName + JSP_VIEW_SUFFIX);
        requestDispatcher.forward(request, response);
    }
}
