package es.jscan.utilidades;

import java.io.Serializable;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class TablaSinEditarCol extends DefaultTableModel implements Serializable {

    private static Vector newVector(int size) {
        Vector v = new Vector(size);
        v.setSize(size);
        return v;
    }

    public TablaSinEditarCol() {
        this(0, 0);
    }

    public TablaSinEditarCol(int numfilas, int numcolumnas) {
        this(newVector(numcolumnas), numfilas);
    }

    public TablaSinEditarCol(Vector columnas, int numfilas) {
        setDataVector(newVector(numfilas), columnas);
    }

    public TablaSinEditarCol(String[][] campos, String[] cabeceras) {
        setDataVector(campos, cabeceras);
    }

    public TablaSinEditarCol(Vector<Vector<String>> campos, Vector<String> cabeceras) {
        setDataVector(campos, cabeceras);
    }

    public TablaSinEditarCol(Object[][] campos, Object[] cabeceras) {
        setDataVector(campos, cabeceras);
    }

    @Override
    public boolean isCellEditable(int fila, int columna) {
        if (columna == 0 || columna == 1 || columna == 2 || columna == 3 || columna==4|| columna==5) {
            return false;
        } else {
            return true;
        }

    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
}