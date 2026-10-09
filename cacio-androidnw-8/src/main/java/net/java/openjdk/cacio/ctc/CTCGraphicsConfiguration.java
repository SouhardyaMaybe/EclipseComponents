package net.java.openjdk.cacio.ctc;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.*;

import sun.awt.peer.cacio.managed.FullScreenWindowFactory;

public class CTCGraphicsConfiguration extends GraphicsConfiguration {

    private CTCGraphicsDevice device;

    CTCGraphicsConfiguration(CTCGraphicsDevice dev) {
        device = dev;
    }

    @Override
    public GraphicsDevice getDevice() {
        return device;
    }

    @Override
    public ColorModel getColorModel() {
        return ColorModel.getRGBdefault();
    }

    @Override
    public ColorModel getColorModel(int transparency) {
        switch (transparency) {
            case Transparency.OPAQUE:
                return new DirectColorModel(24, 0xff0000, 0xff00, 0xff);
            case Transparency.BITMASK:
                return new DirectColorModel(25, 0xff0000, 0xff00, 0xff, 0x1000000);
            case Transparency.TRANSLUCENT:
                return ColorModel.getRGBdefault();
            default:
                return null;
        }
    }

    @Override
    public AffineTransform getDefaultTransform() {
        // TODO Auto-generated method stub
        return new AffineTransform();
    }

    @Override
    public AffineTransform getNormalizingTransform() {
        return new AffineTransform();
    }

    @Override
    public Rectangle getBounds() {
        Dimension d = FullScreenWindowFactory.getScreenDimension();
        return new Rectangle(0, 0, d.width, d.height);
    }

    @Override
    public final boolean isTranslucencyCapable() {
        //we know for sure we have capable config :)
        return true;
    }
}
