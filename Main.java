package moviendoCajas;

import java.util.ArrayList;
import java.util.Scanner;

/** 
 * @author Diego Alcoba Arias
 */

public class Main {
	
	private final static char robot = '@';
	private final static char box = '#';
	private final static char wall = '0';
	private final static char internalWall = '1';
	private final static char objective = '!';
	private final static char robotAtObjective = '+';
	private final static char boxAtObjective = '*';
	private static ArrayList<RoomState> states = new ArrayList<>();

	/**
	 * Takes the inputs of the program, and check if they're correct
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		int rows;
		int cols;
		
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		
		/*Reads number of rows*/
		String stRows = sc.next();
		
		if (!isNumeric(stRows)) {
			System.out.println("Entrada mal formada");
			sc.close();
			
			return;
		} else {
			rows = Integer.parseInt(stRows);
		}
		
		/*Reads number of columns*/
		String stCols = sc.next();
		
		if (!isNumeric(stCols)) {
			System.out.println("Entrada mal formada");
			sc.close();
			
			return;
		} else {
			cols = Integer.parseInt(stCols);
		}
		
		/*Creates room matrix and checks specs*/
		char[][] room = new char[rows][cols];
		
		if (!createRoom(room, sc, rows, cols)) {
			System.out.println("Entrada mal formada");
			
			return;
		}
		
