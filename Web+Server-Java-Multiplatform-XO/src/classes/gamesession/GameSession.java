package classes.gamesession;

import classes.Server;
import classes.handlers.database.DatabaseHandler;
import classes.player.Player;
import classes.player.PlayerStatus;
import classes.gamesession.xo.XOSymbol;
import classes.gamesession.xo.XOTable;
import org.jetbrains.annotations.NotNull;

import javax.xml.crypto.Data;
import java.awt.image.DataBuffer;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by balex on 08.05.2017.
 * Clasa ce conține toate informațiile necesare unei sesiuni de joc cu doi jucători
 */
public class GameSession {
    private final int ID;
    private final Player[] players;
    private final XOTable table;
    private int currentTurn;
    private Player winningPlayer;
    //Un flag corespunde fiecărui jucător din sesiune
    private boolean[] connectionCheckFlag;
    private final Timestamp START_DATE;
    private final Timer POLL_TIMER;

    ////
    protected GameSession(int id, Lobby lobby) {
        this.ID = id;
        players = lobby.getWaitingPlayers();
        players[0].setStatus(PlayerStatus.IN_SESSION);
        players[1].setStatus(PlayerStatus.IN_SESSION);
        players[0].setSessionId(this.ID);
        players[1].setSessionId(this.ID);
        table = new XOTable();
        currentTurn = 0;
        winningPlayer = null;
        connectionCheckFlag = new boolean[]{true, true};
        START_DATE = new Timestamp(new Date().getTime());
        POLL_TIMER = new Timer();
        /**
         * Timerul de monitorizare continuă a activității jucătorului;
         * Condiția necesară ca o sesiune să rămână activă este ca
         * ambele flag-uri să fie setate cu "true";
         */
        POLL_TIMER.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //TODO
                System.out.println("S-A EFECTUAL POLL ÎN GameSession");
                if (connectionCheckFlag[0] && connectionCheckFlag[1]) {
                    //TODO
                    System.out.println("SESSIUNEA " + ID + " ESTE ÎNCĂ CONECTATĂ");
                    connectionCheckFlag[0] = false;
                    connectionCheckFlag[1] = false;
                } else {
                    //TODO
                    System.out.println("SESIUNEA " + ID + " ESTE PE CALE DE A FI DECONECTATĂ");
                    Server.getInstance().notifyGameSessionExpiration(id);
                    POLL_TIMER.cancel();
                    POLL_TIMER.purge();
                }
            }
        }, 2000, 2000);
    }

    public Player[] getPlayers() {
        return players;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    //Adaugă simbol în tabla de joc din sesiune la coordonatele primite ca parametru;
    //Dacă în urma mutării jocul se finalizează, este adăugat în baza de date
    public void processSelectedTableCell(int playerNo, int row, int col) {
        XOSymbol currentPlayerSymbol = getSymbolFromPlayerNo(playerNo);
        table.select(currentPlayerSymbol, row, col);

        if (checkTableForTie() || checkTableForWin())
            finish();

        oppositeTurn();
    }

    public Player getWinningPlayer() {
        return winningPlayer;
    }

    public String getTableJson() {
        return table.toJson();
    }

    public void refreshConnectionCheckFlag(int playerNo) {
        connectionCheckFlag[playerNo] = true;
    }

    ////
    //Metoda internă ce returnează numărul jucătorului în funcție de simbolul acestuia după următoarea convenție:
    //0 - X
    //1 - O
    private int getPlayerNoFromSymbol(XOSymbol symbol) {
        if (symbol == XOSymbol.X)
            return 0;
        else
            return 1;
    }

    //Metoda internă ce returnează simbolul jucătorului în funcție de numărul acestuia după următoarea convenție:
    //X - 0
    //O - 1
    @NotNull
    private XOSymbol getSymbolFromPlayerNo(int playerNo) {
        if (playerNo == 0)
            return XOSymbol.X;
        else
            return XOSymbol.O;
    }

    //Schimbă rândul curent al jucătorului în sesiune: dacă e 0 devine 1, dacă e 1 devine 0
    private void oppositeTurn() {
        currentTurn = (getCurrentTurn() + 1) % 2;
    }

    //Verifică dacă e remiză în sesiunea curentă
    public boolean checkTableForTie() {
        return table.isTie();
    }

    //Returnează true dacă există câștigător în sesiune, false altfel;
    //De asemenea, stochează în atributul winningPlayer numele jucătorului câștigător (dacă este cazul)
    private boolean checkTableForWin() {
        XOSymbol winningSymbol = table.checkForWin();
        if (winningSymbol != null) {
            Integer winningPlayerNo = getPlayerNoFromSymbol(winningSymbol);
            winningPlayer = players[winningPlayerNo];
            return true;
        }
        return false;
    }

    private void finish() {
        DatabaseHandler dh = DatabaseHandler.getInstance();
        if (winningPlayer == null) {
            dh.insertMatchInDatabase(players[getPlayerNoFromSymbol(XOSymbol.X)].getUsername(), players[getPlayerNoFromSymbol(XOSymbol.O)].getUsername(), null, START_DATE, new Timestamp(new Date().getTime()), getTableJson());
        } else {
            dh.insertMatchInDatabase(players[getPlayerNoFromSymbol(XOSymbol.X)].getUsername(), players[getPlayerNoFromSymbol(XOSymbol.O)].getUsername(), winningPlayer.getUsername(), START_DATE, new Timestamp(new Date().getTime()), getTableJson());
            dh.incrementGamesWon(winningPlayer.getUsername());
            dh.incrementGamesLost(getPlayerOpponent(winningPlayer).getUsername());
        }
        dh.incrementGamesPlayed(players[0].getUsername());
        dh.incrementGamesPlayed(players[1].getUsername());
    }

    public Player getPlayerOpponent(Player player) {
        assert player != null;

        return player == players[0] ? players[1] : players[0];
    }

    public void purgePollTimer() {
        POLL_TIMER.cancel();
        POLL_TIMER.purge();
    }
}
