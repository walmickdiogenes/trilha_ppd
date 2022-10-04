package game.main;

import game.model.Envelope;
import game.model.Mensagem.Action;
import game.model.Mensagem;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadServidor extends Thread {

    private static Map<String, Socket> clientesMap = new HashMap<>();
    private Socket socket;

    public ThreadServidor(Socket s) {
        this.socket = s;
    }

    @Override
    public void run() {
        boolean sair = false;
        try {
            while (!sair) {
                //Entrada: recebendo mensagem do Cliente
                ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
                Envelope envelope = (Envelope) entrada.readObject();

                if (envelope.tipo() == Envelope.Tipo.Mensagem) {
                    Mensagem mensagem = (Mensagem) envelope.conteudo();//Recebendo mensagem do Cliente
                    Action action = mensagem.getAction();

                    switch (action) {
                        case CONNECT:
                            conectar(mensagem);
                            enviarParaTodos(mensagem, envelope.tipo());
                            break;
                        case DISCONNECT:
                            desconectar(mensagem);
                            enviarParaTodos(mensagem, envelope.tipo());
                            sair = true;
                            break;
                        case SEND:
                            enviarParaTodos(mensagem, envelope.tipo());
                            break;
                        default:
                            break;
                    }
                }

                if (envelope.tipo() == Envelope.Tipo.Jogada) {
                    enviarParaTodos(envelope.conteudo(), envelope.tipo());
                }

            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ThreadServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void conectar(Mensagem mensagem) {
        clientesMap.put(mensagem.getRemetente(), socket);
    }

    public void desconectar(Mensagem mensagem) throws IOException {
        clientesMap.remove(mensagem.getRemetente());
    }

    public void enviarParaTodos(Object mensagem, Envelope.Tipo tipo) throws IOException {
        for (Map.Entry<String, Socket> cliente : clientesMap.entrySet()) {
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getValue().getOutputStream());
            saida.writeObject(new Envelope(tipo, mensagem));
        }
    }
    
    
}