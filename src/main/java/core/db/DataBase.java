package core.db;

import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import next.model.User;

import java.util.Collection;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataBase {

    private static Map<String, User> users = Maps.newHashMap();

    static {
        String adminId = "admin";
        users.put(adminId, User.builder()
                .userId(adminId)
                .password("password")
                .name("자바지기")
                .email("admin@slipp.net")
                .build());
    }

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
