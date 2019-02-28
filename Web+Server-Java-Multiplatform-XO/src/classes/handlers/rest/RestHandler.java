package classes.handlers.rest;

import classes.handlers.database.DatabaseHandler;
import classes.player.Player;
import classes.player.PlayerStatus;
import classes.Server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.xml.crypto.Data;

/**
 * Created by balex on 06.05.2017.
*/

@Path("/")
public class RestHandler {
    private Server server = Server.getInstance();
    ////

    @POST
    @Path("/login/")
    public Response login(@Context HttpServletRequest request, @FormParam("username") String username, @FormParam("password") String password) {
        HttpSession session = request.getSession();

        System.out.println(username + "," + password);

        if (DatabaseHandler.getInstance().checkUsernameAndPasswordMatch(username, password)) {
            session.setAttribute("playerUsername", username);
            return Response.status(Response.Status.OK).entity("TRUE").build();
        } else {
            //TODO probabil statusul trebuie modificat la UNAUTHORIZED
            return Response.status(Response.Status.OK).entity("FALSE").build();
        }
    }

    @POST
    @Path("/logout/")
    public Response logout(@Context HttpServletRequest request) {
        HttpSession session = request.getSession();

        session.invalidate();
        return Response.status(Response.Status.OK).entity("Logout succes!").build();
    }

    @POST
    @Path("/createAccount/")
    public Response createAccount(@Context HttpServletRequest request, @FormParam("username") String username, @FormParam("password") String password) {
        if (DatabaseHandler.getInstance().addNewUsernameAndPassword(username, password)) {
            return Response.status(Response.Status.OK).entity("TRUE").build();
        } else {
            //TODO probabil statusul trebuie modificat la UNAUTHORIZED
            return Response.status(Response.Status.OK).entity("FALSE").build();
        }
    }

    //Aici i-a fost atribuit un slot jucătorului curent în lista de jucători
    @POST
    @Path("/onConnect/")
    public Response onConnect(@Context HttpServletRequest request) {
        HttpSession session = request.getSession();
        assert session.getAttribute("playerUsername") != null;

        String playerUsername = (String) session.getAttribute("playerUsername");
        Player newPlayer = new Player(playerUsername);
        //onConnect completează datele aferente jucătorului
        server.onConnect(newPlayer);

        //ulterior, după completarea datelor noului jucător de către server, se actualizează și partea client
        session.setAttribute("playerNo", newPlayer.getPlayerNo());

        //TODO
        System.out.println(session.getAttribute("playerNo"));

        return Response.status(Response.Status.OK).entity(session.getAttribute("playerNo").toString()).build();
    }

    //Aici jucătorul curent verifică în mod constant în ce stagiu se află (în lobby sau în sesiune)   prin polling
    @GET
    @Path("/pollCurrentPlayerStatus/")
    public Response pollCurrentPlayerStatus(@Context HttpServletRequest request) {
        HttpSession session = request.getSession();
        assert session.getAttribute("playerUsername") != null;

       PlayerStatus currentPlayerStatus = server.getPlayerStatus((String) session.getAttribute("playerUsername"));

        //TODO
        System.out.println("in RestHandler: " + session.getAttribute("playerUsername") + ":" + currentPlayerStatus);

        return Response.status(Response.Status.OK).entity(currentPlayerStatus.toString()).build();
    }

    @POST
    @Path("/setCurrentPlayerSessionIdAndOpponentUsername/")
    public Response setCurrentPlayerSessionIdAndOpponentUsername(@Context HttpServletRequest request) {
        HttpSession session = request.getSession();
        assert session.getAttribute("playerNo") != null;

        String currentPlayerUsername = (String) session.getAttribute("playerUsername");
        Integer currentPlayerSessionId = server.getPlayerSessionId(currentPlayerUsername);
        session.setAttribute("sessionId", currentPlayerSessionId);
        String currentPlayerOpponentUsername = server.getPlayerOpponentUsername(currentPlayerSessionId, currentPlayerUsername);
        session.setAttribute("opponentUsername", currentPlayerOpponentUsername);

        return Response.status(Response.Status.OK).entity(session.getAttribute("sessionId").toString() + ", " + session.getAttribute("opponentUsername")).build();
    }

