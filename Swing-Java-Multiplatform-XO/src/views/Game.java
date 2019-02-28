package views;

import jsonparser.HardcodedXOTableParser;
import player.CurrentPlayer;
import handlers.ViewHandler;
import handlers.ServerHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class Game {
    private JFrame containingFrame;

    private Container mainPanel;

    private JPanel administrativePanel;
    private JPanel gamePanel;

    private JLabel usernameLabel;
    private JLabel opponentLabel;
    private JLabel sessionIdLabel;
    private JLabel playerNoLabel;
    private JButton returnToMenuButton;
    private JButton[][] tableButtons;

    private Timer pollTimer;

    ////
    public Game(JFrame containingFrame) {
        this.containingFrame = containingFrame;

        mainPanel = new JPanel();
        mainPanel.setBackground(Color.RED);
        mainPanel.setLayout(new GridBagLayout());
    }

    public void show() {
        mainPanel.removeAll();

        pollTimer = new Timer();

        administrativePanel = new JPanel(new GridBagLayout());
        administrativePanel.setBackground(Color.ORANGE);
        gamePanel = new JPanel(new GridBagLayout());
        gamePanel.setBackground(Color.PINK);

        usernameLabel = new JLabel("Username: " + CurrentPlayer.getInstance().getUsername());
        opponentLabel = new JLabel("Opponent: " + CurrentPlayer.getInstance().getOpponentUsername());
        sessionIdLabel = new JLabel("Session ID: " + CurrentPlayer.getInstance().getSessionId());
        playerNoLabel = new JLabel("Player No.: " + CurrentPlayer.getInstance().getPlayerNo());
        returnToMenuButton = new JButton("Return To Menu");

        tableButtons = new JButton[3][3];

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        mainPanel.add(administrativePanel, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 1;
        constraints.weighty = 5;
        constraints.fill = GridBagConstraints.BOTH;
        mainPanel.add(gamePanel, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        administrativePanel.add(usernameLabel, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        administrativePanel.add(opponentLabel, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        administrativePanel.add(sessionIdLabel, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        administrativePanel.add(playerNoLabel, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 4;
        administrativePanel.add(returnToMenuButton, constraints);

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                constraints = new GridBagConstraints();
                constraints.gridx = col;
                constraints.gridy = row;
                constraints.weightx = 1;
                constraints.weighty = 1;
                constraints.fill = GridBagConstraints.BOTH;

                tableButtons[row][col] = new JButton("r" + row + "c" + col);
                tableButtons[row][col].setFont(new Font("Courier", Font.PLAIN, 40));
                gamePanel.add(tableButtons[row][col], constraints);
            }
        }

        containingFrame.getContentPane().removeAll();
        containingFrame.add(mainPanel);
    }

    public void executeScript() {
        returnToMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO
                //ServerHandler.getInstance().onDisconnect();
                CurrentPlayer.getInstance().setPlayerNo(null);
                CurrentPlayer.getInstance().setSessionId(null);
                pollTimer.cancel();
                pollTimer.purge();
                ViewHandler.getInstance().setView(ViewHandler.View.INDEX);
            }
        });

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                final int i = row;
                final int j = col;
                tableButtons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ServerHandler.getInstance().selectedTableCell(i, j);
                    }
                });
            }
        }

        startPolling();
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
        boolean gameSessionAvailable = ServerHandler.getInstance().pollIsGameSessionAvailable();
        if(gameSessionAvailable){
            String tableJson = ServerHandler.getInstance().pollTableInSessionJson();
            String[][] table = HardcodedXOTableParser.parse(tableJson);
            boolean currentPlayerTurn = ServerHandler.getInstance().pollIsCurrentPlayerTurn();
            final String winningPlayer = ServerHandler.getInstance().pollWinningPlayer();
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    final int i = row;
                    final int j = col;
                    tableButtons[i][j].setText(table[i][j]);
                    tableButtons[i][j].setEnabled(!((!currentPlayerTurn) || !"_".equals(table[i][j]) || winningPlayer != null));
                }
            }

            if (winningPlayer != null && !"TIE".equals(winningPlayer)) {
                pollTimer.cancel();
                pollTimer.purge();
                JOptionPane.showMessageDialog(null, winningPlayer + " won!");
            } else if ("TIE".equals(winningPlayer)) {
                pollTimer.cancel();
                pollTimer.purge();
                JOptionPane.showMessageDialog(null, "Tie!");
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Session expired, you will be redirected.");
            ViewHandler.getInstance().setView(ViewHandler.View.INDEX);
            pollTimer.cancel();
            pollTimer.purge();
        }
    }

    @Deprecated
    public void printMatrix(Object[][] objects) {
        for (int i = 0; i < objects.length; i++) {
            for (int j = 0; j < objects[i].length; j++)
                System.out.print(objects[i][j] + ", ");
            System.out.println();
        }
    }

}