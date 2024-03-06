package command;

import geometry.Line;

public class CmdUpdateLine implements Command {

	private Line oldState;
	private Line newState;
	
	private Line originalState = new Line();
	
	public CmdUpdateLine(Line oldState, Line newState) {
		this.oldState = oldState;
		this.newState = newState;
	}
	
	@Override
	public void execute() {
		
		originalState = oldState.clone();
		oldState.setStartPoint(newState.getStartPoint().clone());
		oldState.setEndPoint(newState.getEndPoint().clone());
		oldState.setColor(newState.getColor());
	}

	@Override
	public void unexecute() {
		
		oldState.setStartPoint(originalState.getStartPoint());
		oldState.setEndPoint(originalState.getEndPoint());
		oldState.setColor(originalState.getColor());	
	}

	@Override
	public String logText() {
		return "Updated->" + originalState.toString() + "->" + newState.toString();
	}
	
}
