package es.jscan.Pantallas;

import es.jscan.utilidades.Utilidades;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author julian.collado
 */
public class PantallaTextoIn extends javax.swing.JDialog {

    public static java.awt.Frame ventanapadre = null;
    public String boton = "N";

    public String getTexto(){
        return textTexto.getText();
    }  
    
    public PantallaTextoIn(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        ventanapadre = parent;
        initComponents();
        asignariconos();
        setLocationRelativeTo(ventanapadre);
        repaint();
    }

    public boolean respuesta() {
        return (boton.equals("S") ? true : false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        botonAceptar = new javax.swing.JButton();
        botonCancelar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        textTexto = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        botonAceptar.setText("Aceptar");
        botonAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAceptarActionPerformed(evt);
            }
        });

        botonCancelar.setText("Cancelar");
        botonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarActionPerformed(evt);
            }
        });

        textTexto.setColumns(20);
        textTexto.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        textTexto.setLineWrap(true);
        textTexto.setRows(5);
        textTexto.setWrapStyleWord(true);
        jScrollPane1.setViewportView(textTexto);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(botonAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(botonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {botonAceptar, botonCancelar});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(botonCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(botonAceptar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAceptarActionPerformed
        boton = "S";
        if (!textTexto.getText().equals("")) {
        }
        dispose();
    }//GEN-LAST:event_botonAceptarActionPerformed

    private void botonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarActionPerformed
        boton = "N";
        dispose();
    }//GEN-LAST:event_botonCancelarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            Utilidades.escribeLog("Error en el main de PantallaTextoIn " + " - " + ex.getMessage());
        } catch (InstantiationException ex) {
            Utilidades.escribeLog("Error en el main de PantallaTextoIn " + " - " + ex.getMessage());
        } catch (IllegalAccessException ex) {
            Utilidades.escribeLog("Error en el main de PantallaTextoIn " + " - " + ex.getMessage());
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            Utilidades.escribeLog("Error en el main de PantallaTextoIn " + " - " + ex.getMessage());
        }
        //</editor-fold>

        /*
         * Create and display the dialog
         */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PantallaTextoIn dialog = new PantallaTextoIn(ventanapadre, true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setLocationRelativeTo(ventanapadre);
                asignariconos();
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JButton botonAceptar;
    private static javax.swing.JButton botonCancelar;
    private javax.swing.JScrollPane jScrollPane1;
    private static javax.swing.JTextArea textTexto;
    // End of variables declaration//GEN-END:variables

    public static void asignariconos() {
        java.net.URL imgURL = PantallaTextoIn.class.getClassLoader().getResource("es/jscan/Pantallas/imagenes/si.png");
        Icon imgicon = new ImageIcon(imgURL);
        botonAceptar.setIcon(imgicon);

        imgURL = PantallaTextoIn.class.getClassLoader().getResource("es/jscan/Pantallas/imagenes/no.png");
        imgicon = new ImageIcon(imgURL);
        botonCancelar.setIcon(imgicon);
    }
}
