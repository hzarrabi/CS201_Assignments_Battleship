import hzarrabi_CSCI201_Assignment3.CantAddShipException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.html.BlockView;
import javax.swing.Timer;



public class BattleShipServer extends JFrame
{
	private JComponent  leftGrid[][]=new JComponent[11][11];
	private JComponent rightGrid[][]=new JComponent[11][11];
	private char compGrid[][]=new char[10][10];//this is the one we click on
	private char userGrid[][]=new char[10][10];//this is the one the computer guesses 
	
	JPanel left;
	JPanel right;

	String playersAim="N/A";
	String computersAim="N/A";
	JButton selectFileButton=new JButton("Select File...");
	JLabel fileName=new JLabel("File:                                     ");
	JButton startButton = new JButton("START");
	
	//for the ship placement
	int carriers=0;
	int battlships=0;
	int cruisers=0;
	int destroyers=0;
	
	//ships hit
	int playerCarriers=0;
	int playerBattlships=0;
	int playerCruisers=0;
	int playerDestroyers=0;
	
	int compCarriers=0;
	int compBattlships=0;
	int compCruisers=0;
	int compDestroyers=0;
	
	//bools for different modes of game
	boolean selectedFile=false;
	boolean editMode=true;
	
	//images
	private ImageIcon wave=new ImageIcon("wave.jpg"); 
	private ImageIcon miss=new ImageIcon("x.jpg");
	private ImageIcon hit=new ImageIcon("hit.jpg");
	private ImageIcon aship=new ImageIcon("AShip.jpg");
	private ImageIcon bship=new ImageIcon("BShip.jpg");
	private ImageIcon cship=new ImageIcon("CShip.jpg");
	private ImageIcon dship=new ImageIcon("DShip.jpg");
	
	
	//int for how many hit each side had taken
	int compHits=0;//so if this equals 16 that means that the USER won
	int userHits=0;
	
	//menus
	JMenuBar menuBar = new JMenuBar();
	JMenu fileMenu=new JMenu("Info");
	JMenuItem howToMenu = new JMenuItem("How To");
	JMenuItem aboutMenu=new JMenuItem("About");
	
