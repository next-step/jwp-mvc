package core.mvc.tobe;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

class ControllerAnnotationHandlerTest {

    @Test
    void name() {
        Reflections reflections = new Reflections(
                new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage("core.mvc.tobe")));
        ControllerAnnotationHandler controllerAnnotationHandler = new ControllerAnnotationHandler(reflections);
        controllerAnnotationHandler.init();
    }
}
