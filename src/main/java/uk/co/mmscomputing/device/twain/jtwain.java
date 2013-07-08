package uk.co.mmscomputing.device.twain;

import es.jscan.Pantallas.PantallaPrincipal;
import es.jscan.utilidades.Utilidades;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Vector;
import uk.co.mmscomputing.concurrent.Semaphore;
import uk.co.mmscomputing.concurrent.TimeUnit;

public class jtwain implements TwainConstants {
    
    static private boolean installed;
    static private Thread nativeThread;                         // make sure to call DSM_ENTRY only from this native thread!
    static private TwainSourceManager sourceManager;                        // applet: this object will live as long as browser window is open
    static private WeakReference scanner;                              // applet: set to new object if applet reloads

    static private native int ninitLib();                           // initialize native library

    static private native void nstart();                             // run native event loop

// java -> native
    static private native void ntrigger(Object caller, int cmd);           // call cbexecute from native thread 

// TwainSourceManager -> native
    static private native int ncallSourceManager(int dg, int dat, int msg, byte[] data) throws TwainException;

// TwainSource -> native
    static native byte[] ngetContainer(int type, int handle);       // copy container pointed to by 'handle' into byte buffer

    static native int nsetContainer(int type, byte[] container); // copy byte buffer into native memory buffer return handle

    static native void nfree(int handle);                         // free native memory

    static private native int ncallSource(byte[] source, int dg, int dat, int msg, byte[] data) throws TwainException;

// for twain native transfer mode
    static private native Object ntransferImage(int imagePtr);              // returns BufferedImage object

// for twain memory transfer mode
    static native void nnew(byte[] twImageMemXfer, int size);      // allocate native memory block

    static native int ncopy(byte[] buf, byte[] twImageMemXfer, int size); // copy native memory block into java byte array
//  static         native byte[] ncopy(byte[] twImageMemXfer,int size);     // copy native memory block into java byte array

    static native void ndelete(byte[] twImageMemXfer);            // free native memory block

    static public boolean isInstalled() {
        return installed;
    }
    
    static public int getINT16(byte[] buf, int off) {
        return (buf[off++] & 0x00FF) | (buf[off] << 8);
    }
    
    static public void setINT16(byte[] buf, int off, int i) {
        buf[off++] = (byte) i;
        buf[off++] = (byte) (i >> 8);
    }
    
    static public int getINT32(byte[] buf, int off) {
        return (buf[off++] & 0x00FF) | ((buf[off++] & 0x00FF) << 8) | ((buf[off++] & 0x00FF) << 16) | (buf[off] << 24);
    }
    
    static public void setINT32(byte[] buf, int off, int i) {
        buf[off++] = (byte) i;
        buf[off++] = (byte) (i >> 8);
        buf[off++] = (byte) (i >> 16);
        buf[off++] = (byte) (i >> 24);
    }
    
    static double getFIX32(byte[] buf, int off) {
        int whole = ((buf[off++] & 0x00FF) | (buf[off++] << 8));
        int frac = ((buf[off++] & 0x00FF) | ((buf[off] & 0x00FF) << 8));
        return ((double) whole) + ((double) frac) / 65536.0;            // [1] 4-76
    }
    
    static void setFIX32(byte[] buf, int off, double d) {
        int value = (int) (d * 65536.0 + ((d < 0) ? (-0.5) : 0.5));           // [1] 4-78 + fix for negative numbers
        setINT16(buf, off, value >> 16);                            // whole
        setINT16(buf, off + 2, value & 0x0000FFFF);                     // frac
    }
    
    static void setString(byte[] buf, int off, String s) {
        System.arraycopy(s.getBytes(), 0, buf, 0, s.length());
    }
    
    static int callSourceManager(int dg, int dat, int msg, byte[] data) throws TwainIOException {
        if (Thread.currentThread() != nativeThread) {
            throw new TwainIOException("Call twain source manager only from within native thread.");
        }
        try {
            return ncallSourceManager(dg, dat, msg, data);
        } catch (TwainException te) {                                // disaster: source exception in native code
            installed = false;
            throw te;                               // disable jtwain, no calls to twain anymore                                                               
        }
    }
    
