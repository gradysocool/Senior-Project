package chess;

public class DoubleMove {
	double d;
	Move m;

	public DoubleMove(double d, Move m) {
		this.d = d;
		this.m = m;
	}

	public double getDouble() {
		return d;
	}

	public Move getMove() {
		return m;
	}
	public String toString(Board b, int depth){
		String s = "";
		for(int i = 0; i<depth; i++){
			s = s + "    ";
		}
		return s + m.toString(b) + " " + d;
	}
}
