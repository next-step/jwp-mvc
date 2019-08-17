package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

public class JspView implements View {

    private static final Map<String, View> CACHE = new WeakHashMap<>();

    private final String viewName;

    private JspView(final String viewName) {
        this.viewName = viewName;
    }

    static View of(final String viewName) {
        return CACHE.computeIfAbsent(viewName, JspView::new);
    }

    @Override
    public void render(final Map<String, ?> model,
                       final HttpServletRequest request,
                       final HttpServletResponse response) throws Exception {
        request.getRequestDispatcher(viewName)
                .forward(request, response);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JspView)) {
            return false;
        }

        final JspView that = (JspView) o;
        return Objects.equals(viewName, that.viewName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(viewName);
    }
}
