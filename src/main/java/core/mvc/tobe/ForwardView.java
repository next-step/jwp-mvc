package core.mvc.tobe;

import core.mvc.View;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class ForwardView implements View {
    private final String viewPath;

    public ForwardView(String viewPath) {
        this.viewPath = viewPath;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        addAttribute(model, request);
        request.getRequestDispatcher(viewPath).forward(request, response);
    }

    private void addAttribute(Map<String, ?> model, HttpServletRequest request) {
        model.forEach(request::setAttribute);
    }
}
