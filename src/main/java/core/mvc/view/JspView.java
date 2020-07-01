package core.mvc.view;


import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JspView implements View {
    private static final String suffix = "/";
    private static final String prefix = ".jsp";
    private final String viewName;

    public JspView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestDispatcher rd = request.getRequestDispatcher(combinePath(viewName));
        mergeAttribute(model, request);
        rd.forward(request, response);
    }

    private void mergeAttribute(Map<String, ?> model, HttpServletRequest request) {
        model.forEach(request::setAttribute);
    }

    private String combinePath(String viewName) {
        return suffix + viewName + prefix;
    }
}
