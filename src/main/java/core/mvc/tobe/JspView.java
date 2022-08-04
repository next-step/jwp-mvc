package core.mvc.tobe;

import core.mvc.View;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JspView implements View {

    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private final String jspPath;

    public JspView(final String jspPath) {
        this.jspPath = jspPath;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (jspPath.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            response.sendRedirect(jspPath.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        if (hasModel(model)) {
            copyModelObjectsIntoRequestAttributes(model, request);
        }

        final RequestDispatcher requestDispatcher = request.getRequestDispatcher(jspPath);
        requestDispatcher.forward(request, response);
    }

    private static boolean hasModel(final Map<String, ?> model) {
        return model != null && !model.isEmpty();
    }

    private static void copyModelObjectsIntoRequestAttributes(final Map<String, ?> model, final HttpServletRequest request) {
        for (final Entry<String, ?> entry : model.entrySet()) {
            request.setAttribute(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final JspView jspView = (JspView) o;
        return Objects.equals(jspPath, jspView.jspPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jspPath);
    }
}
