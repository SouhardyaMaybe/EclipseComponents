package sun.awt.peer.cacio;

import java.awt.*;
import java.awt.peer.*;
import sun.awt.AWTAccessor;

class GetPeer {
    static ComponentPeer getComponent(Component c) {
        return AWTAccessor.getComponentAccessor().getPeer(c);
    }
    
    static MenuComponentPeer getMenuComponent(MenuComponent c) {
        return AWTAccessor.getMenuComponentAccessor().getPeer(c);
    }
}
