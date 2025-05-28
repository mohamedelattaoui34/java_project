package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.Timer;
import java.util.TimerTask;

public class DashboardScreen extends JFrame {
    private String userEmail;
    private Timer animationTimer;
    private float titleGlow = 0.0f;
    private boolean glowIncreasing = true;
    private JLabel timeLabel;
    private JLabel statsPreviewLabel;

    public DashboardScreen(String email) {
        this.userEmail = email;
        setTitle("Blocks Master - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Taille considérablement agrandie
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(new Dimension(1200, 800));

        // Panel principal avec dégradé
        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 4),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        // Panel header amélioré
        JPanel headerPanel = createEnhancedHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Contenu principal avec fond animé
        AnimatedCheckerboardPanel contentPanel = new AnimatedCheckerboardPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 70, 50, 70));
        populateEnhancedContentPanel(contentPanel);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Panel footer avec informations supplémentaires
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Démarrer les animations
        startAnimations();
    }

    private JPanel createEnhancedHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(20, 25, 40));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(100, 149, 237)),
                BorderFactory.createEmptyBorder(25, 40, 25, 40)
        ));

        // Panel gauche avec avatar et informations
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        leftPanel.setBackground(new Color(20, 25, 40));

        // Avatar stylisé
        JLabel avatarLabel = createAvatarLabel();

        JPanel textPanel = new JPanel(new GridLayout(3, 1));
        textPanel.setBackground(new Color(20, 25, 40));

        JLabel welcomeLabel = new JLabel(" Bienvenue dans Blocks Master!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(100, 149, 237));

        JLabel emailLabel = new JLabel(" Joueur: " + userEmail);
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emailLabel.setForeground(new Color(180, 180, 190));

        timeLabel = new JLabel(" " + getCurrentTime());
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        timeLabel.setForeground(new Color(160, 160, 170));

        textPanel.add(welcomeLabel);
        textPanel.add(emailLabel);
        textPanel.add(timeLabel);

        leftPanel.add(avatarLabel);
        leftPanel.add(textPanel);

        // Panel droit avec boutons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(new Color(20, 25, 40));

        JButton profileButton = createIconButton("👤", "Profil", new Color(75, 0, 130));
        JButton achievementsButton = createIconButton("🏆", "Succès", new Color(255, 140, 0));
        JButton logoutButton = createIconButton("🚪", "Déconnexion", new Color(220, 53, 69));

        logoutButton.addActionListener(e -> {
            stopAnimations();
            Main.setLoggedInUserEmail(null);
            this.dispose();
            new LoginScreen().setVisible(true);
        });

        rightPanel.add(profileButton);
        rightPanel.add(achievementsButton);
        rightPanel.add(logoutButton);

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private JLabel createAvatarLabel() {
        JLabel avatar = new JLabel("🎮", SwingConstants.CENTER);
        avatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        avatar.setPreferredSize(new Dimension(80, 80));
        avatar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 3),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        avatar.setOpaque(true);
        avatar.setBackground(new Color(40, 45, 60));
        return avatar;
    }

    private JButton createIconButton(String icon, String tooltip, Color bgColor) {
        JButton button = new JButton("<html><center>" + icon + "<br><small>" + tooltip + "</small></center></html>");
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(90, 60));
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setToolTipText(tooltip);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(bgColor.brighter());
                button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(bgColor);
                button.setBorder(null);
            }
        });

        return button;
    }

    private void populateEnhancedContentPanel(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(25, 25, 25, 25);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titre principal animé
        AnimatedTitleLabel titleLabel = new AnimatedTitleLabel("  BLOCKS MASTER  ");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 52));
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 215, 0), 3),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        panel.add(titleLabel, gbc);

        // Sous-titre avec effet
        JLabel subtitleLabel = new JLabel(" Prêt à construire votre légende ? ", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 20));
        subtitleLabel.setForeground(new Color(200, 200, 210));
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 25, 35, 25);
        panel.add(subtitleLabel, gbc);

        // Stats rapides
        JPanel quickStatsPanel = createQuickStatsPanel();
        gbc.gridy = 2;
        gbc.insets = new Insets(15, 25, 25, 25);
        panel.add(quickStatsPanel, gbc);

        // Boutons principaux en grille
        gbc.gridwidth = 1;
        gbc.insets = new Insets(15, 15, 15, 15);

        // Bouton JOUER - Plus grand et central
        JButton playButton = createEnhancedTetrisButton(" JOUER MAINTENANT", new Color(40, 167, 69), "Commencer une nouvelle partie");
        playButton.setFont(new Font("Segoe UI", Font.BOLD, 28));
        playButton.setPreferredSize(new Dimension(420, 90));
        playButton.addActionListener(e -> {
            stopAnimations();
            this.dispose();
            Main.startGame();
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        panel.add(playButton, gbc);

        // Deuxième rangée de boutons
        gbc.gridwidth = 1;
        gbc.gridy = 4;

        JButton statsButton = createEnhancedTetrisButton(" STATISTIQUES", new Color(75, 0, 130), "Voir vos performances");
        statsButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        statsButton.setPreferredSize(new Dimension(280, 70));

        JButton settingsButton = createEnhancedTetrisButton(" PARAMÈTRES", new Color(255, 140, 0), "Configurer le jeu");
        settingsButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        settingsButton.setPreferredSize(new Dimension(280, 70));

        JButton helpButton = createEnhancedTetrisButton(" AIDE", new Color(30, 144, 255), "Comment jouer");
        helpButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        helpButton.setPreferredSize(new Dimension(280, 70));

        gbc.gridx = 0;
        panel.add(statsButton, gbc);
        gbc.gridx = 1;
        panel.add(settingsButton, gbc);
        gbc.gridx = 2;
        panel.add(helpButton, gbc);

        // Troisième rangée de boutons
        gbc.gridy = 5;

        JButton leaderboardButton = createEnhancedTetrisButton("🏆 CLASSEMENT", new Color(220, 20, 60), "Voir le classement");
        JButton achievementsButton = createEnhancedTetrisButton("🎖️ SUCCÈS", new Color(138, 43, 226), "Vos accomplissements");
        JButton themesButton = createEnhancedTetrisButton("🎨 THÈMES", new Color(50, 205, 50), "Personnaliser l'apparence");

        gbc.gridx = 0;
        panel.add(leaderboardButton, gbc);
        gbc.gridx = 1;
        panel.add(achievementsButton, gbc);
        gbc.gridx = 2;
        panel.add(themesButton, gbc);
    }

    private JPanel createQuickStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 20, 0));
        panel.setOpaque(false);

        String[][] stats = {
                {"🎯", "Parties jouées", "42"},
                {"⭐", "Meilleur score", "1798"},
                {"🔥", "Série actuelle", "7"},
                {"🏅", "Niveau", "12"}
        };

        for (String[] stat : stats) {
            JPanel statPanel = createStatPanel(stat[0], stat[1], stat[2]);
            panel.add(statPanel);
        }

        return panel;
    }

    private JPanel createStatPanel(String icon, String label, String value) {
        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.setBackground(new Color(40, 45, 60, 200));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
                BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));

        JLabel labelLabel = new JLabel(label, SwingConstants.CENTER);
        labelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelLabel.setForeground(new Color(180, 180, 190));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(new Color(255, 215, 0));

        panel.add(iconLabel);
        panel.add(labelLabel);
        panel.add(valueLabel);

        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(15, 20, 35));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(100, 149, 237)),
                BorderFactory.createEmptyBorder(15, 30, 15, 30)
        ));

        JLabel versionLabel = new JLabel("Blocks Master v2.0 - © 2024");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        versionLabel.setForeground(new Color(120, 120, 130));

        statsPreviewLabel = new JLabel("🎮 Prêt pour l'action!");
        statsPreviewLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statsPreviewLabel.setForeground(new Color(100, 149, 237));

        JLabel tipsLabel = new JLabel("💡 Astuce: Utilisez les flèches pour déplacer les pièces");
        tipsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tipsLabel.setForeground(new Color(160, 160, 170));

        panel.add(versionLabel, BorderLayout.WEST);
        panel.add(statsPreviewLabel, BorderLayout.CENTER);
        panel.add(tipsLabel, BorderLayout.EAST);

        return panel;
    }

    private JButton createEnhancedTetrisButton(String text, Color bgColor, String tooltip) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.black);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText(tooltip);

        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(bgColor.brighter(), 1),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                )
        ));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(bgColor.brighter());
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.WHITE, 3),
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createRaisedBevelBorder(),
                                BorderFactory.createEmptyBorder(8, 13, 8, 13)
                        )
                ));
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(bgColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createRaisedBevelBorder(),
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(bgColor.brighter(), 1),
                                BorderFactory.createEmptyBorder(10, 15, 10, 15)
                        )
                ));
            }
        });

        return button;
    }

    private String getCurrentTime() {
        return java.time.LocalTime.now().toString().substring(0, 5);
    }

    private void startAnimations() {
        animationTimer = new Timer();
        animationTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    // Animation du titre
                    if (glowIncreasing) {
                        titleGlow += 0.05f;
                        if (titleGlow >= 1.0f) {
                            titleGlow = 1.0f;
                            glowIncreasing = false;
                        }
                    } else {
                        titleGlow -= 0.05f;
                        if (titleGlow <= 0.0f) {
                            titleGlow = 0.0f;
                            glowIncreasing = true;
                        }
                    }

                    // Mise à jour de l'heure
                    if (timeLabel != null) {
                        timeLabel.setText("" + getCurrentTime());
                    }

                    repaint();
                });
            }
        }, 0, 100);
    }

    private void stopAnimations() {
        if (animationTimer != null) {
            animationTimer.cancel();
        }
    }

    @Override
    public void dispose() {
        stopAnimations();
        super.dispose();
    }

    // Panel avec dégradé de fond
    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(15, 20, 35),
                    0, getHeight(), new Color(25, 30, 50)
            );
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    // Fond damier animé
    class AnimatedCheckerboardPanel extends JPanel {
        private final Color color1 = new Color(25, 30, 35, 150);
        private final Color color2 = new Color(35, 40, 45, 150);
        private final int squareSize = 50;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (int y = 0; y < getHeight(); y += squareSize) {
                for (int x = 0; x < getWidth(); x += squareSize) {
                    boolean isEven = (x / squareSize + y / squareSize) % 2 == 0;
                    g2d.setColor(isEven ? color1 : color2);
                    g2d.fillRect(x, y, squareSize, squareSize);
                }
            }
        }
    }

    // Titre avec effet lumineux
    class AnimatedTitleLabel extends JLabel {
        public AnimatedTitleLabel(String text) {
            super(text);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Effet lumineux
            Color glowColor = new Color(255, 215, 0, (int)(100 * titleGlow));
            g2d.setColor(glowColor);
            for (int i = 1; i <= 5; i++) {
                g2d.drawString(getText(), i, getHeight() - 5 + i);
                g2d.drawString(getText(), -i, getHeight() - 5 - i);
            }

            super.paintComponent(g);
        }
    }
}