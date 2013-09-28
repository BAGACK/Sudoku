package com.comze_instancelabs.sudoku;

import java.util.ArrayList;
import java.util.Random;

public class Sudoku {
	// Final int constants.
	static final int ROW = 9;
	static final int COLUMN = 9;
	private final int SHUFFLE = 100000;

	// Difficulty.
	static int DIFFICULTY = 20; // 10

	// Different 2D arrays to save answer, problem, progress.
	private int[][] answer = new int[ROW][COLUMN];
	private int[][] problem = new int[ROW][COLUMN];
	private int[][] player = new int[ROW][COLUMN];
	private int[][] comp = new int[ROW][COLUMN];

	// 2 arrays to
	private int[] ansArray = new int[DIFFICULTY];
	private int[] playerArray = new int[DIFFICULTY];

	// Stores variables to generate random sudoku.
	private int[] variables = new int[ROW];

	// Random class
	Random rand = new Random();

	/* Constructor */
	public Sudoku(int dif) {
		DIFFICULTY = dif;
		build();
	}

	/* Public Methods */

	// Public method to build the whole game.
	public void build() {
		generate();
		storeAns();
		// display();
	}

	// Public method to check if player's sudoku has reached the answer.
	public boolean checkAns() {
		for (int i = 0; i < ansArray.length; i++) {
			if (playerArray[i] != ansArray[i]) {
				return false;
			}
		}
		return true;
	}

	// Public method to clear data.
	public void clear() {
		copy(problem, player);
	}

