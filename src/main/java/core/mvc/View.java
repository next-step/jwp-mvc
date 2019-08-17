package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface View {

    void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception;

    static View parse(String viewName) {
        if (RedirectView.isRedirect(viewName)) {
            return new RedirectView(viewName);
        }
        return new JspView(viewName);
    }
}
