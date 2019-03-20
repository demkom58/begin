package net.minecraft;

public interface IProgressUpdate {
    void display(String var1);

    void displayLoadingString(String var1);

    void setLoadingProgress(int var1);
}