    @POST
    @Path("/selectedTableCell/")
    public Response selectedTableCell(@Context HttpServletRequest request, @FormParam("row") Integer row, @FormParam("col") Integer col) {
        HttpSession session = request.getSession();

        Integer currentSessionId = (Integer) session.getAttribute("sessionId");
        Integer currentPlayerNo = (Integer) session.getAttribute("playerNo");
        server.processSelectedTableCell(currentSessionId, currentPlayerNo, row, col);

        return Response.status(Response.Status.OK).entity("a fost pus la " + row + ", " + col).build();
    }

    @GET
    @Path("/pollTableInSessionJson/")
    public Response pollTableInSessionJson(@Context HttpServletRequest request) {
        HttpSession session = request.getSession();

        Integer currentSessionId = (Integer) session.getAttribute("sessionId");
        String currentTable = server.getTableInSessionJson(currentSessionId);

        return Response.status(Response.Status.OK).entity(currentTable).build();
    }

    @GET
    @Path("/pollIsCurrentPlayerTurn/")
    public Response pollIsCurrentPlayerTurn(@Context HttpServletRequest request) {
        HttpSession session = request.getSession();

        Integer currentPlayerNo = (Integer) session.getAttribute("playerNo");
        Integer currentSessionId = (Integer) session.getAttribute("sessionId");
        Integer currentTurnInSession = server.getCurrentTurnInSession(currentSessionId);

        return Response.status(Response.Status.OK).entity(Boolean.toString(currentPlayerNo.equals(currentTurnInSession))).build();
    }

    @GET
    @Path("/pollWinningPlayer/")
    public Response pollWinningPlayer(@Context HttpServletRequest request) {
        HttpSession session = request.getSession();

        Integer currentSessionId = (Integer) session.getAttribute("sessionId");

        Player winningPlayer = server.getWinningPlayerInSession(currentSessionId);
        if (winningPlayer != null) {
            String winningPlayerName = winningPlayer.getUsername();

            return Response.status(Response.Status.OK).entity(winningPlayerName).build();
        } else if (server.isTieInSession(currentSessionId)) {

            return Response.status(Response.Status.OK).entity("TIE").build();
        }
        else
            return Response.status(Response.Status.OK).entity("NULL").build();
    }

    /*@POST
    @Path("/onDisconnect/")
    public Response onDisconnect(@Context HttpServletRequest request) {
        HttpSession session = request.getSession();
        assert session.getAttribute("playerUsername") != null;

        String playerUsername = (String) session.getAttribute("playerUsername");
        Player currentPlayer = new Player(playerUsername);
        server.onDisconnect(currentPlayer);

        session.setAttribute("playerNo", null);
        session.setAttribute("sessionId", null);

        return Response.status(Response.Status.OK).build();
    }*/

    @GET
    @Path("/pollIsLobbyAvailable/")
    public Response pollIsLobbyAvailable(@Context HttpServletRequest request){
        Boolean lobbyAvailable = server.isLobbyAvailable();
        if(lobbyAvailable)
            server.refreshLobbyConnectionCheckFlag();

        return Response.status(Response.Status.OK).entity(server.isLobbyAvailable().toString().toUpperCase()).build();
    }

    @GET
    @Path("/pollIsGameSessionAvailable/")
    public Response pollIsGameSessionAvailable(@Context HttpServletRequest request){
        HttpSession session = request.getSession();
        assert session.getAttribute("sessionId") != null;

        Boolean gameSessionAvailable = server.isGameSessionAvailable((int) session.getAttribute("sessionId"));
        if(gameSessionAvailable)
            server.refreshGameSessionConnectionCheckFlag((int) session.getAttribute("playerNo"), (int) session.getAttribute("sessionId"));

        return Response.status(Response.Status.OK).entity(server.isGameSessionAvailable((int) session.getAttribute("sessionId")).toString().toUpperCase()).build();
    }

    @GET
    @Path("/isPlayerAlreadyConnected/")
    public Response isPlayerAlreadyConnected(@Context HttpServletRequest request) {
        HttpSession session = request.getSession();

        String currentPlayerUsername = (String) session.getAttribute("playerUsername");

        return Response.status(Response.Status.OK).entity(server.isPlayerAlreadyConnected(currentPlayerUsername).toString().toUpperCase()).build();
    }
}
