package core.mvc.view;

import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JspView implements View {

    private String path;

    public JspView(String path) {
        this.path = path;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        model.forEach((name, value) -> request.setAttribute(name, value));

        RequestDispatcher rd = request.getRequestDispatcher(path);
        rd.forward(request, response);
    }
}
