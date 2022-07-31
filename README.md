# 프레임워크 구현
## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

# 🚀 1단계 - 자바 reflection

### 요구사항
- [x] [요구사항1] Question 클래스의 모든 필드, 생성자, 메소드에 대한 정보를 출력한다.
  - getDeclaredConstructors()
  - getDeclaredMethods()
  - getDeclaredFields()
- [x] [요구사항2] Junit3Test 클래스에서 `test`로 시작하는 메소드만 Java Replection을 활용해 실행하도록 구현한다.
  - getMethods()
  - method.invoke(new Object());
- [x] [요구사항3] Junit4Test 클래스에서 `@MyTest` 애노테이션으로 설정되어 있는 메소드만 Java Replection을 활용해 실행하도록 구현한다.
  - getMethods()
  - method.isAnnotationPresent(MyTest.class);
  - method.invoke(new Object()); 
- [ ] [요구사항4] Student 클래스의 name과 age 필드에 값을 할당한 후 getter 메소드를 통해 값을 확인한다.
- [ ] [요구사항5] Question 클래스의 인스턴스를 자바 Reflection API를 활용해 Question 인스턴스를 생성한다.
- [ ] [요구사항6] core.di.factory.example 패키지에서 @Controller, @Service, @Repository 애노테이션이 설정되어 있는 모든 클래스를 찾아 출력한다.


