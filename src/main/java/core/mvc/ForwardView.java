package core.mvc;

import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public final class ForwardView implements View {

    private final String viewName;

    private ForwardView(String viewName) {
        Assert.hasText(viewName, "'viewName' must not be blank");
        this.viewName = viewName;
    }

    public static ForwardView from(String viewName) {
        return new ForwardView(viewName);
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        model.forEach(request::setAttribute);
        request.getRequestDispatcher(viewName)
                .forward(request, response);
    }
}
