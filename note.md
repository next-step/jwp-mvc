# Controller 인자 매핑

- PathVariable
- RequestParameter

## 작업 노트

pr 전 bad smell 처리하고 가야 할 것 목록

- TypeConverter
    - if문 떡칠 -> 다형성으로 처리


## 1. PathVariable

간단한 코드고 이걸로 생각을 좀 단순히 만들어보자.    

```java
@Controller
@RequestMapping("/")
public class DummyController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView test(@PathVariable long id) {
        return null;
    }
}
```

매핑에 필요한 정보는 다음과 같다.  

- uri
- supported http methods
- method signature
    - method name
    - method parameters

여기서 포인트

- 매핑 uri는 패턴이 될 수 있다.  
- 파라미터가 annotated 될 수 있다.  

HandlerKey를 수정할 필요가 있음

AS-IS

* 현재는 단순히 URI를 string equals로 비교함

TO-BE

* PathPattern을 이용하는 것으로 변경 필요
