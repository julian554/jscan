package es.jscan.utilidades;

import java.io.InputStream;
import java.sql.*;
import java.util.MissingResourceException;
import java.util.Properties;

public class Conectar {
    private Properties pro = new Properties();

    public void CargarConfiguraciones() {
        try {
            InputStream in = Conectar.class.getClassLoader().getResourceAsStream("es/jscan/utilidades/propiedades/conexion.properties");
            if (in == null) {
                Utilidades.escribeLog("Error al cargar el fichero de propiedades de Alfresco");
            }
            pro = new java.util.Properties();
            pro.load(in);
        } catch (Exception ex) {
            Utilidades.escribeLog("Error al cargar el fichero de propiedades. Conexión Alfresco. Error: " + ex.getMessage());
        }
    }

    public String getPropiedad(String propiedad) {
        CargarConfiguraciones();
        String valor = pro.getProperty(propiedad);
        return valor;
    }

//    public Connection dameConexion() {
//        String url = null;
//        String usuario = null;
//        String clave = null;
//        Connection conexion = null;
//
//        CargarConfiguraciones();
//
//        // Cogemos los datos de configuración del fichero de propiedades
//        try {
//            url = pro.getProperty("url");
//            usuario = pro.getProperty("usuario");
//            clave = pro.getProperty("clave");
//
//        } catch (MissingResourceException e) {
//            Utilidades.escribeLog("Error al leer el .properties " + e.getMessage());
//        }
//
//        // Cargamos el driver JDBC
//        try {
//            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
//        } catch (SQLException e) {
//            Utilidades.escribeLog("Error al cargar el driver JDBC de Oracle");
//        }
//
//        // Conectamos a la base de datos
//        try {
//            if (conexion == null || conexion.isClosed()) {
//                conexion = DriverManager.getConnection(url, usuario, clave);
//            }
//        } catch (SQLException e) {
//            Utilidades.escribeLog("Error al conectar con el servidor: " + e.getMessage());
//        }
//        return conexion;
//    }

//    public String ejecutarSqlSimple(String sql, String campo) {
//        String resultado = null;
//
//        Conectar miconexion = new Conectar();
//        Connection conexion = miconexion.dameConexion();
//        CallableStatement cs = null;
//        ResultSet rs = null;
//
//        try {
//            cs = conexion.prepareCall(sql);
//            cs.execute();
//            rs = (ResultSet) cs.getResultSet();
//
//            while (rs.next()) {
//                String valor = rs.getString(campo);
//                if (!valor.equals("")) {
//                    return valor;
//                }
//            }
//
//        } catch (SQLException e) {
//            Utilidades.escribeLog("Error al recorrer el cursor " + e.getMessage() + " - " + sql);
//        } finally {
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (cs != null) {
//                    cs.close();
//                }
//            } catch (SQLException e) {
//                // No se puede hacer más...
//            }
//        }
//
//        return resultado;
//    }
//
//    public String[] ejecutarSqlMultiple(String sql, String campos[]) {
//        String resultado[] = new String[campos.length];
//
//        for(int i=0;i<resultado.length;i++){
//            resultado[i]="";
//        }
//        
//        Conectar miconexion = new Conectar();
//        Connection conexion = miconexion.dameConexion();
//        CallableStatement cs = null;
//        ResultSet rs = null;
//
//        try {
//            cs = conexion.prepareCall(sql);
//            cs.execute();
//            rs = (ResultSet) cs.getResultSet();
//
//            while (rs.next()) {
//                for (int i = 0; i < campos.length; i++) {
//                    resultado[i] = rs.getString(campos[i]);
//                }
//                if (!resultado[0].equals("")) {
//                    return resultado;
//                }
//            }
//
//        } catch (SQLException e) {
//            Utilidades.escribeLog("Error al recorrer el cursor " + e.getMessage() + " - " + sql);
//        } finally {
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (cs != null) {
//                    cs.close();
//                }
//            } catch (SQLException e) {
//                // No se puede hacer más...
//            }
//        }
//
//        return resultado;
//    }
}
