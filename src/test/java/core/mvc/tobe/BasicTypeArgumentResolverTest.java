package core.mvc.tobe;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class BasicTypeArgumentResolverTest {

    @Test
    void string() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String userId = "javajigi";
        String password = "password";
        request.addParameter("userId", userId);
        request.addParameter("password", password);

        BasicTypeArgumentResolver basicTypeArgumentResolver = new BasicTypeArgumentResolver();
        boolean sameType = basicTypeArgumentResolver.equalsTo(String.class);
        Object userIdActual = basicTypeArgumentResolver.getParameterValue(request, String.class, "userId");
        assertThat(sameType).isTrue();
        assertThat(userIdActual).isEqualTo(userId);
    }

    @Test
    void intAndLong() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();

        long id = 1L;
        int age = 29;
        request.setParameter("id", String.valueOf(id));
        request.setParameter("age", String.valueOf(age));

        BasicTypeArgumentResolver basicTypeArgumentResolver = new BasicTypeArgumentResolver();
        boolean sameType = basicTypeArgumentResolver.equalsTo(long.class);
        Object idActual = basicTypeArgumentResolver.getParameterValue(request, long.class, "id");

        assertThat(sameType).isTrue();
        assertThat(idActual).isEqualTo(id);

    }

}
