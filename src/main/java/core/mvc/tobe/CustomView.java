package core.mvc.tobe;

import core.mvc.View;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomView implements View  {

	private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

	private final String name;

	public CustomView(String name) {
		this.name = name;
	}

	@Override public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		if (name.startsWith(DEFAULT_REDIRECT_PREFIX)) {
			response.sendRedirect(name.substring(DEFAULT_REDIRECT_PREFIX.length()));
			return;
		}

		RequestDispatcher rd = request.getRequestDispatcher(name);
		rd.forward(request, response);
	}

}
