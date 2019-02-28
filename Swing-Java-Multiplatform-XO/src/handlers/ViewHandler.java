package handlers;

import views.Game;
import views.Index;
import views.Signup;

import javax.swing.*;

/**
 * Created by balex on 16.05.2017.
 */
public class ViewHandler {
    private static ViewHandler ourInstance = new ViewHandler();

    public static ViewHandler getInstance() {
        return ourInstance;
    }

    private JFrame mainFrame;

    private Index index;
    private Game game;
    private Signup signup;

    public enum View {INDEX, GAME, SIGNUP}

    private ViewHandler() {
        mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setBounds(100, 100, 500, 500);

        index = new Index(mainFrame);
        game = new Game(mainFrame);
        signup = new Signup(mainFrame);
        index.show();
        index.executeScript();

        mainFrame.setVisible(true);
    }

    public void setView(ViewHandler.View view){
        if(View.INDEX.equals(view)){
            index.show();
            index.executeScript();
        }
        else if (View.GAME.equals(view)){
            game.show();
            game.executeScript();
        }
        else if(View.SIGNUP.equals(view)){
            signup.show();
            signup.executeScript();
        }
        mainFrame.invalidate();
        mainFrame.validate();
        mainFrame.repaint();
    }
}
