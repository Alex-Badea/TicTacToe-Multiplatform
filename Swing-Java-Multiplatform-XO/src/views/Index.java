package views;

import player.CurrentPlayer;
import handlers.ViewHandler;
import handlers.ServerHandler;
import player.PlayerStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimerTask;
import java.util.Timer;

/**
 * Created by balex on 16.05.2017.
 */
public class Index {
    private JFrame containingFrame;

    private Container mainPanel;

    private JLabel logoLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;

    private JLabel welcomeLabel;
    private JButton enterLobbyButton;
    private JButton logoutButton;

    private Timer pollTimer;

    ////
    public Index(JFrame containingFrame) {
        this.containingFrame = containingFrame;

        mainPanel = new JPanel();
        mainPanel.setBackground(Color.LIGHT_GRAY);
        mainPanel.setLayout(new GridBagLayout());
    }

    public void show() {
        mainPanel.removeAll();

        pollTimer = new Timer();

        if (CurrentPlayer.getInstance().getUsername() == null) {
            logoLabel = new JLabel("X & O");
            logoLabel.setFont(new Font("Serif", Font.PLAIN, 120));
            usernameLabel = new JLabel("Username : ");
            passwordLabel = new JLabel("Password : ");
            usernameField = new JTextField("", 20);
            passwordField = new JPasswordField("", 20);
            passwordField.setEchoChar('☺');
            loginButton = new JButton("Login");
            signupButton = new JButton("Signup");

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.gridwidth = 5;
            constraints.gridheight = 5;
            constraints.weightx = 5;
            mainPanel.add(logoLabel, constraints);

            constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 10;
            constraints.weightx = 5;
            mainPanel.add(usernameLabel, constraints);

            constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 11;
            constraints.weightx = 5;
            mainPanel.add(passwordLabel, constraints);

            constraints = new GridBagConstraints();
            constraints.gridx = 1;
            constraints.gridy = 10;
            constraints.gridwidth = 2;
            constraints.weightx = 10;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            mainPanel.add(usernameField, constraints);

            constraints = new GridBagConstraints();
            constraints.gridx = 1;
            constraints.gridy = 11;
            constraints.gridwidth = 2;
            constraints.weightx = 10;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            mainPanel.add(passwordField, constraints);

            constraints = new GridBagConstraints();
            constraints.gridx = 1;
            constraints.gridy = 12;
            constraints.weightx = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            mainPanel.add(loginButton, constraints);

            constraints = new GridBagConstraints();
            constraints.gridx = 2;
            constraints.gridy = 12;
            constraints.weightx = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            mainPanel.add(signupButton, constraints);
        } else if (CurrentPlayer.getInstance().getUsername() != null) {
            welcomeLabel = new JLabel("Welcome, " + CurrentPlayer.getInstance().getUsername() + "!");
            enterLobbyButton = new JButton("Enter Lobby");
            logoutButton = new JButton("Logout");

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 1;
            mainPanel.add(welcomeLabel, constraints);

            constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.weightx = 1;
            mainPanel.add(enterLobbyButton, constraints);

            constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 2;
            constraints.weightx = 1;
            mainPanel.add(logoutButton, constraints);
        }

        containingFrame.getContentPane().removeAll();
        containingFrame.add(mainPanel);
    }

    public void executeScript() {
        if (loginButton != null)
            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean successful = ServerHandler.getInstance().login(
                            usernameField.getText(),
                            passwordField.getText());
                    if (successful)
                        ViewHandler.getInstance().setView(ViewHandler.View.INDEX);
                    else {
                        JOptionPane.showMessageDialog(null, "Login unsuccessful");
                    }
                    usernameField.setText(null);
                    passwordField.setText(null);
                }
            });

        if (signupButton != null)
            signupButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ViewHandler.getInstance().setView(ViewHandler.View.SIGNUP);
                }
            });

        if (enterLobbyButton != null)
            enterLobbyButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean playerAlreadyConnected = ServerHandler.getInstance().isPlayerAlreadyConnected();
                    if (!playerAlreadyConnected) {
                        ServerHandler.getInstance().onConnect();
                        startPolling();
                    } else
                        JOptionPane.showMessageDialog(null, "Already connected!");
                }
            });

        if (logoutButton != null)
            logoutButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pollTimer.cancel();
                    pollTimer.purge();
                    ServerHandler.getInstance().logout();
                    ViewHandler.getInstance().setView(ViewHandler.View.INDEX);
                }
            });
    }

    private void startPolling() {
        pollTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                update();
            }
        }, 0, 1000);
    }

    private void update() {
        //TODO
        System.out.println("Polling in Index");

        boolean lobbyAvailable = ServerHandler.getInstance().pollIsLobbyAvailable();
        PlayerStatus currentPlayerStatus = ServerHandler.getInstance().pollCurrentPlayerStatus();
        if (currentPlayerStatus == PlayerStatus.IN_SESSION && !lobbyAvailable) {
            pollTimer.cancel();
            pollTimer.purge();
            ServerHandler.getInstance().setCurrentPlayerSessionIdAndOpponentUsername();
            ViewHandler.getInstance().setView(ViewHandler.View.GAME);
        } else if (currentPlayerStatus == PlayerStatus.IN_LOBBY && lobbyAvailable) {
            enterLobbyButton.setEnabled(false);
        } else if (currentPlayerStatus == PlayerStatus.IN_LOBBY && !lobbyAvailable) {
            JOptionPane.showMessageDialog(null, "Lobby expired, you will be redirected.");
            ViewHandler.getInstance().setView(ViewHandler.View.INDEX);
        }
    }
}
