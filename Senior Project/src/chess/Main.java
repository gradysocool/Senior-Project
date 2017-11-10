package chess;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	private static Board b;

	public static void main(String[] args) {
//		long startTime = System.currentTimeMillis();
//		int counter=0;
//		ArrayList<ArrayList<String>> ary = readPGN("");
//		for (int i = 0; i < ary.size(); i++) {
//
//			b = new Board();

//			for (int j = 0; j < ary.get(i).size(); j++) {
//				System.out.print("Game: " + i);
//				System.out.println("Make move");
//				System.out.println(ary.get(i).get(j));
//				readCommand(ary.get(i).get(j), b);
//				System.out.println(b.getUnweightedEvaluation(1));
//				counter++;
//			}
//		}
//		long endTime = System.currentTimeMillis();
//		System.out.println(counter + " moves");
//		System.out.println((endTime - startTime) + " milliseconds");
		b = new Board();
		Scanner sc = new Scanner(System.in);
		while(b.checkProgress()==2){
			//System.out.println("Make Move");
			long startTime = System.currentTimeMillis();
			DoubleMove dm = b.getUnweightedEvaluation(1);
			readCommand(dm.getMove().toString(b), b);
			System.out.println(dm.getDouble());
			System.out.println(dm.getMove().toString(b));
			printBoard(b);
			long endTime = System.currentTimeMillis();
			System.out.println((endTime - startTime) + " milliseconds");
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
				} else {
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

	public static void readCommand(String s, Board b) {
		if (s.equals("exit")) {
			System.exit(0);
		}
		if (s.equals("1-0") || s.equals("0-1") || s.equals("1/2-1/2")) {
			b.endgame();
		} else {
			b.makeMove(b.convertString(s));
		}
	}

	public static ArrayList<ArrayList<String>> readPGN(String file) {
		ArrayList<ArrayList<String>> games = new ArrayList<ArrayList<String>>();
		BufferedReader br = null;
		String line;
		boolean moreGames = true;
		try {
			br = new BufferedReader(new FileReader(
					"C:\\Users\\User\\AppData\\Local\\Temp\\Adams.pgn"));
		} catch (FileNotFoundException fnfex) {
			System.out.println(fnfex.getMessage() + "Error reading file.");
		}
		while (moreGames) {
			ArrayList<String> game = new ArrayList<String>();
			String s = "";
			// Cuts out extraneous information before game (end is indicated by
			// an empty line)
			try {
				while ((line = br.readLine()) != null) {
					if (line.length() == 0) {
						break;
					}
				}
			} catch (IOException ioex) {
				System.out.println(ioex.getMessage() + "Error reading file.");
			}
			// Get the game as a string (end is indicated by an empty line)
			try {
				while ((line = br.readLine()) != null) {
					if (line.length() == 0 && s.length() > 0) {
						break;
					}
					s = s + line + " ";
				}
			} catch (IOException ioex) {
				System.out.println(ioex.getMessage() + "Error reading file.");
			}
			try {
				if ((line = br.readLine()) == null) {
					moreGames = false;
				}
			} catch (IOException ioex) {
				System.out.println(ioex.getMessage() + "Error reading file.");
			}
			String[] split = s.split(" ");
			for (int i = 0; i < split.length; i++) {
				String move = split[i];
				if (move.length() > 0) {
					if (move.length() > 2) {
						if (move.indexOf('.') > 0) {
							move = move.substring(move.indexOf('.') + 1);
						}
					}
					game.add(move);
				}
			}
			games.add(game);
		}
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return games;
	}
}
