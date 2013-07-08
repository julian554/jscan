package es.jscan.utilidades;

import java.net.*;

public class ConexionUnica extends Thread {

    public static final int PORT = 37777;
    ServerSocket serverSocket = null;
    Socket clientSocket = null;

    public void run() {
        try {
            // Creamos el socket servidor
            serverSocket = new ServerSocket(PORT, 1);
            while (true) {
                // Esperando conexiones
                clientSocket = serverSocket.accept();
                // Conexión establecida;
                clientSocket.close();
            }
        } catch (Exception ex) {
            Utilidades.escribeLog("Error en -ConexionUnica-: " + ex);
        }
    }
}