package game.model;

import java.io.Serializable;

public class Jogada implements Serializable {
    
    private String jogador;
    private String outrojogador;
    private String texto;
    
    private Action action;
    
   
    public enum Action {
        
    }
}
