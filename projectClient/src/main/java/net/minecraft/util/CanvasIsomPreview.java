package net.minecraft.util;

import net.minecraft.client.TerrainTextureManager;
import net.minecraft.client.render.IsoImageBuffer;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkCoordinates;
import net.minecraft.world.storage.SaveHandler;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class CanvasIsomPreview extends Canvas implements KeyListener, MouseListener, MouseMotionListener, Runnable {
    private int field_1793_a = 0;
    private int zoomLevel = 2;
    private boolean displayHelpText = true;
    private World world;
    private File dataFolder = this.getMinecraftDir();
    private boolean running = true;
    private final List<IsoImageBuffer> imageBufferList = Collections.synchronizedList(new LinkedList<>());
    private IsoImageBuffer[][] imageBuffers = new IsoImageBuffer[64][64];
    private int field_1785_i;
    private int field_1784_j;
    private int xPosition;
    private int yPosition;

    public CanvasIsomPreview() {
        for (int var1 = 0; var1 < 64; ++var1) {
            for (int var2 = 0; var2 < 64; ++var2) {
                this.imageBuffers[var1][var2] = new IsoImageBuffer(null, var1, var2);
            }
        }

        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocus();
        this.setBackground(Color.red);
    }

    // $FF: synthetic method
    static boolean isRunning(CanvasIsomPreview var0) {
        return var0.running;
    }

    public File getMinecraftDir() {
        if (this.dataFolder == null) {
            this.dataFolder = EnumOS.getAppDir("minecraft");
        }

        return this.dataFolder;
    }

    public void loadWorld(String var1) {
        this.field_1785_i = this.field_1784_j = 0;
        this.world = new World(new SaveHandler(new File(this.dataFolder, "saves"), var1, false), var1, (new Random()).nextLong());
        this.world.skylightSubtracted = 0;
        synchronized (this.imageBufferList) {
            this.imageBufferList.clear();

            for (int var3 = 0; var3 < 64; ++var3) {
                for (int var4 = 0; var4 < 64; ++var4) {
                    this.imageBuffers[var3][var4].func_888_a(this.world, var3, var4);
                }
            }

        }
    }

    private void setTimeOfDay(int var1) {
        synchronized (this.imageBufferList) {
            this.world.skylightSubtracted = var1;
            this.imageBufferList.clear();

            for (int var3 = 0; var3 < 64; ++var3) {
                for (int var4 = 0; var4 < 64; ++var4) {
                    this.imageBuffers[var3][var4].func_888_a(this.world, var3, var4);
                }
            }

        }
    }

    public void func_1272_b() {
        new Thread(() -> {
            while (CanvasIsomPreview.isRunning(this)) {
                this.showNextBuffer();

                try {
                    Thread.sleep(1L);
                } catch (Exception ignored) {
                }
            }
        }).start();

        for (int i = 0; i < 8; ++i)
            new Thread(this).start();
    }

    public void exit() {
        this.running = false;
    }

    private IsoImageBuffer getImageBuffer(int var1, int var2) {
        int var3 = var1 & 63;
        int var4 = var2 & 63;
        IsoImageBuffer var5 = this.imageBuffers[var3][var4];
        if (var5.field_1354_c == var1 && var5.field_1353_d == var2)
            return var5;

        synchronized (this.imageBufferList) {
            this.imageBufferList.remove(var5);
        }

        var5.func_889_a(var1, var2);
        return var5;

    }

    @Override
    public void run() {
        TerrainTextureManager var1 = new TerrainTextureManager();

        while (this.running) {
            IsoImageBuffer var2 = null;
            synchronized (this.imageBufferList) {
                if (this.imageBufferList.size() > 0) {
                    var2 = this.imageBufferList.remove(0);
                }
            }

            if (var2 != null) {
                if (this.field_1793_a - var2.field_1350_g < 2) {
                    var1.func_799_a(var2);
                    this.repaint();
                } else {
                    var2.field_1349_h = false;
                }
            }

            try {
                Thread.sleep(2L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void update(Graphics var1) {
    }

    @Override
    public void paint(Graphics var1) {
    }

    public void showNextBuffer() {
        BufferStrategy var1 = this.getBufferStrategy();
        if (var1 == null) {
            this.createBufferStrategy(2);
        } else {
            this.drawScreen((Graphics2D) var1.getDrawGraphics());
            var1.show();
        }
    }

    public void drawScreen(Graphics2D var1) {
        ++this.field_1793_a;
        AffineTransform var2 = var1.getTransform();
        var1.setClip(0, 0, this.getWidth(), this.getHeight());
        var1.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        var1.translate(this.getWidth() / 2, this.getHeight() / 2);
        var1.scale(this.zoomLevel, this.zoomLevel);
        var1.translate(this.field_1785_i, this.field_1784_j);
        if (this.world != null) {
            ChunkCoordinates var3 = this.world.getSpawnPoint();
            var1.translate(-(var3.x + var3.z), -(-var3.x + var3.z) + 64);
        }

        Rectangle var17 = var1.getClipBounds();
        var1.setColor(new Color(-15724512));
        var1.fillRect(var17.x, var17.y, var17.width, var17.height);
        byte var4 = 16;
        byte var5 = 3;
        int var6 = var17.x / var4 / 2 - 2 - var5;
        int var7 = (var17.x + var17.width) / var4 / 2 + 1 + var5;
        int var8 = var17.y / var4 - 1 - var5 * 2;
        int var9 = (var17.y + var17.height + 16 + 128) / var4 + 1 + var5 * 2;

        for (int var10 = var8; var10 <= var9; ++var10) {
            for (int var11 = var6; var11 <= var7; ++var11) {
                int var12 = var11 - (var10 >> 1);
                int var13 = var11 + (var10 + 1 >> 1);
                IsoImageBuffer var14 = this.getImageBuffer(var12, var13);
                var14.field_1350_g = this.field_1793_a;
                if (!var14.field_1352_e) {
                    if (!var14.field_1349_h) {
                        var14.field_1349_h = true;
                        this.imageBufferList.add(var14);
                    }
                } else {
                    var14.field_1349_h = false;
                    if (!var14.field_1351_f) {
                        int var15 = var11 * var4 * 2 + (var10 & 1) * var4;
                        int var16 = var10 * var4 - 128 - 16;
                        var1.drawImage(var14.field_1348_a, var15, var16, null);
                    }
                }
            }
        }

        if (this.displayHelpText) {
            var1.setTransform(var2);
            int var18 = this.getHeight() - 32 - 4;
            var1.setColor(new Color(Integer.MIN_VALUE, true));
            var1.fillRect(4, this.getHeight() - 32 - 4, this.getWidth() - 8, 32);
            var1.setColor(Color.WHITE);
            String var19 = "F1 - F5: load levels   |   0-9: Set time of day   |   Space: return to spawn   |   Double click: zoom   |   Escape: hide this text";
            var1.drawString(var19, this.getWidth() / 2 - var1.getFontMetrics().stringWidth(var19) / 2, var18 + 20);
        }

        var1.dispose();
    }

    @Override
    public void mouseDragged(MouseEvent var1) {
        int var2 = var1.getX() / this.zoomLevel;
        int var3 = var1.getY() / this.zoomLevel;
        this.field_1785_i += var2 - this.xPosition;
        this.field_1784_j += var3 - this.yPosition;
        this.xPosition = var2;
        this.yPosition = var3;
        this.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent var1) {
    }

    @Override
    public void mouseClicked(MouseEvent var1) {
        if (var1.getClickCount() == 2) {
            this.zoomLevel = 3 - this.zoomLevel;
            this.repaint();
        }

    }

    @Override
    public void mouseEntered(MouseEvent var1) {
    }

    @Override
    public void mouseExited(MouseEvent var1) {
    }

    @Override
    public void mousePressed(MouseEvent var1) {
        int var2 = var1.getX() / this.zoomLevel;
        int var3 = var1.getY() / this.zoomLevel;
        this.xPosition = var2;
        this.yPosition = var3;
    }

    @Override
    public void mouseReleased(MouseEvent var1) {
    }

    @Override
    public void keyPressed(KeyEvent var1) {
        if (var1.getKeyCode() == 48) {
            this.setTimeOfDay(11);
        }

        if (var1.getKeyCode() == 49) {
            this.setTimeOfDay(10);
        }

        if (var1.getKeyCode() == 50) {
            this.setTimeOfDay(9);
        }

        if (var1.getKeyCode() == 51) {
            this.setTimeOfDay(7);
        }

        if (var1.getKeyCode() == 52) {
            this.setTimeOfDay(6);
        }

        if (var1.getKeyCode() == 53) {
            this.setTimeOfDay(5);
        }

        if (var1.getKeyCode() == 54) {
            this.setTimeOfDay(3);
        }

        if (var1.getKeyCode() == 55) {
            this.setTimeOfDay(2);
        }

        if (var1.getKeyCode() == 56) {
            this.setTimeOfDay(1);
        }

        if (var1.getKeyCode() == 57) {
            this.setTimeOfDay(0);
        }

        if (var1.getKeyCode() == 112) {
            this.loadWorld("World1");
        }

        if (var1.getKeyCode() == 113) {
            this.loadWorld("World2");
        }

        if (var1.getKeyCode() == 114) {
            this.loadWorld("World3");
        }

        if (var1.getKeyCode() == 115) {
            this.loadWorld("World4");
        }

        if (var1.getKeyCode() == 116) {
            this.loadWorld("World5");
        }

        if (var1.getKeyCode() == 32) {
            this.field_1785_i = this.field_1784_j = 0;
        }

        if (var1.getKeyCode() == 27) {
            this.displayHelpText = !this.displayHelpText;
        }

        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent var1) {
    }

    @Override
    public void keyTyped(KeyEvent var1) {
    }
}
