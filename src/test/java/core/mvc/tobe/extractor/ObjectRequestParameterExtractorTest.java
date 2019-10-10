package core.mvc.tobe.extractor;

import core.mvc.tobe.ParameterInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class ObjectRequestParameterExtractorTest extends ExtractorTestSupport {

    private static final long ID = 100L;
    private static final int AGE = 27;
    private static final String NAME = "JAEYEON";

    @Override
    RequestParameterExtractor getExtractor() {
        return new ObjectRequestParameterExtractor();
    }

    @Override
    HttpServletRequest getRequest() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("id", String.valueOf(ID));
        request.setParameter("age", String.valueOf(AGE));
        request.setParameter("name", NAME);

        return request;
    }

    @DisplayName("User 매칭 시 true를 반환한다.")
    @Test
    void isSupportTrue() {
        final ParameterInfo parameterInfo = getParameterInfo(Mock.class, "targetMethod", User.class);
        assertThat(isSupport(parameterInfo)).isTrue();
    }

    @DisplayName("Object가 아닌 값 매칭 시 false를 반환한다.")
    @Test
    void isSupportNonTarget() {
        final ParameterInfo parameterInfo = getParameterInfo(Mock.class, "nonTargetMethod", int.class);
        assertThat(isSupport(parameterInfo)).isFalse();
    }

    @DisplayName("User 추출 시 값을 반환한다.")
    @Test
    void extract() {
        final ParameterInfo parameterInfo = getParameterInfo(Mock.class, "targetMethod", User.class);

        final User extracted = (User) extract(parameterInfo);

        assertThat(extracted.getId()).isEqualTo(ID);
        assertThat(extracted.getAge()).isEqualTo(AGE);
        assertThat(extracted.getName()).isEqualTo(NAME);
    }

    @DisplayName("User 가 아닌 값 추출 시 null을 반환한다.")
    @Test
    void extractNotFound() {
        final ParameterInfo parameterInfo = getParameterInfo(Mock.class, "nonTargetMethod", int.class);

        final Object extracted = extract(parameterInfo);

        assertThat(extracted).isNull();
    }

    static class Mock {

        public void targetMethod(User value) {
        }
        public void nonTargetMethod(int value) {
        }
    }

    public static class User {

        private long id;
        private int age;
        private String name;

        public User() { }

        public long getId() {
            return id;
        }

        public int getAge() {
            return age;
        }

        public String getName() {
            return name;
        }
    }
}