package core.mvc.resolver;

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
}
