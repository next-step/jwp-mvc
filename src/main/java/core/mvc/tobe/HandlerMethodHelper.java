package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created By kjs4395 on 2020-06-24
 *
 * 메소드 invoke 를 하기 위해 request에서 적절한 값을 찾아내는 과정
 */
public interface HandlerMethodHelper {

    public Object bindingProcess(Class<?> type,String name, HttpServletRequest request) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException;
}
