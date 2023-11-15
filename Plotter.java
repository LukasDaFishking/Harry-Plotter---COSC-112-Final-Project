import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.util.Random;

public class Plotter extends JPanel {
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 768;
    public static final int FPS = 60;
    public World world;

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
        super.paintComponent(g
        world.draw();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        //world.drawShapes(g);
    }
}
