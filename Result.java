package edu.uky.ai.chess.ex;

import edu.uky.ai.chess.state.State;

public class Result {

	public State state;
	public double value;

	public Result(State state, double value) {
		this.state = state;
		this.value = value;
	}
}