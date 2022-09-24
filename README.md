# 프레임워크 구현
## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

### 구현 기능 명세
#### Step 1
* 클래스 정보 출력
  * Question 클래스의 모든 필드 정보 출력
  * Question 클래스의 생성자 정보 출력
  * Question 클래스의 모든 메소드 정보 출력
* test로 시작하는 메소드 실행
  * Junit3Test 클래스에서 test로 시작하는 메소드만 실행
* @Test 애노테이션 메소드 실행
  * @MyTest 애노테이션 설정 메소드만 실행
* private field에 값 할당
* 인자를 가진 생성자의 인스턴스 생성
* Component Scan
  * @Controller, @Service, @Repository 애노테이션이 설정되어 있는 모든 클래스를 찾아 출력.

#### Step 2
* 애노테이션 기반 MVC 프레임워크 
  * RequestMapping 기반의 요청 처리
  * method 정보가 없을 경우 method All 허용
* 기존 프레임 워크를 애노테이션 기반으로 변경

#### Step 3
* 클래스 다이어 그램에 맞는 구조로 변경
  * ControllerScanner 클래스 추가
    * reflections 라이브러리를 활용해 @Controller 애노테이션이 설정되어 있는 모든 클래스를 찾고, 인스턴스 생성을 담당하는 클래스를 추가.
  * AnnotationHandlerMapping 클래스 추가 (Step2 에서 완료)
  * 해당하는 HandlerExecution을 반환하는 메소드 구현 (Step2 에서 완료)
* 레거시 MVC와 애노테이션 기반 MVC 통합 (Step2 에서 완료)

#### Step4
* Controller 메소드의 인자 타입에 따라 자동 형변환 후 매핑
* URL을 통해서 동적으로 값 전달
