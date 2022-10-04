package game.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import game.util.Cor;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

/*
 * @author Walmick Diogenes
 */
public class Tabuleiro implements Serializable {

	private static Tabuleiro instance = new Tabuleiro();

	private boolean novoJogo;
	private boolean jogoGanho;
	private boolean definirTrilha;
	private Location localizacaoPedraColocada;

	private Map<Location, Cor> gridJogo;

	private int qtdPedrasColocadasTabuleiro;
	private Location novaLocalizacao;
	private Location antigaLocalizacao;
	private List<Location> localizacoesPermitidas;
	private List<Location> todasLocalizacoes;
	private final Location todasLocalizacoesSinalizadas;
	private Map<Location, Cor> localizacaoSemPedra;
	private Trilha trilha;

	private Location l00 = new Location(0, 0);
	private Location l03 = new Location(0, 3);
	private Location l06 = new Location(0, 6);
	private Location l11 = new Location(1, 1);
	private Location l13 = new Location(1, 3);
	private Location l15 = new Location(1, 5);
	private Location l22 = new Location(2, 2);
	private Location l23 = new Location(2, 3);
	private Location l24 = new Location(2, 4);
	private Location l30 = new Location(3, 0);
	private Location l31 = new Location(3, 1);
	private Location l32 = new Location(3, 2);
	private Location l34 = new Location(3, 4);
	private Location l35 = new Location(3, 5);
	private Location l36 = new Location(3, 6);
	private Location l42 = new Location(4, 2);
	private Location l43 = new Location(4, 3);
	private Location l44 = new Location(4, 4);
	private Location l51 = new Location(5, 1);
	private Location l53 = new Location(5, 3);
	private Location l55 = new Location(5, 5);
	private Location l60 = new Location(6, 0);
	private Location l63 = new Location(6, 3);
	private Location l66 = new Location(6, 6);

	private Tabuleiro() {
		gridJogo = new HashMap<>();
		trilha = new Trilha();
		localizacoesPermitidas = new ArrayList<>();
		todasLocalizacoes = new ArrayList<>();
		todasLocalizacoesSinalizadas = new Location(-1, -1);
		localizacaoSemPedra = new HashMap<>();
	}

	public static Tabuleiro getInstance() {
		return instance;
	}

	/*
	 * Inicializa todos os atributos relevantes.
	 */
	public void iniciarJogo() {
		setNovoJogo(false);
		setJogoGanho(false);
		setDefinirTrilha(false);
		setLocalizacaoPedraColocada(null);

		qtdPedrasColocadasTabuleiro = 0;
		novaLocalizacao = null;
		antigaLocalizacao = null;

		gridJogo.clear();
		localizacoesPermitidas.clear();
		preencherListaTodasLocalizacoes();
		localizacaoSemPedra.clear();
		trilha.clear();
	}

	public transient BooleanProperty novoJogoProperty = new SimpleBooleanProperty();

	public final void setNovoJogo(final boolean novoJogo) {
		this.novoJogo = novoJogo;
		this.novoJogoProperty.set(novoJogo);
	}

	public transient BooleanProperty jogoGanhoProperty = new SimpleBooleanProperty();

	public final boolean isJogoGanho() {
		return this.jogoGanho;
	}

	public final void setJogoGanho(final boolean jogoGanho) {
		this.jogoGanho = jogoGanho;
		this.jogoGanhoProperty.set(jogoGanho);
	}

	public transient ObjectProperty<Location> localizacaoPedraColocadaProperty = new SimpleObjectProperty<Location>();

	public final Location getLocalizacaoPedraColocada() {
		return this.localizacaoPedraColocada;
	}

	public final void setLocalizacaoPedraColocada(final Location localizacaoPedraColocada) {
		this.localizacaoPedraColocada = localizacaoPedraColocada;
		this.localizacaoPedraColocadaProperty.set(localizacaoPedraColocada);
	}

	public transient BooleanProperty definirTrilhaProperty = new SimpleBooleanProperty();

	public final boolean isDefinirTrilha() {
		return this.definirTrilha;
	}

	public final void setDefinirTrilha(final boolean definirTrilha) {
		this.definirTrilha = definirTrilha;
		this.definirTrilhaProperty.set(definirTrilha);
	}

	public Map<Location, Cor> getGridJogo() {
		return gridJogo;
	}

	public Trilha getTrilha() {
		return trilha;
	}

	public int getQtdPedrasColocadasTabuleiro() {
		return qtdPedrasColocadasTabuleiro;
	}

	public List<Location> getLocalizacoesPermitidas() {
		return localizacoesPermitidas;
	}

