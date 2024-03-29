package mvc;

import javax.swing.JFrame;

public class Application {

public static void main(String[] args) {
		
		DrawingFrame frame = new DrawingFrame();
		DrawingModel model = new DrawingModel();
		frame.getView().setModel(model);
		
		DrawingController controller = new DrawingController(model, frame);
		frame.setController(controller);
		
		frame.setSize(500,500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);	
	}
}
