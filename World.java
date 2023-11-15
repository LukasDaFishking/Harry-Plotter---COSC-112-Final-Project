
class World{
 public ArrayList<Point> points;
  //Commented out until the class is implimented after the initial demo (11.15.23)  
  //public ArrayList<Curve> curves;
  public Pair center;
  public int scale;

  //The default scale of the image
  public final int defaultScale = 1;
  
        public World(int scale, Pair center, ArrayList<Point> points){
        this.points = points;
        this.center = center;
        this.scale = scale;
        }
  
        public World(){
        //Creating an arbitrary, default array of points to draw when none are specified
        ArrayList<Points> p = new ArrayList<Point>
        Point l = new Point (0,0)
        Point k = new Point (10,1)
        Point j = new Point (2, -3)
        p.add(l);
        p.add(k);
        p.add(j);

        //Creates a point to feed in as the default center
        Pair c = (0,0);

        this.points = p; 
        this.center = c;
        this.scale = defaultScale;
        }
  
        public void addPoint(Point p){
          if(points.contains(p)){
          }else{
          points.add(p);
          }
        }

        //There may be an error with how this works syntactically.
        //The intent is to draw every element of the array list
        public void draw(Graphics g){
          for(0 : points){
          points.draw(g)
          }
          //Commented out until curves are implimented
          // for(0 : curves){
          // curves.draw(g)
          // }
        }  
//
}//World
//
