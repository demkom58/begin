package net.potion.json;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public final class J_JdomParser {
    public J_JsonRootNode func_27366_a(Reader reader) throws J_InvalidSyntaxException, IOException {
        J_JsonListenerToJdomAdapter adapter = new J_JsonListenerToJdomAdapter();
        new J_SajParser().func_27463_a(reader, adapter);
        return adapter.func_27208_a();
    }

    public J_JsonRootNode func_27367_a(String text) throws J_InvalidSyntaxException {
        try {
            return this.func_27366_a(new StringReader(text));
        } catch (IOException e) {
            throw new RuntimeException("Coding failure in Argo:  StringWriter gave an IOException", e);
        }
    }
}
