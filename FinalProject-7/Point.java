import java.awt.Graphics;

public class Point extends Pair implements Drawable {
private static int radius = 12;
public Point(double x, double y){
        //constructor is the same as for a point
        super(x, y);
        }
public void draw(Graphics g) {
        //draw the point based on the world's center
        //we draw it the same size regardless, so don't account for scale
        int xOnScreen = (int) (x * 100 * Main.world.scale - Main.world.center.x) - radius / 2;
        int yOnScreen = (int) (-y * 100 * Main.world.scale + Main.world.center.y + 360 - radius / 2);
        g.fillOval(xOnScreen, yOnScreen, radius, radius);
        }
}

