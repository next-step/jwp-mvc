package next.dao;

import core.jdbc.ConnectionManager;
import next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public void insert(User user) throws SQLException {
        dbTemplate(connection -> {
            String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, user.getUserId());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getName());
            ps.setString(4, user.getEmail());

            ps.executeUpdate();

            return ps;
        });
    }

    public void update(User user) throws SQLException {
        dbTemplate(connection -> {
            String sql = "UPDATE USERS SET userId=?, password=?, name=?, email=? WHERE userId=?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, user.getUserId());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getName());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getUserId());

            ps.executeUpdate();

            return ps;
        });
    }

    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();

        dbTemplate(connection -> {
            String sql = "SELECT * FROM USERS";

            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                users.add(new User(
                    resultSet.getString("userId"),
                    resultSet.getString("password"),
                    resultSet.getString("name"),
                    resultSet.getString("email")
                ));
            }

            return ps;
        });

        return users;
    }

    public User findByUserId(String userId) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);

            rs = pstmt.executeQuery();

            User user = null;
            if (rs.next()) {
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }

            return user;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    private interface DbStrategy {
        PreparedStatement execute(Connection connection) throws SQLException;
    }

//    @FunctionalInterface
//    private interface DbStrategyExecuteUpdate extends DbStrategy {
//        PreparedStatement execute(Connection connection) throws SQLException;
//    }

    private PreparedStatement dbTemplate(DbStrategy dbTemplate) throws SQLException {

        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();

            pstmt = dbTemplate.execute(con);
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }

            if (con != null) {
                con.close();
            }
        }

        return pstmt;
    }
}
