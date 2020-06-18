package core.mvc.param.extractor.complex;

import core.mvc.param.Parameter;
import core.mvc.tobe.TestUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("사용자 생성 클래스를 동적으로 생성해서 리턴하는 클래스")
class ComplexValueExtractorTest {
    private static final ComplexValueExtractor COMPLEX_VALUE_EXTRACTOR = new ComplexValueExtractor();

    @Test
    @DisplayName("추출 태스트")
    void extract() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Parameter parameter = new Parameter("user", TestUser.class, null);
        request.setParameter("userId", "nokchax");
        request.setParameter("password", "1234");
        request.setParameter("age", "30");

        Object extract = COMPLEX_VALUE_EXTRACTOR.extract(parameter, request);

        assertThat(extract).isNotNull();
        assertThat(extract).isInstanceOf(TestUser.class);
        assertThat(((TestUser) extract).getUserId()).isEqualTo("nokchax");
        assertThat(((TestUser) extract).getPassword()).isEqualTo("1234");
        assertThat(((TestUser) extract).getAge()).isEqualTo(30);
    }



}