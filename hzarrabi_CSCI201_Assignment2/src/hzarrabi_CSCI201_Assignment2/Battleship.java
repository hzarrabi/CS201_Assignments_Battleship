package hzarrabi_CSCI201_Assignment2;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Battleship 
{
	private String fileName;

	public Score highScores[]=new Score[10];//array for the high scores
	public char theGrid[][]=new char[10][10];
	
	private void moveDown(int index)
	{
		Score temp1=highScores[index];
		Score temp2;
		while(index<9 && temp1!=null)//i do temp1!=null because once there are no more score no need to move down
		{
			temp2=highScores[index+1];//this is the original value
			highScores[index+1]=temp1;
			temp1=temp2;
			index++;
		}
	}
	
	public void add(Score newGame)
	{
		for(int i=0;i<10;i++)
		{
			if(highScores[i]!=null)
			{
				if(newGame.getScore()<highScores[i].getScore())//less than
				{
					moveDown(i);
					highScores[i]=newGame;
					break;
				}
				else if(newGame.getScore()==highScores[i].getScore() && i!=9)//equals
				{
					moveDown(i+1);
					highScores[i+1]=newGame;
					break;
				}
			}
			else//if you hit a null index of the array you can put the score there
			{
				highScores[i]=newGame;
				break;
			}
		}
	}
	
	public void readFile(String fileName)
	{
		this.fileName=fileName;
		try 
		{
			FileReader fr = new FileReader(this.fileName);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			if(!line.contains("Highscores"))//make sure the first line is "highscores"
			{
				ErrorDialog notFound= new ErrorDialog(new JFrame(),"Your file was formatted incorrectly!");
			}
			//==================Reading in high scores======================
			for (int i=1;i<11;i++)
			{
				line = br.readLine();
				int i1 = line.indexOf(' ');
				if(line==null || line.isEmpty())
				{
					ErrorDialog notFound= new ErrorDialog(new JFrame(),"Your file had a blank space!");
				}
				else
				{
					//this will allow to splitting with multiple spaces and removes anything that is not a letter or number
					String[] theWords=line.replaceAll("[^0-9a-zA-Z]"," ").split("\\s+");
					if(theWords.length==1)
					{
					}
					else if(theWords.length>3)
					{
						ErrorDialog notFound= new ErrorDialog(new JFrame(),"More than 3 elements were parsed!");
					}
					else
					{
						String name=theWords[1];//name of the high scorer
						int score=Integer.parseInt(theWords[2]);
						Score newScore=new Score(score,name);
						this.add(newScore);//adding the new score
					}
					
				}
		    }
			//============================reading the grid==============================================
			int a=0,b=0,c=0,d=0;//counter for making sure right number of each ship
			for(int q=0;q<10;q++)
			{	
				line = br.readLine();
				char[] charArray = line.toCharArray();//
				if(charArray.length!=10)
				{
					ErrorDialog notFound= new ErrorDialog(new JFrame(),"Incorrect # of coordinates on line "+ (q+1) + " !");
				}
				else
				{
					//copying the char array into the correct array
					for(int j=0;j<10;j++)
					{
						if(charArray[j]!='X'&&charArray[j]!='A'&&charArray[j]!='B'&&charArray[j]!='C'&&charArray[j]!='D')
						{
							ErrorDialog notFound= new ErrorDialog(new JFrame(),"You have a \""+charArray[j]+"\"");//correct characters
						}
						else
						{
							if(charArray[j]=='A') a++;
							if(charArray[j]=='B') b++;
							if(charArray[j]=='C') c++;
							if(charArray[j]=='D') d++;
							theGrid[j][q]=charArray[j];
						}
					}
				}
				
			}
			if(a!=5||b!=4||c!=3||d!=4)
			{
				ErrorDialog notFound= new ErrorDialog(new JFrame(),"Incorrect number of ships!");//correct characters
			}
			
			//=======================================checking to make sure ships in a row===================
			int a1=0,b1=0,c1=0,d1=0;
			for(int i=0;i<10;i++)
			{
				//columns
				String test=new String(theGrid[i]);
				if(test.contains("AAAAA")) a1++;
				if(test.contains("BBB")) b1++;
				if(test.contains("CCC")) c1++;
				if(test.contains("DD")) 
				{
					d1++;
					if(test.contains("DDDD")) d1++;//in case the D ships are one after each other
				}
				
				//rows
				char[] tempArray = new char[10];
				for(int j=0;j<10;j++)
				{
					tempArray[j]=theGrid[j][i];
				}
				String test1=new String(tempArray);
				if(test1.contains("AAAAA")) a1++;
				if(test1.contains("BBB")) b1++;
				if(test1.contains("CCC")) c1++;
				if(test1.contains("DD")) 
				{
					d1++;
					if(test1.contains("DDDD")) d1++;//in case the D ships are one after each other
				}
			}
			if(a1!=1||b1!=1||c1!=1||d1!=2)
			{
				ErrorDialog notFound= new ErrorDialog(new JFrame(),"Incorrect number of ships!");//incorrect number of ships
			}
			
			
			
			 br.close();
			 fr.close();
		} 
		catch (FileNotFoundException fnfe) 
		{
			ErrorDialog notFound= new ErrorDialog(new JFrame(),"Your file was not found!");
			notFound.setVisible(true);
		} 
		catch (IOException ioe) 
		{
			System.out.println("Some random exceptionn that idk what it is");
		}
 }
	
public void writeFile()
{
	try
	{
		PrintWriter writer = new PrintWriter(fileName);
		writer.println("Highscores:");
		for(int i=0;i<10;i++)
		{
			if(highScores[i]!=null)
			{
				writer.println((i+1)+". "+highScores[i].getName()+" - "+highScores[i].getScore());
			}
			else writer.println((i+1)+".");
			writer.flush();
		}
		for(int i=0;i<10;i++)
		{
			char[] tempArray = new char[10];
			for(int j=0;j<10;j++)
			{
				tempArray[j]=theGrid[j][i];
			}
			String test1=new String(tempArray);
			writer.println(test1);
			writer.flush();
		}
		writer.close();
	} 
	catch (FileNotFoundException e)
	{
		
	} 
}
	
	
	public static void main(String[] args)
	{
		Battleship battleshipObject = new Battleship();
		
		if (args.length>0) //if there was text entered
		{
			battleshipObject.readFile(args[0]);
		} 
		else 
		{
			System.out.println("You didn't enter a file name intitially!");
			System.out.print("Enter your file name: ");
			Scanner scan = new Scanner(System.in);
			String file=scan.nextLine();
			while (file.length()<4)
			{
				System.out.println("You didnt enter a filename!");
				System.out.print("Enter your file name:");
				file=scan.nextLine();
			}
			battleshipObject.readFile(file);

		}
		new GameWindow(battleshipObject);
	}
	
}
