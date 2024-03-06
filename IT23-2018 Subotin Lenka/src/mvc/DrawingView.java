package mvc;

import java.util.Iterator;

import javax.swing.JPanel;
import java.awt.Graphics;
import geometry.Shape;

public class DrawingView extends JPanel {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
private DrawingModel model = new DrawingModel();
	
	public DrawingView() {
		
	}

	public void setModel(DrawingModel model) {
		this.model = model;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Iterator<Shape> it = model.getShapes().iterator();
		while (it.hasNext()) {
			it.next().draw(g);
		}	
	}
}
