/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.jscan.utilidades;

import es.jscan.Beans.CampoBean; // |
import es.jscan.Beans.ProcesoBean;
import es.jscan.Beans.ProcesosBean;
import es.jscan.Pantallas.PantallaPrincipal;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 *
 * @author gestion documental
 */
public class UtilXml {
    public ProcesosBean cargarXmlProcesos(String Fichero) {
        //Se crea un SAXBuilder para poder parsear el archivo
        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(Fichero);
        try {
            //Se crea el documento a traves del archivo
            Document document = (Document) builder.build(xmlFile);

            int numelem = document.getContentSize();

            //Se obtiene la raiz 'procesos'
            Element rootNode = document.getRootElement();

            //Se obtiene la lista de hijos de la raiz 'procesos'
            List list = rootNode.getChildren("proceso");

            //Se recorre la lista de hijos de 'procesos'

            ProcesosBean procesosBean = new ProcesosBean();


            ArrayList arrayProceso = new ArrayList();
            for (int i = 0; i < list.size(); i++) // cuenta los PROCESO_
            {

                ProcesoBean procesoBean = new ProcesoBean();

                //Se obtiene el elemento 'proceso'
                Element proceso = (Element) list.get(i);

                //Se obtiene el atributo 'nombre' que esta en el tag 'proceso'
                String nombreProceso = proceso.getAttributeValue("nombre");

                if (PantallaPrincipal.DEBUG) {
                Utilidades.escribeLog("Proceso: " + nombreProceso);
                }
                procesoBean.setNombreProceso(nombreProceso);

                //Se obtiene la lista de hijos del tag 'proceso'
                List lista_campos = proceso.getChildren();
                if (PantallaPrincipal.DEBUG) {
                Utilidades.escribeLog("\tNombre\t\tTipo\t\tValor\t\tObligatorio");
                }
                //Se recorre la lista de campos

                ArrayList arrayCampos = new ArrayList();
                for (int j = 0; j < lista_campos.size(); j++) {
                    CampoBean campoBean = new CampoBean();

                    //Se obtiene el elemento 'campo'
                    Element campo = (Element) lista_campos.get(j);

                    //Se obtienen los valores que estan entre los tags '<campo></campo>'
                    //Se obtiene el valor que esta entre los tags '<nombre></nombre>'
                    String nombre = campo.getChildTextTrim("nombre");

                    //Se obtiene el valor que esta entre los tags '<tipo></tipo>'
                    String tipo = campo.getChildTextTrim("tipo");

                    //Se obtiene el valor que esta entre los tags '<valor></valor>'
                    String valor = campo.getChildTextTrim("valor");

                    //Se obtiene el valor que esta entre los tags '<valor></valor>'
                    String obligatorio = campo.getChildTextTrim("obligatorio");

                    campoBean.setNombre(nombre);
                    campoBean.setTipo(tipo);
                    campoBean.setValor(valor);
                    campoBean.setObligatorio(obligatorio);

                    arrayCampos.add(campoBean);
                    if (PantallaPrincipal.DEBUG) {
                    Utilidades.escribeLog("\t" + nombre + "\t\t" + tipo + "\t\t" + valor + "\t\t" + obligatorio);
}
                }
                procesoBean.setArrayCampo(arrayCampos);
                arrayProceso.add(procesoBean);
            }

            procesosBean.setArrayProcesos(arrayProceso);

            return procesosBean;

        } catch (IOException io) {
            Utilidades.escribeLog("Error en -cargarXmlProcesos- - "+io.getMessage());
            return null;
        } catch (JDOMException jdomex) {
            Utilidades.escribeLog("Error en -cargarXmlProcesos- - "+jdomex.getMessage());
            return null;
        }
    }
}
