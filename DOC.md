- reflection api 를 통해 @Controller 어노테이션을 가지고 있는 클래스를 찾는다.
    - `AnnotationHandler` 인터페이스는 `Set<Class>` 를 반환하는 추상 메서드를 가지고 있다.
    - `ControllerAnnotaionHandler` 는 `AnnotationHandler` 의 구현체 이다.  
- 찾은 클래스들 내에서 @RequestMapping 을 갖는 method 를 찾는다.
    - method 를 통해 RequestMapping 어노테이션 메서드를 찾는다.  
- 어노테이션인 @RequestMapping 클래스 내의 인자 값을 기반으로 분기 처리가 가능하다.



 
