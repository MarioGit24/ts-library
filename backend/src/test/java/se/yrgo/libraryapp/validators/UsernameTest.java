package se.yrgo.libraryapp.validators;

import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;


public class UsernameTest {

    @ParameterizedTest
    @ValueSource(strings = {"Mario", "MArsiio", "Ma_A-Z"})
    void correctUsername(String name1) {
        boolean result = Username.validate(name1);
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Mar", "MArio 1", "Mrios2_", "Mario!!!"})
    @EmptySource
    void incorrectUsername(String name) {
        boolean result = Username.validate(name);
        assertThat(result).isFalse();
    }
}
