import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.EventQueue;

import javax.sound.sampled.Port;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.BoxLayout;

import java.awt.FlowLayout;
import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JButton;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class StartMenu extends JFrame
{
	private JPanel contentPane;
	private JTextField YourNameField;
	private JTextField EnterIPField;
	private JTextField PortField_1;
	private JTextField MapsField;
	private JButton RefreshButton;
	private JCheckBox MapCheckBox;
	private JButton ConnectButton;
	private JLabel PortLabel;
	private JCheckBox CustomPortCheckBox;
	private JLabel EnterIPLabel;
	private JCheckBox HostGameCheckBox;
	private JLabel YourNameLabel;
	private JLabel YourIPLabel;
	String ip="Error";
	
	
	JPanel connectingPanel;
	int secondsLeft=30;
	JLabel secondsLabel=new JLabel("Waiting for another player... " +secondsLeft+ "s until timout.");
	
	JPanel cards;
	CardLayout cardLayout;
	
	ServerSocket ss;//used if we're hosting a game
	Socket s;//used if we're not hosting but connecting to a host
	HostServer server; 
	PrintWriter pr;
	BufferedReader br;
	
	String compCoordinates=new String();

	public static void main(String[] args)
	{
		new StartMenu();
	}

	public StartMenu()
	{
		setTitle("Battleship Menu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setSize(new Dimension(73, 23));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		cards=new JPanel(new CardLayout());
		setContentPane(cards);
		cardLayout= (CardLayout) cards.getLayout();
		
		//getting your IP
		try
		{
			URL toCheckIp= new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(toCheckIp.openStream()));
			ip = in.readLine();
		} 
		catch (MalformedURLException e){} 
		catch (IOException e){}
		
		
		YourIPLabel = new JLabel("Your IP: "+ip);
		YourIPLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		YourIPLabel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		
		YourNameLabel = new JLabel("Name:");
		
		YourNameField = new JTextField();
		YourNameField.setColumns(10);
		
		HostGameCheckBox = new JCheckBox("Host Game");
		HostGameCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			if(HostGameCheckBox.isSelected()) EnterIPField.setEnabled(false);
			else EnterIPField.setEnabled(true);
			}
		});
		HostGameCheckBox.setSelected(true);
		
		EnterIPLabel = new JLabel("Enter an IP:");
		
		EnterIPField = new JTextField("192.0.2.0");
		EnterIPField.setColumns(10);
		EnterIPField.setEnabled(false);

		
		
		CustomPortCheckBox = new JCheckBox("Custom Port");
		CustomPortCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!CustomPortCheckBox.isSelected()) PortField_1.setEnabled(false);
				else PortField_1.setEnabled(true);
			}
		});
		
		PortLabel = new JLabel("Port:");
		
		PortField_1 = new JTextField("6789");
		PortField_1.setColumns(10);
		PortField_1.setEnabled(false);
		
		MapCheckBox = new JCheckBox("201 Maps");
		MapCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(MapCheckBox.isSelected())
				{
					CustomPortCheckBox.setSelected(false);
					CustomPortCheckBox.setEnabled(false);
					HostGameCheckBox.setSelected(false);
					HostGameCheckBox.setEnabled(false);
				}
				else
				{
						CustomPortCheckBox.setEnabled(true);
						HostGameCheckBox.setEnabled(true);
				}
			}
		});
		
		MapsField = new JTextField();
		MapsField.setColumns(10);
		
		//refresh button refreshes IP
		RefreshButton = new JButton("Refresh");
		RefreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				try
				{
					URL toCheckIp= new URL("http://checkip.amazonaws.com");
					BufferedReader in = new BufferedReader(new InputStreamReader(toCheckIp.openStream()));
					ip = in.readLine();
				} 
				catch (MalformedURLException ex)
				{} 
				catch (IOException ex)
				{
					ip="Error";
				}
				finally
				{
					YourIPLabel.setText("Your IP: "+ip);
				}
			}
		});
		
		//thread to check if connected to internet 
		new Thread()
		{
		    public void run() {
		    	while(true){
			    	try
					{
						URL toCheckIp= new URL("http://checkip.amazonaws.com");
						BufferedReader in = new BufferedReader(new InputStreamReader(toCheckIp.openStream()));
						RefreshButton.setEnabled(true);
						
						if(!HostGameCheckBox.isSelected())EnterIPField.setEnabled(true);
						if(CustomPortCheckBox.isSelected()) PortField_1.setEnabled(true);
					} 
					catch (MalformedURLException ex)
					{} 
					catch (IOException ex)
					{
						ip="Error";
						YourIPLabel.setText("Your IP: "+ip);
						RefreshButton.setEnabled(false);
						EnterIPField.setEnabled(false);
						PortField_1.setEnabled(false);
					}
			    	try{Thread.sleep(500);} 
			    	catch (InterruptedException e){}
		    	}
		    }
		}.start();
			

		//CONNECT BUTTON LISTENER
		ConnectButton = new JButton("Connect");
		ConnectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					URL toCheckIp= new URL("http://checkip.amazonaws.com");
					BufferedReader in = new BufferedReader(new InputStreamReader(toCheckIp.openStream()));
					
					//if you're going to be a host then you must allot 30 seconds to other person to join
					if(HostGameCheckBox.isSelected() && !MapCheckBox.isSelected())
					{
						cardLayout.last(cards);
						server = new HostServer();
						server.start();
						new Thread()
						{
							public void run()
							{
								try
								{
									Thread.sleep(1000);
								} 
								catch (InterruptedException e){}
								while(true)
								{
									secondsLeft--;
									secondsLabel.setText("Waiting for another player... " +secondsLeft+ "s until timout.");
									try
									{
										Thread.sleep(1000);
									} 
									catch (InterruptedException e){}
									secondsLeft--;
									if(secondsLeft==0)
									{
										cardLayout.first(cards);
										secondsLeft=30;
										secondsLabel.setText("Waiting for another player... " +secondsLeft+ "s until timout.");
										try
										{
											ss.close();
										} 
										catch (IOException e){System.out.println("problem with closing the serverSocket");}
										server.interrupt();
										break;
									}
								}
							}
						}.start();
					}
					
					//----if you're NOT THE HOST!!-- then connect to the host
					else if (!HostGameCheckBox.isSelected() && !MapCheckBox.isSelected())
					{
						new Thread()
						{
							public void run()
							{
								System.out.println("trying to connect to server");
								try
								{
									s=new Socket(EnterIPField.getText(), Integer.parseInt(PortField_1.getText()));
									System.out.println("client connected to host!");
								} 
								catch (NumberFormatException e)
								{
									JOptionPane.showMessageDialog(null,
										    "Connection to the host failed!",
										    "Connection Error",
										    JOptionPane.ERROR_MESSAGE);
								} 
								catch (UnknownHostException e)
								{
									JOptionPane.showMessageDialog(null,
										    "Connection to the host failed!",
										    "Connection Error",
										    JOptionPane.ERROR_MESSAGE);
								} 
								catch (IOException e)
								{
									JOptionPane.showMessageDialog(null,
										    "Connection to the host failed!",
										    "Connection Error",
										    JOptionPane.ERROR_MESSAGE);
								}
							}
						}.start();
					}
					//if the maps check is selected
					else if(MapCheckBox.isSelected())
					{
						System.out.println("map is selected");
						 try {
					            URL url = new URL("http://www-scf.usc.edu/~csci201/assignments/"+MapsField.getText()+".battle");
					            InputStream is = url.openStream();
					            BufferedReader br = new BufferedReader(new InputStreamReader(is));
					            
					            
					            char compCoor[][]=new char[10][10];
					            int l=0;
					            String line;
					            while ( (line = br.readLine()) != null)
					            {
					                compCoor[l]=line.toCharArray();
					                l++;
					            }
					            for(int i =0;i<10; i++){
					                for (int j = 0; j < 10; j++) {//Iterate rows
					                    System.out.print(compCoor[i][j]);//Print colmns
					                }   
					                System.out.println("");
					            }
					            
					            char compCoor1[][]=new char[10][10];
					            for(int i =0;i<10; i++){
					                for (int j = 0; j < 10; j++) {//Iterate rows
					                    compCoor1[j][i]=compCoor[i][j];
					                }   
					            }
					            br.close();
					            is.close();
					            new BattleShipComp(compCoor1);
					            dispose();
					        } catch (Exception e2) {
					            e2.printStackTrace();
					        }  
					}
				
				} 
				catch (MalformedURLException ex)
				{} 
				catch (IOException ex)
				{
					ip="Error";
					YourIPLabel.setText("Your IP: "+ip);
				}
			}
		});
		
		
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(191)
							.addComponent(YourIPLabel))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(142)
							.addComponent(YourNameLabel)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(YourNameField, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(87)
							.addComponent(HostGameCheckBox)
							.addGap(18)
							.addComponent(EnterIPLabel)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(EnterIPField, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(118)
							.addComponent(CustomPortCheckBox)
							.addGap(18)
							.addComponent(PortLabel)
							.addGap(18)
							.addComponent(PortField_1, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(117)
									.addComponent(MapCheckBox))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(115)
									.addComponent(RefreshButton, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(ConnectButton, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
								.addComponent(MapsField, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap(81, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(YourIPLabel)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(YourNameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(YourNameLabel))
					.addGap(34)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(HostGameCheckBox)
						.addComponent(EnterIPLabel)
						.addComponent(EnterIPField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(CustomPortCheckBox)
						.addComponent(PortLabel)
						.addComponent(PortField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(MapCheckBox)
						.addComponent(MapsField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(RefreshButton, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addComponent(ConnectButton))
					.addContainerGap(27, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		//=======================================================================================
		connectingPanel=new JPanel();
		connectingPanel.setLayout(new BoxLayout(connectingPanel, BoxLayout.Y_AXIS));
		connectingPanel.add(secondsLabel);
		secondsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		//=======================================================================================
		
		cards.add(contentPane, "1");
		cards.add(connectingPanel,"2");
		
		
		//=======================================================================================
		setVisible(true);
	}
	
	public class HostServer extends Thread
	{
		public void run()
		{
			try
			{
				ss=new ServerSocket(Integer.parseInt(PortField_1.getText()));
				System.out.println("waiting on someone to connect");
				s=ss.accept();
				System.out.println("did this shit connect?");
				new BattleShipServer(s,YourNameField.getText());
			} 
			catch (IOException e){System.out.println("something wrong with server socket connection!");}
		}
	}
}
