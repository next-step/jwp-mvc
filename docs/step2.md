# 2단계 - @MVC 구현

## 요구사항 1 - 애노테이션 기반 MVC 프레임워크
> 지금까지 나만의 MVC 프레임워크를 구현해 잘 활용해 왔다. 
> 
> 그런데 새로운 컨트롤러가 추가될 때마다 매번 RequestMapping 클래스에 요청 URL과 컨트롤러를 추가하는 작업이 귀찮다. 
> 
> 귀찮지만 이 정도는 그래도 참을 수 있다. 
> 
> 하지만 유지보수 차원에서 봤을 때 컨트롤러의 수가 계속해서 증가하고 있으며, 각 컨트롤러의 execute() 메소드를 보니 10라인이 넘어가는 경우도 거의 없다. 
> 
> 새로운 기능이 추가될 때마다 매번 컨트롤러를 추가하는 것이 아니라 메소드를 추가하는 방식이면 좋겠다.
> 
> 또 한 가지 아쉬운 점은 요청 URL을 매핑할 때 HTTP 메소드(GET, POST, PUT, DELETE 등)도 매핑에 활용할 수 있으면 좋겠다. 
> 
> HTTP 메소드에 대한 지원이 가능하다면 URL은 같지만 다른 메소드로 매핑하는 것도 가능할 것이다.
  
> 이 같은 단점을 보완하기 위해 다음과 같이 Controller를 구현할 수 있도록 지원하는 프레임워크 구현하려고 한다.
> 
> @RequestMapping()에 method 설정이 되어 있지 않으면 모든 HTTP method를 지원해야 한다.


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

### 새로운 MVC 프레임워크 테스트
* 앞의 기능을 지원하는 애노테이션 기반의 새로운 MVC 프레임워크를 구현해야 한다. 

* 효과적인 실습을 위해 새로운 MVC 프레임워크의 뼈대가 되는 코드(src/main/java 폴더의 core.mvc.tobe 패키지)와 테스트 코드(src/test/java 폴더의 core.mvc.tobe.AnnotationHandlerMappingTest)를 제공하고 있다. 

* AnnotationHandlerMappingTest 클래스의 3개의 테스트가 성공하면 새로운 MVC 프레임워크 구현을 완료한 것으로 생각하면 된다.

### 실습을 기반 코드 및 라이브러리
* 이 실습은 클래스패스로 설정되어 있는 클래스 중에 @Controller 애노테이션이 설정되어 있는 클래스를 찾기 위한 목적으로 reflections(https://github.com/ronmamo/reflections) 라이브러리를 활용할 수 있다. 

* 이 라이브러리를 활용해 @Controller 애노테이션이 설정되어 있는 클래스를 찾은 후 @RequestMapping 설정에 따라 요청 URL과 메소드를 연결하도록 구현할 수 있다.

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
