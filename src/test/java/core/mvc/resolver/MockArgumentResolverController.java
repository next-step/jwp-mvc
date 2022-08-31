package core.mvc.resolver;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestParam;

public class MockArgumentResolverController {

    @RequestMapping("/requestParam")
    String requestParamStringMethod(@RequestParam(value = "user") String str) {
        return str;
    }

    @RequestMapping("/requestParam")
    String requestParamIntegerMethod(@RequestParam int id) {
        return String.valueOf(id);
    }

    @RequestMapping("/pathVariable/{name}")
    String pathVariableStringMethod(@PathVariable(value = "name") String str) {
        return str;
    }

    @RequestMapping("/pathVariable/user/{id}")
    String pathVariableIntegerMethod(@PathVariable int id) {
        return String.valueOf(id);
    }

}
