package net.minecraft;

public class UnexpectedThrowable {
    public final String description;
    public final Throwable throwable;

    public UnexpectedThrowable(String var1, Throwable throwable) {
        this.description = var1;
        this.throwable = throwable;
    }
}
