package core.mvc.tobe;

import core.mvc.HandlerMapper;
import core.mvc.ModelAndView;
import core.mvc.tobe.extractor.HttpServletRequestExtractor;
import core.mvc.tobe.extractor.HttpServletResponseExtractor;
import core.mvc.tobe.extractor.IntegerRequestParameterExtractor;
import core.mvc.tobe.extractor.LongRequestParameterExtractor;
import core.mvc.tobe.extractor.ObjectRequestParameterExtractor;
import core.mvc.tobe.extractor.PathVariableExtractor;
import core.mvc.tobe.extractor.RequestParameterExtractor;
import core.mvc.tobe.extractor.StringRequestParameterExtractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TestUserControllerTest {

    private HandlerMapper handlerMapper;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        final List<RequestParameterExtractor> requestParameterExtractors = List.of(new HttpServletRequestExtractor(),
                new HttpServletResponseExtractor(),
                new PathVariableExtractor(),
                new IntegerRequestParameterExtractor(),
                new LongRequestParameterExtractor(),
                new StringRequestParameterExtractor(),
                new ObjectRequestParameterExtractor());

        final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        final RequestParameterResolver requestParameterResolver = new RequestParameterResolver(parameterNameDiscoverer,
                requestParameterExtractors);

        handlerMapper = new NextGenerationHandlerMapper(requestParameterResolver, "core.mvc.tobe");
        response = new MockHttpServletResponse();
    }

    @Test
    void create_string() {
        // given
        final String USER_ID_KEY = "userId";
        final String USER_ID_VALUE = "jaeyeonling";
        final String PASSWORD_KEY = "password";
        final String PASSWORD_VALUE = "P@ssw01d";

        final MockHttpServletRequest request = new MockHttpServletRequest("POST", "/test/users/string");

        request.addParameter(USER_ID_KEY, USER_ID_VALUE);
        request.addParameter(PASSWORD_KEY, PASSWORD_VALUE);

        // when
        final ModelAndView mav = handlerMapper.mapping(request, response);

        // then
        assertThat(USER_ID_VALUE).isEqualTo(mav.getObject(USER_ID_KEY));
        assertThat(PASSWORD_VALUE).isEqualTo(mav.getObject(PASSWORD_KEY));
    }

    @Test
    void create_int_long() throws NumberFormatException {
        // given
        final String ID_KEY = "id";
        final long ID_VALUE = 1;
        final String AGE_KEY = "age";
        final int AGE_VALUE = 27;

        final MockHttpServletRequest request = new MockHttpServletRequest("POST", "/test/users/int-long");

        request.addParameter(ID_KEY, String.valueOf(ID_VALUE));
        request.addParameter(AGE_KEY, String.valueOf(AGE_VALUE));

        // when
        final ModelAndView mav = handlerMapper.mapping(request, response);

        // then
        assertThat(ID_VALUE).isEqualTo(mav.getObject(ID_KEY));
        assertThat(AGE_VALUE).isEqualTo(mav.getObject(AGE_KEY));
    }

    @Test
    void create_javabean() {
        // given
        final String USER_ID_VALUE = "jaeyeonling";
        final String PASSWORD_VALUE = "P@ssw01d";
        final int AGE_VALUE = 27;

        final MockHttpServletRequest request = new MockHttpServletRequest("POST", "/test/users/javabean");

        request.addParameter("userId", USER_ID_VALUE);
        request.addParameter("password", PASSWORD_VALUE);
        request.addParameter("age", String.valueOf(AGE_VALUE));

        // when
        final ModelAndView mav = handlerMapper.mapping(request, response);
        final TestUser testUser = (TestUser) mav.getObject("testUser");

        // then
        assertThat(USER_ID_VALUE).isEqualTo(testUser.getUserId());
        assertThat(PASSWORD_VALUE).isEqualTo(testUser.getPassword());
        assertThat(AGE_VALUE).isEqualTo(testUser.getAge());
    }

    @Test
    void show_pathVariable() {
        // given
        final long id = 1L;

        final MockHttpServletRequest request = new MockHttpServletRequest("GET", String.format("/test/users/%d", 1));

        // when
        final ModelAndView mav = handlerMapper.mapping(request, response);

        // then
        assertThat(id).isEqualTo(mav.getObject("id"));
    }
}
