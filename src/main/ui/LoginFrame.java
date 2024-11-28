package ui;

import model.User;
import model.UserDatabase;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import persistence.JsonReader;
import persistence.JsonWriter;

// CREATES THE LOGIN PAGE GUI
public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JTextField emailField;

    private JLabel usernameLabel;
    private JLabel emailLabel;
    private JButton loginButton;

    private UserDatabase userDatabase;

    private JsonReader jsonReader;
    private static final String USER_JSON_STORE = "./data/user.json";
    private static final String LIBRARY_JSON_STORE = "./data/library.json";


    // MODIFIES: this
    // EFFECTS: creates the gui for login page
    public LoginFrame() {
        try {
            jsonReader = new JsonReader(USER_JSON_STORE, LIBRARY_JSON_STORE);
            userDatabase = jsonReader.readUsers();
        } catch (IOException e) {
            System.out.println("Unable to read users from file");
        }

        setTitle("Login");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        makeFieldLabel();
        initializeButton();

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(null);

        backgroundPanel.add(usernameField);
        backgroundPanel.add(emailField);
        backgroundPanel.add(usernameLabel);
        backgroundPanel.add(emailLabel);
        backgroundPanel.add(loginButton);

        setContentPane(backgroundPanel);
        setVisible(true);
    }

    // EFFECTS: initializes the login button
    private void initializeButton() {

        loginButton = new JButton("Login");
        loginButton.setBounds(150, 280, 100, 45);
        loginButton.setFont(new Font("Times New Roman", Font.BOLD, 18));
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setBackground(new Color(204, 255, 204));

        loginButton.addActionListener(e -> login());

    }

    // EFFECTS: makes the text fields and labels
    private void makeFieldLabel() {

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        setContentPane(backgroundPanel);

        usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Times New Roman", Font.BOLD, 22));
        usernameLabel.setBounds(150, 120, 180, 30);
        usernameLabel.setForeground(Color.WHITE);

        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Times New Roman", Font.PLAIN,18));
        usernameField.setBounds(150, 150, 200, 40);

        emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Times New Roman", Font.BOLD, 22));
        emailLabel.setBounds(150, 200, 180, 30);
        emailLabel.setForeground(Color.WHITE);

        emailField = new JTextField(15);
        emailField.setFont(new Font("Times New Roman", Font.PLAIN,18));
        emailField.setBounds(150, 230, 200, 40);

    }

    // MODIFIES: this
    // EFFECTS: if user present in the userDatabase, login
    private void login() {
        String username = usernameField.getText();
        String email = emailField.getText();

        Boolean ifLoggedInUser = validateUser(username, email);

        if (ifLoggedInUser) {
            User loggedInUser = new User(username, email);
            openMainPage(loggedInUser);
        } else {
            JOptionPane.showMessageDialog(this, "Wrong username or password!");
        }
    }

    // EFFECTS: checks if user present in the userDatabase
    private Boolean validateUser(String username, String email) {
        loadUsers();
        Boolean ifValidation = false;

        for (User user : userDatabase.getAllUsers()) {
            if (user.getName().equals(username) && user.getEmail().equals(email)) {
                ifValidation = true;
            }
        }
        return ifValidation;
    }

    // EFFECTS: opens the next page
    private void openMainPage(User user) {

        LoadFrame loadFrame = new LoadFrame(user);

        this.dispose();
    }

    // MODIFIES: this
    // EFFECTS: creates the background panel for the program
    private static class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            backgroundImage = new
                    ImageIcon("Images/library1.jpg").getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // MODIFIES: this
    // EFFECTS: load users
    private void loadUsers() {
        try {
            userDatabase = jsonReader.readUsers();
            if (userDatabase == null) {
                System.out.println("Empty user");
            } else if (userDatabase.getAllUsers().size() == 0) {
                System.out.println("Empty");
            } else {
                //System.out.println("Has been loaded");
            }
        } catch (IOException e) {
            System.out.println("Unable to read users from file: " + USER_JSON_STORE);
        }
    }
}
