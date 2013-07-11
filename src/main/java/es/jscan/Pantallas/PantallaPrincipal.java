package es.jscan.Pantallas;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.ws.Action;
import org.apache.pdfbox.pdmodel.PDPage;
import org.imgscalr.Scalr;
import uk.co.mmscomputing.device.scanner.Scanner;
import uk.co.mmscomputing.device.scanner.ScannerIOException;
import uk.co.mmscomputing.device.scanner.ScannerIOMetadata;
import uk.co.mmscomputing.device.scanner.ScannerListener;
import uk.co.mmscomputing.device.twain.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.codec.TiffImage;
import com.thoughtworks.xstream.XStream;
import es.jscan.Beans.ProcesosBean;
import es.jscan.Beans.XmlDocumento;
import es.jscan.Beans.XmlInfo;
import es.jscan.Beans.XmlLote;
import es.jscan.utilidades.CodigoBarras;
import es.jscan.utilidades.ConexionUnica;
import es.jscan.utilidades.EscuchadorMensajes;
import es.jscan.utilidades.Utilidades;
import es.jscan.utilidades.UtilidadesPantalla;
import es.jscan.Beans.DigitaLotesBean;
import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.net.Socket;
import java.net.URI;
import javax.imageio.IIOImage;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.filechooser.FileFilter;

/**
 * @author julian.collado
 */
public class PantallaPrincipal extends javax.swing.JFrame implements PropertyChangeListener, ScannerListener {