	public Location getTodasLocalizacoesSinalizadas() {
		return todasLocalizacoesSinalizadas;
	}

	public Map<Location, Cor> getLocalizacaoSemPedra() {
		return localizacaoSemPedra;
	}

	public void setNovaLocalizacao(Location novaLocalizacao) {
		this.novaLocalizacao = novaLocalizacao;
	}

	/*
	 * Define os possíveis locais de lançamento para o jogador atual.
	 * 
	 * @param antigaLocalizacao
	 * @param id
	 *            1 se uma pedra pode ser largada em qualquer lugar do tabuleiro, 2 caso contrário
	 */
	public void setAntigaLocalizacao(Location antigaLocalizacao, int id) {
		this.antigaLocalizacao = antigaLocalizacao;
		localizacoesPermitidas.clear();

		if (id == 1) {
			localizacoesPermitidas.addAll(todasLocalizacoes);
		} else if (id == 0) {
			if (antigaLocalizacao.equals(l00)) {
				localizacoesPermitidas.add(l30);
				localizacoesPermitidas.add(l03);
			} else if (antigaLocalizacao.equals(l30)) {
				localizacoesPermitidas.add(l00);
				localizacoesPermitidas.add(l60);
				localizacoesPermitidas.add(l31);
			} else if (antigaLocalizacao.equals(l60)) {
				localizacoesPermitidas.add(l30);
				localizacoesPermitidas.add(l63);
			} else if (antigaLocalizacao.equals(l11)) {
				localizacoesPermitidas.add(l31);
				localizacoesPermitidas.add(l13);
			} else if (antigaLocalizacao.equals(l31)) {
				localizacoesPermitidas.add(l51);
				localizacoesPermitidas.add(l11);
				localizacoesPermitidas.add(l30);
				localizacoesPermitidas.add(l32);
			} else if (antigaLocalizacao.equals(l51)) {
				localizacoesPermitidas.add(l31);
				localizacoesPermitidas.add(l53);
			} else if (antigaLocalizacao.equals(l22)) {
				localizacoesPermitidas.add(l32);
				localizacoesPermitidas.add(l23);
			} else if (antigaLocalizacao.equals(l32)) {
				localizacoesPermitidas.add(l22);
				localizacoesPermitidas.add(l42);
				localizacoesPermitidas.add(l31);
			} else if (antigaLocalizacao.equals(l42)) {
				localizacoesPermitidas.add(l32);
				localizacoesPermitidas.add(l43);
			} else if (antigaLocalizacao.equals(l03)) {
				localizacoesPermitidas.add(l00);
				localizacoesPermitidas.add(l06);
				localizacoesPermitidas.add(l13);
			} else if (antigaLocalizacao.equals(l13)) {
				localizacoesPermitidas.add(l11);
				localizacoesPermitidas.add(l15);
				localizacoesPermitidas.add(l03);
				localizacoesPermitidas.add(l23);
			} else if (antigaLocalizacao.equals(l23)) {
				localizacoesPermitidas.add(l22);
				localizacoesPermitidas.add(l24);
				localizacoesPermitidas.add(l13);
			} else if (antigaLocalizacao.equals(l43)) {
				localizacoesPermitidas.add(l42);
				localizacoesPermitidas.add(l44);
				localizacoesPermitidas.add(l53);
			} else if (antigaLocalizacao.equals(l53)) {
				localizacoesPermitidas.add(l51);
				localizacoesPermitidas.add(l55);
				localizacoesPermitidas.add(l43);
				localizacoesPermitidas.add(l63);
			} else if (antigaLocalizacao.equals(l63)) {
				localizacoesPermitidas.add(l60);
				localizacoesPermitidas.add(l66);
				localizacoesPermitidas.add(l53);
			} else if (antigaLocalizacao.equals(l24)) {
				localizacoesPermitidas.add(l23);
				localizacoesPermitidas.add(l34);
			} else if (antigaLocalizacao.equals(l34)) {
				localizacoesPermitidas.add(l24);
				localizacoesPermitidas.add(l44);
				localizacoesPermitidas.add(l35);
			} else if (antigaLocalizacao.equals(l44)) {
				localizacoesPermitidas.add(l34);
				localizacoesPermitidas.add(l43);
			} else if (antigaLocalizacao.equals(l15)) {
				localizacoesPermitidas.add(l13);
				localizacoesPermitidas.add(l35);
			} else if (antigaLocalizacao.equals(l35)) {
				localizacoesPermitidas.add(l15);
				localizacoesPermitidas.add(l55);
				localizacoesPermitidas.add(l34);
				localizacoesPermitidas.add(l36);
			} else if (antigaLocalizacao.equals(l55)) {
				localizacoesPermitidas.add(l53);
				localizacoesPermitidas.add(l35);
			} else if (antigaLocalizacao.equals(l06)) {
				localizacoesPermitidas.add(l03);
				localizacoesPermitidas.add(l36);
			} else if (antigaLocalizacao.equals(l36)) {
				localizacoesPermitidas.add(l06);
				localizacoesPermitidas.add(l66);
				localizacoesPermitidas.add(l35);
			} else if (antigaLocalizacao.equals(l66)) {
				localizacoesPermitidas.add(l36);
				localizacoesPermitidas.add(l63);
			}
		}

	}

