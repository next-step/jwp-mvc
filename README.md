# 프레임워크 구현
## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

# 기능 요구사항 (Java Reflection 실습)
## 요구사항 1 - 클래스 정보 출력
>src/test/java > next.reflection > ReflectionTest 의 showClass() 메소드를 구현해 Question 클래스의 모든 필드, 생성자, 메소드에 대한 정보를 출력한다.

## 요구사항 2 - test 로 시작하는 메소드 실행
>Junit3에서는 test 로 시작하는 메소드를 자동으로 실행한다. 이와 같이 Junit3Test 클래스에서 메소드만 Java Reflection 을 활용해 실행하도록 구현한다.<br>
>구현은 src/test/java -> next.reflection -> Junit3Runner 클래스의 runner() 메소드에 한다.

## 요구사항 3 - @Test 애노테이션 메소드 실행
>Junit4에서는 @Test 애노테이션이 설정되어 있는 메소드를 자동으로 실행한다. 이와 같이 Junit4Test 클래스에서 @MyTest 애노테이션으로 설정되어 있는 메소드만 Java Reflection 을 활용해 실행하도록 구현한다.<br>
>구현은 src/test/java -> next.reflection -> Junit4TestRunner 클래스의 run() 메소드에 한다.

## 요구사항 4 - private field 에 값 할당
>자바 Reflection API 를 활용해 다음 Student 클래스의 name 과 age 필드에 값을 할당한 후 getter 메소드를 통해 값을 확인한다.<br>
>구현은 src/test/java -> next.reflection -> ReflectionTest 클래스의 privateFieldAccess() 메소드에 한다.

## 요구사항 5 - 인자를 가진 생성자의 인스턴스 생성
>Question 클래스의 인스턴스를 자바 Reflection API 를 활용해 Question 인스턴스를 생성한다.

## 요구사항 6 - component scan
>src/test/java 폴더의 core.di.factory.example 패키지를 보면 DI 테스트를 위한 샘플 코드가 있다.<br>
>core.di.factory.example 패키지에서 @Controller, @Service, @Repository 애노테이션이 설정되어 있는 모든 클래스를 찾아 출력한다.

# 기능 요구사항 (@MVC 구현)

> 지금까지 나만의 MVC 프레임워크를 구현해 잘 활용해 왔다. 그런데 새로운 컨트롤러가 추가될 때마다 매번 RequestMapping 클래스에 요청 URL 과 컨트롤러를 추가하는 작업이 귀찮다.<br>
> 이는 유지보수 차원에서 봤을 때 컨트롤러의 수가 계속해서 증가하고 있어서 좋지 않다. 또, 각 컨트롤러의 execute() 메소드는 10라인이 넘어가는 경우도 거의 없다. (메소드의 복잡도가 낮지만 계속해서 객체를 생성하는 것은 부담이다.)

이와 같은 단점을 보완하기 위해 다음과 같이 Controller 를 구현할 수 있도록 지원하는 **애노테이션 기반의 새로운 MVC 프레임워크**를 구현한다.
```java
@Controller
public class MyController {
    private static final Logger logger = LoggerFactory.getLogger(MyController.class);

    @RequestMapping("/users")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("users findUserId");
        return new ModelAndView(new JspView("/users/list.jsp"));
    }
    
    @RequestMapping(value="/users/show", method=RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("users findUserId");
        return new ModelAndView(new JspView("/users/show.jsp"));
    }
    
    @RequestMapping(value="/users", method=RequestMethod.POST)
    public ModelAndView create(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("users create");
        return new ModelAndView(new JspView("redirect:/users"));
    }
}
```
## 요구사항 1 - 애노테이션 기반 MVC 프레임워크
- 새로운 기능이 추가될 때마다 매번 컨트롤러를 추가하는 것이 아닌, 메소드를 추가하는 방식으로 구현한다.
- 요청 URL 을 매핑 할 때 HTTP 메소드(GET, POST, PUT, DELETE 등)도 매핑에 활용하도록 한다. (같은 URL 이지만, 다른 메소드로 매핑하는 것이 가능하도록 한다.)
- @RequestMapping() 에 method 설정이 되어 있지 않으면 모든 HTTP method 를 지원해야 한다.


## 요구사항 2 - 레거시 MVC 와 애노테이션 기반 MVC 통합
- 새로운 MVC 프레임워크를 추가했으니 이전에 구현되어 있던 레거시 MVC 컨트롤러를 애노테이션 기반 MVC 으로 변경한다.
>그런데 새로운 MVC 프레임워크를 적용하기 위해 한 번에 기존의 모든 컨트롤러를 변경하려면 변경 과정 동안 새로운 기능을 추가하거나 변경하는 작업을 중단해야 한다.
- 따라서, 점진적으로 리팩토링이 가능한 구조로 개발한다.
- 즉, 레거시 MVC 프레임워크와 새롭게 구현한 애노테이션 MVC 프레임워크가 공존해야 한다.
- 레거시 MVC 프레임워크가 새로운 애노테이션 MVC 프레임워크로 전환이 완료된 후, 기존의 레거시 MVC 프레임워크를 삭제한다.

