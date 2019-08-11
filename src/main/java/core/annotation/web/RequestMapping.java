package core.annotation.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static core.annotation.web.RequestMethod.DELETE;
import static core.annotation.web.RequestMethod.GET;
import static core.annotation.web.RequestMethod.PATCH;
import static core.annotation.web.RequestMethod.POST;
import static core.annotation.web.RequestMethod.PUT;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    String value() default "";

    RequestMethod[] method() default {GET, POST, PUT, PATCH, DELETE};
}
