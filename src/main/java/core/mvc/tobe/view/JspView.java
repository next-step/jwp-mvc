package core.mvc.tobe.view;

import core.mvc.tobe.view.exception.ForwardException;
import core.mvc.tobe.view.exception.RedirectException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class JspView implements View {
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private String mappedUri;

    public JspView(String mappedUri) {
        this.mappedUri = mappedUri;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) {
        if (isRedirect()) {
            sendRedirect(response);
        }

        if (isForward()) {
            forward(request, response);
        }
    }

    private void sendRedirect(HttpServletResponse response) {
        try {
            response.sendRedirect(mappedUri.substring(DEFAULT_REDIRECT_PREFIX.length()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RedirectException(e);
        }
    }

    private void forward(HttpServletRequest request, HttpServletResponse response){
        RequestDispatcher rd = request.getRequestDispatcher(mappedUri);
        try {
            rd.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            throw new ForwardException(e);
        }
    }

    private boolean isForward() {
        return isRedirect() == false;
    }

    private boolean isRedirect() {
        return this.mappedUri.startsWith(DEFAULT_REDIRECT_PREFIX);
    }
}
