package classes.gamesession;

import classes.Server;
import classes.player.Player;
import classes.player.PlayerStatus;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by balex on 09.05.2017.
 *  Clasa ce conține toate informațiile necesare unei sesiuni de joc cu doi jucători, asemanatoare cu clasa GameSession,
 *  dar incompleta din punct de vedere al repartizarii jucatorilor(o stare intermediara, in care sunt repartizati jucatorii inainte
 *  de a fi introdusi in sesiune).
 */
public class Lobby {
    private Player[] waitingPlayers;
    private boolean connectionCheckFlag;
    private final Timer POLL_TIMER;
    ////
    public Lobby() {
        waitingPlayers = new Player[2];
        connectionCheckFlag = true;
        POLL_TIMER = new Timer();
        POLL_TIMER.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("S-A EFECTUAT POLL ÎN Lobby");
                if (connectionCheckFlag) {
                    System.out.println("LOBBY-UL ESTE ÎNCĂ CONECTAT");
                    connectionCheckFlag = false;
                }
                else {
                    System.out.println("LOBBY-UL ESTE PE CALE DE A FI DECONECTAT");
                    Server.getInstance().notifyLobbyExpiration();
                    POLL_TIMER.cancel();
                    POLL_TIMER.purge();
                }
            }
        }, 2000, 2000);
    }

    // Metoda ce repartizeaza jucatorul curent in lobby-ul actual.
    public void joining(Player player){
        player.setStatus(PlayerStatus.IN_LOBBY);

        if(waitingPlayers[0] == null) {
            waitingPlayers[0] = player;
            player.setPlayerNo(0);
        }
        else {
            waitingPlayers[1] = player;
            player.setPlayerNo(1);
        }
    }

    //Verifica daca este atins numarul maxim de jucatori in lobby(2)
    public boolean isReady(){
        return waitingPlayers[0] != null && waitingPlayers[1] != null;
    }

    public Player[] getWaitingPlayers(){
        return waitingPlayers;
    }

    public void refreshConnectionCheckFlag() {
        connectionCheckFlag = true;
    }

    public void purgePollTimer(){
        POLL_TIMER.cancel();
        POLL_TIMER.purge();
    }
}
