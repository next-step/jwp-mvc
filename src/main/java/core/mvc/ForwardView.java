package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ForwardView implements View {

    private final String viewPath;

    public ForwardView(String viewPath) {
        this.viewPath = viewPath;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        model.forEach(request::setAttribute);

        request.getRequestDispatcher(viewPath).forward(request, response);
    }
}
