/**
 * A representation for an AffineTransform for usage in generating fractals and other
 *  iterated function systems. 
 * An AffineTransform is a linear transform, in the plane this is represetable by a 2x2
 * matrix, combined with a translation. Thus, it can be fully described by 6 values:
 * 
 * <pre>
 *     | a b | | x |   | e |
 * T = |     | |   | + |   |
 *     | c d | | y |   | f |
 * </pre>
 *
 * In the code below, [[a,b],[c,d]] is the matrix and [e,f] is the shift. 
 * @author J. Marcus Hughes
 * @since 2018-04-22
 */
public class AffineTransform extends Transform {
    TwoDMatrix matrix;
    Coordinate shift;

    /**
     * Normal constructor
     * @param matrix linear transform portion [[a,b],[c,d]]
     * @param shift translation [e,f]
     */
    AffineTransform(TwoDMatrix matrix, Coordinate shift) {
        super("Affine Transform");
        this.matrix = matrix;
        this.shift = shift;
    }

    /**
     * Expanded version of Affine Transform
     * @param a element of matrix
     * @param b element of matrix
     * @param c element of matrix
     * @param d element of matrix
     * @param e element of shift
     * @param f element of shift
     */
    AffineTransform(double a, double b, double c, double d, double e, double f) {
        super("Affine Transform");
        this.matrix = new TwoDMatrix(a, b, c, d);
        this.shift = new Coordinate(e, f);
    }

    /** Given a point, executes the affine transformation 
     * @param p a point in the plane
     * @return the point after affine transformation is applied
     */
    public Coordinate transform(Coordinate p) {
        return this.matrix.evaluate(p).add(this.shift);
    }

    /**
     * String representation
     */
    public String toString(){
        return String.format("[[%f,%f],[%f,%f]] [%f, %f]",
                             this.matrix.a,
                             this.matrix.b,
                             this.matrix.c,
                             this.matrix.d,
                             this.shift.getX(),
                             this.shift.getY());
    }

    /**
     * A simple testing script that runs part of a Sierpinski Triangle
     */
    public static void main(String[] args) {
        Coordinate p = new Coordinate(0.041462, 0.408642);
        TwoDMatrix shrink = new TwoDMatrix(0.5, 0.0, 0.0, 0.5);
        AffineTransform t1 = new AffineTransform(shrink, new Coordinate(0.0, 0.0));
        AffineTransform t2 = new AffineTransform(shrink, new Coordinate(0.5, 0.0));
        AffineTransform t3 = new AffineTransform(shrink, new Coordinate(0.0, 0.5));
        for(int i=0; i < 1000; i+=1) {
            System.out.println(p);
            p = t3.transform(p);
        }
    }
}
