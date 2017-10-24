package chess;

public class Piece {
	private boolean color;
	private int type;
	/**Constructor for Piece class.
	 * 
	 * @param color Ownership of Piece. White = True, Black = False.
	 * @param type Type of Piece. 0 = none, 1 = King, 2 = Queen, 3 = Rook, 4 = Bishop, 5 = Knight, 6 = Pawn
	 */
	public Piece(boolean color, int type){
		this.color = color;
		this.type = type;
	}
	
	public boolean getColor(){
		return color;
	}
	public int getType(){
		return type;
	}
}
