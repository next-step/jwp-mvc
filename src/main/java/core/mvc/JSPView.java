package core.mvc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class JspView implements View {

    private String pageName;

    public JspView(String pageName) {
        this.pageName = pageName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        model.entrySet().stream()
                .forEach(stringEntry -> request.setAttribute(stringEntry.getKey(), stringEntry.getValue()));
        request.getRequestDispatcher(pageName).forward(request, response);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JspView jspView = (JspView) o;
        return Objects.equals(pageName, jspView.pageName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageName);
    }

    @Override
    public String toString() {
        return "JspView{" +
                "pageName='" + pageName + '\'' +
                '}';
    }
}
