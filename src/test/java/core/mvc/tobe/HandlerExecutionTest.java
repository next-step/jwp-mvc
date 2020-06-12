package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

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

        if (RequestMethod.GET == declaredAnnotation.method()) {
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
}