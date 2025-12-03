package se.yrgo.libraryapp.services;

import java.util.Optional;
import javax.inject.Inject;

import org.pac4j.core.credentials.password.PasswordEncoder;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.*;


public class UserService {
    private UserDao userDao;
    private PasswordEncoder encoder;

    @Inject
    UserService(UserDao userDao, org.springframework.security.crypto.password.PasswordEncoder encoder) {
        this.userDao = userDao;
    }

    public Optional<UserId> validate(String username, String password) {
        Optional<LoginInfo> maybeLoginInfo = userDao.getLoginInfo(username);
        if (maybeLoginInfo.isEmpty()) {
            return Optional.empty();
        }

        LoginInfo loginInfo = maybeLoginInfo.get();

        if (!encoder.matches(password, loginInfo.getPasswordHash())) {
            return Optional.empty();
        }

        return Optional.of(loginInfo.getUserId());
    }

    public boolean registerUser(String name, String realname, String password){
        Argon2PasswordEncoder encoder = new Argon2PasswordEncoder();
        String passwordHash = encoder.encode(password);

        // handle names like Ian O'Toole
        String cleanRealName = realname.replace("'", "\\'");

       return userDao.register(name, passwordHash, cleanRealName);


    }

    public boolean isNameAvailable(String name) {
        if (name == null || name.trim().length() < 3) {
            return false;
        }
        return userDao.isNameAvilable(name);
    }

}
