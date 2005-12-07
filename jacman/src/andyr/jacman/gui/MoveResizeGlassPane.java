package andyr.jacman.gui;


import javax.swing.*;
import java.awt.*;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.*;

/**
 * A glasspane that can be used to notify the user (for a specified time)
 * what the x and y coordinates of their JFrame (window) are after they
 * have moved it.
 *
 * <p/>
 * Copyright (C) 2005 by Jon Lipsky
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. Y
 * ou may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software d
 * istributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class MoveResizeGlassPane extends JPanel implements ComponentListener, ActionListener
{
    public static float[] BLUR = {0.10f, 0.10f, 0.10f, 0.10f, 0.30f, 0.10f, 0.10f, 0.10f, 0.10f};
    public static ConvolveOp blurOp = new ConvolveOp(new Kernel(3, 3, BLUR));

    // ------------------------------------------------------------------------------------------------------------------
    //  Fields
    // ------------------------------------------------------------------------------------------------------------------

    private JFrame frame;
    private boolean installed = false;
    private Component previousGlassPane;
    private Timer timer;
    private int delay = 2000;
    private Point lastLocation;

    // ------------------------------------------------------------------------------------------------------------------
    //  Constructors and Getter/Setters
    // ------------------------------------------------------------------------------------------------------------------

    public MoveResizeGlassPane(JFrame aFrame)
    {
        frame = aFrame;
        frame.addComponentListener(this);
        setOpaque(false);
    }

    public int getDelay()
    {
        return delay;
    }

    public void setDelay(int aDelay)
    {
        delay = aDelay;
    }

    // ------------------------------------------------------------------------------------------------------------------
    //  Implementation of the methods from ComponentListener
    // ------------------------------------------------------------------------------------------------------------------

    public void componentHidden(ComponentEvent e)
    {
        // Do nothing
    }

    public void componentMoved(ComponentEvent e)
    {
        lastLocation = frame.getLocation();
        repaint();

        if (!installed)
        {
            previousGlassPane = frame.getGlassPane();
            frame.setGlassPane(this);
            setVisible(true);
            installed = true;
        }

        if (timer == null)
        {
            timer = new Timer(delay, this);
        }
        else
        {
            timer.stop();
            timer.setDelay(delay);
        }

        timer.start();
    }

    public void componentResized(ComponentEvent e)
    {

        if (!installed) {
            previousGlassPane = frame.getGlassPane();
            frame.setGlassPane(this);
            setVisible(true);
            installed = true;
        }

        if (timer == null) {
            timer = new Timer(delay, this);
        }
        else {
            timer.stop();
            timer.setDelay(delay);
        }

        timer.start();

    }

    public void componentShown(ComponentEvent e)
    {
        // Do nothing
    }

    // ------------------------------------------------------------------------------------------------------------------
    //  Implementation of the methods from ComponentListener
    // ------------------------------------------------------------------------------------------------------------------

    public void actionPerformed(ActionEvent e)
    {
        timer.stop();

        installed = false;
        setVisible(false);
        frame.setGlassPane(previousGlassPane);
        previousGlassPane = null;
    }

    // ------------------------------------------------------------------------------------------------------------------
    //  Override methods of JPanel
    // ------------------------------------------------------------------------------------------------------------------

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Font font = g.getFont();

        Dimension size = getSize();
        int h = size.height;
        int w = size.width;
        int arc = 0;

        // Figure out what size font to use, and what size arc to use
        if (size.width > 300)
        {
            font = font.deriveFont(Font.PLAIN,48);
            arc = 20;
        }
        else if (size.width > 150)
        {
            font = font.deriveFont(Font.PLAIN,24);
            arc = 10;
        }
        else
        {
            font = font.deriveFont(Font.PLAIN,12);
            arc = 3;
        }

        if (lastLocation == null) {
            lastLocation = frame.getLocation();
        }

        String moveText = lastLocation.x +","+lastLocation.y;
        String resizeText = size.width + " x "+size.height;
        
        String text = moveText + (" (" + resizeText + ")");


        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        Rectangle2D stringBounds = metrics.getStringBounds(text,g);

        // Figure out how big we want our rounded rectangle to be
        int preferredWidth = (int)stringBounds.getWidth()+metrics.getHeight();
        int preferredHeight = (int)stringBounds.getHeight()+metrics.getHeight();

        w = Math.min(preferredWidth,w);
        h = Math.min(preferredHeight,h);

        int x = (size.width - w) / 2;
        int y = (size.height - h) / 2;

        // Create the path that runs through the rounded rectangle
        float h2 = h/2;
        float h4 = h/4;

        GeneralPath path = new GeneralPath();
        path.moveTo(0,h);
        path.curveTo(0,h-h4,h4,h2,h2,h2);
        path.lineTo(w-h2,h2);
        path.curveTo(w-h4,h2,w,h4,w,0);
        path.lineTo(w,h);

        // Create a buffered image to paint the rounded rectangle
        BufferedImage vBuffer = new BufferedImage(preferredWidth, preferredHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D bg2d = vBuffer.createGraphics();
        bg2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Paint the background
        RoundRectangle2D fillArea = new RoundRectangle2D.Double(0.0D, 0.0D, w, h,arc,arc);
        bg2d.setClip(fillArea);

        Area area = new Area(fillArea);
        area.subtract(new Area(path));

        Color vStartColor = new Color(10,0,40);
        Color vEndColor = new Color(175,165,225);

        java.awt.Paint p = new GradientPaint(0.0F, 0.0F, vStartColor, 0.0F, h, vEndColor);
        bg2d.setPaint(p);

        bg2d.fill(area);

        vStartColor = new Color(5,0,50);
        vEndColor = new Color(105,100,155);

        p = new GradientPaint(0.0F, 0.0F, vStartColor, 0.0F, h, vEndColor);
        bg2d.setPaint(p);

        bg2d.fill(path);

        // Blur the background
        vBuffer = blurOp.filter(vBuffer, null);
        bg2d = vBuffer.createGraphics();

        // Figure out where to place the background and the text
        int insetX = (size.width - w) / 2;
        int insetY = (size.height - h) / 2;

        g2d.translate(insetX,insetY);

        Composite composite = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .6f));
        Shape clip = g2d.getClip();
        g2d.setClip(fillArea);
        g2d.drawImage(vBuffer,0,0,null);
        g2d.setClip(clip);
        g2d.setComposite(composite);

        // Paint a border around the background since it was clipped and the edges
        // weren't anti-aliased
        Color vWrapColor = new Color(175,165,225);
        g2d.setColor( vWrapColor );
        g2d.drawRoundRect(0, 0, w, h,arc,arc);

        // Figure out where to draw the text
        x = (w - (int)stringBounds.getWidth()) / 2;
        y = (h / 2) + ((metrics.getAscent()- metrics.getDescent()) / 2);

        // Draw a shadow
        g2d.setColor(new Color(0,0,0,70));
        g2d.drawString(text,x+2,y+2);

        // Draw the text
        g2d.setColor(Color.WHITE);
        g2d.drawString(text,x,y);

        g2d.translate(-insetX,-insetY);
    }

    // ------------------------------------------------------------------------------------------------------------------
    //  Utility Methods
    // ------------------------------------------------------------------------------------------------------------------

    public static void registerFrame(JFrame aFrame)
    {
        new MoveResizeGlassPane(aFrame);
    }
}