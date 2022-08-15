package core.mvc;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {

	Object getHandler(HttpServletRequest request);

	default boolean supports(HttpServletRequest request) {
		return !Objects.isNull(getHandler(request));
	}
}
