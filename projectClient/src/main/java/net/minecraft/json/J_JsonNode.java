package net.minecraft.json;

import java.util.List;
import java.util.Map;

public abstract class J_JsonNode {
    public abstract EnumJsonNodeType func_27218_a();

    public abstract String getValue();

    public abstract Map<J_JsonStringNode, J_JsonNode> func_27214_c();

    public abstract List func_27215_d();

    public final String func_27213_a(Object... objects) {
        return (String) this.func_27219_a(J_JsonNodeSelectors.func_27349_a(objects), this, objects);
    }

    public final List<J_JsonNode> func_27217_b(Object... objects) {
        return (List<J_JsonNode>) this.func_27219_a(J_JsonNodeSelectors.func_27346_b(objects), this, objects);
    }

    private Object func_27219_a(J_JsonNodeSelector var1, J_JsonNode var2, Object[] objects) {
        try {
            return var1.func_27357_b(var2);
        } catch (J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException e) {
            throw J_JsonNodeDoesNotMatchPathElementsException.func_27319_a(e, objects, J_JsonNodeFactories.func_27315_a(var2));
        }
    }
}
