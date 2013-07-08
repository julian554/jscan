package es.jscan.Beans;

import java.util.ArrayList;
import java.util.List;

public class XmlLote {

    private XmlInfo Info;
    private List<XmlDocumento> Documentos =
            new ArrayList<XmlDocumento>();

    public XmlInfo getInfo() {
        return Info;
    }

    public void setInfo(XmlInfo Info) {
        this.Info = Info;
    }

    public List<XmlDocumento> getDocumento() {
        return Documentos;
    }

    public void setDocumento(List<XmlDocumento> Documento) {
        this.Documentos = Documento;
    }

    public void add(XmlDocumento documento) {
        Documentos.add(documento);
    }
}
