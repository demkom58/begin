package net.minecraft.json;

final class J_JsonStringNodeSelector extends J_LeafFunctor {
    public boolean func_27072_a(J_JsonNode var1) {
        return EnumJsonNodeType.STRING == var1.func_27218_a();
    }

    @Override
    public String func_27060_a() {
        return "A short form string";
    }

    public String func_27073_b(J_JsonNode var1) {
        return var1.getValue();
    }

    public String toString() {
        return "a value that is a string";
    }

    // $FF: synthetic method
    // $FF: bridge method
    @Override
    public Object func_27063_c(Object var1) {
        return this.func_27073_b((J_JsonNode) var1);
    }

    // $FF: synthetic method
    // $FF: bridge method
    @Override
    public boolean func_27058_a(Object var1) {
        return this.func_27072_a((J_JsonNode) var1);
    }
}
