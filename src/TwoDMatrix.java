/**
 * A 2x2 matrix with real entries
 * @author J. Marcus Hughes
 */
class TwoDMatrix {
    protected double a, b, c, d;

    /**
     * Default constructor initializes with a=b=c=d=0
     */
    TwoDMatrix(){
        this.a = 0.0;
        this.b = 0.0;
        this.c = 0.0;
        this.d = 0.0;
    }
    
    /** 
     * Construct a <code>TwoDMatrix</code> from a double array
     * @param m contents for matrix in form [a,b,c,d]
     */
    TwoDMatrix(double [] m) {
        this.a = m[0];
        this.b = m[1];
        this.c = m[2];
        this.d = m[3];
    }

    /**
     * TwoDMatrix constructor taking individual values 
     */
    TwoDMatrix(double a, double b, double c, double d){
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    /**
     * Copy constructor
     */
    TwoDMatrix(TwoDMatrix m){
        this(m.a, m.b, m.c, m.d);
    }

    /**
     * Adds another matrix in patter <code> this + m </code>
     * @param m matrix to be added to this
     * @return sum of this and <code> m </code>
     */
    public TwoDMatrix add(TwoDMatrix m){
        return new TwoDMatrix(this.a + m.a,
                              this.b + m.b,
                              this.c + m.c,
                              this.d + m.d);
    }

    /**
     * Evaluates the matrix on a point (or a 2x1 column vector)
     * @param p point to run from 
     * @return where the matrix takes <code> p </code>
     */
    public Coordinate evaluate(Coordinate p){
        return new Coordinate(this.a * p.getX() + this.b * p.getY(),
                              this.c * p.getX() + this.d * p.getY());
    }
    
}