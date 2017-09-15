package gui.stats;


import java.text.DecimalFormat;
import java.text.NumberFormat;

import controller.MPCController;
import gui.mainview.MainMenuViewController;
import gui.sprites.Textures;
import gui.transitions.TransitionFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.GameStatistics;

public class StatsViewController extends GridPane {

    @FXML
    private Text apm_gamesPlayed;

    @FXML
    private Text apm_cardsLeftAvg;

    @FXML
    private Text apm_gamesWon;

    @FXML
    private Text apm_fastestWin;

    @FXML
    private Text apm_longestTurn;

    @FXML
    private Text aph_gamesPlayed;

    @FXML
    private Text aph_cardsLeftAvg;

    @FXML
    private Text aph_gamesWon;

    @FXML
    private Text aph_fastestWin;

    @FXML
    private Text aph_longestTurn;

    @FXML
    private Text ape_gamesPlayed;

    @FXML
    private Text ape_cardsLeftAvg;

    @FXML
    private Text ape_gamesWon;

    @FXML
    private Text ape_fastestWin;

    @FXML
    private Text ape_longestTurn;
    
    @FXML
    private Text aphu_gamesPlayed;

    @FXML
    private Text aphu_cardsLeftAvg;

    @FXML
    private Text aphu_gamesWon;

    @FXML
    private Text aphu_fastestWin;

    @FXML
    private Text aphu_longestTurn;

    @FXML
    private Text ip_gamesPlayed;

    @FXML
    private Text ip_mostCardsPlayed;

    @FXML
    private Text ip_gamesWon;

    @FXML
    private Text ip_CardsLeftAvg;

    @FXML
    private Text ip_fastestWin;

    @FXML
    private Text fc_gamesPlayed;

    @FXML
    private Text fc_cardsLeftAvg;

    @FXML
    private Text fc_gamesWon;

    @FXML
    private Text fc_fastestWin;

    @FXML
    private Text fc_lowestTurns;
    
    @FXML
    private GridPane statsGrid;
    private MPCController mpcController;
    
    //private final Image texture;
    NumberFormat formatter = new DecimalFormat("#0.00");
    	
