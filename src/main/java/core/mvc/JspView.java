package core.mvc;

import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JspView implements View {

  private static final String JSP_VIEW_POSTFIX = ".jsp";
  private String path;

  public JspView(String path) {
    this.path = path;
  }

  @Override
  public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
      throws Exception {

    model.forEach(request::setAttribute);
    RequestDispatcher rd = request.getRequestDispatcher(path + JSP_VIEW_POSTFIX);
    rd.forward(request, response);
  }
}
