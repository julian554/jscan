package es.jscan.Beans;

public class CampoBean {
    private String nombre;
    private String tipo;
    private String valor;
    private String obligatorio;

    public String getObligatorio() {
        if (obligatorio == null  ){
            obligatorio="N";
        }
        return obligatorio;
    }

    public void setObligatorio(String obligatorio) {
        this.obligatorio = obligatorio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
    
}
