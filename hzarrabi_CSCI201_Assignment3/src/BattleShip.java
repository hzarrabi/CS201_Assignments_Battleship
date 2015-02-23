import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileNameExtensionFilter;


public class BattleShip extends JFrame
{
	private JLabel leftGrid[][]=new JLabel[11][11];
	private GridLabel rightGrid[][]=new GridLabel[11][11];
	private char compGrid[][]=new char[10][10];
	private char useGrid[][]=new char[10][10];
	
	JPanel left;
	JPanel right;

	JLabel log=new JLabel("Log:");
	JButton selectFileButton=new JButton("Select File...");
	JLabel fileName=new JLabel("File:");
	JButton startButton = new JButton("START");
	
	//for the ship placement
	int carriers=0;
	int battlships=0;
	int cruisers=0;
	int destroyers=0;
	
	public BattleShip()
	{
		setTitle("BattleShip");
		setLayout(new BorderLayout());
		setSize(640,490);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel north=new JPanel(new FlowLayout(FlowLayout.CENTER));
		north.add(new JLabel("PLAYER                                                                    COMPUTER"));
		add(north,BorderLayout.NORTH);
		
		JPanel center=new JPanel(new FlowLayout());//center holds the left and right grids
		left=new JPanel(new GridLayout(11, 11, 15, 20));
		left.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setLeftGrid();
		right=new JPanel(new GridLayout(11, 11, 15, 20));
		right.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setRightGrid();
		gridLabelListener();
		center.add(left);
		center.add(right);
		add(center,BorderLayout.CENTER);
		
		JPanel south=new JPanel(new BorderLayout());//holds buttons to select file and start
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



		new shipPlacerWindow("g3");
		
		
		setVisible(true);
	}
	
