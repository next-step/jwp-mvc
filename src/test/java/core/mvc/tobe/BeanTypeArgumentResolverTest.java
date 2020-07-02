package core.mvc.tobe;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class BeanTypeArgumentResolverTest {

    @Test
    void beanType() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setParameter("userId", "ju");
        request.setParameter("password", "123");
        request.setParameter("age", "29");

        BeanTypeArgumentResolver beanTypeArgumentResolver = new BeanTypeArgumentResolver();
        boolean sameType = beanTypeArgumentResolver.equalsTo(String.class);
        boolean same = beanTypeArgumentResolver.equalsTo(TestUser.class);
        final Object value = beanTypeArgumentResolver.getParameterValue(request, TestUser.class, "");

        assertThat(sameType).isFalse();
        assertThat(same).isTrue();
        assertThat(value).isInstanceOf(TestUser.class);

    }
}
