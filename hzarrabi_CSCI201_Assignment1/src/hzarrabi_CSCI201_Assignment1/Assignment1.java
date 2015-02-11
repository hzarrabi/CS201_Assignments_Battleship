package hzarrabi_CSCI201_Assignment1;

import java.util.Arrays;
import java.util.Scanner;


public class Assignment1 
{	
	//multiply
	public static double dotProduct(AnObject vec1, AnObject vec2)
	{
		double x = vec1.getX()*vec2.getX();
		double y = vec1.getY()*vec2.getY();
		double z = vec1.getZ()*vec2.getZ();
		
		return (x + y + z);
	}
	
	
	//input error checker
	public static int errorCheck(int lower,int higher)
	{
		Scanner scan = new Scanner(System.in);
		
		int choice=12;
		
		boolean error;
		do
		{
			try
			{
				error=true;
				choice=scan.nextInt();
				
				if(choice<lower || choice>higher)
				{
					System.out.print("You didn't enter an integer between:");
					error=false;
				}
			}
			catch(Exception e)
			{
				System.out.print("You didn't enter an integer:");
				error=false;
			}
			String buffer=scan.nextLine();//this will clear the buffer
		}
		while(!error);
		return choice;
	}
	
	
	
	
	//menu
	public static int menu(AnObject first, AnObject second)
	{
		Scanner scan = new Scanner(System.in);
		
		System.out.println("\n\n");
		
		if(first==null)
		{
			System.out.println("Object 1 has not been assigned.");
		}
		if(second==null)
		{
			System.out.println("Object 2 has not been assigned.");
		}
		System.out.println("1. Change value of "+first);
		System.out.println("2. Change value of "+second);
		System.out.println("3. Add the objects.");
		System.out.println("4. Subtract the objects.");
		System.out.println("5. Find the angle between the objects.");
		System.out.println("6. Quit\n");
		
		System.out.print("Enter your choice 1-6:");
		
		return errorCheck(1,6);
	}
	
	//this will check whether a string is a double or not
	static boolean isDouble(String string) {
        try 
        {
            Double.parseDouble(string);
            return true;
        } 
        catch (NumberFormatException e) 
        {
            return false;
        }
    }
	
