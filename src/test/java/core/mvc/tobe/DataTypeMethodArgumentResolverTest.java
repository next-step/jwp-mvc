package core.mvc.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;


import static org.assertj.core.api.Assertions.assertThat;

public class DataTypeMethodArgumentResolverTest {

    @DisplayName("메소드 파라미터 내 int형 데이터에 적합 리졸버 찾기")
    @Test
    void test_staticFactoryMethod() {
        // given
        MethodParameter methodParameter = new MethodParameter("age", int.class);
        // when
        DataTypeMethodArgumentResolver resolver = DataTypeMethodArgumentResolver.from(methodParameter);
        // then
        assertThat(resolver == DataTypeMethodArgumentResolver.INTEGER_TYPE).isTrue();
    }

    @DisplayName("int형 데이터에 대한 리졸버 처리")
    @Test
    void test_resolve() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("age", "26");

        MethodParameter methodParameter = new MethodParameter("age", int.class);
        // when
        DataTypeMethodArgumentResolver resolver = DataTypeMethodArgumentResolver.from(methodParameter);
        try {
            Object arg = resolver.resolveArgument(methodParameter, request);
        // then
            assertThat((int) arg).isEqualTo(26);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
