package classes.gamesession;

import classes.Server;
import classes.player.Player;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by balex on 10.05.2017.
 * Reține un array de GameSession-uri și un Lobby
 */
public class GameSessionsList {
    private GameSession[] sessions;
    private Lobby lobby;

    ////
    public GameSessionsList() {
        sessions = new GameSession[4]; // TODO: 10.05.2017 PUNE MAI MULȚI PLAYERI
        lobby = null;

        //TODO
        final Timer POLL_TIMER = new Timer();
        POLL_TIMER.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run(){
                GameSessionsList.this.printGameSessionStats();
            }
        }, 2000, 2000);
    }

    //Se apelează când un jucător a dat click pe "Enter Lobby";
    //În cazul în care jucătorul curent este singur, se instanțiază un nou lobby
    //și rămâne în așteptarea unui oponent;
    //Dacă jucătorul curent este al doilea conectat în lobby, se apelează metoda
    //"connectLobby" care crează o sesiune nouă cu cei doi jucători
    public void joiningLobby(Player player) {
        assert !isFull();

        if (lobby == null)
            lobby = new Lobby();

        lobby.joining(player);
        if (lobby.isReady())
            connectLobby();
    }

    //Verifică dacă lista de sesiuni a atins numărul maxim de sesiuni
    public boolean isFull() {
        boolean b = true;
        for (int i = 0; i < sessions.length; i++)
            b = b && sessions[i] != null;
        return b;
    }

    public int getCurrentTurnInSession(int sessionId) {
        return sessions[sessionId].getCurrentTurn();
    }

    public void processSelectedTableCell(int sessionId, int playerNo, int row, int col) {
        sessions[sessionId].processSelectedTableCell(playerNo, row, col);
    }

    public Player getWinningPlayerInSession(int sessionId) {
        return sessions[sessionId].getWinningPlayer();
    }

    public boolean isTieInSession(int sessionId) {
        return sessions[sessionId].checkTableForTie();
    }

    public String getTableInSessionJson(int sessionId) {
        return sessions[sessionId].getTableJson();
    }

    public Player getPlayerOpponent(int sessionId, Player player){
        return sessions[sessionId].getPlayerOpponent(player);
    }

    ////
    //Creează o nouă sesiune bazată pe lobby-ul curent și readuce lobby-ul curent in starea null
    private void connectLobby() {
        assert !isFull();

        int newSessionId = getFreeSessionSlotIndex();
        sessions[newSessionId] = new GameSession(newSessionId, lobby);
        lobby.purgePollTimer();
        lobby = null;
    }

    //Parcurge array-ul de sesiuni si indexul unei sesiuni disponibile
    private int getFreeSessionSlotIndex() {
        for (int i = 0; i < sessions.length; i++)
            if (sessions[i] == null)
                return i;
        return -1;
    }

    public void notifyLobbyExpiration() {
        lobby = null;
    }

    public boolean isLobbyAvailable() {
        return lobby != null;
    }

    public void refreshLobbyConnectionCheckFlag() {
        lobby.refreshConnectionCheckFlag();
    }

    //Cand se observa incactivitate dupa un anumit timp, din partea jucatorilor, sesiunea curenta devine disponibila
    public void notifyGameSessionExpiration(int sessionId) {
        sessions[sessionId].purgePollTimer();
        sessions[sessionId] = null;
    }

    public boolean isGameSessionAvailable(int sessionId) {
        return sessions[sessionId] != null;
    }

    public void refreshGameSessionConnectionCheckFlag(int playerNo, int sessionId) {
        sessions[sessionId].refreshConnectionCheckFlag(playerNo);
    }

    //TODO
    @Deprecated
    public void printGameSessionStats() {
        System.out.println();
        System.out.println("Lobby: " + isLobbyAvailable());
        for (int i = 0; i < sessions.length; i++)
            System.out.println("GameSession " + i + ": " + isGameSessionAvailable(i));
        System.out.println();
    }

    public Player[] getLobbyPlayers(){
        return lobby.getWaitingPlayers();
    }

    //Returneaza jucatorii sesiunii cu id-ul primit ca parametru
    public Player[] getGameSessionPlayers(int sessionId){
        return sessions[sessionId].getPlayers();
    }
}