    public Robot robot;
    public int SESION_LOG = 0;
    public static PantallaPrincipal ventanapadre = null;
    public ProcesosBean procesosbean = null;
    public Utilidades utilidades = new Utilidades();
    public String rutadigita = utilidades.crearDirBase();
    public String separador = utilidades.separador();
    public String lote = "";
    public String rutalote = "";
    public String rutapendiente = "";
    public String rutalog = utilidades.crearDirBase() + separador + "logs";
    public Boolean errorpdf = false;
    public Boolean errortiff = false;
    private Scanner scanner;
    public String[] listaseleccion = null;
    public String miescaner = "";
    public String titulolista = "";
    public UtilidadesPantalla pantutil = new UtilidadesPantalla();
    public int contimagen = 0;
    public PanelDibujo pd;
    private Properties propiniescaner = null;
    private Properties proplote = null;
    public JButton[] boton = new JButton[1];
    public String[] rutaboton = new String[1];
    public boolean desdemin = false;
    public int contpaginas = 0;
    public int maxpaginas = 32765;
    public String titulo = this.getTitle();
    public Boolean escaneando = false;
    public int minisel = -1;
    public int provinciausuario = 0;
    private Boolean arranque = true;
    File ficherolog = null;
    FileOutputStream fis;
    PrintStream out;
    ArrayList<JLabel> arrayLabel = new ArrayList<JLabel>();
    ArrayList<JTextField> arrayCampoTexto = new ArrayList<JTextField>();
    ArrayList<JCheckBox> arrayCheck = new ArrayList<JCheckBox>();
    ArrayList<JComboBox> arrayComboBox = new ArrayList<JComboBox>();
    ArrayList<String> listaMultiAdjuntos = new ArrayList<String>();
    Boolean multiAdjuntos = false;
    TwainCapability[] escanercap = null;
    TwainCapability contraste = null;
    TwainCapability brillo = null;
    TwainCapability resolucion = null;
    boolean escanerconfig = false;
    public static final String COMPRESSION_TYPE_GROUP4FAX = "CCITT T.6";
    public Boolean revisar = false;
    public Boolean enviar = false;
    public static int CONTCONEX = 0;
    public static Boolean DEBUG = false;
    public static Boolean MOSTRARMENSAJE = true;
    public javax.swing.Timer timer = new javax.swing.Timer(2000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String memoria = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 + " KB / " + Runtime.getRuntime().totalMemory() / 1024 + " KB ";
            setTitle(titulo + "     -     Memoria utilizada: " + memoria);
        }
    });

    /**
     * Módulo principal de la aplicación PantallaPrincipal
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public PantallaPrincipal() {

        ventanapadre = this;

        Calendar cal = Calendar.getInstance();
        String anio = String.valueOf(cal.get(Calendar.YEAR));
        String mes = String.valueOf((cal.get(Calendar.MONTH) + 1)).length() == 1 ? "0" + String.valueOf((cal.get(Calendar.MONTH) + 1)) : String.valueOf((cal.get(Calendar.MONTH) + 1));
        String dia = String.valueOf(cal.get(Calendar.DAY_OF_MONTH)).length() == 1 ? "0" + String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) : String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        ficherolog = new File(rutalog + separador + "jscan-" + utilidades.ip() + "-" + dia + "-" + mes + "-" + anio + ".log");

        try {
            fis = new FileOutputStream(ficherolog, true);
            out = new PrintStream(fis);
            System.setOut(out);
        } catch (FileNotFoundException ex) {
            Utilidades.escribeLog(PantallaPrincipal.class.getName().toString() + " - " + ex.getMessage());
        }


        System.out.println();
        Utilidades.escribeLog("INICIANDO jScan\n\n");

        try {
            this.robot = new Robot();
        } catch (AWTException ex) {
            Utilidades.escribeLog("Error al crear 'robot' para PantallaPrincipal - Error " + ex.getMessage());
        }

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Utilidades.escribeLog("Resolución de pantalla: " + screenSize.width + " x " + screenSize.height);

        if (screenSize.width < 1023) {
            utilidades.mensaje(this, "Resolución de Pantalla no válida", "\nLa resolución mínima requerida de la pantalla debe ser 1024 x 768\n\nLa resolución de su pantalla es "
                    + screenSize.width + " x " + screenSize.height);
            System.exit(0);
        }

        String so = "";
        if (utilidades.so().toLowerCase().contains("windows")) {
            so = utilidades.so() + " " + utilidades.soBits();
        } else {
            so = utilidades.so();
        }

        initComponents();
        // Opciones del menú "Apariencia" , por defecto se usa el Nimbus como tema visual
        opcionRBMetal.setSelected(false);
        opcionRBNimbus.setSelected(true);
        opcionRBWindows.setSelected(false);
        opcionRBWindowsClassic.setSelected(false);
        opcionRBPorDefecto.setSelected(false);
        opcionDebug.setSelected(false);
        opcionLeerLog.setText("Ver fichero de log del " + Utilidades.today());

        try {
            setIconImage(new ImageIcon(getLogo()).getImage());
        } catch (NullPointerException e) {
            Utilidades.escribeLog("\nError cargando el Logo " + e.getMessage() + "\n");
        }

        Utilidades.escribeLog("Versión de java: " + utilidades.versionJavaBits());

//        if (!utilidades.versionJavaBits().equals("x86") && utilidades.so().toLowerCase().contains("windows")) {
//            utilidades.mensaje(this, "Error de versión de runtime de Java", "La versión de runtime de Java debe ser de 32 Bits");
//            System.exit(0);
//        }

        Utilidades.escribeLog("Espacio disponible en disco: " + (utilidades.discoLibre(rutadigita) / 1024 / 1024) + " MB");
        // Espacio mínimo en disco 1GB (1073741824 bytes)
        if (utilidades.discoLibre(rutadigita) < (1073741824l)) {
            utilidades.mensaje(this, "Espacio insuficiente en disco", "  Espacio insuficiente en el directorio de trabajo:\n  "
                    + rutadigita + "\n\n  Espacio disponible: " + (utilidades.discoLibre(rutadigita) / 1024 / 1024) + " MB\n"
                    + "  Espacio mínimo necesario: " + (1073741824l) / 1024 / 1024 + " MB");
            System.exit(0);
        }

        setLocationRelativeTo(null);

        try {
            InputStream in = Acercade.class.getClassLoader().getResourceAsStream("es/jscan/Pantallas/propiedades/version.txt");
            if (in == null && DEBUG) {
                Utilidades.escribeLog("No existe el fichero 'version.txt'");
            }
            Properties pro = new java.util.Properties();
            pro.load(in);

            this.setTitle(pro.getProperty("nombre") + " " + pro.getProperty("version"));
        } catch (Exception ex) {
            Utilidades.escribeLog("Error al cargar el fichero version.txt. Error: " + ex.getMessage());
        }

        titulo = getTitle();
        timer.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollImagenes = new javax.swing.JScrollPane();
        panelImagenes = new javax.swing.JPanel();
        panelVisorImagen = new javax.swing.JScrollPane();
        panelImagen = new javax.swing.JPanel();
        panelIconosImagen = new javax.swing.JPanel();
        zoomMas = new javax.swing.JButton();
        zoomMenos = new javax.swing.JButton();
        zoom100 = new javax.swing.JButton();
        ajustarAncho = new javax.swing.JButton();
        rotar = new javax.swing.JButton();
        botonCB = new javax.swing.JButton();
        guardarImagen = new javax.swing.JButton();
        borrarImagen = new javax.swing.JButton();
        botonCB2D = new javax.swing.JButton();
        botonCrearBidi = new javax.swing.JButton();
        panelVisorMini = new javax.swing.JScrollPane();
        panelMini = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        labelPagina = new javax.swing.JLabel();
        labelde = new javax.swing.JLabel();
        labelTotalPaginas = new javax.swing.JLabel();
        labelNumPaginas = new javax.swing.JLabel();
        scrollConfigura = new javax.swing.JScrollPane();
        panelConfigura = new javax.swing.JPanel();
        panelAccion = new javax.swing.JLayeredPane();
        botonDigitalizar = new javax.swing.JButton();
        botonImportar = new javax.swing.JButton();
        panelEscaner = new javax.swing.JLayeredPane();
        botonSelEscaner = new javax.swing.JButton();
        comboColor = new javax.swing.JComboBox();
        comboResolucion = new javax.swing.JComboBox();
        checkDuplex = new javax.swing.JCheckBox();
        nombreEscaner = new javax.swing.JTextField();
        sliderBrillo = new javax.swing.JSlider();
        labelContraste = new javax.swing.JLabel();
        sliderContraste = new javax.swing.JSlider();
        labelBrillo = new javax.swing.JLabel();
        labelBrillo1 = new javax.swing.JLabel();
        labelBrillo2 = new javax.swing.JLabel();
        botonEnviar = new javax.swing.JButton();
        botonGuardar = new javax.swing.JButton();
        botonEliminar = new javax.swing.JButton();
        panelValores = new javax.swing.JLayeredPane();
        jLabel1 = new javax.swing.JLabel();
        textoFichero = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        textoDirectorio = new javax.swing.JTextField();
        botonDirectorio = new javax.swing.JToggleButton();
        comboTipoFichero = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        etiquetaCB = new javax.swing.JTextArea();
        jMenuPrincipal = new javax.swing.JMenuBar();
        opcionOpciones = new javax.swing.JMenu();
        opcionDigitalizar = new javax.swing.JMenuItem();
        opcionImportar = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        opcionSelEscaner = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        opcionCerrar = new javax.swing.JMenuItem();
        Lotes = new javax.swing.JMenu();
        opcionEnviarLote = new javax.swing.JMenuItem();
        opcionGuardarLote = new javax.swing.JMenuItem();
        opcionBorrarLote = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        opcionLotes = new javax.swing.JMenuItem();
        Imagen = new javax.swing.JMenu();
        opcionzoommas = new javax.swing.JMenuItem();
        opcionzoommenos = new javax.swing.JMenuItem();
        opcionzoom100 = new javax.swing.JMenuItem();
        opcionancho = new javax.swing.JMenuItem();
        opcionrotar = new javax.swing.JMenuItem();
        opcionguardarimagen = new javax.swing.JMenuItem();
        opcionborrarimagen = new javax.swing.JMenuItem();
        opcionUtilidades = new javax.swing.JMenu();
        opcionLeerLog = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        opcionDebug = new javax.swing.JCheckBoxMenuItem();
        opcionApariencia = new javax.swing.JMenu();
        opcionRBMetal = new javax.swing.JRadioButtonMenuItem();
        opcionRBNimbus = new javax.swing.JRadioButtonMenuItem();
        opcionRBWindows = new javax.swing.JRadioButtonMenuItem();
        opcionRBWindowsClassic = new javax.swing.JRadioButtonMenuItem();
        opcionRBPorDefecto = new javax.swing.JRadioButtonMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        opcionMaximizar = new javax.swing.JCheckBoxMenuItem();
        opcionAcerca = new javax.swing.JMenu();
        opcionManual = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        opcionLicenciaApache = new javax.swing.JMenuItem();
        opcionLicenciaItext = new javax.swing.JMenuItem();
        opcionLicenciaXStream = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        opcionAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Digitalización v1.0 100");
        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(1024, 768));
        setMinimumSize(new java.awt.Dimension(1024, 768));
        setName("JFramePrincipal"); // NOI18N
        setPreferredSize(new java.awt.Dimension(1024, 768));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        scrollImagenes.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollImagenes.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        panelImagenes.setAutoscrolls(true);
        panelImagenes.setPreferredSize(new java.awt.Dimension(700, scrollImagenes.getHeight()-21));

        panelVisorImagen.setBackground(new java.awt.Color(255, 255, 255));
        panelVisorImagen.setAutoscrolls(true);
        panelVisorImagen.setName("VisorImagen"); // NOI18N
        panelVisorImagen.setOpaque(false);
        panelVisorImagen.setPreferredSize(new java.awt.Dimension(panelIconosImagen.getWidth(), panelImagenes.getHeight()-panelIconosImagen.getHeight()-25 ));

        panelImagen.setBackground(new java.awt.Color(255, 255, 255));
        panelImagen.setAutoscrolls(true);
        panelImagen.setPreferredSize(new java.awt.Dimension(panelVisorImagen.getWidth(), panelVisorImagen.getHeight()-20));

        javax.swing.GroupLayout panelImagenLayout = new javax.swing.GroupLayout(panelImagen);
        panelImagen.setLayout(panelImagenLayout);
        panelImagenLayout.setHorizontalGroup(
            panelImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 537, Short.MAX_VALUE)
        );
        panelImagenLayout.setVerticalGroup(
            panelImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 675, Short.MAX_VALUE)
        );

        panelVisorImagen.setViewportView(panelImagen);

        panelIconosImagen.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelIconosImagen.setMaximumSize(new java.awt.Dimension(4, 32767));
        panelIconosImagen.setMinimumSize(new java.awt.Dimension(4, 51));
        panelIconosImagen.setPreferredSize(new java.awt.Dimension(panelVisorImagen.getWidth(), 50));

        zoomMas.setToolTipText("Aumenta el tamaño de la Imagen");
        zoomMas.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        zoomMas.setMaximumSize(new java.awt.Dimension(43, 43));
        zoomMas.setMinimumSize(new java.awt.Dimension(43, 43));
        zoomMas.setPreferredSize(new java.awt.Dimension(43, 43));
        zoomMas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomMasActionPerformed(evt);
            }
        });

        zoomMenos.setToolTipText("Reduce el tamaño de la Imagen");
        zoomMenos.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        zoomMenos.setMaximumSize(new java.awt.Dimension(43, 43));
        zoomMenos.setMinimumSize(new java.awt.Dimension(43, 43));
        zoomMenos.setPreferredSize(new java.awt.Dimension(43, 43));
        zoomMenos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomMenosActionPerformed(evt);
            }
        });

        zoom100.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        zoom100.setToolTipText("Imagen al tamaño real (100 %)");
        zoom100.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        zoom100.setMaximumSize(new java.awt.Dimension(43, 43));
        zoom100.setMinimumSize(new java.awt.Dimension(43, 43));
        zoom100.setPreferredSize(new java.awt.Dimension(43, 43));
        zoom100.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoom100ActionPerformed(evt);
            }
        });

        ajustarAncho.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        ajustarAncho.setToolTipText("Imagen ajustada al ancho de la Página");
        ajustarAncho.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ajustarAncho.setMaximumSize(new java.awt.Dimension(43, 43));
        ajustarAncho.setMinimumSize(new java.awt.Dimension(43, 43));
        ajustarAncho.setPreferredSize(new java.awt.Dimension(43, 43));
        ajustarAncho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ajustarAnchoActionPerformed(evt);
            }
        });

        rotar.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        rotar.setToolTipText("Rotar imágen 90º a la derecha");
        rotar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rotar.setMaximumSize(new java.awt.Dimension(43, 43));
        rotar.setMinimumSize(new java.awt.Dimension(43, 43));
        rotar.setPreferredSize(new java.awt.Dimension(43, 43));
        rotar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rotarActionPerformed(evt);
            }
        });

        botonCB.setToolTipText("Detecta el código de barras CODE128 de la imágen");
        botonCB.setBorder(null);
        botonCB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonCB.setMaximumSize(new java.awt.Dimension(43, 43));
        botonCB.setMinimumSize(new java.awt.Dimension(43, 43));
        botonCB.setPreferredSize(new java.awt.Dimension(43, 43));
        botonCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCBActionPerformed(evt);
            }
        });

        guardarImagen.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        guardarImagen.setToolTipText("Posibilita guardar la imágen después de rotarla");
        guardarImagen.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        guardarImagen.setMaximumSize(new java.awt.Dimension(43, 43));
        guardarImagen.setMinimumSize(new java.awt.Dimension(43, 43));
        guardarImagen.setPreferredSize(new java.awt.Dimension(43, 43));
        guardarImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarImagenActionPerformed(evt);
            }
        });

        borrarImagen.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        borrarImagen.setToolTipText("Borra la imágen que se está visualizando");
        borrarImagen.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        borrarImagen.setMaximumSize(new java.awt.Dimension(43, 43));
        borrarImagen.setMinimumSize(new java.awt.Dimension(43, 43));
        borrarImagen.setPreferredSize(new java.awt.Dimension(43, 43));
        borrarImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarImagenActionPerformed(evt);
            }
        });

        botonCB2D.setToolTipText("Detecta el código de barras de la imágen");
        botonCB2D.setBorder(null);
        botonCB2D.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonCB2D.setMaximumSize(new java.awt.Dimension(43, 43));
        botonCB2D.setMinimumSize(new java.awt.Dimension(43, 43));
        botonCB2D.setPreferredSize(new java.awt.Dimension(43, 43));
        botonCB2D.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCB2DActionPerformed(evt);
            }
        });

        botonCrearBidi.setToolTipText("Genera una imágen de un código QR a partir de un texto");
        botonCrearBidi.setBorder(null);
        botonCrearBidi.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonCrearBidi.setMaximumSize(new java.awt.Dimension(43, 43));
        botonCrearBidi.setMinimumSize(new java.awt.Dimension(43, 43));
        botonCrearBidi.setPreferredSize(new java.awt.Dimension(43, 43));
        botonCrearBidi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCrearBidiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelIconosImagenLayout = new javax.swing.GroupLayout(panelIconosImagen);
        panelIconosImagen.setLayout(panelIconosImagenLayout);
        panelIconosImagenLayout.setHorizontalGroup(
            panelIconosImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelIconosImagenLayout.createSequentialGroup()
                .addComponent(zoomMas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(zoomMenos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(zoom100, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ajustarAncho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rotar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(guardarImagen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(borrarImagen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(botonCrearBidi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonCB2D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelIconosImagenLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {ajustarAncho, rotar});

        panelIconosImagenLayout.setVerticalGroup(
            panelIconosImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelIconosImagenLayout.createSequentialGroup()
                .addGroup(panelIconosImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(zoomMas, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                    .addComponent(zoomMenos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(zoom100, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                    .addComponent(ajustarAncho, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rotar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(guardarImagen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(borrarImagen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonCB2D, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonCrearBidi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        panelIconosImagenLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {ajustarAncho, rotar});

        panelVisorMini.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panelVisorMini.setAutoscrolls(true);
        panelVisorMini.setMaximumSize(new java.awt.Dimension(186, 32767));
        panelVisorMini.setMinimumSize(new java.awt.Dimension(186, 700));
        panelVisorMini.setName("panelVisorMini"); // NOI18N
        panelVisorMini.setPreferredSize(new java.awt.Dimension(186, 700));

        panelMini.setBorder(javax.swing.BorderFactory.createCompoundBorder(null, javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));
        panelMini.setAutoscrolls(true);
        panelMini.setMaximumSize(new java.awt.Dimension(182, 32767));
        panelMini.setMinimumSize(new java.awt.Dimension(182, 690));
        panelMini.setPreferredSize(new java.awt.Dimension(182, 690));
        panelMini.setVerifyInputWhenFocusTarget(false);

        javax.swing.GroupLayout panelMiniLayout = new javax.swing.GroupLayout(panelMini);
        panelMini.setLayout(panelMiniLayout);
        panelMiniLayout.setHorizontalGroup(
            panelMiniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );
        panelMiniLayout.setVerticalGroup(
            panelMiniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 688, Short.MAX_VALUE)
        );

        panelVisorMini.setViewportView(panelMini);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setMaximumSize(new java.awt.Dimension(185, 51));
        jPanel1.setMinimumSize(new java.awt.Dimension(185, 51));
        jPanel1.setRequestFocusEnabled(false);

        labelPagina.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelPagina.setForeground(new java.awt.Color(0, 0, 102));
        labelPagina.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelPagina.setText("Página");
        labelPagina.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        labelPagina.setMaximumSize(new java.awt.Dimension(50, 25));
        labelPagina.setMinimumSize(new java.awt.Dimension(50, 25));
        labelPagina.setPreferredSize(new java.awt.Dimension(50, 25));
        labelPagina.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        labelde.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelde.setForeground(new java.awt.Color(0, 0, 102));
        labelde.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelde.setText("de");
        labelde.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        labelTotalPaginas.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelTotalPaginas.setForeground(new java.awt.Color(0, 0, 102));
        labelTotalPaginas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTotalPaginas.setText("0");
        labelTotalPaginas.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        labelNumPaginas.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        labelNumPaginas.setForeground(new java.awt.Color(0, 0, 102));
        labelNumPaginas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelNumPaginas.setText("0");
        labelNumPaginas.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelPagina, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelNumPaginas, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelde, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelTotalPaginas, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {labelNumPaginas, labelTotalPaginas, labelde});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(labelPagina, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelde, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelNumPaginas, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(labelTotalPaginas, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {labelNumPaginas, labelTotalPaginas, labelde});

        javax.swing.GroupLayout panelImagenesLayout = new javax.swing.GroupLayout(panelImagenes);
        panelImagenes.setLayout(panelImagenesLayout);
        panelImagenesLayout.setHorizontalGroup(
            panelImagenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelImagenesLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(panelImagenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelIconosImagen, javax.swing.GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)
                    .addComponent(panelVisorImagen, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE))
                .addGap(6, 6, 6)
                .addGroup(panelImagenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelVisorMini, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        panelImagenesLayout.setVerticalGroup(
            panelImagenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelImagenesLayout.createSequentialGroup()
                .addGroup(panelImagenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelIconosImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelImagenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelVisorMini, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(panelVisorImagen, javax.swing.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE))
                .addContainerGap())
        );

        panelImagenesLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jPanel1, panelIconosImagen});

        panelVisorImagen.getAccessibleContext().setAccessibleName("");

        scrollImagenes.setViewportView(panelImagenes);

        scrollConfigura.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollConfigura.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollConfigura.setName("scrollConfigura"); // NOI18N

        panelConfigura.setPreferredSize(new java.awt.Dimension(287, 690));

        panelAccion.setBorder(javax.swing.BorderFactory.createTitledBorder(null, " Acción ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(0, 0, 102))); // NOI18N
        panelAccion.setName("panelAccion"); // NOI18N

        botonDigitalizar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        botonDigitalizar.setText("Digitalizar");
        botonDigitalizar.setToolTipText("Escanear documentos");
        botonDigitalizar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonDigitalizar.setMaximumSize(new java.awt.Dimension(79, 50));
        botonDigitalizar.setMinimumSize(new java.awt.Dimension(79, 50));
        botonDigitalizar.setPreferredSize(new java.awt.Dimension(79, 50));
        botonDigitalizar.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        botonDigitalizar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonDigitalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonDigitalizarActionPerformed(evt);
            }
        });
        botonDigitalizar.setBounds(10, 22, 100, 50);
        panelAccion.add(botonDigitalizar, javax.swing.JLayeredPane.DEFAULT_LAYER);

        botonImportar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        botonImportar.setText("Importar Imagen");
        botonImportar.setToolTipText("<html>\nPermite importar imágenes de ficheros del tipo <b>GIF,JPG,JPEG,BMP,PNG,TIF,TIFF y PDF</b>");
        botonImportar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonImportar.setMaximumSize(new java.awt.Dimension(113, 50));
        botonImportar.setMinimumSize(new java.awt.Dimension(113, 50));
        botonImportar.setPreferredSize(new java.awt.Dimension(113, 50));
        botonImportar.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        botonImportar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonImportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonImportarActionPerformed(evt);
            }
        });
        botonImportar.setBounds(122, 22, 138, 50);
        panelAccion.add(botonImportar, javax.swing.JLayeredPane.DEFAULT_LAYER);

        panelEscaner.setBorder(javax.swing.BorderFactory.createTitledBorder(null, " Escáner ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(0, 0, 102))); // NOI18N
        panelEscaner.setName("PanelAccion"); // NOI18N

        botonSelEscaner.setText("Seleccionar");
        botonSelEscaner.setToolTipText("Seleccionar Escaner");
        botonSelEscaner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSelEscanerActionPerformed(evt);
            }
        });
        botonSelEscaner.setBounds(10, 20, 126, 23);
        panelEscaner.add(botonSelEscaner, javax.swing.JLayeredPane.DEFAULT_LAYER);

        comboColor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Blanco y Negro", "Escala de Grises" }));
        comboColor.setToolTipText("Escanear en Blanco y Negro o en Escala de Grises");
        comboColor.setName("ComboAccion"); // NOI18N
        comboColor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboColorItemStateChanged(evt);
            }
        });
        comboColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboColorActionPerformed(evt);
            }
        });
        comboColor.setBounds(100, 88, 160, 20);
        panelEscaner.add(comboColor, javax.swing.JLayeredPane.DEFAULT_LAYER);

        comboResolucion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "200", "400", "600" }));
        comboResolucion.setToolTipText("Resolución a la que se Escanea");
        comboResolucion.setName("ComboAccion"); // NOI18N
        comboResolucion.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboResolucionItemStateChanged(evt);
            }
        });
        comboResolucion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboResolucionActionPerformed(evt);
            }
        });
        comboResolucion.setBounds(142, 118, 118, 20);
        panelEscaner.add(comboResolucion, javax.swing.JLayeredPane.DEFAULT_LAYER);

        checkDuplex.setText("Duplex");
        checkDuplex.setToolTipText("Si se chequea escanea a doble cara. (si lo soporta el escáner)");
        checkDuplex.setName("checkDuplex"); // NOI18N
        checkDuplex.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                checkDuplexStateChanged(evt);
            }
        });
        checkDuplex.setBounds(176, 20, 82, 23);
        panelEscaner.add(checkDuplex, javax.swing.JLayeredPane.DEFAULT_LAYER);

        nombreEscaner.setEditable(false);
        nombreEscaner.setMaximumSize(new java.awt.Dimension(97, 25));
        nombreEscaner.setMinimumSize(new java.awt.Dimension(97, 25));
        nombreEscaner.setName("nombreEscaner"); // NOI18N
        nombreEscaner.setPreferredSize(new java.awt.Dimension(97, 25));
        nombreEscaner.setRequestFocusEnabled(false);
        nombreEscaner.setBounds(10, 50, 250, 25);
        panelEscaner.add(nombreEscaner, javax.swing.JLayeredPane.DEFAULT_LAYER);

        sliderBrillo.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        sliderBrillo.setToolTipText("Selección del Brillo al Escanear");
        sliderBrillo.setMaximumSize(new java.awt.Dimension(180, 35));
        sliderBrillo.setMinimumSize(new java.awt.Dimension(180, 35));
        sliderBrillo.setPreferredSize(new java.awt.Dimension(180, 35));
        sliderBrillo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderBrilloStateChanged(evt);
            }
        });
        sliderBrillo.setBounds(80, 140, 180, 40);
        panelEscaner.add(sliderBrillo, javax.swing.JLayeredPane.DEFAULT_LAYER);

        labelContraste.setText("Contraste");
        labelContraste.setBounds(16, 180, 58, 18);
        panelEscaner.add(labelContraste, javax.swing.JLayeredPane.DEFAULT_LAYER);

        sliderContraste.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        sliderContraste.setToolTipText("Selección del Contraste al Escanear");
        sliderContraste.setMaximumSize(new java.awt.Dimension(180, 35));
        sliderContraste.setMinimumSize(new java.awt.Dimension(180, 35));
        sliderContraste.setPreferredSize(new java.awt.Dimension(180, 35));
        sliderContraste.setValueIsAdjusting(true);
        sliderContraste.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderContrasteStateChanged(evt);
            }
        });
        sliderContraste.setBounds(80, 170, 180, 40);
        panelEscaner.add(sliderContraste, javax.swing.JLayeredPane.DEFAULT_LAYER);

        labelBrillo.setText("Modo");
        labelBrillo.setBounds(16, 88, 76, 18);
        panelEscaner.add(labelBrillo, javax.swing.JLayeredPane.DEFAULT_LAYER);

        labelBrillo1.setText("Brillo");
        labelBrillo1.setBounds(16, 148, 44, 18);
        panelEscaner.add(labelBrillo1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        labelBrillo2.setText("Resolución (PPP)");
        labelBrillo2.setBounds(16, 118, 112, 18);
        panelEscaner.add(labelBrillo2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        botonEnviar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        botonEnviar.setText("Guardar");
        botonEnviar.setToolTipText("Genera y envia el Lote de imágenes al Gestor Documental");
        botonEnviar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonEnviar.setMaximumSize(new java.awt.Dimension(85, 65));
        botonEnviar.setMinimumSize(new java.awt.Dimension(85, 65));
        botonEnviar.setName("botonEnviar"); // NOI18N
        botonEnviar.setOpaque(false);
        botonEnviar.setPreferredSize(new java.awt.Dimension(85, 65));
        botonEnviar.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        botonEnviar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEnviarActionPerformed(evt);
            }
        });

        botonGuardar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        botonGuardar.setText("Actualizar");
        botonGuardar.setToolTipText("<html>\nGuardar el Lote de imágenes.\n<br>Actualiza el número de páginas del lote. ");
        botonGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonGuardar.setMaximumSize(new java.awt.Dimension(85, 65));
        botonGuardar.setMinimumSize(new java.awt.Dimension(85, 65));
        botonGuardar.setName("botonGuardar"); // NOI18N
        botonGuardar.setOpaque(false);
        botonGuardar.setPreferredSize(new java.awt.Dimension(85, 65));
        botonGuardar.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        botonGuardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGuardarActionPerformed(evt);
            }
        });

        botonEliminar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        botonEliminar.setText("Eliminar");
        botonEliminar.setToolTipText("Eliminar el Lote de imágenes actual");
        botonEliminar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonEliminar.setMaximumSize(new java.awt.Dimension(85, 65));
        botonEliminar.setMinimumSize(new java.awt.Dimension(85, 65));
        botonEliminar.setOpaque(false);
        botonEliminar.setPreferredSize(new java.awt.Dimension(85, 65));
        botonEliminar.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        botonEliminar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarActionPerformed(evt);
            }
        });

        panelValores.setBorder(javax.swing.BorderFactory.createTitledBorder(null, " Valores  ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(0, 0, 102))); // NOI18N
        panelValores.setName("panelValores"); // NOI18N

        jLabel1.setText("Nombre del Fichero (sin extensión)");
        jLabel1.setBounds(10, 98, 210, 14);
        panelValores.add(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        textoFichero.setMaximumSize(new java.awt.Dimension(250, 25));
        textoFichero.setMinimumSize(new java.awt.Dimension(250, 25));
        textoFichero.setPreferredSize(new java.awt.Dimension(250, 25));
        textoFichero.setBounds(8, 116, 250, 25);
        panelValores.add(textoFichero, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLabel2.setText("Directorio de destino");
        jLabel2.setBounds(10, 32, 176, 16);
        panelValores.add(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        textoDirectorio.setEditable(false);
        textoDirectorio.setBackground(new java.awt.Color(255, 255, 255));
        textoDirectorio.setMinimumSize(new java.awt.Dimension(215, 25));
        textoDirectorio.setPreferredSize(new java.awt.Dimension(215, 25));
        textoDirectorio.setBounds(8, 56, 215, 25);
        panelValores.add(textoDirectorio, javax.swing.JLayeredPane.DEFAULT_LAYER);

        botonDirectorio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/jscan/Pantallas/imagenes/carpeta.png"))); // NOI18N
        botonDirectorio.setMaximumSize(new java.awt.Dimension(32, 32));
        botonDirectorio.setMinimumSize(new java.awt.Dimension(32, 32));
        botonDirectorio.setPreferredSize(new java.awt.Dimension(32, 32));
        botonDirectorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonDirectorioActionPerformed(evt);
            }
        });
        botonDirectorio.setBounds(226, 53, 32, 32);
        panelValores.add(botonDirectorio, javax.swing.JLayeredPane.DEFAULT_LAYER);

        comboTipoFichero.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PDF Multipágina", "TIFF Multipágina", "JPG", "GIF", "PNG", "BMP" }));
        comboTipoFichero.setMaximumSize(new java.awt.Dimension(250, 25));
        comboTipoFichero.setMinimumSize(new java.awt.Dimension(250, 25));
        comboTipoFichero.setPreferredSize(new java.awt.Dimension(250, 25));
        comboTipoFichero.setBounds(8, 162, 250, 25);
        panelValores.add(comboTipoFichero, javax.swing.JLayeredPane.DEFAULT_LAYER);

        etiquetaCB.setEditable(false);
        etiquetaCB.setColumns(2);
        etiquetaCB.setLineWrap(true);
        etiquetaCB.setRows(2);
        etiquetaCB.setWrapStyleWord(true);
        jScrollPane2.setViewportView(etiquetaCB);

        javax.swing.GroupLayout panelConfiguraLayout = new javax.swing.GroupLayout(panelConfigura);
        panelConfigura.setLayout(panelConfiguraLayout);
        panelConfiguraLayout.setHorizontalGroup(
            panelConfiguraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelConfiguraLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelConfiguraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelConfiguraLayout.createSequentialGroup()
                        .addGroup(panelConfiguraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelAccion, javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(panelEscaner, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(panelValores))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelConfiguraLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(botonEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(botonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(botonEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelConfiguraLayout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addContainerGap())))
        );
        panelConfiguraLayout.setVerticalGroup(
            panelConfiguraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelConfiguraLayout.createSequentialGroup()
                .addGroup(panelConfiguraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(botonGuardar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(botonEnviar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelAccion, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelEscaner, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelValores, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(70, Short.MAX_VALUE))
        );

        panelAccion.getAccessibleContext().setAccessibleName("PanelAccion");
        panelValores.getAccessibleContext().setAccessibleName(" Procesos ");

        scrollConfigura.setViewportView(panelConfigura);

        opcionOpciones.setMnemonic('O');
        opcionOpciones.setText("Opciones");
        opcionOpciones.setName("opcionOpciones"); // NOI18N

        opcionDigitalizar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        opcionDigitalizar.setText("Digitalizar");
        opcionDigitalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionDigitalizarActionPerformed(evt);
            }
        });
        opcionOpciones.add(opcionDigitalizar);

        opcionImportar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        opcionImportar.setText("Importar Imágenes");
        opcionImportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionImportarActionPerformed(evt);
            }
        });
        opcionOpciones.add(opcionImportar);
        opcionOpciones.add(jSeparator4);

        opcionSelEscaner.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        opcionSelEscaner.setText("Seleccionar Escáner");
        opcionSelEscaner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionSelEscanerActionPerformed(evt);
            }
        });
        opcionOpciones.add(opcionSelEscaner);
        opcionOpciones.add(jSeparator1);

        opcionCerrar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
        opcionCerrar.setText("Cerrar");
        opcionCerrar.setToolTipText("Cerrar la Aplicación");
        opcionCerrar.setActionCommand("");
        opcionCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionCerrarActionPerformed(evt);
            }
        });
        opcionOpciones.add(opcionCerrar);

        jMenuPrincipal.add(opcionOpciones);

        Lotes.setMnemonic('L');
        Lotes.setText("Lotes de Imágenes ");

        opcionEnviarLote.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        opcionEnviarLote.setText("Guardar Imágenes");
        opcionEnviarLote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionEnviarLoteActionPerformed(evt);
            }
        });
        Lotes.add(opcionEnviarLote);

        opcionGuardarLote.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        opcionGuardarLote.setText("Actualizar páginas");
        Lotes.add(opcionGuardarLote);

        opcionBorrarLote.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, java.awt.event.InputEvent.CTRL_MASK));
        opcionBorrarLote.setText("Borrar lote de Imágenes");
        opcionBorrarLote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionBorrarLoteActionPerformed(evt);
            }
        });
        Lotes.add(opcionBorrarLote);
        Lotes.add(jSeparator2);

        opcionLotes.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        opcionLotes.setText("Ver lote de Imágenes guardadas");
        opcionLotes.setToolTipText("Muestra los Lotes Guardados (permitiendo recuperarlos) o Lotes Enviados (mostrando su estado actual)");
        opcionLotes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionLotesActionPerformed(evt);
            }
        });
        Lotes.add(opcionLotes);

        jMenuPrincipal.add(Lotes);

        Imagen.setMnemonic('I');
        Imagen.setText("Imágenes");

        opcionzoommas.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_PLUS, java.awt.event.InputEvent.CTRL_MASK));
        opcionzoommas.setText("Zoom +");
        opcionzoommas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionzoommasActionPerformed(evt);
            }
        });
        Imagen.add(opcionzoommas);

        opcionzoommenos.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_MINUS, java.awt.event.InputEvent.CTRL_MASK));
        opcionzoommenos.setText("Zoom -");
        opcionzoommenos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionzoommenosActionPerformed(evt);
            }
        });
        Imagen.add(opcionzoommenos);

        opcionzoom100.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_0, java.awt.event.InputEvent.CTRL_MASK));
        opcionzoom100.setText("Zoom 100%");
        opcionzoom100.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionzoom100ActionPerformed(evt);
            }
        });
        Imagen.add(opcionzoom100);

        opcionancho.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.InputEvent.CTRL_MASK));
        opcionancho.setText("Ajustar a ancho de Página");
        opcionancho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionanchoActionPerformed(evt);
            }
        });
        Imagen.add(opcionancho);

        opcionrotar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        opcionrotar.setText("Rotar 90º a la derecha");
        opcionrotar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionrotarActionPerformed(evt);
            }
        });
        Imagen.add(opcionrotar);

        opcionguardarimagen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        opcionguardarimagen.setText("Guardar Imagen rotada");
        opcionguardarimagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionguardarimagenActionPerformed(evt);
            }
        });
        Imagen.add(opcionguardarimagen);

        opcionborrarimagen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
        opcionborrarimagen.setText("Borrar Imagen");
        opcionborrarimagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionborrarimagenActionPerformed(evt);
            }
        });
        Imagen.add(opcionborrarimagen);

        jMenuPrincipal.add(Imagen);

        opcionUtilidades.setMnemonic('U');
        opcionUtilidades.setText("Utilidades");

        opcionLeerLog.setText("Ver fichero de log");
        opcionLeerLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionLeerLogActionPerformed(evt);
            }
        });
        opcionUtilidades.add(opcionLeerLog);
        opcionUtilidades.add(jSeparator3);

        opcionDebug.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        opcionDebug.setSelected(true);
        opcionDebug.setText("Activar Debug");
        opcionDebug.setToolTipText("Se activa o desactiva el modo Debug. Añade más información al fichero de Log (no sólo los mensajes de error). Desactivado al arrancar la aplicación.");
        opcionDebug.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                opcionDebugMouseClicked(evt);
            }
        });
        opcionDebug.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionDebugActionPerformed(evt);
            }
        });
        opcionUtilidades.add(opcionDebug);

        jMenuPrincipal.add(opcionUtilidades);

        opcionApariencia.setMnemonic('A');
        opcionApariencia.setText("Apariencia");

        opcionRBMetal.setSelected(true);
        opcionRBMetal.setText("Metal");
        opcionRBMetal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionRBMetalActionPerformed(evt);
            }
        });
        opcionApariencia.add(opcionRBMetal);

        opcionRBNimbus.setSelected(true);
        opcionRBNimbus.setText("Nimbus");
        opcionRBNimbus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionRBNimbusActionPerformed(evt);
            }
        });
        opcionApariencia.add(opcionRBNimbus);

        opcionRBWindows.setSelected(true);
        opcionRBWindows.setText("Windows");
        opcionRBWindows.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionRBWindowsActionPerformed(evt);
            }
        });
        opcionApariencia.add(opcionRBWindows);

        opcionRBWindowsClassic.setSelected(true);
        opcionRBWindowsClassic.setText("Windows Classic");
        opcionRBWindowsClassic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionRBWindowsClassicActionPerformed(evt);
            }
        });
        opcionApariencia.add(opcionRBWindowsClassic);

        opcionRBPorDefecto.setSelected(true);
        opcionRBPorDefecto.setText("Propia del Sistema Operativo");
        opcionRBPorDefecto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionRBPorDefectoActionPerformed(evt);
            }
        });
        opcionApariencia.add(opcionRBPorDefecto);
        opcionApariencia.add(jSeparator6);

        opcionMaximizar.setSelected(true);
        opcionMaximizar.setText("Maximizar ventana al inicio");
        opcionMaximizar.setToolTipText("Si está marcado maximiza la ventana automáticamente al iniciarse la aplicación.");
        opcionMaximizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionMaximizarActionPerformed(evt);
            }
        });
        opcionApariencia.add(opcionMaximizar);

        jMenuPrincipal.add(opcionApariencia);

        opcionAcerca.setMnemonic('d');
        opcionAcerca.setText("Acerca de ...");
        opcionAcerca.setName("opcionAcerca"); // NOI18N

        opcionManual.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_MASK));
        opcionManual.setText("Manual de la Aplicación");
        opcionManual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionManualActionPerformed(evt);
            }
        });
        opcionAcerca.add(opcionManual);
        opcionAcerca.add(jSeparator5);

        opcionLicenciaApache.setText("Apache License 2.0");
        opcionLicenciaApache.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionLicenciaApacheActionPerformed(evt);
            }
        });
        opcionAcerca.add(opcionLicenciaApache);

        opcionLicenciaItext.setText("Free / Open Source Software License");
        opcionLicenciaItext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionLicenciaItextActionPerformed(evt);
            }
        });
        opcionAcerca.add(opcionLicenciaItext);

        opcionLicenciaXStream.setText("XStream License");
        opcionLicenciaXStream.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionLicenciaXStreamActionPerformed(evt);
            }
        });
        opcionAcerca.add(opcionLicenciaXStream);
        opcionAcerca.add(jSeparator7);

        opcionAbout.setText("Acerca de ...");
        opcionAbout.setToolTipText("Información acerca de la Aplicación");
        opcionAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionAboutActionPerformed(evt);
            }
        });
        opcionAcerca.add(opcionAbout);

        jMenuPrincipal.add(opcionAcerca);
        opcionAcerca.getAccessibleContext().setAccessibleName("OpcionAcerca");

        setJMenuBar(jMenuPrincipal);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(scrollConfigura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(scrollImagenes, javax.swing.GroupLayout.DEFAULT_SIZE, 733, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollImagenes)
            .addComponent(scrollConfigura, javax.swing.GroupLayout.DEFAULT_SIZE, 747, Short.MAX_VALUE)
        );

        scrollConfigura.getAccessibleContext().setAccessibleName("ScrollConfigura");

        getAccessibleContext().setAccessibleName("JFramePrincipal");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void opcionAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionAboutActionPerformed
        mostrarAcercade();
    }//GEN-LAST:event_opcionAboutActionPerformed

    private void opcionCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionCerrarActionPerformed
        cerrar();
    }//GEN-LAST:event_opcionCerrarActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        arranque = true;
        asignarIconos();
        inicializarAplicacion();
        arranque = false;
    }//GEN-LAST:event_formWindowOpened

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        cerrar();
    }//GEN-LAST:event_formWindowClosing

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
    }//GEN-LAST:event_formComponentResized

    private void zoomMasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomMasActionPerformed
        if (pd != null) {
            pd.zoomMas(20);
            panelImagen.setPreferredSize(new java.awt.Dimension(pd.getWidth(), pd.getHeight()));
            panelImagen.setSize(new java.awt.Dimension(pd.getWidth(), pd.getHeight()));
            panelImagen.repaint();
            repaint();
            System.gc();
        }
    }//GEN-LAST:event_zoomMasActionPerformed

    private void zoomMenosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomMenosActionPerformed
        if (pd != null) {
            pd.zoomMenos(20);
            panelImagen.setPreferredSize(new java.awt.Dimension(pd.getWidth(), pd.getHeight()));
            panelImagen.setSize(new java.awt.Dimension(pd.getWidth(), pd.getHeight()));
            panelImagen.repaint();
            repaint();
            System.gc();
        }
    }//GEN-LAST:event_zoomMenosActionPerformed

    private void zoom100ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoom100ActionPerformed
        if (pd != null) {
            pd.zoom100();
            panelVisorImagen.setPreferredSize(new java.awt.Dimension(panelIconosImagen.getWidth(), scrollImagenes.getHeight() - panelIconosImagen.getHeight() - 8));
            panelVisorImagen.setSize(new java.awt.Dimension(panelIconosImagen.getWidth(), scrollImagenes.getHeight() - panelIconosImagen.getHeight() - 8));
            panelVisorImagen.repaint();
            panelImagen.setPreferredSize(new java.awt.Dimension(pd.getWidth(), pd.getHeight()));
            panelImagen.setSize(new java.awt.Dimension(pd.getWidth(), pd.getHeight()));
            panelImagen.repaint();
            repaint();
            System.gc();
        }
    }//GEN-LAST:event_zoom100ActionPerformed

    @Action
    private void enviar() {
        if (contimagen > 0) {
            if (comprobarValores()) {
                String tipofichero = comboTipoFichero.getItemAt(comboTipoFichero.getSelectedIndex()).toString().toUpperCase();
                if (tipofichero.contains("TIFF")) {
                    guardarTiff();
                    return;
                }
                if (tipofichero.contains("PDF")) {
                    guardarPdf();
                    return;
                }
                if (tipofichero.contains("JPG")) {
                    guardarImagenTipo("JPG");
                    return;
                }
                if (tipofichero.contains("GIF")) {
                    guardarImagenTipo("GIF");
                    return;
                }
                if (tipofichero.contains("PNG")) {
                    guardarImagenTipo("PNG");
                    return;
                }
                if (tipofichero.contains("BMP")) {
                    guardarImagenTipo("BMP");
                    return;
                }
            }
        }
    }

    private void ajustarAnchoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ajustarAnchoActionPerformed
        if (pd != null) {
            pd.ajustarAncho(panelVisorImagen.getWidth());
            panelVisorImagen.setPreferredSize(new java.awt.Dimension(panelIconosImagen.getWidth(), scrollImagenes.getHeight() - panelIconosImagen.getHeight() - 8));
            panelVisorImagen.setSize(new java.awt.Dimension(panelIconosImagen.getWidth(), scrollImagenes.getHeight() - panelIconosImagen.getHeight() - 8));
            panelVisorImagen.repaint();
            panelImagen.setPreferredSize(new java.awt.Dimension(pd.getWidth(), pd.getHeight()));
            panelImagen.setSize(new java.awt.Dimension(pd.getWidth(), pd.getHeight()));
            panelImagen.repaint();
            repaint();
            System.gc();
        }
    }//GEN-LAST:event_ajustarAnchoActionPerformed

    private void rotarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rotarActionPerformed
        if (pd != null) {
            pd.rotar();
            panelImagen.setPreferredSize(new java.awt.Dimension(pd.getWidth(), pd.getHeight()));
            panelImagen.setSize(new java.awt.Dimension(pd.getWidth(), pd.getHeight()));
            panelImagen.repaint();
            repaint();
            System.gc();
        }
    }//GEN-LAST:event_rotarActionPerformed

    private void botonCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCBActionPerformed
        if (contimagen > 0) {
            CodigoBarras cb = new CodigoBarras();
            ArrayList<String> resultadoCB = cb.leerCodigo(rutaboton[minisel]);
            if (!resultadoCB.isEmpty()) {
                if (resultadoCB.size() > 1) {
                    etiquetaCB.setText(resultadoCB.get(1));
                } else {
                    etiquetaCB.setText(resultadoCB.get(0));
                }
            } else {
                etiquetaCB.setText("");
            }
        }
    }//GEN-LAST:event_botonCBActionPerformed

    private void borrarImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarImagenActionPerformed
        if (minisel >= 0) {
            PantallaConfirmaDialogo confirma = new PantallaConfirmaDialogo(this, true);
            confirma.setTitle("Borrar Imágen");
            confirma.etiqueta.setText("¿Desea realmente borrar esta imágen?");
            confirma.repaint();
            confirma.setVisible(true);
            Boolean resultado = confirma.respuesta();
            if (resultado) {
                borrarImagen();
            }
        }
    }//GEN-LAST:event_borrarImagenActionPerformed
    private void guardarImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarImagenActionPerformed
        if (minisel >= 0) {
            guardarImagenModificada();
        }
    }//GEN-LAST:event_guardarImagenActionPerformed

    private void opcionLotesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionLotesActionPerformed
        mostrarLotes();

    }//GEN-LAST:event_opcionLotesActionPerformed

    private void opcionManualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionManualActionPerformed
//        VisorPdfs vpdf = new VisorPdfs();
//        String manual=rutadigita + separador + "manual jscan.pdf";
//        try {
//            vpdf.verPdf(manual);
//            if (DEBUG){
//                Utilidades.escribeLog("Ruta del manual: "+manual);
//            }
//        } catch (Exception ex) {
//            Utilidades.escribeLog("Error al abrir el manual de la aplicación ("+manual+") - Error " + ex.getMessage());
//        }
//       vpdf=null;
        String manual = rutadigita + separador + "manual jscan.pdf";
        try {
            File path = new File(manual);
            Desktop.getDesktop().open(path);
        } catch (IOException ex) {
            Utilidades.escribeLog("Error al abrir el manual de la aplicación (" + manual + ") - Error " + ex.getMessage());
        }


    }//GEN-LAST:event_opcionManualActionPerformed

    private void opcionRBMetalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionRBMetalActionPerformed
        opcionRBMetal.setSelected(true);
        opcionRBNimbus.setSelected(false);
        opcionRBWindows.setSelected(false);
        opcionRBWindowsClassic.setSelected(false);
        opcionRBPorDefecto.setSelected(false);
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            SwingUtilities.updateComponentTreeUI(this);
            this.pack();
        } catch (Exception ex) {
            Utilidades.escribeLog("Error al cambiar Aspecto Visual a Metal - " + ex.getMessage());
        }
    }//GEN-LAST:event_opcionRBMetalActionPerformed

    private void opcionRBNimbusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionRBNimbusActionPerformed
        opcionRBMetal.setSelected(false);
        opcionRBNimbus.setSelected(true);
        opcionRBWindows.setSelected(false);
        opcionRBWindowsClassic.setSelected(false);
        opcionRBPorDefecto.setSelected(false);
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            SwingUtilities.updateComponentTreeUI(this);
            this.pack();
        } catch (Exception ex) {
            Utilidades.escribeLog("Error al cambiar Aspecto Visual a Nimbus - " + ex.getMessage());
        }

    }//GEN-LAST:event_opcionRBNimbusActionPerformed

    private void opcionRBWindowsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionRBWindowsActionPerformed
        opcionRBMetal.setSelected(false);
        opcionRBNimbus.setSelected(false);
        opcionRBWindows.setSelected(true);
        opcionRBWindowsClassic.setSelected(false);
        opcionRBPorDefecto.setSelected(false);
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            SwingUtilities.updateComponentTreeUI(this);
            this.pack();
        } catch (Exception ex) {
            Utilidades.escribeLog("Error al cambiar Aspecto Visual a Windows - " + ex.getMessage());
        }

    }//GEN-LAST:event_opcionRBWindowsActionPerformed

    private void opcionRBWindowsClassicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionRBWindowsClassicActionPerformed
        opcionRBMetal.setSelected(false);
        opcionRBNimbus.setSelected(false);
        opcionRBWindows.setSelected(false);
        opcionRBWindowsClassic.setSelected(true);
        opcionRBPorDefecto.setSelected(false);
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows Classic".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            SwingUtilities.updateComponentTreeUI(this);
            this.pack();
        } catch (Exception ex) {
            Utilidades.escribeLog("Error al cambiar Aspecto Visual a Windows Classic - " + ex.getMessage());
        }
    }//GEN-LAST:event_opcionRBWindowsClassicActionPerformed

    private void opcionRBPorDefectoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionRBPorDefectoActionPerformed
        try {
            opcionRBMetal.setSelected(false);
            opcionRBNimbus.setSelected(false);
            opcionRBWindows.setSelected(false);
            opcionRBWindowsClassic.setSelected(false);
            opcionRBPorDefecto.setSelected(true);
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ex) {
            Utilidades.escribeLog("Error al cambiar Aspecto por Defecto - " + ex.getMessage());
        }
    }//GEN-LAST:event_opcionRBPorDefectoActionPerformed

    private void opcionEnviarLoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionEnviarLoteActionPerformed
        botonEnviarActionPerformed(evt);
    }//GEN-LAST:event_opcionEnviarLoteActionPerformed

    private void opcionDigitalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionDigitalizarActionPerformed
        botonDigitalizarActionPerformed(evt);
    }//GEN-LAST:event_opcionDigitalizarActionPerformed

    private void opcionImportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionImportarActionPerformed
        botonImportarActionPerformed(evt);
    }//GEN-LAST:event_opcionImportarActionPerformed

    private void opcionBorrarLoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionBorrarLoteActionPerformed
        botonEliminarActionPerformed(evt);
    }//GEN-LAST:event_opcionBorrarLoteActionPerformed

    private void opcionzoommenosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionzoommenosActionPerformed
        zoomMenosActionPerformed(evt);
    }//GEN-LAST:event_opcionzoommenosActionPerformed

    private void opcionzoommasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionzoommasActionPerformed
        zoomMasActionPerformed(evt);
    }//GEN-LAST:event_opcionzoommasActionPerformed

    private void opcionzoom100ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionzoom100ActionPerformed
        zoom100ActionPerformed(evt);
    }//GEN-LAST:event_opcionzoom100ActionPerformed

    private void opcionanchoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionanchoActionPerformed
        ajustarAnchoActionPerformed(evt);
    }//GEN-LAST:event_opcionanchoActionPerformed

    private void opcionrotarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionrotarActionPerformed
        rotarActionPerformed(evt);
    }//GEN-LAST:event_opcionrotarActionPerformed

    private void opcionborrarimagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionborrarimagenActionPerformed
        borrarImagenActionPerformed(evt);
    }//GEN-LAST:event_opcionborrarimagenActionPerformed

    private void opcionguardarimagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionguardarimagenActionPerformed
        guardarImagenActionPerformed(evt);
    }//GEN-LAST:event_opcionguardarimagenActionPerformed

    private void opcionLeerLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionLeerLogActionPerformed
        Calendar cal = Calendar.getInstance();
        String anio = String.valueOf(cal.get(Calendar.YEAR));
        String mes = String.valueOf((cal.get(Calendar.MONTH) + 1)).length() == 1 ? "0" + String.valueOf((cal.get(Calendar.MONTH) + 1)) : String.valueOf((cal.get(Calendar.MONTH) + 1));
        String dia = String.valueOf(cal.get(Calendar.DAY_OF_MONTH)).length() == 1 ? "0" + String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) : String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        PantallaLeerFichero log = new PantallaLeerFichero(this, true);
        log.CargarFichero(rutalog + separador + "jscan-" + utilidades.ip() + "-" + dia + "-" + mes + "-" + anio + ".log");
        log.setTitle("Fichero de log " + rutalog + separador + "jscan-" + utilidades.ip() + "-" + dia + "-" + mes + "-" + anio + ".log");
        log.setVisible(true);
    }//GEN-LAST:event_opcionLeerLogActionPerformed

    private void opcionSelEscanerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionSelEscanerActionPerformed
        botonSelEscanerActionPerformed(evt);
    }//GEN-LAST:event_opcionSelEscanerActionPerformed

    private void opcionMaximizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionMaximizarActionPerformed
        String fichero = rutadigita + separador + "escaner.ini";
        if (opcionMaximizar.isSelected()) {
            propiniescaner.setProperty("Maximizar", "S");
        } else {
            propiniescaner.setProperty("Maximizar", "N");
        }
        utilidades.escribirPropeties(fichero, propiniescaner);
    }//GEN-LAST:event_opcionMaximizarActionPerformed

    private void opcionDebugMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_opcionDebugMouseClicked
        if (opcionDebug.isSelected()) {
            DEBUG = true;
            Utilidades.escribeLog("DEBUG=true");
        } else {
            DEBUG = false;
            Utilidades.escribeLog("DEBUG=false");
        }
    }//GEN-LAST:event_opcionDebugMouseClicked

    private void opcionDebugActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionDebugActionPerformed
        if (opcionDebug.isSelected()) {
            DEBUG = true;
            Utilidades.escribeLog("DEBUG=true");
        } else {
            DEBUG = false;
            Utilidades.escribeLog("DEBUG=false");
        }
    }//GEN-LAST:event_opcionDebugActionPerformed

    private static void open(URI uri) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(uri);
            } catch (IOException e) { /* TODO: error handling */ }
        } else { /* TODO: error handling */ }
    }

    private void opcionLicenciaApacheActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionLicenciaApacheActionPerformed
        try {
            URI uri = new URI("http://www.apache.org/licenses/LICENSE-2.0");
            open(uri);
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_opcionLicenciaApacheActionPerformed

    private void opcionLicenciaItextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionLicenciaItextActionPerformed
        try {
            URI uri = new URI("http://itextpdf.com/terms-of-use/agpl.php");
            open(uri);
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_opcionLicenciaItextActionPerformed

    private void opcionLicenciaXStreamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionLicenciaXStreamActionPerformed
        try {
            URI uri = new URI("http://xstream.codehaus.org/license.html");
            open(uri);
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_opcionLicenciaXStreamActionPerformed

    private void botonCB2DActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCB2DActionPerformed
        if (contimagen > 0) {
            CodigoBarras cb = new CodigoBarras();
            String resultadoCB = cb.leerCodigo2D(rutaboton[minisel]);
            if (!resultadoCB.isEmpty()) {
                etiquetaCB.setText(resultadoCB);
            } else {
                etiquetaCB.setText("");
            }
        }
    }//GEN-LAST:event_botonCB2DActionPerformed

    private void botonDirectorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonDirectorioActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Seleccionar directorio");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            textoDirectorio.setText(chooser.getSelectedFile().toString());
        } else {
            if (DEBUG) {
                Utilidades.escribeLog("No se ha seleccionado directorio de salida ");
            }
        }
    }//GEN-LAST:event_botonDirectorioActionPerformed

    private void botonEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarActionPerformed
        borrarLoteActual();
    }//GEN-LAST:event_botonEliminarActionPerformed

    private void botonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGuardarActionPerformed
        if (pd != null) {
            guardarLote();
        }
    }//GEN-LAST:event_botonGuardarActionPerformed

    private void botonEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEnviarActionPerformed
        enviar();
    }//GEN-LAST:event_botonEnviarActionPerformed

    private void sliderContrasteStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderContrasteStateChanged
        seleccionarContraste();
    }//GEN-LAST:event_sliderContrasteStateChanged

    private void sliderBrilloStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderBrilloStateChanged
        seleccionarBrillo();
    }//GEN-LAST:event_sliderBrilloStateChanged

    private void checkDuplexStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_checkDuplexStateChanged
        seleccionarDuplex();
    }//GEN-LAST:event_checkDuplexStateChanged

    private void comboResolucionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboResolucionActionPerformed
        seleccionarResolucion();
    }//GEN-LAST:event_comboResolucionActionPerformed

    private void comboResolucionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboResolucionItemStateChanged
        seleccionarResolucion();
    }//GEN-LAST:event_comboResolucionItemStateChanged

    private void comboColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboColorActionPerformed
        seleccionarColor();
    }//GEN-LAST:event_comboColorActionPerformed

    private void comboColorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboColorItemStateChanged
        seleccionarColor();
    }//GEN-LAST:event_comboColorItemStateChanged

    private void botonSelEscanerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSelEscanerActionPerformed
        selecionarEscaner();
    }//GEN-LAST:event_botonSelEscanerActionPerformed

    private void botonImportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonImportarActionPerformed
        importarImagenes();
    }//GEN-LAST:event_botonImportarActionPerformed

    private void botonDigitalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonDigitalizarActionPerformed
        if (!nombreEscaner.getText().isEmpty()) {
            escanear();
        }
    }//GEN-LAST:event_botonDigitalizarActionPerformed

    private void botonCrearBidiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCrearBidiActionPerformed
        PantallaTextoIn pantallaTexto = new PantallaTextoIn(this, true);
        pantallaTexto.setTitle("Texto para crear Código QR");
        pantallaTexto.setVisible(true);
        String texto = pantallaTexto.getTexto();
        if (!texto.isEmpty()) {
            CodigoBarras cb = new CodigoBarras();
            cb.CrearBidi(texto);
            importarDeFichero(utilidades.crearDirBase() + utilidades.separador() + "qr_png.png");
        }
    }//GEN-LAST:event_botonCrearBidiActionPerformed

    private Boolean borrarLoteActual() {
        Boolean resultado = true;

        if (minisel < 0) {
            lote = "";
            return false;
        }

        PantallaConfirmaDialogo confirma = new PantallaConfirmaDialogo(this, true);

        confirma.setTitle("Borrar Lote de Imágenes");
        confirma.etiqueta.setText("¿Desea realmente borrar este lote?");
        confirma.repaint();
        confirma.setVisible(true);
        resultado = confirma.respuesta();
        if (resultado) {

            String ruta = rutalote;
            if (!lote.equals("")) {
                borrarLote(lote);
            }
            inicializar();
            utilidades.borrarDirectorio(ruta);
        }

        return resultado;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            Utilidades.escribeLog(PantallaPrincipal.class.getName().toString() + " - " + ex.getMessage());
        } catch (InstantiationException ex) {
            Utilidades.escribeLog(PantallaPrincipal.class.getName().toString() + " - " + ex.getMessage());
        } catch (IllegalAccessException ex) {
            Utilidades.escribeLog(PantallaPrincipal.class.getName().toString() + " - " + ex.getMessage());
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            Utilidades.escribeLog(PantallaPrincipal.class.getName().toString() + " - " + ex.getMessage());
        }

        // Impide que se abrán más de una instancia de la aplicación
        try {
            Socket clientSocket = new Socket("localhost", ConexionUnica.PORT);
            Utilidades.mensa(ventanapadre, "Digita-cliente", "\n  Ya existe una instancia de la aplicación Digita-cliente en ejecución.");
            System.exit(0);
        } catch (Exception e) {
            ConexionUnica sds = new ConexionUnica();
            sds.start();
        }


        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PantallaPrincipal().setVisible(true);

            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu Imagen;
    private javax.swing.JMenu Lotes;
    private javax.swing.JButton ajustarAncho;
    private javax.swing.JButton borrarImagen;
    private javax.swing.JButton botonCB;
    private javax.swing.JButton botonCB2D;
    private javax.swing.JButton botonCrearBidi;
    private javax.swing.JButton botonDigitalizar;
    private javax.swing.JToggleButton botonDirectorio;
    private javax.swing.JButton botonEliminar;
    private javax.swing.JButton botonEnviar;
    private javax.swing.JButton botonGuardar;
    private javax.swing.JButton botonImportar;
    private javax.swing.JButton botonSelEscaner;
    private javax.swing.JCheckBox checkDuplex;
    private javax.swing.JComboBox comboColor;
    private javax.swing.JComboBox comboResolucion;
    private javax.swing.JComboBox comboTipoFichero;
    private javax.swing.JTextArea etiquetaCB;
    private javax.swing.JButton guardarImagen;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuBar jMenuPrincipal;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JLabel labelBrillo;
    private javax.swing.JLabel labelBrillo1;
    private javax.swing.JLabel labelBrillo2;
    private javax.swing.JLabel labelContraste;
    private javax.swing.JLabel labelNumPaginas;
    private javax.swing.JLabel labelPagina;
    private javax.swing.JLabel labelTotalPaginas;
    private javax.swing.JLabel labelde;
    private javax.swing.JTextField nombreEscaner;
    private javax.swing.JMenuItem opcionAbout;
    private javax.swing.JMenu opcionAcerca;
    private javax.swing.JMenu opcionApariencia;
    private javax.swing.JMenuItem opcionBorrarLote;
    private javax.swing.JMenuItem opcionCerrar;
    private javax.swing.JCheckBoxMenuItem opcionDebug;
    private javax.swing.JMenuItem opcionDigitalizar;
    private javax.swing.JMenuItem opcionEnviarLote;
    private javax.swing.JMenuItem opcionGuardarLote;
    private javax.swing.JMenuItem opcionImportar;
    private javax.swing.JMenuItem opcionLeerLog;
    private javax.swing.JMenuItem opcionLicenciaApache;
    private javax.swing.JMenuItem opcionLicenciaItext;
    private javax.swing.JMenuItem opcionLicenciaXStream;
    private javax.swing.JMenuItem opcionLotes;
    private javax.swing.JMenuItem opcionManual;
    private javax.swing.JCheckBoxMenuItem opcionMaximizar;
    private javax.swing.JMenu opcionOpciones;
    private javax.swing.JRadioButtonMenuItem opcionRBMetal;
    private javax.swing.JRadioButtonMenuItem opcionRBNimbus;
    private javax.swing.JRadioButtonMenuItem opcionRBPorDefecto;
    private javax.swing.JRadioButtonMenuItem opcionRBWindows;
    private javax.swing.JRadioButtonMenuItem opcionRBWindowsClassic;
    private javax.swing.JMenuItem opcionSelEscaner;
    private javax.swing.JMenu opcionUtilidades;
    private javax.swing.JMenuItem opcionancho;
    private javax.swing.JMenuItem opcionborrarimagen;
    private javax.swing.JMenuItem opcionguardarimagen;
    private javax.swing.JMenuItem opcionrotar;
    private javax.swing.JMenuItem opcionzoom100;
    private javax.swing.JMenuItem opcionzoommas;
    private javax.swing.JMenuItem opcionzoommenos;
    private javax.swing.JLayeredPane panelAccion;
    private javax.swing.JPanel panelConfigura;
    private javax.swing.JLayeredPane panelEscaner;
    public javax.swing.JPanel panelIconosImagen;
    private javax.swing.JPanel panelImagen;
    public javax.swing.JPanel panelImagenes;
    private javax.swing.JPanel panelMini;
    private javax.swing.JLayeredPane panelValores;
    private javax.swing.JScrollPane panelVisorImagen;
    private javax.swing.JScrollPane panelVisorMini;
    private javax.swing.JButton rotar;
    private javax.swing.JScrollPane scrollConfigura;
    public javax.swing.JScrollPane scrollImagenes;
    private javax.swing.JSlider sliderBrillo;
    private javax.swing.JSlider sliderContraste;
    private javax.swing.JTextField textoDirectorio;
    private javax.swing.JTextField textoFichero;
    private javax.swing.JButton zoom100;
    private javax.swing.JButton zoomMas;
    private javax.swing.JButton zoomMenos;
    // End of variables declaration//GEN-END:variables

    @Action
    public void mostrarAcercade() {
        Acercade about = new Acercade(this, true);
        about.setLocationRelativeTo(this);
        about.setVisible(true);
    }

    public void cerrar() {

        PantallaConfirmaDialogo confirma = new PantallaConfirmaDialogo(this, true);
        confirma.setTitle("Cerrar jScan");
        confirma.etiqueta.setText("¿Desea realmente cerrar la aplicación?");
        confirma.repaint();
        confirma.setVisible(true);

        if (confirma.respuesta()) {
            System.exit(0);
        }


        /*
         Object[] opciones = {"Aceptar", "Cancelar"};

         int eleccion = JOptionPane.showOptionDialog(this, "¿Desea cerrar la aplicación?", "Cerrar jScan",
         JOptionPane.YES_NO_OPTION,
         JOptionPane.QUESTION_MESSAGE, null, opciones, "Aceptar");
         if (eleccion == JOptionPane.YES_OPTION) {
         System.exit(0);
         }

         */
    }

    protected static Image getLogo() {
        java.net.URL imgURL = PantallaPrincipal.class.getClassLoader().getResource("es/jscan/Pantallas/imagenes/scaner.jpg");
        if (imgURL != null) {
            return new ImageIcon(imgURL).getImage();
        } else {
            return null;
        }
    }

    private void asignarIconos() {
        java.net.URL imgURL = PantallaPrincipal.class.getClassLoader().getResource("es/jscan/Pantallas/imagenes/guardar.png");
        Icon imgicon = new ImageIcon(imgURL);
        this.botonEnviar.setIcon(imgicon);

        imgURL = PantallaPrincipal.class.getClassLoader().getResource("es/jscan/Pantallas/imagenes/borrar.png");
        imgicon = new ImageIcon(imgURL);
        this.botonEliminar.setIcon(imgicon);

        imgURL = PantallaPrincipal.class.getClassLoader().getResource("es/jscan/Pantallas/imagenes/enviar.png");
        imgicon = new ImageIcon(imgURL);
        this.botonGuardar.setIcon(imgicon);

        imgURL = PantallaPrincipal.class.getClassLoader().getResource("es/jscan/Pantallas/imagenes/escanear.png");
        imgicon = new ImageIcon(imgURL);
        this.botonDigitalizar.setIcon(imgicon);

        imgURL = PantallaPrincipal.class.getClassLoader().getResource("es/jscan/Pantallas/imagenes/importar.png");
        imgicon = new ImageIcon(imgURL);
        this.botonImportar.setIcon(imgicon);
        imgURL = PantallaPrincipal.class.getClassLoader().getResource("es/jscan/Pantallas/imagenes/zoommas.png");
        imgicon = new ImageIcon(imgURL);
        this.zoomMas.setIcon(imgicon);

        imgURL = PantallaPrincipal.class.getClassLoader().getResource("es/jscan/Pantallas/imagenes/zoommenos.png");
        imgicon = new ImageIcon(imgURL);
        this.zoomMenos.setIcon(imgicon);

        imgURL = PantallaPrincipal.class.getClassLoader().getResource("es/jscan/Pantallas/imagenes/100x100.png");
        imgicon = new ImageIcon(imgURL);
        this.zoom100.setIcon(imgicon);

        imgURL = PantallaPrincipal.class.getClassLoader().getResource("es/jscan/Pantallas/imagenes/ancho.png");
        imgicon = new ImageIcon(imgURL);
        this.ajustarAncho.setIcon(imgicon);

        imgURL = PantallaPrincipal.class.getClassLoader().getResource("es/jscan/Pantallas/imagenes/rotar.png");
        imgicon = new ImageIcon(imgURL);
        this.rotar.setIcon(imgicon);

        imgURL = PantallaPrincipal.class.getClassLoader().getResource("es/jscan/Pantallas/imagenes/borrarimagen.png");
        imgicon = new ImageIcon(imgURL);
        this.borrarImagen.setIcon(imgicon);

        imgURL = PantallaPrincipal.class.getClassLoader().getResource("es/jscan/Pantallas/imagenes/guardarimagen.png");
        imgicon = new ImageIcon(imgURL);
        this.guardarImagen.setIcon(imgicon);

        botonCB.setText("");
//        imgURL = PantallaPrincipal.class.getResource("imagenes/codigobarras.png");
//        imgicon = new ImageIcon(imgURL);
//        this.botonCB.setIcon(imgicon);

        imgURL = PantallaPrincipal.class.getClassLoader().getResource("es/jscan/Pantallas/imagenes/codigobarras.png");
        Icon icono = new ImageIcon(imgURL);
        this.botonCB.setIcon(icono);
        this.botonCB.setBorder(null);
        this.botonCB.setContentAreaFilled(false);
        this.botonCB.setBorderPainted(false);

        imgURL = PantallaPrincipal.class.getClassLoader().getResource("es/jscan/Pantallas/imagenes/cb2d.png");
        icono = new ImageIcon(imgURL);
        this.botonCB2D.setIcon(icono);
        this.botonCB2D.setBorder(null);
        this.botonCB2D.setContentAreaFilled(false);
        this.botonCB2D.setBorderPainted(false);

        imgURL = PantallaPrincipal.class.getClassLoader().getResource("es/jscan/Pantallas/imagenes/imagen-qr.png");
        icono = new ImageIcon(imgURL);
        this.botonCrearBidi.setIcon(icono);
        this.botonCrearBidi.setBorder(null);
        this.botonCrearBidi.setContentAreaFilled(false);
        this.botonCrearBidi.setBorderPainted(false);


        scrollConfigura.revalidate();
        scrollImagenes.revalidate();
        this.repaint();

    }

    @Action
    public void inicializarAplicacion() {
        String fichero = rutadigita + separador + "procesos.xml";
        if (utilidades.versionJavaBits().equals("x86")) {
            System.setProperty("java.library.path", rutadigita + separador + "drivers");
        } else {
            System.setProperty("java.library.path", rutadigita + separador + "drivers" + separador + "x64");
        }
        // Saca la DLL jtwain.dll del jar y la copia en el directorio del trabajo dentro de "drivers"
        utilidades.crearDirectorio(rutadigita + separador + "drivers");
        utilidades.crearDirectorio(rutadigita + separador + "drivers" + separador + "x64");
        utilidades.sacarArchivoJar("/es/jscan/utilidades/drivers/jtwain.dll", rutadigita + separador + "drivers" + separador + "jtwain.dll");
        utilidades.sacarArchivoJar("/es/jscan/utilidades/drivers/x64/jtwain.dll", rutadigita + separador + "drivers" + separador + "x64" + separador + "jtwain.dll");
        //utilidades.sacarArchivoJar("/es/jscan/utilidades/drivers/libjsane.so", rutadigita + separador + "drivers" + separador + "libjsane.so");
        utilidades.sacarArchivoJar("/es/jscan/Pantallas/propiedades/procesos.xml", rutadigita + separador + "procesos.xml");
        utilidades.sacarArchivoJar("/es/jscan/manual/manual jscan.pdf", rutadigita + separador + "manual jscan.pdf");

        if (!utilidades.existeFichero(fichero)) {
            utilidades.crearFichero(fichero, "xml");
            utilidades.escribeFichero(fichero, "<procesos>");
            utilidades.escribeFichero(fichero, "</procesos>");
        }

        fichero = rutadigita + separador + "escaner.ini";
        if (!utilidades.existeFichero(fichero)) {
            utilidades.crearFichero(fichero, "texto");
            Properties props = new Properties();
            props.setProperty("Escaner", "");
            props.setProperty("Resolucion", "");
            props.setProperty("Duplex", "N");
            props.setProperty("Color", "");
            props.setProperty("Rotada", "N");
            props.setProperty("RutaImportar", "");
            props.setProperty("Brillo", "");
            props.setProperty("Contraste", "");
            props.setProperty("Maximizar", "N");

            utilidades.escribirPropeties(fichero, props);
        }

        propiniescaner = utilidades.leerPropeties(fichero);

        nombreEscaner.setText(propiniescaner.getProperty("Escaner"));
        checkDuplex.setSelected((propiniescaner.getProperty("Duplex").equals("S") ? true : false));

        cargarEscaner();

        if (!nombreEscaner.getText().isEmpty()) {
            configurarEscaner(nombreEscaner.getText());
        }

        int posicion = 0;
        for (int i = 0; i < comboColor.getModel().getSize(); i++) {
            if (comboColor.getModel().getElementAt(i).equals(propiniescaner.getProperty("Color"))) {
                posicion = i;
                break;
            }
        }
        comboColor.setSelectedIndex(posicion);

        posicion = 0;
        for (int i = 0; i < comboResolucion.getModel().getSize(); i++) {
            if (comboResolucion.getModel().getElementAt(i).equals(propiniescaner.getProperty("Resolucion"))) {
                posicion = i;
                break;
            }
        }
        comboResolucion.setSelectedIndex(posicion);

        sliderBrillo.setValue(Integer.parseInt(propiniescaner.getProperty("Brillo").equals("") ? "0" : propiniescaner.getProperty("Brillo")));
        sliderContraste.setValue(Integer.parseInt(propiniescaner.getProperty("Contraste").equals("") ? "0" : propiniescaner.getProperty("Contraste")));

        if (propiniescaner.getProperty("Maximizar") == null) {
            propiniescaner.setProperty("Maximizar", "N");
            utilidades.escribirPropeties(fichero, propiniescaner);
        }

        if (propiniescaner.getProperty("Maximizar").equals("S")) {
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            this.getRootPane().setDefaultButton(botonDigitalizar);
            opcionMaximizar.setSelected(true);
        } else {
            opcionMaximizar.setSelected(false);
        }
        botonDigitalizar.requestFocus();

        EscuchadorMensajes escuchamensa = new EscuchadorMensajes();

    }

    @Action
    public boolean cargarEscaner() {
        ImageIO.scanForPlugins();
        scanner = Scanner.getDevice();
        if (scanner != null) {
            try {
                listaseleccion = scanner.getDeviceNames();
            } catch (Exception ex) {
                Utilidades.escribeLog("No se ha podido cargar la lista de Escáneres - Error " + ex.getMessage());
            }
            return true;
        }
        return false;
    }

    @Action
    private void selecionarEscaner() {
        titulolista = "Seleccionar Escaner";
        PantallaSeleccion pantallaescaner = new PantallaSeleccion(this, true);
        miescaner = PantallaSeleccion.getValor();
        if (DEBUG) {
            Utilidades.escribeLog("Mi escaner: " + miescaner);
        }
        nombreEscaner.setText(miescaner);
        String fichero = rutadigita + separador + "escaner.ini";
        propiniescaner.setProperty("Escaner", miescaner);
        utilidades.escribirPropeties(fichero, propiniescaner);
        if (!rutalote.equals("")) {
            proplote = utilidades.leerPropeties(rutalote + separador + "lote.ini");
            proplote.setProperty("Escaner", miescaner);
            utilidades.escribirPropeties(rutalote + separador + "lote.ini", proplote);
        }
        if (!nombreEscaner.getText().isEmpty()) {
            configurarEscaner(nombreEscaner.getText());
        }
    }

    @Action
    private void seleccionarResolucion() {
        String fichero = rutadigita + separador + "escaner.ini";
        propiniescaner.setProperty("Resolucion", comboResolucion.getSelectedItem().toString());
        utilidades.escribirPropeties(fichero, propiniescaner);
        if (!rutalote.equals("")) {
            proplote = utilidades.leerPropeties(rutalote + separador + "lote.ini");
            proplote.setProperty("Resolucion", comboResolucion.getSelectedItem().toString());
            utilidades.escribirPropeties(rutalote + separador + "lote.ini", proplote);
        }
    }

    @Action
    private void seleccionarColor() {
        String fichero = rutadigita + separador + "escaner.ini";
        propiniescaner.setProperty("Color", comboColor.getSelectedItem().toString());
        utilidades.escribirPropeties(fichero, propiniescaner);
        if (!rutalote.equals("")) {
            proplote = utilidades.leerPropeties(rutalote + separador + "lote.ini");
            proplote.setProperty("Color", comboColor.getSelectedItem().toString());
            utilidades.escribirPropeties(rutalote + separador + "lote.ini", proplote);
        }
    }

    @Action
    private void seleccionarDuplex() {
        String fichero = rutadigita + separador + "escaner.ini";
        try {
            propiniescaner.setProperty("Duplex", checkDuplex.isSelected() ? "S" : "N");
        } catch (Exception ex) {
        }
        utilidades.escribirPropeties(fichero, propiniescaner);
        if (!rutalote.equals("")) {
            proplote = utilidades.leerPropeties(rutalote + separador + "lote.ini");
            proplote.setProperty("Duplex", checkDuplex.isSelected() ? "S" : "N");
            utilidades.escribirPropeties(rutalote + separador + "lote.ini", proplote);
        }
    }

    @Action
    private void seleccionarPagRotada() {
        String fichero = rutadigita + separador + "escaner.ini";
        utilidades.escribirPropeties(fichero, propiniescaner);

        if (!rutalote.equals("")) {
            proplote = utilidades.leerPropeties(rutalote + separador + "lote.ini");

            utilidades.escribirPropeties(rutalote + separador + "lote.ini", proplote);
        }
    }

    @Action
    private void seleccionarBrillo() {
        String fichero = rutadigita + separador + "escaner.ini";
        propiniescaner.setProperty("Brillo", "" + sliderBrillo.getValue());
        utilidades.escribirPropeties(fichero, propiniescaner);

        if (!rutalote.equals("")) {
            proplote = utilidades.leerPropeties(rutalote + separador + "lote.ini");
            proplote.setProperty("Brillo", "" + sliderBrillo.getValue());
            utilidades.escribirPropeties(rutalote + separador + "lote.ini", proplote);
        }
    }

    @Action
    private void seleccionarContraste() {
        String fichero = rutadigita + separador + "escaner.ini";
        propiniescaner.setProperty("Contraste", "" + sliderContraste.getValue());
        utilidades.escribirPropeties(fichero, propiniescaner);

        if (!rutalote.equals("")) {
            proplote = utilidades.leerPropeties(rutalote + separador + "lote.ini");
            proplote.setProperty("Contraste", "" + sliderContraste.getValue());
            utilidades.escribirPropeties(rutalote + separador + "lote.ini", proplote);
        }
    }

    @Action
    private void configurarEscaner(String escaner) {
        scanner = Scanner.getDevice();
        try {
            scanner.select(escaner);
            escanerconfig = true;
            scanner.addListener(this);
            scanner.acquire();

            String fichero = rutadigita + separador + "escaner.ini";
            propiniescaner = utilidades.leerPropeties(fichero);
            int valor = Integer.parseInt(propiniescaner.getProperty("Contraste") == null ? "0" : propiniescaner.getProperty("Contraste"));
            int cont = 0;
            while (contraste == null && cont < 1000) {
                Thread.currentThread().sleep(20);
                cont++;
            }

            sliderContraste.setMaximum(contraste.getItems().length - 1);
            sliderContraste.setMinimum(0);

            if (valor > 0 && valor < contraste.getItems().length - 1) {
                sliderContraste.setValue(valor);
            } else {
                sliderContraste.setValue((contraste.getItems().length - 1) / 2);
            }
            sliderContraste.setSnapToTicks(true);
//            Hashtable etiquetas = new Hashtable();
//            etiquetas.put(new Integer(0), new JLabel("Mín"));
//            etiquetas.put(new Integer((contraste.getItems().length - 1) / 2), new JLabel("0"));
//            etiquetas.put(new Integer(contraste.getItems().length - 1), new JLabel("Máx"));
//            sliderContraste.setLabelTable(etiquetas);
//            sliderContraste.setPaintLabels(true);
            valor = Integer.parseInt(propiniescaner.getProperty("Brillo") == null ? "0" : propiniescaner.getProperty("Brillo"));
            cont = 0;
            while (brillo == null && cont < 1000) {
                Thread.currentThread().sleep(20);
                cont++;
            }

            sliderBrillo.setMaximum(brillo.getItems().length - 1);
            sliderBrillo.setMinimum(0);

            if (valor > 0 && valor < brillo.getItems().length - 1) {
                sliderBrillo.setValue(valor);
            } else {
                sliderBrillo.setValue((brillo.getItems().length - 1) / 2);
            }

            sliderBrillo.setSnapToTicks(true);
//            Hashtable etiquetaBrillo = new Hashtable();
//            etiquetaBrillo.put(new Integer(0), new JLabel("Mín"));
//            etiquetaBrillo.put(new Integer((brillo.getItems().length - 1) / 2), new JLabel("0"));
//            etiquetaBrillo.put(new Integer(brillo.getItems().length - 1), new JLabel("Máx"));
//            sliderBrillo.setLabelTable(etiquetaBrillo);
//            sliderBrillo.setPaintLabels(true);
            panelEscaner.repaint();
        } catch (Exception ex) {
            Utilidades.escribeLog("Error al cargar configuración del escáner: " + ex.getMessage());
        }
    }

    @Action
    private void escanear() {
        String escaner = nombreEscaner.getText();
        if (escaner.isEmpty()) {
            return;
        }

        if (contimagen < 0) {
            contimagen = 0;
        }
        if (rutalote.equals("")) {
            crearLote();
        }

        scanner.isAPIInstalled();
        scanner = Scanner.getDevice();
        try {
            scanner.select(escaner);
            scanner.addListener(this);
            scanner.acquire();
            escaneando = true;
            mostrarBarraEscaner();
        } catch (ScannerIOException ex) {
            Utilidades.escribeLog("Error al escanear - " + ex.getMessage());
        }
    }
    PantallaBarra barraescaner = null;

    private void mostrarBarraEscaner() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                barraescaner = new PantallaBarra(PantallaPrincipal.this, false);
                barraescaner.setTitle("Escaneando ...");
                barraescaner.barra.setIndeterminate(true);
                barraescaner.botonParar.setVisible(true);
                barraescaner.setLabelMensa("");
                barraescaner.barra.setOpaque(true);
                barraescaner.barra.setStringPainted(false);
                barraescaner.validate();
            }
        });
    }

    @Override
    public void update(ScannerIOMetadata.Type type, ScannerIOMetadata metadata) {
        if (type.equals(ScannerIOMetadata.ACQUIRED)) {    // acquired BufferedImage
            java.awt.image.BufferedImage miimage = metadata.getImage();// make reference copy here to avoid race condition
            guardarImagen(miimage, contimagen);
            cargarMiniaturas(contimagen);
            panelVisorMini.repaint();
            repaint();
            pintarImagenPorIndice(contimagen);
            ponerBordeBoton(contimagen);
            contimagen++;

            if (barraescaner.PARAR) {
                //               metadata.setCancel(true);
                TwainSource source = ((TwainIOMetadata) metadata).getSource();
                source.setCancel(true);
                barraescaner.PARAR = false;
                barraescaner.botonParar.setVisible(false);
                barraescaner.setLabelMensa("Cancelando ...");
                barraescaner.validate();
            }


        } else if (type.equals(ScannerIOMetadata.FILE)) {  // acquired image as file (twain only for the time being)
            final File file = metadata.getFile();           // make reference copy here to avoid race condition
            new Thread() {
                public void run() {
                    try {
                        pantutil.open(file.getPath(), panelVisorImagen);
                    } catch (Exception e) {
                        Utilidades.escribeLog("9\b" + getClass().getName() + ".update:\n\t" + e);
                        System.err.println(getClass().getName() + ".update:\n\t" + e);
                        e.printStackTrace();
                    } finally {
                        if (!file.delete()) {
                            Utilidades.escribeLog("9\b" + getClass().getName() + ".update:\n\tCould not delete: " + file.getPath());
                            System.err.println(getClass().getName() + ".update:\n\tCould not delete: " + file.getPath());
                        }
                    }
                }
            }.start();
        } else if (type.equals(ScannerIOMetadata.NEGOTIATE)) {
            negotiate(metadata);
        } else if (type.equals(ScannerIOMetadata.STATECHANGE)) {
            // Cuando termina de escanear cierra la ventana de la barra de progreso
            if (escaneando && metadata.isFinished()) {
                escaneando = false;
                barraescaner.dispose();
            }

            if (DEBUG) {
                Utilidades.escribeLog("Scanner State " + metadata.getStateStr());
            }
            //    System.err.println("Scanner State " + metadata.getStateStr());

            if (metadata instanceof TwainIOMetadata) {                                // TWAIN only !
                if (metadata.isState(TwainConstants.STATE_TRANSFERREADY)) {             // state = 6
                    TwainSource source = ((TwainIOMetadata) metadata).getSource();
                    try {
                        TwainImageInfo imageInfo = new TwainImageInfo(source);
                        imageInfo.get();
                        if (DEBUG) {
                            Utilidades.escribeLog(imageInfo.toString());
                        }
                    } catch (Exception e) {
                        Utilidades.escribeLog("3\b" + getClass().getName() + ".update:\n\tCannot retrieve image information.\n\t" + e);
                    }
                    try {
                        TwainImageLayout imageLayout = new TwainImageLayout(source);
                        imageLayout.get();
                        //  Utilidades.escribeLog(imageLayout.toString());
                    } catch (Exception e) {
                        Utilidades.escribeLog("3\b" + getClass().getName() + ".update:\n\tCannot retrieve image layout.\n\t" + e);
                    }
                } else if (metadata.isState(TwainConstants.STATE_TRANSFERRING)) {       // state = 7
//        In state 4: supportTwainExtImageInfo=source.getCapability(TwainConstants.ICAP_EXTIMAGEINFO).booleanValue();
                    TwainSource source = ((TwainIOMetadata) metadata).getSource();
                    try {
                        int[] tweis = new int[0x1240 - 0x1200];
                        for (int i = 0; i < tweis.length; i++) {
                            tweis[i] = 0x1200 + i;
                        }

                        TwainExtImageInfo imageInfo = new TwainExtImageInfo(source, tweis);
                        imageInfo.get();
                        //  Utilidades.escribeLog(imageInfo.toString());
                    } catch (Exception e) {
                        Utilidades.escribeLog("3\b" + getClass().getName() + ".update:\n\tCannot retrieve extra image information.\n\t" + e);
                    }
                }
            }

        } else if (type.equals(ScannerIOMetadata.INFO)) {
            if (DEBUG) {
                Utilidades.escribeLog(metadata.getInfo());
            }
        } else if (type.equals(ScannerIOMetadata.EXCEPTION)) {
            if (DEBUG) {
                Utilidades.escribeLog("9\b" + metadata.getException().getMessage());
            }
            metadata.getException().printStackTrace();
        }

    }

    private void guardarImagen(BufferedImage pimagen, int conta) {
        String formato = "tif";
        String formatomin = "jpg";
        File fichero = new File(rutalote + separador + "Imagen" + conta + "." + formato);
        File ficheromin = new File(rutalote + separador + "Imagenmin" + conta + "." + formatomin);
        java.awt.image.BufferedImage imagenmin = (java.awt.image.BufferedImage) pimagen;
        // Escala de grises.
        int tipoimagen = pimagen.getType();
        java.awt.image.BufferedImage imagen = null;

        switch (tipoimagen) {
            case java.awt.image.BufferedImage.TYPE_INT_RGB:
            case java.awt.image.BufferedImage.TYPE_INT_ARGB:
            case java.awt.image.BufferedImage.TYPE_INT_ARGB_PRE:
            case java.awt.image.BufferedImage.TYPE_INT_BGR:
            case java.awt.image.BufferedImage.TYPE_3BYTE_BGR:
            case java.awt.image.BufferedImage.TYPE_4BYTE_ABGR:
            case java.awt.image.BufferedImage.TYPE_4BYTE_ABGR_PRE:
                imagen = pantutil.convertirAGris((java.awt.image.BufferedImage) pimagen);
                break;
            default:
                imagen = (java.awt.image.BufferedImage) pimagen;
        }

        if (pimagen.getWidth() < pimagen.getHeight()) {
            imagenmin = org.imgscalr.Scalr.resize(imagenmin, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, 90, 127);
        } else {
            imagenmin = org.imgscalr.Scalr.resize(imagenmin, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, 127, 90);
        }
        try {
            guardarTiff(fichero.getAbsolutePath(), imagen);
            ImageIO.write(imagenmin, formatomin, ficheromin);
        } catch (IOException e) {
            Utilidades.escribeLog("Error al guardar la imagen - " + e.getMessage());
        }
    }

    private void guardarTiff(String fichero, java.awt.image.BufferedImage imagen) {
        try {
            // TIFF Comprimido
            int tipoimagen = imagen.getType();
            File file = new File(fichero);
            ImageOutputStream ios = ImageIO.createImageOutputStream(file);
            ImageWriter writer = ImageIO.getImageWritersByFormatName("tiff").next();
            writer.setOutput(ios);
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(param.MODE_EXPLICIT);
            String compresion = "LZW";
            if (tipoimagen == java.awt.image.BufferedImage.TYPE_BYTE_BINARY) {
                compresion = "CCITT T.6";
            }
            param.setCompressionType(compresion);
            IIOImage img = new IIOImage(imagen, null, null);
            writer.write(null, img, param);
            ios.flush();
            ios.close();
            writer.dispose();
        } catch (IOException ex) {
            Utilidades.escribeLog("Error al guardar el fichero TIFF - " + ex.getMessage());
        }
    }

    private void guardarImagenModificada() {
        String formato = "tif";
        String formatomin = "jpg";
        File fichero = new File(rutalote + separador + "Imagen" + minisel + "." + formato);
        File ficheromin = new File(rutalote + separador + "Imagenmin" + minisel + "." + formatomin);

        java.awt.image.BufferedImage imagen = (java.awt.image.BufferedImage) pd.imgmemoria;
        java.awt.image.BufferedImage imagenmin = imagen;

        if (imagen.getWidth() < imagen.getHeight()) {
            imagenmin = org.imgscalr.Scalr.resize(imagenmin, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, 90, 127);
        } else {
            imagenmin = org.imgscalr.Scalr.resize(imagenmin, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, 127, 90);
        }
        try {
            guardarTiff(fichero.getAbsolutePath(), imagen);
            //ImageIO.write(imagen, formato, fichero);
            ImageIO.write(imagenmin, formatomin, ficheromin);
        } catch (IOException ex) {
            Utilidades.escribeLog("Error de escritura tras modificación " + ex.getMessage());
        }

        ImageIcon icono = new ImageIcon(rutalote + separador + "Imagenmin" + minisel + ".jpg");
        icono.getImage().flush();
        icono = new ImageIcon(rutalote + separador + "Imagenmin" + minisel + ".jpg");

        boton[minisel].setIcon(icono);
        boton[minisel].setBorder(null);
        boton[minisel].setBorderPainted(false);

        if (minisel > 0) {
            if (icono.getIconWidth() < icono.getIconHeight()) {
                boton[minisel].setBounds(35, boton[minisel - 1].getY() + boton[minisel - 1].getHeight() + 10, 90, 127);
            } else {
                boton[minisel].setBounds(20, boton[minisel - 1].getY() + boton[minisel - 1].getHeight() + 10, 127, 90);
            }

        } else {
            if (icono.getIconWidth() < icono.getIconHeight()) {
                boton[minisel].setBounds(35, 10, 90, 127);
            } else {
                boton[minisel].setBounds(20, 10, 127, 90);
            }
        }
        boton[minisel].repaint();
        panelMini.revalidate();
        pintarImagenPorIndice(minisel);
    }

    private void borrarImagen() {
        if (DEBUG) {
            Utilidades.escribeLog("Borrada la página " + minisel);
        }
        if (contimagen == 1) {  // Una sola imagen
            inicializar();
            utilidades.borrarDirectorio(rutalote + separador + "Imagen" + minisel + ".tif");
            utilidades.borrarDirectorio(rutalote + separador + "Imagenmin" + minisel + ".jpg");
            labelNumPaginas.setText("0");
            labelTotalPaginas.setText("0");
            return;
        }

        if (minisel == contimagen - 1) {  // La última imagen si hay más de 1
            contimagen--;
            utilidades.borrarDirectorio(rutalote + separador + "Imagen" + (contimagen) + ".tif");
            utilidades.borrarDirectorio(rutalote + separador + "Imagenmin" + (contimagen) + ".jpg");
            panelMini.remove(boton[minisel]);
            minisel--;
            boton = (JButton[]) utilidades.resizeArray(boton, contimagen);
            rutaboton = (String[]) utilidades.resizeArray(rutaboton, contimagen);
            pintarImagenPorIndice(minisel);
            pintarMiniaturaPorIndice(minisel);
            ponerBordeBoton(minisel);
            redimesionarPanelMini();
            panelMini.revalidate();
            panelMini.repaint();
            labelTotalPaginas.setText(boton.length + "");
            return;
        }

        contimagen--;
        panelMini.remove(boton[contimagen]);
        panelMini.revalidate();
        boton = (JButton[]) utilidades.resizeArray(boton, contimagen);
        rutaboton = (String[]) utilidades.resizeArray(rutaboton, contimagen);
        for (int i = minisel; i < contimagen; i++) {
            utilidades.copiarFichero(rutalote + separador + "Imagen" + (i + 1) + ".tif", rutalote + separador + "Imagen" + i + ".tif");
            utilidades.copiarFichero(rutalote + separador + "Imagenmin" + (i + 1) + ".jpg", rutalote + separador + "Imagenmin" + i + ".jpg");
            ImageIcon icono = new ImageIcon(rutalote + separador + "Imagenmin" + i + ".jpg");
            icono.getImage().flush();
            icono = new ImageIcon(rutalote + separador + "Imagenmin" + i + ".jpg");
            boton[i].setIcon(icono);
            panelMini.revalidate();
        }

        utilidades.borrarDirectorio(rutalote + separador + "Imagen" + (contimagen) + ".tif");
        utilidades.borrarDirectorio(rutalote + separador + "Imagenmin" + (contimagen) + ".jpg");

        panelMini.validate();

        if (minisel >= 1) {
            pintarImagenPorIndice(minisel - 1);
            pintarMiniaturaPorIndice(minisel - 1);
            ponerBordeBoton(minisel - 1);
        }
        if (minisel <= contimagen) {
            pintarImagenPorIndice(minisel);
            pintarMiniaturaPorIndice(minisel);
            ponerBordeBoton(minisel);
        }

//        int tam = 0;
//        for (int n = 0; n < boton.length; n++) {
//            tam = tam + boton[n].getHeight() + 10;
//        }
//        if (panelMini.getHeight() < tam) {
//            panelMini.setPreferredSize(new java.awt.Dimension(panelMini.getWidth(), panelMini.getHeight() + boton[contimagen].getHeight()));
//            panelMini.setSize(new java.awt.Dimension(panelMini.getWidth(), panelMini.getHeight() + boton[contimagen].getHeight() + 10));
//            panelMini.repaint();
//        }
        redimesionarPanelMini();
        panelMini.revalidate();
        labelTotalPaginas.setText(boton.length + "");
    }

    @Action
    private boolean comprobarValores() {
        boolean valor = true;

        if (textoDirectorio.getText().isEmpty()) {
            utilidades.mensaje(this, "Directorio  de destino", "Debe indicar el directorio de destino");
            botonDirectorio.requestFocus();
            return false;
        }

        if (textoFichero.getText().isEmpty()) {
            utilidades.mensaje(this, "Nombre de Fichero", "Debe indicar el nombre del fichero de destino");
            textoFichero.requestFocus();
            return false;
        }


        return valor;
    }

    private void guardarLote() {
        if (lote.isEmpty()) {
            crearLote();
        }

        proplote = utilidades.leerPropeties(utilidades.dirBase() + utilidades.separador() + "lotes" + utilidades.separador() + lote + utilidades.separador() + "lote.ini");
        proplote.setProperty("numpaginas", String.valueOf(contimagen));
        utilidades.escribirPropeties(rutalote + separador + "lote.ini", proplote);
    }

    @Action
    public Boolean guardarPdf() {
        if (contimagen < 1) {
            return false;
        }

        final PantallaBarra pantbarra = new PantallaBarra(PantallaPrincipal.this, false);
        pantbarra.setTitle("Generando fichero de destino");
        pantbarra.botonParar.setVisible(false);
        errorpdf = false;

        new Thread() {
            @Override
            public void run() {
                Document pdfDocument = new Document();
//                Document pdfDocument = new Document(PageSize.A4, 0, 0, 0, 0);
                Calendar cal = Calendar.getInstance();
                String anio = String.valueOf(cal.get(Calendar.YEAR));
                String mes = String.valueOf((cal.get(Calendar.MONTH) + 1)).length() == 1 ? "0" + String.valueOf((cal.get(Calendar.MONTH) + 1)) : String.valueOf((cal.get(Calendar.MONTH) + 1));
                String dia = String.valueOf(cal.get(Calendar.DAY_OF_MONTH)).length() == 1 ? "0" + String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) : String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
                String hora = String.valueOf(cal.get(Calendar.HOUR_OF_DAY)).length() == 1 ? "0" + String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) : String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
                String minuto = String.valueOf(cal.get(Calendar.MINUTE)).length() == 1 ? "0" + String.valueOf(cal.get(Calendar.MINUTE)) : String.valueOf(cal.get(Calendar.MINUTE));
                String segundo = String.valueOf(cal.get(Calendar.SECOND)).length() == 1 ? "0" + String.valueOf(cal.get(Calendar.SECOND)) : String.valueOf(cal.get(Calendar.SECOND));

                try {
                    if (DEBUG) {
                        Utilidades.escribeLog("Generando PDFs -guardarPdf-");
                    }
                    if (contimagen < 2) {
                        pantbarra.barra.setMinimum(1);
                        pantbarra.barra.setMaximum(2);
                    } else {
                        pantbarra.barra.setMinimum(1);
                        pantbarra.barra.setMaximum(rutaboton.length);
                    }
                    pantbarra.barra.setValue(1);
                    pantbarra.setTitle(pantbarra.getTitle());
                    pantbarra.validate();


                    String nombrefichero = "Documentacion.pdf";
                    String ruta = rutalote + separador + nombrefichero + ".pdf";

                    if (!textoFichero.getText().isEmpty()) {
                        nombrefichero = textoFichero.getText() + ".pdf";
                    }

                    if (!textoDirectorio.getText().isEmpty()) {
                        ruta = textoDirectorio.getText();
                        ruta = ruta + separador + nombrefichero;
                    }

                    FileOutputStream ficheroPdf = new FileOutputStream(ruta);
                    File filename = new File(rutaboton[0].toString());
                    java.awt.image.BufferedImage imagen = javax.imageio.ImageIO.read(filename);
                    com.itextpdf.text.Image imagenpdf = com.itextpdf.text.Image.getInstance(imagen, null);
                    pdfDocument.setPageSize(new Rectangle(imagenpdf.getWidth(), imagenpdf.getHeight()));
                    PdfWriter writer = PdfWriter.getInstance(pdfDocument, ficheroPdf);
                    writer.open();
                    pdfDocument.open();
                    pdfDocument.addHeader("IP", lote.substring(16, 19) + "." + lote.substring(19, 22) + "." + lote.substring(22, 25) + "." + lote.substring(25, 28));
                    pdfDocument.addHeader("fechadigita", lote.substring(6, 8) + "/" + lote.substring(4, 6) + "/" + lote.substring(0, 4)
                            + " " + lote.substring(9, 11) + ":" + lote.substring(11, 13) + ":" + lote.substring(13, 15));
                    pdfDocument.addHeader("fechacreacion", dia + "/" + mes + "/" + anio + " " + hora + ":" + minuto + ":" + segundo);

                    for (int i = 0; i < rutaboton.length; i++) {
                        filename = new File(rutaboton[i].toString());
                        imagen = javax.imageio.ImageIO.read(filename);
                        imagenpdf = com.itextpdf.text.Image.getInstance(imagen, null);
//                        imagenpdf.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
//                        imagenpdf.setAlignment(com.itextpdf.text.Image.ALIGN_JUSTIFIED_ALL);
//                        com.itextpdf.text.Image instance = com.itextpdf.text.Image.getInstance(imagenpdf);
//                        pdfDocument.setPageSize(new com.itextpdf.text.Rectangle(PageSize.A4.getWidth(), PageSize.A4.getHeight()));
                        pantbarra.barra.setValue(i);
                        pantbarra.setTitle(titulo + "     " + (i + 1) + " de " + rutaboton.length);
                        pdfDocument.setPageSize(new Rectangle(imagenpdf.getWidth(), imagenpdf.getHeight()));
//                        pdfDocument.add(instance);
                        pdfDocument.add(imagenpdf);
                        pdfDocument.newPage();
                        pantbarra.validate();
                    }
                    pdfDocument.close();
                    writer.close();
                } catch (Exception e) {
                    Utilidades.escribeLog("Error Generando PDFs -guardarPdf- " + e.getMessage());
                    errorpdf = true;
                }
                pantbarra.dispose();
            }
        }.start();
        pantbarra.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        pantbarra.setVisible(false);
        pantbarra.setVisible(true);
        return !errorpdf;
    }

    private void tiffToPdf(String origen, String destino) {
        try {
            //Read the Tiff File
            RandomAccessFileOrArray myTiffFile = new RandomAccessFileOrArray(origen);
            //Find number of images in Tiff file
            int numberOfPages = TiffImage.getNumberOfPages(myTiffFile);
            //  System.out.println("Number of Images in Tiff File" + numberOfPages);
            com.itextpdf.text.Image tempImage = TiffImage.getTiffImage(myTiffFile, 1);
            Document TifftoPDF = new Document();
            TifftoPDF.setPageSize(new Rectangle(tempImage.getWidth(), tempImage.getHeight()));
            PdfWriter.getInstance(TifftoPDF, new FileOutputStream(destino));
            TifftoPDF.open();
            for (int i = 1; i <= numberOfPages; i++) {
                tempImage = TiffImage.getTiffImage(myTiffFile, i);
                TifftoPDF.setPageSize(new Rectangle(tempImage.getWidth(), tempImage.getHeight()));
                TifftoPDF.add(tempImage);
            }
            TifftoPDF.close();
        } catch (Exception ex) {
            Utilidades.escribeLog("Error al convertir de Tiff a PDF -tiffToPdf- Error " + ex.getMessage());
        }

    }

    private Boolean guardarImagenTipo(String tipo) {
        String nombrefichero = "Documentacion." + tipo.toLowerCase();
        String ruta = rutalote + separador + nombrefichero + "." + tipo.toLowerCase();

        if (!textoFichero.getText().isEmpty()) {
            nombrefichero = textoFichero.getText();
        }

        if (!textoDirectorio.getText().isEmpty()) {
            ruta = textoDirectorio.getText();
            ruta = ruta + separador + nombrefichero + "." + tipo.toLowerCase();
        }
        try {
            File filename = new File(rutaboton[minisel].toString());
            java.awt.image.BufferedImage imagen = javax.imageio.ImageIO.read(filename);
            File fichero = new File(ruta);
            ImageIO.write(imagen, tipo, fichero);
        } catch (Exception ex) {
        }
        return true;
    }

    private Boolean guardarTiff() {
        if (contimagen < 1) {
            return false;
        }

        final PantallaBarra pantbarra = new PantallaBarra(PantallaPrincipal.this, false);
        pantbarra.setTitle("Generando fichero de destino");
        pantbarra.botonParar.setVisible(false);
        errortiff = false;

        new Thread() {
            @Override
            public void run() {
                try {
                    if (contimagen < 2) {
                        pantbarra.barra.setMinimum(1);
                        pantbarra.barra.setMaximum(2);
                    } else {
                        pantbarra.barra.setMinimum(1);
                        pantbarra.barra.setMaximum(rutaboton.length);
                    }
                    pantbarra.barra.setValue(1);
                    pantbarra.setTitle(pantbarra.getTitle());
                    pantbarra.validate();
                    // TIFF Multipágina Comprimido
                    String nombrefichero = "Documentacion.tiff";
                    String ruta = rutalote + separador + nombrefichero + ".tiff";

                    if (!textoFichero.getText().isEmpty()) {
                        nombrefichero = textoFichero.getText();
                    }

                    if (!textoDirectorio.getText().isEmpty()) {
                        ruta = textoDirectorio.getText();
                        ruta = ruta + separador + nombrefichero + ".tiff";
                    }
                    File file = new File(ruta);
                    ImageOutputStream ios = ImageIO.createImageOutputStream(file);
                    ImageWriter writer = ImageIO.getImageWritersByFormatName("tiff").next();
                    writer.setOutput(ios);
                    for (int i = 0; i < rutaboton.length; i++) {
                        File filename = new File(rutaboton[i].toString());
                        java.awt.image.BufferedImage imagen = javax.imageio.ImageIO.read(filename);
                        ImageWriteParam param = writer.getDefaultWriteParam();
                        param.setCompressionMode(param.MODE_EXPLICIT);
                        String compresion = "LZW";
                        int tipoimagen = imagen.getType();
                        if (tipoimagen == java.awt.image.BufferedImage.TYPE_BYTE_BINARY) {
                            compresion = "CCITT T.6";
                        }
                        param.setCompressionType(compresion);
                        IIOImage img = new IIOImage(imagen, null, null);
                        if (i == 0) {
                            writer.write(null, img, param);
                        } else {
                            writer.writeInsert(-1, img, param);
                        }
                        pantbarra.barra.setValue(i);
                        pantbarra.setTitle(titulo + "     " + (i + 1) + " de " + rutaboton.length);
                        pantbarra.validate();
                    }
                    ios.flush();
                    ios.close();
                    writer.dispose();
                    //tiffToPdf(ruta, ruta.replace(".tiff", ".pdf"));
                } catch (IOException ex) {
                    Utilidades.escribeLog("Error al guardar el fichero TIFF multipagina  - " + ex.getMessage());
                    errortiff = false;
                }

                pantbarra.dispose();
            }
        }.start();
        pantbarra.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        pantbarra.setVisible(false);
        pantbarra.setVisible(true);



        return errortiff;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }

    public void negotiate(ScannerIOMetadata metadata) {

        /*
         * ScannerDevice sd=metadata.getDevice(); // SANE & TWAIN try{ //
         * sd.setShowUserInterface(false); // sd.setShowProgressBar(false); //
         * Twain: works only if user interface is inactive
         * sd.setResolution(100.0); // set resolution in dots per inch //
         * sd.setRegionOfInterest(20,40,600,400); // set region of interest to
         * x,y,width,height in pixels //
         * sd.setRegionOfInterest(20.5,45.0,66.66,40.0); // set region of
         * interest to x,y,width,height in millimeters }catch(Exception e){
         * Utilidades.escribeLog("9\b"+e.getMessage()); //
         * metadata.setCancel(true); // cancel scan if we can't set it up our
         * way }
         */

        if (metadata instanceof TwainIOMetadata) { // TWAIN only!
            TwainSource source = ((TwainIOMetadata) metadata).getSource();
            try {
//                TwainCapability[] caps = source.getCapabilities();

                if (escanerconfig) {
                    contraste = source.getCapability(TwainConstants.ICAP_CONTRAST, 4);
                    brillo = source.getCapability(TwainConstants.ICAP_BRIGHTNESS, 4);

                    resolucion = source.getCapability(TwainConstants.ICAP_XRESOLUTION);

                    Vector comboBoxItems = new Vector();
//                    for (int v = 0; v < resolucion.getItems().length; v++) {
//                        String valor = resolucion.getItems()[v].toString();
//                        double d = Double.parseDouble(valor);
//                        int i = (int) d;
//
//                        Utilidades.escribeLog("" + i);
//
//                        comboBoxItems.add("" + i);
//                    }
                    comboBoxItems.add("200");
                    comboBoxItems.add("400");
                    comboBoxItems.add("600");

                    final DefaultComboBoxModel model = new DefaultComboBoxModel(comboBoxItems);
                    comboResolucion.setModel(model);

                    escanerconfig = false;
                    source.setShowProgressBar(false);
                    source.setShowUI(false);
                    source.setCancel(true);
                    return;
                }

//                TwainCapability alimentador = source.getCapability(TwainConstants.CAP_FEEDERENABLED);
//                  resolucion = source.getCapability(TwainConstants.ICAP_AUTODISCARDBLANKPAGES);
//                resolucion = source.getCapability(TwainConstants.ICAP_COMPRESSION);
//
//                for (int v = 0; v < resolucion.getItems().length; v++) {
//                    String valor = resolucion.getItems()[v].toString();
//                   Utilidades.escribeLog("Compression - " + valor);
//                }

//                brillo = source.getCapability(TwainConstants.ICAP_BRIGHTNESS);
//                for (int v = 0; v < brillo.getItems().length; v++) {
//                    String valor = brillo.getItems()[v].toString();
//                    Utilidades.escribeLog("" + valor);
//                }


            } catch (Exception ex) {
                Utilidades.escribeLog("Error en 'negotiate' - " + ex.getMessage());
            }

            aplicarConfiguracionEscaner(source);

            /*
             * String[] names=TwainIdentity.getProductNames(); // let's see what
             * data sources we have for(int i=0;i<names.length;i++){
             * Utilidades.escribeLog(names[i]); }
             *
             * TwainIdentity[] list=TwainIdentity.getIdentities(); for(int
             * i=0;i<list.length;i++){
             * Utilidades.escribeLog(list[i].toString()); }
             */

            /*
             * try { TwainCapability[] caps = source.getCapabilities(); // print
             * out // all the // capabilities for (int i = 0; i < caps.length;
             * i++) { Utilidades.escribeLog(caps[i].toString()); } } catch
             * (Exception e) { Utilidades.escribeLog("9\b" + e.getMessage()); }
             *
             */



            /*
             * // use automatic document feeder, scan 5 pages try{ int
             * transferCount=5;
             * source.setCapability(TwainConstants.CAP_FEEDERENABLED,true);
             * source.setCapability(TwainConstants.CAP_AUTOFEED,true);
             *
             * // source.setCapability(TwainConstants.CAP_AUTOSCAN,true); //
             * System
             * .err.println(source.getCapability(TwainConstants.CAP_AUTOSCAN
             * ).toString());
             *
             * source.setCapability(TwainConstants.CAP_XFERCOUNT,transferCount);
             * transferCount
             * =source.getCapability(TwainConstants.CAP_XFERCOUNT).intValue();
             * System.err.println("set transferCount: "+transferCount);
             * }catch(Exception e){ Utilidades.escribeLog(
             * "9\bCAP_FEEDERENABLED/CAP_AUTOFEED/CAP_XFERCOUNT: "
             * +e.getMessage()); // metadata.setCancel(true); // negotiation
             * failed let's try to abort scan }
             */
            /*
             * try{ // source.setXferMech(TwainConstants.TWSX_NATIVE); // send
             * image as BufferedImage (default)
             *
             * // source.setXferMech(TwainConstants.TWSX_MEMORY); // send image
             * as byte[] blocks (not implemented here yet)
             *
             * source.setXferMech(TwainConstants.TWSX_FILE); // send image as
             * file // source.setImageFileFormat(TwainConstants.TWFF_BMP); //
             * set file format to bmp (must be supported by all sources)
             * source.setImageFileFormat(TwainConstants.TWFF_JFIF); // set file
             * format to jpeg
             *
             * // source.setImageFileFormat(TwainConstants.TWFF_TIFF); // set
             * file format to tiff if supported otherwise use last settings //
             * i.e. source's default. The default does not have to be bmp!
             * Utilidades.escribeLog();
             * Utilidades.escribeLog(source.getCapability(TwainConstants
             * .ICAP_XFERMECH).toString());
             * Utilidades.escribeLog(source.getCapability
             * (TwainConstants.ICAP_IMAGEFILEFORMAT).toString());
             * Utilidades.escribeLog(); }catch(Exception e){
             * Utilidades.escribeLog("9\bTransfer Mechanism : "+e.getMessage());
             * }
             */
            /*
             * try{ // set Black/White aka Lineart
             * source.setCapability(TwainConstants
             * .ICAP_PIXELTYPE,TwainConstants.TWPT_BW);
             * Utilidades.escribeLog(source
             * .getCapability(TwainConstants.ICAP_PIXELTYPE).toString());
             * }catch(Exception e){ Utilidades.escribeLog("9\bPixel Type:
             * "+e.getMessage()); }
             */
            /*
             * try{ int TWSS_NONE = 0; int TWSS_A4LETTER = 1; int TWSS_USLETTER
             * = 3; int TWSS_USLEGAL = 4;
             *
             * TwainCapability
             * tc=source.getCapability(TwainConstants.ICAP_SUPPORTEDSIZES);
             * if(tc.querySupport(TwainConstants.TWQC_SET)){ // is set operation
             * allowed Utilidades.escribeLog(tc.toString());
             * tc.setCurrentValue(TWSS_A4LETTER);
             * Utilidades.escribeLog(source.getCapability
             * (TwainConstants.ICAP_SUPPORTEDSIZES).toString()); }
             * }catch(Exception e){ Utilidades.escribeLog("9\bPaper Sizes:
             * "+e.getMessage()); }
             */

        }
        /*
         * if(metadata instanceof TwainIOMetadata){ // TWAIN only! TwainSource
         * source=((TwainIOMetadata)metadata).getSource(); try{ // set
         * Black/White aka Lineart
         * source.setCapability(TwainConstants.ICAP_PIXELTYPE
         * ,TwainConstants.TWPT_BW);
         * Utilidades.escribeLog(source.getCapability(TwainConstants
         * .ICAP_PIXELTYPE).toString());
         *
         * source.setCapability(TwainConstants.ICAP_THRESHOLD,40.0);
         * Utilidades.escribeLog
         * (source.getCapability(TwainConstants.ICAP_THRESHOLD).toString());
         * }catch(Exception e){ Utilidades.escribeLog("9\bPixel Type:
         * "+e.getMessage()); } }
         */
        /*
         * if(metadata instanceof TwainIOMetadata){ // TWAIN only! TwainSource
         * source=((TwainIOMetadata)metadata).getSource(); try{
         * source.setCapability
         * (TwainConstants.ICAP_PIXELTYPE,TwainConstants.TWPT_GRAY);
         * Utilidades.escribeLog
         * (source.getCapability(TwainConstants.ICAP_PIXELTYPE).toString());
         * source.setCapability(TwainConstants.ICAP_AUTOBRIGHT,false);
         * System.out
         * .println(source.getCapability(TwainConstants.ICAP_AUTOBRIGHT
         * ).toString());
         * source.setCapability(TwainConstants.ICAP_BRIGHTNESS,-800.0);
         * System.out
         * .println(source.getCapability(TwainConstants.ICAP_BRIGHTNESS
         * ).toString()); //
         * source.setCapability(TwainConstants.ICAP_CONTRAST,500); //
         * System.out.
         * println(source.getCapability(TwainConstants.ICAP_CONTRAST).
         * toString()); }catch(Exception e){ Utilidades.escribeLog("9\bPixel
         * Type: "+e.getMessage()); } }
         */
    }

    public void importarImagenes() {
        try {
            String fichero = rutadigita + separador + "escaner.ini";
            propiniescaner = utilidades.leerPropeties(fichero);

            String ruta = propiniescaner.getProperty("RutaImportar");
            JFileChooser fc = new JFileChooser(ruta);

            fc.addChoosableFileFilter(new ExtensionFileFilter(new String[]{".GIF", ".JPG", ".JPEG", ".BMP", ".PNG", ".TIF", ".TIFF", ".PDF"}, "Imagenes (*.GIF|JPG|JPEG|BMP|PNG|TIF|TIFF|PDF)"));
            //fc.addChoosableFileFilter(new ExtensionFileFilter(new String[]{".PDF"}, "Ficheros PDF (*.PDF)"));
            //     fc.addChoosableFileFilter(new ExtensionFileFilter(null, null));
            fc.setAcceptAllFileFilterUsed(false);

            int x = fc.showOpenDialog(this);
            if (x == JFileChooser.CANCEL_OPTION || x == JFileChooser.ERROR_OPTION) {
                return;
            }

            File fileImagen = fc.getSelectedFile();
            ruta = fc.getSelectedFile().getParent();
            propiniescaner.setProperty("RutaImportar", ruta);
            utilidades.escribirPropeties(fichero, propiniescaner);

            String extension = fileImagen.getName().substring(fileImagen.getName().lastIndexOf('.') + 1, fileImagen.getName().length()).toLowerCase();
            //           fc = null;

            if (rutalote.equals("")) {
                crearLote();
            }
            desdemin = false;

            if (extension.equals("pdf")) {
                importarDePdf(fileImagen);

            } else if (extension.endsWith("jpg") || extension.endsWith("gif") || extension.endsWith("jpeg") || extension.endsWith("bmp") || extension.endsWith("png")) {
                java.awt.image.BufferedImage imagen = javax.imageio.ImageIO.read(fileImagen);
                if (fileImagen != null) {
                    imagen = pantutil.convertirAGris((java.awt.image.BufferedImage) imagen);
                    guardarImagen(imagen, contimagen);
                    pintarImagen(imagen);
                    cargarMiniaturas(contimagen);
                    ponerBordeBoton(contimagen);
                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            JScrollBar bar = panelVisorMini.getVerticalScrollBar();
                            bar.setValue(bar.getMaximum());
                        }
                    });
                    contimagen++;
                }
            } else if (extension.endsWith("tif") || extension.endsWith("tiff")) {
                importarTiff(fileImagen);
            }
        } catch (IOException ex) {
            Utilidades.escribeLog("Error en -importarImagenes- Importando Imágenes - " + ex.getMessage());

        }
    }

    public void importarDeFichero(String fichero) {
        try {
            File fileImagen = new File(fichero);
            String extension = fileImagen.getName().substring(fileImagen.getName().lastIndexOf('.') + 1, fileImagen.getName().length()).toLowerCase();

            if (rutalote.equals("")) {
                crearLote();
            }
            desdemin = false;

            if (extension.equals("pdf")) {
                importarDePdf(fileImagen);

            } else if (extension.endsWith("jpg") || extension.endsWith("gif") || extension.endsWith("jpeg") || extension.endsWith("bmp") || extension.endsWith("png")) {
                java.awt.image.BufferedImage imagen = javax.imageio.ImageIO.read(fileImagen);
                if (fileImagen != null) {
                    imagen = pantutil.convertirAGris((java.awt.image.BufferedImage) imagen);
                    guardarImagen(imagen, contimagen);
                    pintarImagen(imagen);
                    cargarMiniaturas(contimagen);
                    ponerBordeBoton(contimagen);
                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            JScrollBar bar = panelVisorMini.getVerticalScrollBar();
                            bar.setValue(bar.getMaximum());
                        }
                    });
                    contimagen++;
                }
            } else if (extension.endsWith("tif") || extension.endsWith("tiff")) {
                importarTiff(fileImagen);
            }
        } catch (IOException ex) {
            Utilidades.escribeLog("Error en -importarImagenDirecta- Importando Imágenes - " + ex.getMessage());

        }

    }

    public void importarTiff(final File archivo) {
        ImageIO.scanForPlugins();
        final PantallaBarra pantbarra = new PantallaBarra(this, false);

        new Thread() {
            @Override
            public void run() {
                int numpaginas = 0;
                try {
                    numpaginas = pantutil.numPaginasTIFF(archivo.getAbsolutePath());
                } catch (Exception ex) {
                    Utilidades.escribeLog("Error -importarTiff- al obtener el número de páginas del fichero TIFF " + archivo.getAbsolutePath() + " - " + ex.getMessage());
                }
                if (DEBUG) {
                    Utilidades.escribeLog("TIFF multipágina, número de páginas: " + numpaginas);
                }
                java.awt.image.BufferedImage tmpimagen = null;
                pantbarra.barra.setMinimum(1);
                pantbarra.barra.setMaximum(numpaginas);
                String titulo = pantbarra.getTitle();
                for (int i = 0; i < numpaginas; i++) {
                    try {
                        tmpimagen = (java.awt.image.BufferedImage) pantutil.loadImagenTIFF(archivo.getAbsolutePath(), i);
                        if (tmpimagen.getType() != BufferedImage.TYPE_BYTE_BINARY) {
                            tmpimagen = pantutil.convertirAGris((java.awt.image.BufferedImage) tmpimagen);
                        }
                    } catch (Exception ex) {
                        Utilidades.escribeLog("Error -importarTiff- al cargar imagen del fichero TIFF " + archivo.getAbsolutePath() + " - " + ex.getMessage());
                        pantbarra.dispose();
                    }
                    if (archivo != null) {
                        guardarImagen(tmpimagen, contimagen);
                        cargarMiniaturas(contimagen);
                        JScrollBar bar = panelVisorMini.getVerticalScrollBar();
                        bar.setValue(bar.getMaximum());
                        pantbarra.barra.setValue(i);
                        pantbarra.setTitle(titulo + "     " + (i + 1) + " de " + numpaginas);
                        contimagen++;
                    }
                    if (pantbarra.PARAR) {
                        break;
                    }
                }
                JScrollBar bar = panelVisorMini.getVerticalScrollBar();
                if (pantbarra.PARAR) {
                    pintarImagenPorIndice(0);
                    ponerBordeBoton(0);
                    bar.setValue(bar.getMinimum());
                } else {
                    pintarImagen(tmpimagen);
                    ponerBordeBoton(contimagen - 1);
                    bar.setValue(bar.getMaximum());
                }
                pantbarra.dispose();
            }
        }.start();
        pantbarra.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        pantbarra.setVisible(false);
        pantbarra.setVisible(true);
    }

    public void importarDePdf(final File archivo) {
        final PantallaBarra pantbarra = new PantallaBarra(PantallaPrincipal.this, false);

        new Thread() {
            @Override
            public void run() {
                org.apache.pdfbox.pdmodel.PDDocument documento = null;
                try {
                    documento = org.apache.pdfbox.pdmodel.PDDocument.load(archivo);
                } catch (IOException ex) {
                    Utilidades.escribeLog("Error -importarDePdf- al importar del archivo PDF " + archivo.getAbsolutePath() + " - " + ex.getMessage());
                    return;
                }
                int startpage = 1;
                int endpage = Integer.MAX_VALUE;
                List pages = documento.getDocumentCatalog().getAllPages();
                String titulo = pantbarra.getTitle();
                pantbarra.barra.setMinimum(startpage);
                pantbarra.barra.setMaximum(pages.size());
                pantbarra.barra.setValue(1);
                for (int i = startpage - 1; i < endpage && i < pages.size(); i++) {
                    PDPage page = (PDPage) pages.get(i);
                    java.awt.image.BufferedImage imagenpdf = null;
                    try {
                        imagenpdf = page.convertToImage(BufferedImage.TYPE_BYTE_GRAY, 200);
                    } catch (IOException ex) {
                        Utilidades.escribeLog("Error -importarDePdf- al convertir Imagen de PDF a Escala de Grises " + " - " + ex.getMessage());
                    }
                    //  Utilidades.escribeLog("Numero bits por pixel :"+imagenpdf.getColorModel().getPixelSize());

                    //   imagenpdf = pantutil.convertirAGris((java.awt.image.BufferedImage) imagenpdf);
                    guardarImagen(imagenpdf, contimagen);
                    cargarMiniaturas(contimagen);
                    JScrollBar bar = panelVisorMini.getVerticalScrollBar();
                    bar.setValue(bar.getMaximum());
                    pantbarra.barra.setValue(i);
                    pantbarra.setTitle(titulo + "     " + (i + 1) + " de " + pages.size());
                    contimagen++;
                    doLayout();
                    if (pantbarra.PARAR) {

                        break;
                    }
                }

                PDPage page = (PDPage) pages.get(pages.size() - 1);
                java.awt.image.BufferedImage imagenpdf = null;
                try {
                    imagenpdf = page.convertToImage();
                    imagenpdf = pantutil.convertirAGris((java.awt.image.BufferedImage) imagenpdf);
                    documento.close();
                } catch (IOException ex) {
                    Utilidades.escribeLog("Error -importarDePdf- al convertir Imagen de PDF a Escala de Grises " + " - " + ex.getMessage());
                }
                JScrollBar bar = panelVisorMini.getVerticalScrollBar();
                if (pantbarra.PARAR) {
                    pintarImagenPorIndice(0);
                    ponerBordeBoton(0);
                    bar.setValue(bar.getMinimum());
                } else {
                    pintarImagen(imagenpdf);
                    ponerBordeBoton(contimagen - 1);
                    bar.setValue(bar.getMaximum());
                }
                pantbarra.dispose();
            }
        }.start();
        pantbarra.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        pantbarra.setVisible(false);
        pantbarra.setVisible(true);
    }

    public void redimesionarPanelMini() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            }
        });
        int tam = 10;
        for (int n = 0; n < boton.length; n++) {
            tam = tam + boton[n].getHeight() + 10;
        }

        if (panelVisorMini.getHeight() < tam + 20) {
            panelMini.setPreferredSize(new java.awt.Dimension(panelMini.getWidth(), tam + 20));
            panelMini.setSize(new java.awt.Dimension(panelMini.getWidth(), tam + 20));
            panelMini.repaint();
        }
    }

    public void cargarMiniaturas(int conta) {
        boton = (JButton[]) utilidades.resizeArray(boton, conta + 1);
        boton[conta] = new JButton();
        rutaboton = (String[]) utilidades.resizeArray(rutaboton, conta + 1);
        rutaboton[conta] = new String();
        rutaboton[conta] = rutalote + separador + "Imagen" + conta + ".tif";
        String nombre = "" + conta;
        boton[conta].setActionCommand(nombre);
        //nombre = "boton" + contimagen;
        //   boton[contimagen].setText(nombre);

        boton[conta].setContentAreaFilled(false);

        boton[conta].setSize(127, 127);
        boton[conta].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    JButton evento = (JButton) evt.getSource();
                    //   Utilidades.escribeLog("apretado el boton " + evento.getActionCommand());
                    desdemin = true;
                    minisel = Integer.parseInt(evento.getActionCommand());
                    pintarImagenPorIndice(Integer.parseInt(evento.getActionCommand()));
                    ponerBordeBoton(Integer.parseInt(evento.getActionCommand()));
                    labelNumPaginas.setText(minisel + 1 + "");
                } catch (Exception ex) {
                    Utilidades.escribeLog("Error al cargar Miniaturas " + " - " + ex.getMessage());
                }
            }
        });
        minisel = conta;
        pintarMiniaturaPorIndice(conta);
        redimesionarPanelMini();
        labelNumPaginas.setText(conta + 1 + "");
        labelTotalPaginas.setText(boton.length + "");
    }

    private void quitarBordeBotones() {
        for (int n = 0; n < boton.length; n++) {
            boton[n].setVisible(false);
            boton[n].setBorder(null);
            boton[n].setBorderPainted(false);
            boton[n].setVisible(true);
            panelMini.validate();
        }
    }

    private void ponerBordeBoton(int n) {
        if (n < boton.length) {
            quitarBordeBotones();
            boton[n].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            boton[n].setBorderPainted(true);
        }
    }

    public void repintarPanelMini() {
        //   int i = 0;

        int y = 10;
        if (minisel > 0) {
            y = boton[minisel - 1].getY();
        }
        int largoh = 90;
        int largov = 127;
        int largo = 0;
        boolean unavez = true;
        int a = 0;

        while (a < contimagen && rutaboton[a].equals("borrado")) {
            a++;
        }
        int numero1 = a;

        for (int i = minisel + 1; i < contimagen; i++) {
            if (rutaboton[i].equals("borrado")) {
                continue;
            }
            if (minisel == numero1 && unavez) {
                if (boton[i].getWidth() < boton[i].getHeight()) {
                    boton[i].setBounds(35, 10, 90, 127);
                } else {
                    boton[i].setBounds(20, 10, 127, 90);
                }
                unavez = false;

            } else {
                if (boton[i].getWidth() < boton[i].getHeight()) {
                    largo = largo + largov;
                    boton[i].setBounds(35, y + largo + 10, 90, 127);

                } else {
                    largo = largo + largoh;
                    boton[i].setBounds(20, y + largo + 10, 127, 90);
                }
            }
            y = y + 5;
        }

    }

    public void pintarMiniaturaPorIndice(int cont) {
        ImageIcon icono = new ImageIcon(rutalote + separador + "Imagenmin" + cont + ".jpg");
        icono.getImage().flush();
        icono = new ImageIcon(rutalote + separador + "Imagenmin" + cont + ".jpg");

        boton[cont].setIcon(icono);
        boton[cont].setBorder(null);
        boton[cont].setBorderPainted(false);

        if (cont > 0) {
            boton[cont].setBounds(20, boton[cont - 1].getY() + boton[cont - 1].getHeight() + 10, 127, 127);
        } else {
            boton[cont].setBounds(20, 10, 127, 127);
        }

        boton[cont].setVisible(true);
        panelMini.add(boton[cont]);

        int tam = 0;

        for (int n = 0; n < boton.length; n++) {
            tam = tam + boton[n].getHeight() + 10;
        }

        if (panelMini.getHeight() < tam) {
            panelMini.setPreferredSize(new java.awt.Dimension(panelMini.getWidth(), panelMini.getHeight() + boton[cont].getHeight()));
            panelMini.setSize(new java.awt.Dimension(panelMini.getWidth(), panelMini.getHeight() + boton[cont].getHeight() + 10));
            panelMini.repaint();
        }
    }

    public void pintarImagen(java.awt.image.BufferedImage imagen) {
        try {
            pd = null;
            pd = new es.jscan.Pantallas.PanelDibujo(imagen);
            pd.setAutoscrolls(true);
            pd.setVisible(true);
            pd.setSize(new java.awt.Dimension(imagen.getWidth(), imagen.getHeight()));
            pd.repaint();
            panelImagen.setPreferredSize(new java.awt.Dimension(pd.getWidth(), pd.getHeight()));
            panelImagen.setSize(new java.awt.Dimension(imagen.getWidth(), imagen.getHeight()));
            panelImagen.removeAll();
            panelImagen.add(pd);
            pd.ajustarAncho(panelVisorImagen.getWidth());
            panelImagen.setPreferredSize(new java.awt.Dimension(pd.getWidth(), pd.getHeight()));
            panelImagen.setSize(new java.awt.Dimension(pd.getWidth(), pd.getHeight()));
            panelImagen.repaint();
            panelVisorImagen.setPreferredSize(new java.awt.Dimension(panelIconosImagen.getWidth(), scrollImagenes.getHeight() - panelIconosImagen.getHeight() - 8));
            panelVisorImagen.setSize(new java.awt.Dimension(panelIconosImagen.getWidth(), scrollImagenes.getHeight() - panelIconosImagen.getHeight() - 8));
            panelVisorImagen.repaint();

            etiquetaCB.setText("");
            repaint();
            System.gc();

        } catch (Exception ex) {
            Utilidades.escribeLog("Error al pintar Imagen " + " - " + ex.getMessage());
        }
    }

    private void crearLote() {
        Calendar cal = Calendar.getInstance();

        String anio = String.valueOf(cal.get(Calendar.YEAR));
        String mes = String.valueOf((cal.get(Calendar.MONTH) + 1)).length() == 1 ? "0" + String.valueOf((cal.get(Calendar.MONTH) + 1)) : String.valueOf((cal.get(Calendar.MONTH) + 1));
        String dia = String.valueOf(cal.get(Calendar.DAY_OF_MONTH)).length() == 1 ? "0" + String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) : String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        String hora = String.valueOf(cal.get(Calendar.HOUR_OF_DAY)).length() == 1 ? "0" + String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) : String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        String minuto = String.valueOf(cal.get(Calendar.MINUTE)).length() == 1 ? "0" + String.valueOf(cal.get(Calendar.MINUTE)) : String.valueOf(cal.get(Calendar.MINUTE));
        String segundo = String.valueOf(cal.get(Calendar.SECOND)).length() == 1 ? "0" + String.valueOf(cal.get(Calendar.SECOND)) : String.valueOf(cal.get(Calendar.SECOND));

        String fecha = anio + mes + dia + "-" + hora + minuto + segundo;

        lote = fecha + "-" + utilidades.ip().replace(".", "");
        rutalote = rutadigita + separador + "lotes" + separador + lote;
        rutapendiente = rutadigita + separador + "pendientes";
        utilidades.crearDirectorio(rutalote);
        contimagen = 0;

        utilidades.crearFichero(rutalote + separador + "lote.ini", "texto");
        Properties props = new Properties();
        fecha = dia + "/" + mes + "/" + anio + " " + hora + ":" + minuto + ":" + segundo;
        //      Utilidades.escribeLog(fecha);
        props.setProperty("fecha", fecha);
        props.setProperty("Escaner", nombreEscaner.getText());
        props.setProperty("Resolucion", comboResolucion.getSelectedItem().toString());
        props.setProperty("Duplex", checkDuplex.isSelected() ? "S" : "N");
        props.setProperty("Color", comboColor.getSelectedItem().toString());

        utilidades.escribirPropeties(rutalote + separador + "lote.ini", props);
    }

    private void cargarlote(String lote) {
        inicializar();
        this.lote = lote;
        rutalote = utilidades.dirBase() + utilidades.separador() + "lotes" + utilidades.separador() + lote;
        rutapendiente = utilidades.dirBase() + utilidades.separador() + "pendientes";

        proplote = utilidades.leerPropeties(utilidades.dirBase() + utilidades.separador() + "lotes" + utilidades.separador() + lote + utilidades.separador() + "lote.ini");

        if (proplote.getProperty("numpaginas") == null) {
            utilidades.mensaje(this, "Carga del lote " + lote, "Imposible cargar el lote, no está indicado el número de páginas en el fichero " + rutalote + utilidades.separador() + "lote.ini");
            return;
        }
        nombreEscaner.setText(proplote.getProperty("Escaner"));
        checkDuplex.setSelected((proplote.getProperty("Duplex").equals("S") ? true : false));

        int posicion = 0;
        for (int i = 0; i < comboColor.getModel().getSize(); i++) {
            if (comboColor.getModel().getElementAt(i).equals(proplote.getProperty("Color"))) {
                posicion = i;
                break;
            }
        }
        comboColor.setSelectedIndex(posicion);

        posicion = 0;
        for (int i = 0; i < comboResolucion.getModel().getSize(); i++) {
            if (comboResolucion.getModel().getElementAt(i).equals(proplote.getProperty("Resolucion"))) {
                posicion = i;
                break;
            }
        }
        comboResolucion.setSelectedIndex(posicion);

        contimagen = Integer.parseInt(proplote.getProperty("numpaginas"));

        for (int i = 0; i < contimagen; i++) {
            cargarMiniaturas(i);
        }
        pintarImagenPorIndice(contimagen - 1);
    }

    private void inicializar() {
        contimagen = 0;
        rutalote = "";
        lote = "";
        rutapendiente = "";
        pd = null;
        boton = new JButton[1];
        rutaboton = new String[1];
        panelImagen.removeAll();
        panelImagen.setSize(panelVisorImagen.getWidth(), panelVisorImagen.getHeight());
        panelImagen.setPreferredSize(new java.awt.Dimension(panelVisorImagen.getWidth(), panelVisorImagen.getHeight()));
        panelMini.removeAll();
        panelMini.setSize(panelMini.getWidth(), panelImagen.getHeight() + panelIconosImagen.getHeight());
        panelMini.setPreferredSize(new java.awt.Dimension(panelMini.getWidth(), panelImagen.getHeight() + panelIconosImagen.getHeight()));
        listaMultiAdjuntos.clear();
        multiAdjuntos = false;
        repaint();
        labelNumPaginas.setText("0");
        labelTotalPaginas.setText("0");
        System.gc();
    }

    private void pintarImagenPorIndice(int numimagen) {
        String rutaimagen = rutaboton[numimagen].toString();
        File ficherosel = new File(rutaimagen);
        java.awt.image.BufferedImage miimage = null;
        try {
            miimage = javax.imageio.ImageIO.read(ficherosel);
        } catch (IOException ex) {
            Utilidades.escribeLog("Error -pintarImagenPorIndice- al pintar imagen por número " + ex.getMessage());
        }
        pintarImagen(miimage);
        labelNumPaginas.setText(numimagen + 1 + "");
    }

    private void aplicarConfiguracionEscaner(TwainSource source) {
        int valor = 0;
        double decimal = 0;

        try {
//            Utilidades.escribeLog("Resolución: " + source.getCapability(TwainConstants.ICAP_XRESOLUTION));
            source.setShowUI(false);
            source.setShowProgressBar(false);
            source.setCapability(TwainConstants.ICAP_SUPPORTEDSIZES, 1);  // 1 es A4
        } catch (ScannerIOException ex) {
            Utilidades.escribeLog("Error al aplicar configuración Escaner - Tamaño de página A4 " + " - " + ex.getMessage());
        }

        if (comboColor.getItemAt(comboColor.getSelectedIndex()).toString().toLowerCase().contains("color")) {
            valor = TwainConstants.TWPT_RGB;
        } else if (comboColor.getItemAt(comboColor.getSelectedIndex()).toString().toLowerCase().contains("grises")) {
            valor = TwainConstants.TWPT_GRAY;
        } else if (comboColor.getItemAt(comboColor.getSelectedIndex()).toString().toLowerCase().contains("blanco")) {
            valor = TwainConstants.TWPT_BW;
        }

        try {
            source.setCapability(TwainConstants.ICAP_PIXELTYPE, valor);
        } catch (ScannerIOException ex) {
            Utilidades.escribeLog("Excepción al establecer la propiedad del escaner Color -> " + ex.getMessage());
        }

//        try {
//            // TWBP_AUTO -1
//            // TWBP_DISABLE -2
//            source.setCapability(TwainConstants.ICAP_AUTODISCARDBLANKPAGES, -1);
//        } catch (ScannerIOException ex) {
//            Utilidades.escribeLog("Excepción al establecer la propiedad ICAP_AUTODISCARDBLANKPAGES -> " + ex.getMessage());
//        }


        valor = Integer.parseInt(comboResolucion.getItemAt(comboResolucion.getSelectedIndex()).toString());

        try {
            source.setResolution(valor);
        } catch (ScannerIOException ex) {
            Utilidades.escribeLog("Excepción al establecer la propiedad del escaner Resolución -> " + ex.getMessage());
        }

        try {
            source.setCapability(TwainConstants.CAP_DUPLEXENABLED, checkDuplex.isSelected());
            //      source.setCapability(TwainConstants.ICAP_COMPRESSION, 4);
            //  Utilidades.escribeLog(alimentador.getName().);
            /*
             * for (int i = 0; i < caps.length; i++) {
             * Utilidades.escribeLog(caps[i].toString());
             * Utilidades.escribeLog(caps[i].getName()); }
             */
            //source.setCapability(TwainConstants.CAP_FEEDERENABLED, true);
            // source.setCapability(TwainConstants.CAP_AUTOFEED, true);
            //     source.setImageFileFormat(4);
            //    source.setCapability(TwainConstants.ICAP_COMPRESSION, 5);
            //    source.setCapability(TwainConstants.CAP_DUPLEX, false);
            //    source.setCapability(TwainConstants.ICAP_COMPRESSION, TwainConstants.TWFF_JFIF);
            //  source.setCapability(TwainConstants.ICAP_YSCALING,0.25);

        } catch (ScannerIOException ex) {
            Utilidades.escribeLog("Excepción al establecer la propiedad del escaner Duplex -> " + ex.getMessage());
        }

        decimal = Double.parseDouble(contraste.getItems()[sliderContraste.getValue()].toString());

        try {
            //   Utilidades.escribeLog("Contraste: " + source.getCapability(TwainConstants.ICAP_CONTRAST).toString());
            if (DEBUG) {
                Utilidades.escribeLog("Contraste: " + decimal + " - " + sliderContraste.getValue());
            }
            source.setCapability(TwainConstants.ICAP_CONTRAST, decimal);
        } catch (ScannerIOException ex) {
            Utilidades.escribeLog("Excepción al establecer la propiedad del escaner Contraste -> " + ex.getMessage());
        }

        decimal = 0;
        decimal = Double.parseDouble(brillo.getItems()[sliderBrillo.getValue()].toString());

        try {
            //    Utilidades.escribeLog("Contraste: " + source.getCapability(TwainConstants.ICAP_BRIGHTNESS).toString());
            if (DEBUG) {
                Utilidades.escribeLog("Brillo: " + decimal + " - " + sliderBrillo.getValue());
            }
            source.setCapability(TwainConstants.ICAP_BRIGHTNESS, decimal);
        } catch (ScannerIOException ex) {
            Utilidades.escribeLog("Excepción al establecer la propiedad del escaner Brillo -> " + ex.getMessage());
        }
    }

    public void activarDebug(Boolean activar) {
        opcionDebug.setSelected(activar);
    }

    @Action
    private void mostrarLotes() {
        String loteori = lote;
        PantallaLotesGuardados lotes = new PantallaLotesGuardados(this, true);
        if (lote.length() >= 28 && !lote.equals(loteori)) {
            cargarlote(lote);
        } else {
            lote = loteori;
        }

    }

    public void borrarLote(String loteborrar) {
        //  Utilidades.escribeLog(utilidades.dirBase() + utilidades.separador() + "lotes" + utilidades.separador() + lote);
        if (loteborrar.length() >= 28) {
            utilidades.borrarDirectorio(utilidades.dirBase() + utilidades.separador() + "lotes" + utilidades.separador() + loteborrar);
        }
    }

    private void crearXmlLote(String lote) {
        Calendar cal = Calendar.getInstance();
        String anio = String.valueOf(cal.get(Calendar.YEAR));
        String mes = String.valueOf((cal.get(Calendar.MONTH) + 1)).length() == 1 ? "0" + String.valueOf((cal.get(Calendar.MONTH) + 1)) : String.valueOf((cal.get(Calendar.MONTH) + 1));
        String dia = String.valueOf(cal.get(Calendar.DAY_OF_MONTH)).length() == 1 ? "0" + String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) : String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        String hora = String.valueOf(cal.get(Calendar.HOUR_OF_DAY)).length() == 1 ? "0" + String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) : String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        String minuto = String.valueOf(cal.get(Calendar.MINUTE)).length() == 1 ? "0" + String.valueOf(cal.get(Calendar.MINUTE)) : String.valueOf(cal.get(Calendar.MINUTE));
        String segundo = String.valueOf(cal.get(Calendar.SECOND)).length() == 1 ? "0" + String.valueOf(cal.get(Calendar.SECOND)) : String.valueOf(cal.get(Calendar.SECOND));

        String dirorigen = rutadigita + separador + "lotes" + separador + lote + separador;
        File dir = new File(dirorigen);

        FilenameFilter filtropdf = new FiltroPdf();
        String ficheros[] = dir.list(filtropdf);

        String ficheroxml = rutadigita + separador + "lotes" + separador + lote + separador + lote + ".xml";

        XmlLote Lote = new XmlLote();
        XmlInfo Info = new XmlInfo();

        String numpaginas = "";
        String tam = "";
        String proceso = "";
        String usuarioldap = "";
        String ip = "";
        String provincia = "";
        String fechadigita = "";
        String fechacreacion = "";
        String expediente = "";
        String cb = "";
        String notificarinstructor = "";
        String tipodocumento = "";
        String fechanotificacion = "";
        String actualizarfase = "";
        String ubicacionfisica = "";

        for (int i = 0; i < ficheros.length; i++) {
            String fich = ficheros[i].toString();
            try {
                PdfReader reader = new PdfReader(dirorigen + fich);
                proceso = (reader.getInfo().get("proceso") == null ? "" : reader.getInfo().get("proceso"));
                usuarioldap = (reader.getInfo().get("usuarioldap") == null ? "" : reader.getInfo().get("usuarioldap"));
                ip = (reader.getInfo().get("IP") == null ? "" : reader.getInfo().get("IP"));
                provincia = (reader.getInfo().get("provincia") == null ? "" : reader.getInfo().get("provincia"));
                fechadigita = (reader.getInfo().get("fechadigita") == null ? "" : reader.getInfo().get("fechadigita"));
                fechacreacion = (reader.getInfo().get("fechacreacion") == null ? "" : reader.getInfo().get("fechacreacion"));
                numpaginas = "" + reader.getNumberOfPages();
                tam = "" + NumberFormat.getInstance().format(reader.getFileLength());
                expediente = (reader.getInfo().get("Expediente") == null ? "" : reader.getInfo().get("Expediente"));
                cb = (reader.getInfo().get("codigobarras") == null ? "" : reader.getInfo().get("codigobarras"));
                notificarinstructor = (reader.getInfo().get("notificarinstructor") == null ? "" : reader.getInfo().get("notificarinstructor"));
                tipodocumento = (reader.getInfo().get("tipodocumento") == null ? "" : reader.getInfo().get("tipodocumento"));
                fechanotificacion = (reader.getInfo().get("fechanotificacion") == null ? "" : reader.getInfo().get("fechanotificacion"));
                actualizarfase = (reader.getInfo().get("actualizarfase") == null ? "" : reader.getInfo().get("actualizarfase"));
                ubicacionfisica = (reader.getInfo().get("ubicacionfisica") == null ? "" : reader.getInfo().get("ubicacionfisica"));

            } catch (IOException ex) {
                Utilidades.escribeLog("Error -crearXmlLote- al leer el PDF " + dirorigen + fich + "  - Error " + ex.getMessage());
            }

            XmlDocumento doc = new XmlDocumento();
            doc.setExpediente(expediente);
            doc.setFichero(fich);
            doc.setNumPaginas(numpaginas);
            doc.setTam(tam);
            doc.setCb(cb);
            doc.setNotificarinstructor(notificarinstructor);
            doc.setTipodocumento(tipodocumento);
            doc.setFechanotificacion(fechanotificacion);
            doc.setActualizarfase(actualizarfase);
            doc.setUbicacionfisica(ubicacionfisica);
            Lote.add(doc);
        }

        Info.setFechacreacion(fechacreacion);
        Info.setFechadigita(fechadigita);
        Info.setFechaenvio(dia + "/" + mes + "/" + anio + " " + hora + ":" + minuto + ":" + segundo);
        Info.setIp(ip);
        Info.setNumdocumentos("" + ficheros.length);
        Info.setProceso(proceso);
        Info.setProvincia(provincia);
        Info.setUsuarioldap(usuarioldap);

        Lote.setInfo(Info);

        XStream xstream = new XStream();
        xstream.alias("Lote", XmlLote.class);
        xstream.alias("Info", XmlInfo.class);
        xstream.alias("Documento", XmlDocumento.class);

        try {
            xstream.toXML(Lote, new FileOutputStream(ficheroxml));
        } catch (FileNotFoundException ex) {
            Utilidades.escribeLog("Error al escribir el fichero -crearXmlLote- " + ficheroxml + " - Error: " + ex.getMessage());
        }
    }

    private Boolean procesarLoteXml(String ficheroXml, String loteactual, String accion) {
        Boolean resultado = false;

        try {
            XStream xstream = new XStream();
            xstream.alias("Lote", XmlLote.class);
            xstream.alias("Info", XmlInfo.class);
            xstream.alias("Documento", XmlDocumento.class);
            FileInputStream fichero;
            fichero = new FileInputStream(ficheroXml);
            XmlLote lote = (XmlLote) xstream.fromXML(fichero);
            int numdocs = Integer.parseInt(lote.getInfo().getNumdocumentos());
            DigitaLotesBean lotesbean = new DigitaLotesBean();
            Calendar cal = Calendar.getInstance();
            String anio = String.valueOf(cal.get(Calendar.YEAR));
            String mes = String.valueOf((cal.get(Calendar.MONTH) + 1)).length() == 1 ? "0" + String.valueOf((cal.get(Calendar.MONTH) + 1)) : String.valueOf((cal.get(Calendar.MONTH) + 1));
            String dia = String.valueOf(cal.get(Calendar.DAY_OF_MONTH)).length() == 1 ? "0" + String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) : String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

            if (accion.equals("ACTUALIZAR")) {
                lotesbean.setLote(loteactual);
                lotesbean.setEstado("ENVIADO");
                lotesbean.setFechaEnvio(dia + "/" + mes + "/" + anio);
            } else if (accion.equals("INSERTAR")) {
                for (int i = 0; i < numdocs; i++) {
                    lotesbean = new DigitaLotesBean();
                    lotesbean.setEstado("ENVIADO");
                    lotesbean.setExpediente(lote.getDocumento().get(i).getExpediente());
                    lotesbean.setArchivo(lote.getDocumento().get(i).getFichero());
                    lotesbean.setLote(loteactual);
                    lotesbean.setUsuario(lote.getInfo().getUsuarioldap());
                    lotesbean.setTipoProceso(lote.getInfo().getProceso());
                    lotesbean.setFechaCreacion(lote.getInfo().getFechacreacion());
                    lotesbean.setFechaGrabacion(dia + "/" + mes + "/" + anio);
                    lotesbean.setIp(lote.getInfo().getIp());
                    lotesbean.setPaginas(lote.getDocumento().get(i).getNumPaginas());
                    lotesbean.setTamano(lote.getDocumento().get(i).getTam());
                    lotesbean.setProvincia(lote.getInfo().getProvincia());
                    lotesbean.setDescripcion("");
                    lotesbean.setIdDocumentum("");
                    lotesbean.setCb(lote.getDocumento().get(i).getCb());
                }
            }
            fichero.close();
        } catch (Exception ex) {
            Utilidades.escribeLog("Error en -procesarLoteXml- - " + ex.getMessage());
        }
        return resultado;
    }
}

class FiltroPdf implements FilenameFilter {

    public boolean accept(File dir, String name) {
        return (name.toLowerCase().endsWith(".pdf"));
    }
}

class FiltroZip implements FilenameFilter {

    public boolean accept(File dir, String name) {
        return (name.toLowerCase().endsWith(".zip"));
    }
}

class ExtensionFileFilter extends FileFilter {

    private String[] extensions = null;
    private String desc;

    /**
     * Constructor.
     *
     * @param ext A list of filename extensions, ie new String[] { "PDF"}.
     * @param description A description of the files.
     */
    public ExtensionFileFilter(String[] ext, String description) {
        extensions = ext;
        desc = description;
    }

    /**
     * {@inheritDoc}
     */
    public boolean accept(File pathname) {
        if (pathname.isDirectory()) {
            return true;
        }
        boolean acceptable = false;
        String name = pathname.getName().toUpperCase();
        for (int i = 0; !acceptable && i < extensions.length; i++) {
            if (name.endsWith(extensions[i].toUpperCase())) {
                acceptable = true;
            }
        }
        return acceptable;
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return desc;
    }
}