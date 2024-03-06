package command;

import java.util.List;

import geometry.Shape;
import mvc.DrawingModel;

public class CmdRemoveShape implements Command {

	private DrawingModel model;
	private Shape shape;
	private List<Shape> tempList;
	
	public CmdRemoveShape(DrawingModel model, Shape shape, List<Shape> tempList) {
		this.model = model;
		this.shape = shape;
		this.tempList = tempList;
	}
	
	@Override
	public void execute() {
		tempList.remove(shape);
		model.remove(shape);
		
	}

	@Override
	public void unexecute() {
		tempList.add(shape);
		model.add(shape);	
	}
	
	@Override
	public String logText() {
		return "Deleted->" + shape.toString();	 
	}

}
