/*	Ofek Gila
	March 1st, 2014
	SnakeNCliffGUI.java
	This will make a visual representation of SnakeNCliff.java
*/

import java.awt.*;			// Imports
import java.awt.event.*;
import javax.swing.*;
import java.util.Scanner;

public class SnakeNCliffGUI	extends JApplet{			// I'm pretty sure I copied down one of your online codes for key and focus listeners for their methods
	public static void main(String[] args) {	// when I made snake.java, and I copied snake.java to have all the implements for this code, so don't
		JFrame frame = new JFrame("Snake 'N Cliff");	// ask why I extend JApplet or implement all of those things ^_^
		frame.setContentPane(new ContentPanel());
		frame.setSize(1000 + 6, 1000 + 35);		// Sets size of frame
		frame.setResizable(false);						// Makes it so you can't resize the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	public void init()	{
		setContentPane(	new ContentPanel());
	}
}
class ContentPanel extends JPanel implements ActionListener, KeyListener, FocusListener, MouseListener, MouseMotionListener	{

	public final int TILESIZE = 100;
	public int setWidth = 1000, setHeight = 1000;
	public int width, height;							// width and height of frame
	public Graphics g;									// Graphics of frame
	public byte[] directions;
	public double position;
	public Color c;
	public Color[] colorArray;
	public boolean initial = true;
	public Timer t;
	public int countSteps;
	public int countAnimation;
	public Rectangle person;
	public SnakeNCliff SNC;
	public int addBy = 1;
	public int increment;
	public int animationsPerTile;
	public Rectangle[] bullets;
	public boolean[] bulletSpotTaken;
	public int countBullets;
	public int colorOn;
	public int streak;
	
