package mvc;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import adapter.HexagonAdapter;
import command.CmdAddShape;
import command.CmdBringToBack;
import command.CmdBringToFront;
import command.CmdDeselectShape;
import command.CmdRemoveShape;
import command.CmdSelectShape;
import command.CmdToBack;
import command.CmdToFront;
import command.CmdUndoRedo;
import command.CmdUpdateCircle;
import command.CmdUpdateDonut;
import command.CmdUpdateHexagon;
import command.CmdUpdateLine;
import command.CmdUpdatePoint;
import command.CmdUpdateRectangle;
import command.Command;
import drawing.DlgCircle;
import drawing.DlgDonut;
import drawing.DlgHexagon;
import drawing.DlgLine;
import drawing.DlgPoint;
import drawing.DlgRectangle;
import geometry.Circle;
import geometry.Donut;
import geometry.Line;
import geometry.Point;
import geometry.Rectangle;
import geometry.Shape;
import hexagon.Hexagon;
import observer.Observable;
import observer.Observer;
import strategy.LogFile;
import strategy.ManagerFile;
import strategy.SaveDraw;
import strategy.SerializableFile;

public class DrawingController {

	private DrawingModel model;
	private DrawingFrame frame;
	
	private Shape selected;
	private Shape isSelectedShape;
	
	private Observable observable = new Observable();
	private Observer observer;
	private PropertyChangeSupport propertyChangeSupport; 
	private int counterOfSelectedShapes = 0; 
	
	private List<Shape> selectedShapes = new ArrayList<Shape>(); 
	
	private Stack<Command> stackUndo = new Stack<Command>(); 
	private Stack<Command> stackRedo = new Stack<Command>(); 
	
	private DefaultListModel<String> logList; 
	private ManagerFile manager;
	
	public DrawingController(DrawingModel model, DrawingFrame frame) {
		this.model = model;
		this.frame = frame;
		
		observer = new Observer(frame);
		observable.addPropertyChangeListener(observer);
		propertyChangeSupport = new PropertyChangeSupport(this);
		logList = frame.getDlmList();	
	}
		
