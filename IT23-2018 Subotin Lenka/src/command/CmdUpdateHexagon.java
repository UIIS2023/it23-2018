package command;

import adapter.HexagonAdapter;

public class CmdUpdateHexagon implements Command {
	
	private HexagonAdapter oldState;
	private HexagonAdapter newState;
	
	private HexagonAdapter originalState = new HexagonAdapter();

	public CmdUpdateHexagon(HexagonAdapter oldState, HexagonAdapter newState) {
		this.oldState = oldState;
		this.newState = newState;
	}

	@Override
	public void execute() {
		originalState = oldState.clone();
		oldState.getHexagon().setX(newState.getHexagon().getX());
		oldState.getHexagon().setY(newState.getHexagon().getY());
		oldState.setOutlineColor(newState.getOutlineColor());
		oldState.setInnerColor(newState.getInnerColor());
		oldState.setRadius(newState.getRadius());	
	}

	@Override
	public void unexecute() {
		oldState.getHexagon().setX(originalState.getHexagon().getX());
		oldState.getHexagon().setY(originalState.getHexagon().getY());
		oldState.setOutlineColor(originalState.getOutlineColor());
		oldState.setInnerColor(originalState.getInnerColor());
		oldState.setRadius(originalState.getRadius());
	}
	
	@Override
	public String logText() {
		return "Updated->" + originalState.toString() + "->" + newState.toString();
	}
	
}
