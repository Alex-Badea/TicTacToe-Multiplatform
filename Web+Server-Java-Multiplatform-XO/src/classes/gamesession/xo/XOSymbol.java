package classes.gamesession.xo;

/**
 * Created by balex on 07.05.2017.
 * Mulțimea de valori pe care le poate lua textul de pe butoanele din tabla de joc;
 */
public enum XOSymbol {
    X, O, EMPTY;

    ////
    @Override
    public String toString() {
        if (this == X)
            return "X";
        else if (this == O)
            return "O";
        return "_";
    }
}
