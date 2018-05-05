import static java.lang.Math.random;


/** 
 * <code> Coordinate </code> is a representation of a point in the plane
 */
final public class Coordinate {
    final private double x; // horizontal term
    final private double y; // vertical term

    /**
     * a random number between bounds using <code> Math.random </code>
     * @param min lower end of the bound for random generation
     * @param max upper end of bound for random generation
     * @return a random float between <code> min </code> and <code> max </code>
     */
    static double randomWithRange(double min, double max) {
        double range = (max - min);
        return (Math.random() * range) + min;
    }

    /**
     * Default constructor returns random point in [-1,1] X [-1,1]
     */
    Coordinate() {
        this.x = randomWithRange(-1.0, 1.0);
        this.y = randomWithRange(-1.0, 1.0);
    }

    /**
     * integer constructor
     */
    Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * double constructor
     */
    Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * return horizontal portion
     */
    public double getX() { return this.x;}

    /**
     * return vertical portion
     */
    public double getY() { return this.y;}

    /**
     * elementwise addition of coordinates
     * @param p coordinate to add 
     * @return the sum of <code> this </code> and <code> p </code>
     */
    public Coordinate add(Coordinate p) {
        return new Coordinate(this.x + p.x, this.y + p.y);
    }

    /**
     * converts a point representing a relative, fractional location into an absolute term
     * This assumes 0 <= x, y <= 1
     * @param width how many pixels wide the region is
     * @param height how many pixels high the region is
     * @return a version of the point scaled to a larger region
     */
    public Coordinate scale(int width, int height) {
        return new Coordinate((this.x + 1) * width / 2,
                              (this.y + 1) * height / 2);
    }

    /**
     * find nearest lattice point 
     * @return coordinate with elements converted to integers */
    public Coordinate integerize() {
        return new Coordinate((int) this.x,
                              (int) this.y);
    }

    /**
     * default string constructor
     */
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }
    

    /**
     * testing main
     */
    public static void main(String[] args){
        Coordinate p1 = new Coordinate(5,5);
        Coordinate p2 = new Coordinate(6,7);
        System.out.println(p1.add(p2));
        Coordinate p3 = new Coordinate();
        System.out.println(p3);
    }
}

