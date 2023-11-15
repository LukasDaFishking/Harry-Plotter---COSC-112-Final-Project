import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.util.Random;

class Pair {
    public double x;
    public double y;

    public Pair(double setX, double setY) {
        this.x = setX;
        this.y = setY;
    }

    public Pair add(Pair input1) {
        plus = new Pair(this.x + input1.x, this.y + input1.y);
        return (plus);
    }

    public Pair times(double input2) {
        mult = new Pair(this.x * input2, this.y * input2);
        return (mult);
    }

    public Pair divide(double input3) {
        this.x = this.x / input3;
        this.y = this.y / input3;
        return (this);
    }

    public void flipY() {
        this.y = -this.y;
    }

    public void flipX() {
        this.x = -this.x;
    }
}

class World {
    int height;
    int width;

    public World(int initWidth, int initHeight, int initNumShapes) {
        width = initWidth;
        height = initHeight;

        }
}

public class Plotter extends JPanel {
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 768;
    public static final int FPS = 60;
    World world;

    public void run() {
        while (true) {
            //world.updateShapes(1.0 / (double) FPS);
            //repaint();
            try {
                Thread.sleep(1000 / FPS);
            } catch (InterruptedException e) {
            }
        }

    }

    public Plotter() {
        world = new World(WIDTH, HEIGHT, 50);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Harry Plotter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Plotter mainInstance = new Plotter();
        frame.setContentPane(mainInstance);
        frame.pack();
        frame.setVisible(true);
        mainInstance.run();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        //world.drawShapes(g);
    }
}
