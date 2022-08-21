package core.mvc.asis;

import core.mvc.JspView;
import core.mvc.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForwardController implements Controller {
    private static final String EMPTY_URL = "forwardUrl is null. 이동할 URL을 입력하세요.";

    private String forwardUrl;

    public ForwardController(String forwardUrl) {
        this.forwardUrl = forwardUrl;
        if (forwardUrl == null) {
            throw new NullPointerException(EMPTY_URL);
        }
    }

    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return new ModelAndView(new JspView(forwardUrl));
    }
}
