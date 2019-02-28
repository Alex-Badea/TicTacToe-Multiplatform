package player;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Created by balex on 20.05.2017.
 */
public enum PlayerStatus {
    OUT_OF_GAME,
    IN_LOBBY,
    IN_SESSION;

    public static PlayerStatus parse(String stringifiedPlayerStatus){
        if("OUT_OF_GAME".equals(stringifiedPlayerStatus))
            return OUT_OF_GAME;
        else if("IN_LOBBY".equals(stringifiedPlayerStatus))
            return IN_LOBBY;
        else if("IN_SESSION".equals(stringifiedPlayerStatus))
            return IN_SESSION;
        else if(stringifiedPlayerStatus == null || "".equals(stringifiedPlayerStatus.trim()))
            throw new UncheckedIOException("Null or empty PlayerStatus.", new IOException());
        else throw new UncheckedIOException("Unparsable PlayerStatus.", new IOException());
    }
}