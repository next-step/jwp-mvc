package core.mvc;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RedirectView implements View {

  private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
  private String path;

  public RedirectView(String path) {
    this.path = path;
  }

  @Override
  public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    response.sendRedirect(path.substring(DEFAULT_REDIRECT_PREFIX.length()));
  }
}
