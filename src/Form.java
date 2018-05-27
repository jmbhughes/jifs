// Layouts
import java.awt.BorderLayout;
import java.awt.GridLayout;

// Action
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Components
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.text.NumberFormatter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

// Formatting
import java.text.DecimalFormat;
import java.text.NumberFormat;

// Data structures
import java.util.Vector;

/** 
 * A simple form to input affine transformations and create the resulting IFS
 */
public class Form extends JPanel implements ActionListener {

    // vector of affine transformations
    private Vector<Transform> transforms;

    // fields for the linear transformation [[a,b],[c,d]] and translation [e,f] components
    private JFormattedTextField aField;
    private JFormattedTextField bField;
    private JFormattedTextField cField;
    private JFormattedTextField dField;
    private JFormattedTextField eField;
    private JFormattedTextField fField;

    // panels to organize the application
    private JPanel matrixPanel;
    private JPanel buttonPanel;

    // interactive buttons
    private JButton addButton;
    private JButton runButton;

    // formats for the matrix entries
    private NumberFormat numberFormat;
    private NumberFormatter numberFormatter;

    /** construct one of the affine transformation fields with a default value */
    private JFormattedTextField createField(double value){
        JFormattedTextField field = new JFormattedTextField(numberFormatter);
        field.setValue(new Double(value));
        field.setColumns(5);
        return field;
    }

    /** setup the panel for entering the matrix values */
    private void createMatrixPanel() {
        matrixPanel = new JPanel(new GridLayout(2, 3));
        aField = createField(0.5);
        bField = createField(0.0);
        cField = createField(0.0);
        dField = createField(0.5);
        eField = createField(0.0);
        fField = createField(0.0);

        matrixPanel.add(aField);
        matrixPanel.add(bField);
        matrixPanel.add(eField);
        matrixPanel.add(cField);
        matrixPanel.add(dField);
        matrixPanel.add(fField);                        
    }

    /** setup the panel for button interaction */
    private void createButtonPanel() {
        buttonPanel = new JPanel(new GridLayout(2, 1));

        addButton = new JButton("add transformation");
        addButton.setActionCommand("add");
        addButton.addActionListener(this);

        runButton = new JButton("run");
        runButton.setActionCommand("run");
        runButton.addActionListener(this);

        buttonPanel.add(addButton);
        buttonPanel.add(runButton);
    }

    /** the form constructor */
    public Form() {
        super(new BorderLayout());

        transforms = new Vector<Transform>();

        setUpFormats();

        createMatrixPanel();
        createButtonPanel();

        // add the panels to the application
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(buttonPanel, BorderLayout.LINE_START);
        add(matrixPanel, BorderLayout.LINE_END);
    }

    /** Called when a field's "value" property changes. */
    public void propertyChange(PropertyChangeEvent e) {
        /*
        Object source = e.getSource();
        if (source == amountField) {
            amount = ((Number)amountField.getValue()).doubleValue();
        } else if (source == rateField) {
            rate = ((Number)rateField.getValue()).doubleValue();
        } else if (source == numPeriodsField) {
            numPeriods = ((Number)numPeriodsField.getValue()).intValue();
        }

        double payment = computePayment(amount, rate, numPeriods);
        paymentField.setValue(new Double(payment));
        */
    }

    /** get the matrix entry value and cast to double */
    private double getMatrixFieldValue(JFormattedTextField field) {
        Object value = field.getValue();
        if(value instanceof Long) {
            return (double)((long) value);
        } else if (value instanceof Double) {
            return (double) value;
        } else {
            return 0.0;
        }
    }

    /**
     * action to perform when add button is clicked: 
     * creates an affine transformation and adds it to the vector
     */
    private void addAction() {
        double a = getMatrixFieldValue(aField);
        double b = getMatrixFieldValue(bField);
        double c = getMatrixFieldValue(cField);
        double d = getMatrixFieldValue(dField);
        double e = getMatrixFieldValue(eField);
        double f = getMatrixFieldValue(fField);

        Matrix m = new Matrix(a, b, c, d);
        Matrix t = new Matrix(e, f);
        System.out.println(m);
        System.out.println(t);
        
        transforms.add(new AffineTransform(m, t));
    }

    /**
     * action to perform when run button is clicked:
     * creates an image with the specified IFS
     */
    private void runAction() {
        IFS system = new IFS(transforms);
        RandomIFSEvaluator ifsRunner = new RandomIFSEvaluator(system, 1000, 50);
        ifsRunner.run();
        ifsRunner.plot("form.jpeg", 500, 500, 50, -1.0, 1.0, -1.0, 1.0);
    }

    /** listens for requested actions */
    public void actionPerformed(ActionEvent e) {
        if ("add".equals(e.getActionCommand())) {
            addAction();
        }
        else if("run".equals(e.getActionCommand())) {
            runAction();
        }
        else{
            System.out.println("Not defined!");
        }
    }
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add contents to the window.
        frame.add(new Form());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    /** RUN! */
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
	        UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }

    /**
     * Create and set up number formats. These objects also
     * parse numbers input by user.
     */
    private void setUpFormats() {
        numberFormat = new DecimalFormat("#00.00");
        numberFormatter = new NumberFormatter(numberFormat);
    }
}
