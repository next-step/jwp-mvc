# 1단계 - 자바 reflection

## 실습 환경 구축 및 코드 리뷰
* 첫 번째 미션인 프레임워크, 라이브러리 구현 실습을 위한 저장소 저장소 브랜치에 자신의 github 아이디에 해당하는 브랜치가 있는지 확인한다. 없으면 미션 시작 버튼을 눌러 미션을 시작한다.
* 온라인 코드리뷰 요청 1단계 문서의 1단계부터 5단계까지 참고해 실습 환경을 구축한다.
* next.WebServerLauncher를 실행한 후 브라우저에서 http://localhost:8080으로 접근한다.
* 브라우저에 질문/답변 게시판이 뜨면 정상적으로 세팅된 것이다.

## 코드 리뷰
* 모든 사용자의 요청은 core.mvc.asis.DispatcherServlet 가 처리한다. 소스 코드 분석의 시작은 DispatcherServlet의 init -> service 순으로 분석한다.

## Java Reflection
### Java Reflection API 활용해 가능한 작업은?
* Junit4에서 @Test 애노테이션이 설정되어 있는 메소드를 단위 테스트로 실행하고 싶다.
* 현재 실행하는 클래스의 클래스, 필드, 메소드 정보를 알고 싶어요.
* 인자로 전달하는 클래스의 인스턴스를 생성한 후 메소드를 실행하고 싶어요.
* Eclipse/Intellij IDEA가 동적으로 setter와 getter 메소드를 만드는 방법이 뭘까요?
* 데이터베이스에서 조회한 데이터의 칼럼 이름과 자바 클래스의 필드 이름이 같은 경우 자동으로 매핑하고 싶어요.

### Java Reflection API란?
* 컴파일한 클래스 정보를 활용해 동적으로 프로그래밍이 가능하도록 지원하는 API

## Java Reflection 주요 API
### java.lang.Class
* String getName() : 패키지 + 클래스 이름을 반환한다.
* int getModifiers() : 클래스의 접근 제어자를 숫자로 반환한다.
* Field[] getFields() : 접근 가능한 public 필드 목록을 반환한다.
* Field[] getDeclaredFields() : 모든 필드 목록을 반환한다.
* Constructor[] getConstructors() : 접근 가능한 public 생성자 목록을 반환한다.
* Constructor[] getDeclaredConstructors() : 모든 생성자 목록을 반환한다.
* Method[] getMethods() : 부모 클래스, 자신 클래스의 접근 가능한 public 메서드 목록을 반환한다.
* Method[] getDeclaredMethods() : 모든 메서드 목록을 반환한다.

### java.lang.refelct.Constructor
* String getName() : 생성자 이름을 반환한다.
* int getModifiers() : 생성자의 접근 제어자를 숫자로 반환한다.
* Class[] getParameterTypes() : 생성자 패러미터의 데이터 타입을 반환한다.

### java.lang.refelct.Field
* String getName() : 필드 이름을 반환한다.
* int getModifiers() : 필드의 접근 제어자를 숫자로 반환한다.

### java.lang.refelct.Method
* String getName() : 메서드 이름을 반환한다.
* int getModifiers() : 메서드의 접근 제어자를 숫자로 반환한다.
* Class[] getParameterTypes() : 메서드 패러미터의 데이터 타입을 반환한다.


## Java Reflection 실습
### 요구사항 1 - 클래스 정보 출력
> src/test/java > next.reflection > ReflectionTest의 showClass() 메소드를 구현해 Question 클래스의 모든 필드, 생성자, 메소드에 대한 정보를 출력한다.
```java
public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);
    
    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());
    }
}
```
  
### 요구사항 2 - test로 시작하는 메소드 실행
> Junit3에서는 test로 시작하는 메소드를 자동으로 실행한다. 이와 같이 Junit3Test 클래스에서 test로 시작하는 메소드만 Java Reflection을 활용해 실행하도록 구현한다.
> 
> 구현은 src/test/java > next.reflection > Junit3Runner 클래스의 runner() 메소드에 한다.

```java

public class Junit3Test extends TestCase {
    public void test1() throws Exception {
        System.out.println("Running Test1");
    }

    public void test2() throws Exception {
        System.out.println("Running Test2");
    }

    public void three() throws Exception {
        System.out.println("Running Test3");
    }
}
```

