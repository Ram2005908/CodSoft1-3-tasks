import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class NumberGuessingGame {

    private static int numberToGuess;
    private static int attemptsLeft;
    private static int totalScore;
    private static JFrame frame;
    private static JTextField guessField;
    private static JLabel feedbackLabel;
    private static JLabel attemptsLabel;
    private static JLabel scoreLabel;

    public static void initializeGame() {
        Random random = new Random();
        numberToGuess = random.nextInt(100) + 1;
        attemptsLeft = 10;
        feedbackLabel.setText("Enter your guess between 1 and 100.");
        feedbackLabel.setFont(new Font("Arial", Font.BOLD, 25));
        attemptsLabel.setText("Attempts Left: " + attemptsLeft);
        guessField.setText("");
    }

    public static void createAndShowGUI() {
        frame = new JFrame("Number Guessing Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 350);
        frame.setLayout(new GridLayout(7, 1));

        JLabel titleLabel = new JLabel("Number Guessing Game", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setForeground(Color.BLUE);
        frame.add(titleLabel);

        feedbackLabel = new JLabel("Enter your guess between 1 and 100.", JLabel.CENTER);
        feedbackLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        feedbackLabel.setForeground(Color.DARK_GRAY);
        frame.add(feedbackLabel);

        JPanel inputPanel = new JPanel();
        JLabel inputLabel = new JLabel("Your Guess: ");
        inputLabel.setFont(new Font("Arial", Font.PLAIN, 25));
        inputLabel.setForeground(Color.BLACK);
        guessField = new JTextField();
        guessField.setPreferredSize(new Dimension(150, 30));
        guessField.setFont(new Font("Arial", Font.PLAIN, 23)); // Adjusted size
        inputPanel.add(inputLabel);
        inputPanel.add(guessField);
        frame.add(inputPanel);

        JPanel buttonPanel = new JPanel();
        JButton guessButton = new JButton("Submit Guess");
        guessButton.setPreferredSize(new Dimension(150, 50));
        guessButton.setBackground(Color.BLUE);
        guessButton.setForeground(Color.WHITE);
        guessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleGuess();
            }
        });
        buttonPanel.add(guessButton);
        frame.add(buttonPanel);

        attemptsLabel = new JLabel("Attempts Left: 10", JLabel.CENTER);
        attemptsLabel.setFont(new Font("Arial", Font.PLAIN, 25));
        attemptsLabel.setForeground(Color.RED);
        attemptsLabel.setPreferredSize(new Dimension(200, 30)); // Adjusted size
        frame.add(attemptsLabel);

        scoreLabel = new JLabel("Total Score: 0", JLabel.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 25));
        scoreLabel.setForeground(Color.GREEN);
        scoreLabel.setPreferredSize(new Dimension(200, 30)); // Adjusted size
        frame.add(scoreLabel);

        frame.setVisible(true);
        initializeGame();
    }

    public static void handleGuess() {
        String input = guessField.getText();
        int guess;

        try {
            guess = Integer.parseInt(input);
            if (guess < 1 || guess > 100) {
                feedbackLabel.setText("Please enter a number between 1 and 100.");
                return;
            }
        } catch (NumberFormatException e) {
            feedbackLabel.setText("Invalid input. Please enter a valid number.");
            return;
        }

        if (guess == numberToGuess) {
            totalScore += attemptsLeft;
            scoreLabel.setText("Total Score: " + totalScore);
            showCongratulationsPanel();
        } else if (guess < numberToGuess) {
            feedbackLabel.setText("Too low! Try again.");
            guessField.setText("");
        } else {
            feedbackLabel.setText("Too high! Try again.");
            guessField.setText("");
        }

        attemptsLeft--;
        attemptsLabel.setText("Attempts Left: " + attemptsLeft);

        if (attemptsLeft == 0) {
            feedbackLabel.setText("Out of attempts! The number was " + numberToGuess + ".");
            int playAgain = JOptionPane.showConfirmDialog(frame, "You lost! Do you want to play another round?", "Play Again", JOptionPane.YES_NO_OPTION);
            if (playAgain == JOptionPane.YES_OPTION) {
                initializeGame();
            } else {
                frame.dispose();
            }
        }
    }

    public static void showCongratulationsPanel() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(4, 1));

        JLabel congratsLabel = new JLabel("Congratulations! You guessed it right!", JLabel.CENTER);
        congratsLabel.setFont(new Font("Arial", Font.BOLD, 35));
        congratsLabel.setForeground(Color.green);
        frame.add(congratsLabel);

        JLabel scoreSummaryLabel = new JLabel("Your Total Score: " + totalScore, JLabel.CENTER);
        scoreSummaryLabel.setFont(new Font("Arial", Font.PLAIN, 35));
        frame.add(scoreSummaryLabel);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        JButton playAgainButton = new JButton("Play Again");
        playAgainButton.setPreferredSize(new Dimension(150, 40));
        playAgainButton.setBackground(Color.pink);
        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                frame.setLayout(new GridLayout(7, 1));
                createAndShowGUI();
            }
        });
        buttonPanel.add(playAgainButton);

        JButton exitButton = new JButton("Exit Game");
        exitButton.setPreferredSize(new Dimension(150, 60));
        exitButton.setBackground(Color.red);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        buttonPanel.add(exitButton);

        frame.add(buttonPanel);
        frame.revalidate();
        frame.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NumberGuessingGame::createAndShowGUI);
    }
}
