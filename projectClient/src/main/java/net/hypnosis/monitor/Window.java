package net.hypnosis.monitor;

import net.hypnosis.render.gl.OpenGL;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

public class Window implements AutoCloseable {
    private final GLFWErrorCallback errorCallback = GLFWErrorCallback.create(this::printGlError);

    private WindowResizeCallback resizeCallback;
    private WindowPositionCallback positionCallback;
    private long pointer;

    private String title;

    private int x;
    private int y;

    private int width;
    private int height;

    private int windowedWidth;
    private int windowedHeight;

    private boolean fullscreen = false;
    private boolean vsync = false;

    private String phase;

    public Window(@NotNull final String title,
                  int width, int height, long share,
                  boolean fullscreen, boolean resizable, boolean vsync,
                  @Nullable final WindowResizeCallback resizeCallback,
                  @Nullable final WindowPositionCallback positionCallback,
                  @Nullable final WindowFocusCallback focusCallback,
                  @Nullable final WindowCloseCallback closeCallback) {
        this.throwExceptionOnGlError();
        this.setPhase("Window creation");

        this.title = title;

        if (width <= 0)
            width = 1;

        if (height <= 0)
            height = 1;

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, resizable ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);

        this.pointer = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, share);
        if (this.pointer == MemoryUtil.NULL)
            throw new RuntimeException("Failed to create window");

        moveToCenter();

        this.makeCurrentContext();
        OpenGL.createCapabilities();

        setResizeCallback(resizeCallback);
        setPositionCallback(positionCallback);
        setFocusCallback(focusCallback);
        setCloseCallback(closeCallback);

        GLFW.glfwSetWindowSizeCallback(pointer, this::onResize);
        GLFW.glfwSetWindowPosCallback(pointer, this::onPositionChanged);

        setVsync(vsync);
        setSize(width, height);
        setFullscreen(fullscreen);
    }

    public void setResizeCallback(WindowResizeCallback resizeCallback) {
        this.resizeCallback = resizeCallback;
    }

    public void setPositionCallback(WindowPositionCallback positionCallback) {
        this.positionCallback = positionCallback;
    }

    public void setFocusCallback(WindowFocusCallback focusCallback) {
        GLFW.glfwSetWindowFocusCallback(pointer, focusCallback == null ? null : focusCallback::onFocus);
    }

    public void setCloseCallback(WindowCloseCallback closeCallback) {
        GLFW.glfwSetWindowCloseCallback(pointer, closeCallback == null ? null : closeCallback::onClose);
    }

    private void onResize(long pointer, int width, int height) {
        if (pointer != this.pointer)
            return;

        if (width <= 0)
            width = 1;

        if (height <= 0)
            height = 1;

        this.width = width;
        this.height = height;

        if (this.resizeCallback != null)
            this.resizeCallback.onResize(pointer, width, height);
    }

    private void onPositionChanged(long window, int x, int y) {
        final Window wnd = Window.this;
        if (window != wnd.pointer)
            return;

        this.x = x;
        this.y = y;

        if (positionCallback != null)
            positionCallback.onPositionChanged(window, x, y);
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public long getPointer() {
        return pointer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        GLFW.glfwSetWindowTitle(pointer, title);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        setPosition(x, y);
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        setPosition(x, y);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        GLFW.glfwSetWindowPos(pointer, x, y);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        setSize(width, height);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        setSize(width, height);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;

        GLFW.glfwSetWindowSize(pointer, width, height);
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        if (this.fullscreen == fullscreen)
            return;

        this.fullscreen = fullscreen;
        if (fullscreen) {
            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            if (vidMode == null)
                throw new RuntimeException("Can't get primary monitor video mode");

            windowedWidth = width;
            windowedHeight = height;

            width = vidMode.width();
            height = vidMode.height();
        } else {
            width = windowedWidth;
            height = windowedHeight;
        }

        if (this.width <= 0)
            this.width = 1;

        if (this.height <= 0)
            this.height = 1;

        long newPointer = GLFW.glfwCreateWindow(width, height, title,
                fullscreen ? GLFW.glfwGetPrimaryMonitor() : MemoryUtil.NULL, pointer);
        GLFW.glfwDestroyWindow(pointer);
        pointer = newPointer;

        if (!fullscreen) {
            this.setSize(windowedWidth, windowedHeight);
            this.setPosition(x, y);
        }
    }

    public boolean isResizable() {
        return GLFW.glfwGetWindowAttrib(pointer, GLFW.GLFW_RESIZABLE) == GLFW.GLFW_TRUE;
    }

    public void setResizable(boolean resizable) {
        GLFW.glfwSetWindowAttrib(pointer, GLFW.GLFW_RESIZABLE, resizable ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }

    public boolean isVsync() {
        return vsync;
    }

    public void setVsync(boolean vsync) {
        this.vsync = vsync;
        GLFW.glfwSwapInterval(vsync ? 1 : 0);
    }

    public boolean isFocused() {
        return GLFW.glfwGetWindowAttrib(pointer, GLFW.GLFW_FOCUSED) == GLFW.GLFW_TRUE;
    }

    public void focus() {
        GLFW.glfwFocusWindow(pointer);
    }

    public boolean isCloseRequested() {
        return GLFW.glfwWindowShouldClose(pointer);
    }

    public void makeCurrentContext() {
        GLFW.glfwMakeContextCurrent(pointer);
    }

    public void update() {
        GLFW.glfwPollEvents();
        GLFW.glfwSwapBuffers(pointer);
    }

    public void pollEvents() {
        GLFW.glfwPollEvents();
    }

    public void swapBuffer() {
        GLFW.glfwSwapBuffers(pointer);
    }

    public void moveToCenter() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            GLFW.glfwGetWindowSize(pointer, pWidth, pHeight);

            GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            setPosition((vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
        }
    }

    public void destroy() {
        errorCallback.close();
        Callbacks.glfwFreeCallbacks(pointer);
        GLFW.glfwDestroyWindow(pointer);
        GLFW.glfwTerminate();
    }

    public void show() {
        GLFW.glfwShowWindow(pointer);
    }

    public void hide() {
        GLFW.glfwHideWindow(pointer);
    }

    @Override
    public void close() throws Exception {
        destroy();
    }

    private void throwExceptionOnGlError() {
        GLFW.glfwSetErrorCallback(Window::throwGlErrorException);
    }

    private static void throwGlErrorException(int errorCode, long descriptionPointer) {
        throw new IllegalStateException("GLFW error occurred " + errorCode + ": " + MemoryUtil.memUTF8(descriptionPointer));
    }

    public void printGlError(int errorCode, long descriptionPointer) {
        String description = MemoryUtil.memUTF8(descriptionPointer);
        System.out.println("########## GL ERROR ##########");
        System.out.println("@ " + this.phase);
        System.out.println(errorCode + ": " + description);
    }

    public void logOnGlError() {
        GLFWErrorCallback glfwErrorCallback = GLFW.glfwSetErrorCallback(this.errorCallback);
        if (glfwErrorCallback != null) {
            glfwErrorCallback.free();
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String title = "";

        private int width = 854;
        private int height = 480;

        private long share = MemoryUtil.NULL;
        private boolean fullscreen = false;
        private boolean resizable = true;
        private boolean vsync = false;

        private WindowResizeCallback resizeCallback = null;
        private WindowPositionCallback positionCallback = null;
        private WindowFocusCallback focusCallback = null;
        private WindowCloseCallback closeCallback = null;

        private Builder() {
        }

        public Builder title(@Nullable final String title) {
            this.title = title == null ? "" : title;
            return this;
        }

        public Builder width(int width) {
            this.width = Math.max(width, 1);
            return this;
        }

        public Builder height(int height) {
            this.height = Math.max(height, 1);
            return this;
        }

        public Builder share(long share) {
            this.share = share;
            return this;
        }

        public Builder fullscreen(boolean fullscreen) {
            this.fullscreen = fullscreen;
            return this;
        }

        public Builder resizable(boolean resizable) {
            this.resizable = resizable;
            return this;
        }

        public Builder vsync(boolean vsync) {
            this.vsync = vsync;
            return this;
        }

        public Builder onResize(WindowResizeCallback resizeCallback) {
            this.resizeCallback = resizeCallback;
            return this;
        }

        public Builder onPosition(WindowPositionCallback positionCallback) {
            this.positionCallback = positionCallback;
            return this;
        }

        public Builder onFocus(WindowFocusCallback focusCallback) {
            this.focusCallback = focusCallback;
            return this;
        }

        public Builder onClose(WindowCloseCallback closeCallback) {
            this.closeCallback = closeCallback;
            return this;
        }

        public Window build() {
            return new Window(
                    this.title, this.width, this.height, this.share, this.fullscreen, this.resizable, this.vsync,
                    this.resizeCallback, this.positionCallback, this.focusCallback, this.closeCallback
            );
        }
    }

}
