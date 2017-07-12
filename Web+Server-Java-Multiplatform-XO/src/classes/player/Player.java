package classes.player;

import classes.Server;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by balex on 06.05.2017.
 * Contine informatii legate de jucatori
 */
public class Player {
    private final String USERNAME;
    private PlayerStatus status; // Contine informatii despre starea curenta a jucatorului(daca asteapta in lobby sau este participant activ la sesiune)
    private Integer playerNo; // Este 0 sau 1 in functie de ordinea conectarii in lobby sau sesiune relativ la oponentul sau.
    private Integer sessionId; // Id-ul sesiunii in care se afla jucatorul curent

    ////
    public Player(String USERNAME) {
        this.USERNAME = USERNAME;
        status = null;
        playerNo = null;
        sessionId = null;
    }

    public String getUsername() {
        return USERNAME;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public void setPlayerNo(Integer playerNo) {
        this.playerNo = playerNo;
    }

    public Integer getPlayerNo() {
        return playerNo;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    @Override
    public String toString() {
        return USERNAME;
    }
}
