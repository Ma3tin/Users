import jakarta.enterprise.context.ApplicationScoped;

import java.sql.*;


@ApplicationScoped
public class UserRepository {
    public Boolean createUser(String fullName, String email, String hashedPassword) {
        try (
                Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost/users?user=root&password=");
                PreparedStatement preparedStatement = connection.prepareStatement("""
                        INSERT into user(fullName, email, hashedPassword, createdAt, updatedAt)
                        values (?, ?, ?, now(), now())
                        """)
        ) {


            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, hashedPassword);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public User getUser(String email) {
        try (
                Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost/users?user=root&password=");
                PreparedStatement preparedStatement = connection.prepareStatement("""
                        select u.userId, u.fullName, u.hashedPassword, u.createdAt, u.updatedAt
                        from user as u
                        where u.email = ?
                        """)
        ) {

            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return new User(
                        resultSet.getInt(1),
                        email,
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5)
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
