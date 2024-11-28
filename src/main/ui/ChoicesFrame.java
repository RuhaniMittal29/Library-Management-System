package ui;

import model.*;
import model.Event;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

// CREATES THE MAIN PAGE GUI WITH THE CHOICES
public class ChoicesFrame extends JFrame implements ActionListener {

    private User user;
    private Library library;
    private UserDatabase userdatabase;

    private JButton printButton;
    private JButton viewBookButton;
    private JButton borrowButton;
    private JButton returnButton;
    private JButton borrowedBookButton;
    private JButton saveButton;
    private JButton quitButton;


    private JLabel printImageLabel;
    private JLabel viewImageLabel;
    private JLabel borrowImageLabel;
    private JLabel returnImageLabel;
    private JLabel borrowedBookImageLabel;
    private JLabel saveImageLabel;
    private JLabel quitImageLabel;
    private JLabel mainTitle;


    private JTextField titleField;
    private JTextField genreField;
    private JTextField authorField;

    private static final String USER_JSON_STORE = "./data/user.json";
    private static final String LIBRARY_JSON_STORE = "./data/library.json";
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;


    // MODIFIES: this
    // EFFECTS: creates the main gui for Library App
    public ChoicesFrame(Library library, User user, UserDatabase userdatabase) {
        this.library = library;
        this.user = user;
        this.userdatabase = userdatabase;

        setTitle("Main Page");
        setSize(1000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        jsonWriter = new JsonWriter(USER_JSON_STORE, LIBRARY_JSON_STORE);
        jsonReader = new JsonReader(USER_JSON_STORE, LIBRARY_JSON_STORE);

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(null);

        mainTitle = new JLabel("Ruhani's Library");
        mainTitle.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 40));
        mainTitle.setBounds(350, 60, 300, 50);
        mainTitle.setForeground(Color.WHITE);
        mainTitle.setBackground(Color.BLACK);
        mainTitle.setOpaque(true);


        makeButtons();
        addListeners();
        addComponent(backgroundPanel);

        backgroundPanel.add(mainTitle);

