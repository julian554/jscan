package es.jscan.Pantallas.pruebas;

import es.jscan.utilidades.Utilidades;
import es.jscan.utilidades.VisorPdfs;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CodeDecode {
    public static void main(String[] args) {
        Utilidades util = new Utilidades();
        String cadena = util.codificaBase64("extrangn");
        cadena= util.asciiToHex(cadena);
        System.out.println("extrangn - "+cadena);
        cadena= util.hexToASCII(cadena);
        cadena = util.decodificaBase64(cadena);
        System.out.println("extrangn decode - "+cadena);
        cadena = util.codificaBase64("broker1");
        cadena= util.asciiToHex(cadena);
        System.out.println("broker1 - "+cadena);
                cadena= util.hexToASCII(cadena);
        cadena = util.decodificaBase64(cadena);
        System.out.println("broker1 decode - "+cadena);
        cadena = util.codificaBase64("documentum");
        cadena= util.asciiToHex(cadena);
        System.out.println("documentum - "+cadena);
        cadena= util.hexToASCII(cadena);
        cadena = util.decodificaBase64(cadena);
        System.out.println("documentum decode - "+cadena);
        cadena = util.codificaBase64("/home/broker1/dev/opt/activemq/data");
        cadena= util.asciiToHex(cadena);
        System.out.println("/home/broker1/dev/opt/activemq/data - "+cadena);
        cadena= util.hexToASCII(cadena);
        cadena = util.decodificaBase64(cadena);
        System.out.println("/home/broker1/dev/opt/activemq/data decode - "+cadena);
        ArrayList<String> lista = new ArrayList<String>(); 
        lista.add("Pedro");
        lista.add("Luis");
        lista.add("Pedro Alberto");
        lista.add("Juan");
        lista.add("Alfredo");
        lista.add("Pedro");
        lista.add("Luis Antonio");
        lista.add("Pedro");
        lista.add("Ana");
        lista.add("Ana María");
        lista.add("Pedro");
        lista.add("María");
        lista.add("Luis");
        lista.add("Juan");
        lista.add("Ana");
        
        System.out.println(lista);
        
        lista = util.listaTextoSinDuplicados(lista);
        
        System.out.println("\nLista sin duplicados\n");
        System.out.println(lista);
        
        VisorPdfs vpdf = new VisorPdfs();
        try {
            vpdf.verPdf("c:\\tmp\\PAR-21MAA_Operation_Manual.pdf");
        } catch (Exception ex) {
            Logger.getLogger(CodeDecode.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        
    }
}
