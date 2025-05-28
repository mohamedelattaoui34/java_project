package main;

import javax.swing.*;
import java.awt.*;

public class Main {
    private static String loggedInUserEmail = null;
    private static GameFacade gameFacade;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                showLoginScreen();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Erreur lors du démarrage de l'application",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private static void showLoginScreen() {
        LoginScreen loginScreen = new LoginScreen();
        loginScreen.setVisible(true);
    }

    public static void setLoggedInUserEmail(String email) {
        loggedInUserEmail = email;
    }

    public static String getLoggedInUserEmail() {
        return loggedInUserEmail != null ? loggedInUserEmail : "Invité";
    }

    public static void startGame() {
        SwingUtilities.invokeLater(() -> {
            try {
                JFrame window = createMainWindow();
                setupWindowContent(window);

                window.pack();
                window.setLocationRelativeTo(null);
                window.setVisible(true);

                initializeGame();
            } catch (Exception e) {
                e.printStackTrace();
                showErrorDialog("Erreur lors du lancement du jeu");
            }
        });
    }

    private static JFrame createMainWindow() {
        JFrame window = new JFrame("Blocks Master - " + getLoggedInUserEmail());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false); // Désactivé pour un jeu Tetris

        try {
            window.setIconImage(new ImageIcon("icon.png").getImage());
        } catch (Exception e) {
            System.out.println("Icône non trouvée, utilisation par défaut");
        }

        return window;
    }

    private static void setupWindowContent(JFrame window) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        // Panel d'information joueur
        JPanel playerPanel = createPlayerInfoPanel();
        mainPanel.add(playerPanel, BorderLayout.NORTH);

        // Panel de jeu
        JPanel gamePanel = createGamePanel();
        mainPanel.add(gamePanel, BorderLayout.CENTER);

        window.add(mainPanel);
    }

    private static JPanel createPlayerInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(200, 220, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel playerLabel = new JLabel("Joueur: " + getLoggedInUserEmail());
        playerLabel.setFont(new Font("Arial", Font.BOLD, 14));

        panel.add(playerLabel, BorderLayout.WEST);

        // Vous pouvez ajouter d'autres infos ici (score, niveau, etc.)

        return panel;
    }

    private static JPanel createGamePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        gameFacade = getGameFacadeInstance();
        panel.add(gameFacade, BorderLayout.CENTER);

        return panel;
    }

    private static void initializeGame() {
        if (gameFacade != null) {
            gameFacade.launchGame();
        }
    }

    private static GameFacade getGameFacadeInstance() {
        MinoPickingStrategy strategy = new RandomMinoPickingStrategy();
        GameFacade gf = new GameFacade(strategy);
        gf.setMinoPickingStrategy(new SequentialMinoPickingStrategy());
        return gf;
    }

    private static void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null,
                message,
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
    }
}