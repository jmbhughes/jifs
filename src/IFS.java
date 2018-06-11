import java.util.Vector;
import static java.lang.Math.random;

/**
 * A representation of an iterated function system
 * An iterated function system takes a set of functions/transformations
 * and evaluates where a given point goes under the transformation.
 * A key example is the Barnsley Fern.
 * An iterated function system can have multiple portions. Technically,
 * all of the transformations can be evaluated, but a probabalistic 
 * approach is possible. Thus, each transformation has an associated
 * likelihood.
 * @author J. Marcus Hughes
 */
public class IFS {
    Vector<Transform> transforms;
    Vector<Double> probabilities;

    /** 
     * Returns IFS with all transforms having equal weight 
     * @param transforms a list of equally weighted transforms
     */
    IFS(Vector<Transform> transforms){
        assert transforms.size() > 0 : "transforms must have size > 0";
        this.transforms = transforms;
        this.probabilities = new Vector<Double>();
        double weight = 1.0/this.transforms.size();
        for (Transform t : this.transforms){this.probabilities.add(weight);}
        assert checkProbability() : "probability list must sum to 1.0";
    }
    
    /** 
     * Constructor given a list of transforms and probabilities 
     * @param transforms a vector of transform type objects
     * @param probabilities probability of selecting transform, must sum to 1.0
     */
    IFS(Vector<Transform> transforms, Vector<Double> probabilities){
        if (transforms.size() != probabilities.size()) {
            throw new RuntimeException("Transforms and probabilities must be the same size");
        }
        this.transforms = transforms;
        this.probabilities = probabilities;
        assert checkProbability() : "probability list must sum to 1.0";
    }

    /**
     * copy constructor for IFS
     * @param other an IFS to copy
     */
    IFS(IFS other) {
        this.transforms = other.transforms;
        this.probabilities = other.probabilities;
        assert checkProbability() : "probability list must sum to 1.0";
    }

    /** determine if the probability list is sums to 1.0
     * @return <code>true</code> if sum is 1.0 and <code>false</code> otherwise
     */
    private boolean checkProbability(){
        double total = 0.0;
        for (double probability : this.probabilities)
            total += probability;
        return total == 1.0;
    } 
    /** 
     * Append the transform to the set of transforms 
     * @param t transform to be appended
     * @param p probability of selecting that transform
     */
    public void addTransform(Transform t, double p){
        this.transforms.add(t);
        this.probabilities.add(p);
        assert checkProbability() : "probability list must sum to 1.0";
    }

    /**
     * Select a transformation at random using the probability weighting
     */
    public Transform chooseTransform() {
        double n = Math.random();
        double runningTotal = 0.0;
        int i;
        for (i = 0; i < this.probabilities.size(); i++) {
            runningTotal += this.probabilities.get(i);
            if (n <= runningTotal) {
                break;
            }
        }
        return this.transforms.get(i);
    }

    public static IFS sierpinskiTriangle() {
        Vector<Transform> transforms = new Vector<Transform>();
        Vector<Double> probabilities = new Vector<Double>();
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
        return new IFS(transforms, probabilities);
    }

    public static IFS barnsleyFern() {
        Vector<Transform> transforms = new Vector<Transform>();
        Vector<Double> probabilities = new Vector<Double>();
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
        return new IFS(transforms, probabilities);
    }

    /**
     * create a string version
     */
    public String toString() {
        String out = "";
        out += "transform: \n";
        int i = 0;
        for (Transform t : transforms) {
            out += "\t";
            out += t;
            out += " at ";
            out += (int)(100 * probabilities.get(i));
            out += "%\n";
            i += 1;
        }
        return out;
    }
    
    /** 
     * testing main
     */
    public static void main(String[] args){
        System.out.println("Test of IFS");

        Matrix shrink = new Matrix(new double[][]{{0.5, 0.0}, {0.0, 0.5}});
        AffineTransform t1 = new AffineTransform(shrink, new Matrix(0.0,0.0));
        AffineTransform t2 = new AffineTransform(shrink, new Matrix(0.5,0.0));
        AffineTransform t3 = new AffineTransform(shrink, new Matrix(0.0,0.5));
        Vector<Transform> transforms =  new Vector<Transform>();
        transforms.add(t1);
        transforms.add(t2);
        transforms.add(t3);
        IFS system = new IFS(transforms);
        Matrix p = new Matrix(0.041462, 0.408642);
        for(int i=0; i < 10000; i+=1) {
            System.out.println(p);
            Transform t = system.chooseTransform();
            p = t.transform(p);
        }

        
    }

}
