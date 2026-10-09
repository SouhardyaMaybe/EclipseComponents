package net.java.openjdk.cacio.ctc;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.PrintJob;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.dnd.peer.DragSourceContextPeer;
import java.awt.font.TextAttribute;
import java.awt.im.InputMethodHighlight;
import java.awt.im.spi.InputMethodDescriptor;
import java.awt.image.ColorModel;
import java.awt.peer.*;
import java.util.Map;
import java.util.Properties;

import sun.awt.LightweightFrame;
import sun.awt.datatransfer.DataTransferer;
import sun.awt.peer.cacio.CacioLightweightFramePeer;
import sun.awt.peer.cacio.CacioToolkit;
import sun.awt.peer.cacio.PlatformWindowFactory;
import sun.awt.peer.cacio.managed.FullScreenWindowFactory;

public class CTCToolkit extends CacioToolkit {

    private PlatformWindowFactory platformWindowFactory;
    
    static {
        if (System.getProperty("os.name").equals("Linux")) {
            System.load(System.getenv("POJAV_NATIVEDIR")+"/libpojavexec_awt.so");
        }
    }
    
    public CTCToolkit() {
        setDecorateWindows(true);
        // System.setProperty("swing.defaultlaf", "javax.swing.plaf.metal.MetalLookAndFeel");
    }

    public static boolean isHeadless() {
       return false;
    }

    @Override
    public PlatformWindowFactory getPlatformWindowFactory() {
        if (platformWindowFactory == null) {
          CTCScreen screen = CTCScreen.getInstance();
          CTCEventSource eventSource = CTCEventSource.getInstance();
          platformWindowFactory = new FullScreenWindowFactory(screen, eventSource);
        }
        return platformWindowFactory;
    }

    @Override
    public InputMethodDescriptor getInputMethodAdapterDescriptor()
            throws AWTException {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Clipboard getSystemClipboard() {
        return CTCClipboard.INSTANCE;
    }

    @Override
    public DragSourceContextPeer createDragSourceContextPeer(
            DragGestureEvent dge) throws InvalidDnDOperationException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TrayIconPeer createTrayIcon(TrayIcon target)
            throws HeadlessException, AWTException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SystemTrayPeer createSystemTray(SystemTray target) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isTraySupported() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public FontPeer getFontPeer(String name, int style) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RobotPeer createRobot(Robot target, GraphicsDevice screen)
            throws AWTException {
        return new CTCRobotPeer(Boolean.parseBoolean(System.getProperty("net.openjdk.cacio.ctc.CTCToolkit.enableInfdevMouseHandler","true")));
    }

    @Override
    protected int getScreenWidth() {
        return FullScreenWindowFactory.getScreenDimension().width;
    }

    @Override
    protected int getScreenHeight() {
        return FullScreenWindowFactory.getScreenDimension().height;
    }

    @Override
    protected boolean syncNativeQueue(long timeout) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void grab(Window w) {
        // TODO Auto-generated method stub

    }

    @Override
    public void ungrab(Window w) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isDesktopSupported() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    protected DesktopPeer createDesktopPeer(Desktop target)
            throws HeadlessException {
        // TODO Auto-generated method stub
        return new CTCDesktopPeer();
    }

    @Override
    public ColorModel getColorModel() throws HeadlessException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void sync() {
        // TODO Auto-generated method stub

    }

    @Override
    public PrintJob getPrintJob(Frame frame, String jobtitle, Properties props) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void beep() {
       // logger.log(Level.FINE, "BEEP");
    }

    @Override
    public Map<TextAttribute, ?> mapInputMethodHighlight(
            InputMethodHighlight highlight) throws HeadlessException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FramePeer createLightweightFrame(LightweightFrame lightweightFrame) throws HeadlessException {
        return new CacioLightweightFramePeer(lightweightFrame, platformWindowFactory);
    }

    @Override
    public DataTransferer getDataTransferer() {
        // TODO implement for copy paste bridge Android
        return null;
    }
    
    // Cosntant alpha
    @Override
    public boolean isWindowOpacitySupported() {
        return true;
    }

    // Shaping
    @Override
    public boolean isWindowShapingSupported() {
        return true;
    }

    // Per-pixel alpha
    @Override
    public boolean isWindowTranslucencySupported() {
        return true;
    }

    @Override
    public boolean isTranslucencyCapable(GraphicsConfiguration gc) {
        return true;
    }
    
    @Override
     public Cursor createCustomCursor(Image cursor, Point hotSpot, String name) {
         System.out.println("cursor="+cursor+" hotspot="+hotSpot.toString()+" name="+name);
         return new Cursor(Cursor.DEFAULT_CURSOR);
     }
}
