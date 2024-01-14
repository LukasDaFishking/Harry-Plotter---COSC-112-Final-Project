import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InputHandler implements ActionListener, MouseListener{
    //various gui components
    JTextField textField;
    JButton button;
    public JTextField prime;
    public JButton primeSwap;
    //the curve being plotted
    public Curve c;
    //whether it is elliptic
    public boolean isElliptic;
    //how many we have created
    private static int count = 1;
    //so that we can check if one InputHandler equals another
    //identifier is based on count
    private int identifier;
    //stack of colors we will use
    public static GenericStack<Color> colorList = new GenericStack<Color>();

    public InputHandler(String str, int columns){
        //create an input handler
        identifier = count;
        count++;
        isElliptic = false;
        //if it is in fact elliptic we will update that later
        //make the gui components
        textField = new JTextField(str, columns);
        textField.addActionListener(this);
        textField.addMouseListener(this);
        button = new JButton( new AbstractAction("X") {
            //lets us remove an input handler
            public void actionPerformed( ActionEvent e ) {
                if(Main.inputs.get(Main.inputs.size()-1).identifier != identifier) {
                    Main.removeInputHandler(InputHandler.this);
                }
                if(c != null) {
                    //if there is a curve also remove that and return its color
                    Main.world.removeCurve(c);
                    colorList.add(c.color);
                }
                Main.mainInstance.requestFocus();
            }
        });
        button.setMargin(new Insets(0,0,0,0));
        button.setPreferredSize(new Dimension(30, 19));
        primeSwap = new JButton("Use prime field with prime:");
        primeSwap.setFont(new Font("Arial", Font.BOLD, 11));
        primeSwap.setMargin(new Insets(0,0,0,0));
        primeSwap.setPreferredSize(new Dimension(150, 19));
        primeSwap.addActionListener(new ActionListener(){
            //this button will only appear if it is an elliptic curve
            //it lets us swap between plotting the curve and the points
            //in a prime field
            public void actionPerformed(ActionEvent e){
                Main.mainInstance.requestFocus();
                if(c.prime == 0 && !prime.getText().isEmpty()){
                    //error checking
                    boolean isInt = false;
                    try {
                        Integer.parseInt(prime.getText());
                        isInt = true;
                    }
                    catch(NumberFormatException error){
                    }
                    if(isInt) {
                        if(Integer.parseInt(prime.getText()) < 100 && isPrime(Integer.parseInt(prime.getText()))){
                            c.updatePrime(Integer.parseInt(prime.getText()));
                            prime.setVisible(false);
                            primeSwap.setText("Plot continuous curve");
                        }
                    }
                }
                else{
                    primeSwap.setText("Use prime field with prime:");
                    c.prime = 0;
                    prime.setVisible(true);
                }
            }
        });
        prime = new JTextField("", 2);
    }
    public void actionPerformed(ActionEvent e){
        //this is what happens when you press enter in an input handler's main textbox
        Main.mainInstance.requestFocus();
        if(c != null) {
            //if there was a curve before, remove it
            Main.world.removeCurve(c);
            colorList.add(c.color);
        }
        //parse whatever is in the textbox
        double[] coefs = StringParser.parseEquation(textField.getText());
        if(coefs.length > 1){
            //this implies that it parsed successfully
            boolean isZero = true;
            //check if all coefficients are 0
            //starting at 1 is intentional
            for (int i = 1; i < coefs.length; i++) {
                if (coefs[i] != 0) {
                    isZero = false;
                    break;
                }
            }
            if(!isZero) {
                //if not all the coefficients of the curve are 0
                if (coefs[9] == 2 && coefs[2] == 0 && coefs[3] == 1 && coefs[4] == 0 && coefs[5] == 0 && coefs[6] == 0 && coefs[7] == 0 && coefs[8] == 0) {
                    //if it is an elliptic curve
                    c = new EllipticCurve(coefs, Main.world, colorList.pop());
                    isElliptic = true;
                    //we need a temp input handler since we will have to remove
                    //this one to put it back in as an input handler for an elliptic curve
                    //without the temp input handler if this was the last one, the gui would
                    //get messed
                    InputHandler temp = new InputHandler("", 15);
                    Main.addInputHandler(temp);
                    Main.removeInputHandler(this);
                    Main.addInputHandler(this);
                    Main.removeInputHandler(temp);
                } else {
                    //if it isn't elliptic, set the elliptic specific stuff to be invisible
                    c = new Curve(coefs, Main.world, colorList.pop());
                    primeSwap.setVisible(false);
                    prime.setVisible(false);
                }
                //add the curve to world
                Main.world.addCurve(c);
                //if that was the last input handler, add another one
                if(Main.inputs.get(Main.inputs.size()-1).c != null) {
                    Main.addInputHandler(new InputHandler("", 15));
                }
            }
        }
        else{
            //display error message if something went wrong
            if(coefs[0] == -1){
                textField.setText("Unrecognized character");
            }
            else if(coefs[0] == -2){
                textField.setText("Expected number, found operation");
            }
            else if(coefs[0] == -3){
                textField.setText("Only 1 x or y per term allowed");
            }
            else if(coefs[0] == -4){
                textField.setText("Y cannot have a coefficient");
            }
            else if(coefs[0] == -5){
                textField.setText("Only use ^ after x or y");
            }
            else if(coefs[0] == -6){
                textField.setText("Powers of x should be from 0 to 8");
            }
            else{
                textField.setText("Unrecognized error");
            }
        }
    }

    public void mouseClicked(MouseEvent e){
        //remove the default text when clicked
        if(textField.getText().equals("Type equation here!")){
            textField.setText("");
        }
    }
    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}

    public static void initColorQueue(){
        //put all of the colors into the stack
        //badly named method tbh
        InputHandler.colorList.add(new Color(61, 47, 40));
        InputHandler.colorList.add(new Color(204, 53, 176));
        InputHandler.colorList.add(new Color(45, 128, 156));
        InputHandler.colorList.add(new Color(204, 106, 8));
        InputHandler.colorList.add(new Color(133, 19, 214));
        InputHandler.colorList.add(new Color(10, 89, 18));
        InputHandler.colorList.add(new Color(5, 5, 158));
        InputHandler.colorList.add(new Color(227, 29, 11));
    }

    public void setCurve(Curve curve){
        //we only use this when generating a random curve
        //this lets us force an input handler to have certain values
        //since it is only used in one very specific place
        //there is not much error checking
        if(c != null) {
            Main.world.removeCurve(c);
            colorList.add(c.color);
        }
        c = curve;
        //we know its true since it was a randomly generated elliptic curve
        isElliptic = true;
        //make the string that will be displayed
        String newText = "y^" + (int)c.coefficients[9] + "=";
        for(int i = 8; i > 1; i--){
            if(c.coefficients[i] != 0) {
                newText = newText + (int)c.coefficients[i] + "x^" + i + "+";
            }
        }
        if(c.coefficients[1] != 0) {
            newText = newText + (int)c.coefficients[1] + "x+";
        }
        if(c.coefficients[0] != 0){
            newText = newText + (int)c.coefficients[0];
        }
        //do the gui stuff to make this an elliptic curve input handler
        Main.world.addCurve(c);
        textField.setText(newText);
        InputHandler temp = new InputHandler("", 15);
        Main.addInputHandler(temp);
        Main.removeInputHandler(this);
        Main.addInputHandler(this);
        Main.removeInputHandler(temp);
    }

    public static boolean isPrime(int n) {
        for (int i = 2; i < n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
