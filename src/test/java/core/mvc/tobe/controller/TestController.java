package core.mvc.tobe.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;

@Controller
public class TestController {
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    void test(int i) {
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    void test(long i) {
    }
}
