package es.jscan.Pantallas.pruebas;

import com.google.common.base.CharMatcher;
import com.sun.pdfview.Cache;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFObject;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PagePanel;
import es.jscan.utilidades.Utilidades;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import javax.swing.plaf.ActionMapUIResource;
//import oracle.net.aso.p;

public class VisorPdf extends JPanel {

    private Utilidades utilidades = new Utilidades();

    private static enum Navigation {

        GO_FIRST_PAGE, FORWARD, BACKWARD, GO_LAST_PAGE, GO_N_PAGE
    }
    private static final CharMatcher POSITIVE_DIGITAL = CharMatcher.anyOf("0123456789");
    private static final String GO_PAGE_TEMPLATE = "%s de %s";
    private static final int FIRST_PAGE = 1;
    private int currentPage = FIRST_PAGE;
    private JButton btnFirstPage;
    private JButton btnPreviousPage;
    private JTextField txtGoPage;
    private JButton btnNextPage;
    private JButton btnLastPage;
    private PagePanel pagePanel;
    private PDFFile pdfFile;

    public VisorPdf() {
        initial();
    }

    private void initial() {
        setLayout(new BorderLayout(0, 0));
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(topPanel, BorderLayout.NORTH);
        btnFirstPage = createButton("");
        topPanel.add(btnFirstPage);

        btnPreviousPage = createButton("");
        topPanel.add(btnPreviousPage);

        txtGoPage = new JTextField(10);
        txtGoPage.setHorizontalAlignment(JTextField.CENTER);
        topPanel.add(txtGoPage);

        btnNextPage = createButton("");
        topPanel.add(btnNextPage);

        btnLastPage = createButton("");
        topPanel.add(btnLastPage);

        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);
        JPanel viewPanel = new JPanel(new BorderLayout(0, 0));
        scrollPane.setViewportView(viewPanel);

        pagePanel = new PagePanel();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        pagePanel.setPreferredSize(screenSize);
        viewPanel.add(pagePanel, BorderLayout.CENTER);

        disableAllNavigationButton();

        btnNextPage.setMnemonic(KeyEvent.VK_PAGE_DOWN);
        btnPreviousPage.setMnemonic(KeyEvent.VK_PAGE_UP);

        btnFirstPage.addActionListener(new PageNavigationListener(Navigation.GO_FIRST_PAGE));
        
