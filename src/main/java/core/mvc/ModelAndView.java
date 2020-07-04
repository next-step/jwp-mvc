package core.mvc;

import core.mvc.view.JspView;
import core.mvc.view.RedirectView;
import core.mvc.view.View;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    private String viewName;
    private View view;
    private Map<String, Object> model = new HashMap<String, Object>();

    public ModelAndView() {
    }

    public ModelAndView(View view) {
        this.view = view;
    }

    public ModelAndView(String viewName) {
        this.viewName = viewName;
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
        if (viewName != null && viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            return new RedirectView(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
        }
        if(view == null) {
            return new JspView(viewName);
        }
        return view;
    }
}
