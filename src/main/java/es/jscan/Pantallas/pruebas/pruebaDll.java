package es.jscan.Pantallas.pruebas;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.*;

/**
 *
 * @author julian.collado
 */
public class pruebaDll {

    public interface wsock32 extends Library {

        wsock32 INSTANCE = (wsock32) Native.loadLibrary("wsock32", wsock32.class);

        int gethostname(byte[] nombre, int tam);
    }

    public interface kernel32 extends Library {

        kernel32 INSTANCE = (kernel32) Native.loadLibrary("kernel32", kernel32.class);

        int GetMaximumProcessorCount(int nombre);

        boolean Beep(int freq, int tiempo);

        void GetSystemInfo(SYSTEM_INFO lpSystemInfo);
    }

    public static void main(String[] args) {
        byte[] nombre = new byte[255];
        System.setProperty("jna.library.path", "C:\\Windows\\system32\\wsock32.dll");
        wsock32 lib = wsock32.INSTANCE;
        int x = lib.gethostname(nombre, 255);
        System.out.println("x: " + x);
        System.out.println("Nombre: " + new String(nombre));
        System.setProperty("jna.library.path", "C:\\Windows\\system32\\kernell32.dll");
        System.out.println("CPUs: " + kernel32.INSTANCE.GetMaximumProcessorCount(0));
        System.out.println("Beep: " + kernel32.INSTANCE.Beep(2450, 1000));
//        SYSTEM_INFO lpSystemInfo = new SYSTEM_INFO();
//        kernel32.INSTANCE.GetSystemInfo(lpSystemInfo);
//        System.out.println("Beep: " + kernel32.INSTANCE.Beep(11450, 1000));
    }
}
