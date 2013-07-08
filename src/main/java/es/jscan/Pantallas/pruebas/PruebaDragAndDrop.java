/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.jscan.Pantallas.pruebas;

import es.jscan.utilidades.Conectar;
import es.jscan.utilidades.Utilidades;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PruebaDragAndDrop {

    class Ventana extends JFrame {

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();

        public Ventana() {

            Container container = getContentPane();
            container.setLayout(null);
            Boton boton1 = new Boton("Mueveme");

            panel1.setBorder(BorderFactory.createLineBorder(Color.black));
            panel1.setBounds(10, 10, 100, 100);
            panel1.add(boton1);

            panel2.setBorder(BorderFactory.createLineBorder(Color.black));
            panel2.setBounds(270, 260, 100, 100);

            container.add(panel1);
            container.add(panel2);


        }
    }

    class Boton extends JButton implements MouseMotionListener {

        public Boton(String mensaje) {

            super.setText(mensaje);
            addMouseMotionListener(this);
        }

        public void mouseDragged(MouseEvent mme) {

            setLocation(this.getX() + mme.getX() - this.getWidth() / 2, this.getY() + mme.getY() - this.getHeight() / 2);

        }

        public void mouseMoved(MouseEvent mme) {
        }
    }

    public void correr() {

        Ventana window = new Ventana();
        window.setVisible(true);
        window.setSize(400, 400);
        window.setDefaultCloseOperation(window.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
/*        
        String campos[] = {"ECCCOD", "ECCNER", "ECCFER"};
        String cb = "NA4300013300000814680";
        String sql = "select ECCCOD,ECCNER,ECCFER from CODCORREOS where ECCCCO = '" + cb + "'";
        String eccfer = "";
        String expediente = "";
        String id_correos = "";
        Conectar conex = new Conectar();
        System.out.println(sql);
        try {
            String resultado[] = conex.ejecutarSqlMultiple(sql, campos);
            id_correos = resultado[0];
            expediente = resultado[1];
            String fecha[] = resultado[2].split("/");
            for (int n = 0; n < fecha.length; n++) {
                eccfer = eccfer + fecha[n];
            }
        } catch (Exception e) {
            System.out.println("Error acceso BBDD acuses. - " + e.getMessage());
        }

        System.out.println("ECCCOD: " + id_correos);
        System.out.println("ECCNER: " + expediente);
        System.out.println("ECCFER: " + eccfer);


        Utilidades util = new Utilidades();
        util.zipArchivos("c:\\tmp\\prueba.zip", "d:\\tmp\\sql_oscar", "*");

        String cuerpo = "Hola,\n\nMensaje de prueba desde Digita v1.0a. \n\n" + sql + "\n\nECCCOD: " + id_correos
                + "\nECCNER: " + expediente + "\nECCFER: " + eccfer + "\n\n\nUn saludo";

        String destinatarios = "julian.collado@externos.seap.minhap.es";
        destinatarios = "julian.collado@seap.minhap.es";
        util.enviarmail(destinatarios, "gestdocum.extranjeria@seap.minhap.es", cuerpo, "Prueba de email con adjunto", "c:\\tmp\\prueba.zip");

*/
//        PruebaDragAndDrop drag = new PruebaDragAndDrop();      
//        drag.correr();
        
        String c ="A";
        
        int valor = (int)(char)c.charAt(0);
        
        System.out.println(valor);
    }
}
