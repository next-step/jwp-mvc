package next.model.dto;

import next.model.User;

public class UserCreateRequestDto {
    private String userId;
    private String password;
    private String name;
    private String email;

    public User toEntity() {
        return new User(userId, password, name, email);
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
