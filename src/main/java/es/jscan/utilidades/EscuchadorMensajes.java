package es.jscan.utilidades;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class EscuchadorMensajes {
    private  final int PORT = 37778;
    public EscuchadorMensajes(){
        try {
            IoAcceptor acceptor = new NioSocketAcceptor();
            // No recibimos los log
            //  acceptor.getFilterChain ().addLast("logger", new LoggingFilter());
            acceptor.getFilterChain ().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
            acceptor.setHandler (new ProcesadorMensajes());
            acceptor.getSessionConfig ().setReadBufferSize(2048);
            acceptor.getSessionConfig ().setIdleTime(IdleStatus.BOTH_IDLE, 2);
            acceptor.bind (new InetSocketAddress(PORT));
        } catch (Exception ex) {
            Utilidades.escribeLog("Error al crear socket -EscuchadorMensajes- Error: " + ex.getMessage());
        }
    }
}
