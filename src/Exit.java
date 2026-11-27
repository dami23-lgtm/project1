/* Front end: 한 칸의 문자만 출력한다. */
public class Exit extends GraphicsObject 
{
    public Exit(int row, int col) 
  { 
    super(col, row); 
  }
    @Override
    public void draw() 
  { 
    System.out.print('E'); 
  }
    @Override
    public double getArea() 
  { 
    return 1.0; 
  }
}
