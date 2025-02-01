import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

class ATMSystem extends JFrame {
    private final String userID = "1234";
    private final String pin = "1234";
    private double balance = 1000.0;
    private JTextArea textArea;
    //private final String bankName = "Bank of India";
    private final Color primaryColor = new Color(0, 107, 143); // SBI Logo Blue
    private final Color buttonTextColor = Color.WHITE; // White text for buttons

    public ATMSystem() {
        setTitle("ATM INTERFACE");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create a custom panel for the main window to add a background image
        BackgroundPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());

        JLabel bankLabel = new JLabel("ATM Interface", JLabel.CENTER);  // Changed from "Bank of India" to "ATM Interface"
bankLabel.setFont(new Font("Arial", Font.BOLD, 20));


        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(primaryColor); // Using SBI Blue for button background
        loginButton.setForeground(buttonTextColor); // White for text color
        loginButton.addActionListener(e -> showLoginScreen());  // Show login screen when the button is clicked

        mainPanel.add(bankLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(loginButton, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            try {
                backgroundImage = new ImageIcon("C:\\path_to_image\\atm.jpg").getImage();
            } catch (Exception e) {
                System.out.println("Error loading image: " + e.getMessage());
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                System.out.println("Background image not loaded.");
            }
        }
    }

    private void showLoginScreen() {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setSize(300, 150);
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null);
        JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        loginPanel.setBackground(new Color(213, 206, 237));

        JLabel userIDLabel = new JLabel("User ID:");
        JTextField userIDField = new JTextField();
        JLabel pinLabel = new JLabel("PIN:");
        JPasswordField pinField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        loginButton.setBackground(primaryColor); // Using SBI Blue for button background
        loginButton.setForeground(buttonTextColor); // White for text color

        loginButton.addActionListener(e -> {
            if (userIDField.getText().equals(userID) && new String(pinField.getPassword()).equals(pin)) {
                showMainMenu();
                loginFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid User ID or PIN", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loginPanel.add(userIDLabel);
        loginPanel.add(userIDField);
        loginPanel.add(pinLabel);
        loginPanel.add(pinField);
        loginPanel.add(loginButton);

        loginFrame.add(loginPanel);
        loginFrame.setVisible(true);
    }

    private void showMainMenu() {
        JFrame mainMenuFrame = new JFrame("Main Menu");
        mainMenuFrame.setSize(400, 300);
        mainMenuFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainMenuFrame.setLocationRelativeTo(null);
    
        // Set a new light teal background color for the main menu
        JPanel mainMenuPanel = new JPanel();
        mainMenuPanel.setLayout(new GridLayout(5, 1));
        mainMenuPanel.setBackground(new Color(173, 216, 230)); // Light teal color
    
        String[] options = {"Transaction History", "Withdraw", "Deposit", "Transfer", "Quit"};
        JButton[] buttons = new JButton[5];
        ActionListener[] actions = {
            e -> showTransactionHistory(),
            e -> showWithdrawScreen(),
            e -> showDepositScreen(),
            e -> showTransferScreen(),
            e -> mainMenuFrame.dispose()
        };
    
        // Set button color to dark blue with white text
        for (int i = 0; i < options.length; i++) {
            buttons[i] = new JButton(options[i]);
            buttons[i].setBackground(new Color(0, 0, 139)); // Dark blue for buttons
            buttons[i].setForeground(Color.WHITE); // White text for buttons
            buttons[i].setFont(new Font("Arial", Font.BOLD, 14));
            buttons[i].setFocusPainted(false); // Remove the focus ring
            buttons[i].setBorderPainted(false); // Remove button border
            buttons[i].addActionListener(actions[i]);
            mainMenuPanel.add(buttons[i]);
        }
    
        mainMenuFrame.add(mainMenuPanel);
        mainMenuFrame.setVisible(true);
    }
    

    private void showTransactionHistory() {
        JFrame historyFrame = new JFrame("Transaction History");
        historyFrame.setSize(400, 300);
        historyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        historyFrame.setLocationRelativeTo(null);
        JTextArea historyTextArea = new JTextArea();
        historyTextArea.setEditable(false);

        try (BufferedReader br = new BufferedReader(new FileReader("transaction_history.txt"))) {
            historyTextArea.read(br, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        historyFrame.add(new JScrollPane(historyTextArea));
        historyFrame.setVisible(true);
    }

    private void showWithdrawScreen() {
        showTransactionScreen("Withdraw");
    }

    private void showDepositScreen() {
        showTransactionScreen("Deposit");
    }

    private void showTransactionScreen(String type) {
        JFrame frame = new JFrame(type);
        frame.setSize(300, 150);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.setBackground(new Color(213, 206, 237));

        JLabel label = new JLabel("Enter Amount:");
        JTextField amountField = new JTextField();
        JButton actionButton = new JButton(type);
        actionButton.setBackground(primaryColor); // Using SBI Blue for button background
        actionButton.setForeground(buttonTextColor); // White for text color

        actionButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (amount > 0) {
                    if (type.equals("Withdraw") && amount > balance) {
                        JOptionPane.showMessageDialog(null, "Insufficient Balance", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    balance += type.equals("Deposit") ? amount : -amount;
                    updateTransactionHistory(type + ": Rs." + amount);
                    JOptionPane.showMessageDialog(null, type + " Successful!");
                    frame.dispose();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid Amount", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(label);
        panel.add(amountField);
        panel.add(actionButton);
        frame.add(panel);
        frame.setVisible(true);
    }

    private void showTransferScreen() {
        JFrame frame = new JFrame("Transfer");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.setBackground(new Color(213, 206, 237));

        JLabel accountIDLabel = new JLabel("Recipient ID:");
        JTextField accountIDField = new JTextField();
        JLabel amountLabel = new JLabel("Amount:");
        JTextField amountField = new JTextField();
        JButton transferButton = new JButton("Transfer");
        transferButton.setBackground(primaryColor); // Using SBI Blue for button background
        transferButton.setForeground(buttonTextColor); // White for text color

        transferButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(null, "Invalid Amount", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (amount > balance) {
                    JOptionPane.showMessageDialog(null, "Insufficient Balance", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String recipientID = accountIDField.getText();
                if (recipientID.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Recipient ID is required", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                balance -= amount;
                updateTransactionHistory("Transferred Rs." + amount + " to " + recipientID);
                JOptionPane.showMessageDialog(null, "Transfer Successful!");
                frame.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid Amount", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(accountIDLabel);
        panel.add(accountIDField);
        panel.add(amountLabel);
        panel.add(amountField);
        panel.add(transferButton);
        frame.add(panel);
        frame.setVisible(true);
    }

    private void updateTransactionHistory(String transaction) {
        textArea.append(transaction + "\n");
        try (FileWriter writer = new FileWriter("transaction_history.txt", true)) {
            writer.write(transaction + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ATMSystem().setVisible(true));
    }
}
