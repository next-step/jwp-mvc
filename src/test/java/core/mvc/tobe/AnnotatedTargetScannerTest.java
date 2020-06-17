package core.mvc.tobe;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("특정 annotation 이 붙은 클래스, 메서드 로드 클래스")
class AnnotatedTargetScannerTest {

    public static final String BASE_PACKAGE = "core.mvc.tobe";

    @Test
    @DisplayName("패키지 내에서 특정 어노테이션이 붙은 클래스 모두 불러오기")
    void loadAnnotatedClasses() {
        Set<Class<?>> classes = AnnotatedTargetScanner.loadClasses(Controller.class, BASE_PACKAGE);

        assertThat(classes).isNotNull();
        assertThat(classes).hasSize(2);
    }

    @Test
    @DisplayName("클래스 내에서 특정 어노테이션이 붙은 메소드 모두 불러오기")
    void loadAnnotatedMethods() {
        List<Method> methods = AnnotatedTargetScanner.loadClasses(Controller.class, BASE_PACKAGE)
                .stream()
                .map(clazz -> AnnotatedTargetScanner.loadMethodsFromClass(clazz, RequestMapping.class))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        assertThat(methods).hasSize(5);
    }
}