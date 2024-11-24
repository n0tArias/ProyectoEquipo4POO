/*
 * Ejemplo desarrollado por Erick Navarro
 * Blog: e-navarro.blogspot.com
 * Noviembre - 2015
 */

package project.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Los objetos de esta clase son hilos que al correr escuchan permanentemente
 * lo que los clientes puedan decir, hay un hilo para cada cliente que se conecta al servidor y dicho 
 * hilo tiene como función escuchar solamente a ese cliente.
 * @author Erick Navarro
 */
public class HiloCliente extends Thread{
    /**
     * Socket que se utiliza para comunicarse con el cliente.
     */
    private final Socket socket;    
    /**
     * Stream con el que se envían objetos al servidor.
     */    
    private ObjectOutputStream objectOutputStream;
    /**
     * Stream con el que se reciben objetos del servidor. 
     */
    private ObjectInputStream objectInputStream;            
    /**
     * Servidor al que pertenece este hilo.
     */        
    private final Servidor server;
    /**
     * Identificador único del cliente con el que este hilo se comunica.
     */
    private String identificador;
    /**
     * Variable booleana que almacena verdadero cuando este hilo esta escuchando
     * lo que el cliente que atiende esta diciendo.
     */
    private boolean escuchando;
    
    /**
     * Constructor de la clase HiloCliente.
     * Este constructor se utiliza para inicializar la conexión con un cliente, estableciendo los
     * flujos de entrada y salida necesarios para la comunicación entre el servidor y el cliente.
     * @param socket El socket que representa la conexión con el cliente.
     * @param server El objeto Servidor que maneja la comunicación global.
     */
    public HiloCliente(Socket socket, Servidor server) {
        // Asignar el servidor proporcionado al atributo de instancia.
        this.server = server;
        // Asignar el socket proporcionado al atributo de instancia.
        this.socket = socket;
        try {
            // Crear un ObjectOutputStream para enviar objetos al cliente.
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // Crear un ObjectInputStream para recibir objetos del cliente.
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            // Manejar errores durante la inicialización de los flujos de entrada y salida.
            System.err.println("Error en la inicialización del ObjectOutputStream y el ObjectInputStream");
        }
    }

    /**
     * Método encargado de cerrar el socket de comunicación con el cliente.
     * Este método se utiliza para finalizar la conexión entre el servidor y el cliente,
     * liberando los recursos asociados al socket.
     * Si el socket no se puede cerrar debido a un error de entrada/salida, se imprime
     * un mensaje de error en la consola.
     */
    public void desconnectar() {
        try {
            // Cierra el socket asociado a la comunicación con el cliente.
            socket.close();
            // Cambia el estado de la variable 'escuchando' a false, indicando que
            // ya no se está escuchando al cliente.
            escuchando = false;
        } catch (IOException ex) {
            // Maneja cualquier error que ocurra al intentar cerrar el socket.
            System.err.println("Error al cerrar el socket de comunicación con el cliente.");
        }
    }

    /**
     * Sobreescritura del método de Thread, es acá en donde se monta el ciclo infinito.
     */        
    public void run() {
        try{
            escuchar();
        } catch (Exception ex) {
            System.err.println("Error al llamar al método readLine del hilo del cliente.");
        }
        desconnectar();
    }
        
    /**
     * Método que constantemente esta escuchando todo lo que es enviado por 
     * el cliente que se comunica con él.
     */        
    public void escuchar(){        
        escuchando=true;
        while(escuchando){
            try {
                Object aux=objectInputStream.readObject();
                if(aux instanceof LinkedList){
                    ejecutar((LinkedList<String>)aux);
                }
            } catch (Exception e) {                    
                System.err.println("Error al leer lo enviado por el cliente.");
            }
        }
    }
    /**
     * Método que realiza determinadas acciones dependiendo de lo que el socket haya recibido y lo que
     * este le envie el método, en él se manejan una serie de códigos.
     * @param lista
     */        
    public void ejecutar(LinkedList<String> lista){
        // 0 - El primer elemento de la lista es siempre el tipo
        String tipo=lista.get(0);
        switch (tipo) {
            case "SOLICITUD_CONEXION":
                // 1 - Identificador propio del nuevo usuario
                confirmarConexion(lista.get(1));
                break;
            case "SOLICITUD_DESCONEXION":
                // 1 - Identificador propio del nuevo usuario
                confirmarDesConexion();
                break;                
            case "MENSAJE":
                // 1      - Cliente emisor
                // 2      - Cliente receptor
                // 3      - Mensaje
                String destinatario=lista.get(2);
                server.clientes
                        .stream()
                        .filter(h -> (destinatario.equals(h.getIdentificador())))
                        .forEach((h) -> h.enviarMensaje(lista));
                break;
            default:
                break;
        }
    }
       /**
     * Sobreescritura del método `run` del hilo.
     * Este método ejecuta el ciclo principal del hilo del cliente, donde primero
     * invoca el método `escuchar` para recibir datos del cliente y, al terminar,
     * desconecta el socket asociado.
     */
    public void run() {
        try {
            // Inicia el proceso de escucha para recibir datos del cliente.
            escuchar();
        } catch (Exception ex) {
            // Maneja errores durante la ejecución del hilo.
            System.err.println("Error al llamar al método readLine del hilo del cliente.");
        }
        // Finaliza la conexión del cliente.
        desconnectar();
    }
    
