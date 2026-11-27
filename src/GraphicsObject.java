public abstract class GraphicsObject
{
    private double x;
    private double y;

    public GraphicsObject()
    {
        x = 0;
        y = 0;
    }

    public GraphicsObject(double a, double b)
    {
        x = a;
        y = b;
    }

    public void printStatus()
    {
        System.out.println("A GraphicsObject located at: "
                + "(" + x + ", " + y + ")");
    }

    public abstract void draw();
    public abstract double getArea();    
    
    public double getX() { return x; }
    public double getY() { return y; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
}
