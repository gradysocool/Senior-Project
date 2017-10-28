package chess;

import java.util.Scanner;

public class Main {
	private static Board b;

	public static void main(String[] args) {
		b = new Board();
		Scanner sc = new Scanner(System.in);
		// Enter a move from square to square from starting position to test
		while(true){
			System.out.println("Make move");
			readCommand(sc.nextLine(),b);
		}
	}

	public static void test(Move m) {
		Square s1 = m.getPiece().getSquare();
		Square s2 = m.getMoveSquare();
		System.out.println("Piece Description:");
		System.out.println(s1.getPiece().getColor() + " "
				+ s1.getPiece().getType());
		System.out.println("Available Move Squares:");
		for (Square s : s1.getPiece().availableMoveSquares(b)) {
			System.out.println(s.toString());
		}
		System.out.println("Legal Move Squares Without Check:");
		for (Square s : b.legalMoveSquaresWithoutCheck(s1.getPiece())) {
			System.out.println(s.toString());
		}
		System.out.println("Legal Move Squares:");
		for (Square s : b.legalMoveSquares(s1.getPiece())) {
			System.out.println(s.toString());
		}
		System.out.println("Is Legal?");
		System.out.println(b.isValidMove(m));
		System.out.println("End Piece Description:");
		System.out.println(s1.getPiece().getColor() + " "
				+ s1.getPiece().getType());
	}

	public static void printBoard(Board b) {
		for (int i = 7; i >= 0; i--) {
			System.out.println("-----------------");
			for (int j = 0; j < 8; j++) {
				System.out.print("|");
				if (b.getBoardSquares()[j][i].getPiece().getColor()) {
					switch (b.getBoardSquares()[j][i].getPiece().getType()) {
					case 1:
						System.out.print("K");
						break;
					case 2:
						System.out.print("Q");
						break;
					case 3:
						System.out.print("R");
						break;
					case 4:
						System.out.print("B");
						break;
					case 5:
						System.out.print("N");
						break;
					case 6:
						System.out.print("P");
						break;
					default:
						System.out.print(" ");
						break;
					}
				}
				else{
					switch (b.getBoardSquares()[j][i].getPiece().getType()) {
					case 1:
						System.out.print("k");
						break;
					case 2:
						System.out.print("q");
						break;
					case 3:
						System.out.print("r");
						break;
					case 4:
						System.out.print("b");
						break;
					case 5:
						System.out.print("n");
						break;
					case 6:
						System.out.print("p");
						break;
					default:
						System.out.print(" ");
						break;
					}
				}
				
			}
			System.out.print("|\n");
		}
		System.out.println("----------------");
	}
	public static void readCommand(String s, Board b){
		if(s.equals("exit")){
			System.exit(0);
		}
		else{
			b.makeMove(b.convertString(s));
		}
	}
}
