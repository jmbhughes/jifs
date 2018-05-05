import java.awt.image.*;
import javax.imageio.*;
import java.awt.Color;
import java.io.*;

public class Image {
    double xmin;
    double xmax;
    double ymin;
    double ymax;
    int width;
    int height;
    double dxpp; //amount of x movement per pixel
    double dypp; //amount of y movement per pixel
    BufferedImage img;

    public Image(int width, int height,
                 double xmin, double xmax,
                 double ymin, double ymax) {
        assert xmin < xmax : "xmin should be less than xmax";
        assert ymin < ymax : "ymin should be less than ymax";
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
        
        this.width = width;
        this.height = height;
               
        this.img = new BufferedImage(width, height,
                                     BufferedImage.TYPE_INT_RGB);
        
        this.dxpp = width / (xmax - xmin);
        this.dypp = height / (ymax - ymin);
    }
    
    private int[] transform(double x, double y) {
        assert x > xmin && x < xmax && y > ymin && y < ymax : "(x,y) not in range";
        double dx = x - xmin;
        double dy = y - ymin;
        return new int[]{(int)(dx * dxpp), (int)(dy * dypp)};
    }
    
    public void plot(Matrix p) {
        Color c = new Color(255,255,255); // all regions are white
        int rgb = c.getRGB();

        double x = p.get(1,1);
        double y = p.get(2,1);
        if (x > xmin && x < xmax && y > ymin && y < ymax) {
            int[] ij = transform(x, y);
            img.setRGB(ij[0], ij[1], rgb);
            //       System.out.printf("%d %d %f %f\n", ij[0], ij[1], x, y);
        }
    }
    
    public void save(String filename) {
        try {
            File outputfile = new File(filename);
            ImageIO.write(img, "jpg", outputfile);
        }catch(IOException e) {
            System.out.println("FAILED!");   
        }
    }

    public static void main(String args[]) {
        Image img = new Image(500, 500, -1.0, 1.0, -1.0, 1.0);
        img.plot(new Matrix(0.0, 0.0));
        img.save("image.jpg");
    }
}