	/*
	 * Adiciona todos os locais possíveis de pedra a uma lista.
	 */
	private void preencherListaTodasLocalizacoes() {
		todasLocalizacoes.clear();
		todasLocalizacoes.addAll(Arrays.asList(l00, l03, l06, l11, l13, l15, l22, l23, l24, l30, l31, l32, l34, l35, l36, l42, l43, l44, l51, l53, l55,
				l60, l63, l66));
	}

	/*
	 * Adiciona uma pedra à lista de peças livres no tabuleiro.
	 * 
	 * @param color
	 *            cor da pedra
	 */
	public void addPedraTabuleiro(Cor color) {
		gridJogo.put(novaLocalizacao, color);
		addLocalizacaoSemPedra(novaLocalizacao, color);
		qtdPedrasColocadasTabuleiro++;
	}

	/*
	 * Atualiza o estado do tabuleiro depois de fazer um movimento nele.
	 * 
	 * @param color
	 *            cor da pedra
	 */
	public void atualizarTabuleiro(Cor color) {
		gridJogo.remove(antigaLocalizacao);
		trilha.removeLocalTrilha(color, antigaLocalizacao);
		removeLocalizacaoSemPedra(antigaLocalizacao);

		addPedraTabuleiro(color);
	}

	/*
	 * Remove os locais aprovados da lista de locais de pedras livres.
	 * 
	 * @param locations
	 *            locais das pedras a serem removidas
	 */
	public void removeLocalizacaoSemPedra(Location... locations) {
		for (Location location : locations) {
			if (localizacaoSemPedra.containsKey(location)) {
				localizacaoSemPedra.remove(location);
			}
		}
	}

	/*
	 * Adiciona o local passado à lista localizaçoes sem pedra.
	 * 
	 * @param location
	 *            nova localizacao de uma pedra
	 * @param color
	 *            cor da pedra
	 */
	public void addLocalizacaoSemPedra(Location location, Cor color) {
		localizacaoSemPedra.put(location, color);
	}

	/*
	 * Verifica se as pedras podem ser movidas ou não
	 * 
	 * @param location
	 *            localização da pedra para verificar
	 * @return true se um bloco pode ser movido, false caso contrário
	 */
	public boolean proxMovimentoPossivel(Location location) {
		boolean successo = false;
		Iterator<Location> it;

		setAntigaLocalizacao(location, 0);

		it = localizacoesPermitidas.iterator();
		while (it.hasNext() && !successo) {
			if (gridJogo.get(it.next()) == null) {
				successo = true;
			}
		}

		return successo;
	}