    /**
     * Método encargado de escuchar continuamente los mensajes enviados por el cliente.
     * Este método implementa un ciclo infinito mientras la conexión esté activa,
     * leyendo objetos enviados por el cliente a través del flujo de entrada.
     */
    public void escuchar() {
        // Indica que el hilo está activo y escuchando mensajes del cliente.
        escuchando = true;
        while (escuchando) {
            try {
                // Lee un objeto enviado por el cliente.
                Object aux = objectInputStream.readObject();
                // Verifica si el objeto es una lista y, en caso afirmativo, lo procesa.
                if (aux instanceof LinkedList) {
                    ejecutar((LinkedList<String>) aux);
                }
            } catch (Exception e) {
                // Maneja errores al leer datos del cliente.
                System.err.println("Error al leer lo enviado por el cliente.");
            }
        }
    }
    
    /**
     * Método encargado de procesar las acciones solicitadas por el cliente.
     * Dependiendo del contenido de la lista recibida, este método realiza diferentes
     * operaciones como gestionar conexiones, desconexiones o mensajes.
     * @param lista Lista de cadenas que contiene el tipo de solicitud y sus parámetros.
     */
    public void ejecutar(LinkedList<String> lista) {
        // El primer elemento de la lista determina el tipo de acción a realizar.
        String tipo = lista.get(0);
        switch (tipo) {
            case "SOLICITUD_CONEXION":
                // Procesa la conexión de un nuevo cliente.
                confirmarConexion(lista.get(1));
                break;
            case "SOLICITUD_DESCONEXION":
                // Maneja la desconexión del cliente.
                confirmarDesConexion();
                break;
            case "MENSAJE":
                // Envía un mensaje del cliente emisor al destinatario especificado.
                String destinatario = lista.get(2);
                server.clientes
                    .stream()
                    .filter(h -> (destinatario.equals(h.getIdentificador())))
                    .forEach((h) -> h.enviarMensaje(lista));
                break;
            default:
                // No se realiza ninguna acción si el tipo no es reconocido.
                break;
        }
    }
    
    /**
     * Método para enviar un mensaje al cliente a través del socket.
     * Este método utiliza el flujo de salida del cliente para transmitir una lista de cadenas.
     * @param lista Lista de cadenas que contiene los datos del mensaje.
     */
    private void enviarMensaje(LinkedList<String> lista) {
        try {
            // Escribe la lista en el flujo de salida para enviarla al cliente.
            objectOutputStream.writeObject(lista);
        } catch (Exception e) {
            // Maneja errores al intentar enviar el mensaje.
            System.err.println("Error al enviar el objeto al cliente.");
        }
    }
    
    /**
     * Método para notificar a todos los clientes conectados sobre la conexión de un nuevo cliente.
     * Este método también agrega el nuevo cliente al servidor y actualiza la lista de contactos
     * de todos los clientes existentes.
     * @param identificador Identificador único del cliente que se ha conectado.
     */
    private void confirmarConexion(String identificador) {
        // Incrementa el contador global de clientes y genera un identificador único.
        Servidor.correlativo++;
        this.identificador = Servidor.correlativo + " - " + identificador;
    
        // Prepara el mensaje de confirmación de conexión para el cliente.
        LinkedList<String> lista = new LinkedList<>();
        lista.add("CONEXION_ACEPTADA");
        lista.add(this.identificador);
        lista.addAll(server.getUsuariosConectados());
        enviarMensaje(lista);
    
        // Registra la conexión en los logs del servidor.
        server.agregarLog("\nNuevo cliente: " + this.identificador);
    
        // Notifica a todos los clientes sobre el nuevo usuario.
        LinkedList<String> auxLista = new LinkedList<>();
        auxLista.add("NUEVO_USUARIO_CONECTADO");
        auxLista.add(this.identificador);
        server.clientes.stream().forEach(cliente -> cliente.enviarMensaje(auxLista));
    
        // Agrega el nuevo cliente a la lista de clientes del servidor.
        server.clientes.add(this);
    }
    
    /**
     * Método para obtener el identificador único del cliente en el chat.
     * @return Identificador único del cliente.
     */
    public String getIdentificador() {
        return identificador;
    }
    
    /**
     * Método para manejar la desconexión del cliente.
     * Este método informa a todos los clientes conectados sobre la desconexión,
     * elimina al cliente de la lista de contactos del servidor y cierra el socket.
     */
    private void confirmarDesConexion() {
        // Prepara el mensaje de desconexión para los clientes conectados.
        LinkedList<String> auxLista = new LinkedList<>();
        auxLista.add("USUARIO_DESCONECTADO");
        auxLista.add(this.identificador);
    
        // Registra la desconexión en los logs del servidor.
        server.agregarLog("\nEl cliente \"" + this.identificador + "\" se ha desconectado.");
    
        // Cierra el socket asociado al cliente.
        this.desconnectar();
    
        // Elimina al cliente de la lista de clientes conectados.
        for (int i = 0; i < server.clientes.size(); i++) {
            if (server.clientes.get(i).equals(this)) {
                server.clientes.remove(i);
                break;
            }
        }
    
        // Notifica a los demás clientes sobre la desconexión.
        server.clientes.stream().forEach(h -> h.enviarMensaje(auxLista));
    }
