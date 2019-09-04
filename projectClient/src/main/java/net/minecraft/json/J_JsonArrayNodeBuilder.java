package net.minecraft.json;

import java.util.LinkedList;
import java.util.List;

public final class J_JsonArrayNodeBuilder implements J_JsonNodeBuilder {
    private final List<J_JsonNodeBuilder> builders = new LinkedList<>();

    public J_JsonArrayNodeBuilder func_27240_a(J_JsonNodeBuilder var1) {
        this.builders.add(var1);
        return this;
    }

    public J_JsonRootNode func_27241_a() {
        LinkedList<J_JsonNode> var1 = new LinkedList<>();

        for (J_JsonNodeBuilder builder : this.builders) {
            var1.add(builder.func_27234_b());
        }

        return J_JsonNodeFactories.func_27309_a(var1);
    }

    // $FF: synthetic method
    // $FF: bridge method
    public J_JsonNode func_27234_b() {
        return this.func_27241_a();
    }
}
