/* Front end: 한 칸의 문자만 출력한다. */
public class Wall extends GraphicsObject 
{
    public Wall(int row, int col) 
    { 
        super(col, row); 
    }
  
    @Override
    public void draw() 
    { 
        System.out.print('#'); 
    }
  
    @Override
    public double getArea()  // 미로 요소는 논리적으로 한 칸을 차지한다. 게임 규칙에는 사용하지 않는다.
    { 
       return 1.0; 
    }
}
