package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ErrorView implements View {

    private int statusCode;
    private String message;

    public ErrorView(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;

    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendError(statusCode, message);
    }
}
