package chess;

import java.util.ArrayList;

public class Square {
	private int file;
	private int rank;
	private Piece pieceOnSquare;
	/**
	 * 
	 * @param file File of square
	 * @param rank Rank of square
	 * @param p Piece on square
	 */
	public Square(int file, int rank, Piece p){
		this.file = file;
		this.rank = rank;
		pieceOnSquare = p;
	}
	public Square(int file, int rank){
		this.file = file;
		this.rank = rank;
		pieceOnSquare = new Piece(true, 0, this);
	}
	public void changePiece(Piece p){
		pieceOnSquare = p;
	}
	
	
	public Piece getPiece(){
		return pieceOnSquare;
	}
	public String toString(){
		String s = "";
		s += (char)((int)'a'+ file - 1);
		s += rank;
		return s;
	}
	public int getFile(){
		return file;
	}
	public int getRank(){
		return rank;
	}
	public double getEvaluationValue(boolean color){
		double score = 0;
		score += 4.0/Math.pow(2,Math.abs(4.5-getFile())+0.5);
		if(color){
		score += getRank()/4;
		} else{
			score += 2 - getRank()/4;
		}
		return score;
	}
}
