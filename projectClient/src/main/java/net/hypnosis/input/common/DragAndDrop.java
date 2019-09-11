package net.hypnosis.input.common;

import net.hypnosis.monitor.Window;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public final class DragAndDrop {

    private DragAndDrop() {}

    public static void setFileDropCallback(@NotNull final Window window,
                                           @NotNull final FileDropCallback dropCallback) {
        GLFW.glfwSetDropCallback(window.getPointer(), dropCallback::onDragAndDrop);
    }

    public interface FileDropCallback {
        /**
         * Will be called when one or more dragged files are dropped on the window.
         *
         * @param window the window that received the event
         * @param count  the number of dropped files
         * @param names  pointer to the array of UTF-8 encoded path names of the dropped files
         */
        void onDragAndDrop(long window, int count, long names);
    }

}
