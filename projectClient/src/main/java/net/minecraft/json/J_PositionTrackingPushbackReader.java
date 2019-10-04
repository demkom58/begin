package net.minecraft.json;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

final class J_PositionTrackingPushbackReader implements J_ThingWithPosition {
    private final PushbackReader field_27338_a;
    private int field_27337_b = 0;
    private int field_27340_c = 1;
    private boolean field_27339_d = false;

    public J_PositionTrackingPushbackReader(Reader reader) {
        this.field_27338_a = new PushbackReader(reader);
    }

    public void func_27334_a(char var1) throws IOException {
        --this.field_27337_b;
        if (this.field_27337_b < 0) {
            this.field_27337_b = 0;
        }

        this.field_27338_a.unread(var1);
    }

    public void func_27335_a(char[] var1) {
        this.field_27337_b -= var1.length;
        if (this.field_27337_b < 0) {
            this.field_27337_b = 0;
        }

    }

    public int func_27333_c() throws IOException {
        int var1 = this.field_27338_a.read();
        this.func_27332_a(var1);
        return var1;
    }

    public int func_27336_b(char[] var1) throws IOException {
        int var2 = this.field_27338_a.read(var1);

        for (char var6 : var1) {
            this.func_27332_a(var6);
        }

        return var2;
    }

    private void func_27332_a(int var1) {
        if (13 == var1) {
            this.field_27337_b = 0;
            ++this.field_27340_c;
            this.field_27339_d = true;
        } else {
            if (10 == var1 && !this.field_27339_d) {
                this.field_27337_b = 0;
                ++this.field_27340_c;
            } else {
                ++this.field_27337_b;
            }

            this.field_27339_d = false;
        }

    }

    @Override
    public int func_27331_a() {
        return this.field_27337_b;
    }

    @Override
    public int func_27330_b() {
        return this.field_27340_c;
    }
}