```java
public class Junit3Runner {
    @Test
    public void runner() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

    }
}
```

#### 힌트
* Junit3Test의 모든 메소드 목록을 구한다(clazz.getDeclaredMethods())
* 메소드 이름이 test로 시작하는 경우 method.invoke(clazz.newInstance());
    * Class가 기본 생성자를 가질 경우 newInstance()를 활용해 인스턴스를 생성할 수 있다.
    
### 요구사항 3 - @Test 애노테이션 메소드 실행
> Junit4에서는 @Test 애노테이션일 설정되어 있는 메소드를 자동으로 실행한다. 이와 같이 Junit4Test 클래스에서 @MyTest 애노테이션으로 설정되어 있는 메소드만 Java Reflection을 활용해 실행하도록 구현한다.
> 
> 구현은 src/test/java > next.reflection > Junit4TestRunner 클래스의 run() 메소드에 한다.

```java
public class Junit4Test {
    @MyTest
    public void one() throws Exception {
        System.out.println("Running Test1");
    }

    @MyTest
    public void two() throws Exception {
        System.out.println("Running Test2");
    }

    public void testThree() throws Exception {
        System.out.println("Running Test3");
    }
}
```

```java
public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

    }
}
```

#### 힌트
* Method 클래스의 isAnnotationPresent(MyTest.class) 활용

### 요구사항 4 - private field에 값 할당

> 자바 Reflection API를 활용해 다음 Student 클래스의 name과 age 필드에 값을 할당한 후 getter 메소드를 통해 값을 확인한다.
> 
> 구현은 src/test/java > next.reflection > ReflectionTest 클래스의 privateFieldAccess() 메소드에 한다.

```java
public class Student {
    private String name;

    private int age;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
```

```java
public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);
    
    @Test
    public void privateFieldAccess() {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

    }
}
```

#### 힌트
* Class의 getDeclaredField(String name) 메소드를 이용해 private Field를 구한다.
* Field.setAccessible(true)로 설정한 후 값을 할당한다.
* new Studuent()로 Student 인스턴스를 직접 생성한 후 field.set(student, “재성”)과 같이 private 필드에 값을 할당할 수 있다.
* 최종적으로 Student 인스턴스에 값이 할당되어 있는지 확인한다.

### 요구사항 5 - 인자를 가진 생성자의 인스턴스 생성
> Question 클래스의 인스턴스를 자바 Reflection API를 활용해 Question 인스턴스를 생성한다.

#### 힌트
* 기본 생성자가 없는 경우 clazz.newInstance()로 인스턴스 생성할 수 없음.
* 인스턴스를 생성하기 위한 Constructor를 먼저 찾아야 함. Class의 getDeclaredConstructors() 활용.
* constructor.newInstance(Object... args)로 인스턴스 생성

### 요구사항 6 - component scan
> src/test/java 폴더의 core.di.factory.example 패키지를 보면 DI 테스트를 위한 샘플 코드가 있다.
> 
> core.di.factory.example 패키지에서 @Controller, @Service, @Repository 애노테이션이 설정되어 있는 모든 클래스를 찾아 출력한다.

#### 힌트
* Reflections Library 라이브러리를 활용한다. Reflections Library는 클래스패스에 이미 추가되어 있다.
* A Guide to the Reflections Library 문서를 참고한다.


## 1단계 실습 마무리
* 실습을 끝내면 코드 리뷰 1단계 문서의 7단계, 8단계를 참고해 자신의 저장소에 push한다.
* 코드 리뷰 2단계 문서를 참고해 코드 리뷰 요청(pull request)을 보내고, NextStep 우측 상단의 Github 아이콘을 클릭해 리뷰 요청을 보낸다.
* 피드백 또는 merge 될 때까지 기다린다.

> PR에 대한 수정 요청을 받아 코드를 수정하는 경우 새로운 PR을 보낼 필요가 없다.
> 
> 코드를 수정한 후 add/commit/push만 하면 자동으로 해당 PR에 추가된다.

* Slack을 통해 merge가 되는지 확인한 후에 코드 리뷰 3단계 과정으로 다음 단계 미션을 진행한다.