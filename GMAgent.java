package edu.uky.ai.chess.ex;

import java.util.ArrayList;
import java.util.Iterator;
//import Result.java;
import java.util.Random;

import edu.uky.ai.chess.Agent;
import edu.uky.ai.chess.state.Bishop;
import edu.uky.ai.chess.state.Board;
import edu.uky.ai.chess.state.Knight;
import edu.uky.ai.chess.state.Pawn;
import edu.uky.ai.chess.state.Piece;
import edu.uky.ai.chess.state.Player;
import edu.uky.ai.chess.state.Queen;
import edu.uky.ai.chess.state.Rook;
import edu.uky.ai.chess.state.State;

/**
 * This agent chooses a next move randomly from among the possible legal moves.
 * 
 * @author Eli Rice
 * 
 */

public class GMAgent extends Agent {
	
	/**
	 * Constructs a new random agent. You should change the string below from
	 * "Example" to your ID. You should also change the name of this class. In
	 * Eclipse, you can do that easily by right-clicking on this file
	 * (RandomAgent.java) in the Package Explorer and choosing Refactor > Rename.
	 */
	public GMAgent() {
		super("ejri229");
	}
	
	private static int material(Board board, Player player) {
		int material = 0;
		for(Piece piece : board)
			if(piece.player == player)
				material += value(piece);
		return material;
	}
	
	private static int value(Piece piece) {
		if(piece instanceof Pawn)
			return 1;
		else if(piece instanceof Knight)
			return 3;
		else if(piece instanceof Bishop)
			return 3;
		else if(piece instanceof Rook)
			return 5;
		else if(piece instanceof Queen)
			return 9;
		// The piece must be a King.
		else
			return 100;
	}
	
	private static double utility(State current){
		double utility;
		if(current.player == Player.WHITE) {
			utility = material(current.board, current.player) - material(current.board, Player.BLACK);
			return utility;
		}
		else {
			utility = material(current.board, Player.WHITE) - material(current.board, current.player);
			return -utility;
		}
	}
	
	private static Result findMax(State current, double alpha, double beta, int depth) {
		if (depth == 0)
			return new Result(current, utility(current));
		if (current.over == true) {
			if(current.check == true) {
				return new Result(current, Double.NEGATIVE_INFINITY);
			}
			return new Result(current, 0.0);
		}
		double max = Double.NEGATIVE_INFINITY;
		// iterates over all states from given current state
		Iterator<State> iterator = current.next().iterator();
		State child = null;
		while (iterator.hasNext() && !current.budget.hasBeenExhausted()) {
			// sets new state child equal to the next possible state from the current state
			child = iterator.next();
			Result min = findMin(child, alpha, beta, depth-1);
			double value = min.value;
			max = Math.max(max, value);
			if(max >= beta)
				return new Result(child,max);
			alpha = Math.max(alpha, max);
		}
		return new Result(child,max);
	}
	
	private static Result findMin(State current, double alpha, double beta, int depth) {
		if (depth == 0)
			return new Result(current, utility(current));
		if (current.over == true) {
			if(current.check == true) {
				return new Result(current, Double.POSITIVE_INFINITY);
			}
			return new Result(current, 0.0);
		}
		double min = Double.POSITIVE_INFINITY;
		// iterates over all states from given current state
		Iterator<State> iterator = current.next().iterator();
		State child = null;
		while (iterator.hasNext() && !current.budget.hasBeenExhausted()) {
		// sets new state child equal to the next possible state from the current state
			child = iterator.next();
			Result max = findMax(child, alpha, beta, depth-1);
			double value = max.value;
			min = Math.min(min, value);
			if (min <= alpha)
				return new Result(child,min);
			beta = Math.min(beta, min);
		}
		return new Result(child,min);
	} 
	
	@Override
	protected State chooseMove(State current) {
		// This list will hold all the children state (all possible next moves).
		//ArrayList<Result> children = new ArrayList<>();
		// Iterate through each child and put it in the list (as long as the
		// search budget hasn't been used up yet).
	//	Iterator<State> iterator = current.next().iterator();
	//	while(!current.budget.hasBeenExhausted() && iterator.hasNext()) 
	//		children.add(iterator.next());
		//double value;
		double alpha = Double.NEGATIVE_INFINITY;
		double beta = Double.POSITIVE_INFINITY;
		Result best = new Result(current, 0.0);
		Result choice;
		if(current.player == Player.BLACK) {
			for (int i = 0; i < 8; i++) {
				choice = findMin(current, alpha, beta, i);
				if (choice.value <= best.value)
					best.state = choice.state;
			}
			return best.state;
		}
		else {
			for (int i = 0; i < 8; i++) {
				choice = findMax(current, alpha, beta, i);
				if (choice.value >= best.value)
					best.state = choice.state;
			}
			return best.state;
		}
	}
}
