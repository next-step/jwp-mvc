package core.mvc;

import javax.servlet.http.HttpServletRequest;

public interface Mapping {

  void initMapping();

  Object findController(HttpServletRequest request);
}
