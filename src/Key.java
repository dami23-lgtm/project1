/* Front end: 한 칸의 문자만 출력한다. */
public class Key extends GraphicsObject 
{
    public Key(int row, int col) 
    { 
      super(col, row); 
    }
    
    @Override
    public void draw() 
    { 
      System.out.print('K'); 
    }
    
    @Override
    public double getArea() 
    { 
      return 1.0; 
    }
}
