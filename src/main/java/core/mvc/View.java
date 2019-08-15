package core.mvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface View {
    default void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(getViewName());
        requestDispatcher.forward(request, response);
    }

    String getViewName();
}
