package paulscode.sound.codecs;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import paulscode.sound.ICodec;
import paulscode.sound.SoundBuffer;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemLogger;

public class CodecWav implements ICodec {
   private static final boolean GET = false;
   private static final boolean SET = true;
   private static final boolean XXX = false;
   private boolean endOfStream = false;
   private boolean initialized = false;
   private AudioInputStream myAudioInputStream = null;
   private SoundSystemLogger logger = SoundSystemConfig.getLogger();

   public void reverseByteOrder(boolean var1) {
   }

   public boolean initialize(URL var1) {
      this.initialized(true, false);
      this.cleanup();
      if (var1 == null) {
         this.errorMessage("url null in method 'initialize'");
         this.cleanup();
         return false;
      } else {
         try {
            this.myAudioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(var1.openStream()));
         } catch (UnsupportedAudioFileException var3) {
            this.errorMessage("Unsupported audio format in method 'initialize'");
            this.printStackTrace(var3);
            return false;
         } catch (IOException var4) {
            this.errorMessage("Error setting up audio input stream in method 'initialize'");
            this.printStackTrace(var4);
            return false;
         }

         this.endOfStream(true, false);
         this.initialized(true, true);
         return true;
      }
   }

   public boolean initialized() {
      return this.initialized(false, false);
   }

   public SoundBuffer read() {
      if (this.myAudioInputStream == null) {
         return null;
      } else {
         AudioFormat var1 = this.myAudioInputStream.getFormat();
         if (var1 == null) {
            this.errorMessage("Audio Format null in method 'read'");
            return null;
         } else {
            int var2 = 0;
            int var3 = 0;
            byte[] var4 = new byte[SoundSystemConfig.getStreamingBufferSize()];

            try {
               while(!this.endOfStream(false, false) && var2 < var4.length) {
                  if ((var3 = this.myAudioInputStream.read(var4, var2, var4.length - var2)) <= 0) {
                     this.endOfStream(true, true);
                     break;
                  }

                  var2 += var3;
               }
            } catch (IOException var7) {
               this.endOfStream(true, true);
               return null;
            }

            if (var2 <= 0) {
               return null;
            } else {
               if (var2 < var4.length) {
                  var4 = trimArray(var4, var2);
               }

               byte[] var5 = convertAudioBytes(var4, var1.getSampleSizeInBits() == 16);
               SoundBuffer var6 = new SoundBuffer(var5, var1);
               return var6;
            }
         }
      }
   }

   public SoundBuffer readAll() {
      if (this.myAudioInputStream == null) {
         this.errorMessage("Audio input stream null in method 'readAll'");
         return null;
      } else {
         AudioFormat var1 = this.myAudioInputStream.getFormat();
         if (var1 == null) {
            this.errorMessage("Audio Format null in method 'readAll'");
            return null;
         } else {
            byte[] var2 = null;
            int var3 = var1.getChannels() * (int)this.myAudioInputStream.getFrameLength() * var1.getSampleSizeInBits() / 8;
            if (var3 > 0) {
               var2 = new byte[var1.getChannels() * (int)this.myAudioInputStream.getFrameLength() * var1.getSampleSizeInBits() / 8];
               int var4 = 0;
               int var5 = 0;

               try {
                  while((var4 = this.myAudioInputStream.read(var2, var5, var2.length - var5)) != -1 && var5 < var2.length) {
                     var5 += var4;
                  }
               } catch (IOException var11) {
                  this.errorMessage("Exception thrown while reading from the AudioInputStream (location #1).");
                  this.printStackTrace(var11);
                  return null;
               }
            } else {
               int var13 = 0;
               int var15 = 0;
               int var6 = 0;
               Object var7 = null;

               for(byte[] var20 = new byte[SoundSystemConfig.getFileChunkSize()]; !this.endOfStream(false, false) && var13 < SoundSystemConfig.getMaxFileSize(); var2 = appendByteArrays(var2, var20, var15)) {
                  var15 = 0;
                  var6 = (boolean)0;

                  try {
                     while(var15 < var20.length) {
                        if ((var6 = this.myAudioInputStream.read(var20, var15, var20.length - var15)) <= 0) {
                           this.endOfStream(true, true);
                           break;
                        }

                        var15 += var6;
                     }
                  } catch (IOException var10) {
                     this.errorMessage("Exception thrown while reading from the AudioInputStream (location #2).");
                     this.printStackTrace(var10);
                     return null;
                  }

                  var13 += var15;
               }
            }

            byte[] var14 = convertAudioBytes(var2, var1.getSampleSizeInBits() == 16);
            SoundBuffer var17 = new SoundBuffer(var14, var1);

            try {
               this.myAudioInputStream.close();
            } catch (IOException var9) {
               ;
            }

            return var17;
         }
      }
   }

   public boolean endOfStream() {
      return this.endOfStream(false, false);
   }

   public void cleanup() {
      if (this.myAudioInputStream != null) {
         try {
            this.myAudioInputStream.close();
         } catch (Exception var2) {
            ;
         }
      }

      this.myAudioInputStream = null;
   }

   public AudioFormat getAudioFormat() {
      return this.myAudioInputStream == null ? null : this.myAudioInputStream.getFormat();
   }

   private synchronized boolean initialized(boolean var1, boolean var2) {
      if (var1) {
         this.initialized = var2;
      }

      return this.initialized;
   }

   private synchronized boolean endOfStream(boolean var1, boolean var2) {
      if (var1) {
         this.endOfStream = var2;
      }

      return this.endOfStream;
   }

   private static byte[] trimArray(byte[] var0, int var1) {
      byte[] var2 = null;
      if (var0 != null && var0.length > var1) {
         var2 = new byte[var1];
         System.arraycopy(var0, 0, var2, 0, var1);
      }

      return var2;
   }

   private static byte[] convertAudioBytes(byte[] var0, boolean var1) {
      ByteBuffer var2 = ByteBuffer.allocateDirect(var0.length);
      var2.order(ByteOrder.nativeOrder());
      ByteBuffer var3 = ByteBuffer.wrap(var0);
      var3.order(ByteOrder.LITTLE_ENDIAN);
      if (var1) {
         ShortBuffer var4 = var2.asShortBuffer();
         ShortBuffer var5 = var3.asShortBuffer();

         while(var5.hasRemaining()) {
            var4.put(var5.get());
         }
      } else {
         while(var3.hasRemaining()) {
            var2.put(var3.get());
         }
      }

      var2.rewind();
      if (!var2.hasArray()) {
         byte[] var6 = new byte[var2.capacity()];
         var2.get(var6);
         var2.clear();
         return var6;
      } else {
         return var2.array();
      }
   }

   private static byte[] appendByteArrays(byte[] var0, byte[] var1, int var2) {
      if (var0 == null && var1 == null) {
         return null;
      } else {
         byte[] var3;
         if (var0 == null) {
            var3 = new byte[var2];
            System.arraycopy(var1, 0, var3, 0, var2);
            Object var6 = null;
         } else if (var1 == null) {
            var3 = new byte[var0.length];
            System.arraycopy(var0, 0, var3, 0, var0.length);
            Object var4 = null;
         } else {
            var3 = new byte[var0.length + var2];
            System.arraycopy(var0, 0, var3, 0, var0.length);
            System.arraycopy(var1, 0, var3, var0.length, var2);
            Object var5 = null;
            Object var7 = null;
         }

         return var3;
      }
   }

   private void errorMessage(String var1) {
      this.logger.errorMessage("CodecWav", var1, 0);
   }

   private void printStackTrace(Exception var1) {
      this.logger.printStackTrace(var1, 1);
   }
}
