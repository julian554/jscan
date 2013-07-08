package uk.co.mmscomputing.device.twain;

import es.jscan.utilidades.Utilidades;
import uk.co.mmscomputing.util.JarLib;

public class TwainDefaultNativeLoadStrategy implements TwainINativeLoadStrategy {

    public boolean load(Class cl, String libname) {
        //  win : load library 'jtwain.dll'
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            return Utilidades.load(jtwain.class, "jtwain");
        } else {
            return JarLib.load(jtwain.class, "jtwain");
        }
    }
}
