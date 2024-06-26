package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;

public class IconButton extends JButton {

    private ImageIcon icon;
    private ImageIcon hoverIcon;

    public IconButton(ImageIcon icon, ImageIcon hoverIcon) {
        this.icon = icon;
        this.hoverIcon = hoverIcon;

        setIcon(icon);
        setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add a mouse listener to change the icon when hovered
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setIcon(hoverIcon);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setIcon(icon);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isPressed()) {
            g.setColor(Color.LIGHT_GRAY);
        } else {
            g.setColor(getBackground());
        }
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); // Change the radius 15 to adjust the rounded corners
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Không cần phải vẽ viền cho nút
    }

    @Override
    public boolean contains(int x, int y) {
        // Kiểm tra xem điểm x, y có nằm trong hình dạng bo tròn của nút hay không
        if (icon == null) {
            return false;
        }
        int imageWidth = icon.getIconWidth();
        int imageHeight = icon.getIconHeight();
        int diameter = Math.min(imageWidth, imageHeight);
        int radius = diameter / 2;

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        int dx = Math.abs(x - centerX);
        int dy = Math.abs(y - centerY);

        return dx * dx + dy * dy <= radius * radius;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(icon.getIconWidth(), icon.getIconHeight());
    }

    public void setHoverIcon(ImageIcon hoverIcon) {
        this.hoverIcon = hoverIcon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
        super.setIcon(icon);
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public ImageIcon getHoverIcon() {
        return hoverIcon;
    }
}
