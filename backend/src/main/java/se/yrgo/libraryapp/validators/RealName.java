package se.yrgo.libraryapp.validators;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This validator checks that the real names match our high standard for proper names. I.e. no bad
 * words.
 */
public final class RealName {
    private static final Logger logger = LoggerFactory.getLogger(RealName.class);
    private static final Set<String> invalidWords = new HashSet<>();

    static {

        InputStream is = RealName.class.getClassLoader().getResourceAsStream("bad_words.txt");
        try {
            if (is == null) {
                throw new IOException("Resource file 'bad_words.txt' not found in classpath.");
            }

            try (BufferedReader reader =
                         new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

                invalidWords.addAll(reader.lines()
                        .map(String::trim).
                        filter(s -> !s.isEmpty()).
                        map(String::toLowerCase)
                        .collect(Collectors.toSet()));
            }

        } catch (IOException ex) {
            logger.error("Unable to initialize list of bad words", ex);
        }
    }

    private RealName() {}

    /**
     * Validates if the given name is a valid and proper name.
     * * @param name the name to check
     * @return true if valid, false if not
     * */
    public static boolean validate(String name) {
        if (name == null || name.trim().isEmpty()) {
            return true;
        }

        String cleanName = Utils.cleanAndUnLeet(name);
        String[] words = cleanName.split("\\s+");

        for (String word : words) {
            if (invalidWords.contains(word)) {
                return false;
            }
        }

        return true;
    }
}