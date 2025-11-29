package se.yrgo.libraryapp.validators;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThat;
public class UtilsTest {

    @ParameterizedTest
    @ValueSource(strings = {"Mario211", "Mario", "Mario1_1", "Mar1o_-12i1"})
    void removeNonLetters(String s) {
        String result = Utils.onlyLettersAndWhitespace(s);
        assertThat(result).as(s).matches("[a-z\\s]*");

        assertThat(result).isLowerCase();

        assertThat(result).doesNotContainPattern("[0-9]");

    }

    @ParameterizedTest
    @ValueSource(strings = {"H3ll0", "M4r10", "1337 Code"})
    void shouldUnLeetAndClean(String input) {
        String result = Utils.cleanAndUnLeet(input);

        assertThat(result).matches("[a-z\\s]*");

        assertThat(result).isNotEmpty();
    }

}
