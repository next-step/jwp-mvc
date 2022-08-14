# 프레임워크 구현
## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

# 🚀 1단계 - 자바 reflection

### 요구사항
- [x] [요구사항1] Question 클래스의 모든 필드, 생성자, 메소드에 대한 정보를 출력한다.
  - getDeclaredConstructors()
  - getDeclaredMethods()
  - getDeclaredFields()
- [x] [요구사항2] Junit3Test 클래스에서 `test`로 시작하는 메소드만 Java Replection을 활용해 실행하도록 구현한다.
  - getMethods()
  - method.invoke(new Object());
- [x] [요구사항3] Junit4Test 클래스에서 `@MyTest` 애노테이션으로 설정되어 있는 메소드만 Java Replection을 활용해 실행하도록 구현한다.
  - getMethods()
  - method.isAnnotationPresent(MyTest.class);
  - method.invoke(new Object()); 
- [x] [요구사항4] Student 클래스의 name과 age 필드에 값을 할당한 후 getter 메소드를 통해 값을 확인한다.
  - declaredField.setAccessible(true);
  - declaredField.set(student, "홍길동"); 
- [x] [요구사항5] Question 클래스의 인스턴스를 자바 Reflection API를 활용해 Question 인스턴스를 생성한다.
  - constructors[0].newInstance
- [x] [요구사항6] core.di.factory.example 패키지에서 @Controller, @Service, @Repository 애노테이션이 설정되어 있는 모든 클래스를 찾아 출력한다.
  - reflections.getTypesAnnotatedWith(annotation)

# 🚀 2단계 - @MVC 구현

### 요구사항
- [x] @Controller, @RequestMapping 애노테이션이 적용된 컨트롤러를 찾아 실행할 수 있는 프레임워크 구현
  - [x] @RequestMapping은 method가 설정되지 않으면 모든 HTTP Method를 지원해야 한다.
  - [x] AnnotationHandlerMappingTest 테스트가 성공해야한다.
- [x] 기존 컨트롤러와 애너테이션이 적용된 컨트롤러가 공존해야 한다.
  - [x] DispatchServlet에서 RequestMapping과 AnnotationHandlerMapping 모두 적용되어야 한다.
  - [x] 기존의 컨트롤러와 동일한 뷰를 처리할 수 있도록 구현한다.

### 2단계 피드백
- [ ] AnnotationHandlerMapping 에서 컨트롤러를 등록하는 부분을 별도의 클래스로 위임 (3단계의 ControllerScanner?)


# 🚀 3단계 - @MVC 구현(힌트)

### 요구사항
힌트를 참고하여 리팩토링 및 구현하여 신규 MVC 프레임워크로 전환한다.

### TODO LIST
- [x] ControllerScanner 추가
  - [x] Reflections 라이브러리를 활용해 @Controller 애노테이션이 적용된 클래스를 찾는다.
  - [x] 찾은 컨트롤러의 인스턴트를 생성하여 Map 자료 구조에 저장한다. (한 번만 생성해서 재사용할 수 있도록 하기 위함?)
  - [x] 중복 인스턴스를 방지하기 위해 클래스의 이름으로 구분하여 저장한다.
  - [x] @Controller 의 값이 있는 경우 컨트롤러의 이름을 @Controller 애노테이션의 값으로 사용한다.
  - [x] @Controller 의 값이 없는 경우 컨트롤러의 이름을 lowerCamelCase 로 사용한다.
- [x] RequestMappingScanner 추가
  - [x] 컨트롤러 인스턴스를 기반으로 @RequestMapping 애너테이션이 적용된 메서드를 찾는다. 
    - [x] 인스턴스의 클래스에 @Controller 애노테이션이 없으면 예외를 발생시킨다.
    - [x] @RequestMapping 애노테이션이 적용된 메서드가 없으면 빈 값을 반환한다.
  - [x] @RequestMapping 정보로 HandlerKeyGenerator 를 활용하여 HandlerKey 를 생성한다. 
  - [x] 메서드와 컨트롤러 인스턴스로 HandlerExecution 을 생성하여 HandlerKey 를 키로 설정하여 Map 자료 구조로 반환한다. 
