package core.mvc.tobe;

import core.mvc.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ForwardView implements View {

    private final String viewName;

    public ForwardView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        model.forEach(request::setAttribute);
        request.getRequestDispatcher(viewName).forward(request, response);
    }
}
