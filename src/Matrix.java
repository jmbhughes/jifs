import java.lang.Math;

/** 
 * A matrix class with M by N real entries 
 * @author J. Marcus Hughes
 */
public class Matrix {
    private final double[][] entries; // values in matrix
    private final int m; // number of rows
    private final int n; // number of columns

    /**
     * A generic operation on a matrix done by entry, 
     * e.g. scalar multiplication
     */
    interface ElementMath {
        double apply(double a);
    }

    /**
     * A generic operation on a matrix with another matrix done by entry,
     * e.g. matrix addition
     */
    interface ComponentMath {
        double apply(double a, double b);
    }

    public static Matrix randomPoint() {
        double min = -1.0;
        double max = 1.0;
        double x = min + Math.random() * (max - min);
        double y = min + Math.random() * (max - min);
        return new Matrix(x,y);        
    }
    
    /**
     * Constructor for <code> Matrix </code> that fills with zeroes
     * @param m number of rows
     * @param n number of columns
     */
    Matrix(int m, int n) {
        this.m = m;
        this.n = n;
        this.entries = new double[m][n];
    }
    
    /**
     * Constructs column matrix corresponding to a point
     * @param a top entry
     * @param b bottom entry
     */
    Matrix(double a, double b) {
        this.m = 2;
        this.n = 1;
        this.entries = new double[][]{{a},{b}};
    }
    
    /**
     * Constructs a 2x2 matrix with entries of form {{a,b},{c,d}}
     * @param a 1,1 entry
     * @param b 1,2 entry
     * @param c 2,1 entry
     * @param d 2,2 entry
     */
    Matrix(double a, double b, double c, double d) {
        this.m = 2;
        this.n = 2;
        this.entries = new double[][]{{a,b},{c,d}};
    }

    /**
     * Construct a <code> Matrix </code> 
     * @param entries a double array storing the matrix entries row by row
     */
    Matrix(double [][] entries) {
        this.m = entries.length;
        this.n = entries[0].length;
        this.entries = new double[this.m][this.n];
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                this.entries[i][j] = entries[i][j];
            }
        }
    }

    /**
     * Retrieve the (i,j) element from the matrix
     * @param i row index
     * @param j column index
     */
    public double get(int i, int j) {
        if (i <= 0 || i > this.m) {
            throw new RuntimeException("Row out of bounds.");
        }
        if (j <= 0 || j > this.n) {
            throw new RuntimeException("Column  out of bounds.");
        }
        return this.entries[i-1][j-1];
    }
    
    /**  
     * Perform an element-wise operation on a matrix
     * @param f function
     */
    private Matrix elementwise(ElementMath f) {     
        Matrix B = new Matrix(m, n);
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                B.entries[i][j] = f.apply(this.entries[i][j]);
        return B;

    }

    /** 
     * Scale a matrix by parameter
     * @param scale real number to scale each entry by
     */
    public Matrix multiply(double scale) {
        ElementMath f = (a) -> scale * a;
        return elementwise(f);
    }

    /**
     * Add a scalar to a matrix
     * @param n number to add
     */
    public Matrix add(double n) {
        ElementMath f = (a) -> a + n;
        return elementwise(f);
    }

    /**  
     * Perform an component-wise operation on a matrix
     * @param B a matrix to take other element from
     * @param f function
     */
    private Matrix componentwise(Matrix B, ComponentMath f) {     
        Matrix C = new Matrix(m, n);
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                C.entries[i][j] = f.apply(B.entries[i][j], this.entries[i][j]);
        return C;
    }

    /**
     * Determines if the matrices have the same dimension. If not, throws exception.
     */
    private void checkSameDimensions(Matrix B) {
        if (B.m != this.m || B.n != this.n) {
            throw new RuntimeException("Illegal matrix dimensions. Must be same size.");
        }
    }
    /**
     * Adds a matrix 
     * @param B matrix to add 
     * @return this + B
     */
    public Matrix add(Matrix B) {
        checkSameDimensions(B);
        ComponentMath f = (a,b) -> a + b;
        return componentwise(B, f);
    }

    /**
     * Subtracts a matrix 
     * @param B matrix to subtract
     * @return this - B
     */
    public Matrix subtract(Matrix B) {
        checkSameDimensions(B);
        ComponentMath f = (a,b) -> a - b;
        return componentwise(B, f);
    }

    /**
     * Multiply a matrix by another
     * @param B matrix to multiply
     * return this * B
     */
    public Matrix multiply(Matrix B) {
        Matrix A = this;
        // check for proper size
        if (this.n != B.m) {
            throw new RuntimeException("Illegal matrix dimensions for multiply."
                                       + "Must be (n×m)(m×p)=(n×p)");
        }

        // perform multiplication
        Matrix C = new Matrix(A.m, B.n);
        for (int i = 0; i < C.m; i++) {
            for (int j = 0; j < C.n; j++) {
                for (int k = 0; k < A.n; k++) {
                    C.entries[i][j] += (A.entries[i][k] * B.entries[k][j]);
                }
            }
        }
        return C;
    }

    /** 
     * Create a string
     */
    public String toString() {
        String out = "";
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                out += this.entries[i][j];
                out += " ";
            }
            out += "\n";
        }
        return out;
    }

    /**
     * testing method
     */
    public static void main(String[] args) {
        double[][] myList = {{1.0, 2.0},{3.0, 3.0}};
        Matrix m = new Matrix(myList);
        System.out.println(m.multiply(4));
        double[][] myList2 = {{-1.0, -2.0},{-3.0, -3.0}};
        Matrix m2 = new Matrix(myList2);
        System.out.println(m.add(m2));
        System.out.println(m.subtract(m2));
        Matrix a = new Matrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        Matrix b = new Matrix(new double[][]{{3.0},{4.0}});
        System.out.println(a.multiply(b));
                              
    }
}
