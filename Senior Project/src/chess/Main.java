package chess;

public class Main {
	private static Board b;
	public static void main(String[] args){
		b = new Board();
		//Enter a move from square to square from starting position to test
		test(4,7,4,5);
	}
	public static void test(int bF, int bR, int eF, int eR){
		Square s1 = new Square(1,1);
		Square s2 = new Square(1,1);
		for(Square[] sq: b.getBoardSquares()){
			for (Square s: sq){
				if(s.getFile()==bF&&s.getRank()==bR){
					s1= s;
				}
			}
		}
		for(Square[] sq: b.getBoardSquares()){
			for (Square s: sq){
				if(s.getFile()==eF&&s.getRank()==eR){
					s2= s;
				}
			}
		}
		System.out.println("Piece Description:");
		System.out.println(s1.getPiece().getColor()+ " " + s1.getPiece().getType());
		System.out.println("Available Move Squares:");
		for(Square s: s1.getPiece().availableMoveSquares(b)){
			System.out.println(s.toString());
		}
		System.out.println("Legal Move Squares Without Check:");
		for(Square s: b.legalMoveSquaresWithoutCheck(s1.getPiece())){
			System.out.println(s.toString());
		}
		System.out.println("Legal Move Squares:");
		for(Square s: b.legalMoveSquares(s1.getPiece())){
			System.out.println(s.toString());
		}
		System.out.println("Is Legal?");
		System.out.println(b.isValidMove(s1.getPiece(), s2));
		System.out.println("End Piece Description:");
		System.out.println(s1.getPiece().getColor()+ " " + s1.getPiece().getType());
	}
}
