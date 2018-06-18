import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import java.util.List;
import java.util.ArrayList;

public class TransWindow extends JPanel{

    List<ResizableQuad> rects;
    BindMouseMove movingAdapt = new BindMouseMove();
    int clickedRect;
    
    public TransWindow(int size) {        
        assert size % 2 == 0: "size must be odd";
        clickedRect = -1;

        Color cursorColor = new Color(0, 0, 0);
        Color red = new Color(255, 0, 0);
        Color green = new Color(0, 255, 0);
        Color blue = new Color(0, 0, 255);

        rects = new ArrayList<>();
        rects.add(new ResizableQuad(80, 30, 100, cursorColor, red));
        rects.add(new ResizableQuad(20, 50, 100, cursorColor, green));
        rects.add(new ResizableQuad(0, 0, 100, cursorColor, blue));

        addMouseMotionListener(movingAdapt);
        addMouseListener(movingAdapt);
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLUE);
        g2d.drawLine(getWidth()/2, 0, getWidth()/2, getHeight());
        g2d.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);
        Rectangle2D.Float unit = new Rectangle2D.Float(getWidth()/4, getHeight()/4,
                                                       getWidth()/2, getHeight()/2);
        g2d.draw(unit);
        
        for (ResizableQuad q : rects) {
            q.paintComponent(g);
        }
        
    }

    public void addRect(ResizableQuad rect) {
        rects.add(rect);
    }

    class BindMouseMove extends MouseAdapter {
        private int x;
        private int y;
        private int activeIndex;
        
        public void mousePressed(MouseEvent event) {
            x = event.getX();
            y = event.getY();
            activeIndex = -1;
            int index = 0;
            boolean found = false;
            for (ResizableQuad q: rects) {
                if (q.contains(x, y)) {
                    activeIndex = index;
                    found = true;
                    break;
                }
                index += 1;
                //q.movingAdapt.mousePressed(event);
            }
            if (!found)
                activeIndex = -1;
            
            //repaint();
        }

        public void mouseDragged(MouseEvent event) {
            int dx = event.getX() - x;
            int dy = event.getY() - y;
            if (activeIndex != -1)
                rects.get(activeIndex).movingAdapt.mouseDragged(event);
            /*
              for (ResizableQuad q: rects) {
                q.movingAdapt.mouseDragged(event);
            }
            */
            repaint();
        }
    }
    
    /** simple testing method */
    public static void main(String[] args) {
        JFrame myFrame = new JFrame();
        //ResizableQuad resi = new ResizableQuad(800, 800, 90);
        //ResizableQuad resi2 = new ResizableQuad(80, 800, 90);
        //resi.setDoubleBuffered(true);
        myFrame.add(new TransWindow(50));
        //myFrame.add(resi);
        //myFrame.add(resi2);


        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setSize(400, 400);
        myFrame.setLocationRelativeTo(null);
        myFrame.setVisible(true);
    }

}
