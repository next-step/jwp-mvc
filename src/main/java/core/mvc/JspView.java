package core.mvc;

import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JspView implements View{
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    private final String path;

    public JspView(String path) {
        this.path = path;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (path.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            response.sendRedirect(path.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        RequestDispatcher rd = request.getRequestDispatcher(path);
        rd.forward(request, response);
    }
}
