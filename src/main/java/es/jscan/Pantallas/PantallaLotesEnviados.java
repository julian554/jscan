package es.jscan.Pantallas;

import com.thoughtworks.xstream.XStream;
import com.toedter.calendar.JDateChooser;
import es.jscan.Beans.ResultadoGDBean;
import es.jscan.Beans.XmlDocumento;
import es.jscan.Beans.XmlInfo;
import es.jscan.Beans.XmlLote;
import es.jscan.utilidades.TablaSinEditarCol;
import es.jscan.utilidades.Utilidades;
import es.jscan.Beans.DigitaLotesBean;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import org.apache.commons.lang.time.DateUtils;

/**
 * @author julian.collado
 */
public class PantallaLotesEnviados extends javax.swing.JDialog {

    public static PantallaLotesGuardados ventanapadre = null;
    public String titulo = "ENVIADOS";
    public String botonpulsado = "buscar";
    public String loteseleccionado = "";
    private ArrayList<ResultadoGDBean> resultadogd = null;
    Utilidades util = new Utilidades();

    public PantallaLotesEnviados(PantallaLotesGuardados parent, boolean modal) {
        super(parent, modal);
        initComponents();
        ventanapadre = parent;
        setLocationRelativeTo(ventanapadre);
        asignarIconos();
        //     cargarEnviados();
        textoUsuario.setText(util.usuario());
        textoUsuario.setEditable(false);
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelDatos = new javax.swing.JPanel();
        scrollGrid = new javax.swing.JScrollPane(gridDatos);
        gridDatos = new javax.swing.JTable();
        panelSuperior = new javax.swing.JPanel();
        labelExpediente = new javax.swing.JLabel();
        labelLote = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        textoExpediente = new javax.swing.JTextField(15);
        textoLote = new javax.swing.JTextField(30);
        textoUsuario = new javax.swing.JTextField();
        labelFechaDesde = new javax.swing.JLabel();
        dateFCreacionDesde = new JDateChooser("dd/MM/yyyy", "##/##/####", '_');
        labelFechaDesde1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        dateFgrabacionDesde = new JDateChooser("dd/MM/yyyy", "##/##/####", '_');
        comboProceso = new javax.swing.JComboBox();
        labelFechaHasta = new javax.swing.JLabel();
        dateFCreacionHasta = new JDateChooser("dd/MM/yyyy", "##/##/####", '_');
        labelFechaHasta1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        dateFgrabacionHasta = new JDateChooser("dd/MM/yyyy", "##/##/####", '_');
        comboEstado = new javax.swing.JComboBox();
        botonBuscar = new javax.swing.JButton();
        botonSalir = new javax.swing.JButton();
        Menu = new javax.swing.JMenuBar();
        opciones = new javax.swing.JMenu();
        opcionBuscar = new javax.swing.JMenuItem();
        opcionSalir = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Estado de los lotes digitalizados");
        setMinimumSize(new java.awt.Dimension(1000, 642));
        setModal(true);
        setName("tabla"); // NOI18N
        setResizable(false);

        panelDatos.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelDatos.setAutoscrolls(true);
        panelDatos.setMaximumSize(new java.awt.Dimension(998, 640));
        panelDatos.setMinimumSize(new java.awt.Dimension(998, 640));
        panelDatos.setOpaque(false);
        panelDatos.setPreferredSize(new java.awt.Dimension(998, 640));

        scrollGrid.setAutoscrolls(true);
        scrollGrid.setMaximumSize(new java.awt.Dimension(panelDatos.getHeight()-2,panelDatos.getWidth()-2));
        scrollGrid.setMinimumSize(new java.awt.Dimension(panelDatos.getHeight()-2,panelDatos.getWidth()-2));
        scrollGrid.setPreferredSize(new java.awt.Dimension(panelDatos.getHeight(),panelDatos.getWidth()));

        gridDatos.setAutoCreateRowSorter(true);
        gridDatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "", "", "", "", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        gridDatos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        gridDatos.setFillsViewportHeight(true);
        gridDatos.setMaximumSize(new java.awt.Dimension(panelDatos.getHeight()-3,panelDatos.getWidth()-3));
        gridDatos.setMinimumSize(new java.awt.Dimension(panelDatos.getHeight()-3,panelDatos.getWidth()-3));
        gridDatos.setPreferredSize(null);
        gridDatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                gridDatosMouseReleased(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                gridDatosMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                gridDatosMousePressed(evt);
            }
        });
        scrollGrid.setViewportView(gridDatos);

        javax.swing.GroupLayout panelDatosLayout = new javax.swing.GroupLayout(panelDatos);
        panelDatos.setLayout(panelDatosLayout);
        panelDatosLayout.setHorizontalGroup(
            panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollGrid, javax.swing.GroupLayout.DEFAULT_SIZE, 998, Short.MAX_VALUE)
        );
        panelDatosLayout.setVerticalGroup(
            panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatosLayout.createSequentialGroup()
                .addComponent(scrollGrid, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 186, Short.MAX_VALUE))
        );

        panelSuperior.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelSuperior.setToolTipText("Busqueda de Lotes");
        panelSuperior.setMaximumSize(new java.awt.Dimension(990, 130));
        panelSuperior.setMinimumSize(new java.awt.Dimension(990, 130));
        panelSuperior.setPreferredSize(new java.awt.Dimension(990, 130));
        panelSuperior.setVerifyInputWhenFocusTarget(false);

        labelExpediente.setText("Expediente");

        labelLote.setText("Lote");

        jLabel1.setText("Usuario");

        textoExpediente.setToolTipText("Número de Expediente");
        textoExpediente.setAutoscrolls(false);
        textoExpediente.setMaximumSize(new java.awt.Dimension(6, 20));
        textoExpediente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textoExpedienteActionPerformed(evt);
            }
        });

        textoLote.setToolTipText("Código de Lote");
        textoLote.setAutoscrolls(false);

        textoUsuario.setToolTipText("Usuario LDAP");

        labelFechaDesde.setText("F. Creación desde");

        dateFCreacionDesde.setToolTipText("Fecha Desde");
        dateFCreacionDesde.setDateFormatString("DD/MM/YYYY");
        dateFCreacionDesde.setMaximumSize(new java.awt.Dimension(120, 20));
        dateFCreacionDesde.setMinimumSize(new java.awt.Dimension(120, 20));
        dateFCreacionDesde.setPreferredSize(new java.awt.Dimension(120, 20));
        dateFCreacionDesde.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                dateFCreacionDesdeInputMethodTextChanged(evt);
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
        });

        labelFechaDesde1.setText("F. Grabación desde");

        jLabel2.setText("Proceso");

        dateFgrabacionDesde.setToolTipText("Fecha Desde");
        dateFgrabacionDesde.setDateFormatString("DD/MM/YYYY");
        dateFgrabacionDesde.setMaximumSize(new java.awt.Dimension(120, 20));
        dateFgrabacionDesde.setMinimumSize(new java.awt.Dimension(120, 20));
        dateFgrabacionDesde.setPreferredSize(new java.awt.Dimension(120, 20));

        comboProceso.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "RECEPCIÓN CON EXPEDIENTE", "ADJUNTOS", "APORTACIÓN SIN FORMULARIO", "ACUSES DE RECIBO", "LOTES DE ESCRITOS", "TASAS PAGADAS" }));
        comboProceso.setToolTipText("<html>\nProceso para el lote de documentos\n<br>\n<UL>\n    <LI>Recepción con Expediente: una vez ya ha sido generado el expediente a través de la Aplicación de Extranjería, \n<br>se pueden digitalizar los documentos aportados asociándose al Nº de expediente que corresponde. \n    <LI>Adjuntos: permite adjuntar documentación a un expediente que ya tenga el formulario. \n    <LI>Tasas: agrega las tasas a un expediente.\n    <LI>Acuses de Recibo: incorporación de los documentos a su expediente correspondiente.\n    <LI>Lotes de Escritos: añade escritos al expediente.\n    <LI>Aportación sin Formulario: documentación para expedientes que no tenga Formulario.\n</UL>");
        comboProceso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboProcesoActionPerformed(evt);
            }
        });

        labelFechaHasta.setText("F. Creación hasta");

        dateFCreacionHasta.setToolTipText("Fecha Hasta");
        dateFCreacionHasta.setDateFormatString("DD/MM/YYYY");
        dateFCreacionHasta.setMaximumSize(new java.awt.Dimension(120, 20));
        dateFCreacionHasta.setMinimumSize(new java.awt.Dimension(120, 20));
        dateFCreacionHasta.setPreferredSize(new java.awt.Dimension(120, 20));
        dateFCreacionHasta.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dateFCreacionHastaPropertyChange(evt);
            }
        });

        labelFechaHasta1.setText("F. Grabación hasta");

        jLabel3.setText("Estado");

        dateFgrabacionHasta.setToolTipText("Fecha Hasta");
        dateFgrabacionHasta.setDateFormatString("DD/MM/YYYY");
        dateFgrabacionHasta.setMaximumSize(new java.awt.Dimension(120, 20));
        dateFgrabacionHasta.setMinimumSize(new java.awt.Dimension(120, 20));
        dateFgrabacionHasta.setPreferredSize(new java.awt.Dimension(120, 20));
        dateFgrabacionHasta.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dateFgrabacionHastaPropertyChange(evt);
            }
        });

        comboEstado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PENDIENTE", "ENVIADO", "EN PROCESO", "ARCHIVADO" }));
        comboEstado.setToolTipText("<html>\nPENDIENTE - Aún no se ha enviado al servidor de proceso. Está almacenado en el directorio \"Pendientes\". <br>\nENVIADO - El lote ha sido enviado al servidor de proceso y está en cola de espera.<br>\nEN PROCESO - El lote se está procesando.<br>\nARCHIVADO - La documentación ya está en el Gestor Documental.<br>");

        botonBuscar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        botonBuscar.setMnemonic('B');
        botonBuscar.setText("Buscar");
        botonBuscar.setToolTipText("<html>\nPermite buscar los Lotes enviados.<br>\nEn el resultado podremos ver el estado del lote:<br>\nPENDIENTE - Aún no se ha enviado al servidor de proceso. Está almacenado en el directorio \"Pendientes\". <br>\nENVIADO - El lote ha sido enviado al servidor de proceso y está en cola de espera.<br>\nEN PROCESO - El lote se está procesando.<br>\nARCHIVADO - La documentación ya está en el Gestor Documental.<br>\n<br>\nLa búsqueda se puede realizar por uno o varios de los campos disponibles<br>\nEn el caso de las fecha será obligatorio indicar una fecha de \"desde\" y una fecha \"hasta\".<br>\n<br>\nUn lote está compuesto por uno o varios PDFs que contienen las imágenes escaneadas o importadas juntos<br>\n con un fichero XML con la información correspondiente al proceso seleccionado (Recepción con Expediente,<br>\nAcuses de Recibo, Adjutos, Lotes de Escritos, Tasas o Aportación sin formulario) y otros datos como la IP del PC,<br>\n nombre del usuario, Nº de Expediente (si es posible conocerlo), fecha de creación, código de barras <br>\n(si el proceso lo utiliza y es reconocido por el sistema), etc.");
        botonBuscar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        botonBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBuscarActionPerformed(evt);
            }
        });

        botonSalir.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        botonSalir.setMnemonic('S');
        botonSalir.setText("Salir");
        botonSalir.setToolTipText("Volver a la pantalla anterior");
        botonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelSuperiorLayout = new javax.swing.GroupLayout(panelSuperior);
        panelSuperior.setLayout(panelSuperiorLayout);
        panelSuperiorLayout.setHorizontalGroup(
            panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSuperiorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelLote, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelExpediente, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textoExpediente, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textoLote, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelFechaDesde1)
                    .addGroup(panelSuperiorLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(labelFechaDesde))
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateFCreacionDesde, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dateFgrabacionDesde, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comboProceso, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelFechaHasta, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelFechaHasta1)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateFgrabacionHasta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dateFCreacionHasta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(botonBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botonSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(61, Short.MAX_VALUE))
        );

        panelSuperiorLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {labelFechaDesde, labelFechaDesde1, labelFechaHasta});

        panelSuperiorLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {dateFCreacionDesde, dateFCreacionHasta, dateFgrabacionDesde, dateFgrabacionHasta});

        panelSuperiorLayout.setVerticalGroup(
            panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSuperiorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSuperiorLayout.createSequentialGroup()
                        .addComponent(botonBuscar)
                        .addGap(18, 18, 18)
                        .addComponent(botonSalir))
                    .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelSuperiorLayout.createSequentialGroup()
                            .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(labelFechaDesde, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(dateFCreacionDesde, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(labelFechaDesde1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(dateFgrabacionDesde, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(comboProceso, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(panelSuperiorLayout.createSequentialGroup()
                            .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(labelExpediente)
                                .addComponent(textoExpediente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(labelLote)
                                .addComponent(textoLote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(textoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(panelSuperiorLayout.createSequentialGroup()
                            .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(labelFechaHasta, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(dateFCreacionHasta, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(labelFechaHasta1, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(dateFgrabacionHasta, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(comboEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        panelSuperiorLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {labelFechaDesde1, labelFechaHasta1, labelLote, textoLote});

        panelSuperiorLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {dateFCreacionDesde, dateFCreacionHasta, dateFgrabacionDesde, dateFgrabacionHasta, labelExpediente, labelFechaDesde, labelFechaHasta, textoExpediente});

        panelSuperiorLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {comboEstado, comboProceso, jLabel1, jLabel2, jLabel3, textoUsuario});

        opciones.setMnemonic('O');
        opciones.setText("Opciones");

        opcionBuscar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
        opcionBuscar.setMnemonic('B');
        opcionBuscar.setText("Buscar");
        opcionBuscar.setToolTipText("<html>\nPermite buscar los Lotes enviados.<br>\nEn el resultado podremos ver el estado del lote:<br>\nPENDIENTE - Aún no se ha enviado al servidor de proceso. Está almacenado en el directorio \"Pendientes\". <br>\nENVIADO - El lote ha sido enviado al servidor de proceso y está en cola de espera.<br>\nEN PROCESO - El lote se está procesando.<br>\nARCHIVADO - La documentación ya está en el Gestor Documental.<br>\n<br>\nLa búsqueda se puede realizar por uno o varios de los campos disponibles<br>\nEn el caso de las fecha será obligatorio indicar una fecha de \"desde\" y una fecha \"hasta\".<br>\n<br>\nUn lote está compuesto por uno o varios PDFs que contienen las imágenes escaneadas o importadas juntos<br>\n con un fichero XML con la información correspondiente al proceso seleccionado (Recepción con Expediente,<br>\nAcuses de Recibo, Adjutos, Lotes de Escritos, Tasas o Aportación sin formulario) y otros datos como la IP del PC,<br>\n nombre del usuario, Nº de Expediente (si es posible conocerlo), fecha de creación, código de barras <br>\n(si el proceso lo utiliza y es reconocido por el sistema), etc.\n");
        opcionBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionBuscarActionPerformed(evt);
            }
        });
        opciones.add(opcionBuscar);

        opcionSalir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
        opcionSalir.setMnemonic('S');
        opcionSalir.setText("Salir");
        opcionSalir.setToolTipText("Volver a la pantalla anterior");
        opcionSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionSalirActionPerformed(evt);
            }
        });
        opciones.add(opcionSalir);

        Menu.add(opciones);

        setJMenuBar(Menu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelSuperior, javax.swing.GroupLayout.DEFAULT_SIZE, 1020, Short.MAX_VALUE)
            .addComponent(panelDatos, javax.swing.GroupLayout.DEFAULT_SIZE, 1020, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelDatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBuscarActionPerformed
        //  cargarEnviados();
        buscarlotes();
        botonpulsado = "buscar";
    }//GEN-LAST:event_botonBuscarActionPerformed

    private void gridDatosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gridDatosMouseReleased
    }//GEN-LAST:event_gridDatosMouseReleased

    private void gridDatosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gridDatosMousePressed
    }//GEN-LAST:event_gridDatosMousePressed

    private void gridDatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gridDatosMouseClicked
    }//GEN-LAST:event_gridDatosMouseClicked

    private void salir() {
        this.dispose();
    }

    private void botonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSalirActionPerformed
        salir();
    }//GEN-LAST:event_botonSalirActionPerformed

    private void dateFCreacionHastaPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dateFCreacionHastaPropertyChange
    }//GEN-LAST:event_dateFCreacionHastaPropertyChange

    private void textoExpedienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textoExpedienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textoExpedienteActionPerformed

    private void dateFgrabacionHastaPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dateFgrabacionHastaPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_dateFgrabacionHastaPropertyChange

    private void comboProcesoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboProcesoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboProcesoActionPerformed

    private void dateFCreacionDesdeInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_dateFCreacionDesdeInputMethodTextChanged
    }//GEN-LAST:event_dateFCreacionDesdeInputMethodTextChanged

    private void opcionBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionBuscarActionPerformed
        buscarlotes();
        botonpulsado = "buscar";
    }//GEN-LAST:event_opcionBuscarActionPerformed

    private void opcionSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionSalirActionPerformed
        salir();
    }//GEN-LAST:event_opcionSalirActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                final PantallaLotesGuardados dialog = new PantallaLotesGuardados(new PantallaPrincipal(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        dialog.dispose();
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar Menu;
    private javax.swing.JButton botonBuscar;
    private javax.swing.JButton botonSalir;
    private javax.swing.JComboBox comboEstado;
    private javax.swing.JComboBox comboProceso;
    private com.toedter.calendar.JDateChooser dateFCreacionDesde;
    private com.toedter.calendar.JDateChooser dateFCreacionHasta;
    private com.toedter.calendar.JDateChooser dateFgrabacionDesde;
    private com.toedter.calendar.JDateChooser dateFgrabacionHasta;
    private javax.swing.JTable gridDatos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel labelExpediente;
    private javax.swing.JLabel labelFechaDesde;
    private javax.swing.JLabel labelFechaDesde1;
    private javax.swing.JLabel labelFechaHasta;
    private javax.swing.JLabel labelFechaHasta1;
    private javax.swing.JLabel labelLote;
    private javax.swing.JMenuItem opcionBuscar;
    private javax.swing.JMenuItem opcionSalir;
    private javax.swing.JMenu opciones;
    private javax.swing.JPanel panelDatos;
    private javax.swing.JPanel panelSuperior;
    private javax.swing.JScrollPane scrollGrid;
    private javax.swing.JTextField textoExpediente;
    private javax.swing.JTextField textoLote;
    private javax.swing.JTextField textoUsuario;
    // End of variables declaration//GEN-END:variables

    private void cargarEnviados(ArrayList<DigitaLotesBean> lotes) {
        botonBuscar.setBackground(Color.gray);
        setTitle(titulo + " - ENVIADOS");

        Object[] cabecera = {"Nombre del Lote",
            "Archivo",
            "Páginas",
            "Proceso",
            "F. Creación",
            "F. Grabación",
            "Estado",
            "Expediente",
            "Usuario"};

        int filas = lotes.size();
        Object[][] datos = new Object[filas][9];

        int correcto = 0;

        for (int n = 0; n < filas; n++) {
            try {
                datos[n][0] = lotes.get(n).getLote();
                datos[n][1] = lotes.get(n).getArchivo();
                datos[n][2] = lotes.get(n).getPaginas();
                datos[n][3] = lotes.get(n).getTipoProceso();
                datos[n][4] = lotes.get(n).getFechaCreacion();
                datos[n][5] = lotes.get(n).getFechaGrabacion();
                datos[n][6] = lotes.get(n).getEstado();
                datos[n][7] = lotes.get(n).getExpediente().equals("null") ? "" : lotes.get(n).getExpediente();
                datos[n][8] = lotes.get(n).getUsuario();
                correcto++;
            } catch (Exception ex) {
                Utilidades.escribeLog("Error al leer array de Lotes -cargarEnviados- - " + ex.getMessage());
            }
        }

        if (correcto <= 0) {
            TablaSinEditarCol modeloLotes = new TablaSinEditarCol();
            gridDatos.setModel(modeloLotes);
            gridDatos.removeAll();
            gridDatos.doLayout();
            return;
        }

        final TablaSinEditarCol modeloLotes = new TablaSinEditarCol(datos, cabecera);
        try {
            gridDatos.setModel(modeloLotes);
        } catch (Exception e) {
            Utilidades.escribeLog("Error al asignar modelo -cargarEnviados- " + e.getMessage());
        }
        gridDatos.setShowHorizontalLines(true);
        gridDatos.setRowSelectionAllowed(true);
        gridDatos.setAutoCreateRowSorter(true);
        gridDatos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        gridDatos.setAutoscrolls(true);

        for (int i = 0; i < modeloLotes.getColumnCount(); i++) {
            TableColumn columna = gridDatos.getColumnModel().getColumn(i);
            switch (i) {
                case 0:
                    columna.setPreferredWidth(140);
                    columna.setMinWidth(140);
                    break;
                case 1:
                    columna.setPreferredWidth(100);
                    columna.setMinWidth(100);
                    break;
                case 2:
                    columna.setPreferredWidth(20);
                    columna.setMinWidth(20);
                    break;
                case 3:
                    columna.setPreferredWidth(120);
                    columna.setMinWidth(120);
                    break;
                case 4:
                    columna.setPreferredWidth(40);
                    columna.setMinWidth(40);
                    break;
                case 5:
                    columna.setPreferredWidth(40);
                    columna.setMinWidth(40);
                    break;
                case 6:
                    columna.setPreferredWidth(70);
                    columna.setMinWidth(70);
                    break;
                case 7:
                    columna.setPreferredWidth(70);
                    columna.setMinWidth(70);
                    break;
                default:
                    columna.setPreferredWidth(100);
            }
        }

        gridDatos.doLayout();
    }

    private void cargarEnviados() {
        botonBuscar.setBackground(Color.gray);
        setTitle(titulo + " - PENDIENTES - Sin conexión - Leyendo directorio local");

        Object[] cabecera = {"Nombre del Lote",
            "Nº Documentos",
            "Proceso",
            "F. Creación",
            "F. Escaneo",
            "Estado",
            "Usuario"};
        Utilidades util = new Utilidades();
        File dir = new File(util.dirBase() + util.separador() + "pendientes");
        FiltroZip filtrozip = new FiltroZip();
        String[] ficheros = dir.list(filtrozip);
        int filas = ficheros.length;

        if (filas <= 0) {
            return;
        }

        Object[][] datos = new Object[filas][7];

        int correcto = 0;

        for (int n = 0; n < filas; n++) {
            try {
                String nombrelote = ficheros[n].substring(0, 28);
                util.unzip(util.dirBase() + util.separador() + "pendientes" + util.separador() + nombrelote + ".zip", util.dirBase() + util.separador() + "pendientes" + util.separador(), nombrelote + ".xml");
                XStream xstream = new XStream();
                xstream.alias("Lote", XmlLote.class);
                xstream.alias("Info", XmlInfo.class);
                xstream.alias("Documento", XmlDocumento.class);
                FileInputStream fichero;
                fichero = new FileInputStream(util.dirBase() + util.separador() + "pendientes" + util.separador() + nombrelote + ".xml");
                XmlLote lote = (XmlLote) xstream.fromXML(fichero);
                datos[n][0] = nombrelote;
                datos[n][1] = lote.getInfo().getNumdocumentos();
                datos[n][2] = lote.getInfo().getProceso();
                datos[n][3] = lote.getInfo().getFechacreacion();
                datos[n][4] = lote.getInfo().getFechadigita();
                datos[n][5] = "PREPARADO";
                datos[n][6] = lote.getInfo().getUsuarioldap();
                fichero.close();
                correcto++;
            } catch (Exception ex) {
                Utilidades.escribeLog("Error al leer el Bean -cargarEnviados()- - " + ex.getMessage());
            }
        }


        if (correcto <= 0) {
            TablaSinEditarCol modeloLotes = new TablaSinEditarCol();
            gridDatos.setModel(modeloLotes);
            gridDatos.doLayout();
            return;
        }

        final TablaSinEditarCol modeloLotes = new TablaSinEditarCol(datos, cabecera);
        try {
            gridDatos.setModel(modeloLotes);
        } catch (Exception e) {
            Utilidades.escribeLog("Error al asignar modelo -cargarEnviados()- " + e.getMessage());
        }
        gridDatos.setShowHorizontalLines(true);
        gridDatos.setRowSelectionAllowed(true);
        gridDatos.setAutoCreateRowSorter(true);
        gridDatos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        gridDatos.setAutoscrolls(true);

        for (int i = 0; i < modeloLotes.getColumnCount(); i++) {
            TableColumn columna = gridDatos.getColumnModel().getColumn(i);
            switch (i) {
                case 0:
                    columna.setPreferredWidth(140);
                    columna.setMinWidth(140);
                    break;
                case 1:
                    columna.setPreferredWidth(40);
                    columna.setMinWidth(40);
                    break;
                case 2:
                    columna.setPreferredWidth(120);
                    columna.setMinWidth(120);
                    break;
                case 3:
                    columna.setPreferredWidth(90);
                    columna.setMinWidth(90);
                    break;
                case 4:
                    columna.setPreferredWidth(90);
                    columna.setMinWidth(90);
                    break;
                case 5:
                    columna.setPreferredWidth(40);
                    columna.setMinWidth(40);
                    break;
                case 6:
                    columna.setPreferredWidth(70);
                    columna.setMinWidth(70);
                    break;
                default:
                    columna.setPreferredWidth(100);
            }
        }

        gridDatos.doLayout();
    }

    private void asignarIconos() {
        java.net.URL imgURL = PantallaLotesEnviados.class.getClassLoader().getResource("es/jscan/Pantallas/imagenes/buscar.png");
        Icon imgicon = new ImageIcon(imgURL);
        this.botonBuscar.setIcon(imgicon);

        imgURL = PantallaLotesEnviados.class.getClassLoader().getResource("es/jscan/Pantallas/imagenes/salir.png");
        imgicon = new ImageIcon(imgURL);
        this.botonSalir.setIcon(imgicon);

        comboProceso.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"", "RECEPCION CON EXPEDIENTE", "ADJUNTOS", "APORTACION SIN FORMULARIO", "ACUSES DE RECIBO", "LOTES DE ESCRITOS", "TASAS PAGADAS"}));
        comboEstado.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"", "PENDIENTE", "ENVIADO", "EN PROCESO", "ARCHIVADO"}));

    }

    private void buscarlotes() {


        if (comprobar()) {
            String proceso = comboProceso.getItemAt(comboProceso.getSelectedIndex()).toString();
            String estado = comboEstado.getItemAt(comboEstado.getSelectedIndex()).toString();
            String expediente = textoExpediente.getText();
            String lote = textoLote.getText();
            String usuario = textoUsuario.getText();
            SimpleDateFormat formatofecha = new SimpleDateFormat("dd/MM/yyyy");
            String fenviodesde = dateFCreacionDesde.getDate() == null ? "" : formatofecha.format(dateFCreacionDesde.getDate());
            String fenviohasta = dateFCreacionHasta.getDate() == null ? "" : formatofecha.format(dateFCreacionHasta.getDate());
            String fgrabaciondesde = dateFgrabacionDesde.getDate() == null ? "" : formatofecha.format(dateFgrabacionDesde.getDate());
            String fgrabacionhasta = dateFgrabacionHasta.getDate() == null ? "" : formatofecha.format(dateFgrabacionHasta.getDate());
            DigitaLotesBean lotesbean = new DigitaLotesBean();

            lotesbean.setEstado(estado);
            lotesbean.setExpediente(expediente);
            lotesbean.setLote(lote);
            lotesbean.setUsuario(usuario);
            lotesbean.setTipoProceso(proceso);
            lotesbean.setFechaCreacion(fenviodesde + "," + fenviohasta);
            lotesbean.setFechaGrabacion(fgrabaciondesde + "," + fgrabacionhasta);

            TablaSinEditarCol modeloLotes = new TablaSinEditarCol();
            gridDatos.setModel(modeloLotes);
            gridDatos.removeAll();
            gridDatos.doLayout();
            if (PantallaPrincipal.CONTCONEX != 0) {
                cargarEnviados();
            }
        }
    }

    private boolean comprobar() {
        SimpleDateFormat formatofecha = new SimpleDateFormat("dd/MM/yyyy");
        String proceso = comboProceso.getItemAt(comboProceso.getSelectedIndex()).toString();
        String estado = comboEstado.getItemAt(comboEstado.getSelectedIndex()).toString();
        String expediente = textoExpediente.getText();
        String lote = textoLote.getText();
        String usuario = textoUsuario.getText();
        String fenviodesde = dateFCreacionDesde.getDate() == null ? "" : formatofecha.format(dateFCreacionDesde.getDate());
        String fenviohasta = dateFCreacionHasta.getDate() == null ? "" : formatofecha.format(dateFCreacionHasta.getDate());
        String fgrabaciondesde = dateFgrabacionDesde.getDate() == null ? "" : formatofecha.format(dateFgrabacionDesde.getDate());
        String fgrabacionhasta = dateFgrabacionHasta.getDate() == null ? "" : formatofecha.format(dateFgrabacionHasta.getDate());
        Utilidades utilidades = new Utilidades();
        if (!expediente.isEmpty()) {
            if (expediente.length() != 15) {
                utilidades.mensaje(this, "Error campo Expediente", "\n El Nº de Expediente debe estar formado por 15 dígitos.");
                textoExpediente.requestFocus();
                return false;
            }
        }

        if (!lote.isEmpty()) {
            if (lote.length() != 29) {
                utilidades.mensaje(this, "Error campo Lote", "\n El Lote debe tener 28 caracteres. Formato AAAAMMDD-HHMMSS-NNNNNNNNNNNN");
                textoLote.requestFocus();
                return false;
            }
        }

        if (!fenviodesde.isEmpty()) {
            if (fenviohasta.isEmpty()) {
                utilidades.mensaje(this, "Error campo Fecha Creación", "\n Debe indicar una Fecha Hasta correcta.");
                dateFCreacionHasta.requestFocus();
                return false;
            }
        }

        if (!fenviohasta.isEmpty()) {
            if (fenviodesde.isEmpty()) {
                utilidades.mensaje(this, "Error campo Fecha Creación", "\n Debe indicar una Fecha Desde correcta.");
                dateFCreacionDesde.requestFocus(true);
                dateFCreacionDesde.requestFocus();
                return false;
            }
        }


        //       DateUtils.truncate(, Calendar.DATE)

        if (!fenviodesde.isEmpty() & !fenviohasta.isEmpty()) {
            if (DateUtils.truncate(dateFCreacionDesde.getDate(), Calendar.DATE).after(DateUtils.truncate(dateFCreacionHasta.getDate(), Calendar.DATE))
                    & !DateUtils.truncate(dateFCreacionDesde.getDate(), Calendar.DATE).equals(DateUtils.truncate(dateFCreacionHasta.getDate(), Calendar.DATE))) {
                utilidades.mensaje(this, "Error campo Fecha Creación", "\n La Fecha Hasta tiene que ser mayor o igual que la Fecha Desde");
                dateFCreacionHasta.requestFocus();
                return false;
            }
        }

        if (!fgrabaciondesde.isEmpty()) {
            if (fgrabacionhasta.isEmpty()) {
                utilidades.mensaje(this, "Error campo Fecha Grabación", "\n Debe indicar una Fecha Hasta correcta.");
                dateFgrabacionHasta.requestFocus();
                return false;
            }
        }

        if (!fgrabacionhasta.isEmpty()) {
            if (fgrabaciondesde.isEmpty()) {
                utilidades.mensaje(this, "Error campo Fecha Grabación", "\n Debe indicar una Fecha Desde correcta.");
                dateFgrabacionDesde.requestFocus();
                return false;
            }
        }

        if (!fgrabaciondesde.isEmpty() & !fgrabacionhasta.isEmpty()) {
            if (DateUtils.truncate(dateFgrabacionDesde.getDate(), Calendar.DATE).after(DateUtils.truncate(dateFgrabacionHasta.getDate(), Calendar.DATE))
                    & !DateUtils.truncate(dateFgrabacionDesde.getDate(), Calendar.DATE).equals(DateUtils.truncate(dateFgrabacionHasta.getDate(), Calendar.DATE))) {
                utilidades.mensaje(this, "Error campo Fecha Grabación", "\n La Fecha Hasta tiene que ser mayor o igual que la Fecha Desde");
                dateFgrabacionHasta.requestFocus();
                return false;
            }
        }


        return true;
    }
}
