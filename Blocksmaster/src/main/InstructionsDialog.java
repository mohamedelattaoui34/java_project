package main;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class InstructionsDialog {
    private static final Color BACKGROUND_COLOR = new Color(25, 29, 35);
    private static final Color PRIMARY_COLOR = new Color(72, 219, 251);
    private static final Color SECONDARY_COLOR = new Color(255, 184, 108);
    private static final Color TEXT_COLOR = new Color(240, 240, 240);
    private static final Color HOVER_COLOR = new Color(255, 204, 128);

    public static void showInstructions(Component parent) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel iconLabel = new JLabel("🧊", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(iconLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel title = new JLabel("BLOCK MASTER - CONTROLS");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(PRIMARY_COLOR);
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        String[][] instructions = {
                {"↑", "Rotate piece"},
                {"↓", "Speed up fall"},
                {"← →", "Move left/right"},
                {"Space", "Hard drop"},
                {"P", "Pause game"},
                {"R", "Restart game"}
        };

        for (String[] item : instructions) {
            JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
            itemPanel.setBackground(BACKGROUND_COLOR);
            itemPanel.setMaximumSize(new Dimension(350, 35));

            JButton keyButton = new JButton(item[0]);
            keyButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
            keyButton.setForeground(Color.BLACK);
            keyButton.setBackground(SECONDARY_COLOR);
            keyButton.setFocusPainted(false);
            keyButton.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
            keyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Hover effect
            keyButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    keyButton.setBackground(HOVER_COLOR);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    keyButton.setBackground(SECONDARY_COLOR);
                }
            });

            JLabel descLabel = new JLabel(item[1]);
            descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            descLabel.setForeground(TEXT_COLOR);

            itemPanel.add(keyButton);
            itemPanel.add(descLabel);
            panel.add(itemPanel);
            panel.add(Box.createRigidArea(new Dimension(0, 12)));
        }

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel footer = new JLabel("Press OK to start your adventure!");
        footer.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        footer.setForeground(new Color(180, 180, 180));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(footer);

        // Custom background for dialog
        UIManager.put("OptionPane.background", BACKGROUND_COLOR);
        UIManager.put("Panel.background", BACKGROUND_COLOR);

        JOptionPane.showMessageDialog(
                parent,
                panel,
                "How to Play",
                JOptionPane.PLAIN_MESSAGE,
                createCustomIcon()
        );
    }

    private static Icon createCustomIcon() {
        // Crée une icône "🎮" rendue dans une image pour éviter NullPointerException
        int size = 48;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, size - 10));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.BLACK);
        g2.drawString("🎮", 5, size - 10);
        g2.dispose();
        return new ImageIcon(image);
    }
}
