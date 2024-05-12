/**
 * @(#)LoginSystem.java
 * @author: Edwin S. Garcia 
 * @version 1.00 2024/5/12
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class LoginSystemDemo extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, logoutButton;
    private JTextArea outputArea;
    private File dataFile;
    private HashMap<String, String> credentials;

    public LoginSystemDemo() {
        setTitle("Login System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel loginPanel = new JPanel();
        JPanel mainPanel = new JPanel(new BorderLayout());
        outputArea = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (checkCredentials(username, password)) {
                	JOptionPane.showMessageDialog(null, "Access Granted, Welcome! " + username, "Error", JOptionPane.INFORMATION_MESSAGE);
                	outputArea.setText("You can now type in Text Area and click Logout when done.");
                    showMainPanel();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginPanel();
            }
        });

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);

        JLabel usernameLabel = new JLabel("Username: ");
        JLabel passwordLabel = new JLabel("Password: ");

		loginPanel.setLayout(new GridLayout(3,2));
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(new JLabel(""));
        loginPanel.add(loginButton);
		logoutButton.setEnabled(false);
        mainPanel.add(logoutButton, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(loginPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.SOUTH);

        setVisible(true);

        dataFile = new File("records.txt");
        credentials = new HashMap<>();
        loadCredentials();
        loadRecords();
    }

    private boolean checkCredentials(String username, String password) {
        return credentials.containsKey(username) && credentials.get(username).equals(password);
    }

    private void loadCredentials() {
        try (Scanner scanner = new Scanner(dataFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    credentials.put(parts[0], parts[1]);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
        }
    }

    private void loadRecords() {
        try (Scanner scanner = new Scanner(dataFile)) {
            StringBuilder recordsText = new StringBuilder();
            while (scanner.hasNextLine()) {
                recordsText.append(scanner.nextLine()).append("\n");
            }
            outputArea.setText(recordsText.toString());
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
        }
    }

    private void showLoginPanel() {
        usernameField.setText("");
        passwordField.setText("");
        outputArea.setText("");
        loginButton.setEnabled(true);
        logoutButton.setEnabled(false);
    }

    private void showMainPanel() {
        loginButton.setEnabled(false);
        logoutButton.setEnabled(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginSystemDemo();
            }
        });
    }
}
