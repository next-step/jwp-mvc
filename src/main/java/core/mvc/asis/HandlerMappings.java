package core.mvc.asis;

import core.mvc.HandlerMapping;
import core.mvc.tobe.AnnotationHandlerMapping;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javassist.NotFoundException;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by iltaek on 2020/06/22 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class HandlerMappings {

    private static final String ILLEGAL_HANDLER = "해당 요청을 처리할 handler가 없습니다: ";
    private static final String ANNOTATION_HANDLER_BASE_PACKAGE = "next.controller";

    private List<HandlerMapping> handlerMappings;

    public HandlerMappings() {
        initialize();
    }

    private void initialize() {
        LegacyRequestMapping lrm = new LegacyRequestMapping();
        lrm.initMapping();

        AnnotationHandlerMapping ahm = new AnnotationHandlerMapping(ANNOTATION_HANDLER_BASE_PACKAGE);
        ahm.initialize();
        handlerMappings = Arrays.asList(lrm, ahm);
    }

    public Object getHandler(HttpServletRequest req) throws NotFoundException {
        return handlerMappings.stream()
            .map(handlerMapping -> handlerMapping.getHandler(req))
            .filter(Objects::nonNull)
            .findFirst()
            .orElseThrow(() -> new NotFoundException(ILLEGAL_HANDLER + req.getRequestURI()));
    }
}
