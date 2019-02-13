package net.minecraft;

public interface ISaveFormat {
    boolean isOldSaveType(String var1);

    boolean converMapToMCRegion(String var1, IProgressUpdate var2);
}
