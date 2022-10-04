package game.model;

import java.io.Serializable;

public class Envelope implements Serializable {
    private final Tipo tipo;
    private final Object conteudo;

    public Envelope(Tipo tipo, Object conteudo) {
        this.tipo = tipo;
        this.conteudo = conteudo;
    }

    public Tipo tipo() {
        return this.tipo;
    }

    public Object conteudo() {
        return this.conteudo;
    }

    public enum Tipo {
        Mensagem, Jogada
    }
}