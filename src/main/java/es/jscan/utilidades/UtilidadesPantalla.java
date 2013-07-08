package es.jscan.utilidades;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;
import es.jscan.Pantallas.PantallaPrincipal;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.NullOpImage;
import javax.media.jai.OpImage;
import javax.media.jai.PlanarImage;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;

/**
 *
 * @author gestion.documental
 */
public class UtilidadesPantalla implements PropertyChangeListener {

    public void addImage(String fn, BufferedImage img, JScrollPane images) {
        Object md = img.getProperty("iiometadata");
        if ((md != Image.UndefinedProperty) && (md != null) && (md instanceof IIOMetadata)) {
            // new MetadataReader().read((IIOMetadata)md);
        }
        if (PantallaPrincipal.DEBUG) {
            Utilidades.escribeLog("Image.Width =" + img.getWidth());
            Utilidades.escribeLog("Image.Height =" + img.getHeight());
        }
        ImagePanel ip = new ImagePanel();
        ip.addPropertyChangeListener(this);
        ip.setImage(img);
        ip.setVisible(true);

        images.add(ip);
        //  images.add(sp);
    }

    public void open(String filename, JScrollPane images) throws IOException {
        long time = System.currentTimeMillis();

        String ext = filename.substring(filename.lastIndexOf('.') + 1);
        Iterator readers = ImageIO.getImageReadersByFormatName(ext);
        if (!readers.hasNext()) {
            throw new IOException(getClass().getName() + ".open:\n\tNo reader for format '" + ext + "' available.");
        }

        ImageReader reader = (ImageReader) readers.next();
        while (!reader.getClass().getName().startsWith("uk.co.mmscomputing") && readers.hasNext()) {// prefer our own reader
            reader = (ImageReader) readers.next();
        }
        File f = new File(filename);
        ImageInputStream iis = ImageIO.createImageInputStream(f);
        try {
            reader.setInput(iis, true);
            try {
                for (int i = 0; true; i++) {
                    IIOMetadata md = reader.getImageMetadata(i);
                    // if(md!=null){new MetadataReader().read(md);}
                    addImage(f.getName() + " " + i, reader.read(i), images);
                }
            } catch (IndexOutOfBoundsException ioobe) {
            }
        } catch (Error e) {
            Utilidades.escribeLog("9\b" + getClass().getName() + ".open:\n\t" + e.getMessage());
            throw e;
        } finally {
            iis.close();
        }
        time = System.currentTimeMillis() - time;
        if (PantallaPrincipal.DEBUG) {
            Utilidades.escribeLog("Opened : " + filename);
            Utilidades.escribeLog("Time used to load images : " + time);
        }
    }

    public IIOImage getIIOImage(ImageWriter writer, ImageWriteParam iwp, BufferedImage image) {
        ImageTypeSpecifier it = ImageTypeSpecifier.createFromRenderedImage(image);

        /*
         * try{ uk.co.mmscomputing.imageio.bmp.BMPMetadata
         * md=(uk.co.mmscomputing
         * .imageio.bmp.BMPMetadata)image.getProperty("iiometadata");
         * if(md!=null){ md.setXPixelsPerMeter(11812); // force 300 dpi for bmp
         * images md.setYPixelsPerMeter(11812); // works only with mmsc.bmp
         * package } }catch(Exception e){}
         */

        IIOMetadata md;
        Object obj = image.getProperty("iiometadata"); // if image is a
        // TwainBufferedImage
        // get metadata
        if ((obj != null) && (obj instanceof IIOMetadata)) {
            md = (IIOMetadata) obj;
        } else {
            md = writer.getDefaultImageMetadata(it, iwp);
        }
        return new IIOImage(image, null, md);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private BufferedImage getCompatibleImage(int w, int h) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        BufferedImage image = gc.createCompatibleImage(w, h);
        return image;
    }

    public Image ResizeImagen(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    public int numPaginasTIFF(String path) throws Exception {
        FileInputStream in = new FileInputStream(path);
        FileChannel channel = in.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate((int) channel.size());
        channel.read(buffer);

        SeekableStream stream = new ByteArraySeekableStream(buffer.array());
        String[] names = ImageCodec.getDecoderNames(stream);
        ImageDecoder dec = ImageCodec.createImageDecoder(names[0], stream, null);
        return dec.getNumPages();
    }

    public Image loadImagenTIFF(String path, int numpagina) throws Exception {
        javax.imageio.ImageIO.scanForPlugins();
        FileInputStream in = new FileInputStream(path);
        FileChannel channel = in.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate((int) channel.size());
        channel.read(buffer);

        SeekableStream stream = new ByteArraySeekableStream(buffer.array());
        String[] names = ImageCodec.getDecoderNames(stream);
        ImageDecoder dec = ImageCodec.createImageDecoder(names[0], stream, null);
        RenderedImage im = new NullOpImage(dec.decodeAsRenderedImage(numpagina),
                null,
                OpImage.OP_IO_BOUND,
                null);

        Image image = PlanarImage.wrapRenderedImage(im).getAsBufferedImage();
        return image;
    }

    public BufferedImage convertirBN(BufferedImage image) {
        BufferedImage blackAndWhiteImage = new BufferedImage(
                image.getWidth(null),
                image.getHeight(null),
                BufferedImage.TYPE_BYTE_BINARY);
        final Graphics2D g = (Graphics2D) blackAndWhiteImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return blackAndWhiteImage;
    }

    public BufferedImage convertirAGris(BufferedImage image) {
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ColorConvertOp op = new ColorConvertOp(cs, null);
        BufferedImage imagen = op.filter(image, null);
        return imagen;
    }

    private int r,g,b;
    private Color color;
    int umbral = 127;// 
    
     public BufferedImage set_Blanco_y_Negro(BufferedImage f){
        BufferedImage bn = new BufferedImage(f.getWidth(),f.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        //se traspasan los colores Pixel a Pixel
        for(int i=0;i<f.getWidth();i++)
          for(int j=0;j<f.getHeight();j++)
               bn.setRGB(i, j, f.getRGB(i, j));
        return bn;
   }

   public BufferedImage set_Blanco_y_Negro_con_Umbral(BufferedImage f){
        BufferedImage bn = new BufferedImage(f.getWidth(),f.getHeight(), BufferedImage.TYPE_BYTE_BINARY);        
        //se traspasan los colores Pixel a Pixel
        for(int i=0;i<f.getWidth();i++){
          for(int j=0;j<f.getHeight();j++){
              Color color = new Color(f.getRGB(i, j));
               //se extraen los valores RGB
                r = color.getRed();
                g = color.getGreen();
                b = color.getBlue();
                //dependiendo del valor del umbral, se van separando los
                // valores RGB a 0 y 255  
                r =(r>umbral)? 255: 0;
                g =(g>umbral)? 255: 0;
                b =(b>umbral)? 255: 0;
                bn.setRGB(i, j, new Color(r,g,b).getRGB());            
          }
        }        
        return bn;
    }
    
    public BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels; for this method's
        // implementation, see Determining If an Image Has Transparent Pixels
        boolean hasAlpha = hasAlpha(image);

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                    image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }

    public static boolean hasAlpha(Image image) {
        // If buffered image, the color model is readily available
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage) image;
            return bimage.getColorModel().hasAlpha();
        }

        // Use a pixel grabber to retrieve the image's color model;
        // grabbing a single pixel is usually sufficient
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        }

        // Get the image's color model
        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
    }
}
