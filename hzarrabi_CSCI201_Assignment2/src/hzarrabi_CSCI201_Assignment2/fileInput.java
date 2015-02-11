package hzarrabi_CSCI201_Assignment2;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class fileInput extends JFrame
{
	private JLabel label;
	private JTextField tf;
	private JButton start;
	
	public fileInput()
	{
		setLayout(new FlowLayout());
		label=new JLabel("You didn't enter your file name:");
		add(label);
		tf= new JTextField("File Name    ");
		add(tf);
		start=new JButton("Start");
		add(start);

		
		setSize(300,100);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	}
	
	
	
}
