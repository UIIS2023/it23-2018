package command;

import java.util.Collections;

import geometry.Shape;
import mvc.DrawingModel;

public class CmdBringToBack implements Command {

	private DrawingModel drawingModel;
	private Shape selectedShape;
	private int index;

	public CmdBringToBack(DrawingModel drawingModel,Shape selectedShape) {
		this.drawingModel = drawingModel;
		this.selectedShape = selectedShape;
		index = drawingModel.getShapes().indexOf(selectedShape);
	}
	
	@Override
	public void execute() {
		for (int i = index; i > 0; i--) {
			Collections.swap(drawingModel.getShapes(), i, i-1);
			}
	}

	@Override
	public void unexecute() {
		for (int i = 0; i < index; i++) {
			Collections.swap(drawingModel.getShapes(), i, i+1);
			}
	}
	
	@Override
	public String logText() {
		return "Brought to back->" + selectedShape.toString();
	}
}
