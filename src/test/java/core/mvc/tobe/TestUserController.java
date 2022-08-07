package core.mvc.tobe;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.di.factory.example.MockView;
import core.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class TestUserController {
    private static final Logger logger = LoggerFactory.getLogger(TestUserController.class);

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ModelAndView create_string(String userId, String password) {
        logger.debug("userId: {}, password: {}", userId, password);
        return ModelAndView.of(Map.of("userId", userId, "password", password), new MockView());
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ModelAndView create_int_long(long id, int age) {
        logger.debug("id: {}, age: {}", id, age);
        return ModelAndView.of(Map.of("id", id, "age", age), new MockView());
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ModelAndView create_javabean(TestUser testUser) {
        logger.debug("testUser: {}", testUser);
        return ModelAndView.of(Map.of("testUser", testUser), new MockView());
    }


    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public ModelAndView show_pathvariable(@PathVariable long id) {
        logger.debug("userId: {}", id);
        return ModelAndView.of(Map.of("id", id), new MockView());
    }
}
