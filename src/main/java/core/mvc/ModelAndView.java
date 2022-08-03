package core.mvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ModelAndView {
    private View view;
    private Map<String, Object> model = new HashMap<String, Object>();

    public ModelAndView() {
    }

    public ModelAndView(View view) {
        this.view = view;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ModelAndView that = (ModelAndView) o;
        return Objects.equals(getView(), that.getView()) && Objects.equals(getModel(), that.getModel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getView(), getModel());
    }
}
