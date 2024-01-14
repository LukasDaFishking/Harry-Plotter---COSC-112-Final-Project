import java.awt.*;

import static java.lang.Double.NaN;

//This class respresents the curves that we will be plotting
public class Curve implements Drawable{
    public double[] coefficients;
    public Color color;
    private World world;
    //prime is not used in curve class, but is in elliptic curve
    //it makes things more convenient if it is here too
    public int prime;

    public Curve(double[] initCoefficients, World initWorld, Color color){
        //constructor, assigns values to what is passed in
        coefficients = initCoefficients;
        world = initWorld;
        this.color = color;
        prime = 0;
    }
    public void draw(Graphics g){
        //this method is what actually plots the curves
        //we will use prevY(2) to fill in parts of the curve that we miss
        double prevY = NaN;
        double prevY2 = NaN;
        //iterate over the width of the screen
        for(int i = 0; i < world.width; i++){
            //true y is the value of y for the curve that is being drawn
            double trueY = 0;
            //calculate x based on where we are on the screen, where the origin is, and the zoom
            double x = ((double)i + world.center.x)/(100*world.scale);
            //calculate trueY based on our coefficients
            for(int j = 0; j < 9; j++){
                trueY += coefficients[j]*Math.pow(x, j);
            }
            //now account for the power of y
            trueY = Math.pow(trueY, 1/(double)coefficients[9]);
            //stops us from graphing y when it doesn't exist
            if(!Double.isNaN(trueY)) {
                //adjY is the value of y that we will plot on the screen
                //different than the value of y in the function
                double adjY = adjust(-trueY);
                //actually plot the point
                //although we plot it point by point we are not actually using the point class
                //we never store this information, so there's no reason to
                plotPoint(i, (int) adjY, g);
                //for even powers of y we also need to plot the negative of the trueY
                if (coefficients[9] % 2 == 0) {
                    plotPoint(i, (adjust(trueY)), g);
                }
                //if we plotted a point previously
                //this code helps us to connect parts of curves with high slopes
                if (!Double.isNaN(prevY) && Main.continuous) {
                    //if statement lets us toggle this in the gui
                    if (adjY < prevY) {
                        //some of the math changes depending on whether the slope is + or -
                        //basically just fills a rectangle between the two last plotted points
                        //to make it more continuous
                        plotRectangle((int)(prevY - adjY), i, (int)adjY, g);
                        if (coefficients[9] % 2 == 0) {
                            //again for even powers of y we have to plot the negative version
                            plotRectangle((int)(prevY - adjY), i, (int)(adjust(trueY)-(prevY - adjY)), g);
                        }
                    }
                    else {
                        //same as before, just slightly different math
                        plotRectangle((int)(adjY-prevY), i, (int)prevY, g);
                        if (coefficients[9] % 2 == 0) {
                            plotRectangle((int)(adjY-prevY), i, adjust(trueY), g);
                        }
                    }
                }

                //sometimes if a curve only starts at a certain value of x, that x will not
                //align with any pixel on the screen and we would lose some of the curve
                //this code counteracts that
                else if(Double.isNaN(prevY) && coefficients[9] % 2 == 0 && i != 0){
                    if(Main.continuous){
                        if(adjY > adjust(trueY)){
                            //we basically just plot a rectangle between the next point that we plot
                            //and its negative version
                            //it looks very similar to if we hadn't missed the starting x value
                            plotRectangle((int)(adjY -adjust(trueY)), i, adjust(trueY), g);
                        }
                        else{
                            //again math slightly changes based on slope
                            plotRectangle((int)(adjust(trueY)- adjY), i, (int)adjY, g);
                        }
                    }
                    else{
                        //if we're plotting as dots rather than continuous, just plot a dot instead
                        plotPoint(i, (int)((adjY+adjust(trueY))/2), g);
                    }
                }
                prevY = adjY;
                prevY2 = adjust(trueY);
            }
            else{
                //very similar to the previous code, but this one
                //is used for if curve ends at a certain x value
                if(!Double.isNaN(prevY)){
                    plotRectangle(Math.abs((int)(Double.max(prevY, prevY2) - Double.min(prevY, prevY2))), i-1, (int)(Double.min(prevY, prevY2)), g);
                }
                prevY = NaN;
                prevY2 = NaN;
            }


        }
        }

    //helper functions for plotting
    public void plotPoint(int i, int y, Graphics g){
        //plots points as a small circle
        g.setColor(color);
        g.fillOval(i, y, 5, 5);
    }
    public void plotRectangle(int height, int i, int y, Graphics g) {
        g.setColor(color);
        g.fillRect(i, y, 5, height);
    }
    public int adjust(double y){
        y *= (100*world.scale); //adjust y based on scale
        y += (357 + world.center.y); //adjust it based on where the origin is
        return (int)y;
    }

    //this will be used in EllipticCurve, but it is helpful to have unused here too
    public void updatePrime(int p){
    }
}
