/*
 * Ejemplo desarrollado por Erick Navarro
 * Blog: e-navarro.blogspot.com
 * Noviembre - 2015
 */
package project.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import javax.swing.JOptionPane;

/**
 * Clase en la que se maneja la comunicación del lado del servidor.
 *
 * @author Erick Navarro
 */
public class Servidor extends Thread {

    /**
     * Socket servidor que tiene como principal función escuchar cuando los
     * clientes se conectan para incluirlos en el chat.
     */
    private ServerSocket serverSocket;
    /**
     * Lista de todos los hilos de comunicación, para cada cliente se instancia
     * uno de estos hilos ya que cada hilo esta escuchando permanentemente lo
     * que dicho cliente envía al servidor.
     */
    LinkedList<HiloCliente> clientes;
    /**
     * Variable que almacena la ventana que gestiona la interfaz gráfica del
     * servidor.
     */
    private final VentanaS ventana;
    /**
     * Variable que almacena el puerto que el servidor usará para escuchar.
     */
    private final String puerto;
    /**
     * Correlativo para diferenciar a los múltiples clientes que se conectan, si
     * se conectaran, por ejemplo, dos usuarios con el mismo nombre, se podrían
     * diferenciar por este correlativo.
     */
    static int correlativo;

    /**
     * Constructor del servidor.
     *
     * @param puerto
     * @param ventana
     */
    public Servidor(String puerto, VentanaS ventana) {
        correlativo = 0;
        this.puerto = puerto;
        this.ventana = ventana;
        clientes = new LinkedList<>();
        this.start();
    }

    /**
     * Método sobre el que corre el bucle infinito que tiene como función
     * escuchar permenentemente en espera de conexiones de nuevos clientes.
     */
    public void run() {
        try {
            serverSocket = new ServerSocket(Integer.valueOf(puerto));
            ventana.addServidorIniciado();
            while (true) {
                HiloCliente h;
                Socket socket;
                socket = serverSocket.accept();
                System.out.println("Nueva conexion entrante: " + socket);
                h = new HiloCliente(socket, this);
                h.start();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(ventana, "El servidor no se ha podido iniciar,\n"
                    + "puede que haya ingresado un puerto incorrecto.\n"
                    + "Esta aplicación se cerrará.");
            System.exit(0);
        }
    }

    /**
     * Ciclo que devuelve una lista con los identificadores de todos los
     * clientes conectados.
     *
     * @return
     */
    LinkedList<String> getUsuariosConectados() {
        LinkedList<String> usuariosConectados = new LinkedList<>();
        clientes.stream().forEach(c -> usuariosConectados.add(c.getIdentificador()));
        return usuariosConectados;
    }

    /**
     * Método que agrega una linea al log de la interfaz gráfica del servidor.
     *
     * @param texto
     */
    void agregarLog(String texto) {
        ventana.agregarLog(texto);
    }

    void eliminarCliente(String identificador) {
        HiloCliente clienteAEliminar = null;

        // Buscar al cliente en la lista
        for (HiloCliente cliente : clientes) {
            if (cliente.getIdentificador().equals(identificador)) {
                clienteAEliminar = cliente;
                break;
            }
        }

        if (clienteAEliminar != null) {
            try {
                clienteAEliminar.desconnectar(); // Cierra la conexión del cliente
                clientes.remove(clienteAEliminar); // Elimina el cliente de la lista
                agregarLog("Usuario " + identificador + " desconectado.");

                // Notifica a los demás usuarios de la desconexión
                LinkedList<String> auxLista = new LinkedList<>();
                auxLista.add("USUARIO_DESCONECTADO");
                auxLista.add(identificador);
            } catch (Exception e) {
                agregarLog("Error al desconectar al usuario " + identificador + ": " + e.getMessage());
            }
        } else {
            agregarLog("No se encontró al usuario con identificador " + identificador + ".");
        }
    }
}
