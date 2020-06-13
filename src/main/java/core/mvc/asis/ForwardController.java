package core.mvc.asis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForwardController implements Controller {
    private final String forwardUrl;

    public ForwardController(final String forwardUrl) {
        if (forwardUrl == null) {
            throw new NullPointerException("forwardUrl is null. 이동할 URL을 입력하세요.");
        }

        this.forwardUrl = forwardUrl;
    }

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse resp) throws Exception {
        return forwardUrl;
    }
}