	//makes the ?s for the left grid
	private void setLeftGrid()
	{
		for (int i=0;i<11;i++)
		{
			for(int j=0;j<11;j++)
			{
				leftGrid[i][j]=new JLabel();
				leftGrid[i][j].setText("?");//initialize all question marks initially
			}
		}
		leftGrid[0][0].setText("A");
		leftGrid[1][0].setText("B");
		leftGrid[2][0].setText("C");
		leftGrid[3][0].setText("D");
		leftGrid[4][0].setText("E");
		leftGrid[5][0].setText("F");
		leftGrid[6][0].setText("G");
		leftGrid[7][0].setText("H");
		leftGrid[8][0].setText("I");
		leftGrid[9][0].setText("J");
		leftGrid[10][0].setText(" ");
		leftGrid[10][1].setText("1");
		leftGrid[10][2].setText("2");
		leftGrid[10][3].setText("3");
		leftGrid[10][4].setText("4");
		leftGrid[10][5].setText("5");
		leftGrid[10][6].setText("6");
		leftGrid[10][7].setText("7");
		leftGrid[10][8].setText("8");
		leftGrid[10][9].setText("9");
		leftGrid[10][10].setText("10");
		
		//adding the labels
		for (int i=0;i<11;i++)
		{
			for(int j=0;j<11;j++)
			{
				left.add(leftGrid[i][j]);
			}
		}
	}
	//makes the ?s for the right grid
	private void setRightGrid()
	{
		for (int i=0;i<11;i++)
		{
			for(int j=0;j<11;j++)
			{
				rightGrid[i][j]=new GridLabel(i+1,j);
				rightGrid[i][j].setText("?");//initialize all question marks initially
			}
		}
		rightGrid[0][0].setText("A");
		rightGrid[0][0].press=false;
		rightGrid[1][0].setText("B");
		rightGrid[1][0].press=false;
		rightGrid[2][0].setText("C");
		rightGrid[2][0].press=false;
		rightGrid[3][0].setText("D");
		rightGrid[3][0].press=false;
		rightGrid[4][0].setText("E");
		rightGrid[4][0].press=false;
		rightGrid[5][0].setText("F");
		rightGrid[5][0].press=false;
		rightGrid[6][0].setText("G");
		rightGrid[6][0].press=false;
		rightGrid[7][0].setText("H");
		rightGrid[7][0].press=false;
		rightGrid[8][0].setText("I");
		rightGrid[8][0].press=false;
		rightGrid[9][0].setText("J");
		rightGrid[9][0].press=false;
		rightGrid[10][0].setText(" ");
		rightGrid[10][0].press=false;

		
		rightGrid[10][1].setText("1");
		rightGrid[10][1].press=false;
		rightGrid[10][2].setText("2");
		rightGrid[10][2].press=false;
		rightGrid[10][3].setText("3");
		rightGrid[10][3].press=false;
		rightGrid[10][4].setText("4");
		rightGrid[10][4].press=false;
		rightGrid[10][5].setText("5");
		rightGrid[10][5].press=false;
		rightGrid[10][6].setText("6");
		rightGrid[10][6].press=false;
		rightGrid[10][7].setText("7");
		rightGrid[10][7].press=false;
		rightGrid[10][8].setText("8");
		rightGrid[10][8].press=false;
		rightGrid[10][9].setText("9");
		rightGrid[10][9].press=false;
		rightGrid[10][10].setText("10");
		rightGrid[10][10].press=false;
		
		//adding the labels
		for (int i=0;i<11;i++)
		{
			for(int j=0;j<11;j++)
			{
				right.add(rightGrid[i][j]);
			}
		}
	}
	//action listener for the gridlabels
	private void gridLabelListener()
	{

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
							System.out.println("The coordinates are"+ rightGrid[i1][j1].x+rightGrid[i1][j1].y);
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
						System.out.println("i did one thing lol");
						
						//getting the fileName without extenstion to change JLabel
						String fileName=fc.getSelectedFile().getName();
						int pos = fileName.lastIndexOf(".");
						if (pos > 0) {
						    fileName = fileName.substring(0, pos);
						}
						BattleShip.this.fileName.setText("File:" + fileName);
						
						//reading the file
						try
						{
							FileReader fr = new FileReader(fc.getSelectedFile());//make a file object for reading
							BufferedReader br = new BufferedReader(fr); //make a buffer to go line by line
							
							//reading in from the buffer
							for(int i=0;i<10;i++)
							{
								String buffer = br.readLine();//reading in line
								char[] charArray = buffer.toCharArray();//making it into char array
								for(int j=0;j<10;j++)
								{
									compGrid[i][j]=charArray[j];
								}
							}
						} 
						catch (FileNotFoundException e1)
						{
							//we know the file is there so don't worry
						} 
						catch (IOException ioe) 
						{}
				        System.out.println("done");
						
					}
				}
				
			}
		});
		
	}
	
	public class shipPlacerWindow extends JFrame
	{
		
		int x;//this will hold the x coordinate of what we're editing
		int y;//this will hold y
		
		private JComboBox<String> shipList = new JComboBox<String>();
		
		private JRadioButton North= new JRadioButton("North");
		private JRadioButton South= new JRadioButton("South");
		private JRadioButton East= new JRadioButton("East");
		private JRadioButton West= new JRadioButton("West");
		
		JButton placeShip=new JButton("Place Ship");
		
		public shipPlacerWindow(String coordinates)
		{
			setTitle("Select ship at "+ coordinates);
			setSize(300,200);
			setLocationRelativeTo(null);
			setLayout(new BorderLayout());
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
			//making the panel with the combo box
			JPanel top = new JPanel();
			top.add(new JLabel("Select Ship:"));
			shipList.addItem("Aircraft Carrier");
			shipList.addItem("Battleship");
			shipList.addItem("Cruiser");
			shipList.addItem("Destroyer");
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
			
			setVisible(true);
		}
		
		//this function is decides whether the button is valid or not
		private void IsValid()
		{
			int range=0;//this is the range of the ship selected
			
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
					if(range>y) throw new Exception();//checking to make sure we're not out of range
					else
					{
						int yTest=y;
						for(int i=0;i<range;i++)
						{
							if(compGrid[x][yTest]!='X') throw new Exception();//throws an exception if there is a ship already in position
							yTest--;	
						}
					}
				}
				else if(South.isSelected())
				{
					if(range+y>10) throw new Exception();
					else
					{
						int yTest=y;
						for(int i=0;i<range;i++)
						{
							if(compGrid[x][yTest]!='X') throw new Exception();//throws an exception if there is a ship already in position
							yTest++;	
						}
					}
				}
				else if(East.isSelected())
				{
					if(range>x) throw new Exception();
					else
					{
						int xTest=x;
						for(int i=0;i<range;i++)
						{
							if(compGrid[x][xTest]!='X') throw new Exception();//throws an exception if there is a ship already in position
							xTest--;	
						}
					}
				}
				else if(West.isSelected())
				{
					if(range+x>10)throw new Exception();
					else
					{
						int xTest=x;
						for(int i=0;i<range;i++)
						{
							if(compGrid[x][xTest]!='X') throw new Exception();//throws an exception if there is a ship already in position
							xTest++;	
						}
					}
				}
				else throw new Exception();
			}
			
			catch (Exception e)
			{
				placeShip.setEnabled(false);//disabling the button if something is wrong
			}
			placeShip.setEnabled(false);
		}
	}
	
	
	
	
	//==============================================================
	public static void main(String[] args)
	{
		System.out.println("hello");
		new BattleShip();
	}
}