		if (!check(room, rows, cols)) {
			System.out.println("Entrada mal formada");
			
			return;
		} else {		
			move(room, rows, cols);
		}
	}
	
	/**
	 * Checks if the numbers introduced are integers
	 * @param string
	 * @return true if integer, false if not
	 */
	public static boolean isNumeric(String string) {
		
		boolean numeric;
		
		try {
			Integer.parseInt(string);
			numeric = true;
		} 
		catch (NumberFormatException except) {
			numeric = false;
		}
		
		return numeric;
	}
	
	/**
	 * Creates the room from the inputs
	 * @param room
	 * @param sc
	 * @param rows
	 * @param cols
	 * @return true if room creates, false if not
	 */
	private static boolean createRoom(char[][] room, Scanner sc, int rows, int cols) {
		
		String str = sc.nextLine();
		
		for (int i = 0; i < rows; i++) {
			str = sc.nextLine();
			
			if (str.length() != cols) {
				return false;
			}
			
			for (int j = 0; j < cols; j++) {
				
				if (str.length() < 1) {
					return false;
				} else {
					char t = str.charAt(j);
					
					room[i][j] = t;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Checks if there are wrong inputs
	 * @param room
	 * @param rows
	 * @param cols
	 * @return true if matrix meets specs, false if not
	 */
	public static boolean check(char[][] room, int rows, int cols) {
		
		int nRobots = 0;
		int nBoxes = 0;
		int nObjectives = 0;
		
		/*Number of each thing in the room*/
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				
				if (room[i][j] == robot) {
					nRobots++;
				}
				
				if (room[i][j] == box) {
					nBoxes++;
				}
				
				if (room[i][j] == objective) {
					nObjectives++;
				}
				
				if (room[i][j] == boxAtObjective) {
					nObjectives++;
					nBoxes++;
				}
				
				if (room[i][j] == robotAtObjective) {
					nObjectives++;
					nRobots++;
				}
			}
		}
		
		/*There can't be more than one robot*/
		if (nRobots != 1) {
			
			return false;
		}
		
		/*Must be same number of boxes than objectives*/
		if (nBoxes != nObjectives) {
			
			return false;
		}
		
		/*There can't be other different elements than the specified*/
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				
				if (room[i][j] != robot && room[i][j] != wall && room[i][j] != box && room[i][j] != objective && room[i][j] != robotAtObjective && room[i][j] != boxAtObjective && room[i][j] != internalWall && room[i][j] != '-' && room[i][j] != ' ') {
					
					return false;
				}
			}
		}
		
		/*Entire blank rows*/
		for (int i = 0; i < rows; i++) {
			
			boolean blanks = true;
			
			for (int j = 0; j < cols; j++) {
				
				if (room[i][j] != ' ') {
					blanks = false;
				}
			}
			
			if (blanks == true) {
				
				return false;
			}
		}
		
		/*Entire blank columns*/
		for (int i = 0; i < cols; i++) {
			
			boolean blanks = true;
			
			for (int j = 0; j < rows; j++) {
				
				if (room[j][i] != ' ') {
					blanks = false;
				}
			}
			
			if (blanks == true) {
				
				return false;
			}
		}
		
		/*Check open spaces on matrix borders*/
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				
				if (i == 0 || j == 0|| i == rows-1 || j == cols-1) {
					if (room[i][j] != ' ' && room[i][j] != wall) {
						
						return false;
					}
				}
			}
		}
		
		/*Make sure that it is a closed figure
		 *Check cells near to blanks & create a bigger matrix that surround the original one
		 */
		char[][] surRoom = new char [rows+2][cols+2];
		
		//int elements = 0;
		
		for (int i = 0; i < rows+2; i++) {
			for (int j = 0; j < cols+2; j++) {
				
				if (i == 0 || j == 0 || i == rows+1 || j == cols+1) {
					
					surRoom[i][j] = '0';
				} else {
					surRoom[i][j] = room[i-1][j-1];
				}
			}
		}
		
		for (int i = 0; i < rows+2; i++) {
			for (int j = 0; j < cols+2; j++) {
				
				if(surRoom[i][j] == ' ' && ((surRoom[i+1][j] != wall && surRoom[i+1][j] != ' ') || (surRoom[i-1][j] != wall && surRoom[i-1][j] != ' ') || (surRoom[i][j+1] != wall && surRoom[i][j+1] != ' ') || (surRoom[i][j-1] != wall && surRoom[i][j-1] != ' '))) {
					
					System.out.println(i + ", " + j);
					
					return false;
				}
			}
		}
		
		/*No external walls inside the matrix*/
		for (int i = 1; i < rows-1; i++) {			
			int extWalls = 0;
			
			for (int j = 0; j < cols; j++) {
				
				if (room[i][j] == wall) {					
					extWalls++;
				}
			}			
			if (extWalls != 2) {
				
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Movement of the robot
	 * @param room
	 * @param rows
	 * @param cols
	 */
	public static void move(char[][] room, int rows, int cols) {
		
		StringBuilder path = new StringBuilder("");
		RoomState rn = new RoomState(room, path);
		
		states.add(rn);
		
		while (true) {
			
			int nextFree = -1;
			
			for (int i = 0; i < states.size(); i++) {
				
				if (!states.get(i).checked()) {
					nextFree = i;
					
					break;
				}
			}
			
			if (nextFree == -1) {
				
				System.out.println("No hay solución");
				
				break;
			}
			
			if (complete(states.get(nextFree).getRoom(), rows, cols)) {
				
				System.out.println(states.get(nextFree).getPath().length());
				System.out.println(states.get(nextFree).getPath().toString());
				
				break;
			}
			
			states.get(nextFree).check();
			
			char[][] leftState = new char[rows][cols];
			copyArray(leftState, states.get(nextFree).getRoom(), rows, cols);
			
			
			char[][] rightState = new char[rows][cols];
			copyArray(rightState, states.get(nextFree).getRoom(), rows, cols);
			
			char[][] downState = new char[rows][cols];
			copyArray(downState, states.get(nextFree).getRoom(), rows, cols);
			
			char[][] upState = new char[rows][cols];
			copyArray(upState, states.get(nextFree).getRoom(), rows, cols);
		
			String left = leftMovement(leftState, getRobotRow(leftState, rows, cols), getRobotCol(leftState, rows, cols));
			String right = rightMovement(rightState, getRobotRow(rightState, rows, cols), getRobotCol(rightState, rows, cols));
			String up = upMovement(upState, getRobotRow(upState, rows, cols), getRobotCol(upState, rows, cols));
			String down = downMovement(downState, getRobotRow(downState, rows, cols), getRobotCol(downState, rows, cols));
			
			StringBuilder leftPath = new StringBuilder("");
			StringBuilder rightPath = new StringBuilder("");
			StringBuilder upPath = new StringBuilder("");
			StringBuilder downPath = new StringBuilder("");
			
			copySB(leftPath, states.get(nextFree).getPath());
			copySB(rightPath, states.get(nextFree).getPath());
			copySB(upPath, states.get(nextFree).getPath());
			copySB(downPath, states.get(nextFree).getPath());
			
			RoomState leftRoomState = new RoomState(leftState, leftPath);
			RoomState rightRoomState = new RoomState(rightState, rightPath);
			RoomState upRoomState = new RoomState(upState, upPath);
			RoomState downRoomState = new RoomState(downState, downPath);
			
			if (left != "N" ) {
				
				if (notExist(leftRoomState)) {
					
					leftPath.append(left);
					
					states.add(leftRoomState);
				}
			}
			
			if (right != "N" ) {
				
				if (notExist(rightRoomState)) {
					
					rightPath.append(right);
					
					states.add(rightRoomState);
				}
			}
			
			if (up != "N" ) {
				
				if (notExist(upRoomState)) {
					
					upPath.append(up);
					
					states.add(upRoomState);
				}
			}
			
			if (down != "N" ) {
				
				if (notExist(downRoomState)) {
					
					downPath.append(down);
					
					states.add(downRoomState);
				}
			}
		}
	}
	
	/**
	 * Check if all the boxes are in the objective
	 * @param room
	 * @param rows
	 * @param cols
	 * @return true if task complete, false if not 
	 */
	public static boolean complete(char[][] room, int rows, int cols) {
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				
				if (room[i][j] == objective || room[i][j] == robotAtObjective) {
					
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Copies an array to avoid pass by reference
	 * @param originalArray
	 * @param copiedArray
	 * @param rows
	 * @param cols
	 */
	public static void copyArray(char [][] originalArray, char[][] copiedArray, int rows, int cols) {
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				
				originalArray[i][j] = copiedArray[i][j];
			}
		}
	}
	
	/**
	 * Moves robot in negative x-axis (left)
	 * @param room
	 * @param x
	 * @param y
	 * @return N if the movement couldn't be done
	 */
	public static String leftMovement(char[][] room, int x, int y) {
		
		if(room[x][y-1] == '0' || room[x][y-1] == '1') {
			
			return "N";
	
		} else if (room[x][y-1] == '#' || room[x][y-1] == '*') {
			
			if (room[x][y-2] == '0' || room[x][y-2] == '1' || room[x][y-2] == '#'|| room[x][y-2] == '*') {
			
				return "N";
			
			} else {
				if (room[x][y-2] != objective) {
					
					if(((room[x][y-3] == wall || room[x][y-3] == internalWall)) && ((room[x-1][y-2] == wall || room[x-1][y-2] == internalWall || room[x+1][y-2] == wall || room[x+1][y-2] == internalWall))) {
						
						return "N";
					}
				}
				
				if (room[x][y] == '+') {
					room[x][y] = '!';
				} else {
					room[x][y] = '-';
				}
				
				
				if (room[x][y-1] == '*') {
					room[x][y-1] = '+';
				} else {
					room[x][y-1] = '@';
				}
				
				if (room[x][y-2] == '!') {
					room[x][y-2] = '*';	
				} else {
					room[x][y-2] = '#';
				}				
				
				return "I";
			}
			
		} else {
			if (room[x][y] == '+') {
				room[x][y] = '!';
			}else {
				room[x][y] = '-';
			}
			
			if (room[x][y-1] == '!') {
				room[x][y-1] = '+';
			}else {
				room[x][y-1] = '@';
			}
			
			return "i";			
		}
	}
	
	/**
	 * Moves robot in positive x-axis (right)
	 * @param room
	 * @param x
	 * @param y
	 * @return N if the movement couldn't be done
	 */
	public static String rightMovement(char[][] room, int x, int y) {
		
		if (room[x][y+1] == '0'|| room[x][y+1] == '1') {
			
			return "N";
			
		} else if (room[x][y+1] == '#' || room[x][y+1] == '*') {
			
			if (room[x][y+2] == '0' || room[x][y+2] == '1' || room[x][y+2] == '#' || room[x][y+2] == '*') {
			
				return "N";
			
			} else {
				if (room[x][y+2] != objective) {
					if ((room[x][y+3] == wall || room[x][y+3] == internalWall) && (room[x-1][y+2] == wall || room[x-1][y+2] == internalWall || room[x+1][y+2] == wall || room[x+1][y+2] == internalWall)) {
						
						return "N";
					}
				}
				
				if (room[x][y] == '+') {
					room[x][y] = '!';
				}else {
					room[x][y] = '-';
				}
				
				
				if (room[x][y+1] == '*') {
					room[x][y+1] = '+';
				}else {
					room[x][y+1] = '@';
				}
				
				if (room[x][y+2] == '!') {
					room[x][y+2] = '*';	
				}else {
					room[x][y+2] = '#';
				}				
				
				return "D";
			}
			
		} else {
			if (room[x][y] == '+') {
				room[x][y] = '!';
			} else {
				room[x][y] = '-';
			}
			
			if (room[x][y+1] == '!') {
				room[x][y+1] = '+';
			} else {
				room[x][y+1] = '@';
			}
			
			return "d";			
		}
	}
	
	/**
	 * Moves robot in positive y-axis (up)
	 * @param room
	 * @param x
	 * @param y
	 * @return N if the movement couldn't be done
	 */
	public static String upMovement(char[][] room, int x, int y) {
		
		if (room[x-1][y] == '0' || room[x-1][y] == '1') {
			
			return "N";
		}
		else if (room[x-1][y] == '#' || room[x-1][y] == '*') {
			
			if (room[x-2][y] == '0' ||room[x-2][y] == '1' || room[x-2][y] == '#' || room[x-2][y] == '*') {
				
				return "N";
				
			} else {
				
				if (room[x-2][y] != objective) {
					
					if ((room[x-3][y] == wall || room[x-3][y] == internalWall) && (room[x-2][y-1] == wall || room[x-2][y-1] == internalWall || room[x-2][y+1] == wall || room[x-2][y+1] == internalWall)) {
						
						return "N";
					}
				}
				
				if (room[x][y] == '+') {
					room[x][y] = '!';
				} else {
					room[x][y] = '-';
				}
				
				if (room[x-1][y] == '*') {
					room[x-1][y] = '+';
				}else {
					room[x-1][y] = '@';
				}
				
				if (room[x-2][y] == '!') {
					room[x-2][y] = '*';	
				}else {
					room[x-2][y] = '#';
				}				
				
				return "A";
			}
			
		} else {
			if (room[x][y] == '+') {
				room[x][y] = '!';
			}else {
				room[x][y] = '-';
			}
			
			if (room[x-1][y] == '!') {
				room[x-1][y] = '+';
			}else {
				room[x-1][y] = '@';
			}
			
			return "a";
		}	
	}
	
	/**
	 * Moves robot in negative y-axis (down)
	 * @param room
	 * @param x
	 * @param y
	 * @return N if the movement couldn't be done
	 */
	public static String downMovement(char[][] room, int x, int y) {
		
		if (room[x+1][y] == '0' || room[x+1][y] == '1') {
			
			return "N";
	
		} else if (room[x+1][y] == '#' || room[x+1][y] == '*') {
			
			if(room[x+2][y] == '0' || room[x+2][y] == '1' || room[x+2][y] == '#' || room[x+2][y] == '*') {
			
				return "N";
			
			} else {
				if(room[x+2][y] != objective) {
					if ((room[x+3][y] == wall || room[x+3][y] == internalWall) && (room[x+2][y-1] == wall || room[x+2][y-1] == internalWall || room[x+2][y+1] == wall || room[x+2][y+1] == internalWall)) {
						
						return "N";
					}
				}
				
				if (room[x][y] == '+') {
					room[x][y] = '!';
				} else {
					room[x][y] = '-';
				}
				
				
				if (room[x+1][y] == '*') {
					room[x+1][y] = '+';
				} else {
					room[x+1][y] = '@';
				}
				
				if (room[x+2][y] == '!') {
					room[x+2][y] = '*';	
				} else {
					room[x+2][y] = '#';
				}				
				
				return "B";
			}
			
		} else {
			if (room[x][y] == '+') {
				room[x][y] = '!';
			} else {
				room[x][y] = '-';
			}
			
			if (room[x+1][y] == '!') {
				room[x+1][y] = '+';
			}else {
				room[x+1][y] = '@';
			}
			
			return "b";
		}
	}
	
	/**
	 * Gets the position (row) of the robot
	 * @param room
	 * @param rows
	 * @param cols
	 * @return row in robot stay
	 */
	public static int getRobotRow(char[][] room, int rows, int cols) {
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				
				if (room[i][j] == robot || room[i][j] == robotAtObjective) {
					
					return i;
				}
			}
		}
		
		return -1;
	}
	
	/**
	 * Gets the position (column) of the robot
	 * @param room
	 * @param rows
	 * @param cols
	 * @return column in robot stay
	 */
	public static int getRobotCol(char[][] room, int rows, int cols) {
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				
				if (room[i][j] == robot || room[i][j] == robotAtObjective) {
					
					return j;
				}
			}
		}
		
		return -1;
	}
	
	/**
	 * Copies an StringBuffer to avoid pass by reference
	 * @param original
	 * @param copied
	 */
	public static void copySB(StringBuilder original, StringBuilder copied) {
		
		for (int i = 0; i < copied.length(); i++) {
			original.append(copied.charAt(i));
		}
	}
	
	/**
	 * Checks if the state passed already exists
	 * @param state
	 * @return true if it already exists, false if not 
	 */
	public static boolean notExist(RoomState state) {
		
		for (int i = 0; i < states.size(); i++) {
			
			if (states.get(i).equals(state)) {
				
				return false;
			}
		}
		
		return true;
	}

}