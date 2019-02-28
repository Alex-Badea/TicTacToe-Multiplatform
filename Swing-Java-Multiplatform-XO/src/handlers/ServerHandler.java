package handlers;

import player.CurrentPlayer;
import player.PlayerStatus;
import com.sun.istack.internal.Nullable;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.nio.Buffer;
import java.util.Scanner;

/**
 * Created by balex on 16.05.2017.
 */
public class ServerHandler {
    private static ServerHandler ourInstance = new ServerHandler();

    public static ServerHandler getInstance() {
        return ourInstance;
    }

    private final WebResource SERVICE;

    private ServerHandler() {
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));

        final Client CLIENT = Client.create(new DefaultClientConfig());
        SERVICE = CLIENT.resource(UriBuilder.fromUri("http://localhost:80/rest").build());
    }

    ////
    public boolean login(String username, String password) {
        Form form = new Form();
        form.add("username", username);
        form.add("password", password);
        ClientResponse response = SERVICE.path("login").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, form);

        CurrentPlayer.getInstance().setUsername(username);

        return Boolean.parseBoolean(response.getEntity(String.class));
    }

    public void logout() {
        ClientResponse response = SERVICE.path("logout").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class);
        System.out.println(response.getEntity(String.class));
        CurrentPlayer.getInstance().invalidateSession();
    }

    public boolean createAccount(String username, String password) {
        Form form = new Form();
        form.add("username", username);
        form.add("password", password);
        ClientResponse response = SERVICE.path("createAccount").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, form);

        return Boolean.parseBoolean(response.getEntity(String.class));
    }

    public void onConnect() {
        assert CurrentPlayer.getInstance().getUsername() != null;

        CurrentPlayer.getInstance().setStatus(PlayerStatus.IN_LOBBY);

        ClientResponse response = SERVICE.path("onConnect").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class);
        CurrentPlayer.getInstance().setPlayerNo(Integer.parseInt(response.getEntity(String.class)));
    }

    public PlayerStatus pollCurrentPlayerStatus() {
        assert CurrentPlayer.getInstance().getUsername() != null;

        ClientResponse response = SERVICE.path("pollCurrentPlayerStatus").type(MediaType.APPLICATION_FORM_URLENCODED).get(ClientResponse.class);
        PlayerStatus playerStatus = PlayerStatus.parse(response.getEntity(String.class));
        CurrentPlayer.getInstance().setStatus(playerStatus);

        return playerStatus;
    }

    public void setCurrentPlayerSessionIdAndOpponentUsername() {
        assert CurrentPlayer.getInstance().getPlayerNo() != null;

        ClientResponse response = SERVICE.path("setCurrentPlayerSessionIdAndOpponentUsername").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class);
        String responseEntity = response.getEntity(String.class);
        Scanner in = new Scanner(responseEntity);
        in.useDelimiter("[ ,]+");
        Integer sessionId = Integer.parseInt(in.next());
        String opponentUsername = in.next();
        CurrentPlayer.getInstance().setSessionId(sessionId);
        CurrentPlayer.getInstance().setOpponentUsername(opponentUsername);
    }

    public void selectedTableCell(int row, int col) {
        Form form = new Form();
        form.add("row", row);
        form.add("col", col);
        ClientResponse response = SERVICE.path("selectedTableCell").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, form);

        //TODO
        System.out.println(response.getEntity(String.class));
    }

    public String pollTableInSessionJson() {
        ClientResponse response = SERVICE.path("pollTableInSessionJson").type(MediaType.APPLICATION_FORM_URLENCODED).get(ClientResponse.class);

        return response.getEntity(String.class);
    }

    public boolean pollIsCurrentPlayerTurn() {
        ClientResponse response = SERVICE.path("pollIsCurrentPlayerTurn").type(MediaType.APPLICATION_FORM_URLENCODED).get(ClientResponse.class);

        return Boolean.parseBoolean(response.getEntity(String.class));
    }

    @Nullable
    public String pollWinningPlayer() {
        ClientResponse response = SERVICE.path("pollWinningPlayer").type(MediaType.APPLICATION_FORM_URLENCODED).get(ClientResponse.class);

        String winningPlayer = response.getEntity(String.class);

        return "NULL".equals(winningPlayer) ? null : winningPlayer;
    }

    /*public void onDisconnect() {
        assert CurrentPlayer.getInstance().getUsername() != null;

        ClientResponse response = SERVICE.path("onDisconnect").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class);
        CurrentPlayer.getInstance().setPlayerNo(null);
        CurrentPlayer.getInstance().setSessionId(null);
    }*/

    public boolean pollIsLobbyAvailable() {
        ClientResponse response = SERVICE.path("pollIsLobbyAvailable").type(MediaType.APPLICATION_FORM_URLENCODED).get(ClientResponse.class);

        return Boolean.parseBoolean(response.getEntity(String.class));
    }

    public boolean pollIsGameSessionAvailable() {
        ClientResponse response = SERVICE.path("pollIsGameSessionAvailable").type(MediaType.APPLICATION_FORM_URLENCODED).get(ClientResponse.class);

        return Boolean.parseBoolean(response.getEntity(String.class));
    }

    public boolean isPlayerAlreadyConnected() {
        ClientResponse response = SERVICE.path("isPlayerAlreadyConnected").type(MediaType.APPLICATION_FORM_URLENCODED).get(ClientResponse.class);

        return Boolean.parseBoolean(response.getEntity(String.class));
    }
}
