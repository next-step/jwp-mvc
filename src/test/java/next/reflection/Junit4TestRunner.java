package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Junit4TestRunner {


    @DisplayName("@myTest을 가진 메소드만 실행에 성공한다")
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        Junit4Test instance = clazz.newInstance();

        int count = 0;
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if(invokeOf(instance, method)){
                count++;
            }
        }

        assertThat(count).isEqualTo(2);
    }

    private boolean invokeOf(Junit4Test instance, Method method) throws Exception {
        if (method.isAnnotationPresent(MyTest.class)) {
            method.invoke(instance);
            return true;
        }
        return false;
    }
}
