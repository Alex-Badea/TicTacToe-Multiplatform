package classes.gamesession.xo;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Contract;

/**
 * Created by balex on 07.05.2017.
 * Tabla de joc ce conține o matrice 3x3 de tipul enum cu valorile posibile pentru joc
 */
public class XOTable {
    XOSymbol[][] table;

    ////
    //Verifică pe coloană dacă un simbol a câștigat
    @Nullable
    private XOSymbol columnCheck(int column) {
        if (table[column][0] == table[column][1] && table[column][1] == table[column][2])
            return table[column][0] != XOSymbol.EMPTY ? table[column][0] : null;
        return null;
    }

    //Verifică pe linie dacă un simbol a câștigat
    @Nullable
    private XOSymbol rowCheck(int row) {
        if (table[0][row] == table[1][row] && table[1][row] == table[2][row])
            return table[0][row] != XOSymbol.EMPTY ? table[0][row] : null;
        return null;
    }

    //Verifică pe diagonală dacă un simbol a câștigat
    @Nullable
    private XOSymbol diagonalsCheck() {
        if (table[0][0] == table[1][1] && table[1][1] == table[2][2])
            return table[0][0] != XOSymbol.EMPTY ? table[0][0] : null;
        if (table[0][2] == table[1][1] && table[1][1] == table[2][0])
            return table[0][2] != XOSymbol.EMPTY ? table[0][2] : null;
        return null;
    }
    ////
    //Inițializează tabla de joc cu valori nule
    public XOTable() {
        table = new XOSymbol[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                table[i][j] = XOSymbol.EMPTY;
    }

    //Adaugă la linia și coloana primite ca parametru simbolul primit ca parametru
    public void select(XOSymbol symbol, int row, int col) {
        table[row][col] = symbol;
    }

    //Returnează simbolul de pe linia și coloana primite ca parametru
    public XOSymbol get(int row, int col) {
        return table[row][col];
    }

    //Returnează un simbol dacă e câștigător pe linie, coloană sau diagonală; null altfel
    @Nullable
    public XOSymbol checkForWin() {
        for (int i = 0; i < 3; i++)
            if (columnCheck(i) != null)
                return columnCheck(i);
            else if (rowCheck(i) != null)
                return rowCheck(i);
        return diagonalsCheck();
    }

    //Verifică dacă toate celulele tablei de joc sunt completate cu un simbol și nu există un câștigător
    public boolean isTie(){
        boolean b = true;
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                b = b && (table[i][j] != XOSymbol.EMPTY);
        return b && (checkForWin() == null);
    }

    //Returnează tabla de joc în format JSON
    public String toJson(){
        return "[[\"" + table[0][0] + "\",\"" + table[0][1] + "\",\"" + table[0][2] + "\"]," +
                "[\"" + table[1][0] + "\",\"" + table[1][1] + "\",\"" + table[1][2] + "\"]," +
                "[\"" + table[2][0] + "\",\"" + table[2][1] + "\",\"" + table[2][2] + "\"]]";
    }
}
