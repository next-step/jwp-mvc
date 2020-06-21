package next.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import next.model.User;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateView {
    private String userId;
    private String password;
    private String name;
    private String email;

    public User toUser() {
        return new User(this.userId, this.password, this.name, this.email);
    }
}
