package core;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestParam;

import java.lang.annotation.Annotation;

public class AnnotationInstance {
    public static RequestParam newRequestParam(final String value, final String name, final boolean required) {
        RequestParam requestParam = new RequestParam() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return RequestParam.class;
            }

            @Override
            public String value() {
                return value;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public boolean required() {
                return required;
            }
        };

        return requestParam;
    }

    public static PathVariable newPathVariable(final String value, final String name, final boolean required) {
        PathVariable pathVariable = new PathVariable() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return PathVariable.class;
            }

            @Override
            public String value() {
                return value;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public boolean required() {
                return required;
            }
        };

        return pathVariable;
    }
}
