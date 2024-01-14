import java.awt.*;
import java.util.*;

//elliptic curves are curves of the form y^2=x^3+ax+b
public class EllipticCurve extends Curve{
    public ArrayList<Point> primeFieldPoints;
    //this is where we will store the points in the prime field, so we don't have to
    //constantly recalculate
    public EllipticCurve(double[] initCoefficients, World initWorld, Color color) {
        super(initCoefficients, initWorld, color);
    }
    @Override
    public void draw(Graphics g){
        //draw method, if the prime is 0 we just draw normally
        if(prime == 0){
            super.draw(g);
        }
        else{
            //otherwise we draw an arraylist of points that we will have calculated
            g.setColor(color);
            for(int i = 0; i < primeFieldPoints.size(); i++){
                primeFieldPoints.get(i).draw(g);
            }
        }
    }

    public void updatePrime(int newP){
        //this will be called whenever we change the prime, and it recalculates the points
        if(InputHandler.isPrime(newP)){
            primeFieldPoints = new ArrayList<Point>();
            prime = newP;
            //go through the values of x
            for(int x = 0; x < prime; x++){
                //calculate the sum of their x coefficients
                double xSum = (Math.pow(x, 3) + x*coefficients[1] + coefficients[0]);
                if(xSum == (int)xSum){
                    //figure out what that is mode the prime
                    int xMod = (int)xSum % prime;
                    if(xMod < 0){
                        //we have to do negatives a little differently
                        xMod = prime - Math.abs(xMod);
                    }
                    //now for each x go through each y
                    for(int y = 0; y < prime; y++){
                        //figure out what the mod of y^2 is
                        if(Math.pow(y, 2) % prime == xMod){
                            //if it's the same then that's a point we want too plot
                            primeFieldPoints.add(new Point(x, y));
                        }
                    }
                }
            }
        }
    }
}
