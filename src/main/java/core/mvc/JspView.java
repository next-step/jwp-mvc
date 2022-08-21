package core.mvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JspView implements View {
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";


    private String viewPath;

    public JspView(String viewPath) {
        validateViewPath(viewPath);
        this.viewPath = viewPath;
    }

    private void validateViewPath(String viewPath) {
        if (viewPath == null) {
            throw new IllegalArgumentException("전달할 viewPath 는 null 일 수 없습니다.");
        }
    }


    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (viewPath.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            response.sendRedirect(viewPath.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        model.keySet().forEach(key -> request.setAttribute(key, model.get(key)));

        RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewPath);
        requestDispatcher.forward(request, response);
    }
}
