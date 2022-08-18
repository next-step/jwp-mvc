package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ViewImplement implements View {

    private String viewName;

    public ViewImplement() {
    }

    public ViewImplement(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

    }

    @Override
    public String getViewName() {
        return viewName;
    }
}