	// Public method to display player's version of sudoku.
	public void display() {
		for (int i = 0; i < player.length; ++i) {
			// inserting empty line in every 3 rows.
			if (i % 3 == 0 && i != 0) {
				System.out.println();
			}
			for (int j = 0; j < player[i].length; ++j) {
				// inserting empty space in every 3 columns.
				if (j % 3 == 0 && j != 0) {
					System.out.print(' ');
				}
				if (problem[i][j] == 0 && player[i][j] == 0) {
					System.out.print("( )");
				} else if (problem[i][j] == 0) {
					System.out.print("(" + player[i][j] + ")");
				} else {
					System.out.print("[" + player[i][j] + "]");
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	// Public method to display player's version of sudoku.
	public ArrayList<Integer[]> display_() {
		ArrayList<Integer[]> f = new ArrayList<Integer[]>();
		ArrayList<Integer> row = new ArrayList<Integer>();
		Integer[] row_;
		for (int i = 0; i < player.length; ++i) {
			// inserting empty line in every 3 rows.
			if (i % 3 == 0 && i != 0) {
				System.out.println();
			}
			for (int j = 0; j < player[i].length; ++j) {
				// inserting empty space in every 3 columns.
				/*
				 * if (j % 3 == 0 && j != 0) { System.out.print(' '); }
				 */
				if (problem[i][j] == 0 && player[i][j] == 0) {
					// System.out.print("( )");
					row.add(0);
				} else if (problem[i][j] == 0) {
					//System.out.print("(" + player[i][j] + ")");
					row.add(player[i][j]);
				} else {
					//System.out.print("[" + player[i][j] + "]");
					row.add(player[i][j]);
				}
			}
			row_ = new Integer[row.size()];
			row_ = row.toArray(row_);
			f.add(row_);
			row_ = null;
			row.clear();
		}
		System.out.println();

		return f;
	}

	// Public method to generate a new puzzle.
	public void generate() {
		int randNum;

		// Generates variables.
		for (int i = 0; i < variables.length; i++) {
			randNum = rand.nextInt(9) + 1;
			while (!checkDuplicate(variables, i, randNum)) {
				randNum = rand.nextInt(9) + 1;
			}
			variables[i] = randNum;
		}

		// Example sudoku.
		randNum = rand.nextInt(3) + 1;
		switch (randNum) {
		case 1:
			int[][] array1 = { { 4, 7, 1, 3, 2, 8, 5, 9, 6 },
					{ 6, 3, 9, 5, 1, 4, 7, 8, 2 },
					{ 5, 2, 8, 6, 7, 9, 1, 3, 4 },
					{ 1, 4, 2, 9, 6, 7, 3, 5, 8 },
					{ 8, 9, 7, 2, 5, 3, 4, 6, 1 },
					{ 3, 6, 5, 4, 8, 1, 2, 7, 9 },
					{ 9, 5, 6, 1, 3, 2, 8, 4, 7 },
					{ 2, 8, 4, 7, 9, 5, 6, 1, 3 },
					{ 7, 1, 3, 8, 4, 6, 9, 2, 5 } };
			copy(array1, answer); // Copies example to answer.
			break;
		case 2:
			int[][] array2 = { { 8, 3, 5, 4, 1, 6, 9, 2, 7 },
					{ 2, 9, 6, 8, 5, 7, 4, 3, 1 },
					{ 4, 1, 7, 2, 9, 3, 6, 5, 8 },
					{ 5, 6, 9, 1, 3, 4, 7, 8, 2 },
					{ 1, 2, 3, 6, 7, 8, 5, 4, 9 },
					{ 7, 4, 8, 5, 2, 9, 1, 6, 3 },
					{ 6, 5, 2, 7, 8, 1, 3, 9, 4 },
					{ 9, 8, 1, 3, 4, 5, 2, 7, 6 },
					{ 3, 7, 4, 9, 6, 2, 8, 1, 5 } };
			copy(array2, answer);
			break;
		case 3:
			int[][] array3 = { { 5, 3, 4, 6, 7, 8, 9, 1, 2 },
					{ 6, 7, 2, 1, 9, 5, 3, 4, 8 },
					{ 1, 9, 8, 3, 4, 2, 5, 6, 7 },
					{ 8, 5, 9, 7, 6, 1, 4, 2, 3 },
					{ 4, 2, 6, 8, 5, 3, 7, 9, 1 },
					{ 7, 1, 3, 9, 2, 4, 8, 5, 6 },
					{ 9, 6, 1, 5, 3, 7, 2, 8, 4 },
					{ 2, 8, 7, 4, 1, 9, 6, 3, 5 },
					{ 3, 4, 5, 2, 8, 6, 1, 7, 9 } };
			copy(array3, answer);
			break;
		}

		replace(answer); // Randomize once more.
		copy(answer, problem); // Copies answer to problem.
		genProb(); // Generates problem.

		shuffle();
		copy(problem, player); // Copies problem to player.

		// Chekcing for shuffled problem
		copy(problem, comp);

		if (!solve(0, 0, 0, comp)) {
			generate();
		}
		if (!unique()) {
			generate();
		}
	}

	// Public method to get probelm value
	public int getProblemValue(int row, int column) {
		return problem[row][column];
	}

	// Public method to check if player's choice valid
	public boolean isValid(int row, int column, int value) {
		for (int k = 0; k < 9; k++) { // Check row.
			if (value == player[k][column]) {
				return false;
			}
		}

		for (int k = 0; k < 9; k++) { // Check column.
			if (value == player[row][k]) {
				return false;
			}
		}

		int boxRowOffset = (row / 3) * 3; // Set offset to determine box.
		int boxColOffset = (column / 3) * 3;
		for (int k = 0; k < 3; k++) { // Check box.
			for (int m = 0; m < 3; m++) {
				if (value == player[boxRowOffset + k][boxColOffset + m]) {
					return false;
				}
			}
		}

		return true; // no violations, so it's legal
	}

	// Public method to mutate player sudoku.
	public void setPlayer(int row, int col, int value) {
		player[row][col] = value;
	}

	// Public method storePlayer; stores player's answer.
	public void storePlayer(int index, int val) {
		playerArray[index] = val;
	}

	/* Private Helper Methods */

	// Private helper method checkComp; checks if it has same solution.
	private boolean checkComp() {
		for (int i = 0; i < comp.length; i++) {
			for (int j = 0; j < comp[i].length; j++) {
				if (comp[i][j] != answer[i][j]) {
					return false;
				}
			}
		}
		return true;
	}

	// Private helper method checkDuplicate; it checks if it's alrdy repeated.
	private boolean checkDuplicate(int[] array, int index, int value) {
		for (int i = 0; i < index; i++) {
			if (array[i] == value) {
				return false;
			}
		}
		return true;
	}

	// Private helper method copy; it deep copies array.
	private void copy(int[][] origin, int[][] dest) {
		for (int i = 0; i < origin.length; i++) {
			for (int j = 0; j < origin[i].length; j++) {
				dest[i][j] = origin[i][j];
			}
		}
	}

	// Private helper mthod genProb; it generates problem from answer sudoku.
	private void genProb() {
		int row;
		int column;
		int removed1;
		int removed2;

		for (int i = 0; i < DIFFICULTY / 2;) {
			row = rand.nextInt(9);
			column = rand.nextInt(9);
			while (problem[row][column] == 0 || (row == 4 && column == 4)) {
				row = rand.nextInt(9);
				column = rand.nextInt(9);
			}

			// Clearing random boxes.
			removed1 = problem[row][column];
			removed2 = problem[8 - row][8 - column];
			problem[row][column] = 0;
			problem[8 - row][8 - column] = 0;

			copy(problem, comp);

			if (!solve(0, 0, 0, comp)) { // Case of unsolvable.
				// Putting back original values.
				problem[row][column] = removed1;
				problem[8 - row][8 - column] = removed2;
			} else {
				if (unique()) { // Case of solution is unique.
					i++;
				} else { // Case of soluiton is not unique.
					// Putting back original values.
					problem[row][column] = removed1;
					problem[8 - row][8 - column] = removed2;
				}
			}
		}
	}

	// Private helper method legal; checks if the number is legal to put in.
	private boolean legal(int i, int j, int val, int[][] cells) {
		for (int k = 0; k < 9; k++) { // Check row.
			if (val == cells[k][j]) {
				return false;
			}
		}

		for (int k = 0; k < 9; k++) { // Check column.
			if (val == cells[i][k]) {
				return false;
			}
		}

		int boxRowOffset = (i / 3) * 3; // Set offset to determine box.
		int boxColOffset = (j / 3) * 3;
		for (int k = 0; k < 3; k++) { // Check box.
			for (int m = 0; m < 3; m++) {
				if (val == cells[boxRowOffset + k][boxColOffset + m]) {
					return false;
				}
			}
		}

		return true; // no violations, so it's legal
	}

	// Private helper method replace; it creates random puzzle.
	private void replace(int[][] array) {
		int[] temp = new int[ROW];

		for (int i = 0; i < ROW; i++) {
			temp[i] = array[0][i];
		}

		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < ROW; j++) {
				for (int k = 0; k < COLUMN; k++) {
					if (array[j][k] == temp[i]) {
						array[j][k] = variables[i] + 10;
					}
				}
			}
		}

		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COLUMN; j++) {
				array[i][j] -= 10;
			}
		}
	}

