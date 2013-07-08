package es.jscan.Pantallas;

import es.jscan.utilidades.Utilidades;
import es.jscan.utilidades.UtilidadesPantalla;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.awt.image.RescaleOp;
import javax.swing.JPanel;
import org.imgscalr.Scalr;

@SuppressWarnings("serial")
public class PanelDibujo extends JPanel {
    
    Image img;
    Image imgtemp;
    BufferedImage imgmemoria;
    Graphics2D g2D;
    int escalaX = 0;
    int escalaY = 0;
    UtilidadesPantalla pantutil = new UtilidadesPantalla();
    
    public PanelDibujo(BufferedImage f) {
        this.img = f;
        this.imgtemp = f;
        this.setOpaque(true);
        this.setSize(f.getWidth(), f.getHeight());
        this.setVisible(true);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (img != null) {
            imgmemoria = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
            g2D = imgmemoria.createGraphics();
            //  g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2D.drawImage(imgtemp, 0, 0, imgtemp.getWidth(this), imgtemp.getHeight(this), this);
            g2.drawImage(imgmemoria, 0, 0, this);
        }
    }
    
    public void zoomMas(int zoom) {
        //      imgtemp=img;
        escalaX = (int) (imgtemp.getWidth(this) * (zoom / 100f));
        escalaY = (int) (imgtemp.getHeight(this) * (zoom / 100f));
        imgmemoria = pantutil.toBufferedImage(img);
        this.imgtemp = org.imgscalr.Scalr.resize(imgmemoria, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, (int) (imgtemp.getWidth(this) + escalaX), (int) (imgtemp.getHeight(this) + escalaY));
        //this.imgtemp = imgtemp.getScaledInstance((int) (imgtemp.getWidth(this) + escalaX), (int) (imgtemp.getHeight(this) + escalaY), Image.SCALE_AREA_AVERAGING);
        this.setSize(imgtemp.getWidth(this), imgtemp.getHeight(this));
        
    }
    
    public void zoomMenos(int zoom) {
        //     imgtemp=img;
        escalaX = (int) (imgtemp.getWidth(this) * (zoom / 100f));
        escalaY = (int) (imgtemp.getHeight(this) * (zoom / 100f));
        imgmemoria = pantutil.toBufferedImage(img);
        this.imgtemp = org.imgscalr.Scalr.resize(imgmemoria, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, (int) (imgtemp.getWidth(this) - escalaX), (int) (imgtemp.getHeight(this) - escalaY));
        //    this.imgtemp = imgtemp.getScaledInstance((int) (imgtemp.getWidth(this) - escalaX), (int) (imgtemp.getHeight(this) - escalaY), Image.SCALE_AREA_AVERAGING);
        this.setSize(imgtemp.getWidth(this), imgtemp.getHeight(this));
        
    }
    
    public void brillo(float valor) {
        float brightenFactor = valor;
        imgmemoria = pantutil.toBufferedImage(img);
        RescaleOp op = new RescaleOp(brightenFactor, 0, null);
        imgmemoria = op.filter(imgmemoria, imgmemoria);
        this.imgtemp = imgmemoria;
    }
    
    public void rotar() {
        this.imgtemp = org.imgscalr.Scalr.rotate(imgmemoria, Scalr.Rotation.CW_90);
        this.setSize(imgtemp.getWidth(this), imgtemp.getHeight(this));
        
    }
    
    public void zoom100() {
        this.imgtemp = img;
        imgmemoria = pantutil.toBufferedImage(img);
        this.setSize(imgtemp.getWidth(this), imgtemp.getHeight(this));
        
    }
    
    public void ajustarAncho(int ancho) {
        //     this.imgtemp = img.getScaledInstance((int) ancho, (int) (img.getHeight(this) * ancho / img.getWidth(this)), Image.SCALE_AREA_AVERAGING);
        imgmemoria = pantutil.toBufferedImage(img);
        this.imgtemp = org.imgscalr.Scalr.resize(imgmemoria, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, (int) ancho, (int) (img.getHeight(this) * ancho / img.getWidth(this)));
        this.setSize(imgtemp.getWidth(this), imgtemp.getHeight(this));
        
    }

    // value where we can consider that this is a blank image
    // can be much higher depending of the TIF source 
    // (ex. scanner or fax)
    public static final int BLANK_THRESHOLD = 500000;

    public static boolean esPaginaBlanco(BufferedImage bi) {
        long count = 0;
        long total = 0;
        double totalVariance = 0;
        double stdDev = 0;
        int height = bi.getHeight();
        int width = bi.getWidth();
        
        int[] pixels = new int[width * height];
        
        
        try {
            PixelGrabber pg = new PixelGrabber(bi, 0, 0, width, height, pixels, 0, width);
            pg.grabPixels();
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    count++;
                    int pixel = pixels[j * width + i];
                    int red = (pixel >> 16) & 0xff;
                    int green = (pixel >> 8) & 0xff;
                    int blue = (pixel) & 0xff;
                    int pixelValue = new Color(red, green, blue, 0).getRGB();
                    total += pixelValue;
                    double avg = total / count;
                    totalVariance += Math.pow(pixelValue - avg, 2);
                    stdDev = Math.sqrt(totalVariance / count);
                }
            }
        } catch (Exception ex) {
            Utilidades.escribeLog("Error al detectar página en blanco - Error " + ex.getMessage());
        }
        Utilidades.escribeLog("Desviación: " + stdDev + (stdDev < BLANK_THRESHOLD ? " -  Eliminada página en blanco":""));
        return (stdDev < BLANK_THRESHOLD);
    }
}