package core.mvc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

public class ForwardView implements View {

    private final String viewPath;

    public ForwardView(String viewPath) {
        this.viewPath = viewPath;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        model.forEach(request::setAttribute);

        request.getRequestDispatcher(viewPath).forward(request, response);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForwardView that = (ForwardView) o;
        return Objects.equals(viewPath, that.viewPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(viewPath);
    }
}
