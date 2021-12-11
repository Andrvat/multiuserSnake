package app.launcher;

import app.networks.NetworkNode;
import app.model.GameModel;
import app.utilities.DebugPrinter;
import app.utilities.parser.CmdArgsParser;
import proto.SnakesProto;

import java.net.InetAddress;
import java.util.UUID;

public class GameLauncher {
    public static void main(String[] args) {
        CmdArgsParser cmdArgsParser = new CmdArgsParser();
        try {
            cmdArgsParser.parseArguments(args);
            DebugPrinter.printWithSpecifiedDateAndName(GameModel.class.getSimpleName(),
                    "My port: " + cmdArgsParser.getHostPort());
            DebugPrinter.printWithSpecifiedDateAndName(GameModel.class.getSimpleName(),
                    "My address: " + cmdArgsParser.getHostInetAddress());
            GameModel gameModel = new GameModel();
            NetworkNode networkNode = NetworkNode.builder()
                    .nodeId(UUID.randomUUID())
                    .nodeName(cmdArgsParser.getPlayerName())
                    .nodeRole(SnakesProto.NodeRole.NORMAL)
                    .myPort(cmdArgsParser.getHostPort())
                    .gameModel(gameModel)
                    .myInetAddress(InetAddress.getByName(cmdArgsParser.getHostInetAddress()))
                    .build();
            networkNode.startCommunicating();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(0);
        }
    }
}
