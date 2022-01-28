package moviendoCajas;

/**
 * 
 * @author Diego Alcoba Arias
 *
 */

public class RoomState {

	private StringBuilder path;
	private boolean checked;
	private final char[][] room;
	
	/**
	 * Constructor
	 * @param room
	 * @param path
	 */
	public RoomState(char[][] room, StringBuilder path) {
		this.path = path;
		this.room = room;
		this.checked = false;
	}
	
	/**
	 * @return the room
	 */
	public char[][] getRoom() {
		return this.room;
	}
	
	/**
	 * @return if this state has been checked
	 */
	public boolean checked() {
		return this.checked;
	}
	
	/**
	 * @return the path
	 */
	public StringBuilder getPath() {
		return this.path;
	}
	
	/**
	 * Confirms if state already checked
	 */
	public void check() {
		this.checked = true;
	}
	
	/**
	 * Compares the elements of the matrix
	 * @param state
	 */
	public boolean equals(RoomState state) {
		
		for (int i = 0; i < room.length; i++) {
			for (int j = 0; j < room[0].length; j++) {
				
				if (room[i][j] != state.getRoom()[i][j]) {
					
					return false;
				}
			}
		}
		
		return true;
	}	
}