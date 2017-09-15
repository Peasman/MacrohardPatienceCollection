package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import controller.GameController.InitBattlefieldParameter;
import exceptions.InconsistentMoveException;
import model.Battlefield;
import model.CardStack;
import model.CardStackType;
import model.GameCard;
import model.GameCard.Rank;
import model.GameCard.Suit;
import model.GameMove;
import model.GameMove.SingleCardGameMoveParameter;
import model.GameType;
import model.Player;
import model.PlayerType;

public class GameLogicControllerTest {
	private MPCController mpcController;
	private Battlefield battlefield;

	/**
	 * Erzeugt einen GameLogicController mit dem MPCControllern und dazu ein IDIOT_PATIENCE Battlefield
	 */
	@Before
	public void setUp() throws Exception {
		mpcController = new MPCController();
		CardStack talon = CardStack.fromProtocol("Kreuz-Ass Herz-Ass Pik-Ass Karo-Ass ", false);
		battlefield = mpcController.getGameController().initBattlefield(GameType.IDIOT_PATIENCE,
				new InitBattlefieldParameter(new Player(PlayerType.HUMAN, "Peter"), null, talon, null));
		mpcController.setBattlefield(battlefield);
	}

	/**
	 * Überprüft ob der Zug beendet wird.
	 */
	@Test
	public void testEndCurrentTurn() {
		mpcController = new MPCController();
		Player one = new Player(PlayerType.HUMAN, "Peter"), two = new Player(PlayerType.HUMAN, "Hans");
		battlefield = mpcController.getGameController().initBattlefield(GameType.AGGRO_PATIENCE,
				new InitBattlefieldParameter(one, two, null, null));
		mpcController.setBattlefield(battlefield);
		battlefield.setCurrentPlayer(one);
		assertEquals(mpcController.getBattlefield().getCurrentPlayer().getName(), "Peter");
		try {
			mpcController.getGameLogicController().endCurrentTurn();
		} catch (InconsistentMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(mpcController.getBattlefield().getCurrentPlayer().getName(), "Hans");
	}

	/**
	 * Überprüft ob ein Move vernünftig ausgeführt wird.
	 */
	@Test
	public void testExecuteMoveMove() {
		try {
			mpcController.getGameLogicController()
					.executeMove(new GameMove(new SingleCardGameMoveParameter(CardStackType.TALON, CardStackType.ROW_1,
							mpcController.getBattlefield().getUpperCard(CardStackType.TALON)), null, true));
		} catch (InconsistentMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(mpcController.getBattlefield().getUpperCard(CardStackType.ROW_1).getRank(), Rank.ACE);
		assertEquals(mpcController.getBattlefield().getUpperCard(CardStackType.ROW_1).getSuit(), Suit.CLUBS);
	}

	/**
	 * Überprüft ob ein Move vernünftig validiert wird.
	 */
	@Test
	public void testIsValidMove() {
		assertFalse(mpcController.getGameLogicController()
				.isValid(new GameMove(new SingleCardGameMoveParameter(CardStackType.TALON, CardStackType.STACKER_1,
						battlefield.getUpperCard(CardStackType.TALON)), null, false)));
		GameCard testCard1 = new GameCard(Suit.HEARTS, Rank.TWO, false);
		battlefield.getStack(CardStackType.ROW_1).push(testCard1);
		GameCard testCard2 = new GameCard(Suit.HEARTS, Rank.THREE, false);
		battlefield.getStack(CardStackType.ROW_2).push(testCard2);
		GameCard testCard3 = new GameCard(Suit.SPADES, Rank.TWO, false);
		battlefield.getStack(CardStackType.ROW_3).push(testCard3);
		GameCard testCard4 = new GameCard(Suit.CLUBS, Rank.TWO, false);
		battlefield.getStack(CardStackType.ROW_4).push(testCard4);
		assert (mpcController.getGameLogicController()
				.isValid(new GameMove(new SingleCardGameMoveParameter(CardStackType.ROW_1, CardStackType.STACKER_1,
						battlefield.getUpperCard(CardStackType.ROW_1)), null, false)));
		assertFalse(mpcController.getGameLogicController()
				.isValid(new GameMove(new SingleCardGameMoveParameter(CardStackType.ROW_2, CardStackType.STACKER_1,
						battlefield.getUpperCard(CardStackType.ROW_2)), null, false)));
	}

	/**
	 * Überprüft ob ein gewonnenes Spiel erkannt wird.
	 */
	@Test
	public void testHasWon() {
		assertFalse(GameLogicController.hasWon(battlefield));
		for (int i = 0; i < 48; i++) {
			GameCard testCard = new GameCard(Suit.HEARTS, Rank.TWO, false);
			battlefield.getStack(CardStackType.STACKER_1).push(testCard);
		}
		System.out.println(battlefield.getStack(CardStackType.STACKER_1).size());
		try {
			mpcController.getGameLogicController()
					.executeMove(new GameMove(new SingleCardGameMoveParameter(CardStackType.TALON, CardStackType.ROW_1,
							battlefield.getUpperCard(CardStackType.TALON)), null, false));
			mpcController.getGameLogicController()
					.executeMove(new GameMove(new SingleCardGameMoveParameter(CardStackType.TALON, CardStackType.ROW_2,
							battlefield.getUpperCard(CardStackType.TALON)), null, false));
			mpcController.getGameLogicController()
					.executeMove(new GameMove(new SingleCardGameMoveParameter(CardStackType.TALON, CardStackType.ROW_3,
							battlefield.getUpperCard(CardStackType.TALON)), null, false));
			mpcController.getGameLogicController()
					.executeMove(new GameMove(new SingleCardGameMoveParameter(CardStackType.TALON, CardStackType.ROW_4,
							battlefield.getUpperCard(CardStackType.TALON)), null, false));
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(GameLogicController.hasWon(battlefield));

	}

	/**
	 * Überprüft ob ein verlorenes Spiel erkannt wird.
	 */
	@Test
	public void testHasLost() {
		while (battlefield.getStack(CardStackType.TALON).size() != 0) {
			try {
				mpcController.getGameLogicController()
						.executeMove(new GameMove(new SingleCardGameMoveParameter(CardStackType.TALON,
								CardStackType.ROW_1, battlefield.getUpperCard(CardStackType.TALON)), null, false));
				mpcController.getGameLogicController()
						.executeMove(new GameMove(new SingleCardGameMoveParameter(CardStackType.TALON,
								CardStackType.ROW_2, battlefield.getUpperCard(CardStackType.TALON)), null, false));
				mpcController.getGameLogicController()
						.executeMove(new GameMove(new SingleCardGameMoveParameter(CardStackType.TALON,
								CardStackType.ROW_3, battlefield.getUpperCard(CardStackType.TALON)), null, false));
				mpcController.getGameLogicController()
						.executeMove(new GameMove(new SingleCardGameMoveParameter(CardStackType.TALON,
								CardStackType.ROW_4, battlefield.getUpperCard(CardStackType.TALON)), null, false));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println(battlefield.getStack(CardStackType.TALON).size());
		assert (GameLogicController.hasLost(battlefield));
	}

}
