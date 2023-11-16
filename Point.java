Point extends Pair implements drawable {
private static int radius;
  public Point(double x, double y){
      super(x, y);
  }
public void draw(Graphics g){
  g.setColor(white);
  g.fillOval(radius, radius);
//For non minimal viable product
//later display point's coordinates here
}
//Not neccessary for minimum viable product
//public void centerAround()

//
}//Point
//