- [x] HandlerKey 목록을 생성하는 HandlerKeysGenerator 추가 
  - [x] @RequestMapping 의 정보로 HandlerKey 를 생성한다.
    - [x] 컨트롤러에 적용된 @RequestMapping 의 value 를 메서드 @RequestMapping value 의 prefix 로 적용한다. 
    - [x] 클래스에 적용된 RequestMethod 와 메서드에 적용된 RequestMethod 를 모두 허용한다.
      - [x] 컨트롤러에 @RequestMapping 이 없으면 메서드에 적용된 RequestMethod 만 허용한다.
    - [x] 클래스에 적용된 RequestMethod 는 있지만 메서드에 적용된 RequestMethod 가 없다면 클래스의 RequestMethod 만 허용한다.
    - [x] 클래스에 적용된 RequestMethod 가 없다면 메서드에 적용된 RequestMethod 만 허용한다. (둘 다 없다면 모든 RequestMethod 를 허용)
- [x] AnnotationHandlerMapping 에서 ControllerScanner, RequestMappingScanner 활용
  - [x] ControllerScanner 로 @Controller 애노테이션이 적용된 클래스의 인스턴스들을 찾는다.
  - [x] RequestMappingScanner 로 HandlerKey 와 HandlerExecutable 쌍을 생성하여 HANDLER_EXECUTIONS 에 저장한다.
- [x] HandlerMapping 추가
  - [x] RequestMapping 과 AnnotationHandlerMapping 의 공통부분을 추상화한 HandlerMapping 인터페이스를 생성한다.
  - [x] RequestMapping 과 AnnotationHandlerMapping 을 HandlerMapping 의 구현체로 변경한다.
- [x] DispatcherServlet 초기화 변경
  - [x] 초기화 과정에서 HandlerMapping 의 구현체들을 모두 초기화한다.
  - [x] 초기화한 HandlerMapping 을 List 자료 구조로 관리한다. 
  - [x] 클라이언트의 요청을 HandlerMapping 에서 찾아 컨트롤러를 실행한다.
- [x] 기존 컨트롤러들을 애너테이션 기반으로 모두 변경 후 정상적으로 동작해야한다.
  - [x] 1개의 컨트롤러를 애너테이션 기반으로 변경 후 테스트한다.
  - [x] 테스트에 성공하면 모든 컨트롤러를 애너테이션 기반으로 변경하여 테스트한다.
  - [x] 모든 컨트롤러의 테스트가 성공하면 신규 MVC 프레임워크로 전환한다.

### 3단계 피드백
- [x] ControllerScanner 가 불필요한 인스턴스 필드를 가지지 안도록 변경(유틸성으로 변경하여 정적 메서드로 컨스톨러 인스턴스들을 반환)
- [x] 테스트 컨트롤러 픽스처의 의도를 표현할 수 있도록 픽스처 패키지로 이동 

# 🚀 4단계 - Controller 메소드 인자 매핑

### 요구사항
Controller 메소드의 인자 타입에 따라 HttpServletRequest에서 값을 꺼내와 자동으로 형 변환을 한 후 매핑하는 등의 작업을 자동으로 했으면 좋겠다.


### TODO LIST
- [x] HandlerKey의 동등성 비교 변경 
  - [x] PathPattern을 활용하여 url 패턴 일치 여부에 따라 동등성 비교를 할 수 있도록 변경 
- [x] AnnotationHandlerMapping 
  - [x] requestUri 와 일치 하는 PathPattern을 가진 Key를 활용해 HandlerExecutable 을 찾는다
- [x] HandlerMethodArgumentResolver
  - [x] 파라미터의 목록 수 만큼 Object 배열을 생성한다.
    - [x] 스프링의 LocalVariableTableParameterNameDiscoverer 를 활용
  - [x] 파라미터의 이름으로 request.getParameter() 를 활용해 값을 꺼내온다
    - [x] 파라미터 이름이 없다면 아무 작업도 수행하지 않는다
  - [x] 메서드를 인자로 받아 메서드의 파라미터 목록을 구한다.
  - [x] 파라미터 타입에 맞춰 형 변환 후 전달할 Object 배열에 추가한다.
    - [x] 원시 타입
    - [x] Warpper 타입
    - [x] 커스텀 클래스 타입
    - [x] @PathVariable 애노테이션이 적용된 파라미터 
    - [x] HttpServletRequest 타입 
  - [x] Object 배열을 반환한다.
- [x] HandlerExecutable 변경
  - [x] 메서드를 반환하는 인터페이스를 추가한다 (수행될 메서드의 파라미터 정보를 얻기 위해 필요)
  - [x] 인터페이스에서 HttpServeltRequeset, HttpServletResponse 인자를 Object... 가변 인자로 변경한다
- [x] MethodParameter (Value Object)
  - [x] 메서드와 파라미터, 파라미터 네임과 관련된 정보를 가진다
