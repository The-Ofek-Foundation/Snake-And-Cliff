public class SNC2 {
	private int[][] directions;
	private int[][][] directionsSave;
	private int saveIndex;
	private int length, distance;

	private double startTime;

	public SNC2(int length, int distance) {
		this.length = length;
		this.distance = distance;
		saveIndex = 0;

		directions = new int[length / 2][length + 2];
		directionsSave = new int[length][directions.length][directions[0].length];

		for (int i = 0; i < directions.length; i++) {
			directions[i][1] = i;
			for (int a = 2; a < directions[i].length; a++)
				if ((a - 1) % (i + 1) != 0)
					directions[i][a] = -195195;
		}
		/*
		 * Directions:
		 * current sum, next dir, directions
		*/
		save();
	}

	private void save() {
		for (int i = 0; i < directions.length; i++)
			for (int a = 0; a < directions[i].length; a++)
				directionsSave[saveIndex][i][a] = directions[i][a];
		saveIndex++;
	}

	private void undo() {
		saveIndex--;
		reload();
	}

	private void reload() {
		for (int i = 0; i < directions.length; i++)
			for (int a = 0; a < directions[i].length; a++)
				directions[i][a] = directionsSave[saveIndex - 1][i][a];
	}


	public static void main(String... pumpkins) { // length, distance
		SNC2 snc2 = new SNC2(Integer.parseInt(pumpkins[0]), Integer.parseInt(pumpkins[1]));
		snc2.run();
	}

	public void run() {
		// directions[0][0] = -1;
		// directions[0][1] = 1;
		// directions[0][2] = -1;

		// for (int i : directions[2])
		// 	System.out.print(i + " ");
		// System.out.println();

		// System.out.println(playMove(-1, 0));
		// System.out.println(playForcedMoves());

		System.out.println();

		dos("Solving problem");
		if (solve(-1, 0))
			done("found solution");
		else done("deemed impossible");

		System.out.println();

		printDirs();

		// for (int i : directions[0])
		// 	System.out.print(i + " ");
		// System.out.println();
	}

	public boolean solve(int option, int index) {
		// printDirs();
		// System.out.println(option + " " + index + " " + saveIndex);
		if (index < 0)
			return false;
		// System.out.println("75");
		if (option == 2) {
			if (saveIndex <= 1)
				return false;
			// System.out.println(saveIndex + " " + index);
			undo();
			return false;
		}
		// System.out.println("80");
		if (index >= length)
			return true;
		// System.out.println("82");
		if (!playMove(option, index)) {
			// System.out.println("86");
			reload();
			if (option == -1 && solve(1, index))
				return true;
			else {
				if (saveIndex <= 1)
					return false;
				undo();
				return false;
			}
		}
		if (index == length - 1)
			return true;
		// System.out.println("93");
		if (!playForcedMoves()) {
			// System.out.println("Force impossible");
			reload();
			if (option == -1 && solve(1, index))
				return true;
			else {
				undo();
				return false;
			}
		}
		if (directions[0][1] <= index) {
			reload();
			return false;
		}
		// System.out.println("99");
		save();
		// System.out.println("!!! " + directions[0][1]);
		if (solve(-1, directions[0][1]))
			return true;
		else {
			reload();
			if (option == -1 && solve(1, index))
				return true;
			else {
				if (saveIndex <= 1)
					return false;
				undo();
				return false;
			}
		}
	}

	private boolean playForcedMoves() {
		for (int i = 0; i < directions.length; i++)
			while (directions[i][1] < length
			 && (directions[i][0] + 1 == distance
			 || directions[i][0] - 1 == -distance)) {
				// System.out.println(i + " " + directions[i][0]);
				if (!playMove((directions[i][0] == distance - 1) ? -1:1,
					directions[i][1]))
					return false;
			 }
		return true;
	}

	private boolean playMove(int option, int index) {
		// System.out.println("PLAY MOVE " + option + " " + index);
		if (index >= length) return true;
		for (int i = 0; i < directions.length; i++)
			if (directions[i][index + 2] != -195195) {
				if (directions[i][index + 2] != 0) {
					// System.out.println(index + " " + directions[i][index + 2]);
					// System.out.println("HERE");
					return false;
				}
				directions[i][index + 2] = option;
				if (directions[i][1] == index)
					for (; directions[i][1] < length
						&& directions[i][directions[i][1] + 2] != 0;
						directions[i][1] += (i + 1)) {

						directions[i][0] += directions[i][
							directions[i][1] + 2];
						if (directions[i][0] >= distance
							|| directions[i][0] <= -distance)
							return false;
					}
			}

		return true;
	}

	private void printDirs() {
		for (int i = 0; i < directions.length; i++) {
			for (int item : directions[i])
				System.out.printf("%7d ", item);
			System.out.println();
		}
	}

	public void dos(String something) {
		System.out.printf("%-35s", something + "...");
		startTime = System.nanoTime();
	}

	public void done() { done("done"); }

	public void done(String message) {
		double elapsedTime = (System.nanoTime() - startTime) / 1E9;
		System.out.printf("%s in %.6f seconds\n", message, elapsedTime);
	}
}
