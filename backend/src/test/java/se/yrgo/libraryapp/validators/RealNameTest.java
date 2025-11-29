package se.yrgo.libraryapp.validators;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;


import static org.assertj.core.api.Assertions.assertThat;

public class RealNameTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/bad_words.txt")
    void shouldBlockWordsFromFile(String name) {
        boolean result = RealName.validate(name);
        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/bad_words.txt")
    void blockWordsFromFileWrittenInLeet(String name) {
        boolean result = RealName.validate(name);
        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Mario",
            "MARIO",
            "mario",
            "MaRiO",
            "MArio_123-2",
            "MARIO-_123_2",
            "mario-_222",
            "Mario/_-:23"
    })
    void shouldAllowValidNames(String name) {
        boolean result = RealName.validate(name);
        assertThat(result).isTrue();
    }















    }




