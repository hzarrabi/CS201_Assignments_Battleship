package hzarrabi_CSCI201_Assignment2;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ErrorDialog extends JDialog //implements ActionListener
{
	private JLabel error;
	private JButton errorButton;
	
	public ErrorDialog(JFrame owner, String theError)
	{
		super(owner, true);
		setLayout(new FlowLayout());
		setTitle("Error!");
		this.setLocationRelativeTo(null);
		this.setAlwaysOnTop(true);
		error= new JLabel(theError);
		add(error);
		errorButton=new JButton("OK");
		add(errorButton);
		errorButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		
		setSize(300,100);
		
		
		
		setVisible(true);
		System.exit(0);
		
	}
	
	
	

}