	// Private helper method shuffle; it shuffles both answer and problem.
	private void shuffle() {
		int[] temp = new int[ROW];
		int row, col, decider, indexToSwapWith;

		// Shuffle as many we want.
		for (int i = 0; i < SHUFFLE; i++) {
			// Decide which line to shuffle (ex. row or column).
			decider = rand.nextInt(2);

			if (decider == 0) { // Swaps row.
				row = rand.nextInt(9);

				if (row < 3) {
					indexToSwapWith = rand.nextInt(3);

					// Decides row to swap with.
					while (indexToSwapWith == row) {
						indexToSwapWith = rand.nextInt(3);
					}

					// Swap
					for (int j = 0; j < ROW; j++) {
						temp[j] = answer[row][j];
						answer[row][j] = answer[indexToSwapWith][j];
						answer[indexToSwapWith][j] = temp[j];
						temp[j] = problem[row][j];
						problem[row][j] = problem[indexToSwapWith][j];
						problem[indexToSwapWith][j] = temp[j];
					}
				} else if (row < 6) {
					indexToSwapWith = rand.nextInt(3) + 3;

					while (indexToSwapWith == row) {
						indexToSwapWith = rand.nextInt(3) + 3;
					}

					for (int j = 0; j < ROW; j++) {
						temp[j] = answer[row][j];
						answer[row][j] = answer[indexToSwapWith][j];
						answer[indexToSwapWith][j] = temp[j];
						temp[j] = problem[row][j];
						problem[row][j] = problem[indexToSwapWith][j];
						problem[indexToSwapWith][j] = temp[j];
					}
				} else {
					indexToSwapWith = rand.nextInt(3) + 6;

					while (indexToSwapWith == row) {
						indexToSwapWith = rand.nextInt(3) + 6;
					}

					for (int j = 0; j < ROW; j++) {
						temp[j] = answer[row][j];
						answer[row][j] = answer[indexToSwapWith][j];
						answer[indexToSwapWith][j] = temp[j];
						temp[j] = problem[row][j];
						problem[row][j] = problem[indexToSwapWith][j];
						problem[indexToSwapWith][j] = temp[j];
					}
				}
			} else {
				col = rand.nextInt(9);

				if (col < 3) {
					indexToSwapWith = rand.nextInt(3);

					// Decides column to swap with.
					while (indexToSwapWith == col) {
						indexToSwapWith = rand.nextInt(3);
					}

					// Swap
					for (int j = 0; j < COLUMN; j++) {
						temp[j] = answer[j][col];
						answer[j][col] = answer[j][indexToSwapWith];
						answer[j][indexToSwapWith] = temp[j];
						temp[j] = problem[j][col];
						problem[j][col] = problem[j][indexToSwapWith];
						problem[j][indexToSwapWith] = temp[j];
					}
				} else if (col < 6) {
					indexToSwapWith = rand.nextInt(3) + 3;

					// Decides column to swap with.
					while (indexToSwapWith == col) {
						indexToSwapWith = rand.nextInt(3) + 3;
					}

					// Swap
					for (int j = 0; j < COLUMN; j++) {
						temp[j] = answer[j][col];
						answer[j][col] = answer[j][indexToSwapWith];
						answer[j][indexToSwapWith] = temp[j];
						temp[j] = problem[j][col];
						problem[j][col] = problem[j][indexToSwapWith];
						problem[j][indexToSwapWith] = temp[j];
					}
				} else {
					indexToSwapWith = rand.nextInt(3) + 6;

					// Decides column to swap with.
					while (indexToSwapWith == col) {
						indexToSwapWith = rand.nextInt(3) + 6;
					}

					// Swap
					for (int j = 0; j < COLUMN; j++) {
						temp[j] = answer[j][col];
						answer[j][col] = answer[j][indexToSwapWith];
						answer[j][indexToSwapWith] = temp[j];
						temp[j] = problem[j][col];
						problem[j][col] = problem[j][indexToSwapWith];
						problem[j][indexToSwapWith] = temp[j];
					}
				}
			}
		}
	}

