class Pair {
    //pair class from prior hws
    public double x;
    public double y;

    Pair mult;
    Pair plus;

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