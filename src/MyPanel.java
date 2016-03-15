import java.awt.Graphics;

import java.awt.geom.Ellipse2D;
import java.awt.*;
import javax.swing.*;


public class MyPanel extends JPanel {

	
	
	@Override
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    Graphics2D g2d = (Graphics2D) g;

	    myTree.levelsVisited = new double[8];
	    myTree.printAllNodes(myTree.root, g2d, 15,1);
	    
	}
	
	
}
