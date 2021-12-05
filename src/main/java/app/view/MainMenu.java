package app.view;

import app.model.GameModel;
import app.controller.GameController;
import app.utilities.CommunicationMessage;
import proto.SnakesProto;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class MainMenu extends JPanel {
    JPanel box = new JPanel();
    JPanel ann = new JPanel();
    JPanel score = new JPanel();
    GameController gameController;
    GameModel gameModel;
    JButton buttonNewGame = new JButton("Новая игра");
    JButton updateAnn = new JButton("Обновить");

    public MainMenu(GameController gameController, GameModel gameModel){
        this.gameModel = gameModel;
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        ann.setLayout(new BoxLayout(ann, BoxLayout.Y_AXIS));
        score.setLayout(new BoxLayout(score, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(800,800));

        this.gameController = gameController;
        buttonNewGame.addActionListener((e)-> new SettingsMenu(gameController));
        updateAnn.addActionListener((e)-> gameController.nModelSub());
        add(box);
        box.add(buttonNewGame);
        box.add(updateAnn);
        box.add(score);
        box.add(ann);
    }

    public void printScore(){
        char d = ' ';
        score.removeAll();
        for (SnakesProto.GamePlayer player: gameModel.getGameState().getPlayers().getPlayersList()) {
            if (gameModel.getSnakesAllCoordinatesByPlayer().containsKey(player.getId())) {
                if (player.getId() == gameModel.getSessionMasterId()) d = '*';
                score.add(new JLabel("playerId : [" + player.getId() + "]          score : " + gameModel.getSnakesAllCoordinatesByPlayer().get(player.getId()).size() + d));
            }
            else score.add(new JLabel("playerId : [" + player.getId() + "]          score : 0 VIEWER"));

        }
        validate();
    }

    public void printAnn(ConcurrentHashMap<CommunicationMessage, Instant> announcement) {
        ann.removeAll();
        for (Map.Entry<CommunicationMessage, Instant> entry : announcement.entrySet()) {
            JPanel panel = new JPanel(new BorderLayout());
            JButton button = new JButton("Войти");
            JButton button1 = new JButton("Выйти");
            button1.addActionListener((e)-> gameController.exit());
            button.addActionListener((e)-> gameController.join(entry.getKey().getFrom(),
                    entry.getKey().getGameMessage().getAnnouncement().getConfig()));
            JLabel label = new JLabel(Objects.requireNonNull(GameModel.getMasterPlayer(entry.getKey().getGameMessage().getAnnouncement().getPlayers())).getName()
                    + "    [" +entry.getKey().getFrom().getIpAddress() +"]    "
                    +entry.getKey().getGameMessage().getAnnouncement().getPlayers().getPlayersCount()
                    +"     "+ entry.getKey().getGameMessage().getAnnouncement().getConfig().getWidth() +"x"+entry.getKey().getGameMessage().getAnnouncement().getConfig().getHeight()
                    +"     " +entry.getKey().getGameMessage().getAnnouncement().getConfig().getFoodStatic() + " + "
                    + entry.getKey().getGameMessage().getAnnouncement().getConfig().getFoodPerPlayer()+ "x");

            panel.add(label, BorderLayout.WEST);
            if (entry.getKey().getFrom().getId() == gameModel.getSessionMasterId() && gameController.checkAlive()) panel.add(button1, BorderLayout.EAST);
            else
                panel.add(button, BorderLayout.EAST);
            ann.add(panel);
            buttonNewGame.setSize(100, 50);
            validate();
        }
    }
}