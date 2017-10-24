package chess;

public class Board {
	Square[][] boardSquares;
	public Board(){
		//Set up board squares with pieces in starting position.
		boardSquares = new Square[8][8];
		for(int i = 1; i <= 8; i++){
			for(int j = 1; j <= 8; j++){
				boardSquares[i-1][j-1] = new Square(i, j);
			}
			boardSquares[i][1].changePiece(new Piece(true, 6));
			boardSquares[i][6].changePiece(new Piece(false, 6));
		}
		boardSquares[0][0].changePiece(new Piece(true, 3));
		boardSquares[7][0].changePiece(new Piece(false, 3));
		boardSquares[0][1].changePiece(new Piece(true, 5));
		boardSquares[7][1].changePiece(new Piece(false, 5));
		boardSquares[0][2].changePiece(new Piece(true, 4));
		boardSquares[7][2].changePiece(new Piece(false, 4));
		boardSquares[0][3].changePiece(new Piece(true, 2));
		boardSquares[7][3].changePiece(new Piece(false, 2));
		boardSquares[0][4].changePiece(new Piece(true, 1));
		boardSquares[7][4].changePiece(new Piece(false, 1));
		boardSquares[0][5].changePiece(new Piece(true, 4));
		boardSquares[7][5].changePiece(new Piece(false, 4));
		boardSquares[0][6].changePiece(new Piece(true, 5));
		boardSquares[7][6].changePiece(new Piece(false, 5));
		boardSquares[0][7].changePiece(new Piece(true, 3));
		boardSquares[7][7].changePiece(new Piece(false, 3));
		
	}
	
	/**
	 * 
	 * @param color Color of king
	 * @return Square at king's location.
	 */
	public Square kingSquare(boolean color){
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				Piece p = boardSquares[i][j].getPiece();
				if(p.getType() == 1 && p.getColor()==color){
					return boardSquares[i][j];
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param color Color of king we are concerned about.
	 * @return //If the piece can legally move to the king's square (disregarding their own king), return true, else false.
	 */
	public boolean isInCheck(boolean color){
		
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				Square s = boardSquares[i][j];
				//If square has a piece of a different color to our king
				if(s.getPiece().getColor()!=color && s.getPiece().getType()!=0){
					//Check all available moves of the piece on this square disregarding check to other king.
					for(Square t: s.legalMoveSquaresWithoutCheck(this)){
							//If square is the same square as our king return true
							if(t.toString().equals(kingSquare(color).toString())){
								return true;
							}
						}
					}
			}
		}
		return false;
	}
	public boolean isValidMove(Square start, Square finish){
		for(Square s: start.legalMoveSquares(this)){
			if (s.toString().equals(finish.toString())){
				return true;
			}
		}
		return false;
	}
	
	public Square[][] getBoardSquares(){
		return boardSquares;
	}
}
