package classes.handlers;

import classes.views.Game;
import classes.views.Index;
import classes.views.Signup;

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

    public void setView(String viewName){
        if("index".equals(viewName)){
            index.show();
            index.executeScript();
        }
        else if ("game".equals(viewName)){
            game.show();
            game.executeScript();
        }
        else if("signup".equals(viewName)){
            signup.show();
            signup.executeScript();
        }
        mainFrame.invalidate();
        mainFrame.validate();
        mainFrame.repaint();
    }
}
