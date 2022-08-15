# 프레임워크 구현
## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

## 1단계 - 자바 reflection
1. 클래스 정보 출력
   1. showClass() 메소드를 구현해 Question 클래스의 모든 필드, 생성자, 메소드에 대한 정보를 출력한다.
2. test로 시작하는 메소드 실행
   1. Junit3Test 클래스에서 test로 시작하는 메소드만 실행한다.
3. @Test 애노테이션 메소드 실행
   1. Junit4Test 클래스에서 @MyTest 애노테이션으로 설정되어 있는 메소드만 실행한다.
4. private field에 값 할당
   1. Student 클래스의 name과 age 필드에 값을 할당한 후 getter 메소드를 통해 값을 확인한다.
5. 인자를 가진 생성자의 인스턴스 생성
   1. Question 클래스의 인스턴스를 생성한다.
6. component scan
   1. @Controller, @Service, @Repository 애노테이션이 설정되어 있는 모든 클래스를 찾아 출력한다.

## 2단계 - MVC 구현
1. 애노테이션 기반 MVC 프레임워크
   1. reflection 라이브러리를 사용하여, @Controller 애노테이션이 붙은 클래스를 찾는다.
   2. @RequestMapping 설정에 따라 요청 URL과 메소드를 연결한다. (@RequestMapping에 method 설정이 되어있지 않으면 모든 HTTP method 지원)
2. 레거시 MVC와 애노테이션 기반 MVC 통합
   1. 점진적인 리팩토링이 가능한 구조로 개발한다. (기존 코드와 신규 코드가 공존)