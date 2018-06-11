import java.util.Vector;

/** 
 * An abstract representation of an iterated function system <code> IFS </code> evaluator.
 * @author J. Marcus Hughes
 */
abstract class IFSEvaluator {
    IFS ifs;
    int iterations;
    
    IFSEvaluator(IFS ifs, int iterations) {
        this.ifs = ifs;
        this.iterations = iterations;
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

    abstract public Vector<Vector<Matrix>> run();


}
