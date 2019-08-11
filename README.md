# 프레임워크 구현
## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

## step1 - Java Reflection 실습
- [자바 Reflect API](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect)
- [Custom Annotation](https://advenoh.tistory.com/21)
- [ComponetScan참고](https://www.baeldung.com/reflections-library)


- [x] 요구사항 1 - 클래스 정보 출력
    - [x]  모든 필드, 생성자, 메소드에 대한 정보를 출력
- [x] 요구사항 2 - test로 시작하는 메소드 실행
- [x] 요구사항 3 - @Test 애노테이션 메소드 실행
- [x] 요구사항 4 - private field에 값 할당
- [x] 요구사항 5 - 인자를 가진 생성자의 인스턴스 생성
- [x] 요구사항 6 - component scan

## step2 - @MVC 구현

- [x] 요구사항 1 - 애노테이션 기반 MVC 프레임워크
    - [x] @Controller Class Scan
    - [x] @RequestMapping Method Scan
    - [x] RequestMapping 객체 인자 받아서 HandlerKey 생성
    - [x] HandlerExecution - method 실행로직 추가
    
- [ ] 요구사항 2 - 레거시 MVC와 애노테이션 기반 MVC 통합