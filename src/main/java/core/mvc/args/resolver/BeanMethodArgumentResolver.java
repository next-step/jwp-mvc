package core.mvc.args.resolver;

import core.annotation.web.PathVariable;
import core.mvc.args.MethodParameter;
import core.utils.typeresolver.PrimitiveTypeResolvers;
import java.lang.reflect.Field;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

/**
 * Created by iltaek on 2020/06/29 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class BeanMethodArgumentResolver implements MethodArgumentResolver {

    private static final Logger logger = LoggerFactory.getLogger(BeanMethodArgumentResolver.class);

    private static final String FAIL_NEW_INSTANCE = "다음의 클래스의 객체 생성에 실패하였습니다. : ";
    private static final String FAIL_FILL_INSTANCE = "%s 인스턴스의 %s 필드 세팅에 실패하였습니다.";

    @Override
    public boolean isSupport(MethodParameter parameter) {
        return !isPrimitive(parameter.getType()) && !isPathVariable(parameter) && !isRequestVariable(parameter.getType());
    }

    private boolean isRequestVariable(Class<?> parameterType) {
        return parameterType == HttpServletRequest.class;
    }

    private boolean isPathVariable(MethodParameter parameter) {
        return parameter.isParameterAnnotationPresent(PathVariable.class);
    }

    private boolean isPrimitive(Class<?> parameterType) {
        return ClassUtils.isPrimitiveOrWrapper(parameterType) || parameterType == String.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) {
        Object bean = getClazzInstance(parameter.getType());
        fillBean(bean, parameter, request);
        return bean;
    }

    private Object getClazzInstance(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error(e.getMessage());
            throw new IllegalArgumentException(FAIL_NEW_INSTANCE + clazz.getName());
        }
    }

    private void fillBean(Object bean, MethodParameter parameter, HttpServletRequest request) {
        Field[] fields = parameter.getType().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                field.set(bean, PrimitiveTypeResolvers.resolve(request.getParameter(field.getName()), field.getType()));
            } catch (IllegalAccessException e) {
                logger.error(e.getMessage());
                throw new IllegalArgumentException(String.format(FAIL_FILL_INSTANCE, bean.getClass().getName(), field.getName()));
            }
        }
    }
}
