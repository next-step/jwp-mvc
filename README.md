# 프레임워크 구현
## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

## Step1 - 자바 Reflection 구현 기능 목록

1. ReflectionTest.showClass() 구현 
    * Question 클래스의 모든 필드, 생성자, 메소드에 대한 정보를 출력
2. Junit3Test 에서 test 로 시작하는 메소드만 실행하도록 구현
3. Junit4Test 에서 @MyTest 어노테이션으로 시작하는 메소드만 실행하도록 구현
4. Student 클래스 private 필드에 값 할당
5. Reflection API 를 통해 Question 인스턴스 생성
6. core.di.factory.example 패키지에서 @Controller, @Service, @Repository 애노테이션이 설정되어 있는 모든 클래스를 찾아 출력


## Step2 - @MVC 구현

1. @Contoller 어노테이션을 가진 클래스들을 찾아본다.
2. @RequestMapping 어노테이션을 가진 메서드들을 찾아본다.
3. @RequestMapping 메서드들의 요청 정보를 키로 핸들러를 저장해본다.
4. RequestMapping 방식에서 AnnotationHandlerMapping 방식으로 점진적으로 넘어가기 위해, Adapter 인터페이스 구현
5. viewName 을 보고 어떤 View 객체가 필요한지 정하는 ViewResolver 구현
