package core.mvc.tobe;

import core.mvc.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class DefaultView implements View {
    private static final Logger logger = LoggerFactory.getLogger(DefaultView.class);

    private final String name;

    public DefaultView(final String name) {
        this.name = name;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        logger.debug("default view render");
    }

    public String getName() {
        return name;
    }
}
