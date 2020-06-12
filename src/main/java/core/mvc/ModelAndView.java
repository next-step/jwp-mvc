package core.mvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    private View view;
    private String viewName;
    private Map<String, Object> model = new HashMap<String, Object>();

    public ModelAndView() {
    }

    public ModelAndView(View view) {
        this.view = view;
    }

    public ModelAndView(String view) {
        this.viewName = view;
    }

    public ModelAndView addObject(String attributeName, Object attributeValue) {
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
        return view;
    }

    public String getViewName() {
        return viewName;
    }
}
