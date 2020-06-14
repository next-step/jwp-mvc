package core.mvc.view;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    private View view;
    private String viewName;
    private final Map<String, Object> model = new HashMap<>();

    public ModelAndView() {
    }

    public ModelAndView(final View view) {
        this.view = view;
    }

    public ModelAndView(final String viewName) {
        this.viewName = viewName;
    }

    public ModelAndView addObject(final String attributeName, final Object attributeValue) {
        model.put(attributeName, attributeValue);
        return this;
    }

    public Object getObject(final String attributeName) {
        return model.get(attributeName);
    }

    public Map<String, Object> getModel() {
        return Collections.unmodifiableMap(model);
    }

    public void setView(final View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }

    public void setViewName(final String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }
}
