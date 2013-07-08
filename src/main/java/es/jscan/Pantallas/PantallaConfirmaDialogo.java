package es.jscan.Pantallas;

import es.jscan.utilidades.Utilidades;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author julian.collado
 */
public class PantallaConfirmaDialogo extends javax.swing.JDialog {

    public static javax.swing.JDialog ventanapadre = null;
    public static javax.swing.JFrame ventanapadreF = null;
    public String boton = "N";

    public PantallaConfirmaDialogo(javax.swing.JDialog parent, boolean modal) {
        super(parent, modal);
        ventanapadre = parent;
        initComponents();
        asignariconos();
        setLocationRelativeTo(ventanapadre);
        repaint();
    }

    public PantallaConfirmaDialogo(javax.swing.JFrame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        asignariconos();
        setLocationRelativeTo(parent);
        repaint();
    }

    public boolean respuesta() {
        return (boton.equals("S") ? true : false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        botonSi = new javax.swing.JButton();
        botonNo = new javax.swing.JButton();
        etiqueta = new javax.swing.JLabel();
        labelIcono = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        botonSi.setMnemonic('S');
        botonSi.setText("SI");
        botonSi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSiActionPerformed(evt);
            }
        });

        botonNo.setMnemonic('N');
        botonNo.setText("NO");
        botonNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonNoActionPerformed(evt);
            }
        });

        etiqueta.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiqueta.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        labelIcono.setMaximumSize(new java.awt.Dimension(32, 32));
        labelIcono.setMinimumSize(new java.awt.Dimension(32, 32));
        labelIcono.setPreferredSize(new java.awt.Dimension(32, 32));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(botonSi, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                .addComponent(botonNo, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42))
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(labelIcono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(etiqueta, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {botonNo, botonSi});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(etiqueta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelIcono, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(botonNo, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(botonSi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonSiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSiActionPerformed
        boton = "S";
        dispose();
    }//GEN-LAST:event_botonSiActionPerformed

    private void botonNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonNoActionPerformed
        boton = "N";
        dispose();
    }//GEN-LAST:event_botonNoActionPerformed

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
            Utilidades.escribeLog("Error en el main de PantallaConfirmaDialogo " + " - " + ex.getMessage());
        } catch (InstantiationException ex) {
            Utilidades.escribeLog("Error en el main de PantallaConfirmaDialogo " + " - " + ex.getMessage());
        } catch (IllegalAccessException ex) {
            Utilidades.escribeLog("Error en el main de PantallaConfirmaDialogo " + " - " + ex.getMessage());
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            Utilidades.escribeLog("Error en el main de PantallaConfirmaDialogo " + " - " + ex.getMessage());
        }
        //</editor-fold>

        /*
         * Create and display the dialog
         */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PantallaConfirmaDialogo dialog = null;
                if (ventanapadre != null) {
                    dialog = new PantallaConfirmaDialogo(ventanapadre, true);
                    dialog.setLocationRelativeTo(ventanapadre);
                } else {
                    dialog = new PantallaConfirmaDialogo(ventanapadreF, true);
                    dialog.setLocationRelativeTo(ventanapadreF);
                }

                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });

                asignariconos();
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JButton botonNo;
    private static javax.swing.JButton botonSi;
    public javax.swing.JLabel etiqueta;
    private static javax.swing.JLabel labelIcono;
    // End of variables declaration//GEN-END:variables

    public static void asignariconos() {
        java.net.URL imgURL = PantallaConfirmaDialogo.class.getClassLoader().getResource("es/jscan/Pantallas/imagenes/si.png");
        Icon imgicon = new ImageIcon(imgURL);
        botonSi.setIcon(imgicon);

        imgURL = PantallaConfirmaDialogo.class.getClassLoader().getResource("es/jscan/Pantallas/imagenes/no.png");
        imgicon = new ImageIcon(imgURL);
        botonNo.setIcon(imgicon);
        imgURL = PantallaConfirmaDialogo.class.getClassLoader().getResource("es/jscan/Pantallas/imagenes/interroga.png");
        imgicon = new ImageIcon(imgURL);
   //     labelIcono = new JLabel(imgicon);
        labelIcono.setIcon(imgicon);
    }
}
