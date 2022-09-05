package game.controller;

import game.GameManager;
import game.GameTrilha;
import game.ThreadCliente;
import game.model.Tabuleiro;
import game.model.Location;
import game.model.Mensagem;
import game.util.Cor;
import game.util.TrilhaFase;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/*
 * @author Walmick Diogenes
 */
public class RootLayoutController {

	private Stage stage;
	private Tabuleiro tabuleiro;
	private GameManager gameManager;
        private Socket socket;
        private String remetente;

	private ObservableList<ImageView> gridTabuleiroChildren = FXCollections.observableArrayList();

	@FXML
	private GridPane gridTabuleiro;
	@FXML
	private GridPane gridPedrasEsquerda;
	@FXML
	private GridPane gridPedrasDireita;
        @FXML
        private TextField textIP;
        @FXML
        private TextField textPorta;
        @FXML
        private TextArea textAreaHistorico;
        @FXML
        private TextField textNomeJogador;
        @FXML
        private TextField textMensagem;
        
        /*
	* Obtem o nome do jogador;
        * Conectando ao Servidor do Chat;
        * Envia a primeira mensagem informando conexão (apenas para passar o nome do cliente);
        * Instancia uma ThreadCliente para ficar recebendo mensagens do Servidor;
	*/
        public void handleButtonConectar() {
            this.remetente = textNomeJogador.getText();

            try { 
                socket = new Socket(textIP.getText(), Integer.valueOf(textPorta.getText()));
                ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());

                Mensagem mensagem = new Mensagem();
                mensagem.setRemetente(remetente);
                mensagem.setTexto("Entrou no Jogo!");
                mensagem.setAction(Mensagem.Action.CONNECT);

                ThreadCliente thread = new ThreadCliente(remetente, socket, textAreaHistorico);
                thread.setName("Thread Cliente " + remetente);
                thread.start();

                //Saída de Dados do Cliente
                saida.writeObject(mensagem); //Enviando mensagem para Servidor

                } catch (IOException ex) {
                    Logger.getLogger(RootLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                }
                desabilitarTextFields();
        }
        
        /*
	* Desabilitando alguns TextField após conexão
	*/
        public void desabilitarTextFields() {
            this.textNomeJogador.setEditable(false);
            this.textIP.setEditable(false);
            this.textPorta.setEditable(false);
        }
        
