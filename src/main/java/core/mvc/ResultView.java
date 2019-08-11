package core.mvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ResultView implements View {
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private String urlPath;

    public ResultView(String urlPath) {
        this.urlPath = urlPath;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (urlPath.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            resp.sendRedirect(urlPath.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        RequestDispatcher rd = req.getRequestDispatcher(urlPath);
        rd.forward(req, resp);
    }
}
