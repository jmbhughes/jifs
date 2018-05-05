import static java.lang.Math.random;
import static java.lang.Math.round;
import java.awt.Color;

/**
 * a requirement for any transformation for the IFS
 * each transform needs:
 *<ul>
 *<li> a name for logging purposes
 *<li> an initial starting color
 *<li> a definition of how to transform
 *<li> a string representation 
 *</ul>
 * @author J. Marcus Hughes
 */
abstract class Transform {
    String transformName;
    Color color;

    /** 
     * Basic constructor
     */
    Transform(String name) {
        this.transformName = name;
        
        this.color = new Color(randomColorChannel(),
                               randomColorChannel(),
                               randomColorChannel());
    }

    /**
     * utitlity function to get a random RGB color channel
     */
    private static int randomColorChannel() {
        return (int) Math.round(Math.random() * 255.0);
    }

    /**
     * how colors should change as the function evaluates 
     * @param c what color is being considered
     */
    public void transform_color(Color c) {
        this.color = new Color((this.color.getRed() + c.getRed()) / 2,
                               (this.color.getGreen() + c.getGreen()) / 2,
                               (this.color.getBlue() + c.getBlue()) / 2);
    }

    /**
     * the definition of where <code> p </code> should go under this transform
     * @param p starting point
     * @return location after starting from <code> p </code> 
     */
    abstract public Coordinate transform(Coordinate p);

    /**
     * simple string method
     */
    abstract public String toString();
}
