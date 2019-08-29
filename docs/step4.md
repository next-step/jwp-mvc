# @MVC 4단계 - Controller 메소드 인자 매핑

## @MVC 프레임워크 미션에 이어서 진행한다.

### 요구사항
모든 Controller 메소드의 인자가 HttpServletRequest req, HttpServletResponse resp 이다보니 사용자가 전달하는 값을 매번 HttpServletRequest req에서 가져와 형 변환을 해야하는 불편함이 있다.

Controller 메소드의 인자 타입에 따라 HttpServletRequest에서 값을 꺼내와 자동으로 형 변환을 한 후 매핑하는 등의 작업을 자동으로 했으면 좋겠다.

또한 URL을 통해서도 동적으로 값을 전달하는 방법이 있으면 좋겠다. 예를 들어 다음과 같이 개발하는 것이 가능하면 좋겠다.

```java
public class TestUserController {
    private static final Logger logger = LoggerFactory.getLogger(TestUsersController.class);

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ModelAndView create_string(String userId, String password) {
        logger.debug("userId: {}, password: {}", userId, password);
        return null;
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ModelAndView create_int_long(long id, int age) {
        logger.debug("id: {}, age: {}", id, age);
        return null;
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ModelAndView create_javabean(TestUser testUser) {
        logger.debug("testUser: {}", testUser);
        return null;
    }


    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public ModelAndView show_pathvariable(@PathVariable long id) {
        logger.debug("userId: {}", id);
        return null;
    }
}
```

### 힌트
#### HttpServletRequest에 값 전달하기
* Spring에서 제공하는 MockHttpServletRequest을 활용할 수 있다.
```java
MockHttpServletRequest request = new MockHttpServletRequest();
request.addParameter("userId", "javajigi");
request.addParameter("password", "password");
```

#### 메소드의 인자 이름 구하기
* java reflection의 Parameter에서 인자 이름을 구하면 arg0, arg1과 같이 나와 이름을 활용할 수 없다.

* 인자 이름은 Spring에서 제공하는 ParameterNameDiscoverer을 활용해 다음과 같이 구할 수 있다.

```java
ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

Class clazz = TestUsersController.class;
Method[] methods = clazz.getDeclaredMethods();
for (Method method : methods) {
    String[] parameterNames = nameDiscoverer.getParameterNames(method);
    for (int i = 0; i < parameterNames.length; i++) {
        String parameterName = parameterNames[i];
        logger.debug("parameter : {}", parameterName);
    }
}    
```

#### primitive type을 비교할 때
* 메소드 인자의 type이 primitive type 인 경우 Integer.class, Long.class와 비교할 수 없다.

* primitive type을 비교할 경우 int.class, long.class로 비교할 수 있다.

```java
if (parameterType.equals(int.class)) {
    values[i] = Integer.parseInt(value);
}
if (parameterType.equals(long.class)) {
    values[i] = Long.parseLong(value);
}
```

#### PathVariable 구현을 위해 URL 매칭과 값 추출
* Spring의 PathPattern을 활용해 쉽게 구현할 수 있다.

* PathPattern 사용에 대한 간단한 예제는 다음과 같다.

```java
package core.mvc.tobe;

import org.junit.jupiter.api.Test;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PathPatternTest {
    @Test
    void match() {
        PathPattern pp = parse("/users/{id}");
        assertThat(pp.matches(toPathContainer("/users/1"))).isTrue();
        assertThat(pp.matches(toPathContainer("/users"))).isFalse();
    }

    @Test
    void matchAndExtract() {
        Map<String, String> variables = parse("/users/{id}")
                .matchAndExtract(toPathContainer("/users/1")).getUriVariables();
        assertThat(variables.get("id")).isEqualTo("1");

        variables = parse("/{var1}/{var2}")
                .matchAndExtract(toPathContainer("/foo/bar")).getUriVariables();
        assertThat(variables.get("var1")).isEqualTo("foo");
        assertThat(variables.get("var2")).isEqualTo("bar");
    }

    private PathPattern parse(String path) {
        PathPatternParser pp = new PathPatternParser();
        pp.setMatchOptionalTrailingSeparator(true);
        return pp.parse(path);
    }

    private static PathContainer toPathContainer(String path) {
        if (path == null) {
            return null;
        }
        return PathContainer.parsePath(path);
    }
}
```
