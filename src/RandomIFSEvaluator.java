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
    int numPoints;
    Vector<Vector<Matrix>> results;
    /** Set up the random iterated function system evaluator
     * @param ifs an initialized iterated function system
     * @param numPoints how many randomly selected initial starting locations to run
     * @param how many iterations should be evaluated for each point
     * @see IFS
     */
    RandomIFSEvaluator(IFS ifs, int numPoints, int iterations) {
        super(ifs, iterations);
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
     * testing main
     */ 
    public static void main(String[] args) {
        System.out.println("Test of random ifs evaluator");

        IFS system = IFS.sierpinskiTriangle();
        int iterations = 20;
        //int numPoints = 100000;
        int numPoints = 10000;
        //IFS system = IFS.barnsleyFern();
        
        RandomIFSEvaluator ifsRunner = new RandomIFSEvaluator(system, numPoints, iterations);
        ifsRunner.run();
        /*System.out.println("FINISHED RUNNING!");
        for (int i = 0; i < 100; i+=1){
            System.out.println(i);
            String fn = String.format("imgs/trial%03d.jpeg", i, 0.0, 3.0, 0.0, 3.0);
            ifsRunner.plot(fn, 500, 500, i);
            }*/
        ifsRunner.plot("test.jpeg", 500, 500, iterations, 0.0, 1.0, 0.0, 1.0);
    }    
}
