import javax.swing.JLabel;

public class GridLabel extends JLabel
{
	public int x;
	public int y;
	public boolean press; 
	
	public GridLabel(int x, int y)
	{
		this.x=x;
		this.y=y;
		press=true;
	}
}
