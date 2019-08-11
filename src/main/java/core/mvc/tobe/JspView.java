package core.mvc.tobe;

import core.mvc.View;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JspView implements View {

    private String view;

    public JspView(String viewName) {
        this.view = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
        throws Exception {

        RequestDispatcher requestDispatcher = request.getRequestDispatcher(this.view);
        model.forEach(request::setAttribute);
        requestDispatcher.forward(request, response);
    }
}
