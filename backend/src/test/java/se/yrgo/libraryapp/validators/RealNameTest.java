package se.yrgo.libraryapp.validators;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class RealNameTest {

    @ParameterizedTest(name = "Blockera inläst ord: {0}")
    @ValueSource(strings = {
            "användare testord",
            "svordom användare",
            "hemligt namn",
            "ett förbjudet testord"
    })
    void shouldBlockWordsFromFile(String name) {
        boolean result = RealName.validate(name);
        assertThat(result).isFalse();
    }


    }




