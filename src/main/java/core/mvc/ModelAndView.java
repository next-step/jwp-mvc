package core.mvc;

import core.mvc.view.View;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    /** View instance or view name String. */
    private Object view;
    private Map<String, Object> model = new HashMap<>();

    public ModelAndView() {
    }

    public ModelAndView(Object view) {
        this.view = view;
    }

    public ModelAndView addObject(String attributeName,
                                  Object attributeValue) {
        model.put(attributeName, attributeValue);
        return this;
    }

    public Object getObject(String attributeName) {
        return model.get(attributeName);
    }

    public Map<String, Object> getModel() {
        return Collections.unmodifiableMap(model);
    }

    public View getView() {
        return (this.view instanceof View ? (View) this.view : null);
    }

    public String getViewName() {
        return (this.view instanceof String ? (String) this.view : null);
    }
}
