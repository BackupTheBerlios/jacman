package andyr.jacman.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import andyr.jacman.utils.JacmanUtils;


public class HeaderPanel extends JPanel {

    private ImageIcon icon;

    public HeaderPanel(String image, String title, String subTitle1, String subTitle2) {
        super(new BorderLayout());

        this.icon = JacmanUtils.loadIcon(image);

        JPanel pnlTitles = new JPanel(new GridLayout(3, 1));
        pnlTitles.setOpaque(false);
        pnlTitles.setBorder(new EmptyBorder(12, 0, 12, 0));

        JLabel lblMainTitle = new JLabel(title);
        Font police = lblMainTitle.getFont().deriveFont(Font.BOLD);
        lblMainTitle.setFont(police);
        lblMainTitle.setBorder(new EmptyBorder(0, 12, 0, 0));
        pnlTitles.add(lblMainTitle);

        JLabel lblSubTitle;
        pnlTitles.add(lblSubTitle = new JLabel(subTitle1));
        lblSubTitle.setBorder(new EmptyBorder(0, 24, 0, 0));
        pnlTitles.add(lblSubTitle = new JLabel(subTitle2));
        lblSubTitle.setBorder(new EmptyBorder(0, 24, 0, 0));

        lblSubTitle = new JLabel(this.icon);
        lblSubTitle.setBorder(new EmptyBorder(0, 64, 0, 12));

        add(BorderLayout.WEST, pnlTitles);
        add(BorderLayout.EAST, lblSubTitle);

        setPreferredSize(new Dimension((int) getPreferredSize().getWidth(), this.icon.getIconHeight() + 24));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!isOpaque()) {
            return;
        }

        Color control = UIManager.getColor("control");
        int width = getWidth();
        int height = getHeight();

        Graphics2D g2 = (Graphics2D) g;
        Paint storedPaint = g2.getPaint();
        g2.setPaint(new GradientPaint(this.icon.getIconWidth(), 0, Color.white, width, 0, control));
        g2.fillRect(0, 0, width, height);
        g2.setPaint(storedPaint);
    }
}

