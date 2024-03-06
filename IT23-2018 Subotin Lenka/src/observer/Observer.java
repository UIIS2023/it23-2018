package observer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import mvc.DrawingFrame;

public class Observer implements PropertyChangeListener {

	private DrawingFrame frame;
	
	public Observer(DrawingFrame frame) {
		this.frame = frame;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		
		if(event.getPropertyName().equals("btnUpdate")) {
			frame.getBtnUpdate().setEnabled((boolean)event.getNewValue());
		}
		
		if(event.getPropertyName().equals("btnDelete")) {
			frame.getBtnDelete().setEnabled((boolean)event.getNewValue());
		}
		
		if(event.getPropertyName().equals("btnBringToFront")) {
			frame.getBtnBringToFront().setEnabled((boolean)event.getNewValue());
		}
		
		if(event.getPropertyName().equals("btnToFront")) {
			frame.getBtnToFront().setEnabled((boolean)event.getNewValue());
		}
		
		if(event.getPropertyName().equals("btnBringToBack")) {
			frame.getBtnBringToBack().setEnabled((boolean)event.getNewValue());
		}
		
		if(event.getPropertyName().equals("btnToBack")) {
			frame.getBtnToBack().setEnabled((boolean)event.getNewValue());
		}
	}	
}
