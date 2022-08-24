package core.mvc.view;

import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class RedirectView implements View {

    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private final String redirectPath;

    public RedirectView(String redirectPath) {
        Assert.hasText(redirectPath, "redirectPath가 null이어선 안됩니다.");
        this.redirectPath = redirectPath;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(redirectPath.substring(DEFAULT_REDIRECT_PREFIX.length()));
    }
}
