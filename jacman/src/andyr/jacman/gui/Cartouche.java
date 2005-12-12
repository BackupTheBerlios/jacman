/*
 * Classe créée le 16 janv. 2005
 *
 * La classe Cartouche affiche un cartouche dégradé contenant une image et un texte.
 */
package andyr.jacman.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextLayout;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class Cartouche extends JPanel implements MouseListener {
	private ImageIcon icon;
	private static final float blurry = 1.0f / 25.0f;
	private static final float[] BLUR_KERNEL = new float[] {
			blurry, blurry, blurry, blurry, blurry,
			blurry, blurry, blurry, blurry, blurry,
			blurry, blurry, blurry, blurry, blurry,
			blurry, blurry, blurry,	blurry, blurry,
			blurry, blurry, blurry, blurry, blurry };
	private boolean mouseOver = false;
	private boolean mousePressed = false;
	private List listeners;

	public Cartouche(ImageIcon icon, String text) {
		this.listeners = new LinkedList();

		setLayout(new BorderLayout());
		setOpaque(false);
		setEventListeners();
		
		this.icon = icon;

		JLabel il;
        JLabel tl;
		add(BorderLayout.WEST, il = new JLabel(icon));
		il.setBorder(new EmptyBorder(9, 18, 9, 0));
		add(BorderLayout.CENTER, tl = new JLabel(text));
		tl.setBorder(new EmptyBorder(0, 20, 0, 6+ 18));
		tl.setFont(new Font("Tahoma", Font.PLAIN, 18));

        //System.out.println("Icon label width: " + il.getPreferredSize().getWidth());
        //System.out.println("Icon label height: " + il.getPreferredSize().getHeight());
        
        //System.out.println("Text label width: " + tl.getPreferredSize().getWidth());
        //System.out.println("Text label height: " + tl.getPreferredSize().getHeight());
        
        //System.out.println("Panel width: " + this.getPreferredSize().getWidth());
        //System.out.println("Panel height: " + this.getPreferredSize().getHeight());
		//setPreferredSize(new Dimension(250, 66));
		//setMaximumSize(new Dimension(250, 66));
	}

	private void setEventListeners() {
		addMouseListener(this);
	}

	public void paintComponent(Graphics g) {
        Color controle = UIManager.getColor("control");
        int width = getWidth() - 6;
        int height = getHeight() - 6;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        RoundRectangle2D rect = new RoundRectangle2D.Double(0, 0, width, height, 12, 12);
       	drawShadow(g2, rect);

        Paint pinceau  = g2.getPaint();
        if (!mouseOver) {
        	g2.setPaint(new GradientPaint(this.icon.getIconWidth(), 0, Color.white, width, 0, controle));
        } else {
        	g2.setColor(Color.white);
        }

        g2.translate(2, 2);
        if (mousePressed) {
        	g2.translate(1, 1);	
        }

        g2.fill(rect);
        g2.setPaint(pinceau);
        g2.translate(-2, -2);
	}

	public void addActionListener(ActionListener listener) {
		listeners.add(listener);
	}
	
	protected void fireActionEvent() {
		ActionEvent e = new ActionEvent(this, 0, "clicked");
		Iterator it = listeners.iterator();
		while (it.hasNext()) {
			((ActionListener) it.next()).actionPerformed(e);
		}
	}
	
	private void drawShadow(Graphics2D g2, RoundRectangle2D rect) {
		BufferedImage image = new BufferedImage((int) rect.getWidth() + 8, (int) rect.getHeight() + 8, BufferedImage.TYPE_INT_ARGB);

		Graphics2D buffer = (Graphics2D) image.getGraphics();
        buffer.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        buffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		buffer.setColor(new Color(0.0f, 0.0f, 0.0f, 0.5f));
		buffer.translate(4, 4);
		buffer.fill(rect);

		ConvolveOp blur = new ConvolveOp(new Kernel(5, 5, BLUR_KERNEL));
		g2.drawImage(image, blur, 0, 0);
		
		buffer.dispose();
		image.flush();
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
		mouseOver = true;
		if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) == MouseEvent.BUTTON1_MASK) {
			mousePressed = true;
		}
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		repaint();
	}

	public void mouseExited(MouseEvent e) {
		mouseOver = false;
		mousePressed = false;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		repaint();
	}

	public void mousePressed(MouseEvent e) {
		mousePressed = true;
		repaint();
	}

	public void mouseReleased(MouseEvent e) {
		mousePressed = false;
		repaint();
		if (mouseOver) {
			fireActionEvent();
		}
	}
}
