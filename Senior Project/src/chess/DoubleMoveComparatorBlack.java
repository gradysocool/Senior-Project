package chess;

import java.util.Comparator;

public class DoubleMoveComparatorBlack implements Comparator<DoubleMove>{
	public int compare(DoubleMove a, DoubleMove b){
		
		return (int) (-1000*(a.getDouble()-b.getDouble()));
		
	}
}
