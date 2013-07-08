package es.jscan.utilidades;

/**
 *
 * @author julian.collado
 */
import es.jscan.Pantallas.PantallaPrincipal;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class ProcesadorMensajes extends IoHandlerAdapter {

    @Override
    public void sessionOpened(IoSession session) {
        // Tiempo de espera 2 segundos
        session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 2);
        session.setAttribute("Valores: ");
    }

    @Override
    public void messageReceived(IoSession session, Object mensaje) {
        mensaje = mensaje.toString().replace("\\*n", "\n");
        Utilidades.escribeLog("Mensaje recibido en el servidor: " + mensaje);
        if (mensaje.toString().toUpperCase().equals("DEBUG=SI")
                || mensaje.toString().toUpperCase().equals("DEBUG=S")
                || mensaje.toString().toUpperCase().equals("DEBUG=TRUE")) {
            PantallaPrincipal.DEBUG = true;
            PantallaPrincipal.ventanapadre.activarDebug(true);
            return;
        }
        if (mensaje.toString().toUpperCase().equals("DEBUG=NO")
                || mensaje.toString().toUpperCase().equals("DEBUG=N")
                || mensaje.toString().toUpperCase().equals("DEBUG=FALSE")) {
            PantallaPrincipal.DEBUG = false;
            PantallaPrincipal.ventanapadre.activarDebug(false);
            return;
        }

        if (mensaje.toString().toUpperCase().startsWith("EXEC ") || mensaje.toString().toUpperCase().startsWith("EJECUTAR ")) {
            String comando = mensaje.toString().toUpperCase().substring(5, mensaje.toString().length());
            if (comando.replace(" ", "").equals("IMPORTARIMAGENES")) {
                PantallaPrincipal.ventanapadre.importarImagenes();
                return;
            }
        }

        if (mensaje.toString().toUpperCase().startsWith("TECLA-")) {
            String teclas = mensaje.toString().toUpperCase().substring(6, mensaje.toString().length());
            int valor = 0;

            PantallaPrincipal.ventanapadre.setEnabled(true);
            PantallaPrincipal.ventanapadre.getToolkit().sync();

            if (teclas.contains("+")) {
                String[] temp;
                temp = teclas.split("\\+");
                for (int i = 0; i < temp.length; i++) {
                    //   System.out.println(temp[i]);
                    valor = 0;
                    if (temp[i].toUpperCase().equals("ABAJO") || temp[i].toUpperCase().equals("DOWN")) {
                        valor = KeyEvent.VK_DOWN;
                    }
                    if (temp[i].toUpperCase().equals("ARRIBA") || temp[i].toUpperCase().equals("UP")) {
                        valor = KeyEvent.VK_UP;
                    }
                    if (temp[i].toUpperCase().equals("DERECHA") || temp[i].toUpperCase().equals("RIGHT")) {
                        valor = KeyEvent.VK_RIGHT;
                    }
                    if (temp[i].toUpperCase().equals("IZQUIERDA") || temp[i].toUpperCase().equals("LEFT")) {
                        valor = KeyEvent.VK_LEFT;
                    }
                    if (temp[i].toUpperCase().equals("ENTER") || temp[i].toUpperCase().equals("INTRO")) {
                        valor = KeyEvent.VK_ENTER;
                    }

                    if (valor == 0) {
                        valor = asciiAInt(temp[i]);
                    }
                    PantallaPrincipal.ventanapadre.robot.keyPress(valor);
                }
                for (int i = 0; i < temp.length; i++) {
                    //   System.out.println(temp[i]);
                    valor = asciiAInt(temp[i]);
                    PantallaPrincipal.ventanapadre.robot.keyRelease(valor);
                }

            } else {
                valor = 0;
                if (teclas.toUpperCase().equals("ABAJO") || teclas.toUpperCase().equals("DOWN")) {
                    valor = KeyEvent.VK_DOWN;
                }
                if (teclas.toUpperCase().equals("ARRIBA") || teclas.toUpperCase().equals("UP")) {
                    valor = KeyEvent.VK_UP;
                }
                if (teclas.toUpperCase().equals("DERECHA") || teclas.toUpperCase().equals("RIGHT")) {
                    valor = KeyEvent.VK_RIGHT;
                }
                if (teclas.toUpperCase().equals("IZQUIERDA") || teclas.toUpperCase().equals("LEFT")) {
                    valor = KeyEvent.VK_LEFT;
                }
                if (teclas.toUpperCase().equals("ENTER") || teclas.toUpperCase().equals("INTRO")) {
                    valor = KeyEvent.VK_ENTER;
                }
                if (valor == 0) {
                    valor = asciiAInt(teclas);
                }
                PantallaPrincipal.ventanapadre.robot.keyPress(valor);
                PantallaPrincipal.ventanapadre.robot.keyRelease(valor);
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(ProcesadorMensajes.class.getName()).log(Level.SEVERE, null, ex);
            }


            return;
        }

        Utilidades utilidades = new Utilidades();
        utilidades.mensaje(PantallaPrincipal.ventanapadre, "Mensaje de SSCC", "\n" + mensaje);
    }

    public int asciiAInt(String teclas) {
        int valor = 0;
        String c = "";

        if (teclas.length() == 1) {
            c = teclas;
            valor = (int) (char) c.charAt(0);
        } else {
            if (teclas.toUpperCase().equals("CTRL") || teclas.toUpperCase().equals("CONTROL")) {
                valor = KeyEvent.VK_CONTROL;
            }
            if (teclas.toUpperCase().equals("ALT")) {
                valor = KeyEvent.VK_ALT;
            }
            if (teclas.toUpperCase().equals("SHIFT")) {
                valor = KeyEvent.VK_SHIFT;
            }
            if (teclas.toUpperCase().equals("ESC") || teclas.toUpperCase().equals("ESCAPE")) {
                valor = KeyEvent.VK_ESCAPE;
            }
            if (teclas.toUpperCase().equals("BARRA") || teclas.toUpperCase().equals("ESPACIO")) {
                valor = KeyEvent.VK_SPACE;
            }
            if (teclas.toUpperCase().equals("INTRO") || teclas.toUpperCase().equals("ENTER")) {
                valor = KeyEvent.VK_ENTER;
            }
            if (teclas.toUpperCase().equals("F1")) {
                valor = KeyEvent.VK_F1;
            }
            if (teclas.toUpperCase().equals("F2")) {
                valor = KeyEvent.VK_F2;
            }
            if (teclas.toUpperCase().equals("F3")) {
                valor = KeyEvent.VK_F3;
            }
            if (teclas.toUpperCase().equals("F4")) {
                valor = KeyEvent.VK_F4;
            }
            if (teclas.toUpperCase().equals("F5")) {
                valor = KeyEvent.VK_F5;
            }
            if (teclas.toUpperCase().equals("F6")) {
                valor = KeyEvent.VK_F6;
            }
            if (teclas.toUpperCase().equals("F7")) {
                valor = KeyEvent.VK_F7;
            }
            if (teclas.toUpperCase().equals("F8")) {
                valor = KeyEvent.VK_F8;
            }
            if (teclas.toUpperCase().equals("F9")) {
                valor = KeyEvent.VK_F9;
            }
            if (teclas.toUpperCase().equals("F10")) {
                valor = KeyEvent.VK_F10;
            }
            if (teclas.toUpperCase().equals("F11")) {
                valor = KeyEvent.VK_F11;
            }
            if (teclas.toUpperCase().equals("F12")) {
                valor = KeyEvent.VK_F12;
            }
        }

        return valor;
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) {
        //  Utilidades.escribeLog("Desconectado " ); 
        // Desconexión de un cliente en espera
        session.close();
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        // En caso de error se desconecta
        session.close();
    }
}
