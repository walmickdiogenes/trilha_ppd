package game.model;

import java.util.ArrayList;
import java.util.List;

import game.util.Cor;

/*
 * @author Walmick Diogenes
 */
public class Trilha {

	private List<Location> existeTrilha;

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

	public Trilha() {
		existeTrilha = new ArrayList<>();
	}

	public List<Location> getExisteTrilha() {
		return existeTrilha;
	}

	/*
	 * Limpa a lista de trilhas existentes.
	 */
	public void clear() {
		existeTrilha.clear();
	}

	/*
	 * Remove os locais solicitados da lista de trilhas existentes.
	 * 
	 * @param color
	 *            cor da pedra
	 * @param locations
	 *            locais a serem removidos
	 */
	private void removeLocal(Cor color, Location... locations) {
		for (Location location : locations) {
			if (!existeTrilha.contains(location)) {
				return;
			}
		}
		for (Location location : locations) {
			existeTrilha.remove(location);
			if (!existeTrilha.contains(location)) {
				Tabuleiro.getInstance().addLocalizacaoSemPedra(location, color);
			}
		}
	}

	/*
	 * Adiciona as localizações à lista de trilhas existentes.
	 * 
	 * @param locations
	 *            local da pedra na trilha
	 */
	public void addTrilha(Location... locations) {
		for (Location location : locations) {
			existeTrilha.add(location);
		}
	}

	/*
	 * Remove locais da lista de trilhas existente dependendo do local passado.
	 * 
	 * @param color
	 *            cor da pedra
	 * @param location
	 *            localização, que determina quais trilhas devem ser removidas
	 */
	public void removeLocalTrilha(Cor color, Location location) {
		if (location.equals(l00)) {
			removeLocal(color, l00, l30, l60);
			removeLocal(color, l00, l03, l06);
		} else if (location.equals(l30)) {
			removeLocal(color, l00, l30, l60);
			removeLocal(color, l30, l31, l32);
		} else if (location.equals(l60)) {
			removeLocal(color, l60, l63, l66);
			removeLocal(color, l00, l30, l60);
		} else if (location.equals(l11)) {
			removeLocal(color, l11, l31, l51);
			removeLocal(color, l11, l13, l15);
		} else if (location.equals(l31)) {
			removeLocal(color, l11, l31, l51);
			removeLocal(color, l30, l31, l32);
		} else if (location.equals(l51)) {
			removeLocal(color, l11, l31, l51);
			removeLocal(color, l51, l53, l55);
		} else if (location.equals(l22)) {
			removeLocal(color, l22, l32, l42);
			removeLocal(color, l22, l23, l24);
		} else if (location.equals(l32)) {
			removeLocal(color, l22, l32, l42);
			removeLocal(color, l30, l31, l32);
		} else if (location.equals(l42)) {
			removeLocal(color, l22, l32, l42);
			removeLocal(color, l42, l43, l44);
		} else if (location.equals(l03)) {
			removeLocal(color, l00, l03, l06);
			removeLocal(color, l03, l13, l23);
		} else if (location.equals(l13)) {
			removeLocal(color, l11, l13, l15);
			removeLocal(color, l03, l13, l23);
		} else if (location.equals(l23)) {
			removeLocal(color, l22, l23, l24);
			removeLocal(color, l03, l13, l23);
		} else if (location.equals(l43)) {
			removeLocal(color, l43, l53, l63);
			removeLocal(color, l42, l43, l44);
		} else if (location.equals(l53)) {
			removeLocal(color, l43, l53, l63);
			removeLocal(color, l51, l53, l55);
		} else if (location.equals(l63)) {
			removeLocal(color, l43, l53, l63);
			removeLocal(color, l60, l63, l66);
		} else if (location.equals(l24)) {
			removeLocal(color, l24, l34, l44);
			removeLocal(color, l22, l23, l24);
		} else if (location.equals(l34)) {
			removeLocal(color, l24, l34, l44);
			removeLocal(color, l34, l35, l36);
		} else if (location.equals(l44)) {
			removeLocal(color, l24, l34, l44);
			removeLocal(color, l42, l43, l44);
		} else if (location.equals(l15)) {
			removeLocal(color, l15, l35, l55);
			removeLocal(color, l11, l13, l15);
		} else if (location.equals(l35)) {
			removeLocal(color, l15, l35, l55);
			removeLocal(color, l34, l35, l36);
		} else if (location.equals(l55)) {
			removeLocal(color, l15, l35, l55);
			removeLocal(color, l51, l53, l55);
		} else if (location.equals(l06)) {
			removeLocal(color, l00, l03, l06);
			removeLocal(color, l06, l36, l66);
		} else if (location.equals(l36)) {
			removeLocal(color, l34, l35, l36);
			removeLocal(color, l06, l36, l66);
		} else if (location.equals(l66)) {
			removeLocal(color, l60, l63, l66);
			removeLocal(color, l06, l36, l66);
		}
	}

}