    public StatsViewController (MPCController mpcController) {
       	super();
       	this.mpcController = mpcController;
       	
       	
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("stats.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
		getStylesheets().add(this.getClass().getResource("stats.css").toExternalForm());
	    
		
		BackgroundImage bImage = new BackgroundImage(Textures.TABLE_TEXTURE, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
    	Background bground = new Background(bImage);
    	this.setBackground(bground);
	
		//easy aggro patience	
		ape_gamesPlayed.setId("fancy");
		ape_cardsLeftAvg.setId("fancy");
		ape_gamesWon.setId("fancy");
		ape_fastestWin.setId("fancy");
		ape_longestTurn.setId("fancy");
		
		//medium aggro patience
		apm_gamesPlayed.setId("fancy");
		apm_cardsLeftAvg.setId("fancy");
		apm_gamesWon.setId("fancy");
		apm_fastestWin.setId("fancy");
		apm_longestTurn.setId("fancy");
		
		//hard aggro patience
	    aph_gamesPlayed.setId("fancy");
	    aph_cardsLeftAvg.setId("fancy");
	    aph_gamesWon.setId("fancy");
	    aph_fastestWin.setId("fancy");
	    aph_longestTurn.setId("fancy");
	    
	    //against human 
	    aphu_gamesPlayed.setId("fancy");
	    aphu_cardsLeftAvg.setId("fancy");
	    aphu_gamesWon.setId("fancy");
	    aphu_fastestWin.setId("fancy");
	    aphu_longestTurn.setId("fancy");
	    
	    //idiot patience
	    ip_gamesPlayed.setId("fancy");
	    ip_mostCardsPlayed.setId("fancy");
	    ip_gamesWon.setId("fancy");
	    ip_CardsLeftAvg.setId("fancy");
	    ip_fastestWin.setId("fancy");
	    
	    //freecell
	    fc_gamesPlayed.setId("fancy");
	    fc_cardsLeftAvg.setId("fancy");
	    fc_gamesWon.setId("fancy");
	    fc_fastestWin.setId("fancy");
	    fc_lowestTurns.setId("fancy");
	    
	    refresh();
	    
    }
    
    public void refresh() {
    	GameStatistics stats = this.mpcController.getStatisticsController().getStatistics();
		ape_gamesPlayed.setText(getIntStatCheckNull(stats.getAggroPatienceWins()[1]));
	    ape_cardsLeftAvg.setText(getDoubleStatCheckNull(stats.getAggroPatienceCardsLeftAvrg()[1]));
	    ape_gamesWon.setText(getIntStatCheckNull(stats.getAggroPatienceWins()[1]));
	    ape_fastestWin.setText(getLongCheckNull(stats.getAggroPatienceFastestWin()[1])); 
	    ape_longestTurn.setText(getIntStatCheckNull(stats.getAggroPatienceLongestTurn()[1]));
	    
	    apm_gamesPlayed.setText(getIntStatCheckNull(stats.getAggroPatienceWins()[2]));
		apm_cardsLeftAvg.setText(getDoubleStatCheckNull(stats.getAggroPatienceCardsLeftAvrg()[2]));
		apm_gamesWon.setText(getIntStatCheckNull(stats.getAggroPatienceWins()[2]));
	    apm_fastestWin.setText(getLongCheckNull(stats.getAggroPatienceFastestWin()[2]));
	    apm_longestTurn.setText(getIntStatCheckNull(stats.getAggroPatienceLongestTurn()[2]));
	    
	    aph_gamesPlayed.setText(getIntStatCheckNull(stats.getAggroPatienceWins()[3]));
	    aph_cardsLeftAvg.setText(getDoubleStatCheckNull(stats.getAggroPatienceCardsLeftAvrg()[3]));
	    aph_gamesWon.setText(getIntStatCheckNull(stats.getAggroPatienceWins()[3]));
	    aph_fastestWin.setText(getLongCheckNull(stats.getAggroPatienceFastestWin()[3]));
	    aph_longestTurn.setText(getIntStatCheckNull(stats.getAggroPatienceLongestTurn()[3]));
	    
	    aphu_gamesPlayed.setText(getIntStatCheckNull(stats.getAggroPatienceWins()[0]));
	    aphu_cardsLeftAvg.setText(getDoubleStatCheckNull(stats.getAggroPatienceCardsLeftAvrg()[0]));
		aphu_gamesWon.setText(getIntStatCheckNull(stats.getAggroPatienceWins()[0]));
	    aphu_fastestWin.setText(getLongCheckNull(stats.getAggroPatienceFastestWin()[0]));
	    aphu_longestTurn.setText(getIntStatCheckNull(stats.getAggroPatienceLongestTurn()[0]));
	    
	    ip_gamesPlayed.setText(getIntStatCheckNull(stats.getIdiotPatienceGames()));
	    ip_mostCardsPlayed.setText(getIntStatCheckNull(stats.getIdiotPatienceMaxCards()));
	    ip_gamesWon.setText(getIntStatCheckNull(stats.getIdiotPatienceWins()));
	    ip_CardsLeftAvg.setText(getDoubleStatCheckNull(stats.getIdiotPatienceCardsLeftAvrg()));
	    ip_fastestWin.setText(getLongCheckNull(stats.getIdiotPatienceFastestWin()));
    	
	    fc_gamesPlayed.setText(getIntStatCheckNull(stats.getFreecellGames()));
	    fc_cardsLeftAvg.setText(getDoubleStatCheckNull(stats.getFreecellCardsLeftAvrg()));
	    fc_gamesWon.setText(getIntStatCheckNull(stats.getFreecellWins()));
	    fc_fastestWin.setText(getLongCheckNull(stats.getFreecellFastestWin()));
	    fc_lowestTurns.setText(getIntStatCheckNull(stats.getFreecellMinTurns()));
    }
    
    //holt statistik, falls diese noch nicht gesetzt ist, wird "-" angezeigt
    private String getDoubleStatCheckNull (Double stat) {
    	if (stat == null) {
    		return "-";
    	} else {
    		return formatter.format(stat);
    	}
    }

    private String getIntStatCheckNull(Double stat) {
    	if (stat == null) {
    		return "-";
    	
    	} else {
    		return Integer.toString(stat.intValue());
    	}
    }
    private String getLongCheckNull (Long duration) {
    	if (duration == null) {
    		return "-";
    	} else {
    		//return String.format("%.2%f", Long.toString(duration.toMinutes()));
    		int durationAsInt = duration.intValue();
    		return Integer.toString(durationAsInt/60).concat("m ").concat(Integer.toString(durationAsInt % 60)).concat("s");		//toString).concat(str) Long.toString(duration) + "s";
    	}
    }
    @FXML
    void onBackButtonAction(ActionEvent event) {
    	TransitionFactory.animateLayoutSwitch(new MainMenuViewController(mpcController), null, null, false);
    }
    @FXML
    void onResetButtonAction(ActionEvent event) {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Achtung");
    	alert.setHeaderText("Statistiken löschen");
    	alert.setContentText("Statistiken wirklich unwiederruflich löschen?");
    	alert.showAndWait();
    	if(alert.getResult() == ButtonType.OK) {
    		mpcController.getStatisticsController().resetStatistics();
    	}
    	refresh();
    		
    }
}
