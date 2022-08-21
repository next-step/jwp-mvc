package core.mvc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    private View view;
    private final Map<String, Object> model = new HashMap<>();

    public ModelAndView() {
    }

    public ModelAndView(View view) {
        this.view = view;
    }

    public static ModelAndView fromJspView(String viewName) {
        return new ModelAndView(new JspView(viewName));
    }

    public void addObject(String attributeName, Object attributeValue) {
        model.put(attributeName, attributeValue);
    }

    public Object getObject(String attributeName) {
        return model.get(attributeName);
    }

    public Map<String, Object> getModel() {
        return Collections.unmodifiableMap(model);
    }

    public View getView() {
        return view;
    }

    public void render(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            view.render(model, request, response);
        } catch (IOException | ServletException e) {
            throw new ServletException(e.getMessage(), e);
        }
    }
}
