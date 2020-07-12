package next.dto;

import next.model.User;

public class UserDto {
    private String userId;
    private String password;
    private String name;
    private String email;

    public UserDto() {
    }

    public User toEntity() {
        return new User(userId, password, name, email);
    }
}
