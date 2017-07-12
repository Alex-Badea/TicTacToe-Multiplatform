package classes.views;

import classes.handlers.ViewHandler;
import classes.handlers.ServerHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by balex on 16.05.2017.
 */
public class Signup {
    private JFrame containingFrame;

    private Container mainPanel;

    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton createAccountButton;
    private JButton backButton;

    ////
    public Signup(JFrame containingFrame) {
        this.containingFrame = containingFrame;

        mainPanel = new JPanel();
        mainPanel.setBackground(Color.CYAN);
        mainPanel.setLayout(new GridBagLayout());
    }

    public void show() {
        mainPanel.removeAll();

        usernameLabel = new JLabel("Username : ");
        passwordLabel = new JLabel("Password : ");
        usernameField = new JTextField("", 20);
        passwordField = new JPasswordField("", 20);
        passwordField.setEchoChar('☺');
        createAccountButton = new JButton("Create Account");
        backButton = new JButton("Back");

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 5;
        mainPanel.add(usernameLabel, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 5;
        mainPanel.add(passwordLabel, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.weightx = 10;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(usernameField, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.weightx = 10;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(passwordField, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(createAccountButton, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(backButton, constraints);

        containingFrame.getContentPane().removeAll();
        containingFrame.add(mainPanel);
    }

    public void executeScript() {
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean successful = ServerHandler.getInstance().createAccount(
                        usernameField.getText(),
                        passwordField.getText());
                if (successful)
                    ViewHandler.getInstance().setView("index");
                else  {
                    JOptionPane.showMessageDialog(null, "Contul nu a fost creat.");
                }
                usernameField.setText("");
                passwordField.setText("");
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewHandler.getInstance().setView("index");
                usernameField.setText("");
                passwordField.setText("");
            }
        });
    }
}
