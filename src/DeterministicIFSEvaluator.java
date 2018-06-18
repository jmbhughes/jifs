import java.util.Vector;
public class DeterministicIFSEvaluator extends IFSEvaluator {

    DeterministicIFSEvaluator(IFS ifs, int iterations) {
        super(ifs, iterations); 
    }
    
    public Vector<Vector<Matrix>> run() {
        return new Vector<Vector<Matrix>>();
    }
}
