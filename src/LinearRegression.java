import java.util.Vector;
import java.util.function.Function;
import static java.lang.Math.random;

/**
 * Simple linear regression resulting in model of y = ax + b for data points
 * @author J. Marcus Hughes
 */
public class LinearRegression {
    double[] xs;         // input reference data
    double[] ys;         // input predicted value
    double xbar;         // mean of xs
    double xbarsquared;  // mean of xs squared i.e. sum(x^2)  / n
    double ybar;         // mean of ys
    double ybarsquared;  // mean of ys squared, see xbarsquared
    double beta0;        // constant term, b, in model y = ax + b
    double beta1;        // slope term, a, in model y = ax + b
    double xxbar;        // square of x - xbar over xs
    double yybar;        // square of y - ybar over ys
    double xybar;        // (x - xbar) * (y - ybar) over xs, xs
    double R2;           // correlation term
    double svar1;        // variance of slope 
    double svar0;        // variance of constant
    double rss;          // residual sum of squares
    double ssr;          // regression sum of squares

    /**
     * Construct a <code>LinearRegression</code> object with input data
     * automatically performs the regression and the model can be found
     * <code> model() </code> or seen in String, exact values are in 
     * <code>this.beta0</code> for constant and <code>this.beta1</code> 
     * for slope term
     * @param xs certain values
     * @param ys predicted values
     */
    LinearRegression(double[] xs, double[] ys) {
        this.xs = xs;
        this.ys = ys;

        // calculate the means
        double[] xbars = mean(xs);
        this.xbar = xbars[0];
        this.xbarsquared = xbars[1];

        double[] ybars = mean(ys);
        this.ybar = ybars[0];
        this.ybarsquared = ybars[1];

        // determine certainty and fit
        fit();
    }
    
    /**
     * Calculates the mean of some input array
     * @param xs array of values to average, must be nonempty
     * @return average of values
     */
    private static double[] mean(double[] xs) {
        if (xs.length == 0)
            throw new RuntimeException("Input array must be nonempty");
        double sum = 0.0;
        double sumsquared = 0.0;
        for (double x: xs) {
            sum += x;
            sumsquared += (x * x);
        }
        return new double[]{sum/xs.length, sumsquared/xs.length};
    }


    /** 
     * Performs fit and calculates summary statistics
     */
    private void fit() {
        double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
        for (int i = 0; i < xs.length; i++) {
            xxbar += (xs[i] - xbar) * (xs[i] - xbar);
            yybar += (ys[i] - ybar) * (ys[i] - ybar);
            xybar += (xs[i] - xbar) * (ys[i] - ybar);
        }
        // determine fit parameters
        double beta1 = xybar / xxbar;
        double beta0 = ybar - beta1 * xbar;

        // store values
        this.xxbar = xxbar;
        this.yybar = yybar;
        this.xybar = xybar;
        this.beta0 = beta0;
        this.beta1 = beta1;

        // determine summary stats
        int df = xs.length - 2;
        this.rss = 0.0; // residual sum of squares
        this.ssr = 0.0; // regression sum of squares
        for (int i = 0; i < xs.length; i++) {
            double fit = beta1*xs[i] + beta0;
            rss += (fit - ys[i]) * (fit - ys[i]);
            ssr += (fit - ybar) * (fit - ybar);
        }
        this.R2    = ssr / yybar;
        double svar  = rss / df;
        this.svar1 = svar / xxbar;
        this.svar0 = svar/xs.length + xbar*xbar*svar1;
    }

    /** 
     * Returns model as callable function. Use by calling with apply(VALUE)
     * For example:
     * Function<Double, Double> f = m.model();
     * f.apply(3.4);
     */
    public Function<Double, Double> model() {
        return (Double x) -> this.beta1 * x + this.beta0;
    }

    /** 
     * Creates a string with linear model and parameters
     */
    public String toString() {
        return String.format("y = %.2f x + %.2f with R^2=%.2f", beta1, beta0, R2);
    }

    /**
     * Testing method
     */
    public static void main(String[] args) {
        double[] xs = new double[]{2.0, 3.0, 4.0};
        LinearRegression m = new LinearRegression(xs, xs);
        System.out.println(m.mean(xs)[0]);
        System.out.println(m.xbar);
        System.out.printf("y = %.2f x + %.2f\n", m.beta1, m.beta0);
        System.out.println(m);
        Function<Double, Double> f = m.model();
        System.out.println(f.apply(2.0));
    }
}