    static int callSource(byte[] source, int dg, int dat, int msg, byte[] data) throws TwainIOException {
        if (Thread.currentThread() != nativeThread) {
            throw new TwainIOException("Call twain source only from within native thread.");
        }
        
        try {
            return ncallSource(source, dg, dat, msg, data);
        } catch (TwainException tioe) {                              // disaster: source exception in native code
            installed = false;
            throw tioe;                             // disable jtwain, no calls to twain anymore                                                               
        }
    }
    
    static public TwainSourceManager getSourceManager() {
        try {                                                      // wait until native thread is up and running
            while ((sourceManager == null) && installed) {
                Thread.currentThread().sleep(20);
            }
        } catch (Exception e) {
        }
        if (!installed) {
            return null;
        }
        return sourceManager;
    }
    
    static public TwainSource getSource() {
        try {                                                      // wait until native thread is up and running
            while ((sourceManager == null) && installed) {
                Thread.currentThread().sleep(20);
            }
        } catch (Exception e) {
        }
        if (!installed) {
            return null;
        }
        return sourceManager.getSource();
    }
    
    static private void trigger(Object caller, int cmd) {
        try {                                                      // wait until native thread is up and running
            while ((sourceManager == null) && installed) {
                Thread.currentThread().sleep(20);
            }
            if (installed) {
                ntrigger(caller, cmd);
            }
        } catch (Exception e) {
        }
    }
    
    static public void callSetSource(Object caller) {
        trigger(caller, 0);
    } // caller.setSource(sourceManager.getSource());

    static public void setScanner(TwainScanner s) {             // use of weak reference to TwainScanner object 
        scanner = new WeakReference(s);                             // allows garbage collection in applets
    }
    
    static private TwainScanner getScanner() {
        return (TwainScanner) scanner.get();
    }
    
    static public void select(TwainScanner sc) throws TwainIOException {
        setScanner(sc);
        TwainSourceManager sm = getSourceManager();                 // jtwain might not be up and running yet
        sm.getSource().checkState(3);
        trigger(sc, 1);
    }
    
