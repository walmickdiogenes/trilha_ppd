package game.main;

import java.io.Serializable;
import java.util.List;

import game.model.Tabuleiro;
import game.model.Location;
import game.model.Jogador;
import game.util.Cor;
import game.util.TrilhaFase;

/*
 * @author Walmick Diogenes
 */
public class GameManager implements Serializable {

	private Jogador jogador;
	private Jogador outroJogador;
	private Jogador jogador1;
	private Jogador jogador2;

	public Tabuleiro tabuleiro;

	private TrilhaFase gamePhase;

	public GameManager() {
		tabuleiro = Tabuleiro.getInstance();

		startTrilha();
	}

	public void startTrilha() {
		jogador1 = new Jogador("Jogador 1", Cor.PRETA);
		jogador2 = new Jogador("Jogador 2", Cor.BRANCA);
		jogador = jogador1;
		outroJogador = jogador2;

		gamePhase = TrilhaFase.COLOCAR;

		tabuleiro.iniciarJogo();
	}

	public Jogador getJogadorAtual() {
		return jogador;
	}

	public Jogador getOutroJogador() {
		return outroJogador;
	}

	public Cor getCorJogadorAtual() {
		return jogador.getCor();
	}

	public Cor getCorOutroJogador() {
		return outroJogador.getCor();
	}

	public TrilhaFase getFase() {
		return gamePhase;
	}

	public List<Location> getAllowedDropLocations() {
		return tabuleiro.getLocalizacoesPermitidas();
	}

	public void setAllowDropLocations(Location oldLocation) {
		if (gamePhase == TrilhaFase.COLOCAR || jogador.getQtdDePedras() == 3 && gamePhase == TrilhaFase.FINALIZAR) {
			tabuleiro.setAntigaLocalizacao(oldLocation, 1);
		} else {
			tabuleiro.setAntigaLocalizacao(oldLocation, 0);
		}
	}

	/*
	 * Reage ao evento quando uma peça foi colocada no tabuleiro.
	 * 
	 * @param location
	 *            nova localização de uma pedra
	 */
	public void colocarPedra(Location location) {
		tabuleiro.setNovaLocalizacao(location);

		if (gamePhase == TrilhaFase.COLOCAR) {
			tabuleiro.addPedraTabuleiro(jogador.getCor());
		} else {
			tabuleiro.atualizarTabuleiro(jogador.getCor());
		}

		if (tabuleiro.getQtdPedrasColocadasTabuleiro() == 18) {
			gamePhase = TrilhaFase.MOVER;
		}
		if (tabuleiro.getQtdPedrasColocadasTabuleiro() >= 5) {
			if (tabuleiro.verificaTrilha(location)) {
				tabuleiro.setDefinirTrilha(true);
			}
		}

		mudaJogador();

		if (gamePhase == TrilhaFase.MOVER) {
			if (!proxMovimentoPossivel()) {
				tabuleiro.setJogoGanho(true);
			}
		}
	}

	/*
	 * Changes the current jogador.
	 */
	private void mudaJogador() {
		if (jogador.equals(jogador1)) {
			jogador = jogador2;
			outroJogador = jogador1;
		} else {
			jogador = jogador1;
			outroJogador = jogador2;
		}
	}

	/*
	 * Verifica se um movimento é possível.
	 * 
	 * @return true se a movimentação for possível, false caso contrário
	 */
	public boolean proxMovimentoPossivel() {
		boolean pedraPodeSerColocada = false;
		for (Location location : tabuleiro.getGridJogo().keySet()) {
			if (jogador.equals(jogador1) && tabuleiro.getGridJogo().get(location) == jogador.getCor()
					|| jogador.equals(jogador2) && tabuleiro.getGridJogo().get(location) == jogador.getCor()) {
				pedraPodeSerColocada = tabuleiro.proxMovimentoPossivel(location);
				if (tabuleiro.proxMovimentoPossivel(location)) {
					pedraPodeSerColocada = true;
					break;
				}
			}
		}
		return pedraPodeSerColocada;
	}

	/*
	 * Verifica se uma pedra pode ser solta no local solicitado.
	 * 
	 * @param location
	 *            possível local solicitado
	 * @return true se a pedra puder ser descartada, false caso contrário
	 */
	public boolean pedraPodeSerColocada(Location location) {
		return tabuleiro.pedraPodeSerColocada(location);
	}

	/*
	 * Verifica se uma pedra no local solicitado pode ser removida.
	 * 
	 * @param location
	 *            possível local de onde uma pedra deve ser removida
	 * @return true se a pedra puder ser removido, false caso contrário
	 */
	public boolean pedraPodeSerRemovida(Location location) {
		return tabuleiro.pedraPodeSerRemovida(location, jogador.getCor());
	}

	/*
	 * Verifica se uma pedra no local solicitado pode ser removida.
	 * 
	 * @param location
	 *            possível local de onde uma pedra deve ser removida
	 * @param color
	 *            cor da peddra
	 * @return true se a pedra puder ser removido, false caso contrário
	 */
	public boolean pedraPodeSerRemovida(Location location, Cor color) {
		return tabuleiro.pedraPodeSerRemovida(location, color);
	}

	/*
	 * Verifica se a fase de jogo "fim do jogo" foi atingida
	 * 
	 * @return true se o jogo final da fase for alcançado, false caso contrário
	 */
	public boolean fimJogo() {
		return gamePhase == TrilhaFase.FINALIZAR && outroJogador.getQtdDePedras() == 3;
	}

	/*
	 * Remove a pedra no local solicitado.
	 * 
	 * @param location
	 *            o local de onde a pedra é removida
	 */
	public void removerPedra(Location location) {
		jogador.removerPedra();

		if (jogador.getQtdDePedras() == 3 && gamePhase != TrilhaFase.FINALIZAR) {
			gamePhase = TrilhaFase.FINALIZAR;
		}
		if (jogador.getQtdDePedras() < 3) {
			tabuleiro.setJogoGanho(true);
		}

		tabuleiro.removerPedra(location, jogador.getCor());
	}

}
