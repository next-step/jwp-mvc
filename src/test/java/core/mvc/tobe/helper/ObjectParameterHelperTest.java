package core.mvc.tobe.helper;

import core.mvc.tobe.ParameterInfo;
import core.mvc.tobe.TestUser;
import core.mvc.tobe.TestUserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created By kjs4395 on 2020-07-01
 */
public class ObjectParameterHelperTest {

    ParameterInfo parameterInfo;
    MockHttpServletRequest request;

    @BeforeEach
    public void setup() {
        Method method = Arrays.stream(TestUserController.class.getMethods())
                .filter(method1 -> method1.getName().equals("create_javabean"))
                .findFirst()
                .get();
        this.parameterInfo = new ParameterInfo(method, 0);

        request = new MockHttpServletRequest("POST", "/users");
        request.addParameter("userId", "kjs4395");
        request.addParameter("password","1234");
        request.addParameter("age","29");
    }

    @Test
    public void bindProcessTest() {

        ObjectParameterHelper objectParameterHelper = new ObjectParameterHelper();
        TestUser testUser = (TestUser) objectParameterHelper.bindingProcess(parameterInfo, request);

        assertEquals(testUser.getUserId(), "kjs4395");
        assertEquals(testUser.getPassword(), "1234");
        assertEquals(testUser.getAge(), 29);
    }
}