	@SuppressWarnings("deprecation")
	public void mouseClicked(MouseEvent e) {
			
		if(frame.getBtnSelect().isSelected()) 
		{
			isSelectedShape = null;
			selected = null;	
			
			ListIterator<Shape> it = model.getShapes().listIterator();
			while(it.hasNext()) {
				isSelectedShape = it.next();
				if(isSelectedShape.contains(e.getX(),e.getY())) {
					
					selected =isSelectedShape;
				}
				
			}
			if(selected != null) {
				if(selected.isSelected()) {
					CmdDeselectShape cmdShapeDeselect = new CmdDeselectShape(this,selected);
					cmdShapeDeselect.execute();
					
					addCommandInStack(cmdShapeDeselect);
					frame.getBtnUndo().setEnabled(true);
					frame.getBtnRedo().setEnabled(false);
					
					logList.addElement(cmdShapeDeselect.logText());
					
					checkButtons();
					
				}
				else {
					CmdSelectShape cmdShapeSelect = new CmdSelectShape(this,selected);
					cmdShapeSelect.execute();	
					
					addCommandInStack(cmdShapeSelect); 
					frame.getBtnUndo().setEnabled(true);
					frame.getBtnRedo().setEnabled(false);
					
					logList.addElement(cmdShapeSelect.logText());
					
					checkButtons();
				}
			} 
			else {	
				ListIterator<Shape> it1 = selectedShapes.listIterator(); 
				while(it1.hasNext())
				{ 
					isSelectedShape = it1.next(); 
					CmdDeselectShape cmdShapeDeselect = new CmdDeselectShape(this, isSelectedShape);
					
					addCommandInStack(cmdShapeDeselect);
					logList.addElement(cmdShapeDeselect.logText());
					
					isSelectedShape.setSelected(false); 
				}
			  
				
				selectedShapes.clear(); 
				checkButtons();
			}
	
			frame.getView().repaint();
		}

		if(frame.getState() == 1) {
			
			Point point = new Point(e.getX(), e.getY(), frame.getBtnEdge().getBackground());
			
			CmdAddShape cmdAddShape = new CmdAddShape(model, point);
			cmdAddShape.execute();
			
			addCommandInStack(cmdAddShape); 
			stackRedo.removeAllElements();
			
			logList.addElement(cmdAddShape.logText());
			
			frame.getBtnRedo().setEnabled(false);
		}
			
		if(frame.getState() == 2) {
			if(model.getStartPoint() == null) {
				model.setStartPoint(new Point(e.getX(), e.getY()));
			}
			else {
				Line line = new Line(model.getStartPoint(), new Point(e.getX(), e.getY()), frame.getBtnEdge().getBackground());
					 
				model.setStartPoint(null); 
			    CmdAddShape cmdAddShape = new CmdAddShape(model, line);
			    cmdAddShape.execute();
			    
			    addCommandInStack(cmdAddShape); 
				stackRedo.removeAllElements();
				
				logList.addElement(cmdAddShape.logText());
				
				frame.getBtnRedo().setEnabled(false);
			}
		}
			
		if(frame.getState() == 3) {
			DlgCircle dlgCircle = new DlgCircle();
			dlgCircle.getTxtX().setText(Integer.toString(e.getX())); 
			dlgCircle.getTxtX().disable();
			dlgCircle.getTxtY().setText(Integer.toString(e.getY()));
			dlgCircle.getTxtY().disable();
			dlgCircle.getBtnOutlineColor().setBackground(frame.getBtnEdge().getBackground());
			dlgCircle.getBtnInnerColor().setBackground(frame.getBtnInterior().getBackground());
			dlgCircle.setVisible(true);
				
			if(dlgCircle.isOk()) {
				try {
					Circle circle = new Circle(new Point(e.getX(), e.getY()), Integer.parseInt(dlgCircle.getTxtR().getText()), dlgCircle.getBtnOutlineColor().getBackground(), dlgCircle.getBtnInnerColor().getBackground());
			
					CmdAddShape cmdAddShape = new CmdAddShape(model, circle);
					cmdAddShape.execute();
					
					addCommandInStack(cmdAddShape); 
					stackRedo.removeAllElements();
					
					logList.addElement(cmdAddShape.logText());
					
					frame.getBtnRedo().setEnabled(false);
						
				}
				catch (Exception e1) {
					JOptionPane.showMessageDialog(frame, "Radius must be greater than 0!", null, JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
		}
			
		if(frame.getState() == 4) {
			DlgRectangle dlgRectangle = new DlgRectangle();
			dlgRectangle.getTxtUpperLeftPointX().setText(Integer.toString(e.getX())); 
			dlgRectangle.getTxtUpperLeftPointX().disable();
			dlgRectangle.getTxtUpperLeftPointY().setText(Integer.toString(e.getY()));
			dlgRectangle.getTxtUpperLeftPointY().disable();	
			dlgRectangle.getBtnOutlineColor().setBackground(frame.getBtnEdge().getBackground());
			dlgRectangle.getBtnInnerColor().setBackground(frame.getBtnInterior().getBackground());
			dlgRectangle.setVisible(true);
				
			if(dlgRectangle.isOk()) {
				try {
					Rectangle rectangle = new Rectangle(new Point(e.getX(), e.getY()),Integer.parseInt(dlgRectangle.getTxtWidth().getText()),Integer.parseInt(dlgRectangle.getTxtHeight().getText()), dlgRectangle.getBtnOutlineColor().getBackground(), dlgRectangle.getBtnInnerColor().getBackground());
						
					CmdAddShape cmdAddShape = new CmdAddShape(model, rectangle);
					cmdAddShape.execute();
						
					addCommandInStack(cmdAddShape); 
					stackRedo.removeAllElements();
					
					logList.addElement(cmdAddShape.logText());
					
					frame.getBtnRedo().setEnabled(false);	
				}
				catch (Exception e1) {
					JOptionPane.showMessageDialog(frame, "Width and height must be greater than 0!", null, JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
		}
			
		if(frame.getState() == 5) {
			DlgDonut dlgDonut = new DlgDonut();
			dlgDonut.getTxtX().setText(Integer.toString(e.getX())); 
			dlgDonut.getTxtX().disable();
			dlgDonut.getTxtY().setText(Integer.toString(e.getY()));
			dlgDonut.getTxtY().disable();
			dlgDonut.getBtnInnerColor().setBackground(frame.getBtnInterior().getBackground());
			dlgDonut.getBtnOutlineColor().setBackground(frame.getBtnEdge().getBackground());
			dlgDonut.setVisible(true);
				
			if(dlgDonut.isOk()) {
				try {
					Donut donut = new Donut(new Point(e.getX(), e.getY()), Integer.parseInt(dlgDonut.getTxtR().getText()), Integer.parseInt(dlgDonut.getTxtInnerR().getText()), dlgDonut.getBtnOutlineColor().getBackground(), dlgDonut.getBtnInnerColor().getBackground());
						
					CmdAddShape cmdAddShape = new CmdAddShape(model, donut);
					cmdAddShape.execute();
					
					addCommandInStack(cmdAddShape); 
					stackRedo.removeAllElements();
					
					logList.addElement(cmdAddShape.logText());
					
					frame.getBtnRedo().setEnabled(false);			
				}
				catch (Exception e1) {
					JOptionPane.showMessageDialog(frame, "Radius must be greater than 0!", null, JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}	
		}
			
		if(frame.getState() == 6) {
			DlgHexagon dlgHexagon = new DlgHexagon();
			dlgHexagon.getTxtX().setText(Integer.toString(e.getX())); 
			dlgHexagon.getTxtX().disable();
			dlgHexagon.getTxtY().setText(Integer.toString(e.getY()));
			dlgHexagon.getTxtY().disable();
			dlgHexagon.getBtnOutlineColor().setBackground(frame.getBtnEdge().getBackground());
			dlgHexagon.getBtnInnerColor().setBackground(frame.getBtnInterior().getBackground());
			dlgHexagon.setVisible(true);
			
			if(dlgHexagon.isOk()) {
				try {
					HexagonAdapter hexagon = new HexagonAdapter(new Point(e.getX(),e.getY()),Integer.parseInt(dlgHexagon.getTxtRadius().getText()), dlgHexagon.getBtnInnerColor().getBackground(), dlgHexagon.getBtnOutlineColor().getBackground());
					
					CmdAddShape cmdAddShape = new CmdAddShape(model, hexagon);
					cmdAddShape.execute();
					
					addCommandInStack(cmdAddShape); 
					stackRedo.removeAllElements();
					
					logList.addElement(cmdAddShape.logText());
					
					frame.getBtnRedo().setEnabled(false);		
				} 
				catch (Exception e1) {
					JOptionPane.showMessageDialog(frame,
							"Width and height must be greater than 0!", null, JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}	
		}
		
		frame.getView().repaint();
	}
			
	public void delete() {
			
		for(int i = selectedShapes.size()-1; i >= 0; i--) {
			if(selectedShapes.get(i) instanceof Point) {
				CmdRemoveShape cmdRemoveShape = new CmdRemoveShape(model, (Point)selectedShapes.get(i), selectedShapes);
				cmdRemoveShape.execute();
				
				addCommandInStack(cmdRemoveShape); 
				
				logList.addElement(cmdRemoveShape.logText());
				
				frame.getView().repaint();
		
			}
			else if(selectedShapes.get(i) instanceof Line) {
				CmdRemoveShape cmdRemoveShape = new CmdRemoveShape(model, (Line)selectedShapes.get(i), selectedShapes);
				cmdRemoveShape.execute();
					
				addCommandInStack(cmdRemoveShape); 
				
				logList.addElement(cmdRemoveShape.logText());
				
				frame.getView().repaint();
				
			} 
			else if(selectedShapes.get(i) instanceof Rectangle) {
				CmdRemoveShape cmdRemoveShape = new CmdRemoveShape(model, (Rectangle)selectedShapes.get(i), selectedShapes);
				cmdRemoveShape.execute();
				
				addCommandInStack(cmdRemoveShape); 
				
				logList.addElement(cmdRemoveShape.logText());
			
				frame.getView().repaint();
			
			} 
			else if(selectedShapes.get(i) instanceof Donut) {
				CmdRemoveShape cmdRemoveShape  = new CmdRemoveShape(model, (Donut)selectedShapes.get(i), selectedShapes);
				cmdRemoveShape.execute();
				
				addCommandInStack(cmdRemoveShape); 
				
				logList.addElement(cmdRemoveShape.logText());
					
				frame.getView().repaint();
				
			} 
			else if(selectedShapes.get(i) instanceof Circle) {
				CmdRemoveShape cmdRemoveShape = new CmdRemoveShape(model, (Circle)selectedShapes.get(i), selectedShapes);
				cmdRemoveShape.execute();
			  
				addCommandInStack(cmdRemoveShape); 
				
				logList.addElement(cmdRemoveShape.logText());
					
				frame.getView().repaint();
				
			} 
			else if(selectedShapes.get(i) instanceof HexagonAdapter) {
				CmdRemoveShape cmdRemoveShape  = new CmdRemoveShape(model, (HexagonAdapter)selectedShapes.get(i), selectedShapes);
				cmdRemoveShape.execute();
				
				addCommandInStack(cmdRemoveShape); 
				
				logList.addElement(cmdRemoveShape.logText());
					
				frame.getView().repaint();
				
			} 
		}
		checkButtons();
	}

	public void updateShapeClicked() {
		Shape shape = getSelectedShape();
		if(shape instanceof Point) {
			btnUpdatePointClicked((Point) shape); 
		}
		
		else if(shape instanceof Line) {
			btnUpdateLineClicked((Line) shape);
		}
		
		else if(shape instanceof Donut) {
			btnUpdateDonutClicked((Donut) shape);
		}
		
		else if(shape instanceof Circle) {
			btnUpdateCircleClicked((Circle) shape);
		}
		
		else if(shape instanceof Rectangle) { 
			btnUpdateRectangleClicked((Rectangle) shape);
		}
		else if(shape instanceof HexagonAdapter) {
			btnUpdateHexagonClicked((HexagonAdapter) shape);
		}
		
		frame.getView().repaint();
	}
	
	private void btnUpdatePointClicked(Point oldPoint) {
		DlgPoint dlgPoint = new DlgPoint();
		dlgPoint.getTxtX().setText(Integer.toString(oldPoint.getX())); 
		dlgPoint.getTxtY().setText(Integer.toString(oldPoint.getY()));
		dlgPoint.getBtnColor().setBackground(oldPoint.getColor());	
		
		dlgPoint.setVisible(true);
		if(dlgPoint.isConfirmed()) {
			int x = Integer.parseInt(dlgPoint.getTxtX().getText());
			int y = Integer.parseInt(dlgPoint.getTxtY().getText());
			Color color = dlgPoint.getBtnColor().getBackground();
			Point newPoint = new Point(x, y, color);
			
			CmdUpdatePoint cmdUpdatePoint = new CmdUpdatePoint(oldPoint, newPoint);
			cmdUpdatePoint.execute();
			
			addCommandInStack(cmdUpdatePoint); 
			stackRedo.removeAllElements();
			
			logList.addElement(cmdUpdatePoint.logText());
			
			frame.getBtnRedo().setEnabled(false);
		}
	}
	
	private void btnUpdateLineClicked(Line oldLine) {
		DlgLine dlgLine = new DlgLine();
		dlgLine.getTxtStartX().setText(Integer.toString(oldLine.getStartPoint().getX()));
		dlgLine.getTxtStartY().setText(Integer.toString(oldLine.getStartPoint().getY()));
		dlgLine.getTxtEndX().setText(Integer.toString(oldLine.getEndPoint().getX()));
		dlgLine.getTxtEndY().setText(Integer.toString(oldLine.getEndPoint().getY()));
		dlgLine.getBtnOutlineColor().setBackground(oldLine.getColor());
				
		dlgLine.setVisible(true);
		if(dlgLine.isOk()) {
			Line newLine =  new Line(new Point(Integer.parseInt(dlgLine.getTxtStartX().getText()), Integer.parseInt(dlgLine.getTxtStartY().getText())), new Point(Integer.parseInt(dlgLine.getTxtEndX().getText()), Integer.parseInt(dlgLine.getTxtEndY().getText())), dlgLine.getBtnOutlineColor().getBackground());
			
			CmdUpdateLine cmdUpdateLine = new CmdUpdateLine(oldLine, newLine);
			cmdUpdateLine.execute();
			
			addCommandInStack(cmdUpdateLine); 
			stackRedo.removeAllElements();
			
			logList.addElement(cmdUpdateLine.logText());
			
			frame.getBtnRedo().setEnabled(false);
		}	
	}
	
	private void btnUpdateDonutClicked(Donut oldDonut) {
		DlgDonut dlgDonut = new DlgDonut();
		dlgDonut.getTxtX().setText(Integer.toString(oldDonut.getCenter().getX())); 
		dlgDonut.getTxtY().setText(Integer.toString(oldDonut.getCenter().getY()));
		dlgDonut.getTxtInnerR().setText(Integer.toString(oldDonut.getInnerRadius()));
		dlgDonut.getTxtR().setText(Integer.toString(oldDonut.getRadius())); 
		dlgDonut.getBtnOutlineColor().setBackground(oldDonut.getColor());
		dlgDonut.getBtnInnerColor().setBackground(oldDonut.getInnerColor());
		
		dlgDonut.setVisible(true);
		if(dlgDonut.isOk()) {
			Donut newDonut = new Donut(new Point(Integer.parseInt(dlgDonut.getTxtX().getText()), Integer.parseInt(dlgDonut.getTxtY().getText())), Integer.parseInt(dlgDonut.getTxtR().getText()), Integer.parseInt(dlgDonut.getTxtInnerR().getText()), dlgDonut.getBtnOutlineColor().getBackground(), dlgDonut.getBtnInnerColor().getBackground());
			
			CmdUpdateDonut cmdUpdateDonut = new CmdUpdateDonut(oldDonut, newDonut);
			cmdUpdateDonut.execute();
			
			addCommandInStack(cmdUpdateDonut); 
			stackRedo.removeAllElements();
			
			logList.addElement(cmdUpdateDonut.logText());
			
			frame.getBtnRedo().setEnabled(false);
		}			
	}

	private void btnUpdateCircleClicked(Circle oldCircle) {
		DlgCircle dlgCircle = new DlgCircle();
		dlgCircle.getTxtX().setText(Integer.toString(oldCircle.getCenter().getX())); 
		dlgCircle.getTxtY().setText(Integer.toString(oldCircle.getCenter().getY()));
		dlgCircle.getTxtR().setText(Integer.toString(oldCircle.getRadius())); 
		dlgCircle.getBtnOutlineColor().setBackground(oldCircle.getColor());
		dlgCircle.getBtnInnerColor().setBackground(oldCircle.getInnerColor());
		
		dlgCircle.setVisible(true);
		if(dlgCircle.isOk()) {
			Circle newCircle = new Circle(new Point(Integer.parseInt(dlgCircle.getTxtX().getText()), Integer.parseInt(dlgCircle.getTxtY().getText())), Integer.parseInt(dlgCircle.getTxtR().getText()), dlgCircle.getBtnOutlineColor().getBackground(), dlgCircle.getBtnInnerColor().getBackground());			
			
			CmdUpdateCircle cmdUpdateCircle = new CmdUpdateCircle(oldCircle, newCircle);
			cmdUpdateCircle.execute();
			
			addCommandInStack(cmdUpdateCircle); 
			stackRedo.removeAllElements();
			
			logList.addElement(cmdUpdateCircle.logText());
			
			frame.getBtnRedo().setEnabled(false);
		}
	}

	private void btnUpdateRectangleClicked(Rectangle oldRectangle) {
		DlgRectangle dlgRectangle = new DlgRectangle();
		dlgRectangle.getTxtUpperLeftPointX().setText(Integer.toString(oldRectangle.getUpperLeftPoint().getX()));
		dlgRectangle.getTxtUpperLeftPointY().setText(Integer.toString(oldRectangle.getUpperLeftPoint().getY()));
		dlgRectangle.getTxtWidth().setText(Integer.toString(oldRectangle.getWidth()));
		dlgRectangle.getTxtHeight().setText(Integer.toString(oldRectangle.getHeight()));
		dlgRectangle.getBtnOutlineColor().setBackground(oldRectangle.getColor());
		dlgRectangle.getBtnInnerColor().setBackground(oldRectangle.getInnerColor());
		
		dlgRectangle.setVisible(true);
		if(dlgRectangle.isOk()) {
			Rectangle newRectangle = new Rectangle(new Point(Integer.parseInt(dlgRectangle.getTxtUpperLeftPointX().getText()), Integer.parseInt(dlgRectangle.getTxtUpperLeftPointY().getText())), Integer.parseInt(dlgRectangle.getTxtWidth().getText()), Integer.parseInt(dlgRectangle.getTxtHeight().getText()), dlgRectangle.getBtnOutlineColor().getBackground(), dlgRectangle.getBtnInnerColor().getBackground());
			
			CmdUpdateRectangle cmdUpdateRectangle = new CmdUpdateRectangle(oldRectangle, newRectangle);
			cmdUpdateRectangle.execute();
			
			addCommandInStack(cmdUpdateRectangle); 
			stackRedo.removeAllElements();
			
			logList.addElement(cmdUpdateRectangle.logText());
			
			frame.getBtnRedo().setEnabled(false);
		}	
	}
	
	private void btnUpdateHexagonClicked(HexagonAdapter oldHexagon) {
		DlgHexagon dlgHexagon = new DlgHexagon();
		dlgHexagon.getTxtX().setText(Integer.toString(oldHexagon.getHexagon().getX())); 
		dlgHexagon.getTxtY().setText(Integer.toString(oldHexagon.getHexagon().getY()));
		dlgHexagon.getTxtRadius().setText(Integer.toString(oldHexagon.getHexagon().getR())); 
		dlgHexagon.getBtnOutlineColor().setBackground(oldHexagon.getHexagon().getBorderColor());
		dlgHexagon.getBtnInnerColor().setBackground(oldHexagon.getHexagon().getAreaColor());
		
		dlgHexagon.setVisible(true);
		if(dlgHexagon.isOk()) {
			HexagonAdapter newHexagon = new HexagonAdapter(new Point(Integer.parseInt(dlgHexagon.getTxtX().getText()), Integer.parseInt(dlgHexagon.getTxtY().getText())),Integer.parseInt(dlgHexagon.getTxtRadius().getText()), dlgHexagon.getBtnInnerColor().getBackground(), dlgHexagon.getBtnOutlineColor().getBackground());
			
			CmdUpdateHexagon cmdUpdateHexagon = new CmdUpdateHexagon(oldHexagon, newHexagon);
			cmdUpdateHexagon.execute();
			
			addCommandInStack(cmdUpdateHexagon); 
			stackRedo.removeAllElements();
			
			logList.addElement(cmdUpdateHexagon.logText());
			
			frame.getBtnRedo().setEnabled(false);
		}
	}
	
	public void toFront() {
		if(selectedShapes.size() != 1)
			return;
		Shape shape = selectedShapes.get(0);
		
		CmdToFront cmdToFront = new CmdToFront(model, shape);
		cmdToFront.execute();
		
		addCommandInStack(cmdToFront); 
		
		logList.addElement(cmdToFront.logText());
		
		frame.getBtnRedo().setEnabled(false);
		
		frame.getView().repaint();	
		checkButtons();
	}
	
	public void toBack() {
		if(selectedShapes.size() != 1)
			return;
		Shape shape = selectedShapes.get(0);
		
		CmdToBack cmdToBack = new CmdToBack(model, shape);
		cmdToBack.execute();
		
		addCommandInStack(cmdToBack); 
		
		logList.addElement(cmdToBack.logText());
		
		frame.getBtnRedo().setEnabled(false);
		
		frame.getView().repaint();	
		checkButtons();
	}
	
	public void bringToFront() {
		if(selectedShapes.size() != 1)
			return;
		Shape shape = selectedShapes.get(0);
		
		CmdBringToFront cmdBringToFront = new CmdBringToFront(model, shape);
		cmdBringToFront.execute();
		
		addCommandInStack(cmdBringToFront); 
		
		logList.addElement(cmdBringToFront.logText());

		frame.getBtnRedo().setEnabled(false);
		
		frame.getView().repaint();
		checkButtons();
	}

	public void bringToBack() {
		if(selectedShapes.size() != 1)
			return;
		Shape shape = selectedShapes.get(0);
		
		CmdBringToBack cmdBringToBack = new CmdBringToBack(model, shape);
		cmdBringToBack.execute();
		
		addCommandInStack(cmdBringToBack); 
		
		logList.addElement(cmdBringToBack.logText());
		
		frame.getBtnRedo().setEnabled(false);
	
		frame.getView().repaint();
		checkButtons();
	}
	
	public void checkButtons() {
		
		if(selectedShapes.size() != 0)
		{
			if(selectedShapes.size() == 1)
			{
				observable.setBtnUpdateActivated(true);
				btnUpdate();	
			} 
			else {
				observable.setBtnUpdateActivated(false);
				observable.setBtnBringToBackActivated(false);
				observable.setBtnBringToFrontActivated(false);
				observable.setBtnToBackActivated(false);
				observable.setBtnToFrontActivated(false);
			}
			observable.setBtnDeleteActivated(true);
		} 
		else {
			observable.setBtnUpdateActivated(false);
			observable.setBtnDeleteActivated(false);
			observable.setBtnBringToBackActivated(false);
			observable.setBtnBringToFrontActivated(false);
			observable.setBtnToBackActivated(false);
			observable.setBtnToFrontActivated(false);
		}
	}
	
	public void btnUpdate() {
	
		ListIterator<Shape> it = model.getShapes().listIterator();
		while(it.hasNext())
		{
			selected = it.next();
			if(selected.isSelected()) {
				if(model.getShapes().size() != 1) {
					if(selected.equals(model.get(model.getShapes().size() - 1))) { 
						observable.setBtnBringToBackActivated(true);
						observable.setBtnBringToFrontActivated(false);
						observable.setBtnToBackActivated(true);
						observable.setBtnToFrontActivated(false);
					} 
					else if (selected.equals(model.get(0))) { 
						observable.setBtnBringToBackActivated(false);
						observable.setBtnBringToFrontActivated(true);
						observable.setBtnToBackActivated(false);
						observable.setBtnToFrontActivated(true);
					} 
					else { 
						observable.setBtnBringToBackActivated(true);
						observable.setBtnBringToFrontActivated(true);
						observable.setBtnToBackActivated(true);
						observable.setBtnToFrontActivated(true);
					}
				}
			}
		}
	}
	
	public void undo() {
		
			logList.addElement("Undo->" + stackUndo.peek().logText());
			
			CmdUndoRedo.undo(stackUndo, stackRedo);
			
			if(stackUndo.isEmpty() && !stackRedo.isEmpty()) {
				frame.getBtnUndo().setEnabled(false);
				frame.getBtnRedo().setEnabled(true);
			} 
			
			else if(!stackUndo.isEmpty() && !stackRedo.isEmpty()) {
					frame.getBtnUndo().setEnabled(true);
					frame.getBtnRedo().setEnabled(true);
			} 
			
			frame.getView().repaint();
			
			checkButtons();	
	}
	
	public void redo() {
		
			logList.addElement("Redo->" + stackRedo.peek().logText());
			
			CmdUndoRedo.redo(stackUndo, stackRedo);
			if(stackRedo.isEmpty() && !stackUndo.isEmpty()) {
				frame.getBtnUndo().setEnabled(true);
				frame.getBtnRedo().setEnabled(false);
			} 
			
			else if(!stackRedo.isEmpty() && !stackUndo.isEmpty()) {
					frame.getBtnUndo().setEnabled(true);
					frame.getBtnRedo().setEnabled(true);
			}
			
			frame.getView().repaint();
		
			checkButtons();
	}
	
	public void addCommandInStack(Command command) { 
		 stackUndo.push(command); 
		
		if(!stackUndo.isEmpty()) {
			frame.getBtnUndo().setEnabled(true);
		}
	}
	
	public void save() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
	    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY); 
		chooser.enableInputMethods(false);
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileHidingEnabled(false);
		chooser.setEnabled(true);
		chooser.setDialogTitle("Save");
		chooser.setAcceptAllFileFilterUsed(false);
		if (!model.getAll().isEmpty()) {
			chooser.setFileFilter(new FileNameExtensionFilter("Serialized draw", "ser"));
		}
		if (!stackUndo.isEmpty()) chooser.setFileFilter(new FileNameExtensionFilter("Commands log", "log"));
		if(chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			if (chooser.getFileFilter().getDescription().equals("Serialized draw")) manager = new ManagerFile(new SerializableFile(model));
			else if (chooser.getFileFilter().getDescription().equals("Commands log")) manager = new ManagerFile(new LogFile(frame, model, this));
			else manager = new ManagerFile(new SaveDraw(frame));
			manager.save(chooser.getSelectedFile());
		}
		chooser.setVisible(false);
	}
	
	public void open() {
		JFileChooser chooser = new JFileChooser();
		chooser.enableInputMethods(true);
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileHidingEnabled(false);
		chooser.setEnabled(true);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
		chooser.setFileFilter(new FileNameExtensionFilter("Serialized draw", "ser"));
		chooser.setFileFilter(new FileNameExtensionFilter("Commands log", "log"));
		if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			model.removeAll();
			logList.removeAllElements();
			stackRedo.clear();
			stackUndo.clear();
			frame.getView().repaint();
			if (chooser.getFileFilter().getDescription().equals("Serialized draw")) {
				manager = new ManagerFile(new SerializableFile(model));
				propertyChangeSupport.firePropertyChange("serialized draw opened", false, true);
			}
			else if (chooser.getFileFilter().getDescription().equals("Commands log")) manager = new ManagerFile(new LogFile(frame, model, this));
			manager.open(chooser.getSelectedFile());
		}	
		chooser.setVisible(false);
	}
	
	
	
	public void executeCommand(Command command) {
		command.execute();
		stackUndo.push(command);
		
		if (!stackRedo.isEmpty()) {
			stackRedo.removeAllElements();
			propertyChangeSupport.firePropertyChange("redo turn off", false, true);
		}
		
		if (model.getAll().isEmpty()) propertyChangeSupport.firePropertyChange("Don't exist", false, true);
		else if (model.getAll().size() == 1) propertyChangeSupport.firePropertyChange("Exist", false, true);
		
		if (stackUndo.isEmpty()) propertyChangeSupport.firePropertyChange("Draw is empty.", false, true);
		else if (stackUndo.size() == 1) propertyChangeSupport.firePropertyChange("Draw is not empty.", false, true);
		frame.getView().repaint();
	}
	
	public void newDraw() {
		if(JOptionPane.showConfirmDialog(null, "Are you sure you want to start a new drawing?", "Warning", JOptionPane.YES_NO_OPTION) == 0) {	
			model.removeAll();
			logList.removeAllElements();
			stackRedo.clear();
			stackUndo.clear();
			propertyChangeSupport.firePropertyChange("draw is empty", false, true);
			frame.getView().repaint();
		}
	}
	
	private Shape getSelectedShape() {
		Iterator<Shape> iterator = model.getAll().iterator();
		while(iterator.hasNext()) {
			Shape shapeForModification = iterator.next();
			if(shapeForModification.isSelected())
				return shapeForModification;
		}
		return null;
	}

	public List<Shape> getSelectedShapes() {
		return selectedShapes;
	}
	
}

