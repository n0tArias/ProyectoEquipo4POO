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
 * Clase encargada de manejar la comunicación del lado del servidor.
 * Esta clase escucha nuevas conexiones de clientes y gestiona las interacciones
 * entre ellos dentro de un sistema de chat.
 * @author Erick Navarro
 */
public class Servidor extends Thread {

    /**
     * Socket del servidor encargado de escuchar las conexiones entrantes.
     * Su función principal es aceptar nuevos clientes para incluirlos en el chat.
     */
    private ServerSocket serverSocket;

    /**
     * Lista de hilos de cliente, cada cliente conectado tiene un hilo asociado
     * que escucha permanentemente los mensajes enviados al servidor.
     */
    LinkedList<HiloCliente> clientes;

    /**
     * Ventana que gestiona la interfaz gráfica del servidor.
     * Sirve para mostrar los logs y el estado del servidor.
     */
    private final VentanaS ventana;

    /**
     * Puerto en el que el servidor estará escuchando conexiones entrantes.
     */
    private final String puerto;

    /**
     * Contador correlativo para diferenciar a los múltiples clientes conectados,
     * incluso si usan el mismo nombre.
     */
    static int correlativo;

    /**
     * Constructor de la clase Servidor.
     * Inicializa el puerto, la ventana gráfica, la lista de clientes y arranca
     * el hilo del servidor.
     * @param puerto Puerto en el que se ejecutará el servidor.
     * @param ventana Ventana gráfica asociada al servidor.
     */
    public Servidor(String puerto, VentanaS ventana) {
        correlativo = 0; // Inicializa el contador correlativo.
        this.puerto = puerto;
        this.ventana = ventana;
        clientes = new LinkedList<>();
        this.start(); // Inicia el hilo del servidor.
    }

    /**
     * Método principal del hilo del servidor.
     * Este método implementa un bucle infinito que escucha conexiones de nuevos
     * clientes, crea hilos para cada uno y los añade a la lista de clientes.
     */
    public void run() {
        try {
            // Inicializa el socket del servidor con el puerto especificado.
            serverSocket = new ServerSocket(Integer.valueOf(puerto));
            ventana.addServidorIniciado(); // Notifica a la interfaz que el servidor inició.

            // Ciclo infinito para aceptar conexiones de nuevos clientes.
            while (true) {
                HiloCliente h;
                Socket socket;
                socket = serverSocket.accept(); // Espera una nueva conexión.
                System.out.println("Nueva conexion entrante: " + socket);
                h = new HiloCliente(socket, this); // Crea un nuevo hilo para el cliente.
                h.start(); // Inicia el hilo del cliente.
            }
        } catch (Exception e) {
            // Maneja errores al iniciar el servidor.
            JOptionPane.showMessageDialog(ventana, "El servidor no se ha podido iniciar,\n"
                    + "puede que haya ingresado un puerto incorrecto.\n"
                    + "Esta aplicación se cerrará.");
            System.exit(0);
        }
    }

    /**
     * Método que devuelve una lista con los identificadores de todos los clientes conectados.
     * @return Lista de identificadores de los clientes conectados.
     */
    LinkedList<String> getUsuariosConectados() {
        LinkedList<String> usuariosConectados = new LinkedList<>();
        // Agrega el identificador de cada cliente a la lista.
        clientes.stream().forEach(c -> usuariosConectados.add(c.getIdentificador()));
        return usuariosConectados;
    }

    /**
     * Método que agrega una línea de texto al log de la interfaz gráfica del servidor.
     * @param texto Texto a agregar al log de la ventana del servidor.
     */
    void agregarLog(String texto) {
        ventana.agregarLog(texto);
    }

    /**
     * Método que elimina un cliente de la lista de usuarios conectados.
     * Este método cierra la conexión del cliente y notifica a los demás usuarios
     * sobre su desconexión.
     * @param identificador Identificador único del cliente a eliminar.
     */
    void eliminarCliente(String identificador) {
        HiloCliente clienteAEliminar = null;

        // Busca al cliente correspondiente en la lista de hilos.
        for (HiloCliente cliente : clientes) {
            if (cliente.getIdentificador().equals(identificador)) {
                clienteAEliminar = cliente;
                break;
            }
        }

        if (clienteAEliminar != null) {
            try {
                // Cierra la conexión del cliente y lo elimina de la lista.
                clienteAEliminar.desconnectar();
                clientes.remove(clienteAEliminar);
                agregarLog("Usuario " + identificador + " desconectado.");

                // Notifica a los demás usuarios que el cliente se ha desconectado.
                LinkedList<String> auxLista = new LinkedList<>();
                auxLista.add("USUARIO_DESCONECTADO");
                auxLista.add(identificador);

                // Envía el mensaje de desconexión a todos los usuarios.
                clientes.stream().forEach(h -> h.enviarMensaje(auxLista));
            } catch (Exception e) {
                agregarLog("Error al desconectar al usuario " + identificador + ": " + e.getMessage());
            }
        } else {
            // Informa si el cliente no fue encontrado en la lista.
            agregarLog("No se encontró al usuario con identificador " + identificador + ".");
        }
    }
}

