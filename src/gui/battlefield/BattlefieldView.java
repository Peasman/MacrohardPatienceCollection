package gui.battlefield;

import java.util.ArrayList;
import java.util.List;

import application.MpcApplication;
import controller.AIController;
import controller.GameLogicController;
import controller.MPCController;
import exceptions.InconsistentMoveException;
import gui.BattlefieldAUI;
import gui.MoveAUI;
import gui.sprites.CardSprite;
import gui.sprites.CardStackSprite;
import gui.sprites.CardStackSprite.CardStackSpriteBackground;
import gui.sprites.Textures;
import gui.ui.UIOverlay;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import model.Battlefield;
import model.CardStack;
import model.CardStackType;
import model.GameCard;
import model.GameMove;
import model.GameMove.GameMoveParameter;
import model.GameMove.SingleCardGameMoveParameter;
import model.Player;
import model.PlayerType;

/**
 * Die Klasse ist zum Darstellen des Spielfeldes zuständig.
 * 
 * @author Friedemann Runte, Moritz Ludolph
 *
 */
public class BattlefieldView extends AnchorPane implements MoveAUI, BattlefieldAUI {
	private UIOverlay uiLayer;
	private MPCController mpcController;
	private BattlefieldLayout battlefieldLayout;
	protected StackPane overlayPane;

	private boolean cardsBlocked = false;
	
	
	public void setBlocked(boolean blocked){
		cardsBlocked = blocked;
	}
	
	public BattlefieldView(MPCController mpcController) {
		super();

		this.mpcController = mpcController;

		this.setBackground(new Background(new BackgroundImage(Textures.TABLE_TEXTURE, BackgroundRepeat.REPEAT,
				BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

		this.setPickOnBounds(true);
		mpcController.getGameLogicController().addMoveAUI(this);
		mpcController.getGameLogicController().addBattlefieldAUI(this);
	}

	public void setGameLayout() {
		switch (mpcController.getBattlefield().getGameType()) {
		case FREECELL:
			battlefieldLayout = new FreecellLayout(this);
			break;
		case IDIOT_PATIENCE:
			battlefieldLayout = new IdiotLayout(this);
			break;
		case AGGRO_PATIENCE:
			battlefieldLayout = new AggroLayout(this);
			break;
		}

		this.getChildren().add((Node) battlefieldLayout);
		AnchorPane.setTopAnchor((Node) battlefieldLayout, UIOverlay.MENU_BAR_HEIGHT + 30);
		AnchorPane.setRightAnchor((Node) battlefieldLayout, 20.0);
		AnchorPane.setLeftAnchor((Node) battlefieldLayout, 20.0);
	}

	public BattlefieldLayout getBattlefieldLayout() {
		return battlefieldLayout;
	}

	public MPCController getMPCController() {
		return mpcController;
	}

	public Battlefield getBattlefield() {
		return mpcController.getBattlefield();
	}

	private void createUI() {
		uiLayer = new UIOverlay(this);
	}

	private void createCardInteractionEvents() {
		final DragContext dragContext = new DragContext();

		this.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// Verstecken des Menus falls irgendwo anders hingeklickt wurde
				if (uiLayer.getMenu().isVisible()
						&& !uiLayer.getMenu().getLayoutBounds().contains(event.getSceneX(), event.getSceneY()))
					uiLayer.hideMenu();

				if (event.getButton() != MouseButton.PRIMARY)
					return;
				
				if (cardsBlocked)
					return;
				
				if (getBattlefield().getCurrentPlayer().getType() != PlayerType.HUMAN)
					return;
				
				// Überprüfen ob auf eine Karte geklickt wurde
				if (event.getTarget() instanceof CardSprite) {
					dragContext.clear();
					CardSprite targetSprite = (CardSprite) event.getTarget();
					CardStackSprite parent = targetSprite.getParentSprite();
					dragContext.movedCardSpritesList = parent.getFromIncluding(targetSprite);
					
					ArrayList<GameCard> cardsToMove = new ArrayList<GameCard>();
					for (CardSprite cardSprite : dragContext.movedCardSpritesList) {
						cardsToMove.add(cardSprite.getCard());
					}
					GameMove move = new GameMove(new GameMoveParameter(parent.getRole(), null, cardsToMove), getBattlefield().getCurrentPlayer(), false);
					// TODO das sollten wir so nicht machen but hey it works
					switch (mpcController.getBattlefield().getGameType()) {
					case IDIOT_PATIENCE:
						if (parent.getRole() == CardStackType.TALON) {
							dragContext.clear();
							break;
						}
					default:
						if (mpcController.getGameLogicController().isValid(move)) {
							// DragContext initialisieren für mehrere Karten
							dragContext.mouseAnchorX = event.getSceneX() - targetSprite.getTranslateX();
							dragContext.mouseAnchorY = event.getSceneY() - targetSprite.getTranslateY();
							for (CardSprite sprite : dragContext.movedCardSpritesList) {
								sprite.setShadow(true);
							}
						} else {
							dragContext.clear();
						}
					}
				}
			}
		});

		this.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() != MouseButton.PRIMARY)
					return;

