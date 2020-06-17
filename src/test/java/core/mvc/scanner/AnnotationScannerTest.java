package core.mvc.scanner;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Controller
class AnnotationScannerTest {

    private static final Logger log = LoggerFactory.getLogger(AnnotationScannerTest.class);

    @RequestMapping("/path")
    private void dummyMethod() {
        // no-op
    }

    @DisplayName("Controller 어노테이션이 붙은 클래스를 잘 스캔하는지 테스트")
    @Test
    void test_controller_scan_with_object() {
        final Set<Class<?>> classes = AnnotationScanner.loadClasses(Controller.class, this.getClass());
        assertNotNull(classes);
        assertThat(classes.size()).isEqualTo(1);
        log.debug("set: {}", classes);
    }

    @DisplayName("RequestMapping 어노테이션이 붙은 메소드를 잘 스캔하는지 테스트")
    @Test
    void test_controller_scan_with_base_package_name() {
        final Set<Method> methods = AnnotationScanner.loadMethods(this.getClass(), RequestMapping.class);
        assertNotNull(methods);
        assertThat(methods.size()).isEqualTo(1);
        log.debug("set: {}", methods);
    }
}