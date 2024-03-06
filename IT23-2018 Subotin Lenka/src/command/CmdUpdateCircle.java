package command;

import geometry.Circle;

public class CmdUpdateCircle implements Command {

	private Circle oldState;
	private Circle newState;
	
	private Circle originalState = new Circle();
	
	public CmdUpdateCircle(Circle oldState, Circle newState) {
		this.oldState = oldState;
		this.newState = newState;
	}
	
	@Override
	public void execute() {
		
		originalState = oldState.clone();
		
		oldState.setCenter(newState.getCenter().clone());
		oldState.setRadius(newState.getRadius());
		oldState.setInnerColor(newState.getInnerColor());
		oldState.setColor(newState.getColor());
	}
	
	@Override
	public void unexecute() {
		
		oldState.setCenter(originalState.getCenter());
		oldState.setRadius(originalState.getRadius());
		oldState.setInnerColor(originalState.getInnerColor());
		oldState.setColor(originalState.getColor());
	}

	@Override
	public String logText() {
		return "Updated->" + originalState.toString() + "->" + newState.toString();
	}
	
}
