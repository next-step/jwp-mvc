package core.mvc.tobe;

import core.mvc.View;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JspView implements View {

  private String path;

  public JspView(String path) {
    this.path = path;
  }

  @Override
  public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
      throws Exception {

  }
}
