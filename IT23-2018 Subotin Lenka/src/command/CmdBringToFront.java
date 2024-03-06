package command;

import java.util.Collections;

import geometry.Shape;
import mvc.DrawingModel;

public class CmdBringToFront implements Command {

	private DrawingModel drawingModel;
	private Shape selectedShape;
	private int position;
	private int last;
	
	public CmdBringToFront(DrawingModel drawingModel,Shape selectedShape) {	
		this.drawingModel = drawingModel;
	    this.selectedShape = selectedShape;	
	    position = drawingModel.getShapes().indexOf(selectedShape);
		last = drawingModel.getShapes().size()-1;
	}

	@Override
	public void execute() {
		for(int i = position; i < last; i++) {
			Collections.swap(drawingModel.getShapes(), i, i+1);
		}
	}

	@Override
	public void unexecute() {
		for(int i = last; i > position; i--) {
			Collections.swap(drawingModel.getShapes(), i, i-1);
		}
	}
	
	@Override
	public String logText() {
		return "Brought to front->" + selectedShape.toString();
	}

}