    static public void getIdentities(TwainScanner sc, Vector list) throws TwainIOException {
        setScanner(sc);
        TwainSourceManager sm = getSourceManager();                 // jtwain might not be up and running yet
        sm.getSource().checkState(3);                             // System.err.println("select: try "+name);
        Semaphore s = new Semaphore(0, true);
        list.add(s);                                              // misuse here list as transport container for semaphore
        trigger(list, 2);
        try {
            s.tryAcquire(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ie) {
            throw new TwainIOException(jtwain.class.getName() + ".getIdentities\n\tCould not retrieve device names. Request timed out.");
        }
    }
    
    static public void select(TwainScanner sc, String name) throws TwainIOException {
        setScanner(sc);
        TwainSourceManager sm = getSourceManager();                 // jtwain might not be up and running yet
        sm.getSource().checkState(3);                             // System.err.println("select: try "+name);
        trigger(name, 3);
    }
    
    static public void acquire(TwainScanner sc) throws TwainIOException {
        setScanner(sc);
        TwainSourceManager sm = getSourceManager();                 // jtwain might not be up and running yet
        sm.getSource().checkState(3);
        trigger(sc, 4);
    }
    
    static public void setCancel(TwainScanner sc, boolean c) throws TwainIOException {
        getSourceManager().getSource().setCancel(c);
    }
    
    static private Method getMethod(Class clazz, String name, Class[] paramTypes) throws NoSuchMethodException {
        if (clazz == null) {
            throw new NoSuchMethodException();
        }
        try {
            return clazz.getDeclaredMethod(name, paramTypes);
        } catch (NoSuchMethodException nsme) {
            return getMethod(clazz.getSuperclass(), name, paramTypes);
        }
    }
    
    static private void cbexecute(Object obj, int cmd) {     // callback function cpp -> java; in windows thread;
        TwainSource source;
        try {
            switch (cmd) {
                case 0:                                            // an object wants access to current twain source
                    Class clazz = obj.getClass();
                    try {                                             // introspection
                        source = sourceManager.getSource();
                        Method method = getMethod(clazz, "setSource", new Class[]{TwainSource.class});
                        method.invoke(obj, new Object[]{source});       // caller.setSource(source);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }        // cannot find method: programming error
                    break;
                case 1:                                            // select
                    sourceManager.selectSource();                    // System.out.println(sourceManager.getSource());
                    break;
                case 2:                                            // getIdentities
                    java.util.Vector list = (java.util.Vector) obj;
                    Semaphore s = (Semaphore) list.get(0);
                    list.remove(0);
                    sourceManager.getIdentities(list);
                    s.release();
                    break;
                case 3:                                            // select name
                    String name = (String) obj;                       // System.out.println("cbexecute: try "+name);
                    sourceManager.selectSource(name);                // System.err.println(sourceManager.getSource());
                    break;
                case 4:                                            // acquire
                    source = sourceManager.openSource();
                    try {
                        source.enable();
                    } finally {
                        source.close();
                    }
                    break;
            }
        } catch (Throwable e) {
            Utilidades util = new Utilidades();
            util.DIGIERROR = e.getMessage();
            if (PantallaPrincipal.DEBUG) {
                Utilidades.escribeLog("Error al cargar el escaner: " + util.DIGIERROR);
            }
//            signalException(e.getMessage());
//            e.printStackTrace();
        }
    }
    
    static private int cbhandleGetMessage(int msg) {        // callback function cpp -> java; windows thread;
        try {
            return sourceManager.getSource().handleGetMessage(msg);
        } catch (Throwable e) {
            signalException(e.getMessage());
            e.printStackTrace();
            return TWRC_NOTDSEVENT;
        }
    }
    
    static void signalStateChange(TwainSource source) {
        TwainScanner scanner = getScanner();
        if (scanner != null) {
            scanner.setState(source);
        }
    }
    
    static void signalException(String msg) {
        TwainScanner scanner = getScanner();
        if (scanner != null) {
            scanner.signalException(msg);
        }
    }
    
    static void negotiateCapabilities(TwainSource source) {
        TwainScanner scanner = getScanner();
        if (scanner != null) {
            scanner.negotiateCapabilities(source);
        }
    }
    
    static void transferNativeImage(int dibHandle) {
        BufferedImage image = (BufferedImage) ntransferImage(dibHandle);
        if (image != null) {
            TwainScanner scanner = getScanner();
            if (scanner != null) {
                scanner.setImage(image);
            }
        }
    }
    
    static void transferFileImage(File file) {
        if (file != null) {
            TwainScanner scanner = getScanner();
            if (scanner != null) {
                scanner.setImage(file);
            }
        }
    }
    
    static void transferMemoryBuffer(TwainTransfer.MemoryTransfer.Info info) {
        TwainScanner scanner = getScanner();
        if (scanner != null) {
            scanner.setImageBuffer(info);
        }
    }
    
    static {
//  win : load library 'jtwain.dll'
//    installed = JarLib.load(jtwain.class,"jtwain");  
        installed = TwainNativeLoadStrategySingleton.getInstance().getNativeLoadStrategy().load(jtwain.class, "jtwain");
        if (installed) {
//      javax.imageio.ImageIO.scanForPlugins();
            nativeThread = new Thread() {
                public void run() {
                    try {
                        int hwnd = ninitLib();
                        if (hwnd != 0) {
                            sourceManager = new TwainSourceManager(hwnd);
                            nstart();
                        }
                    } catch (Throwable e) {
                        System.err.println("uk.co.mmscomputing.device.twain.jtwain\t\n" + e.getMessage());
                        System.err.println(e);
                        System.out.println("Error al cargar la jtwain.dll " + e.getMessage());
                      //  e.printStackTrace();
                    }
                    installed = false;
                }
            };
            nativeThread.start();
        }
    }
}

// [1] Twain Spec 1.9
