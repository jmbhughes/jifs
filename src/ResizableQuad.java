//package com.javacodegeeks.snippets.desktop;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.awt.Polygon;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * A resizable quadrilateral that can be adjusted with cursors. This is used to control the 
 * IFS definition visually. 
 */
public class ResizableQuad extends JPanel {
    private int CURSORSIZE = 8;
    private int CURSOROFFSET = 3;
    
    private Color cursorColor;
    private Color polyColor;

    private Rectangle2D.Float upperLeftCursor;
    private Polygon myPoly = new Polygon();
    
    BindMouseMove movingAdapt = new BindMouseMove();

    /**
     * Constructor with full customization, creates a square
     * @param x the x coordinate of the upper left point 
     * @param y the y coordinate of the upper left point
     * @param size the width and height of the original square
     * @param cursorColor the color of the cursors for adjusting the shape
     * @param polyColor the fill color of the shape
     */
    public ResizableQuad(int x, int y, int size, Color cursorColor, Color polyColor) {
        // store parameters
        this.cursorColor = cursorColor;
        this.polyColor = polyColor;

        // set up the quadrilateral
        myPoly.addPoint(x, y);
        myPoly.addPoint(x, y+size);
        myPoly.addPoint(x+size, y+size);
        myPoly.addPoint(x+size, y);

        // add the resize cursor
        upperLeftCursor = new Rectangle2D.Float(x-CURSOROFFSET,
                                                y-CURSOROFFSET,
                                                CURSORSIZE,
                                                CURSORSIZE);

        // add the listeners
        addMouseMotionListener(movingAdapt);
        addMouseListener(movingAdapt);
        //addMouseWheelListener(new ResizeHandler());
    }

    /** Constructor with defaulted colors */
    public ResizableQuad(int x, int y, int size) {
        this(x, y, size, new Color(0,0,255), new Color(0,0,0));
    }


    /**
     * Paint the quadrilateral initially
     * @override 
     */
    public void paintComponent(Graphics graphics) {        
        super.paintComponent(graphics);  
        Graphics2D graphics2d = (Graphics2D) graphics;        
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        graphics2d.setColor(polyColor);
        graphics2d.drawPolygon(myPoly);
        graphics2d.fill(myPoly);

        graphics2d.setColor(cursorColor);
        graphics2d.setColor(new Color(200, 0, 200));
        graphics2d.draw(upperLeftCursor);
        graphics2d.fill(upperLeftCursor);
    }

    /**
     * Mouse click and drag listener
     */
    class BindMouseMove extends MouseAdapter {
        private int x;
        private int y;

        /**
         * behavior upon clicking event
         * @override
         */
        public void mousePressed(MouseEvent event) {
            x = event.getX();
            y = event.getY();
        }


        /**
         * behavior upon dragging event
         * @override
         */
        public void mouseDragged(MouseEvent event) {
            // get amount of change
            int dx = event.getX() - x;            
            int dy = event.getY() - y;


            if (upperLeftCursor.getBounds2D().contains(x,y)) { // if on the resize cursor                
                myPoly.xpoints[0] += dx;
                myPoly.ypoints[0] += dy;
                myPoly.xpoints[1] += dx;
                myPoly.ypoints[3] += dy;
                upperLeftCursor.x += dx;
                upperLeftCursor.y += dy;
                repaint();
            } else if (myPoly.contains(x,y)) { // if draggin in the polygon
                myPoly.translate(dx, dy);
                upperLeftCursor.x += dx;
                upperLeftCursor.y += dy;
                repaint();
            }
              
            x += dx;
            y += dy;
        }
    }

    /*
    class ResizeHandler implements MouseWheelListener {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            int x = e.getX();            
            int y = e.getY();

            if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                if (myRect.getBounds2D().contains(x, y)) {
                    float amount = e.getWheelRotation() * 5f;
                    myRect.width += amount;
                    myRect.height += amount;                    
                    repaint();
                }                
            }

        }
    }
    */
    
    /** simple testing method */
    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Moving and Scaling");
        ResizableQuad resi = new ResizableQuad(90, 90, 90);
        resi.setDoubleBuffered(true);        
        jFrame.add(resi);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(1000, 1000);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }
}
