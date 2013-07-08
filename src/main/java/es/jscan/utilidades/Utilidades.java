package es.jscan.utilidades;

import es.jscan.Pantallas.PantallaMensaje;
import es.jscan.Pantallas.PantallaPrincipal;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;
import java.util.zip.ZipInputStream;
import javax.xml.ws.BindingProvider;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author julian
 */
public class Utilidades {

    public String DIGIERROR = "";

    public boolean comprobarFecha(String fecha) {
        if (fecha == null) {
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if (fecha.trim().length() != dateFormat.toPattern().length()) {
            return false;
        }
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(fecha.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    public void crearDirectorio(String dir) {
        if (dir.isEmpty()) {
            return;
        }
        File directorio = new File(dir);
        directorio.mkdirs();
    }

    public String crearDirBase() {
        String SO = so();
        String separador = separador();
        String dirbase = "";
        // dirbase = usuarioHome() + separador + "digita";
        if (!SO.toLowerCase().contains("windows")) {
            dirbase = usuarioHome() + separador + "digita";
        } else {
            if (existeFichero("d:\\")) {
                dirbase = "d:\\usuarios" + separador + usuario() + separador + "digita";
            } else {
                dirbase = usuarioHome() + separador + "digita";
            }
        }
        // Siempre tiene que existir la ruta "digita" en el "home" del usuario
        crearDirectorio(dirbase);
        crearDirectorio(dirbase + separador + "lotes");
        crearDirectorio(dirbase + separador + "pendientes");
        //  crearDirectorio(dirbase + separador + "enviados");
        crearDirectorio(dirbase + separador + "logs");
        return dirbase;

    }

    public boolean borrarDirectorio(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (!file.isDirectory()) {
            return file.delete();
        }
        return this.borrarDirHijo(file) && file.delete();
    }

    public boolean borrarFichero(String directorio, String nombre) {
        File dir = new File(directorio);
        String ficheros[] = dir.list();
        Boolean resultado = true;

        if (nombre.startsWith("*")) {
            nombre = nombre.substring(1, nombre.length());
        }

        for (int i = 0; i < ficheros.length; i++) {
            if (ficheros[i].endsWith(nombre)) {
                File fich = new File(directorio + this.separador() + ficheros[i]);
                resultado = fich.delete();
                if (!resultado) {
                    return resultado;
                }
            }
        }

        return resultado;
    }

    public boolean borrarFichero(String nombre) {
        File fich = new File(nombre);
        return fich.delete();
    }

    public long discoLibre(String ruta) {
        DIGIERROR = "";
        long espacio = 0;
        try {
            File dir = new File(ruta);
            espacio = dir.getFreeSpace();
        } catch (Exception ex) {
            DIGIERROR = ex.getMessage();
        }
        return espacio;
    }

    public static void copiarTextoPortapapeles(String cadena) {
        StringSelection selection = new StringSelection(cadena);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);

    }

    private boolean borrarDirHijo(File dir) {
        File[] children = dir.listFiles();
        boolean childrenDeleted = true;
        for (int i = 0; children != null && i < children.length; i++) {
            File child = children[i];
            if (child.isDirectory()) {
                childrenDeleted = this.borrarDirHijo(child) && childrenDeleted;
            }
            if (child.exists()) {
                childrenDeleted = child.delete() && childrenDeleted;
            }
        }
        return childrenDeleted;
    }

    public String dirBase() {
        String SO = so();
        String separador = "\\";
        if (!SO.toLowerCase().contains("windows")) {
            separador = "/";
        }
        String dirbase = "";
        // dirbase = usuarioHome() + separador + "digita";
        if (!SO.toLowerCase().contains("windows")) {
            dirbase = usuarioHome() + separador + "digita";
        } else {
            if (existeFichero("d:\\")) {
                dirbase = "d:\\usuarios" + separador + usuario() + separador + "digita";
            } else {
                dirbase = usuarioHome() + separador + "digita";
            }
        }

        return dirbase;
    }

    public void crearFichero(String nombrefichero, String tipo) {
        DIGIERROR = "";
        if (!existeFichero(nombrefichero)) {
            try {
                String cabecera = "";
                BufferedWriter bw = new BufferedWriter(new FileWriter(nombrefichero));
                if (tipo.toLowerCase().equals("xml")) {
                    cabecera = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
                }
                bw.write(cabecera);
                bw.close();
            } catch (IOException ioe) {
                DIGIERROR = ioe.getMessage();
            }
        }
    }

    public void escribeFichero(String nombrefichero, String linea) {
        DIGIERROR = "";
        if (existeFichero(nombrefichero)) {
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(nombrefichero, true));
                bw.newLine();
                bw.write(linea);
                bw.close();
            } catch (IOException ioe) {
                DIGIERROR = ioe.getMessage();
            }
        }
    }

