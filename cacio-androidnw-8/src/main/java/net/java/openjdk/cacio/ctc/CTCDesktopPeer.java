package net.java.openjdk.cacio.ctc;

import java.awt.peer.DesktopPeer;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.awt.Desktop.Action;

public class CTCDesktopPeer implements DesktopPeer {
   public boolean isSupported(Action action) {
     switch(action) {
       case OPEN:
       case BROWSE:
         return true;
       default:
         return false;
     }
  }
  private static native void openFile(String path);
  private static native void openUri(String uri);
   
  public void open(File file) throws IOException {
    openFile(file.getAbsolutePath());
  }
  public void edit(File file) throws IOException {
    throw new IOException("Action not supported");
  }
  public void print(File file) throws IOException {
    throw new IOException("Action not supported");
  }
  public void mail(URI mailtoURL) throws IOException {
    throw new IOException("Action not supported");
  }
  public void browse(URI uri) throws IOException {
    openUri(uri.toString());
  }
}
