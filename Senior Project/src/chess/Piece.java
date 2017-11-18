package chess;

import java.util.ArrayList;
import java.util.HashSet;

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

	/**
	 * Constructor for Piece class.
	 * 
	 * @param color
	 *            Ownership of Piece.
	 * @param type
	 *            Type of Piece.
	 */
	public Piece(boolean color, int type, Square s) {
		this.color = color;
		this.type = type;
		square = s;
	}

	public boolean getColor() {
		return color;
	}

	public int getType() {
		return type;
	}

	public Square getSquare() {
		return square;
	}

	public void changeSquare(Square s) {
		square = s;
	}
	public HashSet<Square> hashAvailableMoveSquares(Board b){
		HashSet<Square> ary = new HashSet<Square>(availableMoveSquares(b));
		return ary;
	}
	public ArrayList<Square> availableMoveSquares(Board b) {

		ArrayList<Square> ary = new ArrayList<Square>();
		int file = square.getFile();
		int rank = square.getRank();
		switch (type) {
		case EMPTY:
			break;
		case KING:
			for (int i = file - 1; i <= file + 1; i++) {
				for (int j = rank - 1; j <= rank + 1; j++) {
					if (i > 0 && i < 9 && j > 0 && j < 9
							&& (i != file || j != rank)) {
						ary.add(b.squareAt(i, j));
					}
				}
			}
			if (b.getCastleKingside(color)) {
				ary.add(b.squareAt(7, color?1:8));
			}
			if (b.getCastleQueenside(color)) {
				ary.add(b.squareAt(3, color?1:8));
			}
			break;
		case QUEEN:
			for (int i = 1; i <= 8; i++) {
				if (i != file) {
					ary.add(b.squareAt(i, rank));
					if (rank + file - i > 0 && rank + file - i < 9) {
						ary.add(b.squareAt(i, rank + file - i));
					}
					if (rank - file + i > 0 && rank - file + i < 9) {
						ary.add(b.squareAt(i, rank - file + i));
					}
				}
				if (i != rank) {
					ary.add(b.squareAt(file, i));
				}
			}
			break;
		case ROOK:
			for (int i = 1; i <= 8; i++) {
				if (i != file) {
					ary.add(b.squareAt(i, rank));
				}
				if (i != rank) {
					ary.add(b.squareAt(file, i));
				}
			}
			break;
		case BISHOP:
			for (int i = 1; i <= 8; i++) {
				if (i != file) {
					if (rank + file - i > 0 && rank + file - i < 9) {
						ary.add(b.squareAt(i, rank + file - i));
					}
					if (rank - file + i > 0 && rank - file + i < 9) {
						ary.add(b.squareAt(i, rank - file + i));
					}
				}
			}
			break;
		case KNIGHT:
			if(file-2>0&&rank+1<9){
			ary.add(b.squareAt(file-2, rank+1));
			}
			if(file+2<9&&rank+1<9){
			ary.add(b.squareAt(file+2, rank+1));
			}
			if(file-2>0&&rank-1>0){
			ary.add(b.squareAt(file-2, rank-1));
			}
			if(file+2<9&&rank-1>0){
			ary.add(b.squareAt(file+2, rank-1));
			}
			if(file-1>0&&rank+2<9){
			ary.add(b.squareAt(file-1, rank+2));
			}
			if(file+1<9&&rank+2<9){
			ary.add(b.squareAt(file+1, rank+2));
			}
			if(file-1>0&&rank-2>0){
			ary.add(b.squareAt(file-1, rank-2));
			}
			if(file+1<9&&rank-2>0){
			ary.add(b.squareAt(file+1, rank-2));
			}
			break;
		case PAWN:
			if (color == WHITE) {
				if (rank == 2
						&& b.squareAt(file, 4).getPiece().getType() == EMPTY) {
					ary.add(b.squareAt(file, 4));
				}
				if (b.squareAt(file, rank + 1).getPiece().getType() == EMPTY) {
					ary.add(b.squareAt(file, rank + 1));
				}
				if (file != 1) {
					if (b.squareAt(file - 1, rank + 1).getPiece().getColor() != color
							&& b.squareAt(file - 1, rank + 1).getPiece()
									.getType() != EMPTY) {
						ary.add(b.squareAt(file - 1, rank + 1));
					} else if (b.getEnPessantFile() == file - 1 && rank == 5) {
						ary.add(b.squareAt(file - 1, rank + 1));
					}
				}
				if (file != 8) {
					if (b.squareAt(file + 1, rank + 1).getPiece().getColor() != color
							&& b.squareAt(file + 1, rank + 1).getPiece()
									.getType() != EMPTY) {
						ary.add(b.squareAt(file + 1, rank + 1));
					} else if (b.getEnPessantFile() == file + 1 && rank == 5) {
						ary.add(b.squareAt(file + 1, rank + 1));
					}
				}
			} else {
				if (rank == 7
						&& b.squareAt(file, 5).getPiece().getType() == EMPTY) {
					ary.add(b.squareAt(file, 5));
				}
				if (b.squareAt(file, rank - 1).getPiece().getType() == EMPTY) {
					ary.add(b.squareAt(file, rank - 1));
				}
				if (file != 1) {
					if (b.squareAt(file - 1, rank - 1).getPiece().getColor() != color
							&& b.squareAt(file - 1, rank - 1).getPiece()
									.getType() != EMPTY) {
						ary.add(b.squareAt(file - 1, rank - 1));
					} else if (b.getEnPessantFile() == file - 1 && rank == 4) {
						ary.add(b.squareAt(file - 1, rank - 1));
					}
				}
				if (file != 8) {
					if (b.squareAt(file + 1, rank - 1).getPiece().getColor() != color
							&& b.squareAt(file + 1, rank - 1).getPiece()
									.getType() != EMPTY) {
						ary.add(b.squareAt(file + 1, rank - 1));
					} else if (b.getEnPessantFile() == file + 1 && rank == 4) {
						ary.add(b.squareAt(file + 1, rank - 1));
					}
				}
			}
			break;
		}
		return ary;
	}
}
