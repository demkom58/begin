package net.potion.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ChatAllowedCharacters {
    public static final String ALLOWED_CHARACTERS = getAllowedCharacters();
    public static final char[] ALLOWED_CHARACTERS_ARRAY = new char[]{'/', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '"', ':'};

    private static String getAllowedCharacters() {
        StringBuilder result = new StringBuilder();

        try {
            InputStreamReader reader = new InputStreamReader(ChatAllowedCharacters.class.getResourceAsStream("/font.txt"), StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                if (!temp.startsWith("#")) {
                    result.append(temp);
                }
            }

            bufferedReader.close();
        } catch (Exception e) { }

        return result.toString();
    }
}
