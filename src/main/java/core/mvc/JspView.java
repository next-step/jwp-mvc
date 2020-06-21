package core.mvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JspView implements View {

    private String path;

    public JspView(String path) {
        this.path = path;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        for (String key : model.keySet()) {
            request.setAttribute(key, model.get(key));
        }
        RequestDispatcher rd = request.getRequestDispatcher(path);
        rd.forward(request, response);
    }
}
