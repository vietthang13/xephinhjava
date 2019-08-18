/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import com.sun.javafx.scene.layout.region.Margins.Converter;

import JDBC.ScoreBean;
import JDBC.ScoreDAO;
import application.Shape.Tetrominoes;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Dimension2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public final class Board {
	private final int BoardWidth = 10;
	private final int BoardHeight = 22;

	private double time;

	private AnimationTimer timer;
	private boolean isFallingFinished = false;
	private boolean isStarted = false;
	private boolean isPaused = false;
	private int numLinesRemoved = 0;
	private int curX = 0;
	private int curY = 0;
	public static Label statusbar;
	private int statusScore;
	private Shape curPiece;
	private Tetrominoes[] board;
	private GraphicsContext g;
	private static String name;
	private Tetris parent;
	public static boolean isreturn = false;

	// public Canvas canvas;
	public Board(Tetris parent) {
		this.parent = parent;
		initBoard(parent);

	}

	public void initBoard(Tetris parent) {

		curPiece = new Shape();

		statusbar = parent.getStatusbar();
		board = new Tetrominoes[BoardWidth * BoardHeight];
		clearBoard();

	}

	public void reStart() {
		parent.reSetStatusbar("0");
		initBoard(parent);
		repaint(g);

		animationTimer();

		start();
	}

	public void animationTimer() {
		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				time += 0.017;

				if (time >= 0.5) {
					if (isFallingFinished) {
						isFallingFinished = false;
						newPiece();
					} else {
						oneLineDown();
					}
					time = 0;
				}
			}
		};
		timer.start();
	}

	public int squareWidth() {
		return (int) Tetris.sWidth / BoardWidth;
	}

	public int squareHeight() {
		return (int) Tetris.sHeight / BoardHeight;
	}

	public Tetrominoes shapeAt(int x, int y) {
		return board[(y * BoardWidth) + x];
	}

	public void start() {

		if (isPaused)
			return;

		isStarted = true;
		isFallingFinished = false;
		numLinesRemoved = 0;
		clearBoard();

		newPiece();
		// timer.start();

	}

	public void pause() {

		if (!isStarted)
			return;

		isPaused = !isPaused;

		if (isPaused) {

			timer.stop();
			statusbar.setText("paused");
		} else {

			timer.start();
			statusbar.setText(String.valueOf(numLinesRemoved));
		}

		repaint(g);
	}

	public void doDrawing(GraphicsContext g) {

		Dimension2D size = new Dimension2D(Tetris.sWidth, Tetris.sHeight);
		int boardTop = (int) size.getHeight() - BoardHeight * squareHeight();

		for (int i = 0; i < BoardHeight; ++i) {

			for (int j = 0; j < BoardWidth; ++j) {

				Tetrominoes shape = shapeAt(j, BoardHeight - i - 1);

				if (shape != Tetrominoes.NoShape)
					drawSquare(g, 0 + j * squareWidth(), boardTop + i * squareHeight(), shape);
			}
		}

		if (curPiece.getShape() != Tetrominoes.NoShape) {

			for (int i = 0; i < 4; ++i) {
				int x = curX + curPiece.x(i);
				int y = curY - curPiece.y(i);
				drawSquare(g, 0 + x * squareWidth(), boardTop + (BoardHeight - y - 1) * squareHeight(),
						curPiece.getShape());
			}
		}
	}

	public void repaint(GraphicsContext g) {
		this.g = g;
		g.clearRect(0, 0, Tetris.sWidth, Tetris.sHeight);

		doDrawing(g);
	}

	public void dropDown() {
		int newY = curY;
		while (newY > 0) {

			if (!tryMove(curPiece, curX, newY - 1))
				break;
			--newY;
		}

		pieceDropped();
	}

	public void oneLineDown() {

		if (!tryMove(curPiece, curX, curY - 1))
			pieceDropped();
	}

	public void clearBoard() {

		for (int i = 0; i < BoardHeight * BoardWidth; ++i)
			board[i] = Tetrominoes.NoShape;
	}

	public void pieceDropped() {

		for (int i = 0; i < 4; ++i) {

			int x = curX + curPiece.x(i);
			int y = curY - curPiece.y(i);
			board[(y * BoardWidth) + x] = curPiece.getShape();
		}

		removeFullLines();

		if (!isFallingFinished)
			newPiece();
	}

	public void newPiece() {

		curPiece.setRandomShape();
		curX = BoardWidth / 2 + 1;
		curY = BoardHeight - 1 + curPiece.minY();

		if (!tryMove(curPiece, curX, curY)) {

			curPiece.setShape(Tetrominoes.NoShape);
			timer.stop();
			isStarted = false;
			this.statusScore = Integer.parseInt(statusbar.getText());
			statusbar.setText("game over");

			saveScore();

		}
	}

	public void exitDialog() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(null);
		alert.setHeaderText(null);
		alert.setContentText("Good game. You won! Click OK to exit.");

		alert.setOnHidden(evt -> Platform.exit());

		alert.show();
	}

	private void saveScore() {
		TextInputDialog dialog = new TextInputDialog("Name");
		dialog.setTitle("Save");
		dialog.setHeaderText("Score: " + statusScore);
		dialog.setContentText("Name: ");
		final Button ok = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
		ok.addEventFilter(ActionEvent.ACTION, event -> {
			exitDialog();
		});

		final Button cancel = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
		cancel.addEventFilter(ActionEvent.ACTION, event -> {
			reStart();
		});

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Optional<String> result = dialog.showAndWait();
				result.ifPresent(statusName -> {
					name = statusName;
				});
				if (result.isPresent()) {
					try {
						setScore(name, statusScore);
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	public boolean tryMove(Shape newPiece, int newX, int newY) {

		for (int i = 0; i < 4; ++i) {

			int x = newX + newPiece.x(i);
			int y = newY - newPiece.y(i);

			if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight)
				return false;

			if (shapeAt(x, y) != Tetrominoes.NoShape)
				return false;
		}

		curPiece = newPiece;
		curX = newX;
		curY = newY;

		repaint(g);

		return true;
	}

	public void removeFullLines() {

		int numFullLines = 0;

		for (int i = BoardHeight - 1; i >= 0; --i) {
			boolean lineIsFull = true;

			for (int j = 0; j < BoardWidth; ++j) {
				if (shapeAt(j, i) == Tetrominoes.NoShape) {
					lineIsFull = false;
					break;
				}
			}

			if (lineIsFull) {
				++numFullLines;
				for (int k = i; k < BoardHeight - 1; ++k) {
					for (int j = 0; j < BoardWidth; ++j)
						board[(k * BoardWidth) + j] = shapeAt(j, k + 1);
				}
			}
		}
		if (numFullLines > 0) {

			numLinesRemoved += numFullLines;
			statusbar.setText(String.valueOf(numLinesRemoved));
			isFallingFinished = true;
			curPiece.setShape(Tetrominoes.NoShape);
			repaint(g);
		}
	}

	public void drawSquare(GraphicsContext g, int x, int y, Tetrominoes shape) {

		Color colors[] = { Color.rgb(0, 0, 0), Color.rgb(204, 102, 102), Color.rgb(102, 204, 102),
				Color.rgb(102, 102, 204), Color.rgb(204, 204, 102), Color.rgb(204, 102, 204), Color.rgb(102, 204, 204),
				Color.rgb(218, 170, 0) };

		Color color = colors[shape.ordinal()];

		g.setFill(color);
		g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

		g.setStroke(color.brighter());

	}

	public void keyPressed(KeyEvent e) {
		if (!isStarted || curPiece.getShape() == Tetrominoes.NoShape) {
			return;
		}

		KeyCode code;
		code = e.getCode();
		if (code == KeyCode.P) {
			pause();
			return;
		}

		if (isPaused)
			return;

		if (null != code)
			switch (code) {
			case SPACE:
				dropDown();
				break;
			case LEFT:
				tryMove(curPiece, curX - 1, curY);
				break;

			case RIGHT:
				tryMove(curPiece, curX + 1, curY);
				break;

			case DOWN:
				tryMove(curPiece.rotateRight(), curX, curY);
				break;

			case UP:
				tryMove(curPiece.rotateLeft(), curX, curY);
				break;

			case D:
				oneLineDown();
				break;
			default:
				break;
			}
	}

	public int getStatusScore() {
		return statusScore;
	}

	public void setStatusScore(int statusScore) {
		this.statusScore = statusScore;
	}

	public static void setScore(String name, int score) throws ClassNotFoundException, SQLException {
		ScoreDAO.insertScore(name, score);

	}

	public static ObservableList<ScoreBean> getScore() throws ClassNotFoundException, SQLException {
		ObservableList<ScoreBean> scoreList = FXCollections.observableArrayList(ScoreDAO.selectScore());
		return scoreList;
	}
}
