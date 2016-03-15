import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class Main {
	static myTree tr =null;
	 static MyPanel drawPanel = new MyPanel();
	
	public static void main(String[] args) {
		tr = new myTree();


		
		
		
		tr.add(1, myTree.root, null);
		tr.add(2, myTree.root, null);
		tr.add(3, myTree.root, null);
		
	
	
		

		
		SwingUtilities.invokeLater(new Runnable() {
		     public void run() {
		        createGui();
		     }
		});
		
	}
	
	


	static void createGui(){
		
		
		JFrame frame =new JFrame ("Happy redBlack tree");
		frame.setSize(myTree.drawPanesWidth+100,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		//all panel
		Container allPane = frame.getContentPane();
		allPane.setLayout(new BorderLayout());
		
		//draw panel
		
		allPane.add(drawPanel, BorderLayout.CENTER);
		
		//info panel
		JPanel infoPanel = new JPanel(null);
		infoPanel.setPreferredSize(new Dimension(100,600));
		
		final JTextField addField = new JTextField();
		addField.setBounds(10,10,40,20);
		JButton addB = new JButton("add");
		addB.setBorder(null);
		addB.setBounds(60,10,30,20);

		final JTextField deleteField = new JTextField();
		deleteField.setBounds(10,40,40,20);
		JButton deleteB = new JButton("del");
		deleteB.setBorder(null);
		deleteB.setBounds(60,40,30,20);
		
		final JLabel messL = new JLabel();
		messL.setBounds(10,100,80,20);
		
		infoPanel.add(addField);
		infoPanel.add(addB);
		infoPanel.add(deleteField);
		infoPanel.add(deleteB);
		infoPanel.add(messL);
		
		infoPanel.setVisible(true);
		infoPanel.setBackground(Color.RED);
		allPane.add(infoPanel, BorderLayout.WEST);
		
		
		frame.setVisible(true);
		
		
		
		addB.addActionListener(new ActionListener(){
			
			
			public void actionPerformed(ActionEvent e){
				String str = addField.getText();
				addField.requestFocus();
				if (str.length()>3){
					messL.setText("too long num");
				}
				else{
					int nextNum =0;
					try{
						 nextNum = Integer.parseInt(str);//YOU should enter digits
					}
					catch(NumberFormatException nfe){
						messL.setText("bad input");
						return;
					}
					
					addField.setText("");
					
					if (tr.containsN(nextNum, myTree.root)!=null){
						
						messL.setText("already there");
					}
					else{
						tr.add(nextNum,  myTree.root, null);
						
						
						messL.setText(nextNum+" added");
						drawPanel.repaint();
					}
				}
				
			}
		
		});
		
		deleteB.addActionListener(new ActionListener(){
			
			
			public void actionPerformed(ActionEvent e){
				String str = deleteField.getText();
				deleteField.requestFocus();
				if (str.length()>3){
					messL.setText("too long num");
				}
				else{
					
					int delNum =0;
					try{
						delNum = Integer.parseInt(str);//YOU should enter digits
					}
					catch(NumberFormatException nfe){
						messL.setText("bad input");
						return;
					}
					
					
					
					deleteField.setText("");
					
					Node toDel = tr.containsN(delNum, myTree.root);
					if (toDel==null){
						messL.setText("not in tree");
					}
					else{
						tr.delete(delNum, toDel);
						
						messL.setText(delNum+" deleted");
					//	drawPanel.repaint();
					}
				}
				
				
			}
		
		});
		
		
		
	}

	
	
	
	
	
}