package se.yrgo.libraryapp.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.LoginInfo;
import se.yrgo.libraryapp.entities.UserId;

import java.util.Optional;

import static java.text.DateFormatSymbols.getInstance;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class UserServiceTest {
    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder encoder;

    @Test
    void correctLogin() {
        String username = "testuser";
        String password = "password";
        String passwordHash = "hashedPassword";
        UserId id = UserId.of("1");
        LoginInfo info = new LoginInfo(id, passwordHash);

        when(userDao.getLoginInfo(username)).thenReturn(Optional.of(info));
        when(encoder.matches(password, passwordHash)).thenReturn(true);

        Optional<UserId> result = userService.validate(username, password);

        assertThat(result).isEqualTo(Optional.of(id));
    }

    @Test
    void registerUser() {
        String name = "testuser";
        String realName = "Ian 0'Toole";

        when(userDao.register(eq(name), anyString(), eq("Ian 0\\'Toole")))
                .thenReturn(true);

        boolean result = userService.registerUser(name, realName, "hello");

        assertThat(result).isTrue();

    }



    @Test
    void registerUserFail() {
        String name = "testuser";
        String realName = "Ian 0_Toole";

        when(userDao.register(eq(name), anyString(), anyString()))
                .thenReturn(false);

        boolean result = userService.registerUser(name, realName, "hello");

        assertThat(result).isFalse();
    }

    @Test
    void failToCallDao() {
        String name = "testuser";
        String realName = "Ian 0'Toole";

        String expectedCleanName = "Ian 0\\'Toole";

        when(userDao.register(eq(name), anyString(), eq(expectedCleanName)))
                .thenReturn(false);

        boolean result = userService.registerUser(name, realName, "hello");

        assertThat(result).isFalse();

        verify(userDao).register(eq(name), anyString(), eq(expectedCleanName));
    }


    @Test
    void shouldReturnFalseWhenNameIsNull() {
        boolean result = userService.isNameAvailable(null);
        assertThat(result).isFalse();

        verifyNoInteractions(userDao);
    }

    @Test
    void shouldReturnFalseWhenNameIsTooShort() {
        String name = "ab";
        boolean result = userService.isNameAvailable(name);
        assertThat(result).isFalse();
        verifyNoInteractions(userDao);
    }
}