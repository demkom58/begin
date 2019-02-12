package net.minecraft.src;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public final class J_CompactJsonFormatter implements J_JsonFormatter {
   public String func_27327_a(J_JsonRootNode var1) {
      StringWriter var2 = new StringWriter();

      try {
         this.func_27329_a(var1, var2);
      } catch (IOException var4) {
         throw new RuntimeException("Coding failure in Argo:  StringWriter gave an IOException", var4);
      }

      return var2.toString();
   }

   public void func_27329_a(J_JsonRootNode var1, Writer var2) throws IOException {
      this.func_27328_a(var1, var2);
   }

   private void func_27328_a(J_JsonNode var1, Writer var2) throws IOException {
      // $FF: Couldn't be decompiled
   }
}
