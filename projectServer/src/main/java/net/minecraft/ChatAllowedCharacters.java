package net.minecraft;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ChatAllowedCharacters {
    public static final String allowedCharacters = getAllowedCharacters();
    public static final char[] field_22175_b = new char[]{'/', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '"', ':'};

    private static String getAllowedCharacters() {
        String var0 = "";

        try {
            BufferedReader var1 = new BufferedReader(new InputStreamReader(ChatAllowedCharacters.class.getResourceAsStream("/font.txt"), StandardCharsets.UTF_8));
            String var2 = "";

            while ((var2 = var1.readLine()) != null) {
                if (!var2.startsWith("#")) {
                    var0 = var0 + var2;
                }
            }

            var1.close();
        } catch (Exception e) {
        }

        return var0;
    }
}