	/*
	 * Verifica se uma trilha foi definida.
	 * 
	 * @param location
	 *            local recém-definido
	 * @return true se merel foi definido, false caso contrário
	 */
	public boolean verificaTrilha(Location location) {
		if (location.equals(l00)) {
			return verificaLocalizacao(l00, l30, l60, l00, l03, l06);
		} else if (location.equals(l30)) {
			return verificaLocalizacao(l00, l30, l60, l30, l31, l32);
		} else if (location.equals(l60)) {
			return verificaLocalizacao(l00, l30, l60, l60, l63, l66);
		} else if (location.equals(l11)) {
			return verificaLocalizacao(l11, l13, l15, l11, l31, l51);
		} else if (location.equals(l31)) {
			return verificaLocalizacao(l30, l31, l32, l11, l31, l51);
		} else if (location.equals(l51)) {
			return verificaLocalizacao(l51, l53, l55, l11, l31, l51);
		} else if (location.equals(l22)) {
			return verificaLocalizacao(l22, l23, l24, l22, l32, l42);
		} else if (location.equals(l32)) {
			return verificaLocalizacao(l30, l31, l32, l22, l32, l42);
		} else if (location.equals(l42)) {
			return verificaLocalizacao(l42, l43, l44, l22, l32, l42);
		} else if (location.equals(l03)) {
			return verificaLocalizacao(l00, l03, l06, l03, l13, l23);
		} else if (location.equals(l13)) {
			return verificaLocalizacao(l11, l13, l15, l03, l13, l23);
		} else if (location.equals(l23)) {
			return verificaLocalizacao(l22, l23, l24, l03, l13, l23);
		} else if (location.equals(l43)) {
			return verificaLocalizacao(l42, l43, l44, l43, l53, l63);
		} else if (location.equals(l53)) {
			return verificaLocalizacao(l51, l53, l55, l43, l53, l63);
		} else if (location.equals(l63)) {
			return verificaLocalizacao(l60, l63, l66, l43, l53, l63);
		} else if (location.equals(l24)) {
			return verificaLocalizacao(l22, l23, l24, l24, l34, l44);
		} else if (location.equals(l34)) {
			return verificaLocalizacao(l34, l35, l36, l24, l34, l44);
		} else if (location.equals(l44)) {
			return verificaLocalizacao(l42, l43, l44, l24, l34, l44);
		} else if (location.equals(l15)) {
			return verificaLocalizacao(l11, l13, l15, l15, l35, l55);
		} else if (location.equals(l35)) {
			return verificaLocalizacao(l34, l35, l36, l15, l35, l55);
		} else if (location.equals(l55)) {
			return verificaLocalizacao(l51, l53, l55, l15, l35, l55);
		} else if (location.equals(l06)) {
			return verificaLocalizacao(l00, l03, l06, l06, l36, l66);
		} else if (location.equals(l36)) {
			return verificaLocalizacao(l34, l35, l36, l06, l36, l66);
		} else if (location.equals(l66)) {
			return verificaLocalizacao(l60, l63, l66, l06, l36, l66);
		}
		return false;
	}

	/*
	 * Verifica se os locais passados estão construindo uma trilha ou não.
	 * 
	 * @param l1 localização da pedra 1 da trilha #1
	 * @param l2 localização da pedra 2 da trilha #1
	 * @param l3 localização da pedra 3 da trilha#1
	 * @param l4 localização da pedra 1 da trilha #2
	 * @param l5 localização da pedra 2 da trilha #2
	 * @param l6 localização da pedra 3 da trilha #2
	 * @return true se merel estiver definido, false caso contrário
	 */
	private boolean verificaLocalizacao(Location l1, Location l2, Location l3, Location l4, Location l5, Location l6) {
		boolean trilhaEncontrada = false;
		if (gridJogo.get(l1) != null && gridJogo.get(l2) != null && gridJogo.get(l3) != null) {
			if ((gridJogo.get(l1) == gridJogo.get(l2) && gridJogo.get(l2) == gridJogo.get(l3))) {
				trilha.addTrilha(l1, l2, l3);
				removeLocalizacaoSemPedra(l1, l2, l3);
				trilhaEncontrada = true;
			}
		}
		if (gridJogo.get(l4) != null && gridJogo.get(l5) != null && gridJogo.get(l6) != null) {
			if ((gridJogo.get(l4) == gridJogo.get(l5) && gridJogo.get(l5) == gridJogo.get(l6))) {
				trilha.addTrilha(l4, l5, l6);
				removeLocalizacaoSemPedra(l4, l5, l6);
				trilhaEncontrada = true;
			}
		}
		return trilhaEncontrada;
	}

	/*
	 * Verifica se uma pedra pode realmente ser colocada no local solicitado.
	 * 
	 * @param location
	 *            possível local de queda
	 * @return true se o bloco puder ser descartado, false caso contrário
	 */
	public boolean pedraPodeSerColocada(Location location) {
		return localizacoesPermitidas.contains(location);
	}

	/*
	 * Verifica se a pedra no local solicitado pode ser removida.
	 * 
	 * @param location
	 *            possível local a ser removido
	 * @param color
	 *            cor da pedra
	 * @return true se o bloco puder ser removido, false caso contrário
	 */
	public boolean pedraPodeSerRemovida(Location location, Cor color) {
		if (trilha.getExisteTrilha().contains(location)) {
			if (localizacaoSemPedra.containsValue(color)) {
				return false;
			}
		}
		return true;
	}

	/*
	 * Remove o local passado de todas as estruturas de dados relevantes onde as pedras são salvas.
	 * 
	 * @param location
	 *            o local a ser removido
	 * @param color
	 *            cor da pedra
	 */
	public void removerPedra(Location location, Cor color) {
		gridJogo.remove(location);
		trilha.removeLocalTrilha(color, location);
		removeLocalizacaoSemPedra(location);
	}

}
