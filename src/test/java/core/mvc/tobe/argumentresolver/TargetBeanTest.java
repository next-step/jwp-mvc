package core.mvc.tobe.argumentresolver;

import core.mvc.tobe.TestUser;
import core.mvc.tobe.TestUserController;
import core.mvc.tobe.argumentresolver.util.TargetBean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class TargetBeanTest {
    @DisplayName("생성된 TargetBean의 필드는 초기값(null 또는  0)으로 할당되어 있다.")
    @Test
    void createTargetBean() {
        //given
        Class clazz = TestUserController.class;
        Method method = getMethod("create_javabean", clazz.getDeclaredMethods());
        MockHttpServletRequest request = createRequest();

        //when(create)
        MethodParameter methodParameter = new MethodParameter(method, 0);
        TestUser targetBean = (TestUser) TargetBean.createWithNoArgs(methodParameter);

        //then(create)
        assertThat(targetBean.getAge()).isEqualTo(0);
        assertThat(targetBean.getUserId()).isNull();
        assertThat(targetBean.getPassword()).isNull();
    }

    @DisplayName("targetBean의 필드를 할당한다.")
    @Test
    void setFields() {
        //given
        Class clazz = TestUserController.class;
        Method method = getMethod("create_javabean", clazz.getDeclaredMethods());
        MethodParameter methodParameter = new MethodParameter(method, 0);
        TestUser targetBean = (TestUser) TargetBean.createWithNoArgs(methodParameter);
        MockHttpServletRequest request = createRequest();

        //when
        TargetBean.setFields(targetBean, request, methodParameter);

        //then
        assertThat(targetBean.getUserId()).isEqualTo("javajigi");
        assertThat(targetBean.getPassword()).isEqualTo("password");
        assertThat(targetBean.getAge()).isEqualTo(20);
    }

    private MockHttpServletRequest createRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "javajigi");
        request.addParameter("password", "password");
        request.addParameter("age", "20");
        return request;
    }

    private Method getMethod(String name, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(name))
                .findFirst()
                .get();
    }
}