    public Boolean existeFichero(String nombrefichero) {
        File fichero = new File(nombrefichero);
        return fichero.exists();
    }

    public Boolean copiarFichero(String origen, String destino) {
        String comando = "";

        if (so().toLowerCase().startsWith("windows")) {
            comando = "cmd /c copy /Y \"" + origen + "\"" + " \"" + destino + "\"";
        } else {
            comando = "cp -f " + origen + " " + destino;
        }
        if (PantallaPrincipal.DEBUG) {
            escribeLog(comando);
        }
        int resp = llamarSO(comando);
        return resp == 0;
    }

    public Boolean renombrarFichero(String origen, String destino) {
        String comando = "";

        if (so().toLowerCase().startsWith("windows")) {
            comando = "ren \"" + origen + "\"" + " \"" + destino + "\"";
        } else {
            comando = "mv " + origen + " " + destino;
        }
        if (PantallaPrincipal.DEBUG) {
            escribeLog(comando);
        }
        int resp = llamarSO(comando);
        return resp == 0;
    }

    public boolean zipArchivos(String archivozip, String carpeta, String extension) {
        int BUFFER = 1024;
        DIGIERROR = "";
        //Declaramos el FileOutputStream para guardar el archivo
        FileOutputStream dest;
        try {
            //Nuestro InputStream
            BufferedInputStream origen = null;
            dest = new FileOutputStream(archivozip);
            //Indicamos que será un archivo ZIP
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            //Indicamos que el archivo tendrá compresión
            out.setMethod(ZipOutputStream.DEFLATED);
            byte data[] = new byte[BUFFER];
            // Creamos la referencia de la carpeta a leer
            File dir = new File(carpeta);
            // Guarda los nombres de los archivos de la carpeta a leer
            String files[] = dir.list();
            File[] listaFicheros = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (new EvaluaExtension().accept(listaFicheros[i], "." + extension) || extension.endsWith("*")) {
                    if (PantallaPrincipal.DEBUG) {
                        escribeLog("Agregando al ZIP: " + files[i]);
                    }
                    //Creamos el objeto a guardar para cada uno de los elementos del listado
                    FileInputStream fi = new FileInputStream(carpeta + separador() + files[i]);
                    origen = new BufferedInputStream(fi, BUFFER);
                    ZipEntry entry = new ZipEntry(files[i]);
                    //Guardamos el objeto en el ZIP
                    out.putNextEntry(entry);
                    int count;
                    //Escribimos el objeto en el ZIP
                    while ((count = origen.read(data, 0, BUFFER)) != -1) {
                        out.write(data, 0, count);
                    }
                    origen.close();
                }
            }
            out.close();
        } catch (Exception ex) {
            escribeLog("Error al crear el fichero ZIP -zipArchivos- '" + archivozip + "'. Error - " + ex.getMessage());
            DIGIERROR = ex.getMessage();
            return false;
        }
        return true;
    }

    public boolean zipArchivo(String archivozip, String nombre) {
        int BUFFER = 1024;
        DIGIERROR = "";
        //Declaramos el FileOutputStream para guardar el archivo
        FileOutputStream dest;
        try {
            //Nuestro InputStream
            BufferedInputStream origen = null;
            dest = new FileOutputStream(archivozip);
            //Indicamos que será un archivo ZIP
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            //Indicamos que el archivo tendrá compresión
            out.setMethod(ZipOutputStream.DEFLATED);
            byte data[] = new byte[BUFFER];
            FileInputStream fi = new FileInputStream(nombre);
            origen = new BufferedInputStream(fi, BUFFER);
            ZipEntry entry = new ZipEntry(nombre.substring(nombre.lastIndexOf(separador()) + 1, nombre.length()));
            //Guardamos el objeto en el ZIP
            out.putNextEntry(entry);
            int count;
            //Escribimos el objeto en el ZIP
            while ((count = origen.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            origen.close();
            out.close();
        } catch (Exception ex) {
            escribeLog("Error al crear el fichero ZIP -zipArchivo- '" + archivozip + "'. Error - " + ex.getMessage());
            DIGIERROR = ex.getMessage();
            return false;
        }
        return true;
    }
    private static final int BUFFER_SIZE = 4096;

    public void unzip(String zipFilePath, String destDirectory) {
        DIGIERROR = "";
        try {
            ZipInputStream zipIn = null;
            File destDir = new File(destDirectory);
            if (!destDir.exists()) {
                destDir.mkdir();
            }
            zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
            ZipEntry entry = zipIn.getNextEntry();
            // iterates over entries in the zip file
            while (entry != null) {
                String filePath = destDirectory + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    // if the entry is a file, extracts it
                    extractFile(zipIn, filePath);
                } else {
                    // if the entry is a directory, make the directory
                    File dir = new File(filePath);
                    dir.mkdir();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            zipIn.close();
        } catch (Exception ex) {
            escribeLog("Error al extraer -unzip- del fichero ZIP " + zipFilePath + " - " + ex.getMessage());
            DIGIERROR = ex.getMessage();
        }
    }

    public void unzip(String zipFilePath, String destDirectory, String fichero) {
        DIGIERROR = "";
        try {
            ZipInputStream zipIn = null;
            File destDir = new File(destDirectory);
            if (!destDir.exists()) {
                destDir.mkdir();
            }
            zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
            ZipEntry entry = zipIn.getNextEntry();
            // iterates over entries in the zip file
            while (entry != null) {
                if (!entry.getName().toLowerCase().equals(fichero.toLowerCase())) {
                    entry = zipIn.getNextEntry();
                } else {
                    String filePath = destDirectory + File.separator + entry.getName();
                    if (!entry.isDirectory()) {
                        // if the entry is a file, extracts it
                        extractFile(zipIn, filePath);
                    } else {
                        // if the entry is a directory, make the directory
                        File dir = new File(filePath);
                        dir.mkdir();
                    }
                    zipIn.closeEntry();
                    break;
                }
            }
            zipIn.close();
        } catch (Exception ex) {
            escribeLog("Error al extraer " + fichero + " -unzip- del fichero ZIP " + zipFilePath + " - " + ex.getMessage());
            DIGIERROR = ex.getMessage();
        }
    }

    private void extractFile(ZipInputStream zipIn, String filePath) {
        DIGIERROR = "";
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(filePath));
            byte[] bytesIn = new byte[BUFFER_SIZE];
            int read = 0;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
            bos.close();
        } catch (IOException ex) {
            escribeLog("Error al extraer -extractFile- ... - " + ex.getMessage());
            DIGIERROR = ex.getMessage();
        }
    }

    public Properties leerPropeties(String archivo) {
        DIGIERROR = "";
        Properties props = null;
        try {
            //Cargamos el archivo 
            FileInputStream ini = new FileInputStream(archivo);
            props = new Properties();
            props.load(ini);
            ini.close();
        } catch (Exception ex) {
            escribeLog("Error al leer el fichero de propiedades " + archivo + " - " + ex.getMessage());
            DIGIERROR = ex.getMessage();
        }
        return props;
    }

    public void escribirPropeties(String archivo, Properties props) {
        DIGIERROR = "";
        try {
            //Cargamos el archivo 
            File fichero = new File(archivo);
            // Properties props = new Properties();
            FileOutputStream ops = new FileOutputStream(fichero, false);
            props.store(ops, "\n");
            ops.close();

        } catch (Exception ex) {
            escribeLog("Error al escribir en el fichero de propiedades " + archivo + " - " + ex.getMessage());
            DIGIERROR = ex.getMessage();
        }

    }

    public String separador() {
        String SO = so();
        String separador = "\\";
        if (!SO.toLowerCase().contains("windows")) {
            separador = "/";
        }
        return separador;
    }

    public String nombrePC() {
        return System.getenv("COMPUTERNAME");
    }

    public String usuario() {
        return System.getProperty("user.name");
    }

    public String procesador() {
        return System.getenv("PROCESSOR_IDENTIFIER");
    }

    public String so() {
        return System.getProperty("os.name");
    }

    public String soBits() {
//        for (Map.Entry<Object, Object> e : System.getProperties().entrySet()) {
//            System.out.println(e);
//        }

        String version = " x86";

        if (System.getenv("ProgramW6432") != null) {
            version = " x64";
        }

        return version;
    }

    public String versionJDK() {
        return System.getProperty("java.version");
    }

    public String usuarioHome() {
        return System.getProperty("user.home");
    }

    public String usuarioDir() {
        return System.getProperty("user.dir");
    }

    public String wsServidor() {
        Utilidades util = new Utilidades();
        Properties prop = util.leerConfiguracion("es/seap/minhap/ws/propiedades/ws-servidor.properties");
        String servidor = prop.getProperty("servidor");
        return servidor;
    }

    public String wsUrl() {
        Utilidades util = new Utilidades();
        Properties prop = util.leerConfiguracion("es/seap/minhap/ws/propiedades/ws-servidor.properties");
        String wsurl = prop.getProperty("wsurl");
        return wsurl;
    }

    public String versionJavaBits() {
        return System.getProperty("os.arch");
    }

    public String ip() {
        String ip = "";
        DIGIERROR = "";
        try {
            InetAddress address = InetAddress.getByName("localhost");

            address = InetAddress.getLocalHost();
            // Coge la dirección ip como un array de bytes
            byte[] bytes = address.getAddress();
            // Convierte los bytes de la dirección ip a valores sin
            // signo y los presenta separados por espacios
            for (int cnt = 0; cnt < bytes.length; cnt++) {
                int uByte = bytes[cnt] < 0 ? bytes[cnt] + 256 : bytes[cnt];
                if (ip.equals("")) {
                    ip = ip + uByte;
                } else {
                    ip = ip + "." + uByte;
                }
            }

            StringTokenizer st = new StringTokenizer(ip, ".");
            ip = "";
            while (st.hasMoreTokens()) {
                String ip3 = st.nextToken();
                int tam = ip3.length();
                for (int i = tam; i < 3; i++) {
                    ip3 = "0" + ip3;
                }
                if (ip.equals("")) {
                    ip = ip3;
                } else {
                    ip = ip + "." + ip3;
                }
            }
        } catch (UnknownHostException ex) {
            ip = "000.000.000.000";
            DIGIERROR = ex.getMessage();
        }
        return ip;
    }

    public void sacarArchivoJar(String Archivo, String RutaDescarga) {
        DIGIERROR = "";
        try {
            InputStream in = getClass().getResourceAsStream(Archivo);
            OutputStream out = new FileOutputStream(RutaDescarga);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            if (PantallaPrincipal.DEBUG) {
                escribeLog("Copiado con exito\n");
            }
        } catch (Exception ex) {
            escribeLog(ex.getMessage());
            DIGIERROR = ex.getMessage();
        }

    }
    // Carga Una DLL, en este caso la coge del directorio "drivers" del "home" del usuario

    static public boolean load(Class cl, String name) {
        String libname = System.mapLibraryName(name);
        Utilidades soutil = new Utilidades();
        String ruta = soutil.dirBase() + soutil.separador() + "drivers" + soutil.separador() + libname;
        if (!soutil.versionJavaBits().equals("x86")) {
            ruta = soutil.dirBase() + soutil.separador() + "drivers" + soutil.separador() + "x64" + soutil.separador() + libname;
        }

        try {
            System.load(new File(ruta).getAbsolutePath());
            if (PantallaPrincipal.DEBUG) {
                escribeLog(Utilidades.class.getName() + ".load: Cargada la librería [" + ruta + "]");
            }
        } catch (Exception ex) {
            escribeLog(Utilidades.class.getName() + ".load:\n\tError: " + ex.getMessage() + " - No se ha cargado la librería [" + ruta + "]");
            return false;
        }
        return true;
    }

    @SuppressWarnings("SuspiciousSystemArraycopy")
    public Object resizeArray(Object oldArray, int newSize) {
        int oldSize = java.lang.reflect.Array.getLength(oldArray);
        Class elementType = oldArray.getClass().getComponentType();
        Object newArray = java.lang.reflect.Array.newInstance(
                elementType, newSize);
        int preserveLength = Math.min(oldSize, newSize);
        if (preserveLength > 0) {
            System.arraycopy(oldArray, 0, newArray, 0, preserveLength);
        }
        return newArray;
    }

    public static int llamarSO(String comando) {
        String s = null;
        try {
            // Ejecutamos el comando
            Process p = Runtime.getRuntime().exec(comando);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(
                    p.getErrorStream()));

            // Leemos la salida del comando
            // escribeLog("Ésta es la salida standard del comando:\n");
            while ((s = stdInput.readLine()) != null) {
                //     escribeLog(s);
            }

            // Leemos los errores si los hubiera
            if (PantallaPrincipal.DEBUG) {
                escribeLog("Ésta es la salida standard de error del comando (si la hay):\n");
                while ((s = stdError.readLine()) != null) {
                    escribeLog(s);
                }
            }
            return 0;
        } catch (IOException e) {
            escribeLog("Excepción en -llamarSO- : " + e.getMessage());
            return -1;
        }
    }


    public Properties leerConfiguracionCorreo() {
        Properties props = new Properties();
        DIGIERROR = "";
        try {
            InputStream in = Conectar.class
                    .getClassLoader().getResourceAsStream("es/seap/minhap/utilidades/propiedades/correo.properties");
            if (in == null) {
                escribeLog("Error al cargar el fichero de propiedades de Correo");
            } else {
                props = new java.util.Properties();
                props.load(in);
            }
        } catch (Exception ex) {
            escribeLog("Error al cargar el fichero de propiedades de Correo. Error: " + ex.getMessage());
            DIGIERROR = ex.getMessage();
        }
        return props;
    }

    public static void escribeLog(String texto) {
        String fecha = today() + " " + now();
        System.out.println(fecha + " - " + texto);
    }

    public static String now() {
        Calendar cal = Calendar.getInstance();
        String hora = String.valueOf(cal.get(Calendar.HOUR_OF_DAY)).length() == 1 ? "0" + String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) : String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        String minuto = String.valueOf(cal.get(Calendar.MINUTE)).length() == 1 ? "0" + String.valueOf(cal.get(Calendar.MINUTE)) : String.valueOf(cal.get(Calendar.MINUTE));
        String segundo = String.valueOf(cal.get(Calendar.SECOND)).length() == 1 ? "0" + String.valueOf(cal.get(Calendar.SECOND)) : String.valueOf(cal.get(Calendar.SECOND));
        String horaactual = hora + ":" + minuto + ":" + segundo;
        return horaactual;
    }

    public static String today() {
        Calendar cal = Calendar.getInstance();
        String anio = String.valueOf(cal.get(Calendar.YEAR));
        String mes = String.valueOf((cal.get(Calendar.MONTH) + 1)).length() == 1 ? "0" + String.valueOf((cal.get(Calendar.MONTH) + 1)) : String.valueOf((cal.get(Calendar.MONTH) + 1));
        String dia = String.valueOf(cal.get(Calendar.DAY_OF_MONTH)).length() == 1 ? "0" + String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) : String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        String fecha = dia + "/" + mes + "/" + anio;
        return fecha;
    }

    public void mensaje(java.awt.Frame ventana, String titulo, String texto) {
        PantallaMensaje mensaje = new PantallaMensaje(ventana, true);
        mensaje.setTitle(titulo);
        mensaje.etiqueta.setOpaque(false);
        mensaje.etiqueta.setBackground(new Color(0, 0, 0, 0));
        mensaje.etiqueta.setText(texto);
        mensaje.setVisible(true);
    }

    public static void mensa(java.awt.Frame ventana, String titulo, String texto) {
        PantallaMensaje mensaje = new PantallaMensaje(ventana, true);
        mensaje.setTitle(titulo);
        mensaje.etiqueta.setOpaque(false);
        mensaje.etiqueta.setBackground(new Color(0, 0, 0, 0));
        mensaje.etiqueta.setText(texto);
        mensaje.setVisible(true);
    }

    public void mensaje(javax.swing.JDialog ventana, String titulo, String texto) {
        PantallaMensaje mensaje = new PantallaMensaje(ventana, true);
        mensaje.setTitle(titulo);
        mensaje.etiqueta.setOpaque(false);
        mensaje.etiqueta.setBackground(new Color(0, 0, 0, 0));
        mensaje.etiqueta.setText(texto);
        mensaje.setVisible(true);
    }


    public static String getMime(byte[] contenido) throws MagicException, MagicParseException, MagicMatchNotFoundException {
        Magic parser = new Magic();
        MagicMatch match = parser.getMagicMatch(contenido);

        return match.getMimeType();
    }

    public static String getMime(String fichero) throws MagicException, MagicParseException, MagicMatchNotFoundException, IOException {
        byte[] contenido = getBytesFromFileName(fichero);
        Magic parser = new Magic();
        MagicMatch match = parser.getMagicMatch(contenido);

        return match.getMimeType();
    }

    public static byte[] getBytesFromFileName(String fichero) throws IOException {
        File file = new File(fichero);
        InputStream is = new FileInputStream(file);
        long length = file.length();

        if (length > Integer.MAX_VALUE) {
        }

        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }

    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        long length = file.length();

        if (length > Integer.MAX_VALUE) {
        }

        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }

    public Properties leerConfiguracion(String ruta) {
        Properties props = new Properties();

        DIGIERROR = "";
        try {
            InputStream in = Conectar.class
                    .getClassLoader().getResourceAsStream(ruta);
            if (in == null) {
                escribeLog("Error al cargar el fichero de propiedades: " + ruta);
            } else {
                props = new java.util.Properties();
                props.load(in);
            }
        } catch (Exception ex) {
            escribeLog("Error al cargar el fichero de propiedades " + ruta + ". Error: " + ex.getMessage());
            DIGIERROR = ex.getMessage();
        }
        return props;
    }


    public Boolean validarNombreLote(String fichero) {
        Boolean resultado = false;

        if (fichero == null) {
            return resultado;
        }
        if (fichero.length() != 32) {
            return resultado;
        }
        if (!fichero.contains("-")) {
            return resultado;
        }
        Utilidades util = new Utilidades();
        String ip = util.ip();
        ip = ip.replace(".", "");
        String nombre = fichero.substring(1, 28);
        if (!nombre.contains(ip)) {
            return resultado;
        }

        resultado = true;
        return resultado;
    }

    public String codificaBase64(String cadena) {
        String resultado = "";
        DIGIERROR = "";
        try {
            byte[] codificado = Base64.encodeBase64(cadena.getBytes());
            resultado = new String(codificado);
        } catch (Exception ex) {
            escribeLog("Error al codificar en Base64 '" + cadena + "' - " + ex.getMessage());
            DIGIERROR = ex.getMessage();
        }
        return resultado;
    }

    public String decodificaBase64(String cadena) {
        String resultado = "";
        DIGIERROR = "";
        try {
            byte[] decodificado = Base64.decodeBase64(cadena);
            resultado = new String(decodificado);
        } catch (Exception ex) {
            escribeLog("Error al decodificar de Base64 '" + cadena + "' - " + ex.getMessage());
            DIGIERROR = ex.getMessage();
        }
        return resultado;
    }

    public ArrayList<String> listaTextoSinDuplicados(ArrayList<String> lista) {
        ArrayList<String> listasin = new ArrayList<String>(new HashSet<String>(lista));
        Collections.sort(listasin);
        return listasin;
    }

    public String hexToASCII(String hex) {
        if (hex.length() % 2 != 0) {
            escribeLog("La cadena tiene que tener un número par de caracteres");
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hex.length() - 1; i += 2) {
            String output = hex.substring(i, (i + 2));
            int decimal = Integer.parseInt(output, 16);
            sb.append((char) decimal);
        }
        return sb.toString();
    }

    public String asciiToHex(String ascii) {
        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < ascii.length(); i++) {
            hex.append(Integer.toHexString(ascii.charAt(i)));
        }
        return hex.toString();
    }
}

class EvaluaExtension implements FilenameFilter {

    @Override
    public boolean accept(File dir, String extension) {
        return dir.getName().endsWith(extension);
    }
}
