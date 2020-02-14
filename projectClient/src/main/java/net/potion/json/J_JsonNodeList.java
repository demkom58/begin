package net.potion.json;

import java.util.ArrayList;

final class J_JsonNodeList extends ArrayList<J_JsonNode> {
    final Iterable<J_JsonNode> field_27405_a;

    J_JsonNodeList(Iterable<J_JsonNode> var1) {
        this.field_27405_a = var1;

        for (J_JsonNode var3 : this.field_27405_a) {
            this.add(var3);
        }

    }
}
