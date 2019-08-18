# 3단계 - @MVC 구현(힌트)

## 요구사항 1 힌트
### 새로운 MVC 프레임워크 구현 완료 후 클래스 다이어그램
![diagram](https://firebasestorage.googleapis.com/v0/b/nextstep-real.appspot.com/o/lesson-attachments%2F-Khyltunr6kVA4H56Rxo%2Fmvc%203rd.png?alt=media&token=bb00a790-7799-42e9-9fce-180f5d193fb6)

### 힌트1 - ControllerScanner 클래스 추가
> reflections 라이브러리를 활용해 @Controller 애노테이션이 설정되어 있는 모든 클래스를 찾고, 각 클래스에 대한 인스턴스 생성을 담당하는 ControllerScanner 클래스를 추가한다.
  
* @Controller 애노테이션이 설정되어 있는 모든 클래스를 찾는다.
```java
Reflections reflections = new Reflections("my.project");
Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Controller.class);
```

* 앞 단계에서 찾은 클래스에 대한 인스턴스를 생성해 Map<Class<?>, Object>에 추가한다.
```java
Class clazz = … ;
clazz.newInstance();
```

### 힌트 2 - AnnotationHandlerMapping 클래스 추가
> 애노테이션 기반 매핑을 담당할 AnnotationHandlerMapping 클래스를 추가한 후 초기화한다.

* ControllerScanner를 통해 찾은 @Controller 클래스의 메소드 중 RequestMapping 애노테이션이 설정되어 있는 모든 메소드를 찾는다. reflections 라이브러리를 활용한다.
```java
ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class));
``` 

* 앞 단계에서 찾은 java.lang.reflect.Method 정보를 바탕으로 Map<HandlerKey, HandlerExecution>에 각 요청 URL과 URL과 연결되는 메소드 정보를 값으로 추가한다.
```java
RequestMapping rm = method.getAnnotation(RequestMapping.class);
```

* Map의 HandlerKey는 RequestMapping 애노테이션이 가지고 있는 URL과 HTTP 메소드 정보를 가진다.
```java
private HandlerKey createHandlerKey(RequestMapping rm) {
    return new HandlerKey(rm.value(), rm.method());
}
```
* HandlerExecution은 자바 리플렉션에서 메소드를 실행하기 위해 필요한 정보를 가진다. 즉, 실행할 메소드가 존재하는 클래스의 인스턴스 정보와 실행할 메소드 정보(java.lang.reflect.Method)를 가져야 한다.

### 힌트 3 - 요청에 대한 Controller 반환
> AnnotationHandlerMapping 클래스에 클라이언트 요청 정보(HttpServletRequest)를 전달하면 요청에 해당하는 HandlerExecution을 반환하는 메소드를 구현한다.

* HandlerExecution getHandler(HttpServletRequest request); 메소드를 구현한다.

## 요구사항 2 - 레거시 MVC와 애노테이션 기반 MVC 통합
> 새로운 MVC 프레임워크도 추가했으니 이전에 구현되어 있던 컨트롤러를 애노테이션 기반으로 변경하면 된다. 
>
> 그런데 새로운 MVC 프레임워크를 적용하기 위해 한 번에 모든 컨트롤러를 변경하려면 일정 기간 동안 새로운 기능을 추가하거나 변경하는 작업을 중단해야 한다. 
>
> 이 같은 문제점을 보완하기 위해 점진적으로 리팩토링이 가능한 구조로 개발해야 한다.
>
> 즉, 지금까지 사용한 MVC 프레임워크와 새롭게 구현한 애노테이션 기반 MVC 프레임워크가 공존해야 한다. 
> 
> 기존의 모든 Controller가 새로운 MVC 프레임워크로 전환이 완료된 후 기존의 레거시 MVC 프레임워크를 삭제할 수 있다.

### 2개의 MVC 프레임워크를 통합하기 위한 기반 코드

* 기존 레거시 MVC 프레임워크의 RequestMapping을 LegacyHandlerMapping로 이름을 리팩토링했다고 가정했을 경우
```java
@WebServlet(name = "dispatcher", urlPatterns = { "", "/" }, loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private LegacyHandlerMapping lhm;
    private AnnotationHandlerMapping ahm;

    @Override
    public void init() throws ServletException {
        lhm = new LegacyHandlerMapping();
        lhm.initMapping();
        ahm = new AnnotationHandlerMapping("next.controller");
        ahm.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Controller controller = lhm.findController(req.getRequestURI());
        if (controller != null) {
            render(req, resp, controller.execute(req, resp));
        } else {
            HandlerExecution he = ahm.getHandler(req);
            render(req, resp, he.handle(req, resp));
        }    
    }

    […]
}

```
#### 힌트 1 - HandlerMapping 추가
> RequestMapping, AnnotationHandlerMapping은 요청 URL과 실행할 컨트롤러 클래스 또는 메소드를 매핑하는 역할은 같다. 
> 
> 단지 다른 점이라면 RequestMapping은 개발자가 수동으로 등록하고, AnnotationHandlerMapping은 애노테이션을 설정하면 자동으로 매핑한다는 점이다. 
> 
> 두 클래스의 공통된 부분을 인터페이스로 추상화한다.

* HandlerMapping 이름으로 인터페이스를 추가한다.

```java
public interface HandlerMapping {
    Object getHandler(HttpServletRequest request);
}
```
* RequestMapping 클래스를 LegacyHandlerMapping과 같은 형태로 클래스 이름을 변경한다.

#### 힌트 2 - HandlerMapping 초기화
* DispatcherServlet의 초기화(init() 메소드) 과정에서LegacyHandlerMapping, AnnotationHandlerMapping 모두 초기화한다. 

* 초기화한 2개의 HandlerMapping을 List로 관리한다.

#### 힌트 3 - Controller 실행
> DispatcherServlet의 service() 메소드에서는 앞에서 초기화한 2개의 HandlerMapping에서 요청 URL에 해당하는 Controller를 찾아 메소드를 실행한다.

```java
Object handler = getHandler(req);
if (handler instanceof Controller) {
    ModelAndView mav = ((Controller)handler).execute(req, resp);
} else if (handler instanceof HandlerExecution) {
    ModelAndView mav = ((HandlerExecution)handler).handle(req, resp);
} else {
    // throw exception
}
```

> 기존 컨트롤러를 새로 추가한 애노테이션 기반으로 설정한 후 정상적으로 동작하는지 테스트한다. 
> 
> 테스트에 성공하면 기존의 컨트롤러를 새로운 MVC 프레임워크로 점진적으로 변경한다.