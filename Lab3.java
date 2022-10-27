import java.io.*;
import java.util.*;

public class Lab3 {
	
	/*
	 * 
	 * VARIABLES
	 * 
	 */
	
	static int numberOfPuzzles;
	static ArrayList<String> sudoku;
	static ArrayList<String> original;
	static ArrayList<String> solutions;
	static int currentIndex = 0;
	static char empty = '_';
	static int currentRow = 0;
	
	
	
	
	
	
	
	
	
	
	/*
	 * 
	 * MAIN
	 * 
	 */
	
	// Main
	public static void main(String[] args) {		
		getInput();
		
		for (int i = 1; i <= numberOfPuzzles; i ++) {
			
			solutions = new ArrayList<String>();
			
			int row = 9 * (i - 1);
			int column = 0;
			
			currentRow = row;
			
			if (solveCount(row, column)) {
				
				if (solutions.size() == 1) {
//					display(i); ~ old method
					displaySolution(solutions, i);
					sudoku = original;
				} else {
					print(String.format("Puzzle %d has %d solutions\n", i, solutions.size()));
				}
			} else print(String.format("Puzzle %d has no solution\n", i));
			
			currentIndex ++;
			solutions.clear();
		}
	}
	
	
	
	
	
	
	
	
	
	
	/*
	 * 
	 * INPUT METHODS
	 * 
	 */
	
	// Get the Input from System.in and store the values
	static void getInput() {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		numberOfPuzzles = getNumberOfPuzzles(in);
		sudoku = getSudoku(in);
		original = sudoku;
	}
	
	// Get the total number of puzzles from the input
	static int getNumberOfPuzzles(BufferedReader in) {
		int output = 0;
		
		try {
			print("Input: ");
			
			output = Integer.parseInt(in.readLine());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return output;
	}
	
	// Get Sudoku Puzzles from the input
	static ArrayList<String> getSudoku(BufferedReader in) {
		ArrayList<String> output = new ArrayList<String>();
		
		try {
			for (int i = 0; i < numberOfPuzzles * 9; i ++) {
				output.add(in.readLine());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		print("");
		
		return output;
	}
	
	
	
	
	
	
	
	
	
	
	/*
	 * 
	 * METHODS CHECKING EXISTENCE
	 * 
	 */
	
	// Check if chosen number can be entered in the chosen position
	static boolean isAllowed(int number, int row, int column) {
		return !(isInRow(number, row) || isInColumn(number, column) || isInBox(number, row, column));
	}
	
	// Check if the chosen position is empty
	static boolean isEmpty(int row, int column) {
		return (sudoku.get(row).charAt(column) == empty);
	}
	
	// Check if a number already exists in the chosen row
	static boolean isInRow(int number, int row) {
		boolean output = false;
		
		String numberString = "" + number;
		String content = sudoku.get(row);
		
		output = stringContains(content, numberString);
		
		return output;
	}
	
	// Check if a number already exists in the chosen column
	static boolean isInColumn(int number, int column) {
		boolean output = false;
		
		String numberString = "" + number;
		int start = currentIndex * 9;
		int end = start + 9;
		String content = "";
		
		for (int i = start; i < end; i ++) {
			String row = sudoku.get(i);
			content = content + row.charAt(column);
		}
		
		output = stringContains(content, numberString);
		
		return output;
	}
	
	// Check if the chosen number already exists in the 3x3 box surrounding the position
	static boolean isInBox(int number, int row, int column) {
		boolean output = false;
		
		int r = row - row % 3;
		int c = column - column % 3;
		String numberString = "" + number;
		
		for (int i = r; i< r+3; i++) {
			for(int j = c; j < c+3; j++) {
				String checkFor = "" + sudoku.get(i).charAt(j);
				if (checkFor.equals(numberString)) {
					output = true;
				}
			}
		}
		
		return output;
	}
	
	// Internal method to check if the given string contains the chosen character
	static boolean stringContains(String content, String lookFor) {
		boolean output = false;
		
		content = content.replaceAll("", " ");
		StringTokenizer st = new StringTokenizer(content, " ");
		
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.equals(lookFor)) {
				output = true;
			}
		}
		
		return output;
	}
	
	static boolean isComplete(String content) {
		if (content.contains("" + empty)) {
			return false;
		} else return true;
	}
	
	
	
	
	
	
	
	
	
	
	/*
	 * 
	 * SOLVING METHODS
	 * 
	 */
	
	// Recursive method to solve the puzzle
	public static boolean solveCount(int row, int column) {		
		
		// Check if final cell has been filled and stop recursion (final case. Has to return true)
		if (row % 9 == 8 && column == 9) {
			saveSolution(currentRow);
			return true;
		}
		
		// If column is filled, go to the next row
		if (column == 9) {
			row ++;
			column = 0;
		}
		
		// If the chosen cell is already filled, go to the next cell
		if (!isEmpty(row, column)) {
			return solveCount(row, column + 1);
		}
		
		// Iterate over all possible values for the cell
		for (int number = 1; number < 10; number ++) {
			
			// Check if cell can be filled with the chosen number
			if (isAllowed(number, row, column)) {
				
				// Fill the position with the chosen number
				fill(number, row, column);
				
				// Go to the next iteration of the recursive process
				solveCount(row, column + 1);
			}
			
			// Go back to previous position and try a different value (Backtracking)
			setEmpty(row, column);
		}
		
		// Check if it has solutions and return accordingly.		
		if (solutions.size() == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	// Fill a position with the chosen number
	static void fill(int number, int row, int column) {
		String content = sudoku.get(row);
		
		content = content.substring(0, column) + number + content.substring(column + 1);
		
		sudoku.set(row, content);
	}
	
	// Remove number from cell
	static void setEmpty(int row, int column) {
		String content = sudoku.get(row);
		
		content = content.substring(0, column) + "_" + content.substring(column + 1);
		
		sudoku.set(row, content);
	}
	
	// Turn an ArrayList into a string
	static String arrayToString(int start) {
		String content = "";
		
		for (int i = start; i < start + 9; i ++) {
			content = content + sudoku.get(i);
		}
		
		return content;
	}
	
	// Add the solution to a solutions ArrayList if it is not already in it and does not contain any blank spaces.
	static void saveSolution(int start) {
		String content = arrayToString(start);
		
		if (!solutions.contains(content) && isComplete(content)) {
			solutions.add(content);
		}
	}
	
	
	
	
	
	
	
	
	
	
	/*
	 * 
	 * DISPLAY METHODS
	 * 
	 */
	
	// Display the chosen puzzle
	static void displaySolution(ArrayList<String> s, int puzzleNumber) {
		String header = String.format("Puzzle %d solution is" , puzzleNumber);
		print(header);
		
		String content = s.get(0);
		
		for (int i = 0; i < 9; i ++) {
			int start = i * 9;
			int end = start + 9;
			String row = content.substring(start, end);
			
			if (i == 8) {
				print(row + "\n");
			} else print(row);
		}
	}
	
	// Internal method to make System.out easier
	static void print (Object o) {
		System.out.println(o);
	}
	
	
	
	
	
	
	
	
	
	
}