# 기능 요구사항 (Controller 메소드 인자 매핑)
현재 Controller 메소드의 인자가 모두 `HttpServletRequest req, HttpServletResponse resp` 이므로, 매번 req 에서 값을 가져와서 형 변환을 해야 하는 불편함이 있다.
Controller 메소드의 인자 타입에 따라 HttpServletRequest 에서 값을 꺼내와서 자동으로 형 변환을 한 후 매핑 하는 작업을 하도록 구현한다.
또, URL 을 통해서 동적으로 값을 전달 하도록 한다.
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

## 기능 목록
- HandlerMapping 인터페이스
  - LegacyRequestMapping 구현체
    - path 에 대한 Controller 구현체들을 mapping
    - mapping 된 Controller 를 반환한다.
  - AnnotationHandlerMapping 구현체
    - Reflections 라이브러리 이용하여 @Controller 의 @RequestMapping path & request method 를 mapping 하도록 구현한다.
    - mapping 된 HandlerExecution 을 반환한다.
- ControllerScanner
  - @Controller 애노테이션이 설정되어 있는 모든 클래스를 찾는다.
  - 찾은 클래스와 그 인스턴스를 가지는 `Map<Class<?>, Object>` 를 반환하는 메서드를 구현한다.
- HandlerExecution
  - handler(request 에 해당하는 controller)의 method 를 실행시킨다.
- HandlerKey
  - Request Mapping 을 위한 unique handler key (url & request method)
- View 인터페이스
  - render 추상 메서드를 통해 화면에 보여줄 페이지를 동적으로 생성할 수 있도록 한다.
  - ResourceView 구현체
    - page 에 대한 render 처리를 진행한다. (RequestDispatcher 활용)
- ModelAndView
  - 화면에 보여줄 view (path) 와 데이터 (model)을 관리한다.
- HandlerAdapter 인터페이스
  - Legacy Controller 와 Annotation 기반 Controller 의 handler 를 처리 하기 위한 어댑터 인터페이스 (처리할 Controller or HandlerExecution 을 찾는 지원 메서드 & handler 를 실행할 메서드)
  - 레거시 MVC 와 애노테이션 기반 MVC 의 Controller 를 통합하여 관리한다.
  - ControllerHandlerAdapter 구현체
    - Legacy Controller 에 대한 지원 메서드를 구현한다.
  - HandlerExecutionHandlerAdapter 구현체
    - Annotation 기반 Controller 에 대한 지원 메서드를 구현한다.
- MethodParameter 객체
  - 특정 메서드의 파라미터의 책임을 가진다.
  - 해당 파라미터의 이름, 파라미터 타입, 애노테이션 존재 여부, primitive, wrapper, model type 여부를 지원한다.
- HandlerMethodArgumentResolver 인터페이스
  - Controller 의 파라미터(argument)의 타입을 찾아서 HttpServletRequest 의 값을 매핑해주는 메서드를 지원한다.
  - supportsParameter 메서드를 통해 특정 파라미터의 Resolver 를 지원하는지 확인한다. 
  - resolveArgument 메서드를 통해 특정 파라미터의 값을 추출하여 반환한다.
  - HttpRequestArgumentResolver 구현체
    - 컨트롤러의 파라미터 타입이 HttpServletRequest 일 경우, 그대로 그 값을 반환한다.
  - HttpResponseArgumentResolver 구현체
    - 컨트롤러의 파라미터 타입이 HttpServletResponse 일 경우, 그대로 그 값을 반환한다. 
  - ModelArgumentResolver 구현체
    - Model (객체 without HttpServletRequest, HttpServletResponse) 타입의 파라미터를 지원한다.
    - 해당 객체의 생성자(전체 필드 갯수 매핑)를 찾아서 해당 필드의 이름과 그 값을 찾아서 인스턴스화 한 값을 반환한다.
  - PathVariableArgumentResolver 구현체
    - @PathVariable 애노테이션이 붙은 파라미터를 지원한다.
    - 메서드의 RequestMapping value 값에 contain 된 value 를 찾아서 반환한다.
    - PathVariable Option -> value 혹은 name 값을 우선으로 매핑하고, value, name 이 없을 경우, 파라미터 이름으로 매핑한다.
  - RequestParamArgumentResolver 구현체
    - @RequestParam 애노테이션이 붙은 파라미터를 지원한다.
    - RequestParam Option -> value 혹은 name 값을 우선으로 매핑하고, value, name 이 없을 경우, 파라미터 이름으로 매핑한다.
  - SimpleTypeArgumentResolver 구현체
    - primitive or wrapper type 에 대한 파라미터를 지원한다.
    - 파라미터 타입을 통해 값을 매핑하고 그 값을 반환한다.
- TypeConverter 객체
  - primitive type, wrapper type 에 대해서 input(String) 값을 해당 type 에 맞게 converting 해준다.
- ArgumentResolverMapping 객체
  - 파라미터 resolving 을 지원하는 객체를 매핑해준다.
>Legacy 네이밍으로 시작하는 클래스들은 애노테이션 기반 MVC 패턴이 적용 된 후 삭제한다.