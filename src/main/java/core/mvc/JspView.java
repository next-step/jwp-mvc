package core.mvc;

import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JspView implements View{
	private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    
	private final String viewName;
	
	public JspView(String viewName) {
		this.viewName = viewName;
	}

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (this.viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
			response.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }
		
		modelToAttribute(model, request);

        RequestDispatcher rd = request.getRequestDispatcher(viewName);
        rd.forward(request, response);
	}
	
	private void modelToAttribute(Map<String, ?> model, HttpServletRequest request) {
		if(model == null) {
			return;
		}
		
		for(Map.Entry<String, ?> entry : model.entrySet()) {
			request.setAttribute(entry.getKey(), entry.getValue());	
		}
	}

}
