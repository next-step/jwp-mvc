package core.mvc.resolver;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;

public abstract class TestCase {
    protected static class TargetClass {

        @RequestMapping(value = "/test/{id}", method = RequestMethod.GET)
        private void pathVariableMethod(@PathVariable String id) {

        }

        @RequestMapping(value = "/test", method = RequestMethod.GET)
        private void requestParamMethod(@RequestParam String id) {

        }
    }

}
