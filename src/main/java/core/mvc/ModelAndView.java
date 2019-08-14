package core.mvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

  public void render(Map<String, Object> model, HttpServletRequest req, HttpServletResponse resp)
      throws Exception {
    view.render(model, req, resp);
  }
}
