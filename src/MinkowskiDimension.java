import java.util.Vector;
/*** NOT FINISHED ***/

/**
 * Calculate the box-counting/Minkowski dimension for a set of points
 */
public class MinkowskiDimension {
    int dimension;
    Vector<Vector<Double>> pointSet;
    Vector<Double> sizes;
    Vector<Integer> counts;

    /**
     * Creates a Minkowski dimension evaluator 
     */                                                                       
    MinkowskiDimension(Vector<Vector<Double>> pointSet) {
        this.dimension = pointSet[0].size();
        this.pointSet = pointSet;
        for (Vector<Double> v : pointSet) {
            if (v.size() != dimension) {
                throw new RuntimeException("All points must be of the same dimension.");
            }
        }
        this.counts = new Vector<Integer>();
        this.sizes = new Vector<Double>();
    }

    private int count_boxes() {
        return 0; // TODO        
    }
    

    /**
     * testing function
     */
    public static void main(String[] args) {
        System.out.println("NOT FINISHED");
    }
}
