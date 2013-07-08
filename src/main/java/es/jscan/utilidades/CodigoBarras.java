package es.jscan.utilidades;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.GenericMultipleBarcodeReader;
import com.google.zxing.multi.MultipleBarcodeReader;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import com.google.zxing.oned.Code128Reader;
import es.jscan.Pantallas.PantallaPrincipal;
import java.awt.image.BufferedImage;
import java.util.*;

public class CodigoBarras {

    private static final int MAX_PIXELS = 80000000;
    static final Hashtable<DecodeHintType, Object> HINTS;
    public Utilidades utilidades = new Utilidades();

    static {
        Collection<BarcodeFormat> possibleFormats = new Vector<BarcodeFormat>(1);
        possibleFormats.add(BarcodeFormat.CODE_128);

        HINTS = new Hashtable<DecodeHintType, Object>(3);
        HINTS.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        HINTS.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
        HINTS.put(DecodeHintType.POSSIBLE_FORMATS, possibleFormats);

    }

    public static void main(final String[] args) {
        try {
            final QRCodeMultiReader reader = new QRCodeMultiReader();

            Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            //  hints.put(DecodeHintType.POSSIBLE_FORMATS, Boolean.TRUE);

            //       final BufferedImage image = ImageIO.read(new File("C:\\Users\\julian\\Documents\\Success_Original_AND_Custom_API_600DPI_1.tif"));

            UtilidadesPantalla pantutil = new UtilidadesPantalla();


            BufferedImage image = pantutil.toBufferedImage(pantutil.loadImagenTIFF("C:\\Users\\julian\\Documents\\Success_Original_AND_Custom_API_600DPI_1.tif", 0));

//            LuminanceSource source = new BufferedImageLuminanceSource(image);
//            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            LuminanceSource source = new BufferedImageLuminanceSource(image);

            //   final Result[] results = reader.decodeMultiple(bitmap, hints);
            BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
            Code128Reader rd = new Code128Reader();
            //    Utilidades.escribeLog("Code128Reader---" + rd);
            Result resultado = rd.decode(bitmap, HINTS);

            //      Result resultado = reader.decode(bitmap);

            if (PantallaPrincipal.DEBUG) {
                Utilidades.escribeLog("Resultado: " + resultado.getText());
            }
            //     Utilidades.escribeLog("DecodeMulti DONE, " + (results != null ? results.length : 0) + " results");
        } catch (Exception e) {
            Utilidades.escribeLog("Error de Resultado: " + (e != null ? e.getMessage() : e.toString()));
            e.printStackTrace();
        }
//        UtilidadesPantalla pantutil = new UtilidadesPantalla();
//        try {
//            BufferedImage image = pantutil.toBufferedImage(pantutil.loadImagenTIFF("C:\\Users\\julian\\Documents\\Success_Original_AND_Custom_API_600DPI_1.tif", 0));
//            List<Result> results = getResults(image);
//
//        } catch (Exception ex) {
//            Utilidades.escribeLog("Error al leer Código de Barras "  + " - " + ex.getMessage());
//        }


    }

    public ArrayList<String> leerCodigo(String fichero) {
        //  ZXing  Multi-format 1D/2D barcode image processing library
        String codigo = "";
        ArrayList<String> resultadoCB = new ArrayList<String>();
        UtilidadesPantalla pantutil = new UtilidadesPantalla();
        BufferedImage image = null;
        try {
            image = pantutil.toBufferedImage(pantutil.loadImagenTIFF(fichero, 0));
        } catch (Exception ex) {
            Utilidades.escribeLog("Error al abrir Imagen para leer Código de Barras " + " - " + ex.getMessage());
        }

        int tipoimagen = image.getType();

        switch (tipoimagen) {
            case java.awt.image.BufferedImage.TYPE_BYTE_GRAY:
            case java.awt.image.BufferedImage.TYPE_BYTE_INDEXED:
                image = pantutil.convertirBN(image);
        }

        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
//        BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
        try {
            Code128Reader rd = new Code128Reader();
            MultipleBarcodeReader multiReader = new GenericMultipleBarcodeReader(rd);
            Result[] resultados = multiReader.decodeMultiple(bitmap, HINTS);
            if (PantallaPrincipal.DEBUG) {
                Utilidades.escribeLog("Nº de CB: " + resultados.length);
            }
            /*
             * Result resultado = rd.decode(bitmap, HINTS); codigo =
             * resultado.getText();
             */

            for (int i = 0; i < resultados.length; i++) {
                if (PantallaPrincipal.DEBUG) {
                    Utilidades.escribeLog("Resultado CB" + (i + 1) + ": " + resultados[i].getText());
                }
                resultadoCB.add(resultados[i].getText());
            }
            codigo = "" + resultados.length;
//            if (resultados.length > 0) {
//                codigo = resultados[resultados.length - 1].getText();
//            }
        } catch (Exception ex) {
            if (PantallaPrincipal.DEBUG) {
                Utilidades.escribeLog("Error al leer código de barras Code128: " + ex.getMessage());
            }
        }

        if (codigo == null || codigo.isEmpty()) {
            QRCodeMultiReader reader = new QRCodeMultiReader();
            Result resultado;
            try {
                resultado = reader.decode(bitmap, HINTS);
                codigo = resultado.getText();
                resultadoCB.add(codigo);
            } catch (NotFoundException ex1) {
                if (PantallaPrincipal.DEBUG) {
                    Utilidades.escribeLog("Error al leer código de barras NO Code128: " + ex1.getMessage());
                }
            } catch (ChecksumException ex1) {
                if (PantallaPrincipal.DEBUG) {
                    Utilidades.escribeLog("Error al leer código de barras NO Code128: " + ex1.getMessage());
                }
            } catch (FormatException ex1) {
                if (PantallaPrincipal.DEBUG) {
                    Utilidades.escribeLog("Error al leer código de barras NO Code128: " + ex1.getMessage());
                }
            }
        }

        System.gc();
        return resultadoCB;
    }

