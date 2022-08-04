package core.mvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class DefaultView implements View{
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private final String name;

    public DefaultView(String name) {
        this.name = name;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (name.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            response.sendRedirect(name.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        RequestDispatcher rd = request.getRequestDispatcher(name);
        rd.forward(request, response);
    }
}
