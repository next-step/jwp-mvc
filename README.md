# 프레임워크 구현
## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

## Java Reflection 주요 API

### java.lang.Class
* String getName() : 패키지 + 클래스 이름을 반환한다.
* int getModifiers() : 클래스의 접근 제어자를 숫자로 반환한다.
* Field[] getFields() : 접근 가능한 public 필드 목록을 반환한다.
* Field[] getDeclaredFields() : 모든 필드 목록을 반환한다.
* Constructor[] getConstructors() : 접근 가능한 public 생성자 목록을 반환한다.
* Constructor[] getDeclaredConstructors() : 모든 생성자 목록을 반환한다.
* Method[] getMethods() : 부모 클래스, 자신 클래스의 접근 가능한 public 메서드 목록을 반환한다.
* Method[] getDeclaredMethods() : 모든 메서드 목록을 반환한다.

### java.lang.refelct.Constructor
* String getName() : 생성자 이름을 반환한다.
* int getModifiers() : 생성자의 접근 제어자를 숫자로 반환한다.
* Class[] getParameterTypes() : 생성자 패러미터의 데이터 타입을 반환한다.

### java.lang.refelct.Field
* String getName() : 필드 이름을 반환한다.
* int getModifiers() : 필드의 접근 제어자를 숫자로 반환한다.

### java.lang.refelct.Method
* String getName() : 메서드 이름을 반환한다.
* int getModifiers() : 메서드의 접근 제어자를 숫자로 반환한다.
* Class[] getParameterTypes() : 메서드 패러미터의 데이터 타입을 반환한다.
