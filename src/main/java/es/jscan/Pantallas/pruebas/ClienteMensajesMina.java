package es.jscan.Pantallas.pruebas;

import es.jscan.utilidades.Utilidades;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class ClienteMensajesMina {

    private static final int PORT = 37778;

    public static void main(String[] args) throws IOException, InterruptedException {
        enviarMensaje("010.001.072.140", "Por problemas de espacio en disco no podrán subirse los documentos al servidor Documental.");
        
        enviarMensaje("010.001.072.140","DEBUG=SI");
        
        enviarMensaje("010.001.072.140","Se acaba de activar el modo debug");

    }

    private static void enviarMensaje(String ip, String mensaje) {
        IoConnector connector = new NioSocketConnector();
        connector.getSessionConfig().setReadBufferSize(2048);
// No enviamos los log
      //  connector.getFilterChain().addLast("logger", new LoggingFilter());
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));

        connector.setHandler(new ClienteProcesaMensajeMina(mensaje));
        ConnectFuture future = connector.connect(new InetSocketAddress(ip, PORT));
        future.awaitUninterruptibly();

        if (!future.isConnected()) {
            return;
        }
        IoSession session = future.getSession();
        session.getConfig().setUseReadOperation(true);
        session.getCloseFuture().awaitUninterruptibly();
        connector.dispose();

        Utilidades.escribeLog("Recibido mensaje");
    }
}