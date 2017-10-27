package chess;

import java.util.ArrayList;

public class Piece {
	public static final boolean WHITE = true;
	public static final boolean BLACK = false;

	public static final int EMPTY = 0;
	public static final int KING = 1;
	public static final int QUEEN = 2;
	public static final int ROOK = 3;
	public static final int BISHOP = 4;
	public static final int KNIGHT = 5;
	public static final int PAWN = 6;
	
	private boolean color;
	private int type;
	private Square square;
	/**Constructor for Piece class.
	 * 
	 * @param color Ownership of Piece. 
	 * @param type Type of Piece. 
	 */
	public Piece(boolean color, int type, Square s){
		this.color = color;
		this.type = type;
		square = s;
	}
	
	public boolean getColor(){
		return color;
	}
	public int getType(){
		return type;
	}
	public Square getSquare(){
		return square;
	}
	public void changeSquare(Square s){
		square = s;
	}
	public ArrayList<Square> availableMoveSquares(Board b){
		ArrayList<Square> ary = new ArrayList<Square>();
		for(Square[] sq :  b.getBoardSquares()){
			for(Square s : sq){
				switch (type){
				case EMPTY:
					break;
				case KING:
					if(Math.abs(s.getFile()-square.getFile())<=1&&Math.abs(s.getRank()-square.getRank())<=1){
						if(!s.equals(square)){
							ary.add(s);
						}
					}
					if(b.getCastleKingside(color)){
						if(s.getFile()==7&&s.getRank()==square.getRank()){
							ary.add(s);
						}
					}
					if(b.getCastleQueenside(color)){
						if(s.getFile()==3&&s.getRank()==square.getRank()){
							ary.add(s);
						}
					}
					break;
				case QUEEN:
					if(s.getFile() == square.getFile() 
					|| s.getRank() == square.getRank() 
					|| s.getFile()+s.getRank()==square.getFile()+square.getRank()
					|| s.getFile()-s.getRank()==square.getFile()-square.getRank()){
						if(!s.equals(square)){
							ary.add(s);
						}
					}
					break;
				case ROOK:
					if(s.getFile() == square.getFile() 
					|| s.getRank() == square.getRank()){
						if(!s.equals(square)){
							ary.add(s);
						}
					}
					break;
				case BISHOP:
					if(s.getFile()+s.getRank()==square.getFile()+square.getRank()
					|| s.getFile()-s.getRank()==square.getFile()-square.getRank()){
						if(!s.equals(square)){
							ary.add(s);
						}
					}
					break;
				case KNIGHT:
					if(((Math.abs(s.getFile() - square.getFile())==2
					&&Math.abs(s.getRank() - square.getRank())==1)
					|| (Math.abs(s.getFile() - square.getFile())==1
					&&Math.abs(s.getRank() - square.getRank())==2))){
						if(!s.equals(square)){
							ary.add(s);
						}
					}
					break;
				case PAWN:
					if(color==WHITE){
						
						if(s.getFile() == square.getFile() 
						&& s.getRank() == 4 && square.getRank() == 2){
							ary.add(s);
						}
						if(s.getRank() - square.getRank()==1){
							if(s.getFile() == square.getFile()){
								ary.add(s);
							}
							if(Math.abs(s.getFile() - square.getFile())==1){
								if(s.getPiece().getColor()!=color && s.getPiece().getType() != 0){
									ary.add(s);
								}
								if(b.getEnPessantFile()==s.getFile()){
									ary.add(s);
								}
							}
						}
					}
					else{
						if(s.getFile() == square.getFile() 
								&& s.getRank() == 5 && square.getRank() == 7){
									ary.add(s);
								}
								if(s.getRank() - square.getRank()==-1){
									if(s.getFile() == square.getFile()){
										ary.add(s);
									}
									if(Math.abs(s.getFile() - square.getFile())==1){
										if(s.getPiece().getColor()!=color && s.getPiece().getType() != 0){
											ary.add(s);
										}
										if(b.getEnPessantFile()==s.getFile()){
											ary.add(s);
										}
									}
								}
					}
					break;
				}
				
			}
		}
		return ary;
	}
	
}

