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




