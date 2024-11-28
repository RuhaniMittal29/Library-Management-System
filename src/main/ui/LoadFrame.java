package ui;

import model.Book;
import model.Library;
import model.User;
import model.UserDatabase;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

// CREATES THE GUI FOR A PAGE TO LOAD THE DATA
public class LoadFrame extends JFrame {

    private User user;

    private JButton yesButton;
    private JButton noButton;

    private UserDatabase userdatabase;
    private Library library;

    private JLabel loadLabel;

    private JsonReader jsonReader;
    private static final String USER_JSON_STORE = "./data/user.json";
    private static final String LIBRARY_JSON_STORE = "./data/library.json";


    // MODIFIES: this
    // EFFECTS: creates the gui for load page
    public LoadFrame(User user) {
        this.user = user;

        try {
            jsonReader = new JsonReader(USER_JSON_STORE, LIBRARY_JSON_STORE);
            userdatabase = jsonReader.readUsers();
        } catch (IOException e) {
            System.out.println("Unable to read users from file");;
        }

        // create the frame
        setTitle("Load");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // create the panel
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(null);

        makeButton();
        makeLabel();

        yesButton.addActionListener(e -> load());
        noButton.addActionListener(e -> loadLibrary());

        // add components
        backgroundPanel.add(yesButton);
        backgroundPanel.add(noButton);
        backgroundPanel.add(loadLabel);

        setContentPane(backgroundPanel);
        setVisible(true);
    }

    // EFFECTS: make the buttons for "yes" and "no"
    private void makeButton() {

        yesButton = new JButton("Yes");
        yesButton.setBounds(160, 200, 100, 45);
        yesButton.setFont(new Font("Times New Roman", Font.BOLD, 18));
        yesButton.setOpaque(true);
        yesButton.setBorderPainted(false);
        yesButton.setFocusPainted(false);
        yesButton.setBackground(new Color(204, 255, 204));


        noButton = new JButton("No");
        noButton.setBounds(330, 200, 100, 45);
        noButton.setFont(new Font("Times New Roman", Font.BOLD, 18));
        noButton.setOpaque(true);
        noButton.setBorderPainted(false);
        noButton.setFocusPainted(false);
        noButton.setBackground(new Color(204, 255, 204));

    }

    // MODIFIES: this
    // EFFECTS: load the user and library
    private void load() {
        updateUser();
        try {
            if (jsonReader == null) {
                System.out.println("JsonReader is not initialized.");
                return;
            }

            Library library = jsonReader.readLibrary();

            if (library == null) {
                System.out.println("Library is not properly initialized.");
                return;
            }

            //System.out.println("Library has been Loaded");
            openNextPage(library, user, userdatabase);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + e.getMessage());
        }
    }

    // MODIFIES: this
    // EFFECTS: update the user
    void updateUser() {
        loadUsers();
        for (User user: userdatabase.getAllUsers()) {
            if (user.getName().equals(this.user.getName()) && user.getEmail().equals(this.user.getEmail())) {
                List<Book> userBook = user.getBorrowedBooks();
                this.user.setBorrowedBooks(userBook);
                return;
            }
        }

    }

    // MODIFIES: this
    // EFFECTS: load the users
    private void loadUsers() {
        try {
            userdatabase = jsonReader.readUsers();
            if (userdatabase == null) {
                System.out.println("Empty user");
            } else if (userdatabase.getAllUsers().size() == 0) {
                System.out.println("Empty");
            } else {
                //System.out.println("Has been loaded");
            }
        } catch (IOException e) {
            System.out.println("Unable to read users from file: " + USER_JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: load the library
    private void loadLibrary() {
        try {
            library = jsonReader.readLibrary();
            //System.out.println("Library has been Loaded");
            openNextPage(library, user, userdatabase);
        } catch (IOException e) {
            System.out.println("Unable to read from file: ");
        }
    }

    private void openNextPage(Library library, User user, UserDatabase userDatabase) {
        ChoicesFrame choicesFrame = new ChoicesFrame(library, user, userDatabase);

        this.dispose();
    }

    // EFFECTS: make the label
    private void makeLabel() {

        loadLabel = new JLabel("Load");
        loadLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
        loadLabel.setBounds(260, 130, 70, 50);
        loadLabel.setForeground(Color.WHITE);
        loadLabel.setBackground(Color.BLACK);
        loadLabel.setOpaque(true);

    }

    // MODIFIES: this
    // EFFECTS: creates the background panel for the program
    private static class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            // Load the background image (replace "path/to/background.jpg" with the actual path to your image)
            backgroundImage = new
                    ImageIcon("Images/library1.jpg").getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