	//timer and log
	JLabel timeLabel= new JLabel("0:15");
	int seconds=15;
	Timer time;
	boolean playerShot=false;//the boolean that indicates if the player shot
	boolean compShot=false;
	int computerSeconds=12;//this will be the random time assigned to the computer's turn 
	JTextArea log =new JTextArea();
	JScrollPane scroll = new JScrollPane (log, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	JPanel south=new JPanel(new BorderLayout());//holds buttons to  file and start
	int round=1;
	
	//animations
	BufferedImage wave1;
	BufferedImage wave2;
	
	BufferedImage expl1;
	BufferedImage expl2;
	BufferedImage expl3;
	BufferedImage expl4;
	BufferedImage expl5;
	BufferedImage[] expl = new BufferedImage[5];
		//--
	BufferedImage splash1;
	BufferedImage splash2;
	BufferedImage splash3;
	BufferedImage splash4;
	BufferedImage splash5;
	BufferedImage splash6;
	BufferedImage splash7;
	BufferedImage[] splash = new BufferedImage[7];
		//--
	BufferedImage imageA;
	BufferedImage imageB;
	BufferedImage imageC;
	BufferedImage imageD;
	BufferedImage imageM;
	BufferedImage imageQ;
	BufferedImage imageX;
	
	SoundLibrary sl = new SoundLibrary();
	
	//-------------------------------------------
	Socket s;
	String myName;
	BufferedReader br;
	PrintWriter pw;
	int opponentShips;
	
	public BattleShipServer(Socket s, String name)
	{
		this.s=s;
		myName=name;
		opponentShips=0;
		try
		{
			br=new BufferedReader(new InputStreamReader(this.s.getInputStream()));
			pw = new PrintWriter(this.s.getOutputStream(), true);
		} 
		catch (IOException e){System.out.println("something wrong with instantiating br");}
		
		new Thread()
		{
			public void run()
			{
				while(true)
				{
					System.out.println("Press enter to continue...");
					Scanner keyboard = new Scanner(System.in);
					keyboard.nextLine();
					pw.println("hello:sdfds");
				}
			}
		}.start();
		
		new Thread()
		{
			public void run()
			{
				while(true)
				{
					try
					{
						String hello=br.readLine();
						opponentCommand(hello);
					} 
					catch (IOException e){System.out.println("problem with br reading in");}
					
				}
			}
		}.start();
		
		
		load();
		
		fillUserGrid();//this will instantiate userArray with X's
		
		setTitle("BattleShip");
		setLayout(new BorderLayout());
		setSize(690,460);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel north=new JPanel(new FlowLayout(FlowLayout.CENTER));
		north.setAlignmentX(100);
		north.add(new JLabel(myName+"                                                  "));
		north.add(timeLabel);
		north.add(new JLabel("                                             COMPUTER"));
		add(north,BorderLayout.NORTH);
		
		JPanel center=new JPanel(new FlowLayout());//center holds the left and right grids
		left=new JPanel(new GridLayout(11, 11));
		left.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setLeftGrid();
		right=new JPanel(new GridLayout(11, 11));
		right.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setRightGrid();
		gridLabelListener();
		center.add(left);
		center.add(right);
		add(center,BorderLayout.CENTER);
		
		//JPanel south=new JPanel(new BorderLayout());//holds buttons to  file and start
		JPanel southLeft=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel southRight=new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		southRight.add(selectFileButton);
		southRight.add(fileName);
		southRight.add(startButton);
		startButton.setEnabled(false);
		
		south.add(southLeft,BorderLayout.WEST);
		south.add(southRight,BorderLayout.EAST);
		add(south,BorderLayout.SOUTH);
		selectFileListener();
		startButtonListener();
		
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(fileMenu);
		fileMenu.setMnemonic('I');
		fileMenu.add(howToMenu);
		howToMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, Event.CTRL_MASK));
		howToMenu.setMnemonic('h');
		fileMenu.add(aboutMenu);
		aboutMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK));
		aboutMenu.setMnemonic('a');
		menuListeners();
		
		
		left.setBackground(Color.cyan);
		right.setBackground(Color.cyan);
		setResizable(false);
		setVisible(true);
	}	
	
	//this takes in commands from opponent through socket and interprets them(protocol)
	public void opponentCommand(String command)
	{
		String[] theCommand = command.split(":");
		if(theCommand[0].equals("hello")) System.out.println("hello");
		//receiving opponent's name
		else if(theCommand[0].equals("name"));
		//receiving opponents coordinates of where to place ships
		else if(theCommand[0].equals("placeCoor"))
		{
			System.out.println(theCommand[1]);
			System.out.println("The length of the string is: "+theCommand[1].length());
			char tempArray[][]= new char[10][10];
			//trying to turn string back into char array
			for(int i=0;i<10;i++)
			{
				String upToNCharacters =theCommand[1].substring(i*10,(i*10)+10);
				System.out.println("the row is:"+ upToNCharacters);
				tempArray[i]=upToNCharacters.toCharArray();
			}
			System.out.println("pringing char array");
			for(int i =0;i<10; i++){
                for (int j = 0; j < 10; j++) {//Iterate rows
                    System.out.print(tempArray[i][j]);//Print colmns
                }   
                System.out.println("");
			}
			
			//copying the opponents grid to compGrid
			compGrid=tempArray.clone();
			opponentShips++;//this counts how many ships the opponent has placed down
			//if both players have placed all their ships
			if(opponentShips==5 && carriers+battlships+cruisers+destroyers==5)
			{
				startButton.setEnabled(true);
			}
		}
		//opponent removed coordinates
		else if(theCommand[0].equals("removeCoor"))
		{
			System.out.println(theCommand[1]);
			System.out.println("The length of the string is: "+theCommand[1].length());
			char tempArray[][]= new char[10][10];
			//trying to turn string back into char array
			for(int i=0;i<10;i++)
			{
				String upToNCharacters =theCommand[1].substring(i*10,(i*10)+10);
				System.out.println("the row is:"+ upToNCharacters);
				tempArray[i]=upToNCharacters.toCharArray();
			}
			System.out.println("pringing char array");
			for(int i =0;i<10; i++){
                for (int j = 0; j < 10; j++) {//Iterate rows
                    System.out.print(tempArray[i][j]);//Print colmns
                }   
                System.out.println("");
			}
			
			//copying the opponents grid to compGrid
			compGrid=tempArray.clone();
			opponentShips--;//this counts how many ships the opponent has placed down
			//disabling start button in case it was enabled 
			startButton.setEnabled(false);
		}
		//receiving opponents coordinates of where they attacked
		else if(theCommand[0].equals("attackCoor"))
		{
			int i=Integer.parseInt(theCommand[1]);	
			int j=Integer.parseInt(theCommand[2]);
			opponentAttack(i, j);
		}
		//reseting the time because we both guessed TODO we may not have to do this because other person guesses we send attack coordinates (and reset)
		else if(theCommand[0].equals("resetTime"));
		//resetting game because you won
		else if(theCommand[0].equals("reset"));
		else if(theCommand[0].equals("start")) startFunction();
	}
	
	//this function takes in the opponent's attack coordinates and changes our left grid
	public void opponentAttack(int i1, int j1)
	{
		if(editMode==true || playerShot==true)
		{
			System.out.println("the i is: "+i1);
			System.out.println("the j is: "+j1);
			
			//do nothing to the right grid in edit mode or not the players turn!!
		}
		else//when we're in playing mode then we want to play!!!!
		{
			if(compGrid[((GridLabel)leftGrid[i1][j1]).x-1][((GridLabel)leftGrid[i1][j1]).y-1]!='X' && compGrid[((GridLabel)leftGrid[i1][j1]).x-1][((GridLabel)leftGrid[i1][j1]).y-1]!='O')//you hit a ship!!
			{
				String label=Character.toString(compGrid[((GridLabel)leftGrid[i1][j1]).x-1][((GridLabel)leftGrid[i1][j1]).y-1]);
				((GridLabel)leftGrid[i1][j1]).explode('X', true);
				char theChar=compGrid[((GridLabel)leftGrid[i1][j1]).x-1][((GridLabel)leftGrid[i1][j1]).y-1];
				compGrid[((GridLabel)leftGrid[i1][j1]).x-1][((GridLabel)leftGrid[i1][j1]).y-1]='O';
				compHits++;
				
				Boolean append=true;
				
				String theShip="";
				if(theChar=='A')
				{
					playerCarriers++;
					theShip="AirCraft";
					if(playerCarriers==5)
					{
						log.append("Player sank an AircraftCarrier!\n");
						playerCarriers++;
						append=false;
					}
				}
				else if(theChar=='B')
				{
					playerBattlships++;
					theShip="BattleShip";
					if(playerBattlships==4)
					{
						log.append("Player sank a BattleShip!\n");
						playerBattlships++;
						append=false;
					}
				}
				else if(theChar=='C')
				{
					playerCruisers++;
					theShip="Carrier";
					if(playerCruisers==3)
					{
						log.append("Player sank a Cruiser!\n");
						playerCruisers++;
						append=false;
					}
				}
				else if(theChar=='D')
				{
					playerDestroyers++;
					theShip="Destroyer";
					if(playerDestroyers==2)
					{
						log.append("Player sank a Carrier!\n");
						playerDestroyers=0;
						append=false;
					}
				}
				
				String theSecond="0:";
				if(seconds<10)theSecond="0:0";
				else theSecond="0:";
				
				if(append)log.append("Player hit "+getCharForNumber2(j1+1)+i1+" and hit a "+theShip+"!("+theSecond+seconds+")\n");
				
				playersAim= getCharForNumber2(((GridLabel)leftGrid[i1][j1]).y)+((GridLabel)leftGrid[i1][j1]).x;
				playerShot=true;
				if(compHits>=16)
				{
					time.stop();
					new winnerWindow("You");
				}
				else
				{
					playerShot=true;//making it the computer's turn now, player clicks disabled
				
					if(compShot==true)//if computer has already aimed new round
					{
						seconds=15;//reseting the timer
						timeLabel.setText("0:15");
						
						round++;
						log.append("Round "+round+"\n");
						int randomNum = new Random().nextInt((10 - 0) + 1) + 0;
						//within 10 seconds(60% chance)
						if(randomNum>=4)computerSeconds=new Random().nextInt((14 - 8) + 1) + 8;//15-8 seconds
						//11-25 seconds(30% chance)
						else if(randomNum<4 && randomNum>=1)computerSeconds=new Random().nextInt((7 - 0) + 1) + 0;
						//>25 seconds (20% chance)
						else computerSeconds=-1;//since comp will run out of time under this case we make it 26 
						//compShooter();//if we haven't won then the computer shoots
					
						compShot=false;
						playerShot=false;
					}
				}
			}
			else if(compGrid[((GridLabel)leftGrid[i1][j1]).x-1][((GridLabel)leftGrid[i1][j1]).y-1]=='X')//you did miss
			{
				((GridLabel)leftGrid[i1][j1]).explode('M', true);
				compGrid[((GridLabel)leftGrid[i1][j1]).x-1][((GridLabel)leftGrid[i1][j1]).y-1]='O';
					playersAim= getCharForNumber2(((GridLabel)leftGrid[i1][j1]).y)+((GridLabel)leftGrid[i1][j1]).x;
					
					String theTime="0:";
					if(seconds<10) theTime="0:0";
					log.append("You missed!("+theTime+seconds+")\n");
					
					playerShot=true;//player shot so make true
					
					if(compShot==true)//if computer has already aimed new round
					{
						round++;
						log.append("Round "+round+"\n");
						seconds=15;//reseting the timer
						timeLabel.setText("0:15");
						
						int randomNum = new Random().nextInt((10 - 0) + 1) + 0;
						//within 10 seconds(60% chance)
						if(randomNum>=4)computerSeconds=new Random().nextInt((14 - 8) + 1) + 8;//15-8 seconds
						//11-25 seconds(30% chance)
						else if(randomNum<4 && randomNum>=1)computerSeconds=new Random().nextInt((7 - 0) + 1) + 0;
						//>25 seconds (20% chance)
						else computerSeconds=-1;//since comp will run out of time under this case we make it 26 
						//compShooter();//if we haven't won then the computer shoots
						
						compShot=false;
						playerShot=false;
					}
			}
			else log.append("You've already aimed here! Aim again!\n");
		}
	}
	
	
	//loads images and sounds etc
	private void load()
	{
		try
		{
			wave1=ImageIO.read(new File("4Resources/animatedWater/water1.png"));
			wave2=ImageIO.read(new File("4Resources/animatedWater/water2.png"));
			
			for(int i=0;i<5;i++)
			{
				expl[i]=ImageIO.read(new File("4Resources/explosion/expl"+(i+1)+".png"));
			}
			
			
			for(int i=0;i<7;i++)
			{
				splash[i]=ImageIO.read(new File("4Resources/splash/splash"+(i+1)+".png"));
			}
			
			imageA=ImageIO.read(new File("4Resources/Tiles/A.png"));
			imageB=ImageIO.read(new File("4Resources/Tiles/B.png"));
			imageC=ImageIO.read(new File("4Resources/Tiles/C.png"));
			imageD=ImageIO.read(new File("4Resources/Tiles/D.png"));
			imageM=ImageIO.read(new File("4Resources/Tiles/M.png"));
			imageQ=ImageIO.read(new File("4Resources/Tiles/Q.png"));
			imageX=ImageIO.read(new File("4Resources/Tiles/X.png"));
		
			
		}
		catch(IOException ioe)
		{	
		} 
	}
	
	
	//timer action
	private void timerAction()
	{
		timeLabel.setText("0:15");//reseting the label for when a players makes their decision
		
		//this is the actionlistener for the timer
		ActionListener timePerformer = new ActionListener() {
		      public void actionPerformed(ActionEvent evt) {
		    	  seconds--;
		    	  if(seconds<10)//if our time is 9 or less we have one digit seconds so we need to account for that
		    	  {
			    	  timeLabel.setText("0:0"+seconds);
			    	  if(seconds==3) log.append("Warning - 3 seconds left in the round!\n");
		    	  }
		    	  else timeLabel.setText("0:"+seconds);
		    	  
		    	  if(seconds==0)
		          {
		        	  seconds=15;
		        	  timeLabel.setText("0:15");
		        	  if(playerShot==false)log.append("You ran out of time!\n");//player ran out of time
		        	  if(compShot==false) log.append("Computer ran out of time\n");//computer ran out of time
		        	  compShot=false;
		        	  playerShot=false;
		        	  
	        		  round++;
	        		  log.append("Round "+ round +  "\n");
					  
	        		  int randomNum = new Random().nextInt((10 - 0) + 1) + 0;
					  //within 10 seconds(60% chance)
					  if(randomNum>=4)computerSeconds=new Random().nextInt((14 - 8) + 1) + 8;//15-8 seconds
					  //11-25 seconds(30% chance)
					  else if(randomNum<4 && randomNum>=1)computerSeconds=new Random().nextInt((7 - 0) + 1) + 0;
					  //>25 seconds (20% chance)
					  else computerSeconds=-1;//since comp will run out of time under this case we make it 26 
		        	  
		          }
		          else
		          {
		        	  //when computer "decides" to make turn
		        	  if(compShot==false && computerSeconds==seconds)
		        	  {
		        		  compShooter();
		        	  }
		        	  

		          }
		      }
		  };
		  
		//instantiating the timer to perform every 1000 milliseconds (or 1 sec)  
		time=new Timer(1000,timePerformer);
		time.start();
	}
	
	//action listeners for the menus
	private void menuListeners()
	{
		howToMenu.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				new aboutWindow();
				
			}
		});
		
		aboutMenu.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				new aboutWindow2();
				
			}
		});
	}
	
	//fills the userGrid array with x's
	private void fillUserGrid()
	{
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<10;j++)
			{
				userGrid[i][j]='X';
			}
		}
	}
	
	//makes the ?s for the left grid
	private void setLeftGrid()
	{
		for (int j=0;j<11;j++)
		{
			for(int i=0;i<11;i++)
			{
				leftGrid[i][j]=new GridLabel(i,j+1);
				if(i>0 && j<10)
				{
					//leftGrid[i][j].setIcon(wave);//initialize all question marks initially
				}
			}
		}
		((GridLabel)leftGrid[0][0]).add(new JLabel("A"));
		((GridLabel)leftGrid[0][0]).press=false;
		((GridLabel)leftGrid[0][1]).add(new JLabel("B"));
		((GridLabel)leftGrid[0][1]).press=false;
		((GridLabel)leftGrid[0][2]).add(new JLabel("C"));
		((GridLabel)leftGrid[0][2]).press=false;
		((GridLabel)leftGrid[0][3]).add(new JLabel("D"));
		((GridLabel)leftGrid[0][3]).press=false;
		((GridLabel)leftGrid[0][4]).add(new JLabel("E"));
		((GridLabel)leftGrid[0][4]).press=false;
		((GridLabel)leftGrid[0][5]).add(new JLabel("F"));
		((GridLabel)leftGrid[0][5]).press=false;
		((GridLabel)leftGrid[0][6]).add(new JLabel("G"));
		((GridLabel)leftGrid[0][6]).press=false;
		((GridLabel)leftGrid[0][7]).add(new JLabel("H"));
		((GridLabel)leftGrid[0][7]).press=false;
		((GridLabel)leftGrid[0][8]).add(new JLabel("I"));
		((GridLabel)leftGrid[0][8]).press=false;
		((GridLabel)leftGrid[0][9]).add(new JLabel("J"));
		((GridLabel)leftGrid[0][9]).press=false;
		((GridLabel)leftGrid[0][10]).add(new JLabel(" "));
		((GridLabel)leftGrid[0][10]).press=false;

		
		((GridLabel)leftGrid[1][10]).add(new JLabel("1"));
		((GridLabel)leftGrid[1][10]).press=false;
		((GridLabel)leftGrid[2][10]).add(new JLabel("2"));
		((GridLabel)leftGrid[2][10]).press=false;
		((GridLabel)leftGrid[3][10]).add(new JLabel("3"));
		((GridLabel)leftGrid[3][10]).press=false;
		((GridLabel)leftGrid[4][10]).add(new JLabel("4"));
		((GridLabel)leftGrid[4][10]).press=false;
		((GridLabel)leftGrid[5][10]).add(new JLabel("5"));
		((GridLabel)leftGrid[5][10]).press=false;
		((GridLabel)leftGrid[6][10]).add(new JLabel("6"));
		((GridLabel)leftGrid[6][10]).press=false;
		((GridLabel)leftGrid[7][10]).add(new JLabel("7"));
		((GridLabel)leftGrid[7][10]).press=false;
		((GridLabel)leftGrid[8][10]).add(new JLabel("8"));
		((GridLabel)leftGrid[8][10]).press=false;
		((GridLabel)leftGrid[9][10]).add(new JLabel("9"));
		((GridLabel)leftGrid[9][10]).press=false;
		((GridLabel)leftGrid[10][10]).add(new JLabel("10"));
		((GridLabel)leftGrid[10][10]).press=false;
		
		//adding the labels
		for (int j=0;j<11;j++)
		{
			for(int i=0;i<11;i++)
			{
				left.add(leftGrid[i][j]);
			}
		}
	}
	//makes the ?s for the right grid
	private void setRightGrid()
	{
		for (int j=0;j<11;j++)
		{
			for(int i=0;i<11;i++)
			{
				rightGrid[i][j]=new GridLabel(i,j+1);
				if(i>0 && j<10)
				{
					//rightGrid[i][j].setIcon(wave);//initialize all question marks initially
				}
			}
		}
		((GridLabel)rightGrid[0][0]).add(new JLabel("A"));
		((GridLabel)rightGrid[0][0]).press=false;
		((GridLabel)rightGrid[0][1]).add(new JLabel("B"));
		((GridLabel)rightGrid[0][1]).press=false;
		((GridLabel)rightGrid[0][2]).add(new JLabel("C"));
		((GridLabel)rightGrid[0][2]).press=false;
		((GridLabel)rightGrid[0][3]).add(new JLabel("D"));
		((GridLabel)rightGrid[0][3]).press=false;
		((GridLabel)rightGrid[0][4]).add(new JLabel("E"));
		((GridLabel)rightGrid[0][4]).press=false;
		((GridLabel)rightGrid[0][5]).add(new JLabel("F"));
		((GridLabel)rightGrid[0][5]).press=false;
		((GridLabel)rightGrid[0][6]).add(new JLabel("G"));
		((GridLabel)rightGrid[0][6]).press=false;
		((GridLabel)rightGrid[0][7]).add(new JLabel("H"));
		((GridLabel)rightGrid[0][7]).press=false;
		((GridLabel)rightGrid[0][8]).add(new JLabel("I"));
		((GridLabel)rightGrid[0][8]).press=false;
		((GridLabel)rightGrid[0][9]).add(new JLabel("J"));
		((GridLabel)rightGrid[0][9]).press=false;
		((GridLabel)rightGrid[0][10]).add(new JLabel(" "));
		((GridLabel)rightGrid[0][10]).press=false;

		
		((GridLabel)rightGrid[1][10]).add(new JLabel("1"));
		((GridLabel)rightGrid[1][10]).press=false;
		((GridLabel)rightGrid[2][10]).add(new JLabel("2"));
		((GridLabel)rightGrid[2][10]).press=false;
		((GridLabel)rightGrid[3][10]).add(new JLabel("3"));
		((GridLabel)rightGrid[3][10]).press=false;
		((GridLabel)rightGrid[4][10]).add(new JLabel("4"));
		((GridLabel)rightGrid[4][10]).press=false;
		((GridLabel)rightGrid[5][10]).add(new JLabel("5"));
		((GridLabel)rightGrid[5][10]).press=false;
		((GridLabel)rightGrid[6][10]).add(new JLabel("6"));
		((GridLabel)rightGrid[6][10]).press=false;
		((GridLabel)rightGrid[7][10]).add(new JLabel("7"));
		((GridLabel)rightGrid[7][10]).press=false;
		((GridLabel)rightGrid[8][10]).add(new JLabel("8"));
		((GridLabel)rightGrid[8][10]).press=false;
		((GridLabel)rightGrid[9][10]).add(new JLabel("9"));
		((GridLabel)rightGrid[9][10]).press=false;
		((GridLabel)rightGrid[10][10]).add(new JLabel("10"));
		((GridLabel)rightGrid[10][10]).press=false;
		
		//adding the labels
		for (int j=0;j<11;j++)
		{
			for(int i=0;i<11;i++)
			{
				right.add(rightGrid[i][j]);
			}
		}
	}
	
	private String getCharForNumber2(int i) {
	    return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
	}
	
	//action listener for the gridlabels
	private void gridLabelListener()
	{
		//listener for right grid 
		for(int i=0;i<11;i++)
		{
			for (int j=0;j <11; j++)
			{
				final int i1=i;
				final int j1=j;
				rightGrid[i][j].addMouseListener(new MouseListener()
				{
					public void mouseClicked(MouseEvent e)
					{
						if(((GridLabel)rightGrid[i1][j1]).press)
						{
							if(editMode==true || playerShot==true)
							{
								System.out.println("the i is: "+i1);
								System.out.println("the j is: "+j1);
								
								//do nothing to the right grid in edit mode or not the players turn!!
							}
							else//when we're in playing mode then we want to play!!!!
							{
								if(compGrid[((GridLabel)rightGrid[i1][j1]).x-1][((GridLabel)rightGrid[i1][j1]).y-1]!='X' && compGrid[((GridLabel)rightGrid[i1][j1]).x-1][((GridLabel)rightGrid[i1][j1]).y-1]!='O')//you hit a ship!!
								{
									String label=Character.toString(compGrid[((GridLabel)rightGrid[i1][j1]).x-1][((GridLabel)rightGrid[i1][j1]).y-1]);
									((GridLabel)rightGrid[i1][j1]).explode('X', true);
									char theChar=compGrid[((GridLabel)rightGrid[i1][j1]).x-1][((GridLabel)rightGrid[i1][j1]).y-1];
									compGrid[((GridLabel)rightGrid[i1][j1]).x-1][((GridLabel)rightGrid[i1][j1]).y-1]='O';
									compHits++;
									
									Boolean append=true;
									
									String theShip="";
									if(theChar=='A')
									{
										playerCarriers++;
										theShip="AirCraft";
										if(playerCarriers==5)
										{
											log.append("Player sank an AircraftCarrier!\n");
											playerCarriers++;
											append=false;
										}
									}
									else if(theChar=='B')
									{
										playerBattlships++;
										theShip="BattleShip";
										if(playerBattlships==4)
										{
											log.append("Player sank a BattleShip!\n");
											playerBattlships++;
											append=false;
										}
									}
									else if(theChar=='C')
									{
										playerCruisers++;
										theShip="Carrier";
										if(playerCruisers==3)
										{
											log.append("Player sank a Cruiser!\n");
											playerCruisers++;
											append=false;
										}
									}
									else if(theChar=='D')
									{
										playerDestroyers++;
										theShip="Destroyer";
										if(playerDestroyers==2)
										{
											log.append("Player sank a Carrier!\n");
											playerDestroyers=0;
											append=false;
										}
									}
									
									String theSecond="0:";
									if(seconds<10)theSecond="0:0";
									else theSecond="0:";
									
									if(append)log.append("Player hit "+getCharForNumber2(j1+1)+i1+" and hit a "+theShip+"!("+theSecond+seconds+")\n");
									
									playersAim= getCharForNumber2(((GridLabel)rightGrid[i1][j1]).y)+((GridLabel)rightGrid[i1][j1]).x;
									playerShot=true;
									if(compHits>=16)
									{
										time.stop();
										new winnerWindow("You");
									}
									else
									{
										playerShot=true;//making it the computer's turn now, player clicks disabled
									
										if(compShot==true)//if computer has already aimed new round
										{
											seconds=15;//reseting the timer
											timeLabel.setText("0:15");
											
											round++;
											log.append("Round "+round+"\n");
											int randomNum = new Random().nextInt((10 - 0) + 1) + 0;
											//within 10 seconds(60% chance)
											if(randomNum>=4)computerSeconds=new Random().nextInt((14 - 8) + 1) + 8;//15-8 seconds
											//11-25 seconds(30% chance)
											else if(randomNum<4 && randomNum>=1)computerSeconds=new Random().nextInt((7 - 0) + 1) + 0;
											//>25 seconds (20% chance)
											else computerSeconds=-1;//since comp will run out of time under this case we make it 26 
											//compShooter();//if we haven't won then the computer shoots
										
											compShot=false;
											playerShot=false;
										}
									}
								}
								else if(compGrid[((GridLabel)rightGrid[i1][j1]).x-1][((GridLabel)rightGrid[i1][j1]).y-1]=='X')//you did miss
								{
									((GridLabel)rightGrid[i1][j1]).explode('M', true);
									compGrid[((GridLabel)rightGrid[i1][j1]).x-1][((GridLabel)rightGrid[i1][j1]).y-1]='O';
										playersAim= getCharForNumber2(((GridLabel)rightGrid[i1][j1]).y)+((GridLabel)rightGrid[i1][j1]).x;
										
										String theTime="0:";
										if(seconds<10) theTime="0:0";
										log.append("You missed!("+theTime+seconds+")\n");
										
										playerShot=true;//player shot so make true
										
										if(compShot==true)//if computer has already aimed new round
										{
											round++;
											log.append("Round "+round+"\n");
											seconds=15;//reseting the timer
											timeLabel.setText("0:15");
											
											int randomNum = new Random().nextInt((10 - 0) + 1) + 0;
											//within 10 seconds(60% chance)
											if(randomNum>=4)computerSeconds=new Random().nextInt((14 - 8) + 1) + 8;//15-8 seconds
											//11-25 seconds(30% chance)
											else if(randomNum<4 && randomNum>=1)computerSeconds=new Random().nextInt((7 - 0) + 1) + 0;
											//>25 seconds (20% chance)
											else computerSeconds=-1;//since comp will run out of time under this case we make it 26 
											//compShooter();//if we haven't won then the computer shoots
											
											compShot=false;
											playerShot=false;
										}
								}
								else log.append("You've already aimed here! Aim again!\n");
								pw.println("attackCoor:"+i1+":"+j1);
							}
						}
					}
					public void mouseEntered(MouseEvent e){}
					public void mouseExited(MouseEvent e){}
					public void mousePressed(MouseEvent e){}
					public void mouseReleased(MouseEvent e){}
				});
			}
		}
		
		//listener for left grid
		for(int i=0;i<11;i++)
		{
			for (int j=0;j <11; j++)
			{
				final int i1=i;
				final int j1=j;
				leftGrid[i][j].addMouseListener(new MouseListener()
				{
					public void mouseClicked(MouseEvent e)
					{
						if(((GridLabel)leftGrid[i1][j1]).press)
						{
						    if(editMode==true)//we only want this functionality if we are in edit mode
						    {
								if(userGrid[((GridLabel)leftGrid[i1][j1]).x-1][((GridLabel)leftGrid[i1][j1]).y-1]=='X')//if the coordinate has no ship placed
								{
									if(carriers+battlships+cruisers+destroyers<5)//if we still have ships to place open the window
									new shipPlacerWindow(((GridLabel)leftGrid[i1][j1]).x,((GridLabel)leftGrid[i1][j1]).y);
								}
								else
								{
									shipDeleter(((GridLabel)leftGrid[i1][j1]).x-1, ((GridLabel)leftGrid[i1][j1]).y-1);;
								}
						    }
						    else//if we are in playing mode do this
						    {
						    	//we shouldn't be able to do anything to the left grid but i'll just do this incase
						    }
						}
					}
					public void mouseEntered(MouseEvent e){}
					public void mouseExited(MouseEvent e){}
					public void mousePressed(MouseEvent e){}
					public void mouseReleased(MouseEvent e){}
				});
			}
		}
	}
	
	
	//this is the function for the computer guessing coordinates
	private void compShooter()
	{
		Random rand=new Random();
		int x=rand.nextInt(9 - 0 + 1) + 0;
		int y=rand.nextInt(9 - 0 + 1) + 0;
		
		if(userGrid[x][y]!='X' && userGrid[x][y]!='O')//if comp hits a target
		{
			((GridLabel)leftGrid[x+1][y]).explode('X', true);;
			char theChar=userGrid[x][y];
			userGrid[x][y]='O';//marking that computer shot here
			userHits++;
			computersAim= getCharForNumber2(y+1)+(x+1);
			
			Boolean append=true;
			
			String theShip="";
			if(theChar=='A')
			{
				compCarriers++;
				theShip="AirCraft";
				if(compCarriers==5)
				{
					log.append("Computer sank an AircraftCarrier!\n");
					compCarriers++;
					append=false;
				}
			}
			else if(theChar=='B')
			{
				compBattlships++;
				theShip="BattleShip";
				if(compBattlships==4)
				{
					log.append("Computer sank a BattleShip!\n");
					compBattlships++;
					append=false;
				}
			}
			else if(theChar=='C')
			{
				compCruisers++;
				theShip="Carrier";
				if(compCarriers==3)
				{
					log.append("Computer sank a Cruiser!\n");
					compCruisers++;
					append=false;
				}
			}
			else if(theChar=='D')
			{
				compDestroyers++;
				theShip="Destroyer";
				if(compDestroyers==2)
				{
					log.append("Computer sank a Carrier!\n");
					compCarriers=0;
					append=false;
				}
			}
			
			String theSecond="0:";
			if(seconds<10)theSecond="0:0";
			else theSecond="0:";
			
			if(append)log.append("Computer hit "+getCharForNumber2(y+1)+(x+1)+" and hit a "+theShip+"!("+theSecond+seconds+")\n");
			compShot=true;
			
			if(userHits==16)//if the computer has hit all ships
			{
				time.stop();
				new winnerWindow("Computer");
			}
			else
			{
				compShot=true;//player's turn otherwise
				if(playerShot==true)//if the player has shot too
				{
					seconds=15;
					timeLabel.setText("0:15");
					
					int randomNum = new Random().nextInt((10 - 0) + 1) + 0;
					//within 10 seconds(60% chance)
					if(randomNum>=4)computerSeconds=new Random().nextInt((14 - 8) + 1) + 8;//15-8 seconds
					//11-25 seconds(30% chance)
					else if(randomNum<4 && randomNum>=1)computerSeconds=new Random().nextInt((7 - 0) + 1) + 0;
					//>25 seconds (20% chance)
					else computerSeconds=-1;//since comp will run out of time under this case we make it 26 
					//compShooter();//if we haven't won then the computer shoots
					
					compShot=false;
					playerShot=false;
					
					round++;
					log.append("Round "+ round +"\n");
				}
			}
			
		}
		else if(userGrid[x][y]=='X')//if the computer misses
		{
			userGrid[x][y]='O';
			((GridLabel)leftGrid[x+1][y]).explode('M',true);
			computersAim= getCharForNumber2(y+1)+(x+1);
			compShot=true;
			
			String theTime="0:";
			if(seconds<10) theTime="0:0";
			log.append("Computer missed!("+theTime+seconds+")\n");
			
			if(playerShot==true)//if the player has shot too
			{
				seconds=15;
				timeLabel.setText("0:15");
				
				int randomNum = new Random().nextInt((10 - 0) + 1) + 0;
				//within 10 seconds(60% chance)
				if(randomNum>=4)computerSeconds=new Random().nextInt((14 - 8) + 1) + 8;//15-8 seconds
				//11-25 seconds(30% chance)
				else if(randomNum<4 && randomNum>=1)computerSeconds=new Random().nextInt((7 - 0) + 1) + 0;
				//>25 seconds (20% chance)
				else computerSeconds=-1;//since comp will run out of time under this case we make it 26 
				//compShooter();//if we haven't won then the computer shoots
				
				compShot=false;
				playerShot=false;
				
				round++;
				log.append("Round "+ round +"\n");
			}
		}
		else//computer hit's something already hit
		{
			compShooter();
		}
	}
	//ship deleter
	private void shipDeleter(int x, int y)
	{
		int range=0;
		char shipWeWant='X';

		if(userGrid[x][y]=='A')
		{
			range=5;
			shipWeWant='A';
			carriers--;
		}
		else if(userGrid[x][y]=='B')
		{
			range=4;
			shipWeWant='B';
			battlships--;
		}
		else if(userGrid[x][y]=='C')
		{
			range=3;
			shipWeWant='C';
			cruisers--;
		}
		else if(userGrid[x][y]=='D')
		{
			range=2;
			shipWeWant='D';
			destroyers--;
		}
		else if(userGrid[x][y]=='E')
		{
			range=2;
			shipWeWant='E';
			destroyers--;
		}
		

		//checking north
		if(range<=y+1)//if there is room in the north for the full ship check for it
		{
			for(int i=0;i<range;i++)
			{
				if(userGrid[x][y-i]==shipWeWant)//if it is the ship we want
				{
					userGrid[x][y-i]='X';
					leftGrid[x+1][y-i].removeAll();
					((GridLabel)leftGrid[x+1][y-i]).add(new JLabel(new ImageIcon(imageQ)));
				}
				else break;//if it's not stop searching the north
			}
		}
		else //if there isn't room we still want to check incase we pressed the middle of a ship but don't want to go out of bound
		{
			for(int i=0;i<y+1;i++)
			{
				if(userGrid[x][y-i]==shipWeWant)//if it is the ship we want
				{
					userGrid[x][y-i]='X';
					leftGrid[x+1][y-i].removeAll();
					((GridLabel)leftGrid[x+1][y-i]).add(new JLabel(new ImageIcon(imageQ)));
				}
				else break;
			}
		}
		//checking south
		if(range+(y+1)<11)
		{
			for(int i=0;i<range;i++)
			{
				if(userGrid[x][y+i]==shipWeWant)//if it is the ship we want
				{
					userGrid[x][y+i]='X';
					leftGrid[x+1][y+i].removeAll();
					((GridLabel)leftGrid[x+1][y+i]).add(new JLabel(new ImageIcon(imageQ)));
				}
			}
		}
		else //if there isn't room we still want to check incase we pressed the middle of a ship but don't want to go out of bound
		{
			for(int i=0;i<10-y;i++)
			{
				if(userGrid[x][y+i]==shipWeWant)//if it is the ship we want
				{
					userGrid[x][y+i]='X';
					leftGrid[x+1][y+i].removeAll();
					((GridLabel)leftGrid[x+1][y+i]).add(new JLabel(new ImageIcon(imageQ)));
				}
			}
		}
		//checking west
		if(range<=x+1)
		{
			for(int i=0;i<range;i++)
			{
				if(userGrid[x-i][y]==shipWeWant)//if it is the ship we want
				{
					userGrid[x-i][y]='X';
					leftGrid[x-i+1][y].removeAll();
					((GridLabel)leftGrid[x-i+1][y]).add(new JLabel(new ImageIcon(imageQ)));
				}
				
			}
		}
		else //if there isn't room we still want to check incase we pressed the middle of a ship but don't want to go out of bound
		{
			for(int i=0;i<x+1;i++)
			{
				if(userGrid[x-i][y]==shipWeWant)//if it is the ship we want
				{
					userGrid[x-i][y]='X';
					leftGrid[x-i+1][y].removeAll();
					((GridLabel)leftGrid[x-i+1][y]).add(new JLabel(new ImageIcon(imageQ)));
				}
			}
		}
		//checking east
		if(range+x<11)
		{
			for(int i=0;i<range;i++)
			{
				if(userGrid[x+i][y]==shipWeWant)//if it is the ship we want
				{
					userGrid[x+i][y]='X';
					leftGrid[x+i+1][y].removeAll();
					((GridLabel)leftGrid[x+i+1][y]).add(new JLabel(new ImageIcon(imageQ)));
				}
			}
		}
		else //if there isn't room we still want to check incase we pressed the middle of a ship but don't want to go out of bound
		{
			for(int i=0;i<10-x;i++)
			{
				if(userGrid[x+i][y]==shipWeWant)//if it is the ship we want
				{
					userGrid[x+i][y]='X';
					leftGrid[x+i+1][y].removeAll();
					((GridLabel)leftGrid[x+i+1][y]).add(new JLabel(new ImageIcon(imageQ)));
				}
			}
		}
		
		//sending new grid with removed ship to opponent
		StringBuilder builder = new StringBuilder();
	    for(int i = 0; i < 10; i++)
	    {
	        for(int j = 0; j <10; j++)
	        {
	            builder.append(userGrid[i][j]);
	        }
	    }    
	    System.out.println(builder.toString());
		
	    for(int i =0;i<10; i++){
            for (int j = 0; j < 10; j++) {//Iterate rows
                System.out.print(userGrid[i][j]);
            }   
            System.out.println("");
        }
		pw.println("removeCoor:"+builder.toString());
				
		startButton.setEnabled(false);//if you delete a ship you can't start so disabling the button
	}
	
	private void startButtonListener()
	{
		startButton.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				startFunction();
			}
		});
	}
	
	public void startFunction()
	{
		pw.println("start");
		selectFileButton.setVisible(false);
		startButton.setVisible(false);
		fileName.setText("");//delete the text instead of setting invisible because then i only have to reset in new game
		editMode=false;
		
		timerAction();//timer starts working once we press start
		setSize(690,600);//changing the size of the frame for the log
		log.setLineWrap(true);
	    log.setWrapStyleWord(true);
	    scroll.setPreferredSize(new Dimension(690, 150));
	    south.setBorder(BorderFactory.createTitledBorder("Game Log"));
		south.add(scroll);
		south.setVisible(true);
		
		log.append("Round 1\n");
	}
	
	//action listener for select file
	private void selectFileListener()
	{
		selectFileButton.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(e.getSource()==selectFileButton)//if we click that button
				{
					JFileChooser fc= new JFileChooser();
					
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Battle Files (*.battle)", "battle"); //filter to only allow .battle files
					fc.setFileFilter(filter);//making our chooser take that filter
					fc.setAcceptAllFileFilterUsed(false);//will only allow battle files
					
					int returnVal=fc.showOpenDialog(selectFileButton);//opens up fileSelector
					
					if(returnVal==fc.APPROVE_OPTION)//if we selected a file
					{
						selectFileButton.setVisible(false);//removing the select file button
						
						//getting the fileName without extenstion to change JLabel
						String fileName=fc.getSelectedFile().getName();
						int pos = fileName.lastIndexOf(".");
						if (pos > 0) {
						    fileName = fileName.substring(0, pos);
						}
						BattleShipServer.this.fileName.setText("File:" + fileName+ "                                     ");
						
						//reading the file
						try
						{
							FileReader fr = new FileReader(fc.getSelectedFile());//make a file object for reading
							BufferedReader br = new BufferedReader(fr); //make a buffer to go line by line
							
							//reading in from the buffer
							for(int j=0;j<10;j++)
							{
								String buffer = br.readLine();//reading in line
								char[] charArray = buffer.toCharArray();//making it into char array
								for(int i=0;i<10;i++)
								{
									compGrid[i][j]=charArray[i];
								}
							}
						} 
						catch (FileNotFoundException e1)
						{
							//we know the file is there so don't worry
						} 
						catch (IOException ioe) 
						{}
				        
				        selectedFile=true;
				        if(carriers+battlships+cruisers+destroyers==5 && selectedFile)//if all ships places and file selected 
				        {
				        	startButton.setEnabled(true);
				        }
					}
				}
				
			}
		});
		
	}
	
	public class shipPlacerWindow extends JDialog
	{
		
		int x;//this will hold the x coordinate of what we're editing
		int y;//this will hold y
		
		private JComboBox<String> shipList = new JComboBox<String>();
		
		private JRadioButton North= new JRadioButton("North");
		private JRadioButton South= new JRadioButton("South");
		private JRadioButton East= new JRadioButton("East");
		private JRadioButton West= new JRadioButton("West");
		
		private JButton placeShip=new JButton("Place Ship");
		
		public shipPlacerWindow(int x,int y)
		{
			this.x=x-1;//making the coordinate into index value so u subtract by one
			this.y=y-1;
			
			setTitle("Select ship at "+ getCharForNumber(y)+x);
			setSize(300,200);
			setLocationRelativeTo(null);
			setLayout(new BorderLayout());
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
			//making the panel with the combo box
			JPanel top = new JPanel();
			top.add(new JLabel("Select Ship:"));
			if(carriers==0)shipList.addItem("Aircraft Carrier");//if we haven't placed a carrier
			if(battlships==0)shipList.addItem("Battleship");
			if(cruisers==0)shipList.addItem("Cruiser");
			if(destroyers<2)shipList.addItem("Destroyer");
			top.add(shipList);
			
			JPanel middle = new JPanel();
			middle.setLayout(new GridLayout(2,2));
			ButtonGroup directions = new ButtonGroup();
			directions.add(North);//adding buttons to group
			directions.add(South);
			directions.add(East);
			directions.add(West);
			middle.add(North);
			middle.add(South);
			middle.add(East);
			middle.add(West);
			

			add(top,BorderLayout.NORTH);
			add(middle,BorderLayout.CENTER);
			add(placeShip,BorderLayout.SOUTH);
			
			IsValid();//be careful you don't use isValid() because that's a another function that belongs to JFrame (this initially disables the button becase no radio buttons are selected
			everyThingListener();//this is the listener for everything 
			
			setModal(true);//this prevents us from accessing the board behind it
			
			setVisible(true);
		}
		
		//this function converts numbers to chars
		private String getCharForNumber(int i) {
		    return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
		}
		
		
		//this function is decides whether the button is valid or not
		private void IsValid()
		{			
			int range=0;//this is the range of the ship selected
			placeShip.setEnabled(true);
			
			try
			{
				String ship=(String) shipList.getSelectedItem();//returns what kind of ship is selected in combobox
				//setting the range based on what kind of ship selected
				if(ship.equals("Aircraft Carrier")) range=5;
				else if(ship.equals("Battleship")) range=4;
				else if(ship.equals("Cruiser")) range=3;
				else if(ship.equals("Destroyer")) range=2;
				
				if(North.isSelected())
				{
					if(range>y+1) throw new CantAddShipException();//checking to make sure we're not out of range
					else
					{
						int yTest=y;
						for(int i=0;i<range;i++)
						{
							if(userGrid[x][yTest]!='X')
							{
								throw new CantAddShipException();//throws an exception if there is a ship already in position
							}
							yTest--;	
						}
					}
				}
				else if(South.isSelected())
				{
					if(range+y>10)
					{
						throw new CantAddShipException();
					}
					else
					{
						int yTest=y;
						for(int i=0;i<range;i++)
						{
							if(userGrid[x][yTest]!='X') throw new CantAddShipException();//throws an exception if there is a ship already in position
							yTest++;	
						}
					}
				}
				else if(East.isSelected())
				{
					if(range+x>10)
					{
						throw new CantAddShipException();
					}
					else
					{
						int xTest=x;
						for(int i=0;i<range;i++)
						{
							
							if(userGrid[xTest][y]!='X')
							{
								throw new CantAddShipException();//throws an exception if there is a ship already in position
							}
							xTest++;	
						}
					}
				}
				else if(West.isSelected())
				{
					if(range>x+1)throw new CantAddShipException();//x+1 to make up for x being turned into index
					else
					{
						int xTest=x;
						for(int i=0;i<range;i++)
						{
							if(userGrid[xTest][y]!='X')
							{
								throw new CantAddShipException();//throws an exception if there is a ship already in position
							}
							xTest--;	
						}
					}
				}
				else 
				{
					throw new CantAddShipException();
				}
			}
			catch (CantAddShipException e)
			{
				placeShip.setEnabled(false);//disabling the button if something is wrong
			}
		}
	

					
		//this function adds all the listeners for to see if selection is valid and for button adding ship
		private void everyThingListener()
		{
			//the listener for the JComboBox
			shipList.addActionListener (new ActionListener () {
			    public void actionPerformed(ActionEvent e) {
			        IsValid();
			    }
			});
			North.addActionListener (new ActionListener () {
			    public void actionPerformed(ActionEvent e) {
			        IsValid();
			    }
			});
			South.addActionListener (new ActionListener () {
			    public void actionPerformed(ActionEvent e) {
			        IsValid();
			    }
			});
			West.addActionListener (new ActionListener () {
			    public void actionPerformed(ActionEvent e) {
			        IsValid();
			    }
			});
			East.addActionListener (new ActionListener () {
			    public void actionPerformed(ActionEvent e) {
			        IsValid();
			    }
			});
			
			
			//the listener for for the button that places the ships----------------------------------------
			placeShip.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					int range=0;//this is the range of the ship selected
					char shipCharacter='X';
					
					String ship=(String) shipList.getSelectedItem();//returns what kind of ship is selected in combobox
					//setting the range based on what kind of ship selected
					ImageIcon theShip=wave;//image icon that will either be a b c or d
					if(ship.equals("Aircraft Carrier"))
					{
						shipCharacter='A';
						theShip=aship;
						range=5;
						carriers++;
					}
					else if(ship.equals("Battleship"))
					{
						shipCharacter='B';
						theShip=bship;
						range=4;
						battlships++;
					}
					else if(ship.equals("Cruiser"))
					{
						shipCharacter='C';
						theShip=cship;
						range=3;
						cruisers++;
					}
					else if(ship.equals("Destroyer"))
					{
						if(destroyers==0)
						{
							shipCharacter='D';
						}
						else if(destroyers==1)
						{
							shipCharacter='E';//i make it 'E' to be able to distinguish between the two destroyer ships
						}
						theShip=dship;
						range=2;
						destroyers++;
					}
					String shipString=Character.toString(shipCharacter);
					//changing the texts in the grid and on the actual layout
					if(North.isSelected())
					{
							int yTest=y;
							for(int i=0;i<range;i++)
							{
								userGrid[x][yTest]=shipCharacter;
								((GridLabel)leftGrid[x+1][yTest]).explode(shipCharacter,false);
								yTest--;	
							}
					}
					else if(South.isSelected())
					{
							int yTest=y;
							for(int i=0;i<range;i++)
							{
								userGrid[x][yTest]=shipCharacter;
								((GridLabel)leftGrid[x+1][yTest]).explode(shipCharacter,false);
								yTest++;	
							}
					}
					else if(East.isSelected())
					{
							int xTest=x;
							for(int i=0;i<range;i++)
							{
								userGrid[xTest][y]=shipCharacter;
								((GridLabel)leftGrid[xTest+1][y]).explode(shipCharacter,false);
								xTest++;	
							}
					}
					else if(West.isSelected())
					{
							int xTest=x;
							for(int i=0;i<range;i++)
							{
								userGrid[xTest][y]=shipCharacter;
								((GridLabel)leftGrid[xTest+1][y]).explode(shipCharacter,false);
								xTest--;	
							}
					}		
				if(carriers+battlships+cruisers+destroyers==5 && selectedFile)//if they chose all ships
				{ 
					startButton.setEnabled(true);
				}
				
				//TODO send new coordinates to the other side
				StringBuilder builder = new StringBuilder();
			    for(int i = 0; i < 10; i++)
			    {
			        for(int j = 0; j <10; j++)
			        {
			            builder.append(userGrid[i][j]);
			        }
			    }    
			    System.out.println(builder.toString());
				
			    for(int i =0;i<10; i++){
	                for (int j = 0; j < 10; j++) {//Iterate rows
	                    System.out.print(userGrid[i][j]);
	                }   
	                System.out.println("");
	            }
				pw.println("placeCoor:"+builder.toString());
				
				shipPlacerWindow.this.dispose();//closes shipplacer window after you you place a ship
				}
			});
		}
	}

	public class winnerWindow extends JDialog
	{
		private JButton okButton =new JButton("OK");
		
		private ImageIcon wave1=new ImageIcon("wave.jpg"); 
		
		public winnerWindow(String winner)
		{
			setTitle("Game Over");
			setSize(300,150);
			setLocationRelativeTo(null);
			setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
			JLabel winner1=new JLabel(winner+ " won!");
			winner1.setAlignmentX( Component.LEFT_ALIGNMENT);
			add(winner1);
			add(okButton);
			okListener();
			
			addWindowListener(new java.awt.event.WindowAdapter() {
			    @Override
			    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
			        reset();
			    }
			});
			
			setModal(true);
			setVisible(true);
		}
		
		public void okListener()
		{
			okButton.addActionListener(new ActionListener()
			{
				
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					reset();
				}
			});
		}
		
		//resets the game
		private void reset()
		{
			//reset right grid
			for (int j=0;j<11;j++)
			{
				for(int i=0;i<11;i++)
				{
					if(i>0 && j<10)
					{
						rightGrid[i][j].removeAll();
						rightGrid[i][j].add(new JLabel(new ImageIcon(imageQ)));
					}
				}
			}
			
			//reset left grid 
			for (int j=0;j<11;j++)
			{
				for(int i=0;i<11;i++)
				{
					if(i>0 && j<10)
					{
						leftGrid[i][j].removeAll();
						leftGrid[i][j].add(new JLabel(new ImageIcon(imageQ)));
					}
				}
			}
			
			//reseting user and computer grid
			for (int j=0;j<10;j++)
			{
				for(int i=0;i<10;i++)
				{
					userGrid[i][j]='X';
					compGrid[i][j]='X';
				}
			}
			
			playersAim="N/A";
			computersAim="N/A";
			selectFileButton.setVisible(true);;
			fileName.setText("File:                                     ");
			startButton.setEnabled(false);
			startButton.setVisible(true);
			
			//for the ship placement
			carriers=0;
			battlships=0;
			cruisers=0;
			destroyers=0;
			
			//bools for different modes of game
			selectedFile=false;
			editMode=true;
			
			int compHits=0;//so if this equals 16 that means that the USER won
			int userHits=0;
			
			selectFileButton.setVisible(true);
			startButton.setVisible(true);
			fileName.setText("File");//delete the text instead of setting invisible because then i only have to reset in new game
			editMode=true;
			log.setText("");
			scroll.setVisible(false);
			south.setBorder(null);
			BattleShipServer.this.setSize(690,460);
			timeLabel.setText("0:15");
			seconds=15;
			playerShot=false;//the boolean that indicates if the player shot
			compShot=false;
			computerSeconds=12;//this will be the random time assigned to the computer's turn 
			round=1;
			
			
			winnerWindow.this.dispose();
		}
	}
	
	private class aboutWindow extends JDialog
	{
		JTextArea infoText=new JTextArea();
		
		public aboutWindow()
		{
			setTitle("Battleship Instructions");
			setSize(300,200);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
			
			JScrollPane sp = new JScrollPane(infoText); 
			add(sp);
			
			
			try
			{
				BufferedReader in = new BufferedReader(new FileReader(new File("howTo.txt")));
				String line = in.readLine();
				while(line != null){
				  infoText.append(line + "\n");
				  line = in.readLine();
				}
			} 
			catch (FileNotFoundException e)
			{
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			infoText.setEditable(false);
			setModal(true);
			setVisible(true);
		}
		
	}
	
	
	private class aboutWindow2 extends JDialog
	{
		
		public aboutWindow2()
		{
			setTitle("About");
			setSize(300,200);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
			
			add(new JLabel("Made by Hooman Zarrabi - 3/01/15"),BorderLayout.NORTH);
			add(new JLabel(new ImageIcon("hooman.png")),BorderLayout.CENTER);
			add(new JLabel("CSCI201USC: Assignment 3"),BorderLayout.SOUTH);
			
			
			
			
			
			setModal(true);
			setVisible(true);
		}
		
	}
	
	private class GridLabel extends JComponent implements Runnable
	{
		public int x;
		public int y;
		public boolean press; 
		
		BufferedImage currentWave;
		int current=0;

		//for the explosion thread
		Boolean explode;
		int counter=0;
		ImageIcon blastIcon;
		char c;
		
		public GridLabel(int x, int y)
		{
			this.x=x;
			this.y=y;
			press=true;
			setPreferredSize(new Dimension(30,30));
			setLayout(new FlowLayout());
			
			if(x>0 && y<11)
			{ 
				setBorder(BorderFactory.createLineBorder(Color.black));
				add(new JLabel(new ImageIcon(imageQ)));
				new Thread(this).start();			
			}
		}
		
		

		@Override
		protected void paintComponent(Graphics g) 
		{
			super.paintComponent(g);
			g.drawImage(currentWave,0,0,null);
		};
		
		@Override
		public void run()
		{
		   while (true) 
		   {
		    if(current == 0)
		    {
		    currentWave=wave1; 
		    current++;
		    }
		    else
		    {
		     currentWave=wave2;
		     current--;
		    }
		    validate();
		    repaint();
		    try { Thread.sleep(150); }
		    catch (InterruptedException e) { }
		   }
	   }
		
		public void explode(char c, Boolean b)
		{
			counter=0;
			explode=b;
			this.c=c;
			if(c=='E')this.c='D';
			
			if(this.c=='A') blastIcon=new ImageIcon(imageA);
			else if(this.c=='B') blastIcon=new ImageIcon(imageB);
			else if(this.c=='C') blastIcon=new ImageIcon(imageC);
			else if(this.c=='D') blastIcon=new ImageIcon(imageD);
			else if(this.c=='M') blastIcon=new ImageIcon(imageM);
			else if(this.c=='Q') blastIcon=new ImageIcon(imageQ);
			else if(this.c=='X') blastIcon=new ImageIcon(imageX);
			
			new Explosion().start();
			new Sound().start();
		}
		
		public class Explosion extends Thread
		{
			public void run() 
			{
				counter=0;
				while(true)
				{
					if(c!='M')//we're not idicating a miss
					{
						if(explode)//if you want explode animation to happen
						{
							if(counter<5)
							{
								removeAll();//removes previous icons or labels
								add(new JLabel(new ImageIcon(expl[counter])));
							    counter++;
							}
							else if(counter==5)
							{
								removeAll();
								add(new JLabel(blastIcon));
								counter++;
							}
							else
							{
								return;//stop thread
							}
						}
						else
						{
							removeAll();
							add(new JLabel(blastIcon));
							counter=0;
							return;//stop thread
						}
					}
					else//we're indicating a miss
					{
						if(counter<7)
						{
							removeAll();
							add(new JLabel(new ImageIcon(splash[counter])));
							counter++;
						}
						else if(counter==7)
						{
							removeAll();
							add(new JLabel(new ImageIcon(imageM))); 
							counter++;
						}
						else 
						{
							return;	
						}
					}
					try
					{sleep(300);} 
					catch (InterruptedException e)
					{}
				}
		    }
		}
		public class Sound extends Thread
		{
			String theString;
			public Sound()
			{
				if(c=='M')
				{
					theString="splash";
				}
				else if(c=='X') theString="explode";
				
			}
			public void run()
			{
				if(explode)
				{
					sl.playSound("cannon");
					sl.playSound(theString);
				}
			}
		}
	}
	
	//==============================================================
	public static void main(String[] args)
	{
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		new BattleShip();
	}
}
