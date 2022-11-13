# 프레임워크 구현
## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

## Step 2 - @MVC 구현
- 요구사항1 - 애노테이션 기반 MVC 프레임워크
    - [x] HTTP 메소드 애노테이션 만들기 : `@RequestMapping` -> 기존에 존재하는 애노테이션 활용
    - [x] `@RequestMapping` 애노테이션 속성값이 없을 경우 모든 메소드를 받을 수 있도록 만들기

# Step 3 - @MVC 구현 (힌트)
> Step 2에서 리뷰어님에게 받은 피드백을 위주로 리팩토링한다.
> 
## Step 4 - Controller 메소드 인자 매핑

- 요구사항
    - [x] 메서드에 필요한 파라미터 HttpServletRequest에서 추출하기
    - [x] @PathVariable 애노테이션이 붙은 파라미터 값 requestURI에서 추출하기
    - [x] 기존 컨트롤러의 파라미터 전환하기x