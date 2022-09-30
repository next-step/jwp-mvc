# 프레임워크 구현
## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

## 요구사항
### 1 단계
- [x] 클래스 정보 출력
  - [x] Question 클래스의 모든 필드, 생성자, 메소드에 대한 정보를 출력한다.
- [x] test로 시작하는 메소드 실행
  - [x] Junit3에서는 test로 시작하는 메소드를 자동으로 실행한다.
- [x] @Test 애노테이션 메소드 실행
  - [x] Junit4에서는 @Test 애노테이션일 설정되어 있는 메소드를 자동으로 실행한다.
- [x] private field에 값 할당
  - [x] 자바 Reflection API를 활용해 다음 Student 클래스의 name과 age 필드에 값을 할당한 후 getter 메소드를 통해 값을 확인한다.
- [x] 인자를 가진 생성자의 인스턴스 생성
  - [x] Question 클래스의 인스턴스를 자바 Reflection API를 활용해 Question 인스턴스를 생성한다.
- [x] component scan
  - [x] core.di.factory.example 패키지에서 @Controller, @Service, @Repository 애노테이션이 설정되어 있는 모든 클래스를 찾아 출력한다.

### 2단계
- [x] 2단계 다시 구현
  - [x] 애노테이션 기반 MVC 프레임워크 구현
    - [x] ControllerScanner 구현
    - [x] AnnotationHandlerMappingTest 테스트가 잘 동작 하는지 확인
    - [x] 새로운 기능이 추가될 때마다 매번 컨트롤러를 추가하는 것이 아니라 메소드를 추가되도록 개발한다.
      - [x] @Controller 애노테이션이 설정되어 있는 클래스를 찾은 후 @RequestMapping 설정에 따라 요청 URL과 메소드를 연결하도록 구현
    - [x] HTTP 메소드에 대해 URL은 같지만 다른 메소드로 매핑되도록 한다.
  - [x] 레거시 MVC와 애노테이션 기반 MVC 통합
    - [x] 지금까지 사용한 MVC 프레임워크와 새롭게 구현한 애노테이션 기반 MVC 프레임워크가 공존하도록 함
      - [x] 기존에 사용하던 프레임워크를 주석 처리 해도 잘 동작하는지 체크

### 3단계
- [x] 코드 리뷰 반영
- [x] 3단계 힌트 참고하여 리팩토링

### 4단계
- [x] Controller 메소드의 인자 타입에 따라 HttpServletRequest에서 값을 꺼내와 자동으로 형 변환을 한 후 매핑하는 등의 작업을 자동으로 한다.
  - [x] 테스트 케이스를 작성하여 메서드가 인자에 맞게 invoke 되어 실행 되도록 테스트 한다.
- [x] URL을 통해서도 동적으로 값을 전달한다. 