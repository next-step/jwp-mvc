package core.mvc.tobe.handler.mapping;

import core.db.DataBase;
import core.mvc.tobe.ControllerScanner;
import core.mvc.tobe.MyController;
import core.mvc.tobe.handler.resolver.BeanTypeRequestParameterArgumentResolver;
import core.mvc.tobe.handler.resolver.HandlerMethodArgumentResolvers;
import core.mvc.tobe.handler.resolver.HttpServletArgumentResolver;
import core.mvc.tobe.handler.resolver.PathVariableArgumentResolver;
import core.mvc.tobe.handler.resolver.SimpleTypeRequestParameterArgumentResolver;
import core.mvc.tobe.handler.resolver.utils.PatternMatcher;
import core.mvc.tobe.handler.resolver.utils.SimpleTypeConverter;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AnnotationHandlerMappingTest {
    private AnnotationHandlerMapping handlerMapping;
    private HandlerMethodArgumentResolvers handlerMethodArgumentResolvers;

    @BeforeEach
    public void setup() {
        handlerMapping = new AnnotationHandlerMapping(
                new ControllerScanner("core.mvc.tobe"),
                new PatternMatcher());
        handlerMapping.initialize();

        handlerMethodArgumentResolvers = initArgumentResolvers();
    }

    private HandlerMethodArgumentResolvers initArgumentResolvers() {
        LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        SimpleTypeConverter simpleTypeConverter = new SimpleTypeConverter();
        PatternMatcher patternMatcher = new PatternMatcher();

        SimpleTypeRequestParameterArgumentResolver simpleTypeRequestParameterArgumentResolver = new SimpleTypeRequestParameterArgumentResolver(simpleTypeConverter);
        HttpServletArgumentResolver httpServletArgumentResolver = new HttpServletArgumentResolver();
        PathVariableArgumentResolver pathVariableArgumentResolver = new PathVariableArgumentResolver(
                simpleTypeConverter,
                patternMatcher
        );
        BeanTypeRequestParameterArgumentResolver beanTypeRequestParameterArgumentResolver = new BeanTypeRequestParameterArgumentResolver(
                parameterNameDiscoverer,
                simpleTypeRequestParameterArgumentResolver
        );

        return new HandlerMethodArgumentResolvers(
                parameterNameDiscoverer,
                List.of(
                        httpServletArgumentResolver,
                        pathVariableArgumentResolver,
                        simpleTypeRequestParameterArgumentResolver,
                        beanTypeRequestParameterArgumentResolver
                )
        );
    }

    @Test
    public void create_find() throws Exception {
        User user = new User("pobi", "password", "포비", "pobi@nextstep.camp");
        createUser(user);
        assertThat(DataBase.findUserById(user.getUserId())).isEqualTo(user);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users");
        request.setParameter("userId", user.getUserId());
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.setHandlerMethodArgumentResolvers(handlerMethodArgumentResolvers);

        execution.handle(request, response);

        assertThat(request.getAttribute("user")).isEqualTo(user);
    }

    private void createUser(User user) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        request.setParameter("userId", user.getUserId());
        request.setParameter("password", user.getPassword());
        request.setParameter("name", user.getName());
        request.setParameter("email", user.getEmail());
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecution execution = handlerMapping.getHandler(request);
        execution.setHandlerMethodArgumentResolvers(handlerMethodArgumentResolvers);

        execution.handle(request, response);
    }

    @DisplayName("RequestMapping에 지정된 url이 패턴형식인 경우, 패턴이 일치하고, method가 동일한 핸들러를 반환한다.")
    @Test
    void mapping_pattern_url() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/2");
        HandlerExecution execution = handlerMapping.getHandler(request);


        MyController myController = new MyController();
        Method realMethod = Arrays.stream(MyController.class.getDeclaredMethods())
                .filter(method -> "pathPatternMethod".equals(method.getName()))
                .findFirst()
                .get();

        assertThat(execution).usingRecursiveComparison()
                .isEqualTo(new HandlerExecution(myController, realMethod));

    }
}
