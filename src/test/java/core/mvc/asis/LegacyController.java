package core.mvc.asis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LegacyController implements Controller {

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse resp) throws Exception {

        final String name = req.getParameter("userId");
        if (name == null) {
            return "redirect:/users/loginForm";
        }

        req.setAttribute("welcome", "hi " + name);
        return "/home.jsp";
    }
}
