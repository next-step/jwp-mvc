package core.mvc.tobe;

import core.mvc.View;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JspView implements View {

    private final String viewName;

    public JspView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        model.forEach(request::setAttribute);
        RequestDispatcher dispatcher = request.getRequestDispatcher(String.format("/%s.jsp", viewName));
        dispatcher.forward(request, response);
    }
}
