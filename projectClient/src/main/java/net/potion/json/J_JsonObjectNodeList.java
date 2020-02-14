package net.potion.json;

import java.util.HashMap;

class J_JsonObjectNodeList extends HashMap {
    // $FF: synthetic field
    final J_JsonObjectNodeBuilder field_27308_a;

    J_JsonObjectNodeList(J_JsonObjectNodeBuilder var1) {
        this.field_27308_a = var1;

        for (J_JsonFieldBuilder var3 : J_JsonObjectNodeBuilder.func_27236_a(this.field_27308_a)) {
            this.put(var3.func_27303_b(), var3.func_27302_c());
        }

    }
}
