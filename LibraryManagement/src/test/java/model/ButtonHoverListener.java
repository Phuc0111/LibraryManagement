package model;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

public class ButtonHoverListener extends MouseAdapter {
    private JButton button;
    private Color hoverColor;
    private Color originalColor;

    public ButtonHoverListener(JButton button, Color hoverColor, Color originalColor) {
        this.button = button;
        this.hoverColor = hoverColor;
        this.originalColor = originalColor;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        button.setBackground(hoverColor);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        button.setBackground(originalColor);
    }
}
