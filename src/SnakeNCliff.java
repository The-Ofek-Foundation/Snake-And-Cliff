/*	Ofek Gila
	February 28th, 2014
	SnakeNCliff.java
	This program will create solutions for the Snake and Cliff problem.
*/
import java.io.File;										// Imports java classes
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SnakeNCliff {
	
	public Scanner keyboard = new Scanner(System.in);
	public PrintWriter output;
	public File iofile = new File("SnakeNCliffSave.txt");
	public Scanner input;
	public int steps;
	public int countStep;
	public int distance;
	public byte[] directions; //(left(-1), undefined(0), right(1))
	public int currentLocation;
	public boolean dead, done;
	public byte[][] directionsSave;
	public double timeStarted;
	
	SnakeNCliff() {
		currentLocation = 0;
		dead = false;
		done = false;
	}
	public static void main(String[] pumpkins) {
		SnakeNCliff SNC = new SnakeNCliff();
		/*System.out.print("load?\t->\t");
		if (SNC.keyboard.nextLine().equalsIgnoreCase("yes")) {
			SNC.load();
			SNC.run(false);
			System.exit(0);
		}*/
		SNC.run(true, true);
	}
	public void run(boolean needUserInput, boolean output) {
		if (needUserInput) getUserInput();
		if (output) {
			System.out.println();
			timeStarted = System.nanoTime();
			if (!(solved(1, 0, 0, 0))) System.out.println("Impossible");
			else System.out.println("Possible!");
			System.out.println();
			System.out.println("Time taken: " + ((System.nanoTime() - timeStarted)/1E9) + "\n");
			printDirections();
			keyboard.nextLine();
		}
		else solved(1, 0, 0, 0);
		//System.out.print("Save?\t->\t");
		//if (keyboard.nextLine().equalsIgnoreCase("yes")) save();
	}
	public void printDirections() {
		for (int i = 0; i < steps; i++)	{
			System.out.printf("%2d ", directions[i]);
			if (i % 30 == 29) System.out.println();
		}
		System.out.println();
	}
	public boolean solved(int direction, int nextEmptySpot, int saveNumber, int lastFilledSpot)	{
		//System.out.println("Direction " + direction);
		if (nextEmptySpot == -1) {	return true;	}
		for (int i = 0; i < lastFilledSpot; i++)
			directions[i] = directionsSave[i][saveNumber];
		dead = false;
		directions[nextEmptySpot] = (byte)direction;
		createSolution(1);
		nextEmptySpot = findNextEmptySpot();
		lastFilledSpot = findLastFilledSpot();
		if (dead) return false;
		for(int i = 0; i < lastFilledSpot; i++)
			directionsSave[i][saveNumber + 1] = directions[i];
		//System.out.println("not dead");
		//if (!(done)) printDirections(directions);
		if (solved(1, nextEmptySpot, saveNumber + 1, lastFilledSpot)) return true;
		//System.out.println("not 1");
		//if (!(done)) printDirections(directions);
		//done = true;
		if (solved(-1, nextEmptySpot, saveNumber + 1, lastFilledSpot)) return true;
		return false;
	}
	public void createSolution(int jumpBy) {
		if (dead) return;
		if (steps / distance < jumpBy) {
			return;
		}
		currentLocation = 0;
		for (countStep = jumpBy - 1; countStep < steps; countStep += jumpBy) {
			if (directions[countStep] == 0) break;
			if (Math.abs(currentLocation + directions[countStep]) == distance) break;
			currentLocation += directions[countStep];
		}
		countStep -= jumpBy;
		if (distance - currentLocation == 1 && countStep + jumpBy < steps) {
			if (directions[countStep + jumpBy] == 1)	{
				dead = true;
				return;
			}
			directions[countStep + jumpBy] = -1;
		}
		if (distance + currentLocation == 1 && countStep + jumpBy < steps) {
			if (directions[countStep + jumpBy] == -1)	{
				dead = true;
				return;
			}
			directions[countStep + jumpBy] = 1;
		}
		createSolution(jumpBy + 1);
		//return directions;
	}
	public int findNextEmptySpot() {
		int i;
		for (i = 0; i < steps; i += 1)
			if (directions[i] == 0) return i;
		return -1;
	}
	public int findLastFilledSpot() {
		int i;
		for (i = steps - 1; directions[i] == 0; i--);
		return i;
	}
	public void getUserInput() {
		System.out.print("Steps (-1 for keep going 'till done)\t->\t");
		steps = keyboard.nextInt();
		if (steps < 0) steps = 5000;
		System.out.print("Distance from obstacles\t\t\t->\t");
		distance = keyboard.nextInt();
		
		directions = new byte[steps];
		directionsSave = new byte[steps][steps];
		//directions[0] = 1;
	}
	public void save()	{
		try {
			output = new PrintWriter(iofile);
		}
		catch (IOException e)	{
			System.err.println("ERROR: Cannot open file SnakeNCliffSave.txt");
			System.exit(99);
		}
		output.println(steps + " " + distance);
		for (int i = 0; i < steps; i++)
			output.print(directions[i] + " ");
		output.println();
		output.close();
	}
	public void load()	{
		try {
			input = new Scanner(iofile);
		}
		catch (FileNotFoundException e)	{
			System.err.println("ERROR: Cannot open file SnakeNCliffSave.txt");
			System.exit(97);
		}
		steps = input.nextInt();
		distance = input.nextInt();
		System.out.println("Steps:\t" + steps);
		System.out.print("How many more to add\t->\t");
		int add = keyboard.nextInt();
		steps += add;
		directions = new byte[steps];
		directionsSave = new byte[steps][steps];
		for (int i = 0; i < steps - add; i++) 
			directions[i] = (byte)input.nextInt();
		input.close();
	}
}