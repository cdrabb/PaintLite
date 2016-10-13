import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.*;
public class Paint {

	ArrayList<Point> matrix = new ArrayList<Point>();
	Point start = null;
	Point end = null;
	@SuppressWarnings("serial")
	public Paint()
	{
		JFrame frame = new JFrame("Paint LITE");
		JMenuBar jmb = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem save = new JMenuItem("Save");
		JMenuItem load = new JMenuItem("Load");
		JMenuItem clear = new JMenuItem("Clear");
		JPanel drawArea = new JPanel(){
			public void paint(Graphics g)
			{
				super.paint(g);
				
				//Draws the current line that has not been added to the matrix
				if(start != null)
				{
					g.setColor(Color.BLACK);
					g.drawLine(start.x,start.y,
							end.x, end.y);
				}
				//Draws the matrix if there are an even number of points
				if(matrix.size()%2 == 0)
					for(int i = 0; i < matrix.size(); i+=2)
					{
						g.drawLine(matrix.get(i).x,matrix.get(i).y,
								matrix.get(i+1).x, matrix.get(i+1).y);
					}	
			}
		};
		
		frame.setSize(800,800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		jmb.add(file);
		file.add(save);
		file.add(load);
		file.add(clear);
		
		clear.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				matrix.clear();
				start = null;
				end = null;
				drawArea.repaint();
			}
		});
		
		save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Specify a file to save");
				int selection = fileChooser.showSaveDialog(frame);
				
				if(selection == JFileChooser.APPROVE_OPTION)
				{
					try
					{
						FileWriter fw = new FileWriter(fileChooser.getSelectedFile()+".xml");
						for(Point p:matrix)
							fw.write(p.x + "," + p.y + "~\n");
						fw.close();
					} catch (Exception err){
						err.getMessage();
					}
					
				}
			}
		});
		
		load.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Specify a file to open");
				int selection = fileChooser.showOpenDialog(frame);
				
				if(selection == JFileChooser.APPROVE_OPTION)
				{
					try
					{
						BufferedReader br = new BufferedReader(new FileReader(fileChooser.getSelectedFile()));
						String line = br.readLine();
						
						while(line != null)
						{
							line = line.replace("~", "");
							String [] coords = line.split(",");
							Point p = new Point(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
							matrix.add(p);
							line = br.readLine();
						}
						drawArea.repaint();
						br.close();
					} catch (Exception err){
						err.getMessage();
					}
				}
			}
		});
		
		frame.setJMenuBar(jmb);
		drawArea.setBackground(new java.awt.Color(255,255,255));
		drawArea.setSize(900,900);
		frame.add(drawArea);
		
		drawArea.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e)
			{
				start = e.getPoint();
			}
			public void mouseReleased(MouseEvent e)
			{
				matrix.add(start);
				matrix.add(e.getPoint());
			}
		});
		
		drawArea.addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseDragged(MouseEvent e)
			{
				end = e.getPoint();
				drawArea.repaint();
			}
		});
		
		frame.setVisible(true);
	}
	public static void main(String [] args)
	{
		SwingUtilities.invokeLater(new Runnable(){
			public void run()
			{
				new Paint();
			}
		});

	}
}
