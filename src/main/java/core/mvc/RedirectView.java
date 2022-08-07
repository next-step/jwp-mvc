package core.mvc;

import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public final class RedirectView implements View {

    private static final String ROOT_PATH = "/";

    private final String viewName;

    private RedirectView(String viewName) {
        Assert.hasText(viewName, "`viewName` must not be blank");
        this.viewName = viewName;
    }

    public static RedirectView root() {
        return new RedirectView(ROOT_PATH);
    }

    public static RedirectView from(String viewName) {
        return new RedirectView(viewName);
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendRedirect(viewName);
    }
}
