import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class BattleShip extends JFrame
{
	private JLabel leftGrid[][]=new JLabel[11][11];
	private GridLabel rightGrid[][]=new GridLabel[11][11];
	
	JPanel left;
	JPanel right;

	JLabel log=new JLabel("Log:");
	JButton selectFileButton=new JButton("Select File...");
	JLabel fileName=new JLabel("File:");
	JButton startButton = new JButton("START");
	
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
		
		JPanel south=new JPanel(/*new BorderLayout()*/);//holds buttons to select file and start
		JPanel southLeft=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel southRight=new JPanel(new FlowLayout(FlowLayout.RIGHT));
		southLeft.add(log);
		southRight.add(selectFileButton);
		southRight.add(fileName);
		southRight.add(startButton);
		south.add(southLeft/*,BorderLayout.WEST*/);
		south.add(southRight/*,BorderLayout.EAST*/);
		add(south,BorderLayout.SOUTH);



		
		
		
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
	
	//==============================================================
	public static void main(String[] args)
	{
		System.out.println("hello");
		new BattleShip();
	}
}
