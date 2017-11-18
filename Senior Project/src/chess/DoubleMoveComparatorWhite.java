package chess;

import java.util.Comparator;

public class DoubleMoveComparatorWhite implements Comparator<DoubleMove>{
	public int compare(DoubleMove a, DoubleMove b){
		return (int) (1000*(a.getDouble()-b.getDouble()));
	}
}
