package core.mvc.view;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

public class JspView implements View {

    private final String viewName;

    public JspView(String viewName) {
        Objects.requireNonNull(viewName, "._. What are you..?: " + viewName);
        if (viewName.isEmpty()) {
            throw new IllegalArgumentException("name is empty.");
        }

        if (!viewName.endsWith(".jsp")) {
            throw new IllegalArgumentException("Invalid jsp view name.");
        }
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        model.forEach(request::setAttribute);
        final RequestDispatcher rd = request.getRequestDispatcher(viewName);
        rd.forward(request, response);
    }
}
