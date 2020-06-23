package core.mvc;

import core.mvc.view.JspView;
import core.mvc.view.RedirectView;
import core.mvc.view.View;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class ModelAndView {

    private View view;
    private Map<String, Object> model = new HashMap<>();

    public ModelAndView(View view) {
        this.view = view;
    }

    public static ModelAndView withJspView(String viewName) {
        return new ModelAndView(new JspView(viewName));
    }

    public static ModelAndView withRedirectView(String location) {
        return new ModelAndView(new RedirectView(location));
    }

    public void render(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (this.view == null) {
            return;
        }

        this.view.render(model, request, response);
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
}
