package core.mvc.tobe;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestParam;
import core.mvc.ModelAndView;
import core.mvc.tobe.support.ArgumentResolver;
import core.mvc.tobe.support.HttpRequestArgumentResolver;
import core.mvc.tobe.support.HttpResponseArgumentResolver;
import core.mvc.tobe.support.RequestParamArgumentResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HandlerExecutionTest {

    private static final Logger logger = LoggerFactory.getLogger(HandlerExecutionTest.class);

    private List<ArgumentResolver> argumentResolvers;
    private ParameterNameDiscoverer parameterNameDiscoverer;

    @BeforeEach
    void setUp() {
        this.argumentResolvers = asList(
                new HttpRequestArgumentResolver(),
                new HttpResponseArgumentResolver(),
                new RequestParamArgumentResolver()
        );
        this.parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    }

    @Test
    void requestArgumentResolver() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("id", "jun");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockController target = new MockController();
        Method[] methods = MockController.class.getMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                logger.info("test {} method", method.getName());
                HandlerExecution handlerExecution = new HandlerExecution(parameterNameDiscoverer, argumentResolvers, target, method);
                handlerExecution.handle(request, response);
            }
        }
    }

    public static class MockController {

        @RequestMapping
        public ModelAndView request(HttpServletRequest request) {
            assertNotNull(request);
            return new ModelAndView();
        }

        @RequestMapping
        public ModelAndView response(HttpServletResponse response) {
            assertNotNull(response);
            return new ModelAndView();
        }

        @RequestMapping
        public String responseReturnString(HttpServletResponse response) {
            assertNotNull(response);
            return "index.html";
        }

        @RequestMapping
        public ModelAndView requestParam(@RequestParam(value = "id") String userId) {
            assertThat(userId).isEqualTo("jun");
            return new ModelAndView();
        }


    }




}
