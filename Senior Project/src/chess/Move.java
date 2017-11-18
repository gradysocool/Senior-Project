package chess;

public class Move {
	private Piece p;
	private Square finish;
	private int promoteType;

	public Move(Piece p, Square finish) {
		this.p = p;
		this.finish = finish;
		promoteType = 0;
	}

	public Move(Piece p, Square finish, int i) {
		this.p = p;
		this.finish = finish;
		promoteType = i;
	}

	public Piece getPiece() {
		return p;
	}

	public Square getMoveSquare() {
		return finish;
	}

	public int promoteType() {
		return promoteType;
	}

	public String toString(Board b) {
		String s = "";
		switch (p.getType()) {
		case 1:
			if(p.getSquare().getFile()==5){
				if(finish.getFile()==3){
					return "O-O-O";
				} 
				if(finish.getFile()==7){
					return "O-O";
				} 
			}
			s = s + "K";
			break;
		case 2:
			s = s + "Q";
			break;
		case 3:
			s = s + "R";
			break;
		case 4:
			s = s + "B";
			break;
		case 5:
			s = s + "N";
			break;
		case 6:
			if (p.getSquare().getFile() != finish.getFile()) {
				s = s + (char) (p.getSquare().getFile() + 'a' - 1);
			}
			break;
		}
		for(Piece op :b.getPieces(p.getColor())){
			if(op.getType()==p.getType()&&!op.equals(p)&&p.getType()!=6){
				for(Square os: b.legalMoveSquares(op)){
					if(finish.toString().equals(os.toString())){
						if(p.getSquare().getFile()==op.getSquare().getFile()){
							s = s + p.getSquare().getRank();
						} else{
							s = s + (char) (p.getSquare().getFile() + 'a' - 1);
						}
					}
				}
			}
		}
		if (finish.getPiece().getColor() != p.getColor()&&finish.getPiece().getType()!=0) {
			s = s + "x";
		}
		s = s + finish.toString();
		if (promoteType != 0) {
			switch (promoteType) {
			case 2:
				s = s + "=Q";
				break;
			case 3:
				s = s + "=R";
				break;
			case 4:
				s = s + "=B";
				break;
			case 5:
				s = s + "=N";
				break;
			}
		}
		return s;
	}
}
