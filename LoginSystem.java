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

public class LoginSystem extends JFrame {
    private JTextField usernameField, userIdField, userNameField, courseField, yearField, sectionField;
    private JPasswordField passwordField;
    private JButton loginButton, logoutButton, addRecordButton, editRecordButton, deleteRecordButton, searchRecordButton, displayAllButton;
    private JTextArea outputArea;
    private JPanel loginPanel, mainPanel, addRecordPanel, editRecordPanel, deleteRecordPanel, searchRecordPanel, displayAllPanel;
    private File dataFile, userFile;
    private HashMap<String, String> credentials;
    private JTextArea recordTextArea;

    public LoginSystem() {
        setTitle("Login System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        loginPanel = new JPanel();
        mainPanel = new JPanel(new CardLayout());

        outputArea = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (checkCredentials(username, password)) {
                    showMainMenu();
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

        mainPanel.add(loginPanel, "loginPanel");
        mainPanel.add(createMainMenu(), "mainMenuPanel");
        mainPanel.add(createAddRecordPanel(), "addRecordPanel");
        mainPanel.add(createEditRecordPanel(), "editRecordPanel");

        add(mainPanel);

        setVisible(true);

        dataFile = new File("studData.txt");
        userFile = new File("userData.txt");
        credentials = new HashMap<>();
        loadCredentials();
    }

    private JPanel createMainMenu() {
        JPanel menuPanel = new JPanel(new GridLayout(0, 1));

        addRecordButton = new JButton("Add Record");
        addRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPanel("addRecordPanel");
            }
        });

        editRecordButton = new JButton("Edit Record");
        editRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPanel("editRecordPanel");
            }
        });

        deleteRecordButton = new JButton("Delete Record");
        // Implement delete record functionality here

        searchRecordButton = new JButton("Search Record");
        // Implement search record functionality here

        displayAllButton = new JButton("Display All Records");
        // Implement display all records functionality here

        menuPanel.add(addRecordButton);
        menuPanel.add(editRecordButton);
        menuPanel.add(deleteRecordButton);
        menuPanel.add(searchRecordButton);
        menuPanel.add(displayAllButton);
        menuPanel.add(logoutButton);

        return menuPanel;
    }

    private void showMainMenu() {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, "mainMenuPanel");
    }

    private void showLoginPanel() {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, "loginPanel");
    }

    private void showPanel(String panelName) {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, panelName);
    }

    private boolean checkCredentials(String username, String password) {
        return credentials.containsKey(username) && credentials.get(username).equals(password);
    }

    private void loadCredentials() {
        try (Scanner scanner = new Scanner(userFile)) {
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

// Add Record

private JPanel createAddRecordPanel() {
    JPanel addRecordPanel = new JPanel(new GridLayout(0, 1));

    userIdField = new JTextField(20);
    userNameField = new JTextField(20);
    courseField = new JTextField(20);
    yearField = new JTextField(20);
    sectionField = new JTextField(20);

    JButton saveRecordButton = new JButton("Save Record");
    saveRecordButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String userId = userIdField.getText();
            String userName = userNameField.getText();
            String course = courseField.getText();
            String year = yearField.getText();
            String section = sectionField.getText();

            String record = userId + "," + userName + "," + course + "," + year + "," + section +"\n";
            saveRecord(record);

            JOptionPane.showMessageDialog(null, "Record added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearAddRecordFields();
            showPanel("mainMenuPanel");
        }
    });

    addRecordPanel.add(new JLabel("User ID:"));
    addRecordPanel.add(userIdField);
    addRecordPanel.add(new JLabel("User Name:"));
    addRecordPanel.add(userNameField);
    addRecordPanel.add(new JLabel("Course:"));
    addRecordPanel.add(courseField);
    addRecordPanel.add(new JLabel("Year:"));
    addRecordPanel.add(yearField);
    addRecordPanel.add(new JLabel("Section:"));
    addRecordPanel.add(sectionField);
    addRecordPanel.add(saveRecordButton);

    return addRecordPanel;
}

private void saveRecord(String record) {
    try (PrintWriter writer = new PrintWriter(new FileWriter(dataFile, true))) {
        writer.println(record);
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error saving record", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void clearAddRecordFields() {
    userIdField.setText("");
    userNameField.setText("");
    courseField.setText("");
    yearField.setText("");
    sectionField.setText("");
}

// End of Add Record

// Edit Record

private JPanel createEditRecordPanel() {
    editRecordPanel = new JPanel(new GridLayout(0, 1));

    recordTextArea = new JTextArea(10, 30);
    recordTextArea.setEditable(false); // Make text area read-only

    JScrollPane scrollPane = new JScrollPane(recordTextArea);

    JButton loadRecordsButton = new JButton("Load Records");
    loadRecordsButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String records = loadRecords();
            recordTextArea.setText(records);
        }
    });

    JButton saveChangesButton = new JButton("Save Changes");
    saveChangesButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String editedRecords = recordTextArea.getText();
            saveRecord(editedRecords);
            JOptionPane.showMessageDialog(null, "Records updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            showPanel("mainMenuPanel");
        }
    });

    editRecordPanel.add(scrollPane);
    editRecordPanel.add(loadRecordsButton);
    editRecordPanel.add(saveChangesButton);

    return editRecordPanel;
}

private String loadRecords() {
    StringBuilder recordsText = new StringBuilder();
    try (Scanner scanner = new Scanner(dataFile)) {
        while (scanner.hasNextLine()) {
            recordsText.append(scanner.nextLine()).append("\n");
        }
    } catch (FileNotFoundException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error loading records", "Error", JOptionPane.ERROR_MESSAGE);
    }
    return recordsText.toString();
}


// End of Edit Record


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginSystem();
            }
        });
    }
}
