# 프레임워크 구현
## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)


## step2 (MVC 구현)
### 요구사항 1
- @Controller 애노테이션이 설정되어있는 클래스 찾기
- @RequestMapping 설정에 따라 요청 URL과 메소드를 연결하도록 구현

### 요구사항 2
- RequestMapping, AnnotationHandlerMapping의 공통 부분인 HandlerMapping 인터페이스 추가
- DispatcherServlet에서 HandlerMapping 초기화
- DispatcherServlet에서 메소드 실행부분 구현 

### 요구사항 3

### 요구사항 4
- MethodArgumentResolver 인터페이스 구현
- 메소드의 파라미터 한개를 추상화한 MethodParameter 클래스 구현
- MethodArgumentResolver 구현체 구현 및 등록
    - UserObjectTypeArgumentResolver (o) (@RequestBody 대응)
    - PrimitiveTypeArgumentResolver (o) (@RequestParam, @RequestBody Primitive Type 대응)
    - HttpServletArgumentResolver (o) (HttpRequest, HttpRequest 대응)
    - PathVariableArgumentResolver (o) (@PathVariable 대응)
    - HttpSessionResolver (o) 
