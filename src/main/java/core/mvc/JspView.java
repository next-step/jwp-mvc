package core.mvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JspView implements View {
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    private final String viewName;
    private final boolean isRedirect;

    public JspView(final String pathName) {
        boolean isRedirect = pathName.startsWith(DEFAULT_REDIRECT_PREFIX);
        this.viewName = isRedirect ? pathName.substring(DEFAULT_REDIRECT_PREFIX.length()) : pathName;
        this.isRedirect = isRedirect;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (isRedirect) {
            response.sendRedirect(viewName);
            return;
        }
        RequestDispatcher rd = request.getRequestDispatcher(viewName);
        rd.forward(request, response);
    }

}
