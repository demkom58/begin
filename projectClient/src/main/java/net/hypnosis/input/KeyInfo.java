package net.hypnosis.input;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class KeyInfo {
    private static final Map<String, KeyInfo> NAMES = new HashMap<>();

    private final String name;
    private final KeySource source;
    private final int keyCode;

    KeyInfo(String keyName, KeySource source, int integer) {
        this.name = keyName;
        this.source = source;
        this.keyCode = integer;
        NAMES.put(keyName, this);
    }

    public KeySource getCategory() {
        return this.source;
    }

    public int getKeyCode() {
        return this.keyCode;
    }

    public String getName() {
        return this.name;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            KeyInfo info = (KeyInfo) o;
            return this.keyCode == info.keyCode && this.source == info.source;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(this.source, this.keyCode);
    }

    public String toString() {
        return this.name;
    }

}