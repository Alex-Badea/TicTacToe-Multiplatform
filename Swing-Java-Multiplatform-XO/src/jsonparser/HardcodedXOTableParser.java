package jsonparser;

import java.util.Scanner;

/**
 * Created by balex on 17.05.2017.
 */

//TODO de înlocuit cu un parser adevărat cândva
@Deprecated
public abstract class HardcodedXOTableParser {
    public static String[][] parse(String tableJson){
        String[][] table = new String[3][3];
        Scanner in = new Scanner(tableJson);
        in.useDelimiter("[ ,\\[\\]\"]+");

        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++) {
                table[i][j] = in.next();
                assert  "X".equals(table[i][j]) || "O".equals(table[i][j]) || "EMPTY".equals(table[i][j]);
            }
        return table;
    }
}
