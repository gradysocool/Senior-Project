package chess;

public class Square {
	private int file;
	private int rank;
	private Piece pieceOnSquare;
	/**
	 * 
	 * @param file File of square
	 * @param rank Rank of square
	 * @param p Piece on square
	 */
	public Square(int file, int rank, Piece p){
		this.file = file;
		this.rank = rank;
		pieceOnSquare = p;
	}
	public Square(int file, int rank){
		this.file = file;
		this.rank = rank;
		pieceOnSquare = new Piece(true, 0);
	}
	public void changePiece(Piece p){
		pieceOnSquare = p;
	}
	public Square[] availableMoveSquares(Board b){
		Square[] ary = new Square[64];
		for(Square[] sq :  b.getBoardSquares()){
			for(Square s : sq){
				switch (s.getPiece().getType()){
				//To Be Implemented
				}
			}
		}
		return ary;
	}
	public Square[] legalMoveSquaresWithoutCheck(Board b){
		Square[] ary = new Square[64];
		for(Square s: availableMoveSquares(b)){
			//To Be Implemented
		}
		return ary;
	}
	public Square[] legalMoveSquares(Board b){
		Square[] ary = new Square[64];
		int i = 0;
		for(Square s: legalMoveSquaresWithoutCheck(b)){
			Piece currentSquarePrevious = pieceOnSquare;
			Piece otherSquarePrevious = s.getPiece();
			changePiece(new Piece(true, 0));
			s.changePiece(currentSquarePrevious);
			if(!b.isInCheck(currentSquarePrevious.getColor())){
				ary[i]=s;
				i++;
			}
			changePiece(currentSquarePrevious);
			s.changePiece(otherSquarePrevious);
		}
		return ary;
	}
	public Piece getPiece(){
		return pieceOnSquare;
	}
	public String toString(){
		String s = "";
		s += (char)((int)'a'+ file - 1);
		s += rank;
		return s;
	}
}