        /*
	 * Evia a mensagem para o servidor, para que em seguida, ele envie para o oponente
	*/
        public void handleButtonEnviar() {
            try {
                Mensagem mensagem = new Mensagem();
                mensagem.setRemetente(this.remetente);
                mensagem.setTexto(this.textMensagem.getText());
                mensagem.setAction(Mensagem.Action.SEND);

                //Saída de Dados do Cliente
                ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
                saida.writeObject(mensagem); //Enviando mensagem para Servidor

                this.textMensagem.setText("");

            } catch (IOException ex) {
                Logger.getLogger(RootLayoutController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        /*
	 * Abre uma caixa de diálogo antes de fechar o jogo e sinaliza para o oponente via chat.
	 */
        public void handleButtonSair() {
            novoJogoEFecharDialogo(GameTrilha.GAME_NAME, "Sair do Jogo?", "Você realmente quer sair do jogo?", 1);
            try {
                Mensagem mensagem = new Mensagem();
                mensagem.setRemetente(this.remetente);
                mensagem.setTexto("Saiu do Jogo!");
                mensagem.setAction(Mensagem.Action.DISCONNECT);

                //Saída de Dados do Cliente
                ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
                saida.writeObject(mensagem); //Enviando mensagem para Servidor

            } catch (IOException ex) {
                Logger.getLogger(RootLayoutController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

	/*
	 * Retorna o local do {@code ImageView} passado para o método.
	 * 
	 * @param iv
	 *            {@code ImageView} para descobrir a localização de
	 * @return local no {@code GridPane} correspondente
	 */
	private Location getLocalizacaoPedra(ImageView iv) {
		Integer coluna = GridPane.getColumnIndex(iv);
		Integer linha = GridPane.getRowIndex(iv);
		return new Location(coluna == null ? 0 : coluna, linha == null ? 0 : linha);
	}

	/*
	 * Define o estágio do aplicativo.
	 * 
	 * @param stage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/*
	 * Define o objeto {@code GameManager} no qual a lógica do jogo é colocada.
	 * 
	 * @param gameManager
	 */
	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}
        
	/*
	 * Abre uma caixa de diálogo antes de fechar o jogo.
	 */
	@FXML
	public void handleClose() {
		novoJogoEFecharDialogo(GameTrilha.GAME_NAME, "Sair do Jogo?", "Você realmente quer sair do jogo?", 1);
	}

	/*
	 * Abre uma caixa de diálogo de confirmação especificada pelos parâmetros fornecidos.
	 * 
	 * @param title
	 * @param header
	 * @param contentText
	 * @param id
	 *            0 if new game dialog, 1 if close dialog
	 */
	private void novoJogoEFecharDialogo(String title, String header, String contentText, int id) {
		ButtonType btnSim = new ButtonType("Sim", ButtonData.YES);
		ButtonType btnNao = new ButtonType("Não", ButtonData.NO);
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(contentText);
		alert.initOwner(stage);
		alert.getButtonTypes().clear();
		alert.getButtonTypes().addAll(btnSim, btnNao);
		alert.showAndWait();
		if (id == 0) {
			if (alert.getResult() == btnSim) {
				initWindow();
				tabuleiro.setNovoJogo(true);
			}
		} else if (id == 1) {
			if (alert.getResult() == btnSim) {
				Platform.exit();
			}
		}
	}

        
	/*
	 * Se um movimento inválido for feito por um jogador, esta caixa de diálogo aparecerá.
	 * 
	 * @param contentText
	 *            a mensagem a ser exibida na caixa de diálogo
	 */
	private void movimentoInvalidoDialogo(String contentText) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Atenção");
		alert.setHeaderText(null);
		alert.setContentText(contentText);
		alert.initStyle(StageStyle.UTILITY);
		alert.initOwner(stage);
		alert.showAndWait();
	}

	/*
	 * Popup que informa aos jogadores que alguém ganhou e quem ganhou o jogo.
	 */
	private void mostraVencedorDialogo() {
		Label lbl = new Label("GAME OVER!");
		gridTabuleiro.add(lbl, 3, 3);

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Jogo Finalizado");
		alert.setHeaderText(null);
		alert.setContentText(gameManager.getOutroJogador().getNome() + " (" + gameManager.getOutroJogador().getCor() + ")" + " venceu o jogo!");
		alert.initOwner(stage);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.OK) {
			novoJogoEFecharDialogo("Jogo Finalizado", null, "GAME OVER. Você quer começar um novo jogo?", 0);
		}
	}

	/*
	 * Se uma trilha foi definida, um 'X' é exibido nas peças que podem ser removidas.
	 * 
	 * @param corRemovivel
	 */
	private void trocarImagemPedrasRemoviveis(Cor corRemovivel) {
		for (ImageView iv : gridTabuleiroChildren) {
			Location removableLocation = getLocalizacaoPedra(iv);
			if (iv.getId() != null) {
				if (corRemovivel == Cor.PRETA && iv.getId().contains("blk")) {
					if (gameManager.pedraPodeSerRemovida(removableLocation, gameManager.getCorOutroJogador())) {
						iv.setImage(new Image("file:res/textures/black_tile_removable.png"));
					}
				} else if (corRemovivel == Cor.BRANCA && iv.getId().contains("wht")) {
					if (gameManager.pedraPodeSerRemovida(removableLocation, gameManager.getCorOutroJogador())) {
						iv.setImage(new Image("file:res/textures/white_tile_removable.png"));
					}
				}
			}
		}
	}

	/*
	 * Se uma pedra for removida depois que uma trilha definida, a imagem padrão será carregada novamente.
	 * 
	 * @param cor
	 */
	private void rstImagemPedraRemovida(Cor color) {
		for (ImageView iv : gridTabuleiroChildren) {
			if (iv.getId() != null) {
				if (color == Cor.PRETA && iv.getId().contains("blk")) {
					iv.setImage(new Image("file:res/textures/black_tile.png"));
				} else if (color == Cor.BRANCA && iv.getId().contains("wht")) {
					iv.setImage(new Image("file:res/textures/white_tile.png"));
				}
			}
		}
	}

	/*
	 * Se um arrasto for iniciado, uma pedra verde é definida como imagem em todos os locais de lançamento possíveis do tabuleiro do jogo.
	 */
	private void imagenLocaisPermitidos() {
		for (ImageView iv : gridTabuleiroChildren) {
			Location location = getLocalizacaoPedra(iv);
			if (gameManager.getAllowedDropLocations().contains(location) && iv.getId() == null) {
				iv.setImage(new Image("file:res/textures/green_tile.png"));
			}
		}
	}

	/*
	 * Se um lançamento for concluído (com ou sem sucesso), os blocos verdes que indicam os possíveis locais de lançamento serão removidos novamente.
	 */
	private void rstImagenLocaisPermitidos() {
		for (ImageView iv : gridTabuleiroChildren) {
			if (iv.getId() == null) {
				iv.setImage(null);
			}
		}
	}

	/*
	 * Define a cena para seus valores padrão conforme definido na primeira inicialização.
	 */
	private void initWindow() {
		stage.getScene().getStylesheets().clear();
		stage.getScene().getStylesheets().add(GameTrilha.class.getResource("view/RootLayout.fxml").toExternalForm());

		if (tabuleiro.isJogoGanho()) {
			gridTabuleiro.getChildren().remove(24);
		}

		for (ImageView iv : gridTabuleiroChildren) {
			iv.setId(null);
			iv.setImage(null);
		}
	}

	/*
	 * Define os Listeners para as Propriedades {@code Tabuleiro} da classe .
	 */
	private void initTabuleiroPropertyListeners() {
		tabuleiro.novoJogoProperty().addListener((observableValue, oldValue, newValue) -> {
			if (newValue) {
				gameManager.startTrilha();
			}
		});
		tabuleiro.definirTrilhaProperty().addListener((observableValue, oldValue, newValue) -> {
			if (newValue) {
				trocarImagemPedrasRemoviveis(gameManager.getCorOutroJogador());
			} else {
				rstImagemPedraRemovida(gameManager.getCorJogadorAtual());
			}
		});
		tabuleiro.jogoGanhoProperty().addListener((observableValue, oldValue, newValue) -> {
			if (newValue) {
				mostraVencedorDialogo();
			}
		});
		tabuleiro.localizacaoPedraColocadaProperty().addListener((observableValue, oldValue, newValue) -> {
			if (newValue != null) {
				gameManager.colocarPedra(newValue);
			}
		});
	}

	/*
	 * Inicializa a remoção de uma pedra depois de clicar nela sempre que uma trilha foi definido.
	 */
	private void iniciaRemocaoPedraAposTrilha() {
		for (ImageView iv : gridTabuleiroChildren) {
			iv.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> { // MouseEvent
				if (tabuleiro.isDefinirTrilha()) {
					if (iv.getImage() != null && iv.getId() != null) {
						if (iv.getId().contains("blk") && gameManager.getCorJogadorAtual() == Cor.BRANCA
								|| iv.getId().contains("wht") && gameManager.getCorJogadorAtual() == Cor.PRETA) {
							movimentoInvalidoDialogo("Você não pode remover suas próprias pedras!");
						}

						if (iv.getId().contains("blk") && gameManager.getCorJogadorAtual() == Cor.PRETA
								|| iv.getId().contains("wht") && gameManager.getCorJogadorAtual() == Cor.BRANCA || gameManager.fimJogo()) {

							Location location = getLocalizacaoPedra(iv);

							if (gameManager.pedraPodeSerRemovida(location)) {
								iv.setImage(null);
								iv.setId(null);

								gameManager.removerPedra(location);
								tabuleiro.setDefinirTrilha(false);
							} else {
								movimentoInvalidoDialogo("A pedra selecionada não pode ser removido.");
							}
						}
					}
				}
				event.consume();
			});
		}
	}

	/*
	 * Inicializa a funcionalidade de arrastar em um determinado parâmetro {@code GridPane}.
	 * 
	 * @param grid
	 *           ser permitido arrastar de
	 */
	private void iniciaArrastar(GridPane grid) {
		for (Node i : grid.getChildren()) {
			ImageView iv = (ImageView) i;

			iv.setOnDragDetected(event -> { // MouseEvent
				if (tabuleiro.isJogoGanho()) {
					novoJogoEFecharDialogo("Game over", null, "O jogo acabou. Você quer começar um novo jogo?", 0);
					return;
				}
				if (tabuleiro.isDefinirTrilha()) {
					movimentoInvalidoDialogo(gameManager.getOutroJogador().getNome() + " (" + gameManager.getOutroJogador().getColorString() + ")"
							+ " fez uma trilha. Uma pedra de" + gameManager.getJogadorAtual().getNome() + " ("
							+ gameManager.getJogadorAtual().getColorString() + ")" + " tem que ser removida.");
					return;
				}
				if (iv.getImage() == null) {
					return;
				}
				if (gameManager.getFase() != TrilhaFase.COLOCAR && !grid.getId().equals(gridTabuleiro.getId())) {
					return;
				}
				if (gameManager.getJogadorAtual().getCor() == Cor.PRETA && iv.getId().contains("blk")
						|| gameManager.getJogadorAtual().getCor() == Cor.BRANCA && iv.getId().contains("wht")) {
					if (gameManager.getFase() == TrilhaFase.COLOCAR && grid.getId().equals(gridTabuleiro.getId())) {
						movimentoInvalidoDialogo("Todas as pedras devem ser colocadas no tabuleiro do jogo antes de movê-las.");
						return;
					}

					gameManager.setAllowDropLocations(getLocalizacaoPedra(iv));
					if (gameManager.getFase() != TrilhaFase.COLOCAR) {
						imagenLocaisPermitidos();
					}

					Dragboard db = iv.startDragAndDrop(TransferMode.ANY);
					ClipboardContent content = new ClipboardContent();
					content.putImage(iv.getImage());
					content.putString(iv.getId());
					db.setContent(content);
				} else {
					movimentoInvalidoDialogo("É a vez do " + gameManager.getJogadorAtual().getNome() + ". Uma pedra " 
                                                + gameManager.getJogadorAtual().getCor().toString().toLowerCase() + " precisa ser movimentada.");
				}
				event.consume();
			});

			iv.setOnDragDone(event -> { // DragEvent
				if (event.getTransferMode() == TransferMode.MOVE) {
					iv.setImage(null);
					if (grid.getId().equals(gridTabuleiro.getId())) {
						iv.setId(null);
					}
				}
				event.consume();
				rstImagenLocaisPermitidos();
			});
		}
	}

	/*
	 * Inicializa a funcionalidade de soltar em um determinado local {@code GridPane}.
	 * 
	 * @param grid
	 *            ser permitido cair no local selecionado
	 */
	private void iniciarSoltar(GridPane grid) {
		for (Node i : grid.getChildren()) {
			ImageView iv = (ImageView) i;

			iv.setOnDragOver(event -> { // DragEvent
				Dragboard db = event.getDragboard();
				if (event.getGestureSource() != iv && db.hasImage() && db.hasString() && iv.getId() == null) {
					event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
				}
				event.consume();
			});

			iv.setOnDragDropped(event -> { // DragEvent
				Dragboard db = event.getDragboard();
				if (db.hasImage() && db.hasString()) {
					if (iv.getId() == null) {
						Location dropLocation = getLocalizacaoPedra(iv);
						if (gameManager.pedraPodeSerColocada(dropLocation)) {
							iv.setImage(db.getImage());
							iv.setId(db.getString());
							tabuleiro.setLocalizacaoPedraColocada(dropLocation);
							event.setDropCompleted(true);
						} else {
							movimentoInvalidoDialogo("Não é permitido soltar uma pedra neste local.");
						}
					}
				}
				event.consume();
			});
		}
	}

	/*
	 * Inicializa a classe do controlador. Esse método é chamado automaticamente após o carregamento do arquivo fxml.
	 */
	@FXML
	private void initialize() {
                this.textIP.setText("127.0.0.1");
                this.textPorta.setText("54321");
                
		tabuleiro = Tabuleiro.getInstance();

		for (Node i : gridTabuleiro.getChildren()) {
			gridTabuleiroChildren.add((ImageView) i);
		}

		initTabuleiroPropertyListeners();

		iniciaArrastar(gridPedrasEsquerda);
		iniciaArrastar(gridPedrasDireita);
		iniciaArrastar(gridTabuleiro);
		iniciarSoltar(gridTabuleiro);

		iniciaRemocaoPedraAposTrilha();
	}

}
