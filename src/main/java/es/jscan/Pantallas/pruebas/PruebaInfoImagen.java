/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.jscan.Pantallas.pruebas;

import es.jscan.utilidades.ImageInfo;
import java.text.DecimalFormat;

/**
 *
 * @author julian.collado
 */
public class PruebaInfoImagen {

    /**
     * @param args the command line arguments
     */
    public static DecimalFormat decimalformat = new DecimalFormat("00.00");
    public static void main(String[] args) {
        ImageInfo imageninfo = new ImageInfo("\\\\jca\\discoc\\Users\\julian\\Documents\\Codigo20de20barras.jpg");
        System.out.println("Formato de Imagen: " + imageninfo.getFormatName());
        System.out.println("Tipo mime: " + imageninfo.getMimeType());
        System.out.println("Tamaño en pixeles: " + imageninfo.getWidth() + " x " + imageninfo.getHeight());
        System.out.println("Tamaño en cm: " + decimalformat.format(imageninfo.getPhysicalWidthCm()) + " x " +  decimalformat.format(imageninfo.getPhysicalHeightCm()));
        System.out.println("Resolución: " + imageninfo.getPhysicalWidthDpi()+" x "+ imageninfo.getPhysicalHeightDpi());
        System.out.println("Bits por Pixel: " + imageninfo.getBitsPerPixel());
        System.out.println("Número de Imágenes: " +imageninfo.getNumberOfImages());
    }
}
