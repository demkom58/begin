package net.minecraft.json;

import java.util.List;
import java.util.Map;

public final class J_JsonStringNode extends J_JsonNode implements Comparable<J_JsonStringNode> {
    private final String value;

    J_JsonStringNode(String var1) {
        if (var1 == null)
            throw new NullPointerException("Attempt to construct a JsonString with a null value.");

        this.value = var1;
    }

    @Override
    public EnumJsonNodeType func_27218_a() {
        return EnumJsonNodeType.STRING;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public Map func_27214_c() {
        throw new IllegalStateException("Attempt to get fields on a JsonNode without fields.");
    }

    @Override
    public List func_27215_d() {
        throw new IllegalStateException("Attempt to get elements on a JsonNode without elements.");
    }

    public boolean equals(Object var1) {
        if (this == var1) {
            return true;
        } else if (var1 != null && this.getClass() == var1.getClass()) {
            J_JsonStringNode var2 = (J_JsonStringNode) var1;
            return this.value.equals(var2.value);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    public String toString() {
        return "JsonStringNode value:[" + this.value + "]";
    }

    public int func_27223_a(J_JsonStringNode var1) {
        return this.value.compareTo(var1.value);
    }

    // $FF: synthetic method
    // $FF: bridge method
    @Override
    public int compareTo(J_JsonStringNode var1) {
        return this.func_27223_a(var1);
    }
}
