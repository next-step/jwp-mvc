package core.mvc.tobe;

import core.mvc.asis.Controller;
import core.mvc.asis.LegacyHandlerMapping;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class HandlerMappingsTest {

    @DisplayName("HandlerMapping 일급 컬렉션 생성")
    @Test
    void create() {
        assertThatCode(() -> new HandlerMappings(List.of(new LegacyHandlerMapping())));
    }

    @DisplayName("HandlerMapping 이 존재하지 않으면 Exception")
    @Test
    void createFail() {
        assertThatIllegalArgumentException().isThrownBy(() -> new HandlerMappings(List.of(new LegacyHandlerMapping())));
    }

    @DisplayName("Request 에 맞는 Handler 를 전달할수 있다.")
    @Test
    void getHandler() {
        HandlerMappings handlerMappings = new HandlerMappings(List.of(new LegacyHandlerMapping()));
        handlerMappings.initialize();

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setRequestURI("/users/profile");

        assertThat(handlerMappings.getHandler(req)).isInstanceOf(Controller.class);
    }
}