				// Bewegungen der Sprites bei einem Drag
				if (event.getTarget() instanceof CardSprite) {
					CardSprite targetSprite = (CardSprite) event.getTarget();
					if (!(dragContext.movedCardSpritesList == null || dragContext.movedCardSpritesList.isEmpty())) {
						if (targetSprite == dragContext.movedCardSpritesList.get(0)) {
							double i = 0;
							for (CardSprite sprite : dragContext.movedCardSpritesList) {

								sprite.setTranslateX(event.getX() - dragContext.mouseAnchorX
										+ sprite.getParentSprite().getCardOffsetX() * i);
								sprite.setTranslateY(event.getY() - dragContext.mouseAnchorY
										+ sprite.getParentSprite().getCardOffsetY() * i);
								i += 1;

							}
						}
					}
				}
			}

		});

		this.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() != MouseButton.PRIMARY)
					return;

				// Diese Fälle treten hoffentlich nicht auf
				if (dragContext.movedCardSpritesList == null || dragContext.movedCardSpritesList.isEmpty())
					return;

				// Kartensprites auf not-visible und not-mangaged setzen sodass
				// das Finden des
				// DropTargets ordentlich funktioniert
				for (CardSprite sprite : dragContext.movedCardSpritesList) {
					sprite.setVisible(false);
					sprite.setManaged(false);
				}

				// Über Koordinaten das DropTarget holen
				Node target = pick(getScene().getRoot(), event.getSceneX(), event.getSceneY());
				CardStackSprite targetSprite;

				// Überprüfen, ob die Karte auf irgendeinen Stack gezogen wurde
				if (target instanceof CardStackSprite) {
					targetSprite = (CardStackSprite) target;
				} else if (target instanceof CardSprite) {
					targetSprite = ((CardSprite) target).getParentSprite();
				} else if (target instanceof CardStackSpriteBackground) {
					targetSprite = ((CardStackSpriteBackground) target).getParentCardStackSprite();
				} else {

					// Falls nein, Sprite wieder anzeigen und den
					// ParentStackSprite erneut zeichnen
					// sodass die Karte zurückgemoved wird

					for (CardSprite sprite : dragContext.movedCardSpritesList) {
						sprite.setVisible(true);
						sprite.setManaged(true);
						sprite.getParentSprite().fill();
					}

					dragContext.clear();
					return;
				}

				ArrayList<GameCard> movedCards = new ArrayList<GameCard>();
				for (CardSprite sprite : dragContext.movedCardSpritesList) {
					sprite.setShadow(false);
					movedCards.add(sprite.getCard());
				}
				GameMove move = new GameMove(new GameMoveParameter(dragContext.movedCardSpritesList.get(0).getParentSprite().getRole(), targetSprite.getRole(), movedCards),
						getBattlefield().getCurrentPlayer(), false, (int) System.currentTimeMillis());

				if (mpcController.getGameLogicController().isValid(move)) {
					try {
						mpcController.getGameLogicController().executeMove(move);
					} catch (InconsistentMoveException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				for (CardSprite sprite : dragContext.movedCardSpritesList) {
					sprite.setVisible(true);
					sprite.setManaged(true);
					sprite.getParentSprite().fill();
				}
				dragContext.clear();

			}
		});

		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				
				if (event.getTarget() instanceof CardSprite) {
					CardSprite targetSprite = (CardSprite) event.getTarget();
					CardStackSprite parent = targetSprite.getParentSprite();

					Battlefield battlefield = mpcController.getBattlefield();
					GameLogicController glc = mpcController.getGameLogicController();

					switch (battlefield.getGameType()) {
					case AGGRO_PATIENCE:
						break;
					case IDIOT_PATIENCE:
						if (event.getButton() == MouseButton.PRIMARY) {
							if (parent.getRole() == CardStackType.TALON) {
								try {
									int turn = (int) System.currentTimeMillis();
									GameMove move;
									move = new GameMove(new SingleCardGameMoveParameter(CardStackType.TALON, CardStackType.ROW_1, battlefield.getUpperCard(CardStackType.TALON)), null,
											true, turn);
									glc.executeMove(move);
									move = new GameMove(new SingleCardGameMoveParameter(CardStackType.TALON, CardStackType.ROW_2, battlefield.getUpperCard(CardStackType.TALON)), null,
											true, turn);
									glc.executeMove(move);
									move = new GameMove(new SingleCardGameMoveParameter(CardStackType.TALON, CardStackType.ROW_3, battlefield.getUpperCard(CardStackType.TALON)), null,
											true, turn);
									glc.executeMove(move);
									move = new GameMove(new SingleCardGameMoveParameter(CardStackType.TALON, CardStackType.ROW_4, battlefield.getUpperCard(CardStackType.TALON)), null,
											true, turn);
									glc.executeMove(move);

								} catch (InconsistentMoveException e) {
									e.printStackTrace();
								}
							}
						} else if(event.getButton() == MouseButton.SECONDARY) {
							GameCard card = targetSprite.getCard();
							CardStack stack = parent.getCardStack();
							if(stack.peek() == card) {
								try {
									GameMove move = new GameMove(new SingleCardGameMoveParameter(parent.getRole(), CardStackType.STACKER_1, card), null, false, (int)System.currentTimeMillis());
									if(glc.isValid(move)) {
										glc.executeMove(move);
									}
								} catch(InconsistentMoveException ime) {
									ime.printStackTrace();
								}
							}
						}
						break;
					case FREECELL:
						if (event.getButton() == MouseButton.SECONDARY) {
							GameCard card = targetSprite.getCard();
							CardStack stack = parent.getCardStack();
							if (stack.peek() == card) {
								CardStackType[] stacker = new CardStackType[] { CardStackType.STACKER_1,
										CardStackType.STACKER_2, CardStackType.STACKER_3, CardStackType.STACKER_4 };
								for (CardStackType type : stacker) {
									try {

										GameMove move = new GameMove(new SingleCardGameMoveParameter(parent.getRole(), type, card), null, false,
												(int)System.currentTimeMillis());
										if (glc.isValid(move)) {
											glc.executeMove(move);
											break;
										}
									} catch (InconsistentMoveException e) {
										e.printStackTrace();
									}
								}
							}
						}
						break;
					}

				}
			}
		});

	}

	private ArrayList<CardSprite> highlightedSprites = new ArrayList<>();

	public void showTip() {
		// Spiel wird nicht mehr in die Statistik aufgenommen
		mpcController.getBattlefield().setGotHelp();

		ArrayList<GameMove> moves = AIController.getAllPossibleMoves(getBattlefield());
		highlightedSprites.clear();

		for (CardStackSprite stackSprite : getBattlefieldLayout().getCardStackSprites().values()) {
			for (CardSprite sprite : stackSprite.getCardSprites()) {
				sprite.setHighlighted(false);
			}
		}

		for (GameMove move : moves) {
			for (CardSprite sprite : getBattlefieldLayout().getCardStackSprites().get(move.getFrom())
					.getSpritesForCards(move.getCards())) {
				if (sprite != null) {
					sprite.setHighlighted(true);
					highlightedSprites.add(sprite);
				}
			}

		}
	}

	@Override
	public void beforeMove(GameMove move, boolean implicitMove) {
		// Move animieren falls nicht von Spieler ausgeführt
		if (move.getPlayer() == null || move.getPlayer().getType() != PlayerType.HUMAN || implicitMove) {
			battlefieldLayout.animateMove(move);
		}
	}

	@Override
	public void afterMove(GameMove move, boolean implicitMove) {
		// Keine Animation falls Move vom Spieler durchgeführt wurde
		battlefieldLayout.getCardStackSprites().get(move.getFrom()).fill();
		if (move.getPlayer() != null && move.getPlayer().getType() == PlayerType.HUMAN && !implicitMove) {
			battlefieldLayout.getCardStackSprites().get(move.getTo()).fill();
		}
		this.playMoveSound(move.getTurn());

		for (CardSprite sprite : highlightedSprites) {
			sprite.setHighlighted(false);
		}
		battlefieldLayout.afterMove();
		highlightedSprites.clear();
	}

	@Override
	public void onTurnBegin(Player playerOfTurn) {
		battlefieldLayout.onTurnBegin();

		switch (mpcController.getBattlefield().getGameType()) {
		case AGGRO_PATIENCE:
			break;
		case FREECELL:
			break;
		case IDIOT_PATIENCE:
			break;
		default:
			break;
		}
	}

	@Override
	public void onTurnEnd(Player playerOfTurn) {
		switch (mpcController.getBattlefield().getGameType()) {
		case AGGRO_PATIENCE:
			break;
		case FREECELL:
			break;
		case IDIOT_PATIENCE:
			break;
		default:
			break;
		}
	}

	// final String IPADDRESS_PATTERN =
	// "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";

	@Override
	public void onGameStart() {
		Battlefield battlefield = mpcController.getBattlefield();
		createCardInteractionEvents();
		setGameLayout();
		createUI();
		
		uiLayer.setGameRunning(true);

		switch (battlefield.getGameType()) {
		case AGGRO_PATIENCE:
			break;
		case FREECELL:
			break;
		case IDIOT_PATIENCE:
			break;
		default:
			break;
		}
	}

	@Override
	public void onGameFinished() {
		uiLayer.setGameRunning(false);
		switch (mpcController.getBattlefield().getGameType()) {
		case AGGRO_PATIENCE:
			if (GameLogicController.hasWon(mpcController.getBattlefield())) {
				overlayPane = battlefieldLayout.performWinAnimation(mpcController);
			} else {
				overlayPane = battlefieldLayout.performLoseAnimation(mpcController);
			}
			mpcController.getServerController().disconnect();
			break;
		case FREECELL:
			// break;
		case IDIOT_PATIENCE:
			// break;
		default:
			if (GameLogicController.hasWon(mpcController.getBattlefield())) {
				overlayPane = battlefieldLayout.performWinAnimation(mpcController);
			} else {
				overlayPane = battlefieldLayout.performLoseAnimation(mpcController);
			}
		}

		MpcApplication.GAME_WRAPPER.getChildren().add(overlayPane);
		StackPane.setAlignment(overlayPane, Pos.CENTER);
	}

	public void destroy() {
		uiLayer.setGameRunning(false);
		mpcController.getGameLogicController().removeMoveAUI(this);
		mpcController.getGameLogicController().removeBattlefieldAUI(this);
	}

	/**
	 * Versucht mittels der X,Y Koordinaten die Node zu finden
	 */
	public static Node pick(Node node, double sceneX, double sceneY) {
		Point2D p = node.sceneToLocal(sceneX, sceneY, true);
		if (!node.contains(p))
			return null;

		if (node instanceof Parent) {
			Node bestMatchingChild = null;
			List<Node> children = ((Parent) node).getChildrenUnmodifiable();
			for (int i = children.size() - 1; i >= 0; i--) {
				Node child = children.get(i);

				p = child.sceneToLocal(sceneX, sceneY, true /* rootScene */);
				if (child.isVisible() && !child.isMouseTransparent() && child.contains(p)) {
					bestMatchingChild = child;
					break;
				}
			}

			if (bestMatchingChild != null) {
				return pick(bestMatchingChild, sceneX, sceneY);
			}
		}

		return node;
	}

	/**
	 * Klasse zur Verwaltung von
	 */
	private class DragContext {
		public double mouseAnchorX, mouseAnchorY;
		public ArrayList<CardSprite> movedCardSpritesList;

		public void clear() {
			mouseAnchorX = 0;
			mouseAnchorY = 0;
			movedCardSpritesList = null;
		}
	}

	@Override
	public void onAIMove(boolean end) {
		if (end)
			cardsBlocked = false;
		else
			cardsBlocked = true;
		
	}

}
