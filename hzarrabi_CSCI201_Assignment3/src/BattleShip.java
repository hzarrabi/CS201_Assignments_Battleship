import hzarrabi_CSCI201_Assignment3.CantAddShipException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
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


public class BattleShip extends JFrame
{
	private GridLabel leftGrid[][]=new GridLabel[11][11];
	private GridLabel rightGrid[][]=new GridLabel[11][11];
	private char compGrid[][]=new char[10][10];//this is the one we click on
	private char userGrid[][]=new char[10][10];//this is the one the computer guesses 
	
	JPanel left;
	JPanel right;

	JLabel log=new JLabel("Log: You are in edit mode, click to place your ships.");
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
	
	public BattleShip()
	{
		fillUserGrid();//this will instantiate userArray with X's
		
		setTitle("BattleShip");
		setLayout(new BorderLayout());
		setSize(690,460);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel north=new JPanel(new FlowLayout(FlowLayout.CENTER));
		north.add(new JLabel("PLAYER                                                                    COMPUTER"));
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
		
		JPanel south=new JPanel(new BorderLayout());//holds buttons to  file and start
		JPanel southLeft=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel southRight=new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		southLeft.add(log);
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
					leftGrid[i][j].setIcon(wave);//initialize all question marks initially
				}
			}
		}
		leftGrid[0][0].setText("A");
		leftGrid[0][0].press=false;
		leftGrid[0][1].setText("B");
		leftGrid[0][1].press=false;
		leftGrid[0][2].setText("C");
		leftGrid[0][2].press=false;
		leftGrid[0][3].setText("D");
		leftGrid[0][3].press=false;
		leftGrid[0][4].setText("E");
		leftGrid[0][4].press=false;
		leftGrid[0][5].setText("F");
		leftGrid[0][5].press=false;
		leftGrid[0][6].setText("G");
		leftGrid[0][6].press=false;
		leftGrid[0][7].setText("H");
		leftGrid[0][7].press=false;
		leftGrid[0][8].setText("I");
		leftGrid[0][8].press=false;
		leftGrid[0][9].setText("J");
		leftGrid[0][9].press=false;
		leftGrid[0][10].setText(" ");
		leftGrid[0][10].press=false;

		
		leftGrid[1][10].setText("1");
		leftGrid[1][10].press=false;
		leftGrid[2][10].setText("2");
		leftGrid[2][10].press=false;
		leftGrid[3][10].setText("3");
		leftGrid[3][10].press=false;
		leftGrid[4][10].setText("4");
		leftGrid[4][10].press=false;
		leftGrid[5][10].setText("5");
		leftGrid[5][10].press=false;
		leftGrid[6][10].setText("6");
		leftGrid[6][10].press=false;
		leftGrid[7][10].setText("7");
		leftGrid[7][10].press=false;
		leftGrid[8][10].setText("8");
		leftGrid[8][10].press=false;
		leftGrid[9][10].setText("9");
		leftGrid[9][10].press=false;
		leftGrid[10][10].setText("10");
		leftGrid[10][10].press=false;
		
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
					rightGrid[i][j].setIcon(wave);//initialize all question marks initially
				}
			}
		}
		rightGrid[0][0].setText("A");
		rightGrid[0][0].press=false;
		rightGrid[0][1].setText("B");
		rightGrid[0][1].press=false;
		rightGrid[0][2].setText("C");
		rightGrid[0][2].press=false;
		rightGrid[0][3].setText("D");
		rightGrid[0][3].press=false;
		rightGrid[0][4].setText("E");
		rightGrid[0][4].press=false;
		rightGrid[0][5].setText("F");
		rightGrid[0][5].press=false;
		rightGrid[0][6].setText("G");
		rightGrid[0][6].press=false;
		rightGrid[0][7].setText("H");
		rightGrid[0][7].press=false;
		rightGrid[0][8].setText("I");
		rightGrid[0][8].press=false;
		rightGrid[0][9].setText("J");
		rightGrid[0][9].press=false;
		rightGrid[0][10].setText(" ");
		rightGrid[0][10].press=false;

		
		rightGrid[1][10].setText("1");
		rightGrid[1][10].press=false;
		rightGrid[2][10].setText("2");
		rightGrid[2][10].press=false;
		rightGrid[3][10].setText("3");
		rightGrid[3][10].press=false;
		rightGrid[4][10].setText("4");
		rightGrid[4][10].press=false;
		rightGrid[5][10].setText("5");
		rightGrid[5][10].press=false;
		rightGrid[6][10].setText("6");
		rightGrid[6][10].press=false;
		rightGrid[7][10].setText("7");
		rightGrid[7][10].press=false;
		rightGrid[8][10].setText("8");
		rightGrid[8][10].press=false;
		rightGrid[9][10].setText("9");
		rightGrid[9][10].press=false;
		rightGrid[10][10].setText("10");
		rightGrid[10][10].press=false;
		
		//adding the labels
		for (int j=0;j<11;j++)
		{
			for(int i=0;i<11;i++)
			{
				right.add(rightGrid[i][j]);
			}
		}
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
						if(rightGrid[i1][j1].press)
						{
							if(editMode==true)
							{
								//do nothing to the right grid in edit mode
							}
							else//when we're in playing mode then we want to play!!!!
							{
								if(compGrid[rightGrid[i1][j1].x-1][rightGrid[i1][j1].y-1]!='X' && compGrid[rightGrid[i1][j1].x-1][rightGrid[i1][j1].y-1]!='O')//it's not a ship nore have we aims for it before
								{
									String label=Character.toString(compGrid[rightGrid[i1][j1].x-1][rightGrid[i1][j1].y-1]);
									rightGrid[rightGrid[i1][j1].x][rightGrid[i1][j1].y-1].setIcon(hit);
									compGrid[rightGrid[i1][j1].x-1][rightGrid[i1][j1].y-1]='O';
									compHits++;
									if(compHits>=16)
									{
										new winnerWindow("You");
									}
									else compShooter();//if we haven't won then the computer shoots
								}
								else if(compGrid[rightGrid[i1][j1].x-1][rightGrid[i1][j1].y-1]=='X')//you did miss
								{
									rightGrid[rightGrid[i1][j1].x][rightGrid[i1][j1].y-1].setIcon(miss);
										compGrid[rightGrid[i1][j1].x-1][rightGrid[i1][j1].y-1]='O';
										compShooter();
								}
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
						if(leftGrid[i1][j1].press)
						{
						    if(editMode==true)//we only want this functionality if we are in edit mode
						    {
								if(userGrid[leftGrid[i1][j1].x-1][leftGrid[i1][j1].y-1]=='X')//if the coordinate has no ship placed
								{
									if(carriers+battlships+cruisers+destroyers<5)//if we still have ships to place open the window
									new shipPlacerWindow(leftGrid[i1][j1].x,leftGrid[i1][j1].y);
								}
								else
								{
									shipDeleter(leftGrid[i1][j1].x-1, leftGrid[i1][j1].y-1);;
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
			leftGrid[x+1][y].setIcon(hit);
			userGrid[x][y]='O';//marking that computer missed
			userHits++;
			if(userHits==16)//if the computer has hit all ships
			{
				new winnerWindow("Computer");
			}
		}
		else if(userGrid[x][y]=='X')//if the computer has aimed at that before it aims again
		{
			userGrid[x][y]='O';
			leftGrid[x+1][y].setIcon(miss);
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
					leftGrid[x+1][y-i].setIcon(wave);
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
					leftGrid[x+1][y-i].setIcon(wave);
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
					leftGrid[x+1][y+i].setIcon(wave);
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
					leftGrid[x+1][y+i].setIcon(wave);
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
					leftGrid[x-i+1][y].setIcon(wave);
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
					leftGrid[x-i+1][y].setIcon(wave);
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
					leftGrid[x+i+1][y].setIcon(wave);
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
					leftGrid[x+i+1][y].setIcon(wave);
				}
			}
		}
		
		startButton.setEnabled(false);//if you delete a ship you can't start so disabling the button
	}
	
	private void startButtonListener()
	{
		startButton.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				log.setText("Log: Player:"+playersAim+" Computer:"+computersAim);
				selectFileButton.setVisible(false);
				startButton.setVisible(false);
				fileName.setText("");//delete the text instead of setting invisible because then i only have to reset in new game
				editMode=false;
			}
		});
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
						BattleShip.this.fileName.setText("File:" + fileName+ "                                     ");
						
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
								//leftGrid[x+1][yTest].setText(shipString);
								leftGrid[x+1][yTest].setIcon(theShip);
								yTest--;	
							}
					}
					else if(South.isSelected())
					{
							int yTest=y;
							for(int i=0;i<range;i++)
							{
								userGrid[x][yTest]=shipCharacter;
								//leftGrid[x+1][yTest].setText(shipString);
								leftGrid[x+1][yTest].setIcon(theShip);
								yTest++;	
							}
					}
					else if(East.isSelected())
					{
							int xTest=x;
							for(int i=0;i<range;i++)
							{
								userGrid[xTest][y]=shipCharacter;
								//leftGrid[xTest+1][y].setText(shipString);
								leftGrid[xTest+1][y].setIcon(theShip);
								xTest++;	
							}
					}
					else if(West.isSelected())
					{
							int xTest=x;
							for(int i=0;i<range;i++)
							{
								userGrid[xTest][y]=shipCharacter;
								//leftGrid[xTest+1][y].setText(shipString);
								leftGrid[xTest+1][y].setIcon(theShip);
								xTest--;	
							}
					}		
				if(carriers+battlships+cruisers+destroyers==5 && selectedFile)//if they chose all ships
				{ 
					startButton.setEnabled(true);
				}
				
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
						rightGrid[i][j].setIcon(wave1);//initialize all question marks initially

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
						leftGrid[i][j].setIcon(wave1);//initialize all question marks initially
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
			
			log.setText("Log: You are in edit mode, click to place your ships.");
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
	
	
	//==============================================================
	public static void main(String[] args)
	{
		new BattleShip();
	}
}