        btnPreviousPage.addActionListener(new PageNavigationListener(Navigation.BACKWARD));
        btnNextPage.addActionListener(new PageNavigationListener(Navigation.FORWARD));
        viewPanel.addKeyListener(new PageNavigationListener(Navigation.FORWARD));
        btnLastPage.addActionListener(new PageNavigationListener(Navigation.GO_LAST_PAGE));
        txtGoPage.addActionListener(new PageNavigationListener(Navigation.GO_N_PAGE));
        asignarIconos();
    }

    private void asignarIconos() {
        java.net.URL imgURL = VisorPdf.class.getClassLoader().getResource("es/jscan/Pantallas/imagenes/primero.png");
        Icon imgicon = new ImageIcon(imgURL);
        this.btnFirstPage.setIcon(imgicon);

        imgURL = VisorPdf.class.getClassLoader().getResource("es/jscan/Pantallas/imagenes/anterior.png");
        imgicon = new ImageIcon(imgURL);
        this.btnPreviousPage.setIcon(imgicon);

        imgURL = VisorPdf.class.getClassLoader().getResource("es/jscan/Pantallas/imagenes/siguiente.png");
        imgicon = new ImageIcon(imgURL);
        this.btnNextPage.setIcon(imgicon);

        imgURL = VisorPdf.class.getClassLoader().getResource("es/jscan/Pantallas/imagenes/ultimo.png");
        imgicon = new ImageIcon(imgURL);
        this.btnLastPage.setIcon(imgicon);
    }

    public void VisualizarPdf(String fichero) {
        try {
            JFrame frame = new JFrame("Visor de PDFs");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //load a pdf from a byte buffer
            File file = new File(fichero);
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            FileChannel channel = raf.getChannel();
            ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            final PDFFile pdffile = new PDFFile(buf);
            VisorPdf pdfViewer = new VisorPdf();
            pdfViewer.setPDFFile(pdffile);
            frame.add(pdfViewer);
            frame.pack();
            frame.setVisible(true);

            PDFPage page = pdffile.getPage(0);
            pdfViewer.getPagePanel().showPage(page);
        } catch (Exception e) {
            System.out.println("Error");
            // e.printStackTrace();
        }
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(55, 20));

        return button;
    }

    private void disableAllNavigationButton() {
        btnFirstPage.setEnabled(false);
        btnPreviousPage.setEnabled(false);
        btnNextPage.setEnabled(false);
        btnLastPage.setEnabled(false);
    }

    private boolean isMoreThanOnePage(PDFFile pdfFile) {
        return pdfFile.getNumPages() > 1;
    }

    private class PageNavigationListener implements ActionListener, KeyListener {

        private final Navigation navigation;

        private PageNavigationListener(Navigation navigation) {
            this.navigation = navigation;
        }

        public void actionPerformed(ActionEvent e) {
            if (pdfFile == null) {
                return;
            }
            int numPages = pdfFile.getNumPages();
            if (numPages <= 1) {
                disableAllNavigationButton();
            } else {
                if ((navigation == Navigation.FORWARD) && hasNextPage(numPages)) {
                    goPage(currentPage, numPages);
                }

                if (navigation == Navigation.GO_LAST_PAGE) {
                    goPage(numPages, numPages);
                }

                if (navigation == Navigation.BACKWARD && hasPreviousPage()) {
                    goPage(currentPage, numPages);
                }

                if (navigation == Navigation.GO_FIRST_PAGE) {
                    goPage(FIRST_PAGE, numPages);
                }

                if (navigation == Navigation.GO_N_PAGE) {
                    String text = txtGoPage.getText();
                    boolean isValid = false;
                    if (text != null) {
                        if (!text.isEmpty()) {
                            boolean isNumber = POSITIVE_DIGITAL.matchesAllOf(text);
                            if (isNumber) {
                                int pageNumber = Integer.valueOf(text);
                                if (pageNumber >= 1 && pageNumber <= numPages) {
                                    goPage(Integer.valueOf(text), numPages);
                                    isValid = true;
                                }
                            }
                        }
                    }

                    if (!isValid) {
//                        utilidades.mensaje(this,"Error" ,"Invalid page number '%s' in this document");
                        txtGoPage.setText(format(GO_PAGE_TEMPLATE, currentPage, numPages));
                    }
                }
            }
        }

        private void goPage(int pageNumber, int numPages) {
            currentPage = pageNumber;
            try {
                PDFPage page = pdfFile.getPage(currentPage);
                pagePanel.showPage(page);
            } catch (Exception ex) {
                ex.getMessage();
            }

            boolean notFirstPage = isNotFirstPage();
            btnFirstPage.setEnabled(notFirstPage);
            btnPreviousPage.setEnabled(notFirstPage);
            txtGoPage.setText(format(GO_PAGE_TEMPLATE, currentPage, numPages));
            boolean notLastPage = isNotLastPage(numPages);
            btnNextPage.setEnabled(notLastPage);
            btnLastPage.setEnabled(notLastPage);
        }

        private boolean hasNextPage(int numPages) {
            return (++currentPage) <= numPages;
        }

        private boolean hasPreviousPage() {
            return (--currentPage) >= FIRST_PAGE;
        }

        private boolean isNotLastPage(int numPages) {
            return currentPage != numPages;
        }

        private boolean isNotFirstPage() {
            return currentPage != FIRST_PAGE;
        }

        @Override
        public void keyTyped(KeyEvent e) {
            System.out.println(""+e.getKeyChar());

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (pdfFile == null) {
                return;
            }

            int numPages = pdfFile.getNumPages();
            if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
                if (hasNextPage(numPages)) {
                    goPage(currentPage, numPages);
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    public PagePanel getPagePanel() {
        return pagePanel;
    }

    public void setPDFFile(PDFFile pdfFile) {
        this.pdfFile = pdfFile;
        currentPage = FIRST_PAGE;
        disableAllNavigationButton();
        txtGoPage.setText(format(GO_PAGE_TEMPLATE, FIRST_PAGE, pdfFile.getNumPages()));
        boolean moreThanOnePage = isMoreThanOnePage(pdfFile);
        btnNextPage.setEnabled(moreThanOnePage);
        btnLastPage.setEnabled(moreThanOnePage);
    }

    public static String format(String template, Object... args) {
        template = String.valueOf(template); // null -> "null"
        // start substituting the arguments into the '%s' placeholders
        StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
        int templateStart = 0;
        int i = 0;
        while (i < args.length) {
            int placeholderStart = template.indexOf("%s", templateStart);
            if (placeholderStart == -1) {
                break;
            }
            builder.append(template.substring(templateStart, placeholderStart));
            builder.append(args[i++]);
            templateStart = placeholderStart + 2;
        }
        builder.append(template.substring(templateStart));

        // if we run out of placeholders, append the extra args in square braces
        if (i < args.length) {
            builder.append(" [");
            builder.append(args[i++]);
            while (i < args.length) {
                builder.append(", ");
                builder.append(args[i++]);
            }
            builder.append(']');
        }

        return builder.toString();
    }
}