	//this will be the method that returns to us a correct array of doubles once the user gives a correct input
	public static double [] chooseCoordinates()
	{
		Boolean accept=false;
		double[] theCoordinates={0.0,0.0,0.0};
		while(!accept)
		{
			try
			{
				System.out.println("Enter values in the following format: <x,y,z>");
				Scanner scan = new Scanner(System.in);
				String coordinates=scan.nextLine();
				char firstLetter=coordinates.charAt(0);
				char secondLetter=coordinates.charAt(coordinates.length()-1);

				if(firstLetter!='<' || secondLetter!='>')
				{
					throw new IllegalArgumentException();
				}
				
				coordinates=coordinates.substring(1,coordinates.length()-1);
				
				String[] doubleString=coordinates.split(",");
				
				//checks how many coordinates they put in there
				if(doubleString.length>3 || doubleString.length<1)
				{
					throw new IllegalArgumentException();
				}
				
				//checks to make sure everything they entered was a double
				for(int i=0;i<doubleString.length;i++)
				{
					if(!isDouble(doubleString[i]))
					{
						throw new IllegalArgumentException();
					}
				}
				
				//making the string is a double
				theCoordinates=new double[doubleString.length];
				for(int i=0;i<doubleString.length;i++)
				{
					theCoordinates[i]=Double.parseDouble(doubleString[i]);
				}
			accept=true;//allows you to break out of for loop
			}
			catch(IllegalArgumentException ex) 
			{ 
				accept=false;
			}	 
		}
		return theCoordinates;
	}
	
	
	
	
	
//---------------------------------------------------------------------------------------------------------------
	public static void main(String[] args) 
	{
		
		AnObject object1=null;//not instantiating them yet
		AnObject object2=null;
		
		boolean loop=true;
		
		
		while(loop)
		{
			int choice=menu(object1,object2);
			
			if(choice==1)
			{
				double[] theCoordinates={0,0,0};
				double[] theCoordinates1;
				theCoordinates1=chooseCoordinates();
				
				
				for(int i=0;i<theCoordinates1.length;i++)
				{
					theCoordinates[i]=theCoordinates1[i];
				}
				
					
				boolean again=false;
				while(!again)
				{
					System.out.println("Is this is a Vector or a Point?");
					String theKind;
					Scanner kind=new Scanner(System.in);
					theKind=kind.next();
					
					if(theKind.equalsIgnoreCase("point"))
					{
						object1=new Point(theCoordinates[0], theCoordinates[1], theCoordinates[2]);
						again=true;
					}
					else if(theKind.equalsIgnoreCase("vector"))
					{
						object1=new TheVector(theCoordinates[0], theCoordinates[1], theCoordinates[2]);
						again=true;
					}
					else System.out.println("That's not a valid kind. Try again!");
				}
			}
			else if(choice==2)
			{
				double[] theCoordinates={0,0,0};//array for the coordinates
				double[] theCoordinates1;
				theCoordinates1=chooseCoordinates();
				
				for(int i=0;i<theCoordinates1.length;i++)
				{
					theCoordinates[i]=theCoordinates1[i];
				}
				
				boolean again=false;
				while(!again)
				{
					System.out.println("Is this is a Vector or a Point?");
					String theKind;
					Scanner kind=new Scanner(System.in);
					theKind=kind.next();
					
					if(theKind.equalsIgnoreCase("point"))
					{
						object2=new Point(theCoordinates[0], theCoordinates[1], theCoordinates[2]);
						again=true;
					}
					else if(theKind.equalsIgnoreCase("vector"))
					{
						object2=new TheVector(theCoordinates[0], theCoordinates[1], theCoordinates[2]);
						again=true;
					}
					else System.out.println("That's not a valid kind. Try again!");
				}
			}
			else if(choice==3)//add
			{
				//vector + vector
				double[] theCoordinates=new double[3];
				if(object1 instanceof TheVector && object2 instanceof TheVector )
				{
					theCoordinates[0]=object1.getX()+object2.getX();
					theCoordinates[1]=object1.getY()+object2.getY();
					theCoordinates[2]=object1.getZ()+object2.getZ();
					System.out.println("The result is a Vector:<"+theCoordinates[0]+","+theCoordinates[1]+","+theCoordinates[2]+">");

				
				}
				else if (object1 instanceof Point && object2 instanceof TheVector)
				{
					theCoordinates[0]=object1.getX()+object2.getX();
					theCoordinates[1]=object1.getY()+object2.getY();
					theCoordinates[2]=object1.getZ()+object2.getZ();
					System.out.println("The result is a Point:<"+theCoordinates[0]+","+theCoordinates[1]+","+theCoordinates[2]+">");

				}
				else 
				{
					String kind1="null";
					String kind2="null";
					if(object1 instanceof Point)
					{
						kind1="Point";
					}
					else if(object1 instanceof TheVector)
					{
						kind1="Vector";
					}
					if(object2 instanceof Point)
					{
						kind2="Point";
					}
					else if(object2 instanceof TheVector)
					{
						kind2="Vector";
					}
					
					System.out.println("Cannot add a "+kind1+" to a "+kind2+"!");
				}
			}
			else if(choice==4)//subtract
			{
				double[] theCoordinates=new double[3];
				if(object1 instanceof TheVector && object2 instanceof TheVector)
				{
					theCoordinates[0]=object1.getX()-object2.getX();
					theCoordinates[1]=object1.getY()-object2.getY();
					theCoordinates[2]=object1.getZ()-object2.getZ();
					System.out.println("The result is a Vector:<"+theCoordinates[0]+","+theCoordinates[1]+","+theCoordinates[2]+">");

				}
				else if(object1 instanceof Point && object2 instanceof Point)
				{
					theCoordinates[0]=object1.getX()-object2.getX();
					theCoordinates[1]=object1.getY()-object2.getY();
					theCoordinates[2]=object1.getZ()-object2.getZ();
					System.out.println("The result is a Vector:<"+theCoordinates[0]+","+theCoordinates[1]+","+theCoordinates[2]+">");
				}
				else if(object1 instanceof Point && object2 instanceof TheVector)
				{
					theCoordinates[0]=object1.getX()-object2.getX();
					theCoordinates[1]=object1.getY()-object2.getY();
					theCoordinates[2]=object1.getZ()-object2.getZ();
					System.out.println("The result is a Point:<"+theCoordinates[0]+","+theCoordinates[1]+","+theCoordinates[2]+">");
				}
				else
				{
					String kind1="null";
					String kind2="null";
					if(object1 instanceof Point)
					{
						kind1="Point";
					}
					else if(object1 instanceof TheVector)
					{
						kind1="Vector";
					}
					if(object2 instanceof Point)
					{
						kind2="Point";
					}
					else if(object2 instanceof TheVector)
					{
						kind2="Vector";
					}
					
					System.out.println("Cannot subtract a "+kind1+" by a "+kind2+"!");
				}
			}
			else if(choice==5)//angle
			{
				if(object1 instanceof TheVector && object2 instanceof TheVector)
				{
					if((object1.getX()==0 && object1.getY()==0 && object1.getZ()==0) || (object2.getX()==0 && object2.getY()==0 && object2.getZ()==0))
					{
						System.out.println("Vector has coordinates <0.0,0.0,0.0>! Can't calculate angle.");
					}
					else
					{
						double length1=Math.sqrt(Math.pow(object1.getX(),2.0) + Math.pow(object1.getY(),2.0) + Math.pow(object1.getZ(),2.0));
						double length2=Math.sqrt(Math.pow(object2.getX(),2.0) + Math.pow(object2.getY(),2.0) + Math.pow(object2.getZ(),2.0));
						double dotProduct=dotProduct(object1, object2);
						System.out.println("The angle between them is "+Math.round(Math.toDegrees(Math.acos(dotProduct/(length1*length2))))+" degrees");
					}
				}
				else  
				{
					String kind1="null";
					String kind2="null";
					if(object1 instanceof Point)
					{
						kind1="Point";
					}
					else if(object1 instanceof TheVector)
					{
						kind1="Vector";
					}
					if(object2 instanceof Point)
					{
						kind2="Point";
					}
					else if(object2 instanceof TheVector)
					{
						kind2="Vector";
					}
					
					System.out.println("Cannot find the angle of a "+kind1+" and a "+kind2+"!");
				};
			}
			else if(choice==6)
			{
				System.out.println("quiting!");
				loop=false;
			}
		}
		

		

		
	}
	
}
