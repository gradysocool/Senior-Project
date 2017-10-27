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
	private boolean canWhiteCastleKingside;
	private boolean canWhiteCastleQueenside;
	private boolean canBlackCastleKingside;
	private boolean canBlackCastleQueenside;
	public Board(){
		//Set up board squares with pieces in starting position.
		boardSquares = new Square[8][8];
		
		for(int i = 1; i <= 8; i++){
			for(int j = 1; j <= 8; j++){
				boardSquares[i-1][j-1] = new Square(i, j);
			}
			boardSquares[i-1][1].changePiece(new Piece(WHITE, PAWN, boardSquares[i-1][1]));
			boardSquares[i-1][6].changePiece(new Piece(BLACK, PAWN, boardSquares[i-1][6]));
		}
		boardSquares[0][0].changePiece(new Piece(WHITE, ROOK, boardSquares[0][0]));
		boardSquares[0][7].changePiece(new Piece(BLACK, ROOK, boardSquares[0][7]));
		boardSquares[1][0].changePiece(new Piece(WHITE, KNIGHT, boardSquares[1][0]));
		boardSquares[1][7].changePiece(new Piece(BLACK, KNIGHT, boardSquares[1][7]));
		boardSquares[2][0].changePiece(new Piece(WHITE, BISHOP, boardSquares[2][0]));
		boardSquares[2][7].changePiece(new Piece(BLACK, BISHOP, boardSquares[2][7]));
		boardSquares[3][0].changePiece(new Piece(WHITE, QUEEN, boardSquares[3][0]));
		boardSquares[3][7].changePiece(new Piece(BLACK, QUEEN, boardSquares[3][7]));
		boardSquares[4][0].changePiece(new Piece(WHITE, KING, boardSquares[4][0]));
		boardSquares[4][7].changePiece(new Piece(BLACK, KING, boardSquares[4][7]));
		boardSquares[5][0].changePiece(new Piece(WHITE, BISHOP, boardSquares[5][0]));
		boardSquares[5][7].changePiece(new Piece(BLACK, BISHOP, boardSquares[5][7]));
		boardSquares[6][0].changePiece(new Piece(WHITE, KNIGHT, boardSquares[6][0]));
		boardSquares[6][7].changePiece(new Piece(BLACK, KNIGHT, boardSquares[6][7]));
		boardSquares[7][0].changePiece(new Piece(WHITE, ROOK, boardSquares[7][0]));
		boardSquares[7][7].changePiece(new Piece(BLACK, ROOK, boardSquares[7][7]));
		kingSquareWhite = boardSquares[4][0]; 
		kingSquareBlack = boardSquares[4][7]; 
		enPessantFile = 0;
		canWhiteCastleKingside = true;
		canWhiteCastleQueenside = true;
		canBlackCastleKingside = true;
		canBlackCastleQueenside = true;
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
			switch(p.getType()){
			
			case EMPTY:
				break;
			case KING:
				//Some more queenside castling checks.
				if(s.getFile()==3&&p.getSquare().getFile()==5){
					if(boardSquares[1][p.getColor()?0:7].getPiece().getType()==EMPTY
							&&boardSquares[2][p.getColor()?0:7].getPiece().getType()==EMPTY
							&&boardSquares[3][p.getColor()?0:7].getPiece().getType()==EMPTY){
						if(!isInCheck(p.getColor())){
							//Move the king over one square to test check.
							p.getSquare().changePiece(new Piece(true, EMPTY, p.getSquare()));
							boardSquares[3][p.getColor()?0:7].changePiece(p);
							p.changeSquare(boardSquares[3][p.getColor()?0:7]);
							if(!isInCheck(p.getColor())){
								//Move the king over another square to test check.
								p.getSquare().changePiece(new Piece(true, EMPTY, p.getSquare()));
								boardSquares[2][p.getColor()?0:7].changePiece(p);
								p.changeSquare(boardSquares[2][p.getColor()?0:7]);
								if(!isInCheck(p.getColor())){
									//Finally it has passed the necessary requirements.
									ary.add(s);
								}
							}
							//Put king back.
							p.getSquare().changePiece(new Piece(true, EMPTY, p.getSquare()));
							boardSquares[4][p.getColor()?0:7].changePiece(p);
							p.changeSquare(boardSquares[4][p.getColor()?0:7]);
						}
					}
						
				}
				//Some more kingside castling checks.
				else if(s.getFile()==7&&p.getSquare().getFile()==5){
					if(boardSquares[5][p.getColor()?0:7].getPiece().getType()==EMPTY
							&&boardSquares[6][p.getColor()?0:7].getPiece().getType()==EMPTY){
						if(!isInCheck(p.getColor())){
							//Move the king over one square to test check.
							p.getSquare().changePiece(new Piece(true, EMPTY, p.getSquare()));
							boardSquares[5][p.getColor()?0:7].changePiece(p);
							p.changeSquare(boardSquares[5][p.getColor()?0:7]);
							if(!isInCheck(p.getColor())){
								//Move the king over another square to test check.
								p.getSquare().changePiece(new Piece(true, EMPTY, p.getSquare()));
								boardSquares[6][p.getColor()?0:7].changePiece(p);
								p.changeSquare(boardSquares[6][p.getColor()?0:7]);
								if(!isInCheck(p.getColor())){
									//Finally it has passed the necessary requirements.
									ary.add(s);
								}
							}
							//Put king back.
							p.getSquare().changePiece(new Piece(true, EMPTY, p.getSquare()));
							boardSquares[4][p.getColor()?0:7].changePiece(p);
							p.changeSquare(boardSquares[4][p.getColor()?0:7]);
						}
					}
				}
				//Simple king moves.
				else{
					if(s.getPiece().getColor()!=p.getColor()||s.getPiece().getType()==EMPTY){
						ary.add(s);
					}
				}
				break;
			default:
				//For other pieces, check if there are empty squares in between
				//and that the destination square is empty or the opponent's piece.
				if(checkSquaresBetween(p.getSquare(), s)){
					if(s.getPiece().getColor()!=p.getColor()||s.getPiece().getType()==EMPTY){
						ary.add(s);
					}
				}
				break;
			}
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
	public boolean checkSquaresBetween(Square a, Square b){
		if(a.toString().equals(b.toString())){
			return true;
		}
		if(a.getFile()==b.getFile()){
			int start;
			int end;
			if(a.getRank()<b.getRank()){
				start = a.getRank();
				end = b.getRank();
			}
			else{
				start = b.getRank();
				end = a.getRank();
			}
			for(int i=start+1;i<end;i++){
				if(boardSquares[a.getFile()-1][i-1].getPiece().getType()!=EMPTY){
					return false;
				}
			}
		}
		else if(a.getRank()==b.getRank()){
			int start;
			int end;
			if(a.getFile()<b.getFile()){
				start = a.getFile();
				end = b.getFile();
			}
			else{
				start = b.getFile();
				end = a.getFile();
			}
			for(int i=start+1;i<end;i++){
				if(boardSquares[i-1][a.getRank()-1].getPiece().getType()!=EMPTY){
					return false;
				}
			}
		}
		else if(a.getFile()+a.getRank()==b.getFile()+b.getRank()){
			int start;
			int end;
			int diff;
			if(a.getFile()<b.getFile()){
				start = a.getFile();
				end = b.getFile();
				diff = a.getRank()+a.getFile();
			}
			else{
				start = b.getFile();
				end = a.getFile();
				diff = b.getRank()+b.getFile();
			}
			for(int i=start+1;i<end;i++){
				if(boardSquares[i-1][diff-i-1].getPiece().getType()!=EMPTY){
					return false;
				}
			}
		}
		else if(a.getFile()-a.getRank()==b.getFile()-b.getRank()){
			int start;
			int end;
			int diff;
			if(a.getFile()<b.getFile()){
				start = a.getFile();
				end = b.getFile();
				diff = a.getRank()-a.getFile();
			}
			else{
				start = b.getFile();
				end = a.getFile();
				diff = b.getRank()-b.getFile();
			}
			for(int i=start+1;i<end;i++){
				if(boardSquares[i-1][diff+i-1].getPiece().getType()!=EMPTY){
					return false;
				}
			}
		}
		return true;
	}
	public int getEnPessantFile(){
		return enPessantFile;
	}
	public boolean getCastleKingside(boolean color){
		
		return color ? canWhiteCastleKingside : canBlackCastleKingside;
	}
	public boolean getCastleQueenside(boolean color){
		
		return color ? canWhiteCastleQueenside : canBlackCastleQueenside;
	}

}
