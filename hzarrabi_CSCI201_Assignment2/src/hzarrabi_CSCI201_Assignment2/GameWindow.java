package hzarrabi_CSCI201_Assignment2;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameWindow extends JFrame
{
	private char board[][]=new char[10][10];
	private JLabel coordinates[][]=new JLabel[11][11];
	
	//return number of letter to make sure not more than j
	private static int whatNumber(char c)
	{
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String alphabet1="abcdefghijklmnopqrstuvwxyz";
		if(alphabet.indexOf( c )==-1)
		{
			return alphabet1.indexOf(c);
		}
		return alphabet.indexOf( c );
	}
	
	private static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str); 
	    if(d<1 || d>10) throw new NumberFormatException();
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
	public GameWindow(Battleship x)
	{
		//copying that 2d array of board
		for(int i=0; i<10; i++)
			  for(int j=0; j<10; j++)
			    board[i][j]=x.theGrid[i][j];
		
		this.setSize(600,400);
		this.setLocationRelativeTo(null);
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Battleship");

		int a=0;
		int b=0;
		int c=0;
		int d=0;
		int turns=0;
		
		for (int i=0;i<11;i++)
		{
			for(int j=0;j<11;j++)
			{
				coordinates[i][j]=new JLabel();
				coordinates[i][j].setText("?");//initialize all question marks initially
			}
		}
		
		coordinates[0][0].setText("A");
		coordinates[1][0].setText("B");
		coordinates[2][0].setText("C");
		coordinates[3][0].setText("D");
		coordinates[4][0].setText("E");
		coordinates[5][0].setText("F");
		coordinates[6][0].setText("G");
		coordinates[7][0].setText("H");
		coordinates[8][0].setText("I");
		coordinates[9][0].setText("J");
		coordinates[10][0].setText(" ");
		
		coordinates[10][1].setText("1");
		coordinates[10][2].setText("2");
		coordinates[10][3].setText("3");
		coordinates[10][4].setText("4");
		coordinates[10][5].setText("5");
		coordinates[10][6].setText("6");
		coordinates[10][7].setText("7");
		coordinates[10][8].setText("8");
		coordinates[10][9].setText("9");
		coordinates[10][10].setText("10");
		
		//gameboard JPanel
		JPanel gameBoard=new JPanel();
		gameBoard.setLayout(new GridLayout(11, 11, 2, 2));
		for (int i=0;i<11;i++)
		{
			for(int j=0;j<11;j++)
			{
				gameBoard.add(coordinates[i][j]);
			}
		}
		
		//highscoresJPanel
		JPanel highScores=new JPanel();
		highScores.setLayout(new BoxLayout(highScores, BoxLayout.Y_AXIS) );
		highScores.add(new JLabel("Highscores:"));
		for(int i=0;i<10;i++)
		{
			if(x.highScores[i]!=null)
			{
				highScores.add(new JLabel((i+1)+" : "+x.highScores[i].getName()+" - "+x.highScores[i].getScore()));
			}
			else highScores.add(new JLabel((i+1)+" : "));
		}		
			
		add(gameBoard,BorderLayout.CENTER);
		add(highScores,BorderLayout.EAST);
		
		
		this.setVisible(true);
		
		
		while(a+b+c+d!=16)
		{
			turns++;
			System.out.print("Turn "+ turns+ " Please enter a Coordinate: ");
			Scanner scan = new Scanner(System.in);
			String coordinates=scan.nextLine();
			boolean isNumber=true;

			//making sure input is correct
			while(coordinates.length()<2||coordinates.length()>3|| !Character.isAlphabetic(coordinates.charAt(0))|| whatNumber(coordinates.charAt(0))>9 || !isNumeric(coordinates.substring(1)) || Integer.parseInt(coordinates.substring(1))<1|| Integer.parseInt(coordinates.substring(1))>10)                             
			{
				System.out.println("That's an invalid Coordinate, try again!");
				System.out.print("Turn "+ turns+ " Please enter a Coordinate: ");
				coordinates=scan.nextLine();
			}
			int yCoordinate=whatNumber(coordinates.charAt(0));//this will return the number of what you're trying to attack
			int xCoordinate=Integer.parseInt(coordinates.substring(1))-1;
			
			if(board[xCoordinate][yCoordinate]=='A')
			{
				a++;
				board[xCoordinate][yCoordinate]='O';
				this.coordinates[yCoordinate][xCoordinate+1].setText("A");
				if(a==5)
				{
					System.out.println("You have sunken an Aircraft Carrier!");
				}
				else System.out.println("You hit an Aircraft Carrier!");
			}
			else if(board[xCoordinate][yCoordinate]=='B')
			{
				b++;
				board[xCoordinate][yCoordinate]='O';
				this.coordinates[yCoordinate][xCoordinate+1].setText("B");
				if(b==4)
				{
					System.out.println("You have sunken a Battleship!");
				}	
				else System.out.println("You hit a Battleship!");
			}
			else if(board[xCoordinate][yCoordinate]=='C')
			{
				c++;
				board[xCoordinate][yCoordinate]='O';
				this.coordinates[yCoordinate][xCoordinate+1].setText("C");
				if(c==3)
				{
					System.out.println("You have sunken a Cruiser!");
				}
				else System.out.println("You hit a Cruiser!");

				
			}
			else if(board[xCoordinate][yCoordinate]=='D')
			{
				d++;
				board[xCoordinate][yCoordinate]='Q';
				this.coordinates[yCoordinate][xCoordinate+1].setText("D");
				if(d==2)
				{
					if(xCoordinate-1>-1 && board[xCoordinate-1][yCoordinate]=='Q')
					{
						System.out.println("You have sunken a Destroyer!");
					}
					else if(xCoordinate+1<10 && board[xCoordinate+1][yCoordinate]=='Q')
					{
						System.out.println("You have sunken a Destroyer!");
					}
					else if(yCoordinate-1>-1 && board[xCoordinate][yCoordinate-1]=='Q')
					{
						System.out.println("You have sunken a Destroyer!");
					}
					else if(yCoordinate+1<10 && board[xCoordinate][yCoordinate+1]=='Q')
					{
						System.out.println("You have sunken a Destroyer!");
					}
					else System.out.println("You hit a Destroyer!");
				}
				else if(d==3)
				{
					if(xCoordinate-1>-1 && board[xCoordinate-1][yCoordinate]=='Q')
					{
						System.out.println("You have sunken a Destroyer!");
					}
					else if(xCoordinate+1<10 && board[xCoordinate+1][yCoordinate]=='Q')
					{
						System.out.println("You have sunken a Destroyer!");
					}
					else if(yCoordinate-1>-1 && board[xCoordinate-1][yCoordinate-1]=='Q')
					{
						System.out.println("You have sunken a Destroyer!");
					}
					else if(yCoordinate+1<10 && board[xCoordinate+1][yCoordinate+1]=='Q')
					{
						System.out.println("You have sunken a Destroyer!");
					}
					else System.out.println("You hit a Destroyer!");
				}
				else if(d==4)
				{
					System.out.println("You have sunken a Destroyer!");
				}
				else System.out.println("You hit a Destroyer!");
			}
			else if(board[xCoordinate][yCoordinate]=='O' || board[xCoordinate][yCoordinate]=='Q')//O means they've already guessed this
			{
				System.out.println("You've already guessed this! Guess again!");
				turns--;
			}
			else
			{
				this.coordinates[yCoordinate][xCoordinate+1].setText("MISS!");
				System.out.println("You missed!");
			}
		}
		System.out.println("You sank all ships!");
		System.out.print("Please enter your name:");
		Scanner scan = new Scanner(System.in);
		String name=scan.nextLine();
		while(name.length()<1)
		{
			System.out.print("You didn't enter a name! Please enter your name:");
			name=scan.nextLine();
		}
		Score newPlayer= new Score(turns,name);
		x.add(newPlayer);
		x.writeFile();//writing to the file
		boolean open=false;
		for(int i=0;i<10;i++)
		{
			if(x.highScores[i].getName()==name)
			{
				open=true;
				break;
			}
		}
		if(open==true)
		{
			new HighScoreWindow(x,name,turns);
			this.dispose();
		}
		else
		{
			this.dispose();
			System.exit(0);
		}
	}
}

