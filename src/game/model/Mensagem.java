package game.model;

import java.io.Serializable;
import java.util.ArrayList;


public class Mensagem implements Serializable {

    private String remetente;
    private String destinatario;
    private String texto;
    public static ArrayList<String> jogadoresOnline;

    private Action action;
    
    public ArrayList<String> getjogadoresOnline() {
        return jogadoresOnline;
    }

    public void setjogadoresOnline(ArrayList<String> jogadoresOnline) {
        this.jogadoresOnline = jogadoresOnline;
    }

    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }
    
    public enum Action {
        CONNECT, DISCONNECT, SEND
    }
}