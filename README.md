# 프레임워크 구현
## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

## 요구사항
### 4단계: Controller 메소드 인자 매핑

* 아래와 같이 Controller 메소드의 인자를 동적으로 바인딩 한다.
```java
public class TestUserController {
    private static final Logger logger = LoggerFactory.getLogger(TestUsersController.class);

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ModelAndView create_string(String userId, String password) {
        logger.debug("userId: {}, password: {}", userId, password);
        // implement ...
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ModelAndView create_int_long(long id, int age) {
        logger.debug("id: {}, age: {}", id, age);
        // implement ... 
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ModelAndView create_javabean(TestUser testUser) {
        logger.debug("testUser: {}", testUser);
        // implement ...
    }


    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public ModelAndView show_pathvariable(@PathVariable long id) {
        logger.debug("userId: {}", id);
        // implement ...
    }
}
```

 1. 인자로 설정한 매개변수명과 동일한 이름으로 전달된 requestParameter값에 대하여
    1. 매개변수에 String 타입으로 전달할 수 있다.
    2. 파라미터 값이 숫자형인 경우 매개변수에 int,Integer,long, Long등 숫자형 타입으로 전달할 수 있다.
    3. 해당 파라미터 값들을 필드로 가지고 있는 오브젝트로 전달할 수 있다.
 2. 인자로 설정한 매개변수명과 동일한 이름으로 전달된 pathVariable에 대하여 
    1. 매개변수에 String 타입으로 전달할 수 있다. 
    2. 파라미터 값이 숫자형인 경우 매개변수에 int,Integer,long, Long등 숫자형 타입으로 전달할 수 있다.

