###새로 추가한 작업 $step1:
1. 리플렉션을 다루는 테스트 클래스 정리

###새로 추가한 작업 $step2:
1. 애노테이션 MVC 구현
2. 레거시 MVC와 애노테이션 기반 MVC 통합
3. 레거시와 어노테이션 기반 통합을 위해 일부 클래스만 변경 하였습니다
- 변경클래스
- HomeController
- CreateUserController
- 변경전 레거시 코드는 controller.bak 패키지로 이동하였습니다.
4. HandleView 클래스 추가
- 기존 view 인터페이스를 구현하였습니다
- view 랜더링 부분을 담당합니다.
5. HandlerExecution handle 메소드 정리
- 애노테이션 함수들을 호출하는 로직을 추가하였습니다
6. AnnotationHandlerMapping 어노태이션 검색 추가
7. DispatcherServlet 레거시 controller 와 애노테이션 controller 둘다 쓰기위해 로직을 변경 하였습니다.

###새로 추가한 작업 $step3:
1. HandlerMapping 인터 페이스 추가
- 기존 컨트롤러, 어노테이션 컨트롤러를 DispatcherServlet에 분리 시켰습니다
2. 기존 컨트롤러 execute 리턴값을 modelAndView로 변환

###새로 추가한 작업 $step3-1:
1. Controller 인터페이스 String 으로 수정
2. AnnotationCotroller 인터페이스 추가
3. BasicHandlerMapping 구조 수정
- 기존 핸들러를 가지고 있는 상테에서 받는 것으로 수정
- init 함수를 제거 addMapping 으로 추가와 동시에 init
- find 함수로 컨트롤러를 리턴
4. DispatcherServlet
- BasicHandlerMapping 에 컨트롤러 추가
