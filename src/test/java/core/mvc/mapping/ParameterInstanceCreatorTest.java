package core.mvc.mapping;

import core.mvc.resolver.ParameterInstanceCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ParameterInstanceCreatorTest {

    private ParameterInstanceCreator creator;

    @BeforeEach
    void setup() {
        this.creator = new ParameterInstanceCreator();
    }

    @Test
    void create() throws Exception {
        long id = 111;
        String name = "name!!!";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("id", String.valueOf(id));
        request.addParameter("name", name);

        TestInstance result = creator.create(TestInstance.class, request);

        assertThat(result).isNotNull();
        assertThat(result.id).isEqualTo(id);
        assertThat(result.name).isEqualTo(name);
        assertThat(result.password).isNull();
    }

    @DisplayName("기본생성자가 없으면 에러")
    @Test
    void create1() {
        assertThatThrownBy(() -> creator.create(NotDefaultConstructorClass.class, new MockHttpServletRequest()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("기본 생성자가 없습니다. type : [core.mvc.tobe.ParameterInstanceCreatorTest$NotDefaultConstructorClass]");
    }

    static class TestInstance {
        private long id;
        private String name;
        private String password;

        private TestInstance() {}

        public TestInstance(long id, String name, String password) {
            this.id = id;
            this.name = name;
            this.password = password;
        }
    }

    static class NotDefaultConstructorClass {
        private long id;
        private String name;

        public NotDefaultConstructorClass(long id, String name, String password) {
            this.id = id;
            this.name = name;
            this.password = password;
        }

        private String password;
    }
}