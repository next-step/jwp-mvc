package core.mvc.view;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JspView implements View {
    private static final String SUFFIX = ".jsp";
    private final String viewName;

    public JspView(final String viewName) {
        validate(viewName);

        this.viewName = viewName;
    }

    private void validate(String viewName) {
        if (viewName == null || viewName.isEmpty()) {
            throw new IllegalArgumentException("ViewName is null or empty");
        }

        if (!viewName.endsWith(SUFFIX)) {
            throw new IllegalArgumentException(viewName + " is not jsp resource");
        }
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        model.keySet()
                .forEach(key -> request.setAttribute(key, model.get(key)));

        // not render but bypass to tomcat...?
        RequestDispatcher rd = request.getRequestDispatcher(viewName);
        rd.forward(request, response);
    }

}
