package core.mvc;

import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JspView implements View {

    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private String path;

    public JspView(String path) {
        this.path = path;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (this.path.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            resp.sendRedirect(this.path.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        RequestDispatcher requestDispatcher = req.getRequestDispatcher(this.path);
        requestDispatcher.forward(req, resp);
    }

}
