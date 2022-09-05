package game.model;

import game.util.Cor;

/*
 * @author Walmick Diogenes
 */
public class Jogador {

	private String nome;
	private Cor cor;
	private int qtdDePedras = 9;

	public Jogador(String name, Cor color) {
		this.nome = name;
		this.cor = color;
	}

	public String getNome() {
		return nome;
	}

	public Cor getCor() {
		return cor;
	}

	/*
	 * Retorna o cor do jogador em letras minúsculas.
	 * 
	 * @return minúscula Representação de string do cor do jogador
	 */
	public String getColorString() {
		return cor.toString().toLowerCase();
	}

	/*
	 * Retorna o número de peças que o jogador tem para jogar
	 * 
	 * @return número de peças do jogador
	 */
	public int getQtdDePedras() {
		return qtdDePedras;
	}

	/*
	 * Se uma peça foi removida pelo oponente depois que uma trilha foi definida, o número de peças do jogador diminui.
	 */
	public void removerPedra() {
		qtdDePedras--;
	}

	@Override
	public String toString() {
		return "{Jogador: nome=" + nome + ", cor=" + cor + ", qtdDePedras=" + qtdDePedras + "}";
	}

}
