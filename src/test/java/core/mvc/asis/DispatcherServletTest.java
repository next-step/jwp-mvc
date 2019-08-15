package core.mvc.asis;

import core.db.DataBase;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class DispatcherServletTest {
    private DispatcherServlet dispatcher;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() throws Exception {
        dispatcher = new DispatcherServlet();
        dispatcher.init();
        response = new MockHttpServletResponse();
    }

    @DisplayName("Legacy MVC 테스트")
    @ParameterizedTest
    @CsvSource({"GET,/"
            , "GET,/users/form"
            , "GET,/users/loginForm"})
    void legacyMvcHandlerMappingTest(String method, String requestURI) throws Exception {
        request = new MockHttpServletRequest(method, requestURI);
        dispatcher.service(request, response);
    }

    @DisplayName("@MVC 테스트")
    @ParameterizedTest
    @CsvSource({"GET,/users"
            , "POST,/users/create"
            , "POST,/users/login"
            , "GET,/users/logout"
            , "GET,/users/profile"})
    void annotationHandlerMappingTest(String method, String requestURI) throws Exception {
        request = new MockHttpServletRequest(method, requestURI);
        dispatcher.service(request, response);
    }

    @DisplayName("@MVC 테스트 - 회원가입")
    @ParameterizedTest
    @CsvSource({"POST,/users/create,123,123,name,whowho@who"})
    void createUser(ArgumentsAccessor argumentsAccessor) throws Exception {
        //given
        request = new MockHttpServletRequest(argumentsAccessor.getString(0), argumentsAccessor.getString(1));
        request.addParameter("userId", argumentsAccessor.getString(2));
        request.addParameter("password", argumentsAccessor.getString(3));
        request.addParameter("name", argumentsAccessor.getString(4));
        request.addParameter("email", argumentsAccessor.getString(5));

        //when
        dispatcher.service(request, response);
        User user = DataBase.findUserById(argumentsAccessor.getString(2));

        //then
        assertThat(user.getUserId()).isEqualTo(argumentsAccessor.getString(2));
        assertThat(user.getPassword()).isEqualTo(argumentsAccessor.getString(3));
        assertThat(user.getName()).isEqualTo(argumentsAccessor.getString(4));
        assertThat(user.getEmail()).isEqualTo(argumentsAccessor.getString(5));
    }

    @DisplayName("@MVC 테스트 - 로그인")
    @ParameterizedTest
    @CsvSource({"POST,/users/login"})
    void login(ArgumentsAccessor argumentsAccessor) throws Exception {
        //given
        request = new MockHttpServletRequest(argumentsAccessor.getString(0), argumentsAccessor.getString(1));
        User befUser = new User("id", "pwd", "name", "mail");
        DataBase.addUser(befUser);

        //when
        dispatcher.service(request, response);
        User user = DataBase.findUserById(befUser.getUserId());

        //then
        assertThat(user.getUserId()).isEqualTo(befUser.getUserId());
        assertThat(user.getPassword()).isEqualTo(befUser.getPassword());
        assertThat(user.getName()).isEqualTo(befUser.getName());
        assertThat(user.getEmail()).isEqualTo(befUser.getEmail());
    }
}