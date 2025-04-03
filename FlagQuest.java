import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.HashSet;

public class FlagQuest extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel, landingPage, gamePanel;
    private JLabel flagLabel, timerLabel, attemptLabel;
    private JTextField guessField;
    private JButton submitButton, startButton;
    private Timer timer;
    private int timeLeft, attemptsLeft, level, flagIndex;
    private final int maxLevel = 3;
    private String[] flags;
    private String currentFlag;
    private final HashSet<String> guessedFlags = new HashSet<>();

    public FlagQuest() {
        setTitle("Flag Guessing Game");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        createLandingPage();
        createGamePanel();

        mainPanel.add(landingPage, "Landing");
        mainPanel.add(gamePanel, "Game");

        add(mainPanel);
        setVisible(true);
    }

    private void createLandingPage() {
        landingPage = new JPanel(new GridBagLayout());
        landingPage.setBackground(new Color(10, 31, 68));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel welcomeLabel = new JLabel("Welcome to Flag Quest", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);

        gbc.gridy = 1;
        startButton = new JButton("Start");
        startButton.setPreferredSize(new Dimension(120, 40));
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setBackground(new Color(76, 175, 80));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorderPainted(false);
        startButton.setOpaque(true);

        startButton.addActionListener(e -> showDifficultySelection());

        landingPage.add(welcomeLabel, gbc);
        gbc.gridy++;
        landingPage.add(startButton, gbc);
    }

    private void showDifficultySelection() {
        JPanel difficultyPanel = new JPanel(new GridBagLayout());
        difficultyPanel.setBackground(new Color(200, 220, 255)); 
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        JLabel titleLabel = new JLabel("Select Difficulty");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        gbc.gridy = 0;
        difficultyPanel.add(titleLabel, gbc);

        JButton easyButton = new JButton("Easy");
        easyButton.setBackground(new Color(144, 238, 144));
        gbc.gridy = 1;
        difficultyPanel.add(easyButton, gbc);
    
        JButton normalButton = new JButton("Normal");
        normalButton.setBackground(new Color(255, 255, 153)); 
        gbc.gridy = 2;
        difficultyPanel.add(normalButton, gbc);
    
        JButton hardButton = new JButton("Hard");
        hardButton.setBackground(new Color(255, 182, 193)); 
        gbc.gridy = 3;
        difficultyPanel.add(hardButton, gbc);
    
        JButton backButton = new JButton("Back");
        gbc.gridy = 4;
        difficultyPanel.add(backButton, gbc);
    
        easyButton.addActionListener(e -> openGameWindow("Easy"));
        normalButton.addActionListener(e -> openGameWindow("Normal"));
        hardButton.addActionListener(e -> openGameWindow("Hard"));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Landing")); 
    
        mainPanel.add(difficultyPanel, "Difficulty"); 
        cardLayout.show(mainPanel, "Difficulty"); 
    }
    
    private void openGameWindow(String difficulty) {   
        setLevel(difficulty.equals("Easy") ? 1 : difficulty.equals("Normal") ? 2 : 3);
        cardLayout.show(mainPanel, "Game");
    }

    private void createGamePanel() {
        gamePanel = new JPanel(new GridBagLayout());
        gamePanel.setBackground(new Color(200, 220, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx = 0; 
        gbc.insets = new Insets(10, 10, 10, 10); 
        
        flagLabel = new JLabel();
        gbc.gridy = 0; 
        gbc.anchor = GridBagConstraints.CENTER;
        gamePanel.add(flagLabel, gbc);
        
        guessField = new JTextField(15);
        gbc.gridy = 1; 
        gamePanel.add(guessField, gbc);
        
        submitButton = new JButton("Submit Guess");
        gbc.gridy = 2; 
        gamePanel.add(submitButton, gbc);
        
        timerLabel = new JLabel();
        gbc.gridy = 3;
        gamePanel.add(timerLabel, gbc);
    
        attemptLabel = new JLabel();
        gbc.gridy = 4;
        gamePanel.add(attemptLabel, gbc);
    
        submitButton.addActionListener(e -> checkGuess());
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

        if (level == 1) {
            timeLeft = 30;
            timerLabel.setText("Time Left: 30");
            timerLabel.setVisible(true);
            attemptLabel.setText("");
            startTimer();
        } else {
            attemptsLeft = (level == 2) ? 5 : 3;
            attemptLabel.setText("Attempts Left: " + attemptsLeft);
            timerLabel.setVisible(false);
        }

        showNextFlag();
    }

    private void loadFlags() {
        flags = switch (level) {
            case 1 ->
                new String[]{"Phillipines.png", "United Kindom.png", "USA.png",
                    "Canada.png", "USA.png"};
            case 2 ->
                new String[]{"Vietnam.png", "FlagImages/Spain.png", "FlagImages/Thailand.png",
                    "FlagImages/Brazil.png", "FlagImages/Turkey.png", "FlagImages/Venezuela.png", "FlagImages/Portugal.png",
                    "FlagImages/United Kingdom.png"};
            case 3 ->
                new String[]{"FlagImages/Uganda.png", "FlagImages/Somalia.png", "FlagImages/Slovenia.png",
                    "FlagImages/Sri Lanka.png", "FlagImages/South Sudan.png", "FlagImages/Uzbekistan.png", "FlagImages/Serbia.png",
                    "FlagImages/Tanzania.png", "FlagImages/Syria.png", "FlagImages/Russia.png"};
            default ->
                new String[]{};
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
                Image resizedImg = img.getScaledInstance(370, 200, Image.SCALE_SMOOTH); // Resize to 360x200 pixels
                flagLabel.setIcon(new ImageIcon(resizedImg));
    
                currentFlag = currentFlag.substring(currentFlag.lastIndexOf("/") + 1).replace(".png", "");
    
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading flag: " + currentFlag);
            }
        } else {
            JOptionPane.showMessageDialog(this, "All flags guessed! Moving to the next difficulty level");
            setLevel(level + 1);
        }
    }
    
    private void checkGuess() {
        String guess = guessField.getText().trim();

        if (guess.equalsIgnoreCase(currentFlag)) {
            guessedFlags.add(currentFlag);
            JOptionPane.showMessageDialog(null, "Correct! You have guessed " + guessedFlags.size() + " flags.");
            showNextFlag();
        } else {
            if (level > 1) {
                attemptsLeft--;
                attemptLabel.setText("Attempts Left: " + attemptsLeft);
                if (attemptsLeft <= 0) {
                    JOptionPane.showMessageDialog(null, "Out of attempts! The correct flag was: " + currentFlag);
                    setLevel(level);
                    return;
                }
            }
            JOptionPane.showMessageDialog(null, "Incorrect! Try again.");
        }
        guessField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FlagQuest::new);
    }
}
