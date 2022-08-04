# 프레임워크 구현

## 진행 방법

* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정

* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

## Step 1 - 자바 reflection

- 요구사항
    - [x] 클래스 정보 출력
      > src/test/java > next.reflection > ReflectionTest의 showClass() 메소드를 구현해 Question 클래스의 모든 필드, 생성자, 메소드에 대한 정보를
      출력한다.
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
    - [x] test로 시작하는 메소드 실행
      > Junit3에서는 test로 시작하는 메소드를 자동으로 실행한다. 이와 같이 Junit3Test 클래스에서 test로 시작하는 메소드만 Java Reflection을 활용해 실행하도록 구현한다.
      구현은 src/test/java > next.reflection > Junit3Runner 클래스의 runner() 메소드에 한다.
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
        - Junit3Test의 모든 메소드 목록을 구한다(clazz.getDeclaredMethods())
        - 메소드 이름이 test로 시작하는 경우 method.invoke(clazz.newInstance());
        - Class가 기본 생성자를 가질 경우 newInstance()를 활용해 인스턴스를 생성할 수 있다.

    - [x] @Test 애노테이션 메소드 실행
      > Junit4에서는 @Test 애노테이션일 설정되어 있는 메소드를 자동으로 실행한다. 이와 같이 Junit4Test 클래스에서 @MyTest 애노테이션으로 설정되어 있는 메소드만 Java
      Reflection을 활용해 실행하도록 구현한다.
      구현은 src/test/java > next.reflection > Junit4TestRunner 클래스의 run() 메소드에 한다.

        - 힌트: Method 클래스의 isAnnotationPresent(MyTest.class) 활용
    - [x] private field에 값 할당
      > 자바 Reflection API를 활용해 다음 Student 클래스의 name과 age 필드에 값을 할당한 후 getter 메소드를 통해 값을 확인한다. 구현은 src/test/java >
      next.reflection > ReflectionTest 클래스의 privateFieldAccess() 메소드에 한다.
        - Class의 getDeclaredField(String name) 메소드를 이용해 private Field를 구한다.
        - Field.setAccessible(true)로 설정한 후 값을 할당한다.
        - new Studuent()로 Student 인스턴스를 직접 생성한 후 field.set(student, “재성”)과 같이 private 필드에 값을 할당할 수 있다.
        - 최종적으로 Student 인스턴스에 값이 할당되어 있는지 확인한다.
    - [x] 인자를 가진 생성자의 인스턴스 생성
      > Question 클래스의 인스턴스를 자바 Reflection API를 활용해 Question 인스턴스를 생성한다.
        - 힌트1: 기본 생성자가 없는 경우 clazz.newInstance()로 인스턴스 생성할 수 없음.
        - 힌트2: 인스턴스를 생성하기 위한 Constructor를 먼저 찾아야 함. Class의 getDeclaredConstructors() 활용.
        - 힌트3: constructor.newInstance(Object... args)로 인스턴스 생성

    - [x] component scan
      > src/test/java 폴더의 core.di.factory.example 패키지를 보면 DI 테스트를 위한 샘플 코드가 있다.
      core.di.factory.example 패키지에서 @Controller, @Service, @Repository 애노테이션이 설정되어 있는 모든 클래스를 찾아 출력한다.
        - Reflections Library 라이브러리를 활용한다. Reflections Library는 클래스패스에 이미 추가되어 있다.
        - A Guide to the Reflections Library 문서를 참고한다.


## Step 2 - @MVC 구현
- 요구사항1 - 애노테이션 기반 MVC 프레임워크
  - [x] HTTP 메소드 애노테이션 만들기 : `@RequestMapping` -> 기존에 존재하는 애노테이션 활용
  - [x] `@RequestMapping` 애노테이션 속성값이 없을 경우 모든 메소드를 받을 수 있도록 만들기 

- 요구사항2 - 레거시 MVC와 애노테이션 기반 MVC 통합
  - [ ] 이전에 구현되어 있던 컨트롤러를 애노테이션 기반으로 변환작업
