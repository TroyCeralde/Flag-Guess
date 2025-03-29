import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.HashSet;

public class FlagGuessingGame extends JFrame {

    private final JLabel flagLabel;
    private final JTextField guessField;
    private final JButton submitButton;
    private final JLabel timerLabel;
    private final JLabel attemptLabel;
    private Timer timer;
    private int timeLeft;
    private int attemptsLeft;
    private String[] flags;
    private String currentFlag;
    private int level;
    private final int maxLevel = 3;
    private int flagIndex;
    private final HashSet<String> guessedFlags = new HashSet<>();

    public FlagGuessingGame() {
        setTitle("Flag Guessing Game");
        setSize(1920, 1200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        flagLabel = new JLabel();
        guessField = new JTextField(15);
        submitButton = new JButton("Submit Guess");
        timerLabel = new JLabel(); // Timer label without default text
        attemptLabel = new JLabel(); // Attempt label without default text

        add(flagLabel);
        add(guessField);
        add(submitButton);
        add(timerLabel);
        add(attemptLabel);

        submitButton.addActionListener(e -> checkGuess());

        setLevel(1);
        setVisible(true);
    }

    private void setLevel(int newLevel) {
        if (newLevel > maxLevel) {
            JOptionPane.showMessageDialog(this, "Congratulations! You completed all levels.");
            return;
        }

        this.level = newLevel;
        guessedFlags.clear();
        flagIndex = 0;
        loadFlags();

        switch (level) {
            case 1:
                timeLeft = 30;
                timerLabel.setText("Time Left: 30");
                attemptLabel.setText(""); // Hide attempt counter
                startTimer();
                break;
            case 2:
                attemptsLeft = 5;
                attemptLabel.setText("Attempts Left: " + attemptsLeft);
                timerLabel.setText(""); // Hide timer
                break;
            case 3:
                attemptsLeft = 3;
                attemptLabel.setText("Attempts Left: " + attemptsLeft);
                timerLabel.setText(""); // Hide timer
                break;
        }
        showNextFlag();
    }

    private void loadFlags() {
        flags = switch (level) {
            case 1 -> new String[]{
                "FlagImages" + File.separator + "Philippines.png",
                "FlagImages" + File.separator + "South Korea.png",
                "FlagImages" + File.separator + "India.png",
                "FlagImages" + File.separator + "China.png",
                "FlagImages" + File.separator + "USA.png",
            };
            case 2 -> new String[]{
                "FlagImages" + File.separator + "Vietnam.png",
                "FlagImages" + File.separator + "Spain.png",
                "FlagImages" + File.separator + "Thailand.png",
                "FlagImages" + File.separator + "Brazil.png",
                "FlagImages" + File.separator + "Turkey.png",
                "FlagImages" + File.separator + "Venezuela.png",
                "FlagImages" + File.separator + "Portugal.png",
                "FlagImages" + File.separator + "United Kingdom.png",
            };
            case 3 -> new String[]{
                "FlagImages" + File.separator + "Uganda.png",
                "FlagImages" + File.separator + "Somalia.png",
                "FlagImages" + File.separator + "Slovenia.png",
                "FlagImages" + File.separator + "Sri Lanka.png",
                "FlagImages" + File.separator + "South Sudan.png",
                "FlagImages" + File.separator + "Uzbekistan.png",
                "FlagImages" + File.separator + "Serbia.png",
                "FlagImages" + File.separator + "Tanzania.png",
                "FlagImages" + File.separator + "Syria.png",
                "FlagImages" + File.separator + "Russia.png",
            };
            default -> new String[]{};
        };
    }

    private void startTimer() {
        if (timer != null) {
            timer.stop();
        }
        timer = new Timer(1000, (ActionEvent e) -> {
            timeLeft--;
            timerLabel.setText("Time Left: " + timeLeft);
            if (timeLeft <= 0) {
                timer.stop();
                JOptionPane.showMessageDialog(null, "Time's up! The flag was: " + currentFlag);
                setLevel(level);
            }
        });
        timer.start();
    }

    private void showNextFlag() {
        if (flagIndex < flags.length) {
            currentFlag = flags[flagIndex++];
            try {
                File file = new File(currentFlag);
                if (!file.exists()) {
                    throw new IOException("Image file not found: " + currentFlag);
                }
                Image img = ImageIO.read(file);
                flagLabel.setIcon(new ImageIcon(img.getScaledInstance(100, 50, Image.SCALE_SMOOTH)));
                currentFlag = currentFlag.substring(currentFlag.lastIndexOf(File.separator) + 1)
                        .replace(".png", "");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading flag: " + currentFlag);
            }
        } else {
            if (level < maxLevel) {
                JOptionPane.showMessageDialog(this, "All flags guessed! Moving to Level " + (level + 1));
                setLevel(level + 1);
            } else {
                JOptionPane.showMessageDialog(this, "You have completed the game!");
            }
        }
    }

    private void checkGuess() {
        String guess = guessField.getText().trim();
        if (guess.equalsIgnoreCase(currentFlag)) {
            guessedFlags.add(currentFlag);
            JOptionPane.showMessageDialog(null, "Correct! You have guessed " + guessedFlags.size() + " flags.");
            showNextFlag();
        } else {
            if (level == 1) {
                JOptionPane.showMessageDialog(null, "Incorrect! Try again.");
            } else {
                attemptsLeft--;
                if (attemptsLeft <= 0) {
                    JOptionPane.showMessageDialog(null, "Out of attempts! The flag was: " + currentFlag);
                    setLevel(level);
                } else {
                    JOptionPane.showMessageDialog(null, "Incorrect! Try again. " + attemptsLeft + " attempts left.");
                    attemptLabel.setText("Attempts Left: " + attemptsLeft);
                }
            }
        }
        guessField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FlagGuessingGame::new);
    }
}
