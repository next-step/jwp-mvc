package core.mvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class JSPView implements View {

    public static final String REDIRECT_PREFIX = "redirect:";
    private String pageName;

    public JSPView(String pageName) {
        this.pageName = pageName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (pageName.startsWith(REDIRECT_PREFIX)) {
            String page = pageName.substring(REDIRECT_PREFIX.length() + 1);
            response.sendRedirect(page);
            return;
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(pageName);
        requestDispatcher.forward(request, response);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JSPView jspView = (JSPView) o;
        return Objects.equals(pageName, jspView.pageName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageName);
    }

    @Override
    public String toString() {
        return "JSPView{" +
                "pageName='" + pageName + '\'' +
                '}';
    }
}
