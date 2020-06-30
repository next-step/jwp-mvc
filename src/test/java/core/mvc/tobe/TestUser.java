package core.mvc.tobe;

public class TestUser {
    private String userId;
    private String password;
    private int age;

    public TestUser() {
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestUser testUser = (TestUser) o;

        if (age != testUser.age) {
            return false;
        }
        if (userId != null ? !userId.equals(testUser.userId) : testUser.userId != null) {
            return false;
        }
        return password != null ? password.equals(testUser.password) : testUser.password == null;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + age;
        return result;
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
