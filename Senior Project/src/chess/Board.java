package chess;

import java.util.ArrayList;

public class Board {

	public static final boolean WHITE = true;
	public static final boolean BLACK = false;

	public static final int EMPTY = 0;
	public static final int KING = 1;
	public static final int QUEEN = 2;
	public static final int ROOK = 3;
	public static final int BISHOP = 4;
	public static final int KNIGHT = 5;
	public static final int PAWN = 6;
	private Square[][] boardSquares;
	private Square kingSquareWhite;
	private Square kingSquareBlack;
	private int enPessantFile;
	public Board(){
		//Set up board squares with pieces in starting position.
		boardSquares = new Square[8][8];
		
		for(int i = 1; i <= 8; i++){
			for(int j = 1; j <= 8; j++){
				boardSquares[i-1][j-1] = new Square(i, j);
			}
			boardSquares[i][1].changePiece(new Piece(WHITE, PAWN, boardSquares[i][1]));
			boardSquares[i][6].changePiece(new Piece(BLACK, PAWN, boardSquares[i][6]));
		}
		boardSquares[0][0].changePiece(new Piece(WHITE, ROOK, boardSquares[0][0]));
		boardSquares[7][0].changePiece(new Piece(BLACK, ROOK, boardSquares[7][0]));
		boardSquares[0][1].changePiece(new Piece(WHITE, KNIGHT, boardSquares[0][1]));
		boardSquares[7][1].changePiece(new Piece(BLACK, KNIGHT, boardSquares[7][1]));
		boardSquares[0][2].changePiece(new Piece(WHITE, BISHOP, boardSquares[0][2]));
		boardSquares[7][2].changePiece(new Piece(BLACK, BISHOP, boardSquares[7][2]));
		boardSquares[0][3].changePiece(new Piece(WHITE, QUEEN, boardSquares[0][3]));
		boardSquares[7][3].changePiece(new Piece(BLACK, QUEEN, boardSquares[7][3]));
		boardSquares[0][4].changePiece(new Piece(WHITE, KING, boardSquares[0][4]));
		boardSquares[7][4].changePiece(new Piece(BLACK, KING, boardSquares[7][4]));
		boardSquares[0][5].changePiece(new Piece(WHITE, BISHOP, boardSquares[0][5]));
		boardSquares[7][5].changePiece(new Piece(BLACK, BISHOP, boardSquares[7][5]));
		boardSquares[0][6].changePiece(new Piece(WHITE, KNIGHT, boardSquares[0][6]));
		boardSquares[7][6].changePiece(new Piece(BLACK, KNIGHT, boardSquares[7][6]));
		boardSquares[0][7].changePiece(new Piece(WHITE, ROOK, boardSquares[0][7]));
		boardSquares[7][7].changePiece(new Piece(BLACK, ROOK, boardSquares[7][7]));
		kingSquareWhite = boardSquares[0][4]; 
		kingSquareBlack = boardSquares[7][4]; 
		enPessantFile = 0;
	}
	
	
	/**
	 * 
	 * @param color Color of king we are concerned about.
	 * @return //If the piece can legally move to the king's square (disregarding their own king), return true, else false.
	 */
	public boolean isInCheck(boolean color){
		
		for(Piece p: getPieces(!color)){	
			//Check all available moves of the piece on this square disregarding check to other king.
			for(Square t: legalMoveSquaresWithoutCheck(p)){
				//If square is the same square as our king return true
				if(color){						
					if(t.toString().equals(kingSquareWhite.toString())){
						return true;
					}
				}
				else{
					if(t.toString().equals(kingSquareBlack.toString())){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean isValidMove(Piece p, Square finish){
		for(Square s: legalMoveSquares(p)){
			if (s.toString().equals(finish.toString())){
				return true;
			}
		}
		return false;
	}
	
	public Square[][] getBoardSquares(){
		return boardSquares;
	}
	public ArrayList<Piece> getPieces(boolean color){
		ArrayList<Piece> ary = new ArrayList<Piece>();
		for(Square[] sq: boardSquares){
			for(Square s: sq){
				if(s.getPiece().getType()!=0&& s.getPiece().getColor() == color){
					ary.add(s.getPiece());
				}
			}
		}
		return ary;
	}
	public ArrayList<Square> legalMoveSquaresWithoutCheck(Piece p){
		ArrayList<Square> ary = new ArrayList<Square>();
		for(Square s: p.availableMoveSquares(this)){
			//To Be Implemented
		}
		return ary;
	}
	public ArrayList<Square> legalMoveSquares(Piece p){
		ArrayList<Square> ary = new ArrayList<Square>();
		
		for(Square s: legalMoveSquaresWithoutCheck(p)){
			Piece currentSquarePrevious = p;
			Piece otherSquarePrevious = s.getPiece();
			p.getSquare().changePiece(new Piece(true, EMPTY, p.getSquare()));
			s.changePiece(currentSquarePrevious);
			if(!isInCheck(currentSquarePrevious.getColor())){
				ary.add(s);
			}
			p.getSquare().changePiece(currentSquarePrevious);
			s.changePiece(otherSquarePrevious);
		}
		return ary;
	}
	public int getEnPessantFile(){
		return enPessantFile;
	}

}
