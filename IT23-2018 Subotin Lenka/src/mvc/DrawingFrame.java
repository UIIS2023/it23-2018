 package mvc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;



public class DrawingFrame  extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private JButton btnEdge;
	private JButton btnInterior;

	private JToggleButton btnSelect;
	private JToggleButton btnUpdate;
	private JToggleButton btnDelete;
	
	private JButton btnBringToFront;
	private JButton btnToFront;
	private JButton btnBringToBack;
	private JButton btnToBack;
	
	private JButton btnUndo;
	private JButton btnRedo;

	private DrawingController controller;
	private DrawingView view = new DrawingView();
	private int state = 0;
	private DefaultListModel<String> dlmList;
	final JList<String> list;
	final JScrollPane scrollPane; 
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem btnSave;
	private JMenuItem btnOpen;
	private JMenuItem btnClearView;
	
	public DrawingFrame() {
		setTitle("Lenka Subotin IT23/2018");
		
		dlmList = new DefaultListModel<String>();
		//NORTH
		JPanel north = new JPanel();
		north.setBackground(Color.RED);
		getContentPane().add(north, BorderLayout.NORTH);
								
		btnEdge = new JButton("Edge color");
		btnEdge.setBackground(Color.BLACK);
		btnEdge.setForeground(Color.WHITE);
		btnEdge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnEdge.setBackground(JColorChooser.showDialog(null, "Choose edge color: ", Color.BLUE));
				if(btnEdge.getBackground().equals(Color.BLACK)) {
					btnEdge.setForeground(Color.WHITE);
				}
				
			}
		});
		north.add(btnEdge);
		
		btnInterior = new JButton("Interior color");
		btnInterior.setBackground(Color.WHITE);
		btnInterior.setForeground(Color.BLACK);
		btnInterior.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnInterior.setBackground(JColorChooser.showDialog(null, "Choose interior color: ", Color.RED));
				if(btnInterior.getBackground().equals(Color.WHITE)) {
					btnInterior.setForeground(Color.BLACK);
				}
			}
		});
		north.add(btnInterior);
		
		JRadioButton btnPoint = new JRadioButton("Point");
		btnPoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(1);
			}
		});
		north.add(btnPoint);
				
		JRadioButton btnLine = new JRadioButton("Line");
		btnLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(2);
			}
		});
		north.add(btnLine);
		
		JRadioButton btnCircle = new JRadioButton("Circle");
		btnCircle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(3);
			}
		});
		north.add(btnCircle);
		
		JRadioButton btnRectangle = new JRadioButton("Rectangle");
		btnRectangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(4);
			}
		});
		north.add(btnRectangle);
		
		JRadioButton btnDonut = new JRadioButton("Donut");
		btnDonut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(5);
			}
		});
		north.add(btnDonut);
		
		JRadioButton btnHexagon = new JRadioButton("Hexagon");
		btnHexagon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(6);
			}
		});
		north.add(btnHexagon);
		
		//SOUTH
		JPanel south = new JPanel();
		south.setBackground(Color.RED);
		getContentPane().add(south, BorderLayout.SOUTH);
			
		btnSelect = new JToggleButton("Select");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(7);		
			}
		});
		south.add(btnSelect);
		
		btnUpdate = new JToggleButton("Update");
		btnUpdate.setEnabled(false);
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.updateShapeClicked(); 
			}
		});
		south.add(btnUpdate);
		
		btnDelete = new JToggleButton("Delete");
		btnDelete.setEnabled(false);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.delete();
			}
		});
		south.add(btnDelete);
		
		btnBringToFront = new JButton("BringToFront");
		btnBringToFront.setEnabled(false);
		btnBringToFront.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.bringToFront();
			}
		});
		
		south.add(btnBringToFront);
		
		btnToFront = new JButton("ToFront");
		btnToFront.setEnabled(false);
		btnToFront.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.toFront(); 
			}
		});
		
		south.add(btnToFront);
		
		btnBringToBack = new JButton("BringToBack");
		btnBringToBack.setEnabled(false);
		btnBringToBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.bringToBack();
			
			}
		});
		
		south.add(btnBringToBack);
		
		btnToBack = new JButton("ToBack");
		btnToBack.setEnabled(false);
		btnToBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.toBack();
			}
		});
		
		south.add(btnToBack);
		
		btnUndo = new JButton("Undo");
		btnUndo.setEnabled(false);
		btnUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.undo();
			}
		});
		
		south.add(btnUndo);
		
		btnRedo = new JButton("Redo");
		btnRedo.setEnabled(false);
		btnRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.redo();
			}
		});
		
		south.add(btnRedo);
		
		scrollPane = new JScrollPane();
		list = new JList<String>(); 
		list.setBackground(new Color(255, 248, 220)); 
		list.setEnabled(false); 
		list.setModel(dlmList); 
		scrollPane.setViewportView(list);
		
		south.add(scrollPane);
		
		
		view.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.mouseClicked(e);
			}
		});
		
		getContentPane().add(view, BorderLayout.CENTER);
		view.setBackground(Color.WHITE);
		
		ButtonGroup group = new ButtonGroup();
		group.add(btnDonut);
		group.add(btnHexagon);
		group.add(btnCircle);
		group.add(btnPoint);
		group.add(btnLine);
		group.add(btnRectangle);
		
		group.add(btnSelect);
		group.add(btnDelete);
		group.add(btnUpdate);
		group.add(btnToFront);
		group.add(btnBringToFront);
		group.add(btnToBack);
		group.add(btnBringToBack);
		group.add(btnUndo);
		group.add(btnRedo);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menu = new JMenu("Menu");
		menuBar.add(menu);
		
		btnSave = new JMenuItem("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.save();
			}
		});
		menu.add(btnSave);
		
		btnOpen = new JMenuItem("Open");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.open();
			}
		});
		menu.add(btnOpen);
		
		btnClearView = new JMenuItem("Clear view");
		btnClearView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.newDraw();
			}
		});
		menu.add(btnClearView);
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public DrawingController getController() {
		return controller;
	}
	
	public void setController(DrawingController controller) {
		this.controller = controller;
	}
	
	public DrawingView getView() {
		return view;
	}
	
	public void setView(DrawingView view) {
		this.view = view;
	}
	
	public JToggleButton getBtnSelect() {
		return btnSelect;
	}
	
	public void setBtnSelect(JToggleButton btnSelect) {
		this.btnSelect = btnSelect;
	}

	public JToggleButton getBtnUpdate() {
		return btnUpdate;
	}

	public void setBtnUpdate(JToggleButton btnUpdate) {
		this.btnUpdate = btnUpdate;
	}

	public JToggleButton getBtnDelete() {
		return btnDelete;
	}

	public void setBtnDelete(JToggleButton btnDelete) {
		this.btnDelete = btnDelete;
	}

	public JButton getBtnEdge() {
		return btnEdge;
	}

	public void setBtnEdge(JButton btnEdge) {
		this.btnEdge = btnEdge;
	}

	public JButton getBtnInterior() {
		return btnInterior;
	}

	public void setBtnInterior(JButton btnInterior) {
		this.btnInterior = btnInterior;
	}

	public JButton getBtnBringToFront() {
		return btnBringToFront;
	}

	public void setBtnBringToFront(JButton btnBringToFront) {
		this.btnBringToFront = btnBringToFront;
	}

	public JButton getBtnToFront() {
		return btnToFront;
	}

	public void setBtnToFront(JButton btnToFront) {
		this.btnToFront = btnToFront;
	}

	public JButton getBtnBringToBack() {
		return btnBringToBack;
	}

	public void setBtnBringToBack(JButton btnBringToBack) {
		this.btnBringToBack = btnBringToBack;
	}

	public JButton getBtnToBack() {
		return btnToBack;
	}

	public void setBtnToBack(JButton btnToBack) {
		this.btnToBack = btnToBack;
	}

	public JButton getBtnUndo() {
		return btnUndo;
	}

	public void setBtnUndo(JButton btnUndo) {
		this.btnUndo = btnUndo;
	}

	public JButton getBtnRedo() {
		return btnRedo;
	}

	public void setBtnRedo(JButton btnRedo) {
		this.btnRedo = btnRedo;
	}
	
	public DefaultListModel<String> getDlmList() {
		return dlmList;
	}

	public void setDlmList(DefaultListModel<String> dlmList) {
		this.dlmList = dlmList;
	}
}
