package adapter;

import java.awt.Color;
import java.awt.Graphics;

import geometry.Point;
import geometry.SurfaceShape;
import hexagon.Hexagon;

public class HexagonAdapter extends SurfaceShape {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Hexagon hexagon;
	
	public HexagonAdapter() {
		
	}
	
	public HexagonAdapter(Point startPoint, int radius) {
		
		hexagon = new Hexagon(startPoint.getX(), startPoint.getY(), radius);
	}
	
	public HexagonAdapter(Point center, int r, Color innerColor, Color color) {
		this.hexagon = new Hexagon(center.getX(), center.getY(), r);
		this.hexagon.setAreaColor(innerColor);
		this.hexagon.setBorderColor(color);
	}

	public HexagonAdapter(Hexagon hexagon) {
		this.hexagon = hexagon;
	}

	public void setHexagon(Point center, int r, Color innerColor, Color color) {
		this.hexagon = new Hexagon(center.getX(), center.getY(), r);
		this.hexagon.setAreaColor(innerColor);
		this.hexagon.setBorderColor(color);
		hexagon.setSelected(true);
	}

	@Override
	public void moveBy(int byX, int byY) {
		hexagon.setX(hexagon.getX() + byX); 	
	  	hexagon.setY(hexagon.getY() + byY);
	}

	@Override
	public int compareTo(Object o) {
		if(o instanceof Hexagon) {
			Hexagon h = (Hexagon) o;
			return (int) (hexagon.getR() - h.getR());
		}
		else
			return 0;
	}
	
	@Override
	public boolean contains(int x, int y) {
		return hexagon.doesContain(x, y);
	}
	
	@Override
	public void setSelected(boolean selected) {
		super.setSelected(selected);
		hexagon.setSelected(selected);
	}
	
	@Override
	public boolean isSelected() {
		return hexagon.isSelected();
	}
	
	public boolean equals(Object obj){
		if(obj instanceof HexagonAdapter){
			HexagonAdapter hexAdapter = (HexagonAdapter) obj;
			Point p1 = new Point(hexagon.getX(),hexagon.getY());
			Point p2 = new Point(hexAdapter.hexagon.getX(),hexAdapter.hexagon.getY());
			if(p1.equals(p2) && hexagon.getR() == hexAdapter.getHexagon().getR())
				return true;
			else
				return false;

		}
		else
			return false;
	}
	
	public HexagonAdapter clone() {
		HexagonAdapter ha = new HexagonAdapter(new Point(-1,-1),-1,Color.black, Color.black);
		
		ha.getHexagon().setX(this.getHexagon().getX());
		ha.getHexagon().setY(this.getHexagon().getY());
		ha.getHexagon().setR(this.getHexagon().getR());
		ha.getHexagon().setBorderColor(this.getHexagon().getBorderColor());
		ha.getHexagon().setAreaColor(this.getHexagon().getAreaColor());
		
		return ha;
	}
	
	public double area() {
		return hexagon.getR() * hexagon.getR() * Math.PI;
	}

	@Override
	public void draw(Graphics g) {
		hexagon.paint(g);
		
	}
	
	@Override
	public void fill(Graphics g) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public String toString() {
		return "Hexagon: radius=" + hexagon.getR() + "; x=" + hexagon.getX() + "; y=" + hexagon.getY() + "; edge color=" + getOutlineColor().toString().substring(14).replace('=', '-') + "; area color=" + getInnerColor().toString().substring(14).replace('=', '-');
	}
	
	public Hexagon getHexagon() {
		return hexagon;
	}
	
	public int getRadius() {
		return hexagon.getR();
	}
	
	public void setRadius(int radius) {
		hexagon.setR(radius);
	}
	
	public Point getCenter() {
		return new Point(hexagon.getX(),hexagon.getY());
	}
	
	public void setCenter(Point center) {
		hexagon.setX(center.getX());
		hexagon.setY(center.getY());
	}
	public Color getInnerColor() {
		return hexagon.getAreaColor();
	}

	public void setInnerColor(Color innerColor) {
		hexagon.setAreaColor(innerColor);
	}
	
	public Color getOutlineColor() {
		return hexagon.getBorderColor();
	}

	public void setOutlineColor(Color color) {
		hexagon.setBorderColor(color);
	}
}
