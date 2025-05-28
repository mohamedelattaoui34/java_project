package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RegisterScreen extends JFrame {
    private JTextField emailField, usernameField;
    private JPasswordField passwordField;
    private JButton registerButton, backButton;
    private JComboBox<String> genderComboBox;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/blockmaster";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private static final Color TETRIS_BLUE = new Color(0, 162, 232);
    private static final Color TETRIS_PURPLE = new Color(146, 43, 140);
    private static final Color BACKGROUND_DARK = new Color(25, 25, 35);
    private static final Color BACKGROUND_LIGHT = new Color(40, 44, 52);
    private static final Color TEXT_WHITE = new Color(255, 255, 255);
    private static final Color TEXT_GRAY = new Color(180, 180, 180);

    public RegisterScreen() {
        setTitle("Blocks Master - Inscription");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, BACKGROUND_DARK, 0, getHeight(), BACKGROUND_LIGHT);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                drawTetrisPattern(g2d);
            }
        };
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(60, 150, 60, 150));
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createFormPanel(), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        add(mainPanel);
        makeWindowDraggable(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Inscription à Blocks Master");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_WHITE);
        headerPanel.add(titleLabel);
        return headerPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        emailField = new JTextField(20);
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        genderComboBox = new JComboBox<>(new String[]{"Homme", "Femme", "Autre"});

        String[] labels = {"Adresse e-mail :", "Nom d'utilisateur :", "Mot de passe :", "Genre :"};
        JComponent[] fields = {emailField, usernameField, passwordField, genderComboBox};

        gbc.gridx = 0;
        gbc.gridy = 0;
        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setForeground(TEXT_GRAY);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            formPanel.add(label, gbc);
            gbc.gridx = 1;
            fields[i].setFont(new Font("Segoe UI", Font.PLAIN, 16));
            formPanel.add(fields[i], gbc);
            gbc.gridy++;
            gbc.gridx = 0;
        }

        return formPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        registerButton = new JButton("S'inscrire");
        backButton = new JButton("Retour");

        styleButton(registerButton, TETRIS_PURPLE);
        styleButton(backButton, TETRIS_BLUE);

        registerButton.addActionListener(e -> registerUser());
        backButton.addActionListener(e -> dispose());

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        return buttonPanel;
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.black);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void registerUser() {
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String gender = (String) genderComboBox.getSelectedItem();

        if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO users (email, username, password, gender) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, username);
                stmt.setString(3, password);
                stmt.setString(4, gender);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Inscription réussie !");
                dispose(); // Ferme la fenêtre après inscription
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'inscription.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void drawTetrisPattern(Graphics2D g2d) {
        g2d.setColor(new Color(255, 255, 255, 30));
        int size = 30;
        for (int x = 0; x < getWidth(); x += size) {
            for (int y = 0; y < getHeight(); y += size) {
                if ((x + y) / size % 2 == 0) {
                    g2d.fillRect(x, y, size - 2, size - 2);
                }
            }
        }
    }

    private void makeWindowDraggable(Component component) {
        final Point[] mouseDownCompCoords = {null};
        component.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseDownCompCoords[0] = e.getPoint();
            }
        });
        component.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point currCoords = e.getLocationOnScreen();
                setLocation(currCoords.x - mouseDownCompCoords[0].x, currCoords.y - mouseDownCompCoords[0].y);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RegisterScreen().setVisible(true);
        });
    }
}
