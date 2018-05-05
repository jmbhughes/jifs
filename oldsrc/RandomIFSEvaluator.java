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
                    Complex z = new Complex(p.getX(), p.getY());
                    z = Complex.divide(Complex.multiply(z, new Complex(0.5, 0.0)),
                                       new Complex(1.0, 0.0));
                    p = new Coordinate(z.getRe(), z.getIm());
                    Coordinate pix = p.scale(width, height).integerize();
                    System.out.printf("%f %f\n", p.getX(), p.getY());
                    System.out.printf("%f %f\n", pix.getX(), pix.getY());
                    System.out.println();
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
        Vector<Transform> transforms = new Vector<Transform>();
        Vector<Double> probabilities = new Vector<Double>();
        /* //Sierpinski triangle 
        TwoDMatrix shrink = new TwoDMatrix(0.5, 0.0, 0.0, 0.5);
        AffineTransform t1 = new AffineTransform(shrink, new Coordinate(0.0,0.0));
        AffineTransform t2 = new AffineTransform(shrink, new Coordinate(0.5,0.0));
        AffineTransform t3 = new AffineTransform(shrink, new Coordinate(0.0,0.5));
        transforms.add(t1);
        transforms.add(t2);
        transforms.add(t3);*/

        // Barnsley Fern
        //stem
        AffineTransform t1 = new AffineTransform(new TwoDMatrix(0.0, 0.0, 0.0, 0.16),
                                                 new Coordinate(0.0, 0.0));
        transforms.add(t1);
        probabilities.add(0.01);
        
        //smaller leaflets
        AffineTransform t2 = new AffineTransform(new TwoDMatrix(0.85, 0.04, -0.04, 0.85),
                                                 new Coordinate(0.0, 1.6));
        transforms.add(t2);
        probabilities.add(0.85);

        //largest left leaflet
        AffineTransform t3 = new AffineTransform(new TwoDMatrix(0.20, -0.26, 0.23, 0.22),
                                                 new Coordinate(0.0, 1.6));
        transforms.add(t3);
        probabilities.add(0.07);
        

        //largest right leaflet
        AffineTransform t4 = new AffineTransform(new TwoDMatrix(-0.15, 0.28, 0.26, 0.24),
                                                 new Coordinate(0.0, 0.44));
        transforms.add(t4);
        probabilities.add(0.07);

        IFS system = new IFS(transforms, probabilities);
        RandomIFSEvaluator ifsRunner = new RandomIFSEvaluator(system, 1000, 100);
        ifsRunner.run(1000,1000);
    }
    
}
