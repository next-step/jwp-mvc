package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.view.ForwardView;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForwardController implements Controller {
    private final String forwardUrl;

    public ForwardController(String forwardUrl) {
        Assert.notNull(forwardUrl, "forwardUrl is null. 이동할 URL을 입력하세요.");
        this.forwardUrl = forwardUrl;
    }

    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return new ModelAndView(new ForwardView(forwardUrl));
    }
}
