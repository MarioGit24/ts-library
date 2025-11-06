package se.yrgo.libraryapp.validators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsernameTest {

    @ParameterizedTest
    @ValueSource(strings = {"Mario", "MArsiio", "Ma-:A-Z"})
    @EmptySource
    void correctUsername(String name1) {
        boolean result = Username.validate(name1);
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Mario", "MArio1", "AmArios2"})
    @EmptySource
    void incorrectUsername(String name) {
        boolean result = Username.validate(name);
        assertThat(result).isFalse();
    }
}
