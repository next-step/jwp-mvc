package core.mvc;

import core.mvc.view.View;
import core.mvc.view.ViewResolvers;
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

    private static final ViewResolvers VIEW_RESOLVERS = new ViewResolvers();

    private View view;
    private Map<String, Object> model = new HashMap<>();

    public ModelAndView(View view) {
        this.view = view;
    }

    public ModelAndView(String viewName) {
        this.view = VIEW_RESOLVERS.resolve(viewName);
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
