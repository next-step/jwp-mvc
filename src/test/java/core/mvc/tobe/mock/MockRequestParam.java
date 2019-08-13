package core.mvc.tobe.mock;

import core.annotation.web.RequestParam;

import java.lang.annotation.Annotation;

public class MockRequestParam implements RequestParam {
    @Override
    public String value() {
        return "name";
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public boolean required() {
        return false;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return RequestParam.class;
    }
}
