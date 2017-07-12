package classes;

import classes.gamesession.GameSessionsList;
import classes.gamesession.Lobby;
import classes.player.Player;
import classes.player.PlayerStatus;

import java.util.*;

/**
 * Created by balex on 06.05.2017.
 * Clasa ce contine informatii privind multimea de sesiuni si jucatorii activi conectati
 */
public class Server {
    private static Server ourInstance = new Server();

    public static Server getInstance() {
        return ourInstance;
    }

    private GameSessionsList gameSessionsList;
    private HashMap<String, Player> playersList;

    ////
    private Server() {
        gameSessionsList = new GameSessionsList();
        playersList = new HashMap<>();

        //TODO
        final Timer POLL_TIMER = new Timer();
        POLL_TIMER.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run(){
                Server.this.printPlayers();
            }
        }, 2000, 2000);
    }

    ////
    // Adauga playerul curent in lobby si completeaza cu datele aferente playerului: playerNo
    public void onConnect(Player player) {
        gameSessionsList.joiningLobby(player);
        playersList.put(player.getUsername(), player);
    }

    public PlayerStatus getPlayerStatus(String playerUsername) {
        return playersList.get(playerUsername).getStatus();
    }

    public int getPlayerSessionId(String playerUsername) {
        return playersList.get(playerUsername).getSessionId();
    }

    public String getPlayerOpponentUsername(int sessionId, String playerUsername){
        return gameSessionsList.getPlayerOpponent(sessionId, playersList.get(playerUsername)).getUsername();
    }

    public int getCurrentTurnInSession(int sessionId) {
        return gameSessionsList.getCurrentTurnInSession(sessionId);
    }

    // METODELE PT JOC
    // Adauga simbolul jucatorului apelant la linia si coloana primata ca parametru in sesiunea cu id-ul primit ca parametru
    public void processSelectedTableCell(int sessionId, int playerNo, int row, int col){
        gameSessionsList.processSelectedTableCell(sessionId, playerNo, row, col);
    }

    public Player getWinningPlayerInSession(int sessionId){
        return gameSessionsList.getWinningPlayerInSession(sessionId);
    }

    public boolean isTieInSession(int sessionId){
        return gameSessionsList.isTieInSession(sessionId);
    }

    public String getTableInSessionJson(int sessionId) {
        return gameSessionsList.getTableInSessionJson(sessionId);
    }

    /*public void onDisconnect(Player player){
        int playerSessionId = player.getSessionId();
        gameSessionsList.notifyGameSessionExpiration(playerSessionId);
        playersList.remove(player.getUsername());
    }*/

    // Metoda ce notifica lobby-ul de prezenta activa a jucatorului
    public void refreshLobbyConnectionCheckFlag(){
        gameSessionsList.refreshLobbyConnectionCheckFlag();
    }

    // Notifica cand jucatorul nu mai participa activ la asteptarea repartizarii sale in sesiune
    public void notifyLobbyExpiration(){
        Player[] players = gameSessionsList.getLobbyPlayers();
        ////////////////////////////
        assert players[1] == null;//
        ////////////////////////////
        if(players[0] != null)
            playersList.remove(players[0].getUsername());
        if(players[1] != null)
            playersList.remove(players[1].getUsername());
        gameSessionsList.notifyLobbyExpiration();

        //TODO
        System.out.println("S-A DECONECTAT LOBBY-UL");
    }

    // Verifica daca lobby-ul curent este deschis sau in starea null
    public Boolean isLobbyAvailable(){
        return gameSessionsList.isLobbyAvailable();
    }

    // Metoda ce notifica sesiunea de prezenta activa a jucatorului
    public void refreshGameSessionConnectionCheckFlag(int playerNo, int sessionId){
        gameSessionsList.refreshGameSessionConnectionCheckFlag(playerNo, sessionId);
    }

    // Notifica cand jucatorul nu mai participa activ la gameplay
    public void notifyGameSessionExpiration(int sessionId){
        Player[] players = gameSessionsList.getGameSessionPlayers(sessionId);
        if(players[0] != null)
            playersList.remove(players[0].getUsername());
        if(players[1] != null)
            playersList.remove(players[1].getUsername());
        gameSessionsList.notifyGameSessionExpiration(sessionId);

        //TODO
        System.out.println("S-A DECONECTAT SESIUNEA " + sessionId);
    }

    // Verifica daca sesiunea cu id-ul primit ca parametru este sau nu disponibila
    public Boolean isGameSessionAvailable(int sessionId){
        return gameSessionsList.isGameSessionAvailable(sessionId);
    }

    //TODO
    @Deprecated
    public void printPlayers(){
        Iterator it = playersList.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
        }
    }

    // Verifica daca jucatorul nu este deja conectat la sesiune/lobby
    public Boolean isPlayerAlreadyConnected(String playerUsername) {
        return playersList.get(playerUsername) != null;
    }
}
