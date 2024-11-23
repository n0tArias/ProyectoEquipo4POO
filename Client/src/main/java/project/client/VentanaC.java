/*
 * Ejemplo desarrollado por Erick Navarro
 * Blog: e-navarro.blogspot.com
 * Noviembre - 2015
 */
package project.client;

import java.awt.*;
import static java.awt.event.KeyEvent.VK_ENTER;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

/**
 * Clase que maneja la interfaz gráfica del cliente.
 *
 * @author Erick Navarro
 */
public class VentanaC extends javax.swing.JFrame {

    /**
     * Constructor de la ventana.
     */
    public VentanaC() {
        initComponents();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String ip_puerto_nombre[] = getIP_Puerto_Nombre();
        String ip = ip_puerto_nombre[0];
        String puerto = ip_puerto_nombre[1];
        String nombre = ip_puerto_nombre[2];
        cliente = new Cliente(this, ip, Integer.valueOf(puerto), nombre);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cmbContactos = new javax.swing.JComboBox();
        btnEnviar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        ActiveUsers = new javax.swing.JScrollPane();
        btnEmoji = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtMensaje = new javax.swing.JTextPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtHistorial = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        btnEnviar.setText("Enviar");
        btnEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarActionPerformed(evt);
            }
        });

        jLabel1.setText("Destinatario:");

        ActiveUsers.setBorder(javax.swing.BorderFactory.createTitledBorder("Usuarios activos"));

        btnEmoji.setText("😀");
        btnEmoji.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmojiActionPerformed(evt);
            }
        });

        txtMensaje.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                txtMensajePropertyChange(evt);
            }
        });
        txtMensaje.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMensajeKeyPressed(evt);
            }
        });
        txtMensaje.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                verificarYReemplazarEmoji(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        jScrollPane3.setViewportView(txtMensaje);

        txtHistorial.setEditable(false);
        jScrollPane2.setViewportView(txtHistorial);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ActiveUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnEmoji)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEnviar))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 155, Short.MAX_VALUE)
                        .addComponent(cmbContactos, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbContactos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnEmoji)
                            .addComponent(btnEnviar)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(ActiveUsers))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    /**
     * Al hacer clic en el botón de enviar, se debe pedir al cliente del chat
     * que envíe al servidor el mensaje.
     *
     * @param evt
     */
    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarActionPerformed
        txtMensaje.requestFocusInWindow();
        if (cmbContactos.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe escoger un destinatario válido, si no \n"
                    + "hay uno, espere a que otro usuario se conecte\n"
                    + "para poder chatear con él.");
            return;
        }

        String cliente_receptor = cmbContactos.getSelectedItem().toString();
        StyledDocument doc = txtMensaje.getStyledDocument();
        StringBuilder mensajePlano = new StringBuilder();

        try {
            for (int i = 0; i < doc.getLength();) {
                Element element = doc.getCharacterElement(i);
                AttributeSet attributes = element.getAttributes();

                // Verifica si el elemento es un Icon
                Icon icon = StyleConstants.getIcon(attributes);
                if (icon != null) {
                    Object emojiTag = attributes.getAttribute("emojiTag");
                    if (emojiTag != null) {
                        mensajePlano.append(emojiTag.toString()); // Agrega la etiqueta del emoji
                    } else {
                        mensajePlano.append("[ICON]"); // Solo para depuración
                    }
                    i = element.getEndOffset(); // Avanza al siguiente elemento
                } else {
                    // Elemento normal (texto)
                    int start = i;
                    int end = element.getEndOffset();
                    mensajePlano.append(doc.getText(start, end - start));
                    i = end;
                }
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        cliente.enviarMensaje(cliente_receptor, mensajePlano.toString());

        // Mostrar el mensaje en el historial
        StyledDocument historial = txtHistorial.getStyledDocument();
        StyledDocument mensaje = txtMensaje.getStyledDocument();
        try {
            historial.insertString(historial.getLength(), "\n## Yo -> " + cliente_receptor + " ## : \n", null);  // Añadir encabezado
            for (int i = 0; i < mensaje.getLength();) {
                Element element = mensaje.getCharacterElement(i);
                int endOffset = element.getEndOffset();
                String text = mensaje.getText(i, endOffset - i);
                AttributeSet attributes = element.getAttributes();

                historial.insertString(historial.getLength(), text, attributes);
                i = endOffset;
            }
        } catch (BadLocationException ex) {
            Logger.getLogger(VentanaC.class.getName()).log(Level.SEVERE, null, ex);
        }
        txtMensaje.setText("");
    }//GEN-LAST:event_btnEnviarActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
    }//GEN-LAST:event_formWindowClosed
    /**
     * Cuando la ventana se este cerrando se notifica al servidor que el cliente
     * se ha desconectado, por lo que los demás clientes del chat no podrán
     * enviarle más mensajes.
     *
     * @param evt
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        cliente.confirmarDesconexion();
    }//GEN-LAST:event_formWindowClosing

    private void btnEmojiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmojiActionPerformed
        JDialog emojiDialog = new JDialog(this, "Seleccionar Emoji", true);
        emojiDialog.setSize(500, 500);

        JPanel emojiPanel = new JPanel(new GridLayout(0, 10, 5, 5));
        emojiPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String emojiFolderPath = "src/emojis/";
        String textEmojiFolderPath = "src/EmojisName";
        File emojiFolder = new File(emojiFolderPath);
        File emojisNameFolder = new File(textEmojiFolderPath);
        File[] emojisNameFiles = emojisNameFolder.exists() && emojisNameFolder.isDirectory()
                ? emojisNameFolder.listFiles((dir, name) -> name.endsWith(".png")) : null;
        File[] emojiFiles = emojiFolder.exists() && emojiFolder.isDirectory()
                ? emojiFolder.listFiles((dir, name) -> name.endsWith(".png")) : null;

        if (emojiFiles != null && emojisNameFiles != null) {
            Map<String, String> emojiMap = buildEmojiMap(emojiFiles, emojisNameFiles);
            for (File emojiFile : emojiFiles) {
                ImageIcon icon = new ImageIcon(new ImageIcon(emojiFile.getAbsolutePath()).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
                JButton emojiButton = new JButton(icon);
                emojiButton.setPreferredSize(new Dimension(40, 40));
                emojiButton.addActionListener(e -> {
                    emojiDialog.dispose();
                    String emojiName = emojiMap.get(emojiFile.getName()).replace(".png", "");
                    txtMensaje.requestFocusInWindow();
                    agregarEmojiEnJTextPane(txtMensaje, emojiName);
                });
                emojiPanel.add(emojiButton);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se encontraron emojis en la carpeta.");
        }

        emojiDialog.add(new JScrollPane(emojiPanel));
        emojiDialog.setLocationRelativeTo(this);
        emojiDialog.setVisible(true);
    }//GEN-LAST:event_btnEmojiActionPerformed

    private void txtMensajeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMensajeKeyPressed
        if (evt.getKeyCode() == VK_ENTER) {
            if (evt.isShiftDown()) {
                // Permitir salto de línea en JTextArea
                txtMensaje.setText(txtMensaje.getText() + "\n");
            } else {
                evt.consume();
                if(isJTextPaneEmpty(txtMensaje)) return;
                if (cmbContactos.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(this, "Debe escoger un destinatario válido, si no \n"
                            + "hay uno, espere a que otro usuario se conecte\n"
                            + "para poder chatear con él.");
                    return;
                }

                String cliente_receptor = cmbContactos.getSelectedItem().toString();
                StyledDocument doc = txtMensaje.getStyledDocument();
                StringBuilder mensajePlano = new StringBuilder();

                try {
                    for (int i = 0; i < doc.getLength();) {
                        Element element = doc.getCharacterElement(i);
                        AttributeSet attributes = element.getAttributes();

                        // Verifica si el elemento es un Icon
                        Icon icon = StyleConstants.getIcon(attributes);
                        if (icon != null) {
                            Object emojiTag = attributes.getAttribute("emojiTag");
                            if (emojiTag != null) {
                                mensajePlano.append(emojiTag.toString()); // Agrega la etiqueta del emoji
                            } else {
                                mensajePlano.append("[ICON]"); // Solo para depuración
                            }
                            i = element.getEndOffset(); // Avanza al siguiente elemento
                        } else {
                            // Elemento normal (texto)
                            int start = i;
                            int end = element.getEndOffset();
                            mensajePlano.append(doc.getText(start, end - start));
                            i = end;
                        }
                    }
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }

                cliente.enviarMensaje(cliente_receptor, mensajePlano.toString());

                // Mostrar el mensaje en el historial
                StyledDocument historial = txtHistorial.getStyledDocument();
                StyledDocument mensaje = txtMensaje.getStyledDocument();
                try {
                    historial.insertString(historial.getLength(), "\n## Yo -> " + cliente_receptor + " ## : \n", null);  // Añadir encabezado
                    for (int i = 0; i < mensaje.getLength();) {
                        Element element = mensaje.getCharacterElement(i);
                        int endOffset = element.getEndOffset();
                        String text = mensaje.getText(i, endOffset - i);
                        AttributeSet attributes = element.getAttributes();

                        historial.insertString(historial.getLength(), text, attributes);
                        i = endOffset;
                    }
                } catch (BadLocationException ex) {
                    Logger.getLogger(VentanaC.class.getName()).log(Level.SEVERE, null, ex);
                }
                txtMensaje.setText("");
            }

        }

    }//GEN-LAST:event_txtMensajeKeyPressed

    private void txtMensajePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_txtMensajePropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMensajePropertyChange

    private Map<String, String> buildEmojiMap(File[] prefixedFiles, File[] unprefixedFiles) {
        Map<String, String> emojiMap = new HashMap<>();
        for (File prefixedFile : prefixedFiles) {
            String prefixedName = prefixedFile.getName();
            String baseName = prefixedName.substring(prefixedName.indexOf('_') + 1, prefixedName.length() - 4);
            for (File unprefixedFile : unprefixedFiles) {
                String unprefixedName = unprefixedFile.getName();
                if (unprefixedName.startsWith(baseName)) {
                    emojiMap.put(prefixedFile.getName(), unprefixedName);
                    break;
                }
            }
        }
        return emojiMap;
    }

    private void verificarYReemplazarEmoji(DocumentEvent e) {
        StyledDocument doc = (StyledDocument) e.getDocument();
        try {
            // Obtén el texto completo del JTextPane
            String text = doc.getText(0, doc.getLength());

            // Encuentra emojis en formato :emoji: usando una expresión regular
            Pattern emojiPattern = Pattern.compile(":(\\w+):");
            Matcher matcher = emojiPattern.matcher(text);

            while (matcher.find()) {
                String emojiName = matcher.group(1);

                // Verificar si el archivo del emoji existe
                File emojiFile = new File("src/emojisName/" + emojiName + ".png");
                if (!emojiFile.exists()) {
                    System.out.println("Emoji no encontrado: " + emojiFile.getAbsolutePath());
                    continue;
                }

                // Crear y redimensionar el icono
                ImageIcon icon = new ImageIcon(new ImageIcon(emojiFile.getAbsolutePath()).getImage()
                        .getScaledInstance(20, 20, Image.SCALE_SMOOTH));
                SimpleAttributeSet attrSet = new SimpleAttributeSet();
                StyleConstants.setIcon(attrSet, icon);
                attrSet.addAttribute("emojiTag", ":" + emojiName + ":");

                // Reemplazar el texto del emoji con el icono
                int start = matcher.start();
                int end = matcher.end();

                // Diferir la modificación al documento
                SwingUtilities.invokeLater(() -> {
                    try {
                        doc.remove(start, end - start);
                        doc.insertString(start, " ", attrSet);
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void agregarEmojiEnJTextPane(JTextPane textPane, String emojiName) {
        StyledDocument doc = textPane.getStyledDocument();
        File emojiFile = new File("src/emojisName/" + emojiName + ".png");

        if (!emojiFile.exists()) {
            return;
        }

        // Cargar y redimensionar el emoji
        ImageIcon icon = new ImageIcon(emojiFile.getAbsolutePath());
        Image scaledImage = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH); // Ajusta el tamaño aquí
        icon = new ImageIcon(scaledImage);

        try {
            // Configurar el atributo del icono
            SimpleAttributeSet attrSet = new SimpleAttributeSet();
            StyleConstants.setIcon(attrSet, icon);
            attrSet.addAttribute("emojiTag", ":" + emojiName + ":");
            doc.insertString(doc.getLength(), " ", attrSet); // Espacio con el icono
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        Element element = doc.getCharacterElement(doc.getLength() - 1);
        AttributeSet attributes = element.getAttributes();

        Object emojiTag = attributes.getAttribute("emojiTag");

        if (emojiTag != null) {
            System.out.println("Atributo emojiTag encontrado: " + emojiTag);
        } else {
            System.out.println("No se encontró el atributo emojiTag.");
        }

    }

    private boolean isJTextPaneEmpty(JTextPane textPane) {
        return textPane.getDocument().getLength() == 0;

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        //</editor-fold>
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaC().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane ActiveUsers;
    private javax.swing.JButton btnEmoji;
    private javax.swing.JButton btnEnviar;
    private javax.swing.JComboBox cmbContactos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextPane txtHistorial;
    private javax.swing.JTextPane txtMensaje;
    // End of variables declaration//GEN-END:variables
    /**
     * Constante que almacena el puerto por defecto para la aplicación.
     */
    private final String DEFAULT_PORT = "10101";
    /**
     * Constante que almacena la IP por defecto (localhost) para el servidor.
     */
    private final String DEFAULT_IP = "127.0.0.1";
    /**
     * Constante que almacena el cliente, con el cual se gestiona la
     * comunicación con el servidor.
     */
    private final Cliente cliente;

    /**
     * Agrega un contacto al JComboBox de contactos.
     *
     * @param contacto
     */
    void addContacto(String contacto) {
        cmbContactos.addItem(contacto);
    }

    /**
     * Agrega un nuevo mensaje al historial de la conversación.
     *
     * @param emisor
     * @param mensaje
     */
    void addMensaje(String emisor, String mensaje) {
        StyledDocument historial = txtHistorial.getStyledDocument();
        try {
            // Añadir encabezado
            historial.insertString(historial.getLength(), "\n##### " + emisor + " ##### : \n", null);

            // Reemplazar etiquetas :emoji: con íconos
            Pattern emojiPattern = Pattern.compile(":(\\w+):"); // Busca etiquetas :emoji:
            Matcher matcher = emojiPattern.matcher(mensaje);

            int lastIndex = 0;
            while (matcher.find()) {
                String textBefore = mensaje.substring(lastIndex, matcher.start()); // Texto antes del emoji
                String emojiName = matcher.group(1); // Nombre del emoji

                // Inserta el texto antes del emoji
                if (!textBefore.isEmpty()) {
                    historial.insertString(historial.getLength(), textBefore, null);
                }

                // Verifica si existe el archivo del emoji
                File emojiFile = new File("src/emojisName/" + emojiName + ".png");
                if (emojiFile.exists()) {
                    // Crea y redimensiona el ícono
                    ImageIcon icon = new ImageIcon(new ImageIcon(emojiFile.getAbsolutePath()).getImage()
                            .getScaledInstance(20, 20, Image.SCALE_SMOOTH));
                    SimpleAttributeSet attrSet = new SimpleAttributeSet();
                    StyleConstants.setIcon(attrSet, icon);
                    attrSet.addAttribute("emojiTag", ":" + emojiName + ":");

                    // Inserta el ícono en el historial
                    historial.insertString(historial.getLength(), " ", attrSet);
                } else {
                    // Si no se encuentra el emoji, deja el texto como está
                    historial.insertString(historial.getLength(), matcher.group(), null);
                }

                lastIndex = matcher.end();
            }

            // Inserta cualquier texto que quede después del último emoji
            if (lastIndex < mensaje.length()) {
                historial.insertString(historial.getLength(), mensaje.substring(lastIndex), null);
            }

        } catch (BadLocationException ex) {
            Logger.getLogger(VentanaC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Se configura el título de la ventana para una nueva sesión.
     *
     * @param identificador
     */
    void sesionIniciada(String identificador) {
        this.setTitle(" --- " + identificador + " --- ");
    }

    /**
     * Método que abre una ventana para que el usuario ingrese la IP del host en
     * el que corre el servidor, el puerto con el que escucha y el nombre con el
     * que quiere participar en el chat.
     *
     * @return
     */
    private String[] getIP_Puerto_Nombre() {
        String s[] = new String[3];
        s[0] = DEFAULT_IP;
        s[1] = DEFAULT_PORT;
        JTextField ip = new JTextField(20);
        JTextField puerto = new JTextField(20);
        JTextField usuario = new JTextField(20);
        ip.setText(DEFAULT_IP);
        puerto.setText(DEFAULT_PORT);
        usuario.setText("Usuario");
        JPanel myPanel = new JPanel();
        myPanel.setLayout(new GridLayout(3, 2));
        myPanel.add(new JLabel("IP del Servidor:"));
        myPanel.add(ip);
        myPanel.add(new JLabel("Puerto de la conexión:"));
        myPanel.add(puerto);
        myPanel.add(new JLabel("Escriba su nombre:"));
        myPanel.add(usuario);
        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Configuraciones de la comunicación", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            s[0] = ip.getText();
            s[1] = puerto.getText();
            s[2] = usuario.getText();
        } else {
            System.exit(0);
        }
        return s;
    }

    /**
     * Método que elimina cierto cliente de la lista de contactos, este se llama
     * cuando cierto usuario cierra sesión.
     *
     * @param identificador
     */
    void eliminarContacto(String identificador) {
        for (int i = 0; i < cmbContactos.getItemCount(); i++) {
            if (cmbContactos.getItemAt(i).toString().equals(identificador)) {
                cmbContactos.removeItemAt(i);
                return;
            }
        }
    }
}
