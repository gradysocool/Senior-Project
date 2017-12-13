package chess;

import java.util.ArrayList;
import java.util.Collections;

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
	private boolean turn;
	private int gameInProgress;
	private boolean realBoard;

	public Board() {
		// Set up board squares with pieces in starting position.
		boardSquares = new Square[8][8];

		for (int i = 1; i <= 8; i++) {
			for (int j = 1; j <= 8; j++) {
				boardSquares[i - 1][j - 1] = new Square(i, j);
			}
			boardSquares[i - 1][1].changePiece(new Piece(WHITE, PAWN,
					boardSquares[i - 1][1]));
			boardSquares[i - 1][6].changePiece(new Piece(BLACK, PAWN,
					boardSquares[i - 1][6]));
		}
		boardSquares[0][0].changePiece(new Piece(WHITE, ROOK,
				boardSquares[0][0]));
		boardSquares[0][7].changePiece(new Piece(BLACK, ROOK,
				boardSquares[0][7]));
		boardSquares[1][0].changePiece(new Piece(WHITE, KNIGHT,
				boardSquares[1][0]));
		boardSquares[1][7].changePiece(new Piece(BLACK, KNIGHT,
				boardSquares[1][7]));
		boardSquares[2][0].changePiece(new Piece(WHITE, BISHOP,
				boardSquares[2][0]));
		boardSquares[2][7].changePiece(new Piece(BLACK, BISHOP,
				boardSquares[2][7]));
		boardSquares[3][0].changePiece(new Piece(WHITE, QUEEN,
				boardSquares[3][0]));
		boardSquares[3][7].changePiece(new Piece(BLACK, QUEEN,
				boardSquares[3][7]));
		boardSquares[4][0].changePiece(new Piece(WHITE, KING,
				boardSquares[4][0]));
		boardSquares[4][7].changePiece(new Piece(BLACK, KING,
				boardSquares[4][7]));
		boardSquares[5][0].changePiece(new Piece(WHITE, BISHOP,
				boardSquares[5][0]));
		boardSquares[5][7].changePiece(new Piece(BLACK, BISHOP,
				boardSquares[5][7]));
		boardSquares[6][0].changePiece(new Piece(WHITE, KNIGHT,
				boardSquares[6][0]));
		boardSquares[6][7].changePiece(new Piece(BLACK, KNIGHT,
				boardSquares[6][7]));
		boardSquares[7][0].changePiece(new Piece(WHITE, ROOK,
				boardSquares[7][0]));
		boardSquares[7][7].changePiece(new Piece(BLACK, ROOK,
				boardSquares[7][7]));

		kingSquareWhite = boardSquares[4][0];
		kingSquareBlack = boardSquares[4][7];
		enPessantFile = 0;
		canWhiteCastleKingside = true;
		canWhiteCastleQueenside = true;
		canBlackCastleKingside = true;
		canBlackCastleQueenside = true;
		turn = WHITE;
		gameInProgress = 2;
		realBoard = true;
	}

	public Board(Square[][] bs, Square ksw, Square ksb, int epf, boolean cwck,
			boolean cbck, boolean cwcq, boolean cbcq, boolean t, int gip) {
		boardSquares = new Square[8][8];
		for (int i = 1; i <= 8; i++) {
			for (int j = 1; j <= 8; j++) {
				Piece op = bs[i - 1][j - 1].getPiece();
				boardSquares[i - 1][j - 1] = new Square(i, j);
				boardSquares[i - 1][j - 1].changePiece(new Piece(op.getColor(),
						op.getType(), boardSquares[i - 1][j - 1]));

			}
		}
		kingSquareWhite = boardSquares[ksw.getFile()-1][ksw.getRank()-1];
		kingSquareBlack = boardSquares[ksb.getFile()-1][ksb.getRank()-1];
		enPessantFile = epf;
		canWhiteCastleKingside = cwck;
		canWhiteCastleQueenside = cwcq;
		canBlackCastleKingside = cbck;
		canBlackCastleQueenside = cbcq;
		turn = t;
		gameInProgress = gip;
		realBoard = false;
	}

	/**
	 * 
	 * @param color
	 *            Color of king we are concerned about.
	 * @return //If the piece can legally move to the king's square
	 *         (disregarding their own king), return true, else false.
	 */
	public boolean isInCheck(boolean color) {

		for (Piece p : getPieces(!color)) {
			// Check all available moves of the piece on this square
			// disregarding check to other king.

			// If square is the same square as our king return true
			if (color) {
				if (p.availableMoveSquares(this).contains(kingSquareWhite)) {
					if (checkSquaresBetween(p.getSquare(), kingSquareWhite)) {
						if (p.getType() != 1
								|| (p.getSquare().getFile()
										- kingSquareWhite.getFile() < 2 && p
										.getSquare().getFile()
										- kingSquareWhite.getFile() > -2)) {
							return true;
						}
					}
				}
			} else {
				if (p.availableMoveSquares(this).contains(kingSquareBlack)) {
					if (checkSquaresBetween(p.getSquare(), kingSquareBlack)) {
						if (p.getType() != 1
								|| (p.getSquare().getFile()
										- kingSquareBlack.getFile() < 2 && p
										.getSquare().getFile()
										- kingSquareBlack.getFile() > -2)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public boolean isLegalWithoutCheck(Move m) {
		Piece p = m.getPiece();
		Square finish = m.getMoveSquare();
		for (Square s : p.availableMoveSquares(this)) {
			if (s.sameSquare(finish)) {
				switch (p.getType()) {

				case EMPTY:
					break;
				case KING:
					boolean isValid = false;
					// Some more queenside castling checks.
					if (s.getFile() == 3 && p.getSquare().getFile() == 5) {

						if (boardSquares[1][p.getColor() ? 0 : 7].getPiece()
								.getType() == EMPTY
								&& boardSquares[2][p.getColor() ? 0 : 7]
										.getPiece().getType() == EMPTY
								&& boardSquares[3][p.getColor() ? 0 : 7]
										.getPiece().getType() == EMPTY) {
							if (!isInCheck(p.getColor())) {
								// Move the king over one square to test check.
								p.getSquare().changePiece(
										new Piece(true, EMPTY, p.getSquare()));
								boardSquares[3][p.getColor() ? 0 : 7]
										.changePiece(p);
								p.changeSquare(boardSquares[3][p.getColor() ? 0
										: 7]);
								if (!isInCheck(p.getColor())) {
									// Move the king over another square to test
									// check.
									p.getSquare().changePiece(
											new Piece(true, EMPTY, p
													.getSquare()));
									boardSquares[2][p.getColor() ? 0 : 7]
											.changePiece(p);
									p.changeSquare(boardSquares[2][p.getColor() ? 0
											: 7]);
									if (!isInCheck(p.getColor())) {
										// Finally it has passed the necessary
										// requirements.
										isValid = true;
									}
								}
								// Put king back.
								p.getSquare().changePiece(
										new Piece(true, EMPTY, p.getSquare()));
								boardSquares[4][p.getColor() ? 0 : 7]
										.changePiece(p);
								p.changeSquare(boardSquares[4][p.getColor() ? 0
										: 7]);
								if (isValid) {
									return true;
								}
							}
						}

					}
					// Some more kingside castling checks.
					else if (s.getFile() == 7 && p.getSquare().getFile() == 5) {
						if (boardSquares[5][p.getColor() ? 0 : 7].getPiece()
								.getType() == EMPTY
								&& boardSquares[6][p.getColor() ? 0 : 7]
										.getPiece().getType() == EMPTY) {
							if (!isInCheck(p.getColor())) {
								// Move the king over one square to test check.
								p.getSquare().changePiece(
										new Piece(true, EMPTY, p.getSquare()));
								boardSquares[5][p.getColor() ? 0 : 7]
										.changePiece(p);
								p.changeSquare(boardSquares[5][p.getColor() ? 0
										: 7]);
								if (!isInCheck(p.getColor())) {
									// Move the king over another square to test
									// check.
									p.getSquare().changePiece(
											new Piece(true, EMPTY, p
													.getSquare()));
									boardSquares[6][p.getColor() ? 0 : 7]
											.changePiece(p);
									p.changeSquare(boardSquares[6][p.getColor() ? 0
											: 7]);
									if (!isInCheck(p.getColor())) {
										// Finally it has passed the necessary
										// requirements.
										isValid = true;
									}
								}
								// Put king back.
								p.getSquare().changePiece(
										new Piece(true, EMPTY, p.getSquare()));
								boardSquares[4][p.getColor() ? 0 : 7]
										.changePiece(p);
								p.changeSquare(boardSquares[4][p.getColor() ? 0
										: 7]);
								if (isValid) {
									return true;
								}
							}
						}
					}
					// Simple king moves.
					else {
						if (s.getPiece().getColor() != p.getColor()
								|| s.getPiece().getType() == EMPTY) {
							return true;
						}
					}
					break;
				default:
					// For other pieces, check if there are empty squares in
					// between
					// and that the destination square is empty or the
					// opponent's
					// piece.
					if (checkSquaresBetween(p.getSquare(), s)) {
						if (s.getPiece().getColor() != p.getColor()
								|| s.getPiece().getType() == EMPTY) {
							return true;
						}
					}
					break;
				}
			}
		}

		return false;
	}

	public boolean isLegalMove(Move m) {
		if (isLegalWithoutCheck(m)) {
			Piece p = m.getPiece();
			Square s = m.getMoveSquare();
			boolean isLegal = false;
			boolean enPessant = false;
			if (p.getType() == PAWN && p.getSquare().getFile() != s.getFile()
					&& s.getPiece().getType() == EMPTY) {
				enPessant = true;
				if (p.getColor()) {
					boardSquares[s.getFile() - 1][4].changePiece(new Piece(
							true, EMPTY, boardSquares[s.getFile() - 1][4]));
				} else {
					boardSquares[s.getFile() - 1][3].changePiece(new Piece(
							true, EMPTY, boardSquares[s.getFile() - 1][3]));
				}
			}
			Piece currentSquarePrevious = p;
			Piece otherSquarePrevious = s.getPiece();
			Square previousSquare = p.getSquare();
			p.getSquare().changePiece(new Piece(true, EMPTY, p.getSquare()));
			p.changeSquare(s);
			s.changePiece(currentSquarePrevious);
			if (p.getType() == KING) {
				if (p.getColor()) {
					kingSquareWhite = p.getSquare();
				} else {
					kingSquareBlack = p.getSquare();
				}
			}
			if (!isInCheck(currentSquarePrevious.getColor())) {
				isLegal = true;
			}
			previousSquare.changePiece(currentSquarePrevious);
			s.changePiece(otherSquarePrevious);
			p.changeSquare(previousSquare);
			if (enPessant) {
				if (p.getColor()) {
					boardSquares[s.getFile() - 1][4].changePiece(new Piece(
							BLACK, PAWN, boardSquares[s.getFile() - 1][4]));
				} else {
					boardSquares[s.getFile() - 1][3].changePiece(new Piece(
							WHITE, PAWN, boardSquares[s.getFile() - 1][3]));
				}
			}
			
			if (p.getType() == KING) {
				if (p.getColor()) {
					kingSquareWhite = p.getSquare();
				} else {
					kingSquareBlack = p.getSquare();
				}
			}
			if (isLegal) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<Move> allValidMoves() {
		return allValidMoves(turn);
	}

	public ArrayList<Move> allValidMoves(boolean color) {
		ArrayList<Move> ary = new ArrayList<Move>();
		for (Piece p : getPieces(color)) {
			for (Square s : legalMoveSquares(p)) {
				if (p.getType() == PAWN
						&& (s.getRank() == 1 || s.getRank() == 8)) {
					ary.add(new Move(p, s, 2));
					ary.add(new Move(p, s, 3));
					ary.add(new Move(p, s, 4));
					ary.add(new Move(p, s, 5));
				} else {
					ary.add(new Move(p, s));
				}
			}
		}
		return ary;
	}

	public void checkEndConditions(boolean color) {
		if (getPieces(WHITE).size() + getPieces(BLACK).size() == 2) {
			System.out.println("Draw by insufficient material");
			gameInProgress = 0;
		}
		for (Piece p : getPieces(color)) {

			if (legalMoveSquares(p).size() > 0) {
				return;
			}
		}
		if (isInCheck(color)) {
			if (color) {
				if (realBoard) {
					System.out.println("Black wins by Checkmate!");
				}
				gameInProgress = -1;
			} else {
				if (realBoard) {
					System.out.println("White wins by Checkmate!");
				}
				gameInProgress = 1;
			}
		} else {
			if (realBoard) {
				System.out.println("Draw by Stalemate!");
			}
			gameInProgress = 0;
		}

	}

	public Square[][] getBoardSquares() {
		return boardSquares;
	}

	public ArrayList<Piece> getPieces(boolean color) {
		ArrayList<Piece> ary = new ArrayList<Piece>();
		for (Square[] sq : boardSquares) {
			for (Square s : sq) {
				if (s.getPiece().getType() != 0
						&& s.getPiece().getColor() == color) {
					ary.add(s.getPiece());
				}
			}
		}
		return ary;
	}

	public ArrayList<Square> legalMoveSquaresWithoutCheck(Piece p) {
		ArrayList<Square> ary = new ArrayList<Square>();
		for (Square s : p.availableMoveSquares(this)) {
			switch (p.getType()) {

			case EMPTY:
				break;
			case KING:
				// Some more queenside castling checks.
				if (s.getFile() == 3 && p.getSquare().getFile() == 5) {
					if (boardSquares[1][p.getColor() ? 0 : 7].getPiece()
							.getType() == EMPTY
							&& boardSquares[2][p.getColor() ? 0 : 7].getPiece()
									.getType() == EMPTY
							&& boardSquares[3][p.getColor() ? 0 : 7].getPiece()
									.getType() == EMPTY) {
						if (!isInCheck(p.getColor())) {
							// Move the king over one square to test check.
							p.getSquare().changePiece(
									new Piece(true, EMPTY, p.getSquare()));
							boardSquares[3][p.getColor() ? 0 : 7]
									.changePiece(p);
							p.changeSquare(boardSquares[3][p.getColor() ? 0 : 7]);
							if (!isInCheck(p.getColor())) {
								// Move the king over another square to test
								// check.
								p.getSquare().changePiece(
										new Piece(true, EMPTY, p.getSquare()));
								boardSquares[2][p.getColor() ? 0 : 7]
										.changePiece(p);
								p.changeSquare(boardSquares[2][p.getColor() ? 0
										: 7]);
								if (!isInCheck(p.getColor())) {
									// Finally it has passed the necessary
									// requirements.
									ary.add(s);
								}
							}
							// Put king back.
							p.getSquare().changePiece(
									new Piece(true, EMPTY, p.getSquare()));
							boardSquares[4][p.getColor() ? 0 : 7]
									.changePiece(p);
							p.changeSquare(boardSquares[4][p.getColor() ? 0 : 7]);
						}
					}

				}
				// Some more kingside castling checks.
				else if (s.getFile() == 7 && p.getSquare().getFile() == 5) {
					if (boardSquares[5][p.getColor() ? 0 : 7].getPiece()
							.getType() == EMPTY
							&& boardSquares[6][p.getColor() ? 0 : 7].getPiece()
									.getType() == EMPTY) {
						if (!isInCheck(p.getColor())) {
							// Move the king over one square to test check.
							p.getSquare().changePiece(
									new Piece(true, EMPTY, p.getSquare()));
							boardSquares[5][p.getColor() ? 0 : 7]
									.changePiece(p);
							p.changeSquare(boardSquares[5][p.getColor() ? 0 : 7]);
							if (!isInCheck(p.getColor())) {
								// Move the king over another square to test
								// check.
								p.getSquare().changePiece(
										new Piece(true, EMPTY, p.getSquare()));
								boardSquares[6][p.getColor() ? 0 : 7]
										.changePiece(p);
								p.changeSquare(boardSquares[6][p.getColor() ? 0
										: 7]);
								if (!isInCheck(p.getColor())) {
									// Finally it has passed the necessary
									// requirements.
									ary.add(s);
								}
							}
							// Put king back.
							p.getSquare().changePiece(
									new Piece(true, EMPTY, p.getSquare()));
							boardSquares[4][p.getColor() ? 0 : 7]
									.changePiece(p);
							p.changeSquare(boardSquares[4][p.getColor() ? 0 : 7]);
						}
					}
				}
				// Simple king moves.
				else {
					if (s.getPiece().getColor() != p.getColor()
							|| s.getPiece().getType() == EMPTY) {
						ary.add(s);
					}
				}
				break;
			default:
				// For other pieces, check if there are empty squares in between
				// and that the destination square is empty or the opponent's
				// piece.

				if (checkSquaresBetween(p.getSquare(), s)) {
					if (s.getPiece().getColor() != p.getColor()
							|| s.getPiece().getType() == EMPTY) {
						ary.add(s);
					}
				}

				break;
			}
		}
		return ary;
	}

	public ArrayList<Square> legalMoveSquares(Piece p) {
		ArrayList<Square> ary = new ArrayList<Square>();

		for (Square s : legalMoveSquaresWithoutCheck(p)) {
			boolean enPessant = false;
			if (p.getType() == PAWN && p.getSquare().getFile() != s.getFile()
					&& s.getPiece().getType() == EMPTY) {
				enPessant = true;
				if (p.getColor()) {
					boardSquares[s.getFile() - 1][4].changePiece(new Piece(
							true, EMPTY, boardSquares[s.getFile() - 1][4]));
				} else {
					boardSquares[s.getFile() - 1][3].changePiece(new Piece(
							true, EMPTY, boardSquares[s.getFile() - 1][3]));
				}
			}
			Piece currentSquarePrevious = p;
			Piece otherSquarePrevious = s.getPiece();
			Square previousSquare = p.getSquare();
			p.getSquare().changePiece(new Piece(true, EMPTY, p.getSquare()));
			p.changeSquare(s);
			s.changePiece(currentSquarePrevious);
			if (p.getType() == KING) {
				if (p.getColor()) {
					kingSquareWhite = p.getSquare();
				} else {
					kingSquareBlack = p.getSquare();
				}
			}
			if (!isInCheck(currentSquarePrevious.getColor())) {
				ary.add(s);
			}
			previousSquare.changePiece(currentSquarePrevious);
			s.changePiece(otherSquarePrevious);
			p.changeSquare(previousSquare);
			if (enPessant) {
				if (p.getColor()) {
					boardSquares[s.getFile() - 1][4].changePiece(new Piece(
							BLACK, PAWN, boardSquares[s.getFile() - 1][4]));
				} else {
					boardSquares[s.getFile() - 1][3].changePiece(new Piece(
							WHITE, PAWN, boardSquares[s.getFile() - 1][3]));
				}
			}
			if (p.getType() == KING) {
				if (p.getColor()) {
					kingSquareWhite = p.getSquare();
				} else {
					kingSquareBlack = p.getSquare();
				}
			}
		}
		return ary;
	}

	public ArrayList<Square> checkableMoveSquares(Piece p) {

		ArrayList<Square> ary = new ArrayList<Square>();
		ArrayList<Square> aMS = p.availableMoveSquares(this);
		for (Square s : aMS) {
			if (p.getType() != KING) {

				if (checkSquaresBetween(p.getSquare(), s)) {
					if (s.getPiece().getColor() != p.getColor()
							|| s.getPiece().getType() == EMPTY) {
						ary.add(s);
					}
				}

			} else {
				int distance = s.getFile() - p.getSquare().getFile();
				if (distance == 0 || distance == -1 || distance == 1) {

					ary.add(s);
				}
			}
		}
		return ary;
	}

	public boolean checkSquaresBetween(Square a, Square b) {
		if (a.sameSquare(b) || a.getFile() - b.getFile() == 1
				|| a.getFile() - b.getFile() == -1
				|| a.getRank() - b.getRank() == 1
				|| a.getRank() - b.getRank() == -1) {
			return true;
		}
		if (a.getFile() == b.getFile()) {
			int start;
			int end;
			if (a.getRank() < b.getRank()) {
				start = a.getRank();
				end = b.getRank();
			} else {
				start = b.getRank();
				end = a.getRank();
			}
			for (int i = start + 1; i < end; i++) {
				if (boardSquares[a.getFile() - 1][i - 1].getPiece().getType() != EMPTY) {
					return false;
				}
			}
		} else if (a.getRank() == b.getRank()) {
			int start;
			int end;
			if (a.getFile() < b.getFile()) {
				start = a.getFile();
				end = b.getFile();
			} else {
				start = b.getFile();
				end = a.getFile();
			}
			for (int i = start + 1; i < end; i++) {
				if (boardSquares[i - 1][a.getRank() - 1].getPiece().getType() != EMPTY) {
					return false;
				}
			}
		} else if (a.getFile() + a.getRank() == b.getFile() + b.getRank()) {
			int start;
			int end;
			int diff;
			if (a.getFile() < b.getFile()) {
				start = a.getFile();
				end = b.getFile();
				diff = a.getRank() + a.getFile();
			} else {
				start = b.getFile();
				end = a.getFile();
				diff = b.getRank() + b.getFile();
			}
			for (int i = start + 1; i < end; i++) {
				if (boardSquares[i - 1][diff - i - 1].getPiece().getType() != EMPTY) {
					return false;
				}
			}
		} else if (a.getFile() - a.getRank() == b.getFile() - b.getRank()) {
			int start;
			int end;
			int diff;
			if (a.getFile() < b.getFile()) {
				start = a.getFile();
				end = b.getFile();
				diff = a.getRank() - a.getFile();
			} else {
				start = b.getFile();
				end = a.getFile();
				diff = b.getRank() - b.getFile();
			}
			for (int i = start + 1; i < end; i++) {
				if (boardSquares[i - 1][diff + i - 1].getPiece().getType() != EMPTY) {
					return false;
				}
			}
		}
		return true;
	}

	public Board getBoardAfterMove(Move m) {
		Board b = new Board(boardSquares, kingSquareWhite, kingSquareBlack,
				enPessantFile, canWhiteCastleKingside, canBlackCastleKingside,
				canWhiteCastleQueenside, canBlackCastleQueenside, turn,
				gameInProgress);
		Move newMove = new Move(b.convertSquare(m.getPiece().getSquare())
				.getPiece(), b.convertSquare(m.getMoveSquare()),
				m.promoteType());
		b.makeMove(newMove);
		return b;
	}

	public void makeMove(Move m) {
		Piece p = m.getPiece();
		boolean color = p.getColor();
		Square finish = m.getMoveSquare();
		if (color == turn) {
			if (isLegalMove(m)) {
				enPessantFile = 0;
				// if white's piece
				if (p.getColor() == WHITE) {
					switch (p.getType()) {
					case KING:
						if (Math.abs(p.getSquare().getFile() - finish.getFile()) > 1) {
							// Move pieces via castling
							if (finish.getFile() == 3) {
								// Queenside Castle
								p.changeSquare(boardSquares[2][0]);
								Piece rook = boardSquares[0][0].getPiece();
								rook.changeSquare(boardSquares[3][0]);
								boardSquares[0][0].changePiece(new Piece(WHITE,
										EMPTY, p.getSquare()));
								boardSquares[2][0].changePiece(p);
								boardSquares[3][0].changePiece(rook);
								boardSquares[4][0].changePiece(new Piece(WHITE,
										EMPTY, p.getSquare()));

							} else {
								// Kingside Castle
								p.changeSquare(boardSquares[6][0]);
								Piece rook = boardSquares[7][0].getPiece();
								rook.changeSquare(boardSquares[5][0]);
								boardSquares[7][0].changePiece(new Piece(WHITE,
										EMPTY, p.getSquare()));
								boardSquares[6][0].changePiece(p);
								boardSquares[5][0].changePiece(rook);
								boardSquares[4][0].changePiece(new Piece(WHITE,
										EMPTY, p.getSquare()));

							}
						} else {
							// Continue normally
							p.getSquare().changePiece(
									new Piece(WHITE, EMPTY, p.getSquare()));
							finish.changePiece(p);
							p.changeSquare(finish);

						}
						// Remove Castling ability.
						canWhiteCastleKingside = false;
						canWhiteCastleQueenside = false;
						kingSquareWhite = p.getSquare();
						break;
					case PAWN:
						// if en Pessant, remove the captured pawn.
						if (p.getSquare().getFile() != finish.getFile()
								&& finish.getPiece().getType() == EMPTY) {
							boardSquares[finish.getFile() - 1][4]
									.changePiece(new Piece(
											WHITE,
											EMPTY,
											boardSquares[finish.getFile() - 1][4]));
						}
						// if the pawn moves forward 2, notify that it can be
						// captured en Pessant.
						else if (p.getSquare().getRank() - finish.getRank() == -2) {

							enPessantFile = finish.getFile();
						}
						// pawn promotion
						if (finish.getRank() == 8) {
							p.getSquare().changePiece(
									new Piece(WHITE, EMPTY, p.getSquare()));
							finish.changePiece(new Piece(WHITE,
									m.promoteType(), finish));

						}
						// Continue normally
						else {
							p.getSquare().changePiece(
									new Piece(WHITE, EMPTY, p.getSquare()));
							finish.changePiece(p);
							p.changeSquare(finish);

						}
						break;
					case ROOK:
						// Remove castling ability as needed
						if (p.getSquare().getFile() == 8) {
							canWhiteCastleKingside = false;
						}
						if (p.getSquare().getFile() == 1) {
							canWhiteCastleQueenside = false;
						}
						// Continue normally
						p.getSquare().changePiece(
								new Piece(WHITE, EMPTY, p.getSquare()));
						finish.changePiece(p);
						p.changeSquare(finish);

						break;
					default:
						// Move or capture piece on the square moved to and
						// check end conditions.
						p.getSquare().changePiece(
								new Piece(WHITE, EMPTY, p.getSquare()));
						finish.changePiece(p);
						p.changeSquare(finish);

						break;
					}
				}
				// If black's piece
				else {
					switch (p.getType()) {
					case KING:
						if (Math.abs(p.getSquare().getFile() - finish.getFile()) > 1) {
							// Move pieces via castling
							if (finish.getFile() == 3) {
								// Queenside Castle
								p.changeSquare(boardSquares[2][7]);
								Piece rook = boardSquares[0][7].getPiece();
								rook.changeSquare(boardSquares[3][7]);
								boardSquares[0][7].changePiece(new Piece(WHITE,
										EMPTY, p.getSquare()));
								boardSquares[2][7].changePiece(p);
								boardSquares[3][7].changePiece(rook);
								boardSquares[4][7].changePiece(new Piece(WHITE,
										EMPTY, p.getSquare()));

							} else {
								// Kingside Castle
								p.changeSquare(boardSquares[6][7]);
								Piece rook = boardSquares[7][7].getPiece();
								rook.changeSquare(boardSquares[5][7]);
								boardSquares[7][7].changePiece(new Piece(WHITE,
										EMPTY, p.getSquare()));
								boardSquares[6][7].changePiece(p);
								boardSquares[5][7].changePiece(rook);
								boardSquares[4][7].changePiece(new Piece(WHITE,
										EMPTY, p.getSquare()));

							}
						} else {
							// Continue normally
							p.getSquare().changePiece(
									new Piece(WHITE, EMPTY, p.getSquare()));
							finish.changePiece(p);
							p.changeSquare(finish);

						}
						// Remove Castling ability.
						canBlackCastleKingside = false;
						canBlackCastleQueenside = false;
						kingSquareBlack = p.getSquare();
						break;
					case PAWN:
						// if en Pessant, remove the captured pawn.
						if (p.getSquare().getFile() != finish.getFile()
								&& finish.getPiece().getType() == EMPTY) {
							boardSquares[finish.getFile() - 1][3]
									.changePiece(new Piece(
											WHITE,
											EMPTY,
											boardSquares[finish.getFile() - 1][3]));
						}
						// if the pawn moves forward 2, notify that it can be
						// captured en Pessant.
						else if (p.getSquare().getRank() - finish.getRank() == 2) {
							enPessantFile = finish.getFile();
						}
						// pawn promotion
						if (finish.getRank() == 1) {
							p.getSquare().changePiece(
									new Piece(WHITE, EMPTY, p.getSquare()));
							finish.changePiece(new Piece(BLACK,
									m.promoteType(), finish));

						}
						// Continue normally
						else {
							p.getSquare().changePiece(
									new Piece(WHITE, EMPTY, p.getSquare()));
							finish.changePiece(p);
							p.changeSquare(finish);

						}
						break;
					case ROOK:
						// Remove castling ability as needed
						if (p.getSquare().getFile() == 8) {
							canBlackCastleKingside = false;
						}
						if (p.getSquare().getFile() == 1) {
							canBlackCastleQueenside = false;
						}
						// Continue normally
						p.getSquare().changePiece(
								new Piece(WHITE, EMPTY, p.getSquare()));
						finish.changePiece(p);
						p.changeSquare(finish);

						break;
					default:
						// Move or capture piece on the square moved to and
						// check end conditions.
						p.getSquare().changePiece(
								new Piece(WHITE, EMPTY, p.getSquare()));
						finish.changePiece(p);
						p.changeSquare(finish);

						break;
					}
				}
				turn = !turn;

			} else {
				System.out.println("Invalid Move!");
			}
		} else {
		}
		// Main.printBoard(this);

	}

	public int getEnPessantFile() {
		return enPessantFile;
	}

	public boolean getTurn() {
		return turn;
	}
	public Square getKingSquareWhite(){
		return kingSquareWhite;
	}
	public boolean getCastleKingside(boolean color) {

		return color ? canWhiteCastleKingside : canBlackCastleKingside;
	}

	public boolean getCastleQueenside(boolean color) {

		return color ? canWhiteCastleQueenside : canBlackCastleQueenside;
	}

	public Square squareAt(int file, int rank) {
		return boardSquares[file - 1][rank - 1];
	}

	public Square convertSquare(Square s) {
		return squareAt(s.getFile(), s.getRank());
	}

	public Move convertString(String s) {
		// default piece if string is entered wrong.
		Piece p = new Piece(WHITE, EMPTY, boardSquares[0][0]);
		int type;
		int pieceFile = 0;
		int pieceRank = 0;
		int finishFile;
		int finishRank;
		int promoteType = 0;
		Square finish;
		if (s.charAt(s.length() - 1) == '+' || s.charAt(s.length() - 1) == '#') {
			s = s.substring(0, s.length() - 1);
		}
		// Evaluate promotions. Ex:e8=Q.

		if (s.charAt(s.length() - 2) == '=') {
			switch (s.charAt(s.length() - 1)) {
			case 'Q':
				promoteType = 2;
				break;
			case 'R':
				promoteType = 3;
				break;
			case 'B':
				promoteType = 4;
				break;
			case 'N':
				promoteType = 5;
			}
			s = s.substring(0, s.length() - 2);
		}
		if (s.equals("O-O")) {
			for (Piece k : getPieces(turn)) {
				if (k.getType() == 1) {
					p = k;
				}
			}
			finish = boardSquares[6][turn ? 0 : 7];
		} else if (s.equals("O-O-O")) {
			for (Piece k : getPieces(turn)) {
				if (k.getType() == 1) {
					p = k;
				}
			}
			finish = boardSquares[2][turn ? 0 : 7];
		} else {
			switch (s.charAt(0)) {
			case 'K':
				type = 1;
				break;
			case 'Q':
				type = 2;
				break;
			case 'R':
				type = 3;
				break;
			case 'B':
				type = 4;
				break;
			case 'N':
				type = 5;
				break;
			// If the string does not start like above, it is a pawn move.
			default:

				type = 6;
				pieceFile = (int) s.charAt(0) - 96;
				s = " " + s;
				break;
			}
			finishFile = (int) s.charAt(s.length() - 2) - 96;
			finishRank = (int) s.charAt(s.length() - 1) - 48;
			finish = boardSquares[finishFile - 1][finishRank - 1];
			// Evaluate move determining letters for move. Ex:Raxd1.
			String specifics = s.substring(1, s.length() - 2);
			if (specifics.length() > 0) {
				if (specifics.charAt(specifics.length() - 1) == 'x') {
					specifics = specifics.substring(0, specifics.length() - 1);
				}
				if (specifics.length() == 1) {
					if ((int) specifics.charAt(0) < 97) {
						pieceRank = (int) specifics.charAt(0) - 48;
					} else {
						pieceFile = (int) specifics.charAt(0) - 96;
					}
				}
				if (specifics.length() == 2) {
					pieceFile = (int) specifics.charAt(0) - 96;
					pieceRank = (int) specifics.charAt(1) - 48;
				}

			}
			for (Piece k : getPieces(turn)) {
				if (k.getType() == type) {
					if (pieceFile != 0 && pieceFile != k.getSquare().getFile()) {
						continue;
					}
					if (pieceRank != 0 && pieceRank != k.getSquare().getRank()) {
						continue;
					}
					for (Square sq : legalMoveSquares(k)) {
						if (sq.sameSquare(finish)) {
							p = k;
						}
					}
				}
			}

			if (p.getType() == 0) {
				System.out.println("Syntax Error");
			}

		}
		return new Move(p, finish, promoteType);
	}

	public void endgame() {
		gameInProgress = 0;

	}

	public int checkProgress() {
		return gameInProgress;
	}

	public DoubleMove getEvaluation(int evalDepth, int depth, int boards,
			int transferPercentage) {
		Move bestMove = null;
		double score = 0;
		if (evalDepth == 1) {
			if (turn) {
				score = -500;
				ArrayList<Move> aVM = allValidMoves();
				if (aVM.size() >= 1) {
					for (Move m : aVM) {
						double newScore = getBoardAfterMove(m)
								.getWeightedEvaluation(depth + 1, boards,
										transferPercentage).getDouble();
						if (newScore >= score) {
							bestMove = m;
							score = newScore;
						}
					}
				} else {
					if (isInCheck(turn)) {
						score = -500 - depth;
					} else {
						score = 0;
					}
				}
			} else {
				score = 500;
				ArrayList<Move> aVM = allValidMoves();
				if (aVM.size() >= 1) {
					for (Move m : aVM) {
						if(m.toString(this).equals("Qxf2")){
							System.out.println(getBoardAfterMove(m).isInCheck(WHITE));
							System.out.println(getBoardAfterMove(m).getKingSquareWhite().toString());
							System.out.println(getBoardAfterMove(m).getBoardSquares()[5][1].getPiece().availableMoveSquares(this).contains(kingSquareWhite));
							Main.printBoard(getBoardAfterMove(m));
						}
						double newScore = getBoardAfterMove(m)
								.getWeightedEvaluation(depth + 1, boards,
										transferPercentage).getDouble();
						if (newScore <= score) {
							bestMove = m;
							score = newScore;
						}
						if(m.toString(this).equals("Qxf2")){
							System.out.println("Test End");
						}
					}
				} else {
					if (isInCheck(turn)) {
						score = 500 + depth;
					} else {
						score = 0;
					}
				}
			}
		} else {
			if (turn) {
				score = -500;
				ArrayList<Move> aVM = allValidMoves();
				if (aVM.size() >= 1) {
					for (Move m : aVM) {
						double newScore = getBoardAfterMove(m).getEvaluation(
								evalDepth - 1, depth + 1, boards,
								transferPercentage).getDouble();

						if (newScore >= score) {
							bestMove = m;
							score = newScore;
						}
						
					}
				} else {
					if (isInCheck(turn)) {
						score = -500 - depth;
					} else {
						score = 0;
					}

				}
			} else {
				score = 500;
				ArrayList<Move> aVM = allValidMoves();
				if (aVM.size() >= 1) {
					for (Move m : aVM) {
						double newScore = getBoardAfterMove(m).getEvaluation(
								evalDepth - 1, depth + 1, boards,
								transferPercentage).getDouble();
						if (newScore <= score) {
							bestMove = m;
							score = newScore;
						}
					}
				} else {
					if (isInCheck(turn)) {
						score = 500 + depth;
					} else {
						score = 0;
					}
				}
			}
		}
		return new DoubleMove(score, bestMove);

	}

	public DoubleMove getWeightedEvaluation(int depth, int boards,
			int transferPercentage) {
		Move bestMove = null;
		double score = 0;
		if (boards > 1) {
			if (turn) {
				score = -500;
				ArrayList<Move> aVM = allValidMoves(turn);
				if (aVM.size() >= 1) {
					ArrayList<DoubleMove> evals = new ArrayList<DoubleMove>();
					for (Move m : aVM) {
						DoubleMove eval = new DoubleMove(getBoardAfterMove(m)
								.getQuickEvaluation(), m);
						evals.add(eval);
					}

					Collections.sort(evals, new DoubleMoveComparatorWhite());

					boards--;
					for (int i = evals.size() - 1; boards > 0 && i >= 0; i--) {
						int boardsUsed = (int) (boards * transferPercentage / 100.0);
						DoubleMove dm = evals.get(i);
						evals.set(
								i,
								getBoardAfterMove(dm.getMove())
										.getWeightedEvaluation(depth + 1,
												boardsUsed, transferPercentage));
						boards -= boardsUsed;
					}

					Collections.sort(evals, new DoubleMoveComparatorWhite());
					return evals.get(evals.size() - 1);
					
				} else {
					if (isInCheck(turn)) {
						score = -500 - depth;
					} else {
						score = 0;
					}
				}
			} else {
				score = 500;
				ArrayList<Move> aVM = allValidMoves();
				if (aVM.size() != 0) {
					ArrayList<DoubleMove> evals = new ArrayList<DoubleMove>();
					for (Move m : aVM) {
						DoubleMove eval = new DoubleMove(getBoardAfterMove(m)
								.getQuickEvaluation(), m);
						evals.add(eval);

					}
					Collections.sort(evals, new DoubleMoveComparatorBlack());
					boards--;
					for (int i = evals.size() - 1; boards > 0 && i >= 0; i--) {
						int boardsUsed = (int) (boards * transferPercentage / 100.0);
						DoubleMove dm = evals.get(i);
						evals.set(
								i,
								getBoardAfterMove(dm.getMove())
										.getWeightedEvaluation(depth + 1,
												boardsUsed, transferPercentage));
						boards -= boardsUsed;
					}
					Collections.sort(evals, new DoubleMoveComparatorBlack());
					return evals.get(evals.size() - 1);
				} else {
					if (isInCheck(turn)) {
						score = 500 + depth;
					} else {
						score = 0;
					}
				}
			}
		} else if (boards == 1) {
			return getUnweightedEvaluation(0);
		} else {

			return new DoubleMove(getQuickEvaluation(), null);

		}
		return new DoubleMove(score, bestMove);
	}

	public DoubleMove getUnweightedEvaluation(int depth) {
		Move bestMove = null;
		double score;

		if (turn) {
			score = -500;
			ArrayList<Move> aVM = allValidMoves();
			if (aVM.size() != 0) {
				for (Move m : aVM) {
					if (depth == 0) {
						double newScore = getBoardAfterMove(m)
								.getQuickEvaluation();
						if (newScore >= score) {
							bestMove = m;
							score = newScore;
						}
					} else {
						double newScore = getBoardAfterMove(m)
								.getUnweightedEvaluation(depth - 1).getDouble();
						if (newScore >= score) {
							bestMove = m;
							score = newScore;
						}
					}
				} 
			} else {
				if (isInCheck(turn)) {
					score = -500 - depth;
				} else {
					score = 0;
				}
			}
		} else {
			score = 500;
			ArrayList<Move> aVM = allValidMoves();
			if (aVM.size() != 0) {
				for (Move m : aVM) {
					if (depth == 0) {
						double newScore = getBoardAfterMove(m)
								.getQuickEvaluation();
						if (newScore <= score) {
							bestMove = m;
							score = newScore;
						}
					} else {
						double newScore = getBoardAfterMove(m)
								.getUnweightedEvaluation(depth - 1).getDouble();
						if (newScore <= score) {
							bestMove = m;
							score = newScore;
						}
					}
				}
			} else {
				if (isInCheck(turn)) {
					score = 500 + depth;
				} else {
					score = 0;
				}
			}
		}
		return new DoubleMove(score, bestMove);

	}

	public double getQuickEvaluation() {
		boolean gameContinues = true;
//		for (Piece p : getPieces(turn)) {
//
//			if (legalMoveSquares(p).size() > 0) {
//				gameContinues = true;
//				break;
//			}
//		}
		if(!gameContinues){
			if(!isInCheck(turn)){
				return 0;
			} else if(turn){
				return -500;
			} else {
				return 500;
			}
		} else {
			double score = 0;
			int rookFile = 0;
			int rookRank = 0;
			int pieces = getPieces(WHITE).size() + getPieces(BLACK).size();
			boolean rookFound = false;
			for (Piece p : getPieces(WHITE)) {
				Square s = p.getSquare();
				switch (p.getType()) {
				case KING:
					score -= (0.05 * pieces - 0.5)
							* s.getEvaluationValue(WHITE);
				case QUEEN:
					score += 9;
					score -= (0.012 * pieces - 0.2)
							* s.getEvaluationValue(WHITE);
				case ROOK:
					score += 5;
					if (rookFound) {
						score += 0.005 * s.getEvaluationValue(WHITE);
						if (p.getSquare().getFile() == rookFile
								|| p.getSquare().getRank() == rookRank) {
							if (checkSquaresBetween(p.getSquare(),
									boardSquares[rookFile - 1][rookRank - 1])) {
								score += 0.5;
							}
						}
					} else {
						rookFound = true;
						rookFile = p.getSquare().getFile();
						rookRank = p.getSquare().getRank();
					}
				case BISHOP:
					score += 3.25;
					score += 0.025 * s.getEvaluationValue(WHITE);
				case KNIGHT:
					score += 3;
					score += 0.05 * s.getEvaluationValue(WHITE);
				case PAWN:
					score += 1;
					score += 0.3 * s.getEvaluationValue(WHITE);
				}
			}
			rookFile = 0;
			rookRank = 0;
			for (Piece p : getPieces(BLACK)) {
				Square s = p.getSquare();
				switch (p.getType()) {
				case KING:
					score += (0.05 * pieces - 0.5)
							* s.getEvaluationValue(BLACK);
				case QUEEN:
					score -= 9;
					score += (0.012 * pieces - 0.2)
							* s.getEvaluationValue(BLACK);
				case ROOK:
					score -= 5;
					if (rookFound) {
						score -= 0.005 * s.getEvaluationValue(BLACK);
						if (p.getSquare().getFile() == rookFile
								|| p.getSquare().getRank() == rookRank) {
							if (checkSquaresBetween(p.getSquare(),
									boardSquares[rookFile - 1][rookRank - 1])) {
								score -= 0.5;
							}
						}
					} else {
						rookFound = true;
						rookFile = p.getSquare().getFile();
						rookRank = p.getSquare().getRank();
					}
				case BISHOP:
					score -= 3.25;
					score -= 0.015 * s.getEvaluationValue(BLACK);
				case KNIGHT:
					score -= 3;
					score -= 0.03 * s.getEvaluationValue(BLACK);
				case PAWN:
					score -= 1;
					score -= 0.3 * s.getEvaluationValue(BLACK);
				}
			}

			return score;
		}
	}
}
