package core.mvc.tobe;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ArgumentResolver {

    private static ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    public static Object[] resolve(HttpServletRequest request, Method method) {
        Object[] args = getArguments(request, method);
        registerSession(args, request, method);
        return args;
    }

    private static Object[] getArguments(HttpServletRequest request, Method method) {
        return Arrays.stream(nameDiscoverer.getParameterNames(method))
                .map(request::getParameter)
                .toArray();
    }

    private static void registerSession(Object[] args, HttpServletRequest request, Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < method.getParameterCount(); i++) {
            if (HttpSession.class.equals(parameterTypes[i])) {
                args[i] = request.getSession();
                return;
            }
        }
    }
}
