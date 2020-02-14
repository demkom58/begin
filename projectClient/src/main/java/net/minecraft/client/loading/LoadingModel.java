package net.minecraft.client.loading;

import net.minecraft.client.gui.Gui;

public class LoadingModel extends Gui {
    private final int totalLoadUnits;
    private int loadUnits = 0;
    private boolean done = false;

    private String title = "Initializing";

    public LoadingModel(int totalLoadUnits) {
        this.totalLoadUnits = totalLoadUnits;
    }

    public int getTotalLoadUnits() {
        return totalLoadUnits;
    }

    public void setLoadUnits(int loadUnits) {
        this.loadUnits = loadUnits;
    }

    public void addLoadUnits(int loadUnits) {
        this.loadUnits += loadUnits;
    }

    public int getLoadUnits() {
        return loadUnits;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public float getPercent(float scale) {
        return (loadUnits / ((float) totalLoadUnits) * scale);
    }

    public float getPercent() {
        return getPercent(100);
    }

}
