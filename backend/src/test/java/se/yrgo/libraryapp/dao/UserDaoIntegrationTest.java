package se.yrgo.libraryapp.dao;
import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.radcortez.flyway.test.annotation.H2;
import org.pac4j.core.credentials.password.PasswordEncoder;
import se.yrgo.libraryapp.entities.LoginInfo;
import se.yrgo.libraryapp.entities.User;
import se.yrgo.libraryapp.entities.UserId;
@Tag("integration")
@H2
public class UserDaoIntegrationTest {
    private static DataSource ds;
    PasswordEncoder encoder;
    @BeforeAll
    static void initDataSource() {
// this way we do not need to create a new datasource every time
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        UserDaoIntegrationTest.ds = ds;
    }
    @Test
    void getUserById() {
// this data comes from the test migration files
        final String username = "test";
        final UserId userId = UserId.of(1);
        UserDao userDao = new UserDao(ds);
        Optional<User> maybeUser = userDao.get(Integer.toString(userId.getId()));
        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.get().getName()).isEqualTo(username);
        assertThat(maybeUser.get().getId()).isEqualTo(userId);
    }


    @Test
    void registerNewUser() {
        UserDao userDao = new UserDao(ds);

        final String username = "ny_anvandare";
        final String realName = "Sven Svensson";
        final String password = "hemligt_losenord";

        boolean result = userDao.register(username, realName, password);
        assertThat(result).isTrue();

        assertThat(userDao.isNameAvilable(username)).isFalse();

        Optional<LoginInfo> login = userDao.getLoginInfo(username);
        assertThat(login).isPresent();

        UserId assignedId = login.get().getUserId();

        Optional<User> createdUser = userDao.get(Integer.toString(assignedId.getId()));
        assertThat(createdUser).isPresent();

        assertThat(createdUser.get().getName()).isEqualTo(username);
        assertThat(createdUser.get().getRealname()).isEqualTo(realName);
        assertThat(createdUser.get().getId()).isEqualTo(assignedId);
    }

    @Test
    void updateUserRole() throws SQLException {
        UserDao userDao = new UserDao(ds);

        final String username = "ny_anvandare";
        final String realName = "Sven Svensson";
        final String password = "hemligt_losenord";

        boolean result = userDao.register(username, realName, password);
        assertThat(result).isTrue();

        Optional<LoginInfo> info = userDao.getLoginInfo(username);
        assertThat(info).isPresent();

        int newUserId = info.get().getUserId().getId();

        try(Connection conn = ds.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT role_id FROM user_role WHERE user_id = ?")) {

            ps.setInt(1, newUserId);

        try(ResultSet rs = ps.executeQuery()) {
            assertThat(rs.next()).isTrue();
            assertThat(rs.getInt("role_id")).isEqualTo(2);

        }

        }


    }





}