    private static List<Result> getResults(BufferedImage image) throws Exception {
        if (image == null) {
            Utilidades.escribeLog("Error in getResults() BufferedImage Image object is null");
            throw new Exception("Error in getResults() BufferedImage Image object is null");
        }
        if (image.getHeight() <= 1 || image.getWidth() <= 1 || image.getHeight() * image.getWidth() > MAX_PIXELS) {
            Utilidades.escribeLog("Dimensions too large: " + image.getWidth() + 'x' + image.getHeight());
            throw new Exception("Dimensions too large: " + image.getWidth() + 'x' + image.getHeight());
        }
        List<Result> results = new ArrayList<Result>(1);

        LuminanceSource source = new BufferedImageLuminanceSource(image);
        //LuminanceSource source = new BufferedImageLuminanceSource(image, image.getWidth() >> 1, 0 , image.getWidth() >> 1, image.getHeight() >> 3);        

        ReaderException savedException = null;

        Utilidades.escribeLog(" --------------------- TestCase START --------------------- ");

        try {
            BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
            Code128Reader rd = new Code128Reader();
            Utilidades.escribeLog("Code128Reader---" + rd);
            Result rs = rd.decode(bitmap, HINTS);
            Utilidades.escribeLog("rs---" + rs);
            if (rs != null) {
                results.addAll(Arrays.asList(rs));
                Utilidades.escribeLog("results not null---" + results);
            }
        } catch (ReaderException re) {
            savedException = re;
            Utilidades.escribeLog("Barcode NOT Found " + re);
        }

        /*
         * if (results.isEmpty())
         */ {
//        	try {
//                // Customized version of Code128Reader
//        		// Does Exhaustive search, something like DecodeHintType.TRY_HARDEST which is not currently implemented by API
//        		BinaryBitmap bitmap2 = new BinaryBitmap(new GlobalHistogramBinarizer(source));
//                CustomizedCode128Reader rd = new CustomizedCode128Reader();
//                Utilidades.escribeLog("CustomizedCode128Reader with GlobalHistogramBinarizer---"+rd);
//                Result rs = rd.decode(bitmap2, HINTS);
//                Utilidades.escribeLog("Barcode Result : " + rs);
//                if (rs != null) {
//                	results.addAll(Arrays.asList(rs));                    
//                }
//            } catch (ReaderException re) {
//                savedException = re;
//                Utilidades.escribeLog("Barcode NOT Found "+ re);
//            }
//        } 
//        
        /*
             * if (results.isEmpty())
             */ {
//        	try {
//                // Customized version of CustomGlobalHistogramBinarizer No Filtering applied 
//        		// Customized version of Code128Reader
//        		BinaryBitmap bitmap2 = new BinaryBitmap(new CustomGlobalHistogramBinarizer(source));
//                CustomizedCode128Reader rd = new CustomizedCode128Reader();
//                Utilidades.escribeLog("CustomizedCode128Reader with CustomGlobalHistogramBinarizer---"+rd);
//                Result rs = rd.decode(bitmap2, HINTS);
//                Utilidades.escribeLog("Barcode Result : " + rs);
//                if (rs != null) {
//                	results.addAll(Arrays.asList(rs));                    
//                }
//            } catch (ReaderException re) {
//                savedException = re;  
//                Utilidades.escribeLog("Barcode NOT Found "+ re);
//            }
            }

//            if (results.isEmpty()) {
//                handleException(savedException);
//            }

            Utilidades.escribeLog(" --------------------- TestCase END --------------------- ");
            return results;
        }



//    private void handleException(Exception exc) throws IOException, ReaderException {
//        if (exc instanceof NotFoundException) {
//            Utilidades.escribeLog("Barcode Not found:  " + exc);
//        } else if (exc instanceof FormatException) {
//            Utilidades.escribeLog("Format problem: " + exc);
//        } else if (exc instanceof ChecksumException) {
//            Utilidades.escribeLog("Checksum problem: " + exc);
//        } else {
//            Utilidades.escribeLog("Unknown problem: " + exc);
//        }
//    }

    }
}