        setContentPane(backgroundPanel);
        setVisible(true);

    }

    // MODIFIES : this
    // EFFECTS: adds components to the main frame
    private void addComponent(BackgroundPanel backgroundPanel) {
        backgroundPanel.add(printButton);
        backgroundPanel.add(printImageLabel);
        backgroundPanel.add(viewBookButton);
        backgroundPanel.add(viewImageLabel);
        backgroundPanel.add(borrowButton);
        backgroundPanel.add(borrowImageLabel);
        backgroundPanel.add(returnButton);
        backgroundPanel.add(returnImageLabel);
        backgroundPanel.add(borrowedBookButton);
        backgroundPanel.add(borrowedBookImageLabel);
        backgroundPanel.add(saveButton);
        backgroundPanel.add(saveImageLabel);
        backgroundPanel.add(quitButton);
        backgroundPanel.add(quitImageLabel);

    }

    // EFFECTS: makes all the buttons for the main frame
    private void makeButtons() {
        makePrintButton();
        makeViewButton();
        makeBorrowButton();
        makeReturnButton();
        makeBorrowedBookButton();
        makeSaveButton();
        makeQuitButton();
    }

    // MODIFIES: this
    // EFFECTS: Adds listeners to the buttons
    private void addListeners() {
        printButton.addActionListener(this);
        viewBookButton.addActionListener(this);
        borrowButton.addActionListener(this);
        returnButton.addActionListener(this);
        borrowedBookButton.addActionListener(this);
        saveButton.addActionListener(this);
        quitButton.addActionListener(this);
    }

    // EFFECTS: Process action when a button is clicked on the menu
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == printButton) {
            printBookInfo();
        } else if (e.getSource() == viewBookButton) {
            viewBookByGenre();
        } else if (e.getSource() == borrowButton) {
            borrowBook();
        } else if (e.getSource() == returnButton) {
            returnBook();
        } else if (e.getSource() == borrowedBookButton) {
            borrowedBooks();
        } else if (e.getSource() == saveButton) {
            save();
        } else if (e.getSource() == quitButton) {
            quitProgram();
        } else {
            System.out.println("\tWrong Input, Please choose again");
        }
    }

    // MODIFIES: this
    // EFFECTS: prints information of book in the form of a message
    private void printBookInfo() {
        JPanel addPanel = new JPanel();
        initAddPanelPrint(addPanel);

        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));

        int result = JOptionPane.showConfirmDialog(null, addPanel,
                "Print Book information", JOptionPane.OK_CANCEL_OPTION);

        boolean found = false;
        if (result == JOptionPane.OK_OPTION) {
            for (Book book : library.getAllBooks()) {
                if (book.searchByTitle(titleField.getText())) {
                    JOptionPane.showMessageDialog(this, "<html>Title : " + book.getTitle()
                            + "<br>Author :" + book.printAuthorName() + "<br>Genre : " + book.getGenre()
                            + "<br>Book ID : " + book.getBookID() + "<br>Availability Status : "
                            + book.getIsAvailable());
                    break;

                }
            }
        } else if (!found) {
            JOptionPane.showMessageDialog(this, "Book not found!");

        }
    }

    // MODIFIES: this
    // EFFECTS: view all the books by a particular genre
    private void viewBookByGenre() {
        JPanel addPanel = new JPanel();
        initAddPanelView(addPanel);

        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));

        int result = JOptionPane.showConfirmDialog(null, addPanel,
                "Enter Book information", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            StringBuilder message = new StringBuilder("<html>");
            for (Book book : library.getAllBooks()) {
                if (book.getGenre().equals(genreField.getText())) {
                    message.append("Title: ").append(book.getTitle())
                            .append("<br>Author: ").append(book.printAuthorName())
                            .append("<br><br>");
                }
            }

            if (message.toString().endsWith("<br><br>")) {
                message.delete(message.length() - 8, message.length());
            }

            message.append("</html>");

            JOptionPane.showMessageDialog(this, message.toString());
        }
    }

    // MODIFIES: this
    // EFFECTS: borrows book for user by the title and author
    private void borrowBook() {

        JPanel addPanel = new JPanel();
        initAddPanelBorrowReturn(addPanel);

        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));

        int result = JOptionPane.showConfirmDialog(null, addPanel,
                "Enter Book information", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            if (user.borrowBookByUser(titleField.getText(), authorField.getText())) {
                JOptionPane.showMessageDialog(this, titleField.getText() + " by "
                        + authorField.getText() + " has been successfully borrowed!");

            } else {
                JOptionPane.showMessageDialog(this, "<html>Sorry, book has already been borrowed"
                        + "<br>Try Something else!!");

            }
        }

    }

    // EFFECTS: returns book for user by the title and author
    private void returnBook() {
        JPanel addPanel = new JPanel();
        initAddPanelBorrowReturn(addPanel);

        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));

        int result = JOptionPane.showConfirmDialog(null, addPanel,
                "Enter Book information", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            if (user.returnBookByUser(titleField.getText(), authorField.getText())) {
                JOptionPane.showMessageDialog(this, titleField.getText() + " by "
                        + authorField.getText() + " has been successfully returned!");

            } else {
                JOptionPane.showMessageDialog(this, "Sorry, book couldn't be returned!!");

            }
        }

    }

    // MODIFIES: this
    // EFFECTS: prints the borrowed books by a user
    private void borrowedBooks() {

        StringBuilder message = new StringBuilder("<html>");
        for (Book book : user.getBorrowedBooks()) {
            message.append("Title: ").append(book.getTitle())
                    .append("<br>Author: ").append(book.printAuthorName())
                    .append("<br><br>");
        }

        if (message.toString().endsWith("<br><br>")) {
            message.delete(message.length() - 8, message.length());
        }

        message.append("</html>");

        JOptionPane.showMessageDialog(this, message.toString());
    }

    // MODIFIES : this
    // EFFECTS : saves the user and library to the file
    private void save() {
        try {
            jsonWriter.openUser();
            jsonWriter.writeUsers(userdatabase);
            jsonWriter.closeUsers();
            //System.out.println("Saved users to " + USER_JSON_STORE);
            jsonWriter.openLibrary();
            jsonWriter.writeLibrary(library);
            jsonWriter.closeLibrary();
            //System.out.println("Saved library to " + LIBRARY_JSON_STORE);
            JOptionPane.showMessageDialog(this, "Data has been Saved!!");
        } catch (FileNotFoundException e) {
            //System.out.println("Unable to write to file: " + USER_JSON_STORE);
        }
    }

    // EFFECTS: print the logs
    private void printLogs(EventLog events) {
        for (Event event : events) {
            System.out.println(event.toString());
        }
    }

    // MODIFIES: this
    // EFFECTS: ends the application
    private void quitProgram() {
        printLogs(EventLog.getInstance());
        System.exit(0);
        this.dispose();
        this.setVisible(false);
    }

    // MODIFIES: this
    // EFFECTS: helper in initializing the add panel for printing book info.
    private void initAddPanelPrint(JPanel addPanel) {
        addPanel.add(Box.createVerticalStrut(10));
        addPanel.add(new JLabel("Title of the book: "));
        titleField = new JTextField(15);
        addPanel.add(titleField);

    }

    // MODIFIES: this
    // EFFECTS: helper in initializing the add panel for viewing all books
    private void initAddPanelView(JPanel addPanel) {
        addPanel.add(Box.createVerticalStrut(10));
        addPanel.add(new JLabel("Genre: "));
        genreField = new JTextField(15);
        addPanel.add(genreField);

    }

    // MODIFIES: this
    // EFFECTS: helper in initializing the add panel for borrowing and returning books
    private void initAddPanelBorrowReturn(JPanel addPanel) {
        addPanel.add(Box.createVerticalStrut(10));
        addPanel.add(new JLabel("Title of the book: "));
        titleField = new JTextField(15);
        addPanel.add(titleField);
        addPanel.add(Box.createVerticalStrut(10));
        addPanel.add(new JLabel("ONE Author name: "));
        authorField = new JTextField(15);
        addPanel.add(authorField);

    }

    // EFFECTS: makes button and image for printing book info.
    private void makePrintButton() {
        printButton = new JButton("<html>Print Book<br>Information</html>");
        printButton.setBounds(250, 150, 170, 100);
        printButton.setFont(new Font("Times New Roman", Font.BOLD, 25));
        printButton.setOpaque(true);
        printButton.setBorderPainted(false);
        printButton.setFocusPainted(false);
        printButton.setBackground(new Color(204, 255, 204));

        ImageIcon icon = new ImageIcon("Images/book.png");
        Image originalImage = icon.getImage();
        Image resizedImage = originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        printImageLabel = new JLabel(resizedIcon);
        printImageLabel.setBounds(150, 150, 100, 100);

    }

    // EFFECTS: makes button and image for viewing all books
    private void makeViewButton() {

        viewBookButton = new JButton("<html>View All<br>Books</html>");
        viewBookButton.setBounds(600, 150, 170, 100);
        viewBookButton.setFont(new Font("Times New Roman", Font.BOLD, 25));
        viewBookButton.setOpaque(true);
        viewBookButton.setBorderPainted(false);
        viewBookButton.setFocusPainted(false);
        viewBookButton.setBackground(new Color(204, 255, 204));


        ImageIcon icon = new ImageIcon("Images/view.png");
        Image originalImage = icon.getImage();
        Image resizedImage = originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        viewImageLabel = new JLabel(resizedIcon);
        viewImageLabel.setBounds(500, 150, 100, 100);

    }

    // EFFECTS: makes button and image for borrowing a book
    private void makeBorrowButton() {

        borrowButton = new JButton("<html>Borrow A<br>Book</html>");
        borrowButton.setBounds(250, 300, 170, 100);
        borrowButton.setFont(new Font("Times New Roman", Font.BOLD, 25));
        borrowButton.setOpaque(true);
        borrowButton.setBorderPainted(false);
        borrowButton.setFocusPainted(false);
        borrowButton.setBackground(new Color(204, 255, 204));


        ImageIcon icon = new ImageIcon("Images/borrow.png");
        Image originalImage = icon.getImage();
        Image resizedImage = originalImage.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        borrowImageLabel = new JLabel(resizedIcon);
        borrowImageLabel.setBounds(150, 300, 100, 100);

    }

    // EFFECTS: makes button and image for returning a book
    private void makeReturnButton() {

        returnButton = new JButton("<html>Return A<br>Book</html>");
        returnButton.setBounds(600, 300, 170, 100);
        returnButton.setFont(new Font("Times New Roman", Font.BOLD, 25));
        returnButton.setOpaque(true);
        returnButton.setBorderPainted(false);
        returnButton.setFocusPainted(false);
        returnButton.setBackground(new Color(204, 255, 204));


        ImageIcon icon = new ImageIcon("Images/return.png");
        Image originalImage = icon.getImage();
        Image resizedImage = originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        returnImageLabel = new JLabel(resizedIcon);
        returnImageLabel.setBounds(500, 300, 100, 100);

    }

    // EFFECTS: makes button and image for printing book history
    private void makeBorrowedBookButton() {

        borrowedBookButton = new JButton("<html>View Borrowed<br>Books</html>");
        borrowedBookButton.setBounds(250, 450, 170, 100);
        borrowedBookButton.setFont(new Font("Times New Roman", Font.BOLD, 25));
        borrowedBookButton.setOpaque(true);
        borrowedBookButton.setBorderPainted(false);
        borrowedBookButton.setFocusPainted(false);
        borrowedBookButton.setBackground(new Color(204, 255, 204));


        ImageIcon icon = new ImageIcon("Images/history.png");
        Image originalImage = icon.getImage();
        Image resizedImage = originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        borrowedBookImageLabel = new JLabel(resizedIcon);
        borrowedBookImageLabel.setBounds(150, 450, 100, 100);

    }

    // EFFECTS: makes button and image for saving the user and library to the file
    private void makeSaveButton() {

        saveButton = new JButton("Save");
        saveButton.setBounds(600, 450, 170, 100);
        saveButton.setFont(new Font("Times New Roman", Font.BOLD, 25));
        saveButton.setOpaque(true);
        saveButton.setBorderPainted(false);
        saveButton.setFocusPainted(false);
        saveButton.setBackground(new Color(204, 255, 204));


        ImageIcon icon = new ImageIcon("Images/save.png");
        Image originalImage = icon.getImage();
        Image resizedImage = originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        saveImageLabel = new JLabel(resizedIcon);
        saveImageLabel.setBounds(520, 450, 100, 100);

    }

    // EFFECTS: makes button and image for ending the program
    private void makeQuitButton() {

        quitButton = new JButton("Quit");
        quitButton.setBounds(450, 650, 170, 100);
        quitButton.setFont(new Font("Times New Roman", Font.BOLD, 25));
        quitButton.setOpaque(true);
        quitButton.setBorderPainted(false);
        quitButton.setFocusPainted(false);
        quitButton.setBackground(new Color(204, 255, 204));


        ImageIcon icon = new ImageIcon("Images/quit.png");
        Image originalImage = icon.getImage();
        Image resizedImage = originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        quitImageLabel = new JLabel(resizedIcon);
        quitImageLabel.setBounds(350, 650, 100, 100);

    }

    // MODIFIES: this
    // EFFECTS: creates a background panel for the program
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

}
