
package es.jscan.Beans;

import java.util.ArrayList;

public class ProcesoBean {
    
    private String nombreProceso;
    private ArrayList<CampoBean> arrayCampo = new ArrayList();

    public String getNombreProceso() {
        return nombreProceso;
    }

    public void setNombreProceso(String nombreProceso) {
        this.nombreProceso = nombreProceso;
    }

    
    
    
    public ArrayList<CampoBean> getArrayCampo() {
        return arrayCampo;
    }

    public void setArrayCampo(ArrayList<CampoBean> arrayCampo) {
        this.arrayCampo = arrayCampo;
    }
    
    
    
}
