package core.di.factory.example;

import java.util.Map;
import core.mvc.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.View;
import java.util.Map;

public class MockView implements View {

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

    }

}

