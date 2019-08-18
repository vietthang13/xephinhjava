/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import JDBC.ScoreBean;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author DELL
 */
public class Tetris extends Application {
	private static Label statusbar;
	private static ObservableList<ScoreBean> scoreList = null;
;
	public static final int sWidth = 200;
	public static final int sHeight = 400;

	public Label getStatusbar() {
		return statusbar;
	}
	
	public void reSetStatusbar(String status) {
		statusbar.setText(status);
	}
	
	private double time;
	private GraphicsContext g;
	private Board board;
	static String saveName;
	private Pane root;
	private Canvas canvas;
	
	public static String getSaveName() {
		return saveName;
	}

	private Parent createContent() {
		root = new Pane();
		root.setPrefSize(Tetris.sWidth, Tetris.sHeight);
		board = new Board(this);

		canvas = new Canvas(Tetris.sWidth, Tetris.sHeight);
		g = canvas.getGraphicsContext2D();
		board.repaint(g);
		root.getChildren().addAll(canvas);

		board.animationTimer();

		board.start();
		return root;
	}

	private TableView<ScoreBean> addScoreValue(TableView<ScoreBean> tablescore)
			throws ClassNotFoundException, SQLException {
		scoreList.sort((a, b) -> a.getScore() - (b.getScore()));

		tablescore.setItems(scoreList);
		return tablescore;
	}

	private void stageRank() throws ClassNotFoundException, SQLException {
		Stage rankStage = new Stage();
		scoreList = Board.getScore();

		TableView<ScoreBean> tablescore = new TableView<ScoreBean>();

		rankStage.setTitle("Rank");
		rankStage.setWidth(450);
		rankStage.setHeight(500);

		final Label label = new Label("Rank");
		label.setFont(new Font("Arial", 20));

		tablescore.setEditable(false);
		
		// call function add value to tableView
		// if score list not null then add to tableView
		if (null != scoreList) {
			tablescore = addScoreValue(tablescore);
		}
		
		TableColumn<ScoreBean, Integer> idCol = new TableColumn<ScoreBean, Integer>("Id");
		idCol.setMinWidth(100);

		TableColumn<ScoreBean, String> nameCol = new TableColumn<ScoreBean, String>("Name");
		nameCol.setMinWidth(100);
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<ScoreBean, Integer> scoreCol = new TableColumn<ScoreBean, Integer>("Score");
		scoreCol.setMinWidth(200);
		scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));

		tablescore.getColumns().addAll(Arrays.asList(idCol, nameCol, scoreCol));

		final VBox vbox = new VBox();
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10, 0, 0, 10));
		vbox.getChildren().addAll(label, tablescore);

		StackPane root = new StackPane();
		Scene scene = new Scene(root, 450, 500);
		root.getChildren().addAll(vbox);

		rankStage.setScene(scene);
		rankStage.show();
		scene.getRoot().requestFocus();
	}

		private void stagePlay() throws ClassNotFoundException, SQLException {
		Stage primaryStage = new Stage();

		statusbar = new Label("0");
		statusbar.setAlignment(Pos.BOTTOM_LEFT);
		statusbar.setVisible(true);

		BorderPane pane = new BorderPane(createContent(), null, null, statusbar, null);

		Scene scene = new Scene(pane);
		scene.setOnKeyPressed(e2 -> {
			board.keyPressed(e2);

		});

		primaryStage.setTitle("Tetris");
		primaryStage.setScene(scene);
		primaryStage.show();

		scene.getRoot().requestFocus();
	}
	
	public void returnMain(Stage stage) throws Exception {
//		stage.close();
//		Stage stageMain = new Stage();
//		stageMain(stageMain);
		this.stop();
		this.start(stage);
	}

	public void stageMain(Stage Stage) {
		// Scene 1
//				StackPane p1 = new StackPane();
//				p1.setId("root");
				// button
				VBox vButton = new VBox();
				vButton.setPrefWidth(120);
				vButton.setAlignment(Pos.CENTER);
				vButton.setSpacing(10);

				Button btnPlay = new Button("Play");
				btnPlay.setId("button");
				btnPlay.setPadding(new Insets(8, 8, 8, 8));
				btnPlay.setMinWidth(vButton.getPrefWidth());

				Button btnRank = new Button("Rank");
				btnPlay.setId("button");
				btnPlay.setPadding(new Insets(8, 8, 8, 8));
				btnPlay.setMinWidth(vButton.getPrefWidth());

				Button btnQuit = new Button("Quit");
				btnQuit.setId("button");
				btnQuit.setPadding(new Insets(8, 8, 8, 8));
				btnQuit.setMinWidth(vButton.getPrefWidth());

				vButton.getChildren().addAll(btnPlay, btnRank, btnQuit);

//				p1.getChildren().add(vButton);
			

				// Scene 2

				btnPlay.setOnAction(e -> {
					Stage.close();
					// tạo stage mới và mở màn hình game.
					try {
						stagePlay();
					} catch (ClassNotFoundException | SQLException e1) {
						e1.printStackTrace();
					}
				});

				btnRank.setOnAction(e -> {
					Stage.close();
					// tạo stage mới và mở màn hình Rank
					try {
						stageRank();
					} catch (ClassNotFoundException | SQLException e1) {
						e1.printStackTrace();
					}
				});

				btnQuit.setOnAction(eq -> {
					Stage.close();
				});

				Scene scene1 = new Scene(vButton, 600, 600);
				scene1.getStylesheets().addAll(this.getClass().getResource("tetriscss.css").toExternalForm());
				Stage.setTitle("Tetris");
				Stage.setScene(scene1);
				Stage.show();
	}
	@Override
	public void start(Stage Stage) throws Exception {
		stageMain(Stage);
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);

	}

}
