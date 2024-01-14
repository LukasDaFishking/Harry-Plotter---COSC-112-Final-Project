import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Main extends JPanel implements MouseListener, KeyListener{
    //set up constants
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 768;
    public static final int FPS = 30;
    //set up certain values and objects that we will have to reference throughout various methods and/or classes
    public static boolean continuous = true;
    int lastPressedX;
    int lastPressedY;
    boolean pan;

    static JPanel inputPanel;
    static JPanel settingsPanel;
    public static JLabel zoomLabel;
    public static World world;
    public static GenericStack<Double> zoomQueue;
    public static JButton random;
    public static ArrayList<InputHandler> inputs;
    public static Main mainInstance;

    public void run() {
        while (true) {
            //make the program actually run
            if(pan){
                //if panning is going on, update the center accordingly based on how far the mouse moved
                world.center.x -= (MouseInfo.getPointerInfo().getLocation().x - lastPressedX);
                lastPressedX = MouseInfo.getPointerInfo().getLocation().x;
                world.center.y += (MouseInfo.getPointerInfo().getLocation().y - lastPressedY);
                lastPressedY = MouseInfo.getPointerInfo().getLocation().y;
            }
            //update the screen
            repaint();
            try {
                Thread.sleep(1000 / FPS);
            } catch (InterruptedException e) {
            }

        }

    }

    public Main() {
        //contructor, sets up various things
        world = new World(WIDTH, HEIGHT, 1);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addKeyListener(this);
        pan = false;
        lastPressedX = 0;
        lastPressedY = 0;
        //using nullLayout is not advised, but it seems to be the only way
        //that I can figure out to get what I want (panels at a certain place)
        this.setLayout(null);
    }

    public static void main(String[] args) {
        //what happens when you run the program
        mainInstance = new Main();
        //create the jframe
        JFrame frame = new JFrame("Harry Plotter");
        //set up the border for the input panels
        Border border = BorderFactory.createLineBorder(Color.black);
        //set up the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(mainInstance);
        frame.addMouseListener(mainInstance);
        frame.pack();
        //create and set up the input and settings panels
        inputPanel = new JPanel();
        settingsPanel = new JPanel();
        inputPanel.setBorder(border);
        settingsPanel.setBorder(border);
        inputPanel.setSize(new Dimension(200, 203));
        //make the array of InputHandlers
        inputs = new ArrayList<InputHandler>();
        //create the first InputHandler and add it to everything
        InputHandler i = new InputHandler("Type equation here!", 15);
        inputs.add(i);
        inputPanel.add(i.textField);
        inputPanel.add(i.button);
        //button for switching between continuous and dotted
        JButton switchMode = new JButton("Plot dotted curves");
        switchMode.setPreferredSize(new Dimension(180, 20));
        switchMode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(continuous == true){
                    switchMode.setText("Plot curves continuously");
                    continuous = false;
                }
                else{
                    switchMode.setText("Plot dotted curves");
                    continuous = true;
                }
                mainInstance.requestFocus();
            }
        });
        //returns the center of the screen to the origin
        JButton centerOrigin = new JButton("Center origin");
        centerOrigin.setPreferredSize(new Dimension(93, 20));
        centerOrigin.setMargin(new Insets(0,0,0,0));
        centerOrigin.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                world.center.x = -WIDTH/2;
                world.center.y = 0;
                mainInstance.requestFocus();
            }
        });
        //display the zoom and have a button to reset it
        zoomLabel = new JLabel("Scale: 1");
        JButton resetScale = new JButton("Reset scale");
        resetScale.setPreferredSize(new Dimension(84, 20));
        resetScale.setMargin(new Insets(0,0,0,0));
        resetScale.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                world.scale = 1;
                zoomLabel.setText("Scale: 1.0");
                //we have to reset the zoomQueue when we do this
                initZoomQueue();
                mainInstance.requestFocus();
            }
        });
        //let us generate random elliptic curves
        random = new JButton("Random elliptic curve");
        resetScale.setMargin(new Insets(0,0,0,0));
        random.setPreferredSize(new Dimension(180, 20));
        random.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                int a = 1+ (int)(Math.random() * (10));
                int b = 1+ (int)(Math.random() * (10));
                double[] coefArray = new double[10];
                coefArray[0] = b;
                coefArray[1] = a;
                coefArray[3] = 1;
                coefArray[9] = 2;
                Curve generated = new EllipticCurve(coefArray, world, InputHandler.colorList.pop());
                InputHandler last = inputs.get(inputs.size() - 1);
                last.setCurve(generated);
                addInputHandler(new InputHandler("", 15));
                mainInstance.requestFocus();
            }
        });
        //to zoom in
        JButton plus = new JButton("+");
        plus.setPreferredSize(new Dimension(30, 20));
        plus.setFont(new Font("Arial", Font.PLAIN, 30));
        plus.setMargin(new Insets(0,0,0,0));
        plus.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                world.zoomIn();
                mainInstance.requestFocus();
            }
        });
        //to zoom out
        JButton minus = new JButton("-");
        minus.setFont(new Font("Arial", Font.PLAIN, 40));
        minus.setPreferredSize(new Dimension(30, 20));
        minus.setMargin(new Insets(0,0,0,0));
        minus.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                world.zoomOut();
                mainInstance.requestFocus();
            }
        });
        //add all the things to the panels
        inputPanel.add(random);
        settingsPanel.add(switchMode);
        settingsPanel.add(centerOrigin);
        settingsPanel.add(resetScale);
        settingsPanel.add(zoomLabel);
        settingsPanel.add(plus);
        settingsPanel.add(minus);
        //set the locations of the panels
        inputPanel.setLocation(10, 10);
        settingsPanel.setLocation(10, 215);
        settingsPanel.setSize(200, 80);
        //add the panels to the frame
        frame.add(inputPanel);
        frame.add(settingsPanel);
        frame.setVisible(true);
        //initiate the queues
        InputHandler.initColorQueue();
        initZoomQueue();
        //run it
        mainInstance.run();
    }

    public void paintComponent(Graphics g) {
        //paints the stuff
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        world.draw(g);
    }

    public void mouseClicked(MouseEvent e){
    }

    public void mousePressed(MouseEvent e){
        //helps us pan
        lastPressedX = e.getXOnScreen();
        lastPressedY = e.getYOnScreen();
        pan = true;
        this.requestFocusInWindow();
    }

    public void mouseReleased(MouseEvent e){
        //stops panning
        pan = false;
    }

    //all of these empty methods have to be here for the interfaces we are implementing
    public void mouseEntered(MouseEvent e){

    }

    public void mouseExited(MouseEvent e){

    }

    public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();
    }

    public void keyReleased(KeyEvent e) {
        char c = e.getKeyChar();

    }

    public void keyTyped(KeyEvent e) {
        //lets us zoom by using a and s
        char c = e.getKeyChar();
        if(c == 'a'){
            world.zoomOut();
        }
        if (c == 's'){
            world.zoomIn();
        }
        if(c == KeyEvent.VK_ENTER){
            this.requestFocus();
        }
    }

    //does... something... from previous projects
    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    //lets us addInputHandlers to the panel
    public static void addInputHandler(InputHandler i){
        inputs.add(i);
        inputPanel.remove(random);
        inputPanel.add(i.textField);
        inputPanel.add(i.button);
        //if it is ellipitic there is more stuff to add
        if(i.isElliptic){
            inputPanel.add(i.primeSwap);
            inputPanel.add(i.prime);
        }
        inputPanel.add(random);
        inputPanel.revalidate();
        inputPanel.repaint();
    }

    //lets us remove InputHandlers
    public static void removeInputHandler(InputHandler i){
        inputPanel.remove(i.textField);
        inputPanel.remove(i.button);
        if(i.isElliptic){
            inputPanel.remove(i.prime);
            inputPanel.remove(i.primeSwap);
        }
        Component[] components = inputPanel.getComponents();
        if(components.length == 1){
            //if there are no input handlers let, replace it
            inputPanel.remove(0);
            addInputHandler(new InputHandler("Type equation here!", 15));
            inputPanel.add(random);
        }
        //update the panel and arraylist
        inputPanel.revalidate();
        inputPanel.repaint();
        inputs.remove(i);
    }

    //the zoom queue contains the multiples we will zoom by
    //by using the queue going both directions we ensure that we get nice numbers
    //for the grid labels
    public static void initZoomQueue(){
        zoomQueue = new GenericStack<Double>();
        zoomQueue.add(2.0);
        zoomQueue.add(2.5);
        zoomQueue.add(2.0);

    }

}
