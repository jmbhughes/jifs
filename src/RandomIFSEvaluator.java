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
 * @author J. Marcus Hughes
 */
public class RandomIFSEvaluator extends IFSEvaluator {
    IFS ifs;
    int numPoints;
    int iterations;
    Vector<Vector<Matrix>> results;
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
     * Simulate running on a single point
     * @param p the initial point
     */
    public Vector<Matrix> runPoint(Matrix p) {
        Vector<Matrix> ps = new Vector<Matrix>();
        ps.add(p);
        for (int it = 0; it < this.iterations; it++) {
            Transform t = ifs.chooseTransform();
            p = t.transform(p);
            ps.add(p);
        }
        return ps;
    }

    /**
     * Simulate running on many points
     */
    public Vector<Vector<Matrix>> run() {
        Vector<Vector<Matrix>> result = new Vector<Vector<Matrix>>();
        for (int it = 0; it < this.numPoints; it++) {
            Matrix p = Matrix.randomPoint();
            result.add(runPoint(p));
        }
        this.results = result;
        return result;        
    }

    /**
     * Plot the IFS in region (-1.0, 1.0) x (-1.0, 1.0) for last iteration
     * @param filename where to save image
     * @param width number of pixels wide for image
     * @param height number of pixels high for image
     */
    public void plot(String filename, int width, int height) {
        plot(filename, width, height, iterations, -1.0, 1.0, -1.0, 1.0);
    }

    /**
     * Plot the IFS in region (-1.0, 1.0) x (-1.0, 1.0) for requested iteration
     * @param filename where to save image
     * @param width number of pixels wide for image
     * @param height number of pixels high for image
     * @param iteration step to plot
     */
    public void plot(String filename, int width, int height, int iteration) {
        assert iteration < iterations : "not a valid iteration number";
        plot(filename, width, height, iteration, -1.0, 1.0, -1.0, 1.0);
    }

    /**
     * Plot the IFS in requested region for requested iteration
     * @param filename where to save image
     * @param width number of pixels wide for image
     * @param height number of pixels high for image
     * @param iteration step to plot
     * @param xmin least x value to show
     * @param xmax greatest x value to show
     * @param ymin least y value to show
     * @param ymax greatest y value to show
     */
    public void plot(String filename, int width, int height, int iteration,
                     double xmin, double xmax, double ymin, double ymax) {
        assert iteration < iterations : "not a valid iteration number";
        Image img = new Image(width, height, xmin, xmax, ymin, ymax);
        Vector<Vector<Matrix>> results = run();
        for(Vector<Matrix> r: results) {            
            img.plot(r.get(iteration));
        }
        img.save(filename);
    }

    
    /** 
     * testing main
     */ 
    public static void main(String[] args) {
        System.out.println("Test of random ifs evaluator");
        Vector<Transform> transforms = new Vector<Transform>();
        Vector<Double> probabilities = new Vector<Double>();

        //Sierpinski triangle

        Matrix shrink = new Matrix(0.5, 0.0, 0.0, 0.5);
        AffineTransform t1 = new AffineTransform(shrink, new Matrix(0.0,0.0));
        AffineTransform t2 = new AffineTransform(shrink, new Matrix(0.5,0.0));
        AffineTransform t3 = new AffineTransform(shrink, new Matrix(0.0,0.5));
        transforms.add(t1);
        probabilities.add(1.0/3.0);
        transforms.add(t2);
        probabilities.add(1.0/3.0);
        transforms.add(t3);
        probabilities.add(1.0/3.0);
        
        // Barnsley Fern
        /*
        //stem
        AffineTransform t1 = new AffineTransform(new Matrix(0.0, 0.0, 0.0, 0.16),
                                                 new Matrix(0.0, 0.0));
        transforms.add(t1);
        probabilities.add(0.01);
        
        //smaller leaflets
        AffineTransform t2 = new AffineTransform(new Matrix(0.85, 0.04, -0.04, 0.85),
                                                 new Matrix(0.0, 1.6));
        transforms.add(t2);
        probabilities.add(0.85);

        //largest left leaflet
        AffineTransform t3 = new AffineTransform(new Matrix(0.20, -0.26, 0.23, 0.22),
                                                 new Matrix(0.0, 1.6));
        transforms.add(t3);
        probabilities.add(0.07);
        

        //largest right leaflet
        AffineTransform t4 = new AffineTransform(new Matrix(-0.15, 0.28, 0.26, 0.24),
                                                 new Matrix(0.0, 0.44));
        transforms.add(t4);
        probabilities.add(0.07);
        */

        IFS system = new IFS(transforms, probabilities);

        RandomIFSEvaluator ifsRunner = new RandomIFSEvaluator(system, 100000, 100);
        ifsRunner.run();
        /*System.out.println("FINISHED RUNNING!");
        for (int i = 0; i < 100; i+=1){
            System.out.println(i);
            String fn = String.format("imgs/trial%03d.jpeg", i, 0.0, 3.0, 0.0, 3.0);
            ifsRunner.plot(fn, 500, 500, i);
            }*/
        ifsRunner.plot("test.jpeg", 500, 500, 100, 0.0, 1.0, 0.0, 1.0);
    }    
}
