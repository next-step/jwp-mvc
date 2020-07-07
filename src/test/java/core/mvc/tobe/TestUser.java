package core.mvc.tobe;

public class TestUser {
    private String userId;
    private String password;
    private int age;

    public TestUser() { }

    public TestUser(String userId, String password, int age) {
        this.userId = userId;
        this.password = password;
        this.age = age;
    }

    public String getUserId() {
        return userId;
}

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAge(int age) {
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
