import java.awt.image.*;
import javax.imageio.*;
import java.awt.Color;
import java.io.*;

/**
 * An interface for the BufferedImage that maps points mathematically. 
 * Given a region in the real plane [a,b] * [c,d] and a width in pixels
 * and height in pixels provides mapping support from the number value
 * to the image.
 * @author J. Marcus Hughes
 */

public class Image {
    double xmin; // least allowed x value
    double xmax; // most allowed x value
    double ymin; // least allowed y value
    double ymax; // most allowed y value
    int width;   // width of image in pixels
    int height;  // height of image in pixels
    private double dxpp; //amount of x movement per pixel
    private double dypp; //amount of y movement per pixel
    BufferedImage img;

    /**
     * Constructs an image of the given width and height that maps
     * from the given limits
     * @param width pixels wide image is
     * @param height pixels wide image is
     * @param xmin least x value to show
     * @param xmax greatest x value to show
     * @param ymin least y value to show
     * @param ymax greatest y value to show 
     */ 
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

        // Determine how much "real" value should be mapped per pixel
        this.dxpp = width / (xmax - xmin);
        this.dypp = height / (ymax - ymin);
    }

    /**
     * Maps (x,y) in real values to pixel values
     */
    private int[] transform(double x, double y) {
        assert x > xmin && x < xmax && y > ymin && y < ymax : "(x,y) not in range";
        double dx = x - xmin;
        double dy = y - ymin;
        return new int[]{(int)(dx * dxpp), height - (int)(dy * dypp)};
    }

    /**
     * Given a point as a Matrix, plot it
     * @param p point as column vector
     */
    public void plot(Matrix p) {
        Color c = new Color(255,255,255); // all regions are white
        int rgb = c.getRGB();

        double x = p.get(1,1);
        double y = p.get(2,1);
        if (x > xmin && x < xmax && y > ymin && y < ymax) {
            // if not in plotting range do nothing
            int[] ij = transform(x, y);
            if(ij[0] > 0 && ij[1] > 0 && ij[0] < width && ij[1] < height) 
                img.setRGB(ij[0], ij[1], rgb);
        }
    }

    /**
     * Output image to file
     * @param filename where to save image 
     */
    public void save(String filename) {
        try {
            File outputfile = new File(filename);
            ImageIO.write(img, "jpg", outputfile);
        }catch(IOException e) {
            System.out.println("FAILED!");   
        }
    }

    /**
     * testing method
     */
    public static void main(String args[]) {
        Image img = new Image(500, 500, -1.0, 1.0, -1.0, 1.0);
        img.plot(new Matrix(0.0, 0.0));
        img.plot(new Matrix(0.5, 0.0));
        img.plot(new Matrix(0.0, -0.25));
        img.save("image.jpg");
    }
}