	public ContentPanel()	{
		addKeyListener(this);							// implements the implements
		addFocusListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	public void constructor()	{
		setBackground(Color.white);
		colorArray = new Color[18];
		setColorArray();
		bullets = new Rectangle[5];
		bulletSpotTaken = new boolean[bullets.length];
		for (int i = 0; i < bullets.length; i++)
			bulletSpotTaken[i] = false;
		countSteps = countBullets =  colorOn = streak = 0;
		position = 0;
		countAnimation = 1;
		person = new Rectangle(width / 2 - TILESIZE / 4, height / 4 - TILESIZE / 4, TILESIZE / 2, TILESIZE / 2);
	}
	public void setColorArray() {
		colorArray[0] = new Color(255, 0, 0);	colorArray[17] = new Color(255, 0, 0);
		colorArray[1] = new Color(0, 255, 0);
		colorArray[2] = new Color(0, 0, 255);
		colorArray[3] = new Color(142, 120, 0);
		colorArray[4] = new Color(255, 0, 255);
		colorArray[5] = new Color(0, 255, 125);
		colorArray[6] = new Color(255, 100, 80);
		colorArray[7] = new Color(170, 165, 255);
		colorArray[8] = new Color(250, 155, 175);
		colorArray[9] = new Color(255, 140, 0);
		colorArray[10] = new Color(220, 130, 10);
		colorArray[11] = new Color(85, 0, 85);
		colorArray[12] = new Color(0, 255, 255);
		colorArray[13] = new Color(142, 120, 0);
		colorArray[14] = new Color(110, 10, 160);
		colorArray[15] = new Color(0, 200, 150);
		colorArray[16] = new Color(135, 90, 10);
	}
	public int getRelativeX(double x)	{
		return (int)((x / width) * setWidth + 0.5);
	}
	public int getRelativeY(double y)	{
		return (int)((y / height) * setHeight + 0.5);
	}
	public void paintComponent(Graphics a)	{
		super.paintComponent(a);
		g = a;
		width = getWidth();
		height = getHeight();	
		//System.out.println(width + " " + height);
		if (initial)	{
			constructor();
			initial = false;
			SNC = new SnakeNCliff();
			SNC.run(true, false);
			directions = new byte[SNC.steps];
			for (int i = 0; i < SNC.steps; i++)
				directions[i] = SNC.directions[i];
			Scanner keyboard = new Scanner(System.in);
			increment = width / (directions.length);
			if (increment < 25) increment = 25;
			animationsPerTile = getAPT(directions.length);
			System.out.println("\nDone Generating");
			keyboard.nextLine();
			t = new Timer(10, this);
			t.start();
		}
		drawTiles();
	}
	public int getAPT(int dL) {
		if (dL < 20)  return 100;
		if (dL < 100) return 50;
		if (dL < 300) return 20;
		return 10;

	}
	public void drawTiles() {
		g.setColor(Color.yellow);
		g.fillOval(width / 2 - TILESIZE / 2 + TILESIZE * (int)(Math.round(position)), height / 4 - TILESIZE / 2, TILESIZE, TILESIZE);
		g.setColor(Color.black);
		for (int i = 0; i < SNC.distance; i++) {
			g.drawRect(width / 2 - TILESIZE / 2 + TILESIZE * i, height / 4 - TILESIZE / 2, TILESIZE, TILESIZE);
			g.drawRect(width / 2 - TILESIZE / 2 - TILESIZE * i, height / 4 - TILESIZE / 2, TILESIZE, TILESIZE);
		}
		g.setColor(colorArray[colorOn + 1]);
		for (int i = 0; i < bullets.length; i++)
			if (bulletSpotTaken[i])
				g.fillRect((int)bullets[i].getX(), (int)bullets[i].getY(), (int)bullets[i].getWidth(), (int)bullets[i].getHeight());
		g.setColor(colorArray[colorOn]);
		g.fillOval((int)person.getX(), (int)person.getY(), (int)person.getWidth(), (int)person.getHeight());
		g.setFont(new Font("Arial", Font.BOLD, 60));
		g.drawString(addBy + "", 50, 50);
		g.drawString((countSteps + 1) + "", width - 100, 50);
		/*g.setFont(new Font("Arial", Font.BOLD, 100));
		int cv = ((int)Math.round(position) + SNC.distance) * (100 /( SNC.distance * 2 - 1)) + 100;
		c = new Color(cv, cv, 255);
		g.setColor(c);
		g.drawString(directions[countSteps] + "", width / 2 - 40, height / 4 * 3);*/

		g.setFont(new Font("Arial", Font.ITALIC, increment));
		for (int i = 0; i < directions.length; i++) {
			if (i == countSteps) g.setColor(Color.red);
			else g.setColor(Color.blue);
			g.drawString(directions[i] + "", (2 + i * increment) % width, height / 4 + 100 + (i / 40 * increment));
		}
		g.setFont(new Font("Arial", Font.BOLD, 100));
		g.setColor(Color.red);
		g.drawString(directions[countSteps] + "", width / 2 - 40, height / 8 * 7);
		if (countBullets > 0) {
			g.setFont(new Font("Arial", Font.BOLD, 100));
			g.setColor(colorArray[colorOn]);
			g.drawString("Streak: " + streak, width / 2 - 200, height / 8 * 6);
		}
	}
	public void mouseDragged(MouseEvent evt)	{}
	public void mouseMoved(MouseEvent evt)	{	}
	public void actionPerformed(ActionEvent e)	{
		double APT = animationsPerTile;
		person = new Rectangle((int)person.getX() + (int)Math.round(100 / APT) * directions[countSteps], (int)person.getY(), (int)person.getWidth(), (int)person.getHeight());
		for (int i = 0; i < bullets.length; i++)
			if (bulletSpotTaken[i])
					bullets[i] = new Rectangle((int)bullets[i].getX(), (int)(bullets[i].getY() - Math.round(100 / APT)), (int)bullets[i].getWidth(), (int)bullets[i].getHeight());
		position += directions[countSteps] / APT;
		if (countBullets > 0)
			checkBullets();
		repaint();
		if (countAnimation % animationsPerTile == 0) {
			countSteps += addBy;
			if (countSteps >= directions.length) {
				person = new Rectangle(width / 2 - 25, (int)person.getY(), (int)person.getWidth(), (int)person.getHeight());
				position = 0;
				addBy++;
				countSteps = addBy - 1;
			}
			if (addBy > SNC.steps / SNC.distance) {
				t.stop();
				return;
			}
		}
		countAnimation++;
	}
	public void checkBullets() {
		for (int i = 0; i < bullets.length; i++)
			if (bulletSpotTaken[i]) {
				if (bullets[i].getY() + bullets[i].getHeight() < 0) {
					bulletSpotTaken[i] = false;
					streak = 0;
				}
				else for (int row = 0; row < bullets[i].getWidth(); row++)
						for (int col = 0; col < bullets[i].getHeight(); col++)
							if (row == 0 || col == 0)
								if (person.contains(row + bullets[i].getX(), col + bullets[i].getY())) {
									if (colorOn == 16) colorOn = 0;
									else colorOn++;
									bulletSpotTaken[i] = false;
									streak++;
									if (streak == 10) System.out.println("Well done!");
									return;
								}
			}
	}
	public void focusGained(FocusEvent evt) {	}
	public void focusLost(FocusEvent evt) {	}
	public void keyTyped(KeyEvent evt) {}
	public void keyPressed(KeyEvent evt) {	}
	public void keyReleased(KeyEvent evt) {	}
	public void mouseEntered(MouseEvent evt) { } 
	public void mousePressed(MouseEvent evt) {	}
    public void mouseExited(MouseEvent evt) { } 
    public void mouseReleased(MouseEvent evt) {  } 
    public void mouseClicked(MouseEvent evt) { 
    	int x = evt.getX();
    	int y = evt.getY();
    	int freeBulletSpot = -1;
    	for (int i = 0; i < bulletSpotTaken.length; i++)
    		if (!(bulletSpotTaken[i])) {	
    			freeBulletSpot = i;
    			break;
    		}
    	if (freeBulletSpot == -1) return;
    	bullets[freeBulletSpot] = new Rectangle(x - TILESIZE / 32, y - TILESIZE / 4, TILESIZE / 32 * 2, TILESIZE / 2);
    	bulletSpotTaken[freeBulletSpot] = true;
    	countBullets++;
    }
}