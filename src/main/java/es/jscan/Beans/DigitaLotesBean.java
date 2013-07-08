
package es.jscan.Beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para digitaLotesBean complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="digitaLotesBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="actualizar_fase" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="archivo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cb" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="estado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="expediente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fecha_creacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fecha_envio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fecha_grabacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fecha_notificacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fecha_pago" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id_documentum" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ip" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lote" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="notificar_instructor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="paginas" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="provincia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tamano" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipo_documento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipo_proceso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ubicacion_fisica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="usuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "digitaLotesBean", propOrder = {
    "actualizarFase",
    "archivo",
    "cb",
    "descripcion",
    "estado",
    "expediente",
    "fechaCreacion",
    "fechaEnvio",
    "fechaGrabacion",
    "fechaNotificacion",
    "fechaPago",
    "idDocumentum",
    "ip",
    "lote",
    "notificarInstructor",
    "paginas",
    "provincia",
    "tamano",
    "tipoDocumento",
    "tipoProceso",
    "ubicacionFisica",
    "usuario"
})
public class DigitaLotesBean {

    @XmlElement(name = "actualizar_fase")
    protected String actualizarFase;
    protected String archivo;
    protected String cb;
    protected String descripcion;
    protected String estado;
    protected String expediente;
    @XmlElement(name = "fecha_creacion")
    protected String fechaCreacion;
    @XmlElement(name = "fecha_envio")
    protected String fechaEnvio;
    @XmlElement(name = "fecha_grabacion")
    protected String fechaGrabacion;
    @XmlElement(name = "fecha_notificacion")
    protected String fechaNotificacion;
    @XmlElement(name = "fecha_pago")
    protected String fechaPago;
    @XmlElement(name = "id_documentum")
    protected String idDocumentum;
    protected String ip;
    protected String lote;
    @XmlElement(name = "notificar_instructor")
    protected String notificarInstructor;
    protected String paginas;
    protected String provincia;
    protected String tamano;
    @XmlElement(name = "tipo_documento")
    protected String tipoDocumento;
    @XmlElement(name = "tipo_proceso")
    protected String tipoProceso;
    @XmlElement(name = "ubicacion_fisica")
    protected String ubicacionFisica;
    protected String usuario;

    /**
     * Obtiene el valor de la propiedad actualizarFase.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActualizarFase() {
        return actualizarFase;
    }

    /**
     * Define el valor de la propiedad actualizarFase.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActualizarFase(String value) {
        this.actualizarFase = value;
    }

    /**
     * Obtiene el valor de la propiedad archivo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArchivo() {
        return archivo;
    }

    /**
     * Define el valor de la propiedad archivo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArchivo(String value) {
        this.archivo = value;
    }

    /**
     * Obtiene el valor de la propiedad cb.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCb() {
        return cb;
    }

    /**
     * Define el valor de la propiedad cb.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCb(String value) {
        this.cb = value;
    }

    /**
     * Obtiene el valor de la propiedad descripcion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Define el valor de la propiedad descripcion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcion(String value) {
        this.descripcion = value;
    }

    /**
     * Obtiene el valor de la propiedad estado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Define el valor de la propiedad estado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstado(String value) {
        this.estado = value;
    }

    /**
     * Obtiene el valor de la propiedad expediente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpediente() {
        return expediente;
    }

    /**
     * Define el valor de la propiedad expediente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpediente(String value) {
        this.expediente = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaCreacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Define el valor de la propiedad fechaCreacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaCreacion(String value) {
        this.fechaCreacion = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaEnvio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaEnvio() {
        return fechaEnvio;
    }

    /**
     * Define el valor de la propiedad fechaEnvio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaEnvio(String value) {
        this.fechaEnvio = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaGrabacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaGrabacion() {
        return fechaGrabacion;
    }

    /**
     * Define el valor de la propiedad fechaGrabacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaGrabacion(String value) {
        this.fechaGrabacion = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaNotificacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaNotificacion() {
        return fechaNotificacion;
    }

    /**
     * Define el valor de la propiedad fechaNotificacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaNotificacion(String value) {
        this.fechaNotificacion = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaPago.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaPago() {
        return fechaPago;
    }

    /**
     * Define el valor de la propiedad fechaPago.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaPago(String value) {
        this.fechaPago = value;
    }

    /**
     * Obtiene el valor de la propiedad idDocumentum.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdDocumentum() {
        return idDocumentum;
    }

    /**
     * Define el valor de la propiedad idDocumentum.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdDocumentum(String value) {
        this.idDocumentum = value;
    }

    /**
     * Obtiene el valor de la propiedad ip.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIp() {
        return ip;
    }

    /**
     * Define el valor de la propiedad ip.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIp(String value) {
        this.ip = value;
    }

    /**
     * Obtiene el valor de la propiedad lote.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLote() {
        return lote;
    }

    /**
     * Define el valor de la propiedad lote.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLote(String value) {
        this.lote = value;
    }

    /**
     * Obtiene el valor de la propiedad notificarInstructor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotificarInstructor() {
        return notificarInstructor;
    }

    /**
     * Define el valor de la propiedad notificarInstructor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotificarInstructor(String value) {
        this.notificarInstructor = value;
    }

    /**
     * Obtiene el valor de la propiedad paginas.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaginas() {
        return paginas;
    }

    /**
     * Define el valor de la propiedad paginas.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaginas(String value) {
        this.paginas = value;
    }

    /**
     * Obtiene el valor de la propiedad provincia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * Define el valor de la propiedad provincia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvincia(String value) {
        this.provincia = value;
    }

    /**
     * Obtiene el valor de la propiedad tamano.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTamano() {
        return tamano;
    }

    /**
     * Define el valor de la propiedad tamano.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTamano(String value) {
        this.tamano = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoDocumento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * Define el valor de la propiedad tipoDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoDocumento(String value) {
        this.tipoDocumento = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoProceso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoProceso() {
        return tipoProceso;
    }

    /**
     * Define el valor de la propiedad tipoProceso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoProceso(String value) {
        this.tipoProceso = value;
    }

    /**
     * Obtiene el valor de la propiedad ubicacionFisica.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUbicacionFisica() {
        return ubicacionFisica;
    }

    /**
     * Define el valor de la propiedad ubicacionFisica.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUbicacionFisica(String value) {
        this.ubicacionFisica = value;
    }

    /**
     * Obtiene el valor de la propiedad usuario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Define el valor de la propiedad usuario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsuario(String value) {
        this.usuario = value;
    }

}
