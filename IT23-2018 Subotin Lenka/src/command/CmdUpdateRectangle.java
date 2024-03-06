package command;

import geometry.Rectangle;

public class CmdUpdateRectangle implements Command {

	private Rectangle oldState;
	private Rectangle newState;
	
	private Rectangle originalState = new Rectangle();
	
	public CmdUpdateRectangle(Rectangle oldState, Rectangle newState) {
		this.oldState = oldState;
		this.newState = newState;
	}
	
	@Override
	public void execute() {
		
		originalState = oldState.clone();
		oldState.setUpperLeftPoint(newState.getUpperLeftPoint().clone());
		oldState.setWidth(newState.getWidth());
		oldState.setHeight(newState.getHeight());
		oldState.setColor(newState.getColor());
		oldState.setInnerColor(newState.getInnerColor());
	}
	
	@Override
	public void unexecute() {
		
		oldState.setUpperLeftPoint(originalState.getUpperLeftPoint());
		oldState.setWidth(originalState.getWidth());
		oldState.setHeight(originalState.getHeight());
		oldState.setColor(originalState.getColor());
		oldState.setInnerColor(originalState.getInnerColor());
	}
	
	@Override
	public String logText() {
		return "Updated->" + originalState.toString() + "->" + newState.toString();
	}
	
}
