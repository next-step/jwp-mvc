package core.mvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

public class JspView implements View {

    private final String path;

    public JspView(String path) {
        this.path = path;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (path.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            response.sendRedirect(path.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        RequestDispatcher rd = request.getRequestDispatcher(path);
        rd.forward(request, response);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JspView)) return false;
        JspView jspView = (JspView) o;
        return Objects.equals(path, jspView.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
