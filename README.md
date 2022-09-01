# 프레임워크 구현
## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

## 1단계 - 자바 reflection

### 요구사항 1 - 클래스 정보 출력

* `ReflectionTest` 클래스의 `show_class()` 테스트 메서드를 구현.
  * `Question` 클래스의 모든 필드, 생성자, 메서드에 대한 정보를 출력.

### 요구사항 2 - test로 시작하는 메서드 실행

* `Junit3Runner` 클래스의 `run()` 테스트 메서드를 구현.
  * test로 시작하는 테스트 메서드만 Java Reflection을 활용해 실행하도록 구현.

### 요구사항 3 - @Test 어노테이션 메서드 실행

* `Junit4TestRunner` 클래스의 `run()` 테스트 메서드를 구현.
  * Junit4Test 클래스에서 @MyTest 어노테이션으로 설정되어 있는 메서드만 Java Reflection을 활용해 실행하도록 구현.

### 요구사항 4 - private field에 값 할당

* `ReflectionTest` 클래스의 `privateFieldAccess()` 테스트 메서드를 구현.
  * Student 클래스에서 private인 `name`과 `age` 필드에 값을 할당한다.
  * getter 메서드를 통해서 값을 확인한다.

### 요구사항 5 - 인자를 가진 생성자의 인스턴스 생성

* `Question` 클래스의 인스턴스를 Java Reflection을 활용해 Question 인스턴스를 생성.

### 요구사항 6 - component scan

* `core.di.factory.exaple` 패키지에서 `@Controller`, `@Service`, `@Repository` 어노테이션이 설정되어 있는 모든 클래스를 찾아 출력.

## 2단계 - @MVC 구현

### 요구사항 1 - 어노테이션 기반 MVC 프레임워크

* `RequestMapping` 클래스에 매번 url과 컨트롤러를 매핑해주는 방식을 개선한다.
  * 기능 추가시 매번 컨트롤러를 추가하는게 아닌 메서드를 추가하는 방식으로 수정
  * 요청 URL을 매핑할때 HTTP Method `GET`, `POST`, `PUT`, `DELETE`도 매핑하도록 수정
    * URL은 같지만 다른 HTTP Method로 매핑하는것도 가능
  * `@RequestMapping`에 method 설정 안돼있으면 모든 HTTP method를 지원.

* 클래스패스로 설정되어있는 클래스 중에 `@Controller`어노테이션이 설정된 클래스를 찾는다.
  * `RequestMapping` 설정에 따라 요청 URL과 메서드를 연결하도록 구현.
  * Java reflection을 사용하여 어노테이션 붙은 클래스 찾는다.

### 요구사항 2 - 레거시 MVC와 어노테이션 기반 MVC 통합

* 기존 MVC 프레임워크를 어노테이션 기반 프레임워크로 점진적으로 개선 시킨다.
  * 점진적 리팩토링 도입
    * 기존 MVC와 새로운 MVC가 공존.
    * 모든 Controller가 새로운 MVC로 전환되면 기존 레거시 MVC 삭제.

## 3단계 - @MVC 구현(힌트)

### 요구사항 - ControllerScanner 클래스 추가

* **ControllerScanner**
  * reflections 사용해서 `@Controller`어노테이션 설정된 클래스를 **찾는다**.
  * 찾은 각 클래스에 대한 **인스턴스 생성**을 담당한다.

* **AnnotationHandlerMapping**
  * 어노테이션 기반 매핑을 담당.
  * @Controller 클래스의 메서드 중 `@RequestMapping` 어노테이션 설정된 모든 메서드를 **찾는다**
  * 찾은 메서드를 바탕으로 `Map<HandlerKey, HandlerExecution>`에 값을 저장.
    * `HandlerKey` : `@RequestMapping` 어노테이션이 갖고 있는 URL과 HTTP 메서드를 저장.
    * `HandlerExecution` : 자바 리플랙션에서 메서드를 실행하기 위해 필요한 정보 저장.
      * 실행할 메서드가 존재하는 클래스의 인스턴스 정보, 실행할 메서드 정보 저장.

## 4단계 - Controller 메서드 인자 매핑

### 요구사항

* Controller 메서드의 인자 타입에 따라 `HttpServletRequest` 에서 값을 꺼내와 자동으로 **형 변환**을 한 후 매핑 작업 자동화
  * long, int, String
* URL을 통한 동적 값 전달
  * Spring 프레임워크의 `HandlerMethodArgumentResolver`

## Domain

### 1. MethodParameter

- MethodParameter를 캡슐화 시켜주는 객체
- 메서드에 `@RequestMapping` 어노테이션 존재 여부 판단
- 메서드 인자에
  - `@ModelAttribute`, `@PathVariable`, `@RequestParam` 존재 여부 판단
- 메서드와 메서드 인자 관련 정보 제공.

### 2. SimpleTypeConverter

HttpServletRequest 값을 형변환 시키는 객체
Integer, Long, String 형으로의 변환만을 지원.

String → 메서드 인자의 타입으로 변환

### 3. HandlerMethodArgumentResolverComposite

HandlerMethodArgumentResolver들을 저장.
저장되어있는 resolver들을 사용해서 MethodParameter를 resovle(해석)
해석한 MethodParameter를 캐싱해놓는다.

### 4. HandlerMethodArgumentResolver

어노테이션 기반 컨트롤러는 다양한 파라미터 사용이 가능.

- `HttpServletRequest`, `Model`, `@RequestParam`, `@ModelAttribute`, `@RequestBody`, `HttpEntity`

RequestMappingHandlerAdapter가 ArgumentResolver를 호출해서 핸들러가 필요로 하는 다양한 객체를 생성한다.

- `supportsParameter()`
  - 해당 파라미터 지원 여부를 확인
- `resolveArgument`
  - 해당 파라미터를 지원하면 실제 객체를 생성.
  - 생성된 객체를 컨트롤러로 반환

### 4.1 ModelAttributeMethodArgumentResolver

- `@ModelAttribute` 어노테이션이 붙은 메서드 인자를 찾아서 값을 추출
- 인자로 주어진 객체 생성
- String, int, Integer와 같은 단순 타입은 지원하지 않는다.
  - `@RequestParam` 이 지원

### 4.2 RequestParamMethodArgumentResolver

- `@RequestParam` 어노테이션이 붙은 메서드 인자를 찾아서 값을 추출.
- String, int, Integer 등의 단순 타입이면 `@RequestParam` 어노테이션 생략 가능
  - required = true (기본값) 이면 어노테이션 필수
  - required = false 이면 어노테이션 생략 가능

### 4.3 PathVariableMethodArgumentResolver

- `@PathVariable` 어노테이션이 붙은 메서드 인자를 찾아서 추출.
  - `@RequestMapping` 어노테이션이 붙은 메서드에서 사용 가능.
-

### 4.4 ServletRequestParamMethodArgumentResolver

- 서블릿 관련 요청 메서드의 인자를 추출.
