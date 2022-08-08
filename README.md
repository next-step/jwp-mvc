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

- Question 클래스의 모든 필드, 생성자, 메소드 정보 출력
    - `ReflectionTest` 의 `showClass()` 구현

### 요구사항 2 - test로 시작하는 메소드 실행

- `test`로 시작하는 메소드만 Java Reflection으로 실행 구현
    - `Junit3Runner` 의 `runner()` 구현

### 요구사항 3 - @Test 애노테이션 메소드 실행

- `@MyTest` 애노테이션 설정 메소드만 Java Reflection을 활용해 실행 구현
    - `Junit4TestRunner` 의 `run()` 구현

### 요구사항 4 - private field에 값 할당

- `Student` 클래스의 `name`, `age` 필드에 값 할당하고 확인
  - `ReflectionTest` 의 `privateFieldAccess()` 구현

### 요구사항 5 - 인자를 가진 생성자의 인스턴스 생성

- 자바 Reflection API를 활용해 `Question` 인스턴스 생성

### 요구사항 6 - component scan

- `core.di.factory.example` 패키지에서 `@Controller`, `@Service`, `@Repository` 애노테이션 설정된 모든 클래스를 찾아 출력


## 2단계 - @MVC 구현

### 요구사항 1 - 애노테이션 기반 MVC 프레임워크

- 새로운 MVC 프레임워크 테스트
  - `AnnotationHandlerMappingTest` 코드가 통과되도록 구현
- 실습을 기반 코드 및 라이브러리
  - (reflections)[https://github.com/ronmamo/reflections] 라이브러리를 활용하여 `@Controller` 클래스를 찾고 요청 url과 메소드 연결 구현 

### 요구사항 2 - 레거시 MVC와 애노테이션 기반 MVC 통합

- 구현되어 있던 컨트롤러를 애노테이션 기반으로 변경


## 3단계 - @MVC 구현(힌트)

### 요구사항 1 힌트

- 힌트1 - `ControllerScanner` 클래스 추가
  - 각 클래스에 대한 인스턴스 생성을 담당하는 `ControllerScanner` 클래스를 추가

- 힌트 2 - `AnnotationHandlerMapping` 클래스 추가 
  - `ControllerScanner` 를 통해 찾은 `@Controller` 클래스의 메소드 중 `RequestMapping` 애노테이션이 설정되어 있는 모든 메소드 찾기
  - `Method` 정보를 바탕으로 `Map<HandlerKey, HandlerExecution>`에 각 요청 URL과 URL과 연결되는 메소드 정보를 값으로 추가

- 힌트 3 - 요청에 대한 `Controller` 반환
  - `AnnotationHandlerMapping` 클래스에 클라이언트 요청 정보(`HttpServletRequest`)를 전달하면 요청에 해당하는 `HandlerExecution` 반환



### 요구사항 2 - 레거시 MVC와 애노테이션 기반 MVC 통합

- 기존의 `RequestMapping`을 `LegacyHandlerMapping`로 이름을 리팩토링

- 힌트 1 - HandlerMapping 추가
  - `RequestMapping`, `AnnotationHandlerMapping` 두 클래스의 공통된 부분을 인터페이스로 추상화

```java
public interface HandlerMapping {
    Object getHandler(HttpServletRequest request);
}
```

- 힌트 2 - HandlerMapping 초기화
  - `DispatcherServlet` 의 초기화(`init()` 메소드) 과정에서 `LegacyHandlerMapping`, `AnnotationHandlerMapping` 모두 초기화

- 힌트 3 - Controller 실행
  - `DispatcherServlet`의 `service()` 메소드에서 앞에서 초기화한 2개의 `HandlerMapping`에서 요청 URL에 해당하는 Controller를 찾아 메소드를 실행

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
