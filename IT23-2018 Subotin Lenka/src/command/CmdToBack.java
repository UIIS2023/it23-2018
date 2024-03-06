package command;

import java.util.Collections;

import geometry.Shape;
import mvc.DrawingModel;

public class CmdToBack implements Command {

	private DrawingModel drawingModel;
	private Shape shape;

    public CmdToBack(DrawingModel drawingModel,Shape shape) {	
    	this.drawingModel = drawingModel;
    	this.shape = shape;	
	}

  	@Override
  	public void execute() {
  		if(drawingModel.getShapes().indexOf(shape) > 0) {
  			Collections.swap(drawingModel.getShapes(), drawingModel.getShapes().indexOf(shape), drawingModel.getShapes().indexOf(shape)-1);
  		}
  	}

  	@Override
  	public void unexecute() {
  		if (drawingModel.getShapes().indexOf(shape) < drawingModel.getShapes().size()-1) {
  		Collections.swap(drawingModel.getShapes(), drawingModel.getShapes().indexOf(shape), drawingModel.getShapes().indexOf(shape)+1);
  		}	
  	}
	
	@Override
	public String logText() {
		return "Moved to back->"+ shape.toString();
	}

}
