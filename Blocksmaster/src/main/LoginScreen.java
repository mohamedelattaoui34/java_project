package main;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;

public class LoginScreen extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    // Couleurs du thème Tetris
    private static final Color TETRIS_BLUE = new Color(17, 70, 221);
    private static final Color TETRIS_PURPLE = new Color(146, 43, 140);
    private static final Color TETRIS_ORANGE = new Color(255, 127, 39);
    private static final Color TETRIS_GREEN = new Color(34, 177, 76);
    private static final Color BACKGROUND_DARK = new Color(25, 25, 35);
    private static final Color BACKGROUND_LIGHT = new Color(40, 44, 52);
    private static final Color TEXT_WHITE = new Color(241, 236, 236);
    private static final Color TEXT_GRAY = new Color(180, 180, 180);

    public LoginScreen() {
        setTitle("Blocks Master - startGame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true); // Supprime la barre de titre pour un look moderne

        // Forme arrondie pour la fenêtre
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));

        // Panel principal avec gradient
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Gradient de fond
                GradientPaint gradient = new GradientPaint(
                        0, 0, BACKGROUND_DARK,
                        0, getHeight(), BACKGROUND_LIGHT
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Motif Tetris en arrière-plan
                drawTetrisPattern(g2d);
            }
        };
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Header avec titre et bouton fermer
        JPanel headerPanel = createHeaderPanel();

        // Panel central avec le formulaire
        JPanel formPanel = createFormPanel();














        // Panel des boutons
        JPanel buttonPanel = createButtonPanel();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Rendre la fenêtre draggable
        makeWindowDraggable(mainPanel);
    }

    private void drawTetrisPattern(Graphics2D g2d) {
        g2d.setColor(new Color(255, 255, 255, 10));
        int blockSize = 20;

        // Dessiner quelques formes Tetris en arrière-plan
        // Forme L
        g2d.fillRect(50, 100, blockSize, blockSize);
        g2d.fillRect(50, 120, blockSize, blockSize);
        g2d.fillRect(50, 140, blockSize, blockSize);
        g2d.fillRect(70, 140, blockSize, blockSize);

        // Forme T
        g2d.fillRect(350, 200, blockSize, blockSize);
        g2d.fillRect(330, 220, blockSize, blockSize);
        g2d.fillRect(350, 220, blockSize, blockSize);
        g2d.fillRect(370, 220, blockSize, blockSize);

        // Forme Z
        g2d.fillRect(80, 400, blockSize, blockSize);
        g2d.fillRect(100, 400, blockSize, blockSize);
        g2d.fillRect(100, 420, blockSize, blockSize);
        g2d.fillRect(120, 420, blockSize, blockSize);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Logo/Titre
        JLabel titleLabel = new JLabel("BLOCKS MASTER", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_WHITE);

        JLabel subtitleLabel = new JLabel("Connectez-vous pour jouer", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_GRAY);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);

        // Bouton fermer
        JButton closeButton = createCloseButton();

        headerPanel.add(titlePanel, BorderLayout.CENTER);
        headerPanel.add(closeButton, BorderLayout.EAST);

        return headerPanel;
    }

    private JButton createCloseButton() {
        JButton closeButton = new JButton("×");
        closeButton.setFont(new Font("Arial", Font.BOLD, 20));
        closeButton.setForeground(TEXT_GRAY);
        closeButton.setBackground(null);
        closeButton.setBorder(null);
        closeButton.setFocusPainted(false);
        closeButton.setPreferredSize(new Dimension(30, 30));
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeButton.setForeground(TETRIS_ORANGE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeButton.setForeground(TEXT_GRAY);
            }
        });

        closeButton.addActionListener(e -> System.exit(0));

        return closeButton;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Email
        JLabel emailLabel = new JLabel("Adresse email");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setForeground(TEXT_WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(emailLabel, gbc);

        emailField = createStyledTextField();
        emailField.setPreferredSize(new Dimension(300, 45));
        gbc.gridy = 1;
        formPanel.add(emailField, gbc);

        // Mot de passe
        JLabel passwordLabel = new JLabel("Mot de passe");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setForeground(TEXT_WHITE);
        gbc.gridy = 2;
        formPanel.add(passwordLabel, gbc);

        passwordField = createStyledPasswordField();
        passwordField.setPreferredSize(new Dimension(300, 45));
        gbc.gridy = 3;
        formPanel.add(passwordField, gbc);

        return formPanel;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        styleInputField(textField);
        return textField;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        styleInputField(passwordField);
        return passwordField;
    }

    private void styleInputField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setForeground(TEXT_WHITE);
        field.setBackground(new Color(60, 63, 70));
        field.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(10, new Color(80, 80, 90)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        field.setCaretColor(TETRIS_BLUE);

        // Effet de focus
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new RoundedBorder(10, TETRIS_BLUE),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new RoundedBorder(10, new Color(80, 80, 90)),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
            }
        });
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Bouton de connexion
        loginButton = createStyledButton("SE CONNECTER", TETRIS_BLUE, true);
        loginButton.addActionListener(e -> attemptLogin());
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(loginButton, gbc);

        // Bouton d'inscription
        registerButton = createStyledButton("S'INSCRIRE", TETRIS_PURPLE, false);
        registerButton.addActionListener(e -> showRegistration());
        gbc.gridy = 1;
        buttonPanel.add(registerButton, gbc);

        return buttonPanel;
    }

    private JButton createStyledButton(String text, Color color, boolean filled) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(300, 50));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (filled) {
            button.setBackground(color);
            button.setForeground(Color.black);
            button.setBorder(new RoundedBorder(25, color));
        } else {
            button.setBackground(new Color(0, 0, 0, 0)); // Transparent
            button.setForeground(color);
            button.setBorder(new RoundedBorder(25, color));
            button.setOpaque(false);
        }

        // Effets hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (filled) {
                    button.setBackground(color.brighter());
                } else {
                    button.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (filled) {
                    button.setBackground(color);
                } else {
                    button.setBackground(new Color(0, 0, 0, 0)); // Transparent
                }
            }
        });

        return button;
    }

    private void makeWindowDraggable(JPanel panel) {
        final Point[] mouseClickPoint = {null};

        panel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseClickPoint[0] = e.getPoint();
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (mouseClickPoint[0] != null) {
                    Point currentLocation = LoginScreen.this.getLocation();
                    LoginScreen.this.setLocation(
                            currentLocation.x + e.getX() - mouseClickPoint[0].x,
                            currentLocation.y + e.getY() - mouseClickPoint[0].y
                    );
                }
            }
        });
    }








    private void attemptLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Basic validation
        if (email.isEmpty() || password.isEmpty()) {
            showStyledMessage("Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            showStyledMessage("Invalid email format", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Show loading state
        loginButton.setText("LOGGING IN...");
        loginButton.setEnabled(false);

        // Database connection details
        final String DB_URL = "jdbc:mysql://localhost:3306/blockmaster";
        final String DB_USER = "root"; // Change to your MySQL username
        final String DB_PASSWORD = ""; // Change to your MySQL password

        // Perform database check in a background thread
        new Thread(() -> {
            try {
                // Load MySQL driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Connect to database
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

                // Prepare SQL query
                String sql = "SELECT * FROM users WHERE email = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, email);

                // Execute query
                ResultSet result = statement.executeQuery();

                // Check if user exists and password matches
                if (result.next()) {
                    String storedPassword = result.getString("password");

                    // In a real application, use BCrypt for password hashing
                    // For now, we'll do plain text comparison (NOT SECURE FOR PRODUCTION)
                    if (password.equals(storedPassword)) {
                        // Login successful
                        SwingUtilities.invokeLater(() -> {
                            showStyledMessage("Login successful!", "Welcome", JOptionPane.INFORMATION_MESSAGE);
                            Main.setLoggedInUserEmail(email);
                            LoginScreen.this.dispose();
                            new DashboardScreen(email).setVisible(true);
                        });
                    } else {
                        // Wrong password
                        SwingUtilities.invokeLater(() -> {
                            showStyledMessage("Invalid email or password", "Error", JOptionPane.ERROR_MESSAGE);
                            loginButton.setText("LOGIN");
                            loginButton.setEnabled(true);
                        });
                    }
                } else {
                    // User not found
                    SwingUtilities.invokeLater(() -> {
                        showStyledMessage("User not found", "Error", JOptionPane.ERROR_MESSAGE);
                        loginButton.setText("LOGIN");
                        loginButton.setEnabled(true);
                    });
                }

                // Close resources
                result.close();
                statement.close();
                connection.close();

            } catch (ClassNotFoundException e) {
                SwingUtilities.invokeLater(() -> {
                    showStyledMessage("MySQL driver not found", "Error", JOptionPane.ERROR_MESSAGE);
                    loginButton.setText("LOGIN");
                    loginButton.setEnabled(true);
                });
                e.printStackTrace();
            } catch (SQLException e) {
                SwingUtilities.invokeLater(() -> {
                    showStyledMessage("Database connection error", "Error", JOptionPane.ERROR_MESSAGE);
                    loginButton.setText("LOGIN");
                    loginButton.setEnabled(true);
                });
                e.printStackTrace();
            }
        }).start();
    }


    private void showStyledMessage(String message, String title, int messageType) {
        // Créer un JOptionPane personnalisé avec le style sombre
        UIManager.put("OptionPane.background", BACKGROUND_LIGHT);
        UIManager.put("Panel.background", BACKGROUND_LIGHT);
        UIManager.put("OptionPane.messageForeground", TEXT_WHITE);

        JOptionPane.showMessageDialog(this, message, title, messageType);

        // Restaurer les couleurs par défaut
        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
        UIManager.put("OptionPane.messageForeground", null);
    }

    private void showRegistration() {
        SwingUtilities.invokeLater(() -> {
            RegisterScreen registerScreen = new RegisterScreen();
            registerScreen.setVisible(true);
        });
    }


    private void launchGame() {
        SwingUtilities.invokeLater(() -> {
            try {
                JFrame window = new JFrame("Blocks master");
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                window.setResizable(true);

                try {
                    ImageIcon icon = new ImageIcon("icon.png");
                    window.setIconImage(icon.getImage());
                } catch (Exception e) {
                    System.out.println("Icon not found, using default");
                }

                window.getContentPane().setBackground(new Color(240, 240, 240));

                MinoPickingStrategy strategy = new RandomMinoPickingStrategy();
                GameFacade gf = new GameFacade(strategy);
                gf.setMinoPickingStrategy(new SequentialMinoPickingStrategy());

                JPanel container = new JPanel(new BorderLayout());
                container.setBackground(new Color(240, 240, 240));
                container.add(gf, BorderLayout.CENTER);

                JPanel outerContainer = new JPanel(new BorderLayout());
                outerContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                outerContainer.setBackground(new Color(240, 240, 240));
                outerContainer.add(container, BorderLayout.CENTER);

                window.add(outerContainer);
                window.setPreferredSize(new Dimension(800, 600));
                window.pack();
                window.setLocationRelativeTo(null);
                window.getRootPane().setBorder(BorderFactory.createLineBorder(new Color(100, 100, 255), 2));
                window.setVisible(true);

                gf.launchGame();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Classe pour créer des bordures arrondies
    static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;

        public RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(x + 1, y + 1, width - 2, height - 2, radius, radius);
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(2, 2, 2, 2);
        }
    }
}