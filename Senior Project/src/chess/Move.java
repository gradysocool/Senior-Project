package chess;

public class Move {
	private Piece p;
	private Square finish;
	private int promoteType;
	public Move(Piece p, Square finish){
		this.p = p;
		this.finish = finish;
		promoteType = 0;
	}
	public Move(Piece p, Square finish, int i){
		this.p = p;
		this.finish = finish;
		promoteType = i;
	}
	public Piece getPiece(){
		return p;
	}
	public Square getMoveSquare(){
		return finish;
	}
	public int promoteType(){
		return promoteType;
	}
	
	public String toString(){
		return "";
	}
}
