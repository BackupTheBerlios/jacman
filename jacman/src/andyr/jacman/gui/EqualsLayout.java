package andyr.jacman.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.SwingConstants;

/** 
 * @author Santhosh Kumar - santhosh@in.fiorano.com 
 */
public class EqualsLayout implements LayoutManager, SwingConstants {
    private int gap;

    private int alignment;

    private int orientation;

    public EqualsLayout(int alignment, int gap) {
        setGap(gap);
        setAlignment(alignment);
    }

    public EqualsLayout(int orientation, int alignment, int gap) {
        setOrientation(orientation);
        setGap(gap);
        setAlignment(alignment);
    }

    public EqualsLayout(int gap) {
        this(RIGHT, gap);
    }

    public int getAlignment() {
        return alignment;
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL)
            throw new IllegalArgumentException("Invalid Orientation: "
                    + orientation);
        this.orientation = orientation;
    }

    private Dimension[] dimensions(Component children[]) {
        int maxWidth = 0;
        int maxHeight = 0;
        int visibleCount = 0;
        Dimension componentPreferredSize;

        for (int i = 0, c = children.length; i < c; i++) {
            if (children[i].isVisible()) {
                componentPreferredSize = children[i].getPreferredSize();
                maxWidth = Math.max(maxWidth, componentPreferredSize.width);
                maxHeight = Math.max(maxHeight, componentPreferredSize.height);
                visibleCount++;
            }
        }

        /*int usedWidth = maxWidth * visibleCount + gap * (visibleCount - 1); 
         int usedHeight = maxHeight;
         */
        int usedWidth = orientation == HORIZONTAL ? maxWidth * visibleCount
                + gap * (visibleCount - 1) : maxWidth;
        int usedHeight = orientation == VERTICAL ? maxHeight * visibleCount
                + gap * (visibleCount - 1) : maxHeight;
        return new Dimension[] { new Dimension(maxWidth, maxHeight),
                new Dimension(usedWidth, usedHeight), };
    }

    /*public void layoutContainer(Container container){ 
     Insets insets = container.getInsets(); 
     
     Component[] children = container.getComponents(); 
     Dimension dim[] = dimensions(children); 
     
     int maxWidth = dim[0].width; 
     int maxHeight = dim[0].height; 
     int usedWidth = dim[1].width; 
     int usedHeight = dim[1].height; 
     
     switch(alignment){ 
     case LEFT: 
     case TOP: 
     for(int i = 0, c = children.length; i < c; i++){ 
     if(!children[i].isVisible()) 
     continue; 
     children[i].setBounds(insets.left+(maxWidth+gap)*i,  insets.top, 
     maxWidth, maxHeight); 
     } 
     break; 
     case RIGHT: 
     case BOTTOM: 
     for(int i = 0, c = children.length; i < c; i++){ 
     if(!children[i].isVisible()) 
     continue; 
     children[i].setBounds(container.getWidth()-insets.right-usedWidth+(maxWidth+gap)*i, 
     insets.top, 
     maxWidth, maxHeight); 
     } 
     break; 
     } 
     } */

    public void layoutContainer(Container container) {
        Insets insets = container.getInsets();

        Component[] children = container.getComponents();
        Dimension dim[] = dimensions(children);

        int maxWidth = dim[0].width;
        int maxHeight = dim[0].height;
        int usedWidth = dim[1].width;
        int usedHeight = dim[1].height;

        switch (orientation) {
        case HORIZONTAL:
            switch (alignment) {
            case LEFT:
            case TOP:
                for (int i = 0, c = children.length; i < c; i++) {
                    if (!children[i].isVisible())
                        continue;
                    children[i].setBounds(insets.left + (maxWidth + gap) * i,
                            insets.top, maxWidth, maxHeight);
                }
                break;
            case RIGHT:
            case BOTTOM:
                for (int i = 0, c = children.length; i < c; i++) {
                    if (!children[i].isVisible())
                        continue;
                    children[i].setBounds(container.getWidth() - insets.right
                            - usedWidth + (maxWidth + gap) * i, insets.top,
                            maxWidth, maxHeight);
                }
                break;
            }
            break;
        case VERTICAL:
            switch (alignment) {
            case LEFT:
            case TOP:
                for (int i = 0, c = children.length; i < c; i++) {
                    if (!children[i].isVisible())
                        continue;
                    children[i].setBounds(insets.left, insets.top
                            + (maxHeight + gap) * i, maxWidth, maxHeight);
                }
                break;
            case RIGHT:
            case BOTTOM:
                for (int i = 0, c = children.length; i < c; i++) {
                    if (!children[i].isVisible())
                        continue;
                    children[i].setBounds(insets.left, container.getHeight()
                            - insets.bottom - usedHeight + (maxHeight + gap)
                            * i, maxWidth, maxHeight);
                }
            }
        }
    }

    public Dimension minimumLayoutSize(Container c) {
        return preferredLayoutSize(c);
    }

    public Dimension preferredLayoutSize(Container container) {
        Insets insets = container.getInsets();

        Component[] children = container.getComponents();
        Dimension dim[] = dimensions(children);

        int usedWidth = dim[1].width;
        int usedHeight = dim[1].height;

        return new Dimension(insets.left + usedWidth + insets.right, insets.top
                + usedHeight + insets.bottom);
    }

    public void addLayoutComponent(String string, Component comp) {
    }

    public void removeLayoutComponent(Component c) {
    }
}