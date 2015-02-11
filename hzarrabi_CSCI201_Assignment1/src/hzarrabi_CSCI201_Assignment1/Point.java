package hzarrabi_CSCI201_Assignment1;

public class Point extends AnObject
{
	//constructor
	public Point(double x, double y, double z)
	{
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	 public String toString()
	 {
		 return "Point:<"+x+","+y+","+z+">";
	 }
	
	
}
