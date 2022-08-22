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
