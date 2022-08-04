# 요구사항 정리

## 1단계 - 자바 reflection
### 요구사항 1 - 클래스 정보 출력
- src/test/java > next.reflection > ReflectionTest의 showClass() 메소드를 구현
- Question 클래스의 모든 필드, 생성자, 메소드에 대한 정보 출력


### 요구사항 2 - test로 시작하는 메소드 실행
- Junit3에서는 test로 시작하는 메소드를 자동으로 실행
- Junit3Test 클래스에서 test로 시작하는 메소드만 Java Reflection을 활용해 실행하도록 구현
- src/test/java > next.reflection > Junit3Runner 클래스의 runner() 메소드에 구현


### 요구사항 3 - @Test 애노테이션 메소드 실행
- Junit4에서는 @Test 애노테이션일 설정되어 있는 메소드를 자동으로 실행
- Junit4Test 클래스에서 @MyTest 애노테이션으로 설정되어 있는 메소드만 Java Reflection을 활용해 실행하도록 구현
- src/test/java > next.reflection > Junit4TestRunner 클래스의 run() 메소드에 구현


### 요구사항 4 - private field에 값 할당
- 자바 Reflection API를 활용해 다음 Student 클래스의 name과 age 필드에 값을 할당한 후 getter 메소드를 통해 값을 확인
- src/test/java > next.reflection > ReflectionTest 클래스의 privateFieldAccess() 메소드에 구현


### 요구사항 5 - 인자를 가진 생성자의 인스턴스 생성
- Question 클래스의 인스턴스를 자바 Reflection API를 활용해 Question 인스턴스를 생성


### 요구사항 6 - component scan
- src/test/java 폴더의 core.di.factory.example 패키지를 보면 DI 테스트를 위한 샘플 코드가 존재
- core.di.factory.example 패키지에서 @Controller, @Service, @Repository 애노테이션이 설정되어 있는 모든 클래스를 찾아 출력

<hr/>

## 2단계 - @MVC 구현
### 요구사항 1 - 애노테이션 기반 MVC 프레임워크
- 새로운 기능이 추가될 때마다 매번 컨트롤러를 추가하는 것이 아니라 메소드를 추가하는 방식
- URL을 매핑할 때 HTTP 메소드(GET, POST, PUT, DELETE 등) 지원(URL은 같지만 다른 메소드로 매핑하는 것도 가능)
- 효과적인 실습을 위해 새로운 MVC 프레임워크의 뼈대가 되는 코드(src/main/java 폴더의 core.mvc.tobe 패키지)와 테스트 코드(src/test/java 폴더의 core.mvc.tobe.AnnotationHandlerMappingTest)를 제공
- AnnotationHandlerMappingTest 클래스의 테스트가 성공하면 새로운 MVC 프레임워크 구현을 완료한 것으로 생각

### 요구사항 2 - 레거시 MVC와 애노테이션 기반 MVC 통합
- 이전에 구현되어 있던 컨트롤러를 애노테이션 기반으로 변경
- 점진적으로 리팩토링이 가능한 구조로 개발
  - 지금까지 사용한 MVC 프레임워크와 새롭게 구현한 애노테이션 기반 MVC 프레임워크가 공존
  - 기존의 모든 Controller가 새로운 MVC 프레임워크로 전환이 완료된 후 기존의 레거시 MVC 프레임워크를 삭제

<hr/>