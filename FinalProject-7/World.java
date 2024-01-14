import java.awt.*;
import java.util.ArrayList;

//World is where we do will draw everything and store center and scale
class World{
    public ArrayList<Curve> curves;
    //this is the list of curves that we will be plotting
    public Pair center;
    public double scale;
    public int width;
    public int height;

    //The default scale of the image
    public final double defaultScale = 1;

    public World(int width, int height, int scale){
        //constructor, only used once
        curves = new ArrayList<Curve>();
        this.width = width;
        this.height = height;

        //sets the center at the origin
        this.center =  new Pair(-Main.WIDTH/2,0);
        this.scale = defaultScale;
    }

    //lets us add curves
    public void addCurve(Curve c){
        curves.add(c);
    }

    //lets us remove curves
    public void removeCurve(Curve c){curves.remove(c);}

    //draw all of the things
    public void draw(Graphics g){
        drawGrid(g);
        drawAxis(g);
        for(int i = 0; i < curves.size(); i++) {
            curves.get(i).draw(g);
        }
    }

    //code triggered when the user zooms out
    public void zoomOut(){
        //they can't zoom out past a certain point
        if(1/scale < 10000){
            //use zoomQueue sort of backwards, so we take out the last element
            //and then put it in at the front
            //zooming in will have this reversed
            //this just ensures that we will zoom in and out by the same multiples
            //so that the numbers always remain nice
            double temp = Main.zoomQueue.popLast();
            scale /= temp;
            Main.zoomQueue.add(temp);
            //update the displayed scale
            Main.zoomLabel.setText("Scale: " + 1/scale);
            Main.settingsPanel.revalidate();
            Main.settingsPanel.repaint();
        }
    }

    public void zoomIn(){
        //very similar to zoom out, just the reverse
        if(1/scale > (10e-10)) {
            double temp = Main.zoomQueue.pop();
            scale *= temp;
            Main.zoomQueue.addLast(temp);
            Main.zoomLabel.setText("Scale: " + 1/scale);
            Main.settingsPanel.revalidate();
            Main.settingsPanel.repaint();
        }
    }
    public void drawAxis(Graphics g){
        //draw the axes based on where the center of the screen is
        //x axis
        g.setColor(Color.black);
        g.fillRect((int) -center.x, (int) center.y+358, 10000, 4);
        g.fillRect((int) center.x, (int) center.y+358, 10000, 4);
        //y axis
        g.fillRect((int) -center.x-2, (int) 0, 4, 1000);
    }

    public void drawGrid(Graphics g){
        //drawing the grid is surprisingly complicated
        g.setColor(Color.lightGray);
        g.setFont(new Font(g.getFont().getFontName(), java.awt.Font.PLAIN, 12));

        //at a scale of 1, 1 = 100 units
        int counter = 0;
        double multiple;
        int decimalPlaces = 0;
        //multiple is what the labels on the grid are
        //we will have 3 unlabelled lines between each labelled one
        if(scale <= 1) {
            //this implies we are zoomed out, so we can have whole number labels
            multiple = (int) (1.0 / scale);
            //we will have to account for rounding error that occurs, though
        }
        else {
            //here we can't use whole numbers, but we still want to round multiple
            //this code rounds truncates multiple to only have 1 nonzero decimal
            //(for example 0.002642 becomes 0.002)
            multiple = (1 / scale);
            while (multiple < 1) {
                multiple *= 10;
                decimalPlaces++;
            }
            multiple = Math.round(multiple);
            for (int j = 0; j < decimalPlaces; j++){
                multiple /= 10;
            }

        }
        //draw grid lines until we reach the edge of the screen
        //basically we are adjusting multiple to account for zoom and center
        //then we do the same for quarter multiples of multiple
        //(we would have to multiply by 100 to do whole multiples)
        while((multiple*counter*(25*scale)) - center.x < Main.WIDTH) {
            if(counter % 4 == 0){
                //every fourth grid line make it darker
                g.setColor(Color.gray);
                //and label it
                g.drawString(Double.toString(Math.round(counter/4 * multiple*Math.pow(10,decimalPlaces))/Math.pow(10,decimalPlaces)), (int)((multiple*(25*scale))*counter - center.x) + 5, (int) center.y+355);
            }
            //actually draw the grid line
            g.fillRect((int)((multiple*(25*scale))*counter - center.x), 0, 2, 1000);
            g.setColor(Color.lightGray);
            //increment counter so that the next grid line is drawn the appropriate distance away
            counter += 1;
        }
        //do the same but in the negative direction now
        counter = 0;
        while(((multiple*(25*scale))*counter - center.x) > 0) {
            if(counter % 4 == 0){
                g.setColor(Color.gray);
                g.drawString(Double.toString(Math.round(counter/4 * multiple*Math.pow(10,decimalPlaces))/Math.pow(10,decimalPlaces)), (int)((multiple*(25*scale))*counter - center.x)+ 5, (int) center.y+355);

            }
            g.fillRect((int)((multiple*(25*scale))*counter - center.x), 0, 2, 1000);
            g.setColor(Color.lightGray);
            counter -= 1;
        }
        //now draw horizontal grid lines
        //the code is very similar to above
        counter = 0;
        while((multiple*(25*scale))*counter + center.y+360 < Main.HEIGHT) {
            if(counter % 4 == 0){
                g.setColor(Color.gray);
                g.drawString(Double.toString(-Math.round(counter/4 * multiple*Math.pow(10,decimalPlaces))/Math.pow(10,decimalPlaces)),(int)-center.x + 5, (int)((multiple*(25*scale))*counter + center.y+360)-5);
            }
            g.fillRect(0, (int)((multiple*(25*scale))*counter + center.y+360), 2000, 2);
            g.setColor(Color.lightGray);
            counter += 1;
        }
        //and draw horizontal gridlines below the x axis
        counter = 0;
        while((multiple*(25*scale))*counter + center.y+360 > 0) {
            if(counter % 4 == 0){
                g.setColor(Color.gray);
                g.drawString(Double.toString(-Math.round(counter/4 * multiple*Math.pow(10,decimalPlaces))/Math.pow(10,decimalPlaces)),(int)-center.x + 5, (int)((multiple*(25*scale))*counter + center.y+360)-5);
            }
            g.fillRect(0, (int)((multiple*(25*scale))*counter + center.y+360), 2000, 2);
            g.setColor(Color.lightGray);
            counter -= 1;
        }
    }
}