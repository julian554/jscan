package es.jscan.Beans;

public class XmlInfo {

    private String Fechadigita;
    private String Usuarioldap;
    private String Provincia;
    private String Ip;
    private String Numdocumentos;
    private String Fechacreacion;
    private String Proceso;
    private String Fechaenvio;

    public String getFechacreacion() {
        return Fechacreacion;
    }

    public void setFechacreacion(String Fechacreacion) {
        this.Fechacreacion = Fechacreacion;
    }

    public String getFechadigita() {
        return Fechadigita;
    }

    public void setFechadigita(String Fechadigita) {
        this.Fechadigita = Fechadigita;
    }

    public String getFechaenvio() {
        return Fechaenvio;
    }

    public void setFechaenvio(String Fechaenvio) {
        this.Fechaenvio = Fechaenvio;
    }

    public String getIp() {
        return Ip;
    }

    public void setIp(String Ip) {
        this.Ip = Ip;
    }

    public String getNumdocumentos() {
        return Numdocumentos;
    }

    public void setNumdocumentos(String Numdocumentos) {
        this.Numdocumentos = Numdocumentos;
    }

    public String getProceso() {
        return Proceso;
    }

    public void setProceso(String Proceso) {
        this.Proceso = Proceso;
    }

    public String getProvincia() {
        return Provincia;
    }

    public void setProvincia(String Provincia) {
        this.Provincia = Provincia;
    }

    public String getUsuarioldap() {
        return Usuarioldap;
    }

    public void setUsuarioldap(String Usuarioldap) {
        this.Usuarioldap = Usuarioldap;
    }
}
