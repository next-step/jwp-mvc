package core.mvc.tobe;

import lombok.Getter;

@Getter
public class TestUser {
    private String userId;
    private String password;
    private int age;

    public TestUser(String userId, String password, int age) {
        this.userId = userId;
        this.password = password;
        this.age = age;
    }

    @Override
    public String toString() {
        return "TestUser{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                '}';
    }
}
