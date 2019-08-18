package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

public class RedirectView implements View {

    private static final Map<String, View> CACHE = new WeakHashMap<>();

    private final String viewName;

    private RedirectView(final String viewName) {
        this.viewName = viewName;
    }

    static View of(final String viewName) {
        return CACHE.computeIfAbsent(viewName, RedirectView::new);
    }

    @Override
    public void render(final Map<String, ?> model,
                       final HttpServletRequest request,
                       final HttpServletResponse response) throws Exception {
        response.sendRedirect(viewName);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RedirectView)) {
            return false;
        }

        final RedirectView that = (RedirectView) o;
        return Objects.equals(viewName, that.viewName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(viewName);
    }
}
