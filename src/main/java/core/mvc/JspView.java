package core.mvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JspView implements View {

    private final String viewPath;

    public JspView(String viewPath) {
        this.viewPath = viewPath;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        for (String attributeName : model.keySet()) {
            request.setAttribute(attributeName, model.get(attributeName));
        }

        RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewPath);
        requestDispatcher.forward(request, response);
    }
}
