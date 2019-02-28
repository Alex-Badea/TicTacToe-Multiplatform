package player;

/**
 * Created by balex on 16.05.2017.
 */
public class CurrentPlayer {
    private static CurrentPlayer ourInstance = new CurrentPlayer();

    public static CurrentPlayer getInstance() {
        return ourInstance;
    }

    private String username;
    private PlayerStatus status;
    private Integer playerNo;
    private Integer sessionId;
    private String opponentUsername;


    ////
    private CurrentPlayer() {}
    ////

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getPlayerNo() {
        return playerNo;
    }

    public void setPlayerNo(Integer playerNo) {
        this.playerNo = playerNo;
    }

    public void invalidateSession(){
        username = null;
        playerNo = null;
        sessionId = null;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    public String getOpponentUsername() {
        return opponentUsername;
    }

    public void setOpponentUsername(String opponentUsername) {
        this.opponentUsername = opponentUsername;
    }
}
