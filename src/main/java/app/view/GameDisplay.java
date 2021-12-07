package app.view;

import app.model.GameModel;
import app.controller.GameController;
import app.networks.CommunicationMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

public class GameDisplay extends JFrame {
    private static final String GAME_NAME = "ONLINE SNAKE";

    private final GameField gameField;
    private final MainMenu mainMenu;

    public GameDisplay(int screenWidth, int screenHeight,
                       int gameFieldWidth, int gameFieldHeight,
                       GameController gameController, GameModel gameModel, int ownerNodeId) {
        this.setSize(new Dimension(screenWidth, screenHeight));
        this.setLayout(new BorderLayout());
        this.setTitle(GAME_NAME);
        this.setScreenListenerForCloseOperation();
        this.setResizable(false);
        gameField = new GameField(screenWidth / 2, gameModel, gameController, ownerNodeId);
        mainMenu = new MainMenu(gameController, gameModel);
        add(gameField, BorderLayout.WEST);
        add(mainMenu, BorderLayout.EAST);

        setVisible(true);
    }

    private void setScreenListenerForCloseOperation() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        JFrame thisFrame = this;
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int chosenOption = JOptionPane.showConfirmDialog(thisFrame,
                        "Are you sure?",
                        "Exit",
                        JOptionPane.YES_NO_OPTION);
                if (isYesChosen(chosenOption)) {
                    System.exit(0);
                }
            }

            private boolean isYesChosen(int index) {
                return index == 0;
            }
        });
    }

    public void updateDisplay() {
        gameField.setFocusable(true);
        gameField.requestFocusInWindow();
        mainMenu.printScore();
        gameField.repaint();
    }

    public void updateGames(ConcurrentHashMap<CommunicationMessage, Instant> ann) {
        mainMenu.printAnn(ann);
    }
}
