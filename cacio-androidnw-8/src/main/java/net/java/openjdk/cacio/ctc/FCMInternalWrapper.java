package net.java.openjdk.cacio.ctc;

import java.lang.reflect.*;

import sun.font.*;
import sun.font.FontConfigManager.*;

public class FCMInternalWrapper {
    public static FontConfigInfo getFontConfigInfo(FontConfigManager instance) {
        return (FontConfigInfo) invokeMethod(instance, "getFontConfigInfo");
    }

    public static FcCompFont[] loadFontConfig(FontConfigManager instance) {
        return (FcCompFont[]) invokeMethod(instance, "loadFontConfig");
    }

    public static void populateFontConfig(FontConfigManager instance, FcCompFont[] arr) {
        invokeMethod(instance, "populateFontConfig", new Class[]{FcCompFont[].class}, new Object[]{arr});
    }
    
    private static Object invokeMethod(FontConfigManager instance, String method) {
        return invokeMethod(instance, method, null);
    }
    
    private static Object invokeMethod(FontConfigManager instance, String method, Class[] clsArr, Object... objs) {
        try {
            Method m;
            if (clsArr == null) {
                m = instance.getClass().getDeclaredMethod(method);
                m.setAccessible(true);
                return m.invoke(instance);
            } else {
                m = instance.getClass().getDeclaredMethod(method, clsArr);
                m.setAccessible(true);
                return m.invoke(instance, objs);
            }
            
        } catch (Throwable th) {
            throw new RuntimeException(th);
        }
    }
}

