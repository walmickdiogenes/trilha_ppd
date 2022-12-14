package game;

import game.controller.RootLayoutController;
import game.model.Envelope;
import game.model.Mensagem;
import game.model.Mensagem.Action;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class ThreadCliente extends Thread {

    private final Socket socket;
    private final TextArea textArea;
    private final RootLayoutController rootLayoutController;
    private final String remetente;
    boolean sair = false;

    public ThreadCliente(String r, Socket s, TextArea textArea, RootLayoutController rootLayoutController) {
        this.remetente = r;
        this.socket = s;
        this.textArea = textArea;
        this.rootLayoutController = rootLayoutController;
    }

    @Override
    public void run() {
        try {
            while (!sair) {
                ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
                Envelope envelope = (Envelope) entrada.readObject();

                if (envelope.tipo() == Envelope.Tipo.Mensagem) {
                    Mensagem mensagem = (Mensagem) envelope.conteudo();

                    Action action = mensagem.getAction();

                    switch (action) {
                        case CONNECT:
                            conectar(mensagem);
                            break;
                        case DISCONNECT:
                            desconectar(mensagem);
                            break;
                        case SEND:
                            receberMensagem(mensagem);
                            break;
                        default:
                            break;
                    }
                }

                if (envelope.tipo() == Envelope.Tipo.Jogada) {
                    var gm = (GameManager) envelope.conteudo();
                    rootLayoutController.setGameManager(gm);
                }

            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ThreadServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void conectar(Mensagem mensagem) {
        Platform.runLater(() -> this.textArea.appendText(mensagem.getRemetente() + " >> " + mensagem.getTexto() + "\n"));
    }

    public void desconectar(Mensagem mensagem) throws IOException {
        Platform.runLater(() -> this.textArea.appendText(mensagem.getRemetente() + " >> " + mensagem.getTexto() + "\n"));

        if (mensagem.getRemetente().equals(this.remetente)) {
            this.socket.close();
            this.sair = true;
        }
    }

    public void receberMensagem(Mensagem mensagem) throws IOException {
        Platform.runLater(() -> this.textArea.appendText(mensagem.getRemetente() + " >> " + mensagem.getTexto() + "\n"));
    }
}