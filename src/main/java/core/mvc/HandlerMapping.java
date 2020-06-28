package core.mvc;

import core.mvc.tobe.HandlerExecution;
import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    HandlerExecution getHandler(HttpServletRequest request);
}
