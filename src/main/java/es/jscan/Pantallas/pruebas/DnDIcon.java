/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.jscan.Pantallas.pruebas;

import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.TransferHandler;

class DragMouseAdapter extends MouseAdapter {

    public void mousePressed(MouseEvent e) {
        JComponent c = (JComponent) e.getSource();
        TransferHandler handler = c.getTransferHandler();
        handler.exportAsDrag(c, e, TransferHandler.COPY);
    }
}

public class DnDIcon {

    public static void main(String[] args) {
        JFrame f = new JFrame("Icon Drag & Drop");

        java.net.URL imgURL1 = DnDIcon.class.getResource("enviar.png");
        ImageIcon icon1 = new ImageIcon(imgURL1);
        java.net.URL imgURL2 = DnDIcon.class.getResource("scaner.jpg");
        ImageIcon icon2 = new ImageIcon(imgURL2);
        java.net.URL imgURL3 = DnDIcon.class.getResource("lupa.png");
        ImageIcon icon3 = new ImageIcon(imgURL3);

        JButton button = new JButton(icon2);

        JButton button1 = new JButton(icon2);
        JButton button2 = new JButton(icon3);
        
        JLabel label1 = new JLabel(icon1, JLabel.CENTER);
        JLabel label2 = new JLabel(icon3, JLabel.CENTER);

        MouseListener listener = new DragMouseAdapter();
        label1.addMouseListener(listener);
        label2.addMouseListener(listener);
        
        button1.addMouseListener(listener);
        button2.addMouseListener(listener);
        
        button1.setTransferHandler(new TransferHandler("icon"));
        button2.setTransferHandler(new TransferHandler("icon"));
        
        label1.setTransferHandler(new TransferHandler("icon"));
        button.setTransferHandler(new TransferHandler("icon"));
        label2.setTransferHandler(new TransferHandler("icon"));

        f.setLayout(new FlowLayout());
        
        f.add(button1);
        f.add(button2);
        
        f.add(label1);
        f.add(button);
        f.add(label2);
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.setVisible(true);
    }
}