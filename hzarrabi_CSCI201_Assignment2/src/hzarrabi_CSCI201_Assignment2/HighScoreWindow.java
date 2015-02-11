package hzarrabi_CSCI201_Assignment2;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HighScoreWindow extends JFrame
{
	public HighScoreWindow(Battleship x, String name, int score)
	{
		this.setSize(600,400);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setTitle("Battleship");
		this.setAlwaysOnTop(true);
		
		JPanel highScores=new JPanel();
		highScores.setLayout(new BoxLayout(highScores, BoxLayout.Y_AXIS) );
		JLabel label=new JLabel();
		
		label.setText("Thanks for playing, "+ name+"!");
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		highScores.add(label);
		
		label=new JLabel();
		label.setText("Highscores:");
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		highScores.add(label);
		
		
		for(int i=0;i<10;i++)
		{
			label=new JLabel();
			if(x.highScores[i]!=null)
			{
				label.setText((i+1)+" : "+x.highScores[i].getName()+" - "+x.highScores[i].getScore());
			}
			else label.setText((i+1)+" :                       ");
			label.setAlignmentX(Component.CENTER_ALIGNMENT);
			highScores.add(label);
		}		
		add(highScores,BorderLayout.NORTH);
		
		JPanel highScores1=new JPanel();
		highScores1.setLayout(new BoxLayout(highScores1, BoxLayout.Y_AXIS) );
		label=new JLabel("Your score: "+score);
		label.setAlignmentX(Container.CENTER_ALIGNMENT);
		highScores1.add(label);
		add(highScores1,BorderLayout.SOUTH);
		
		this.setVisible(true);
	}
	
	
}
