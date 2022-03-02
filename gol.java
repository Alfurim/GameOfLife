import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;

import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.TimerTask;
import java.util.Timer;

public class gol extends JFrame
{
	private static final long serialVersionUID = 1L;
	final int col = 29, row = 19; //Cell by cell size of the table.
	int gen = 0; //Iteration counter.
	boolean play; //Decider to make running the program easier.
	boolean[][] currentMove = new boolean[row][col], nextMove = new boolean[row][col]; //Two separate boolean arrays to hold cell information which will be applied to the rules.
	private static JTable table;
	private JTextField textField;
	
	//Main method responsible for firing up the frame.
	public static void main(String[] args)
	{
		gol a = new gol();
		a.setVisible(true);
	}
	
	//Constructor to create the frame and hold components. Also tasked to run the program.
	public gol() 
	{
		//The frame
		setTitle("The Game of Life");
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initComponents();
		//Timer and timerTask to enable the loop to execute the program on a schedule.
		Timer time = new Timer();
		TimerTask task = new TimerTask()
		{
			public void run()
			{
				if(play)
				{
					for (int i = 0; i < row; i++)
					{
						for (int j = 0; j < col; j++)
						{
							if (!(table.getValueAt(i, j) == null)) currentMove[i][j] = true;
							nextMove[i][j] = decide(i, j);
						}
					}
					for (int i = 0; i < row; i++)
					{
						for (int j = 0; j < col; j++)
						{
							currentMove[i][j] = nextMove[i][j];
						}
					}
					textField.setText("" + gen);
					gen++;
					change();
				}
			}
		};
		time.scheduleAtFixedRate(task, 0, 500);
		change();
	}

	//Decision method that checks for the surrounding cells of the current selected cell and counts its neighbours given that they are not dead.
	//Wrapping around the table is coded separately.
	protected boolean decide(int i, int j)
	{
		int neighbours = 0;
		
		if (j > 0)
		{
			if (!(table.getValueAt(i, j - 1) == null)) neighbours++;
			if (i > 0) if(!(table.getValueAt(i - 1, j - 1) == null)) neighbours++;
			if (i < row - 1) if (!(table.getValueAt(i + 1, j - 1) == null)) neighbours++;
		}
		if (j < col - 1)
		{
			if (!(table.getValueAt(i, j + 1) == null)) neighbours++;
			if (i > 0) if(!(table.getValueAt(i - 1, j + 1) == null)) neighbours++;
			if (i < row - 1) if (!(table.getValueAt(i + 1, j + 1) == null)) neighbours++;
		}
		if (i > 0) if (!(table.getValueAt(i - 1, j) == null)) neighbours++;
		if (i < row - 1) if (!(table.getValueAt(i + 1, j) == null)) neighbours++;
		if (j == 0)
		{
			if (!(table.getValueAt(i, col - 1) == null)) neighbours++;
			if (i == 0) if (!(table.getValueAt(row - 1, col - 1) == null)) neighbours++;
			if (i == row - 1) if (!(table.getValueAt(0, col - 1) == null)) neighbours++;
			if (i != 0 && !(table.getValueAt(i - 1, col - 1) == null)) neighbours++;
			if (i != row - 1 && !(table.getValueAt(i + 1, col - 1) == null)) neighbours++;
		}
		if (j == col - 1)
		{
			if (!(table.getValueAt(i, 0) == null)) neighbours++;
			if (i == 0) if (!(table.getValueAt(row - 1, 0) == null)) neighbours++;
			if (i == row - 1) if (!(table.getValueAt(0, 0) == null)) neighbours++;
			if (i != 0 && !(table.getValueAt(i - 1, 0) == null)) neighbours++;
			if (i != row - 1 && !(table.getValueAt(i + 1, 0) == null)) neighbours++;
		}
		if (i == 0)
		{
			if(!(table.getValueAt(row - 1, j) == null)) neighbours++;
			if(j != col - 1 && !(table.getValueAt(row - 1, j + 1) == null)) neighbours++;
			if(j != 0 && !(table.getValueAt(row - 1, j - 1) == null)) neighbours++;
		}
		if (i == row - 1)
		{
			if(!(table.getValueAt(0, j) == null)) neighbours++;
			if(j != col - 1 && !(table.getValueAt(0, j + 1) == null)) neighbours++;
			if(j != 0 && !(table.getValueAt(0, j - 1) == null)) neighbours++;
		}
		//Rules of The Game of Life.
		if(neighbours == 3) return true;
		if(currentMove[i][j] && neighbours == 2) return true;
		if(neighbours < 2 || neighbours > 3) return false;
		return false;
	}

	//Depending on the return value of the array that is assigned to the current cell, this method decides whether the cell should be alive or dead.
	private void change()
	{
		for (int i = 0; i < row; i++)
		{
			for (int j = 0; j < col; j++)
			{
				if (currentMove[i][j])
				{
					table.setValueAt("!!!!", i, j);
				}
				else
				{
					table.setValueAt(null, i, j);
				}
			}
		}	
	}

	//Components of the frame.
	public void initComponents()
	{
		table = new JTable(19, 29);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(false);
		table.setBackground(UIManager.getColor("text"));
		table.setBorder(new LineBorder(UIManager.getColor("inactiveCaptionText")));
		table.addMouseMotionListener(new MouseMotionAdapter() 
		{
			@Override
			public void mouseDragged(MouseEvent e) 
			{
				table.setValueAt("!!!!", table.getSelectedRow(), table.getSelectedColumn());
				if ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) 
				{
					table.setValueAt(null, table.getSelectedRow(), table.getSelectedColumn());
				}
			}
		});
		table.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				if ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) 
				{
					table.setValueAt("!!!!", table.getSelectedRow(), table.getSelectedColumn());
				}
			}
		});
		
		JButton btnNewButton = new JButton("Start");
		btnNewButton.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				if (btnNewButton.getText() == "Start")
				{
					play = true;
					btnNewButton.setText("Pause");
				}
				else
				{
					play = false;
					btnNewButton.setText("Start");
				}
			}
		});

		JButton btnNewButton_1 = new JButton("Reset");
		btnNewButton_1.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				play = false;
				dispose();
				gol a = new gol();
				a.setVisible(true);
			}
		});
		
		JLabel lblGeneration = new JLabel("Generation:");
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setColumns(10);
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(17)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
							.addGap(84)
							.addComponent(lblGeneration, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE)
							.addGap(96)
							.addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
						.addComponent(table, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE))
					.addGap(17))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(11)
					.addComponent(table, GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE, false)
						.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblGeneration, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(20))
		);
		getContentPane().setLayout(groupLayout);
		getContentPane().add(table);
		validate();
		pack();
	}
}
