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
}
