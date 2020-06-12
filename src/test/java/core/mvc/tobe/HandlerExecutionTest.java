package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("@Controller 내부에 존재하는 @RequestMapping 과 해당 함수를 읽어서 하나의 HandlerExecution 으로 매칭한다.")
class HandlerExecutionTest {
    private static final Logger logger = LoggerFactory.getLogger(HandlerExecutionTest.class);

    @Test
    @DisplayName("리플렉션을 이용해 함수를 실행하기 위해선 메소드와 인스턴스를 필드로 가지고 있어야한다")
    void field() throws IllegalAccessException, InstantiationException {
        Class<MyController> myController = MyController.class;
        MyController instance = myController.newInstance();
        Method[] methods = myController.getMethods();

        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> execute(new HandlerExecution(method, instance), method));
    }

    private void execute(final HandlerExecution handler, final Method method) {
        RequestMapping declaredAnnotation = method.getDeclaredAnnotation(RequestMapping.class);

        if (RequestMethod.GET == declaredAnnotation.method()[0]) {
            findUserTest(handler);
            return;
        }

        createUserTest(handler);
    }

    private void findUserTest(final HandlerExecution handler) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "admin");

        try {
            handler.handle(request, new MockHttpServletResponse());

            assertThat(request.getAttribute("user")).isNotNull();
        } catch (Exception e) {
            logger.error("Fail to execute method in my controller : {}", e.getMessage());
        }
    }

    private void createUserTest(final HandlerExecution handler) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "nokchax");
        request.addParameter("password", "pw");
        request.addParameter("name", "kh");
        request.addParameter("email", "kh@nokchax.net");

        int prevUserCount = DataBase.findAll().size();

        try {
            handler.handle(request, new MockHttpServletResponse());

            assertThat(DataBase.findAll()).hasSize(prevUserCount + 1);
        } catch (Exception e) {
            logger.error("Fail to execute method in my controller : {}", e.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("method 나 instance 가 null 인 경우 예외 발생")
    void constructFail(final Method method, final Object instance) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new HandlerExecution(method, instance));
    }

    private static Stream<Arguments> constructFail()
            throws IllegalAccessException, InstantiationException, NoSuchMethodException {
        Class<MyController> myControllerClass = MyController.class;
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(null, myControllerClass.newInstance()),
                Arguments.of(myControllerClass.getMethod("save", HttpServletRequest.class, HttpServletResponse.class), null)
        );
    }

}