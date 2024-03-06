package command;

import geometry.Donut;

public class CmdUpdateDonut implements Command {

	private Donut oldState;
	private Donut newState;
	
	private Donut originalState = new Donut();
	
	public CmdUpdateDonut(Donut oldState, Donut newState) {
		this.oldState = oldState;
		this.newState = newState;
	}
	
	@Override
	public void execute() {
		
		originalState = oldState.clone();
		
		oldState.setCenter(newState.getCenter().clone());
		oldState.setInnerRadius(newState.getInnerRadius());
		oldState.setRadius(newState.getRadius());
		oldState.setInnerColor(newState.getInnerColor());
		oldState.setColor(newState.getColor());
	}

	@Override
	public void unexecute() {
		
		oldState.setCenter(originalState.getCenter());
		oldState.setInnerRadius(originalState.getInnerRadius());
		oldState.setRadius(originalState.getRadius());
		oldState.setInnerColor(originalState.getInnerColor());
		oldState.setColor(originalState.getColor());
	}

	@Override
	public String logText() {
		return "Updated->" + originalState.toString() + "->" + newState.toString();
	}
	
}
