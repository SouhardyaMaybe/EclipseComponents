package net.java.openjdk.cacio.ctc;

import java.io.File;

import java.lang.reflect.*;

import java.awt.*;
import java.awt.image.*;
/*
import sun.font.FontConfigManager;
import static sun.font.FontConfigManager.*;
*/
import sun.java2d.SunGraphicsEnvironment;
import sun.java2d.SurfaceManagerFactory;

public class CTCGraphicsEnvironment extends SunGraphicsEnvironment {
    static {
/*
        try {
            android.os.OpenJDKNativeRegister.registerNatives();
        } catch (Throwable th) {
            th.printStackTrace();
        }
*/

        // android.os.OpenJDKNativeRegister.registerNatives();
    
        // We should force set instead of use property, as property one get ignored (?)
        String propertyFontManager = System.getProperty("cacio.font.fontmanager");
        if (propertyFontManager != null) {
            FontManagerUtil.setFontManager(propertyFontManager);
        }
        
        try {
            String propertyFontScaler = System.getProperty("cacio.font.fontscaler");
            if (propertyFontScaler != null) {
                FontManagerUtil.setFontScaler(propertyFontScaler);
            }
            
            /*
             * Make AWT use Caciocavallo and not load libawt_xawt.so
             * to prevent linking X11 libraries.
             */
            if (Boolean.getBoolean(System.getProperty("java.awt.headless", "true"))) {
                // Initialize headless mode first
                Class.forName("java.awt.Toolkit");
                drawStringTest();

                // Set false it...
                System.setProperty("java.awt.headless", "false");

                // Set false to GraphicsEvnironment saved isHeadless
                Field headlessField = GraphicsEnvironment.class.getDeclaredField("headless");
                headlessField.setAccessible(true);
                headlessField.set(null, Boolean.FALSE);

                Field defaultHeadlessField = GraphicsEnvironment.class.getDeclaredField("defaultHeadless");
                defaultHeadlessField.setAccessible(true);
                defaultHeadlessField.set(null, Boolean.FALSE);
                
                Field localEnvField = GraphicsEnvironment.class.getDeclaredField("localEnv");
                localEnvField.setAccessible(true);
                localEnvField.set(null, new CTCGraphicsEnvironment());
                
                drawStringTest();
            }
            
            // System.setProperty("awt.toolkit", "net.java.openjdk.cacio.ctc.CTCToolkit");
            // System.setProperty("java.awt.graphicsenv", "net.java.openjdk.cacio.ctc.CTCGraphicsEnvironment");
        } catch (Throwable th) {
            // System.err.println("Unable to unheadless:");
            th.printStackTrace();
        }
    }
    
    private static void drawStringTest() {
        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) img.createGraphics();
        g2d.setPaint(Color.WHITE);
        g2d.drawString("Test", 0, 4);
        g2d.dispose();
        img = null;
    }
    
    public CTCGraphicsEnvironment() {
        SurfaceManagerFactory f = null;
        try {
            f = SurfaceManagerFactory.getInstance();
        } catch (IllegalStateException e) {
            SurfaceManagerFactory.setInstance(new CTCSurfaceManagerFactory());
        }
        if (f != null && !f.getClass().getName().equals(CTCSurfaceManagerFactory.class.getName())) {
            throw new IllegalStateException("The surface manager factory is already initialized to " + f.getClass().getName());
        }
    }

    @Override
    protected int getNumScreens() {
        return 1;
    }

    @Override
    protected GraphicsDevice makeScreenDevice(int screennum) {
        return new CTCGraphicsDevice();
    }

    @Override
    public boolean isDisplayLocal() {
        return true;
    }

    // Headless override
    /*
     @Override
     public boolean isHeadlessInstance() {
     return false;
     }

     @Override
     public static boolean isHeadless() {
     return false;
     }
     */
}
