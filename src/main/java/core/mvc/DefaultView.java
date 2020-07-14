package core.mvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class DefaultView implements View {
    private static final String REDIRECT = "redirect:";

    private final String viewName;

    public DefaultView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (viewName.startsWith(REDIRECT)) {
            response.sendRedirect(viewName.substring(REDIRECT.length()));
            return;
        }
        setModelAttributes(model, request);

        RequestDispatcher rd = request.getRequestDispatcher(viewName);
        rd.forward(request, response);
    }

    private void setModelAttributes(final Map<String, ?> model, final HttpServletRequest request) {
        model.entrySet().stream()
                .forEach(entry -> request.setAttribute(entry.getKey(), entry.getValue()));
    }

    public String getViewName() {
        return viewName;
    }

    @Override
    public String toString() {
        return "DefaultView{" +
                "viewName='" + viewName + '\'' +
                '}';
    }
}
