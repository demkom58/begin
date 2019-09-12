package net.hypnosis.util;

import org.lwjgl.system.JNI;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.macosx.CoreFoundation;
import org.lwjgl.system.macosx.ObjCRuntime;

public class Sys {
    public static boolean openURL(String urlStr) {
        long objc_msgSend = ObjCRuntime.getLibrary().getFunctionAddress("objc_msgSend");

        try (MemoryStack stack = MemoryStack.stackPush()) {
            long string = CoreFoundation.CFStringCreateWithCStringNoCopy(MemoryUtil.NULL, stack.UTF8(urlStr), CoreFoundation.kCFStringEncodingUTF8, CoreFoundation.kCFAllocatorNull);

            long sharedWorkspace = JNI.invokePPP(objc_msgSend, ObjCRuntime.objc_getClass("NSWorkspace"), ObjCRuntime.sel_getUid("sharedWorkspace"));
            long url = JNI.invokePPPP(objc_msgSend, ObjCRuntime.objc_getClass("NSURL"), ObjCRuntime.sel_getUid("URLWithString:"), string);
            int result = JNI.invokePPPI(objc_msgSend, sharedWorkspace, ObjCRuntime.sel_getUid("openURL:"), url);

            CoreFoundation.CFRelease(string);
            return result == 1;
        }
    }
}
