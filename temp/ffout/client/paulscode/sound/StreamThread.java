package paulscode.sound;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class StreamThread extends SimpleThread {
   private SoundSystemLogger logger = SoundSystemConfig.getLogger();
   private List streamingSources = new LinkedList();
   private final Object listLock = new Object();

   protected void cleanup() {
      this.kill();
      super.cleanup();
   }

   public void run() {
      // $FF: Couldn't be decompiled
   }

   public void watch(Source var1) {
      if (var1 != null) {
         if (!this.streamingSources.contains(var1)) {
            synchronized(this.listLock) {
               ListIterator var2 = this.streamingSources.listIterator();

               while(var2.hasNext()) {
                  Source var3 = (Source)var2.next();
                  if (var3 == null) {
                     var2.remove();
                  } else if (var1.channel == var3.channel) {
                     var3.stop();
                     var2.remove();
                  }
               }

               this.streamingSources.add(var1);
            }
         }
      }
   }

   private void message(String var1) {
      this.logger.message(var1, 0);
   }

   private void importantMessage(String var1) {
      this.logger.importantMessage(var1, 0);
   }

   private boolean errorCheck(boolean var1, String var2) {
      return this.logger.errorCheck(var1, "StreamThread", var2, 0);
   }

   private void errorMessage(String var1) {
      this.logger.errorMessage("StreamThread", var1, 0);
   }
}
