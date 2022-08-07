package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

public class ModelAndView {

    private final Map<String, Object> model;
    private final View view;

    private ModelAndView(Map<String, Object> model, View view) {
        this.model = Collections.unmodifiableMap(model);
        this.view = view;
    }

    public static ModelAndView of(Map<String, Object> model, View view) {
        return new ModelAndView(model, view);
    }

    public static ModelAndView from(View view) {
        return of(Collections.emptyMap(), view);
    }

    public Object getObject(String attributeName) {
        return model.get(attributeName);
    }

    public void viewRender(HttpServletRequest request, HttpServletResponse response) throws Exception {
        view.render(model, request, response);
    }
}