	// Private helper method solve; checks if it's solvable.
	private boolean solve(int i, int j, int pos, int[][] cells) {
		if (i == 9) {
			i = 0;
			if (++j == 9)
				return true;
		}
		if (cells[i][j] != 0) // Skip filled cells
			return solve(i + 1, j, pos, cells);

		for (int k = 1; k <= 9; k++) {
			int val = k + pos;
			if (val > 9) {
				val = val - 9;
			}
			if (legal(i, j, val, cells)) {
				cells[i][j] = val;
				if (solve(i + 1, j, pos, cells))
					return true;
			}
		}
		cells[i][j] = 0; // Reset on backtrack
		return false;
	}

	// Private helper method storeAns; stores blanks.
	private void storeAns() {
		int count = 0; // Keeps track of blank number.

		for (int i = 0; i < 3; i++) { // row
			for (int j = 0; j < 3; j++) { // column
				for (int k = i * 3; k < i * 3 + 3; k++) { // Corressponding box.
					for (int l = j * 3; l < j * 3 + 3; l++) {
						if (problem[k][l] == 0) {
							ansArray[count] = answer[k][l]; // fills ansArray.
							count++;
						}
					}
				}
			}
		}
	}

	// Private helper method unique;
	private boolean unique() {
		for (int i = 1; i <= 9; i++) {
			copy(problem, comp);
			solve(0, 0, i, comp);
			if (!checkComp()) {
				return false;
			}
		}
		return true;
	}
}
