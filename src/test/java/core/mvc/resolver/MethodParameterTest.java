package core.mvc.resolver;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MethodParameterTest {

    private static final int ID = 3245;
    private static final String NAME = "name!!!";
    private static final long COUNT = 796;

    private Method testMethod;
    private MockHttpServletRequest request = new MockHttpServletRequest();
    private MethodParameter methodParameter;

    @BeforeEach
    void setup() throws Exception {
        testMethod = this.getClass().getMethod("testMethod", int.class, String.class, long.class, TestClass.class);

        request.addParameter("name", NAME);
        request.addParameter("count", String.valueOf(COUNT));

        methodParameter = MethodParameter.from(testMethod, "/root/" + ID + "/");
    }

    @Test
    void getParameterValue() throws Exception {
        assertThat(methodParameter.getParameterValue(request, "id")).as("id").isEqualTo(String.valueOf(ID));
        assertThat(methodParameter.getParameterValue(request, "name")).as("name").isEqualTo(NAME);
        assertThat(methodParameter.getParameterValue(request, "count")).as("count").isEqualTo(String.valueOf(COUNT));
        assertThat(methodParameter.getParameterValue(request, "instance")).isNull();
    }

    @Test
    void getType() throws Exception {
        assertThat(methodParameter.getType("id")).isEqualTo(int.class);
        assertThat(methodParameter.getType("name")).isEqualTo(String.class);
        assertThat(methodParameter.getType("count")).isEqualTo(long.class);
        assertThat(methodParameter.getType("instance")).isEqualTo(TestClass.class);
    }

    @DisplayName("메소드의 파라미터 순서와 동일한 리스트가 반환된다.")
    @Test
    void getParameterNames() {
        String[] originNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(testMethod);

        List<String> parameterNames = methodParameter.getParameterNames();

        assertThat(parameterNames.size()).isEqualTo(originNames.length);

        for (int i = 0; i < originNames.length; i++) {
            assertThat(parameterNames.get(i)).isEqualTo(originNames[i]);
        }
    }

    @RequestMapping("/root/{id}/")
    public void testMethod(@PathVariable int id, String name, long count, TestClass instance) {

    }

    static class TestClass {

    }
}