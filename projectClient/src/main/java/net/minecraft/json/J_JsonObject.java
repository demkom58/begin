package net.minecraft.json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class J_JsonObject extends J_JsonRootNode {
    private final Map field_27222_a;

    J_JsonObject(Map var1) {
        this.field_27222_a = new HashMap(var1);
    }

    @Override
    public Map func_27214_c() {
        return new HashMap(this.field_27222_a);
    }

    @Override
    public EnumJsonNodeType func_27218_a() {
        return EnumJsonNodeType.OBJECT;
    }

    @Override
    public String getValue() {
        throw new IllegalStateException("Attempt to get text on a JsonNode without text.");
    }

    @Override
    public List func_27215_d() {
        throw new IllegalStateException("Attempt to get elements on a JsonNode without elements.");
    }

    public boolean equals(Object var1) {
        if (this == var1) {
            return true;
        } else if (var1 != null && this.getClass() == var1.getClass()) {
            J_JsonObject var2 = (J_JsonObject) var1;
            return this.field_27222_a.equals(var2.field_27222_a);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.field_27222_a.hashCode();
    }

    public String toString() {
        return "JsonObject fields:[" + this.field_27222_a + "]";
    }
}
