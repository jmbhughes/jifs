import java.io.*;
import java.util.Vector;
import java.awt.image.*;
import javax.imageio.*;
import java.awt.Color;

/**
 * An iterated function system <code> IFS </code> can be evaluated either 
 * deterministically, where every point is evaluated in a straight forward 
 * manner as described by the system, or probabilistically. 
 * This is a probabilistic approach. A set of random points is selected 
 * and iterated forward. These characterize the behavior of the iterated
 * function system without as much computational work. 
 */
public class RandomIFSEvaluator extends IFSEvaluator {
    IFS ifs;
    int numPoints;
    int iterations;

    /** Set up the random iterated function system evaluator
     * @param ifs an initialized iterated function system
     * @param numPoints how many randomly selected initial starting locations to run
     * @param how many iterations should be evaluated for each point
     * @see IFS
     */
    RandomIFSEvaluator(IFS ifs, int numPoints, int iterations) {
        assert numPoints > 0 : "numPoints must be positive";
        assert iterations > 0 : "iterations must be positive";
        this.ifs = ifs;
        this.iterations = iterations;
        this.numPoints = numPoints;
    }

    /**
     * Simulate running to create an image of width by height
     * @param width how many pixels wide the image should be 
     * @param height how many pixels high the region is
     */ 
    public void run(int width, int height) {
        Color c = new Color(255,255,255); // all regions are white
        int rgb = c.getRGB();
        BufferedImage img = new BufferedImage(width, height,
                                              BufferedImage.TYPE_INT_RGB);

        // for every requested point
        for(int point=0; point < this.numPoints; point++) {
            Coordinate p = new Coordinate(); // random in [-1,1] X [-1,1]

            // iterate it forward the requested iteration count
            for(int iteration=0; iteration < this.iterations; iteration++) {
                // each time pick a random transformation and evaluate it
                Transform t = this.ifs.chooseTransform();
                p = t.transform(p);
                if (iteration > 25) {
                    Coordinate pix = p.scale(width, height).integerize();
                    img.setRGB((int)pix.getX(), (int)pix.getY(), rgb);
                }
            }

            // save in a silly location
            try {
                File outputfile = new File("image.jpg");
                ImageIO.write(img, "jpg", outputfile);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }        
    }

    /** 
     * testing main
     */ 
    public static void main(String[] args) {
        System.out.println("Test of random ifs evaluator");
        TwoDMatrix shrink = new TwoDMatrix(0.5, 0.0, 0.0, 0.5);
        AffineTransform t1 = new AffineTransform(shrink, new Coordinate(0.0,0.0));
        AffineTransform t2 = new AffineTransform(shrink, new Coordinate(0.5,0.0));
        AffineTransform t3 = new AffineTransform(shrink, new Coordinate(0.0,0.5));
        Vector<Transform> transforms = new Vector<Transform>();
        transforms.add(t1);
        transforms.add(t2);
        transforms.add(t3);
        IFS system = new IFS(transforms);
        RandomIFSEvaluator ifsRunner = new RandomIFSEvaluator(system, 1000, 100);
        ifsRunner.run(1000,1000);
    }
    
}
