package es.jscan.utilidades;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

public class VisorPdfs {

    public void verPdf(String fichero) {
        String filepath = fichero;
        // build a controller
        SwingController controller = new SwingController();
        // Build a SwingViewFactory configured with the controller
        SwingViewBuilder factory = new SwingViewBuilder(controller);
        factory.buildViewMenu();
        JPanel viewerComponentPanel = factory.buildViewerPanel();
        viewerComponentPanel.remove(factory.buildSaveAsFileButton());
        viewerComponentPanel.remove(factory.buildAnnotationlToolBar());
        viewerComponentPanel.remove(factory.buildAnnotationUtilityToolBar());
        viewerComponentPanel.remove(factory.buildAnnotationPanel());
        // add copy keyboard command
        ComponentKeyBinding.install(controller, viewerComponentPanel);
        // add interactive mouse link annotation support via callback
        controller.getDocumentViewController().setAnnotationCallback(
                new org.icepdf.ri.common.MyAnnotationCallback(
                controller.getDocumentViewController()));


        JFrame window = new JFrame("Visor de PDFs");
        window.getContentPane().add(viewerComponentPanel);
        window.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        window.setVisible(true);
        window.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        try {
            window.setIconImage(new ImageIcon(getLogo()).getImage());
        } catch (NullPointerException e) {
            Utilidades.escribeLog("\nError cargando el Logo " + e.getMessage() + "\n");
        }
        // Open a PDF document to view
        controller.openDocument(filepath);
    }

    protected static Image getLogo() {
        java.net.URL imgURL = VisorPdfs.class.getClassLoader().getResource("es/jscan/Pantallas/imagenes/scaner.jpg");
        if (imgURL != null) {
            return new ImageIcon(imgURL).getImage();
        } else {
            return null;
        }
    }
}