package hzarrabi_CSCI201_Assignment1;

public class TheVector extends AnObject
{
	private double length;

	public TheVector(double x, double y, double z)
	{
		this.x=x;
		this.y=y;
		this.z=z;
		
		//finds the length
		length = Math.sqrt(Math.pow(x,2.0) + Math.pow(y,2.0) + Math.pow(z,2.0));
	}
	
	
	public double length()
	{
		return length;
	}
	
	 public String toString()
	 {
		 return "Vector:<"+x+","+y+","+z+">";
	 }
		
}
