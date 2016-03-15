import java.awt.Graphics2D;
import java.awt.geom.*;
import java.awt.*;
import java.awt.font.*;
import javax.swing.Timer;
import java.awt.event.*;

public class myTree {


	static Node root;
	static int currDepth = 0;
	static double[] levelsVisited;
	static final int drawPanesWidth = 1000;//change this to change draw pane's width
	
	public myTree(){
		root = null;
	}
	
	
	void add(int num, Node current, Node previous){
		if (root==null){
			root = new Node(num, false, null);
			
			return;
		}
		else{
			if (num > current.number){
				if (current.right==null){
					current.right = new Node(num, true, current);	
					maintainRB(current.right);//starting from added node. maintaining separately from recursion as i am THE noob
				}
				else{
					add(num, current.right, current);
				}
			}
			else if (num<current.number){
				if (current.left==null){
					current.left = new Node(num, true, current);	
					maintainRB(current.left);
				}
				else{
					add(num, current.left, current);
				}
				
			}
		}
	}
	
	void maintainRB(Node current){
		
		while(current!=myTree.root && current.colorRed==true){
			
			
			if (current.previous.colorRed==false){
				break;//if black parent, everything is fine, do nothing
			}
			else{
				
				//1ST OPTION -- both children of a granddad are red, just recolor and continue from granddad. null == black
				if ((current.previous.previous.left!=null && current.previous.previous.left.colorRed==true)
						&&  (current.previous.previous.right!=null && current.previous.previous.right.colorRed==true)){
					current.previous.previous.left.colorRed =false;
					current.previous.previous.right.colorRed=false;
					if (current.previous.previous!=myTree.root){
						current.previous.previous.colorRed=true;
					}
					current = current.previous.previous;
					continue;
					
				}
				//2ND OPTION
				else{
					//OPTION A -- if come from granddad's right child
					if (current.previous.previous.right== current.previous){
						
						if (current==current.previous.left){//zig-zag --> do first right rotation
							
							Node temp = current.right;
							current.right = current.previous;
							current.previous = current.previous.previous;
							
							current.previous.right = current;
							
							current.right.left = temp;
							current.right.previous=current;
							
							if (temp!=null){
								temp.previous=current.right;
							}
						}
						else{
							current = current.previous;
						}
						//straight line --> rotate left
						Node temp = current.left;
						current.left = current.previous;
						current.previous = current.previous.previous;
						
						if (current.left.previous!=null){
							if (current.left.previous.right == current.left){
								current.left.previous.right = current;
							}
							else if (current.left.previous.left == current.left){
								current.left.previous.left = current;
							}
						}
						
						current.left.right= temp;
						current.left.previous=current;
						
						if (temp!=null){
							temp.previous=current.left;
						}
						
						current.colorRed=false;
						current.left.colorRed=true;
						
						//if current becomes root
						if (current.previous==null){
							myTree.root= current;
						}
					}
					
					
					//OPTION B -- if come from granddad's left child
					else if (current.previous.previous.left== current.previous){
						
						
						if (current==current.previous.right){//zig-zag --> do first left rotation
							
							Node temp = current.left;
							current.left = current.previous;
							current.previous = current.previous.previous;
							
							current.previous.left = current;
							
							current.left.right = temp;
							current.left.previous=current;
							
							if (temp!=null){
								temp.previous=current.left;
							}
						}
						else{
							current = current.previous;
						}

						//straight line --> rotate right
						Node temp = current.right;
						current.right = current.previous;
						current.previous = current.previous.previous;
						
						if (current.right.previous!=null){
							if (current.right.previous.right == current.right){
								current.right.previous.right = current;
							}
							else if (current.right.previous.left == current.right){
								current.right.previous.left = current;
							}
						}
						
						current.right.left = temp;
						current.right.previous=current;
						
						if (temp!=null){
							temp.previous=current.right;
						}
						
						current.colorRed=false;
						current.right.colorRed=true;
						
						if (current.previous==null){
							myTree.root= current;
						}
						
					}
					
				}//end of 2nd option
			}
		}
	}

	
	
	//narrows to case when either no children or 1 child
	void delete(int num, Node toDel){

			if (toDel.right==null && toDel.left==null){
				maintainRBdel(toDel);
			}
			else if (toDel.right==null || toDel.left==null){
				maintainRBdel(toDel);
			}
			else{//if has 2 children
				Node rightmostLeft = toDel.left;
				while(rightmostLeft.right!=null){
					rightmostLeft=rightmostLeft.right;
				}
				toDel.number = rightmostLeft.number;
				
				maintainRBdel(rightmostLeft);
			}
		
	}

	void maintainRBdel(Node curr){
		//http://www.geeksforgeeks.org/red-black-tree-set-3-delete-2/
		Node doubleBlack=null;
		
		//0 CHILDREN
		if (curr.left==null && curr.right==null){
			if (curr==myTree.root){
				myTree.root=null;
				Main.drawPanel.repaint();
				return;
			}
			//red
			if (curr.colorRed==true){
				if (curr.previous.right==curr){
					curr.previous.right=null;
				}
				else if (curr.previous.left==curr){
					curr.previous.left=null;
				}
			}
			//black
			else{
				doubleBlack = new Node(-100,false,curr.previous );
				if (curr.previous.right==curr){
					curr.previous.right=doubleBlack;
				}
				else if (curr.previous.left==curr){
					curr.previous.left=doubleBlack;
				}
				
			}
			
		}
		//1 CHILD
		else{
			if (curr==myTree.root){
				if (curr.left!=null ){
					myTree.root = curr.left;
				}
				else{
					myTree.root = curr.right;
				}
				myTree.root.colorRed=false;
				myTree.root.previous =null;
				Main.drawPanel.repaint();
				return;
			}
			
			Node potentiallyRecolor = null;
			if (curr.left!=null ){//has left child
				potentiallyRecolor = curr.left;
					if (curr.previous.right==curr){
						curr.previous.right= curr.left;
						curr.left.previous = curr.previous;
					}
					else if (curr.previous.left==curr){
						curr.previous.left= curr.left;
						curr.left.previous = curr.previous;
					}
			}
			else if (curr.right!=null){//has right child
				potentiallyRecolor = curr.right;
				if (curr.previous.right==curr){
					curr.previous.right= curr.right;
					curr.right.previous = curr.previous;
				}
				else if (curr.previous.left==curr){
					curr.previous.left= curr.right;
					curr.right.previous = curr.previous;
				}
			}
			
			
			if (curr.colorRed==true){
				//do nothing
			}
			//recolor if curr was black with 1 red child
			else if (curr.colorRed!=true && ((curr.left!=null && curr.left.colorRed==true) ||
					(curr.right!=null && curr.right.colorRed==true) ) ){
				potentiallyRecolor.colorRed=false;
			}
			//1 black child, black current
			else{
				doubleBlack = potentiallyRecolor;
				
			}
		}
		
		//ALL STUFF WITH DOUBLE BLACK
		killDoubleBlack(doubleBlack);
		
		
		
		Main.drawPanel.repaint();//rem
		
	}
		
		
		
	void killDoubleBlack(Node doubleBlack){
	
				if (doubleBlack!=null && doubleBlack!=myTree.root){
					
					Node sibling =null;
					if (doubleBlack.previous.left==doubleBlack){
						sibling = doubleBlack.previous.right;
					}
					else {
						sibling = doubleBlack.previous.left;
					}
					//after this sibling is NOT null
					
					
					//CASE A
					//-------------------------------------------------------------------------------
					//iii)
					if (sibling.colorRed==false && (sibling.previous.right==sibling) && sibling.right!=null && sibling.right.colorRed==true){
						
						rotateLeft(sibling);
						
						//recolor (this not on website, so hz
						if (sibling.left.colorRed==false){
							sibling.right.colorRed=false;
						}
						else if (sibling.left.colorRed==true){
							sibling.left.colorRed = false;
							sibling.colorRed=true;//upper X cannot be red
							sibling.right.colorRed = false;
						}
					}
					//i)
					else if (sibling.colorRed==false && (sibling.previous.left==sibling) && sibling.left!=null && sibling.left.colorRed==true){
						
						rotateRight(sibling);
						
						//recolor (this not on website, so hz
						if (sibling.right.colorRed==false){
							sibling.left.colorRed=false;
						}
						else{
							sibling.right.colorRed = false;
							sibling.colorRed=true;//upper X cannot be red
							sibling.left.colorRed = false;
						}			
					}
					//iv
					else if (sibling.colorRed==false && (sibling.previous.right==sibling) && sibling.left!=null && sibling.left.colorRed==true){
						
						rotateRight(sibling.left);
						sibling.colorRed = true;//tupo sledujem sajtu dlia sravnenija, ne vazhno, 4to potom obratno
						sibling.previous.colorRed = false;
						
						rotateLeft(sibling.previous);
						sibling.colorRed=false;
						if (doubleBlack.previous.colorRed==true){
							doubleBlack.previous.colorRed=false;
							sibling.previous.colorRed=true;
						}

					}
					//ii
					else if (sibling.colorRed==false && (sibling.previous.left==sibling) && sibling.right!=null && sibling.right.colorRed==true){
						
						rotateLeft(sibling.right);
						sibling.colorRed = true;//tupo sledujem sajtu dlia sravnenija, ne vazhno, 4to potom obratno
						sibling.previous.colorRed = false;
						
						rotateRight(sibling.previous);
						sibling.colorRed=false;
						if (doubleBlack.previous.colorRed==true){
							doubleBlack.previous.colorRed=false;
							sibling.previous.colorRed=true;
						}
					}
					//CASE B --------------------------------------------------------------------------------------------------------------
					else if(sibling.colorRed==false &&  (sibling.right==null || sibling.right.colorRed==false) &&
																				(sibling.left==null || sibling.left.colorRed==false) ){
						
						sibling.colorRed=true;
						if (sibling.previous.colorRed==true){
							sibling.previous.colorRed = false;
						}
						else{
							killDoubleBlack(sibling.previous);
							
						}
						
					}
					//CASE C --------------------------------------------------------------------------------------------------------------
					else if (sibling.colorRed==true){
						//ii -- sibling is right child
						if (sibling.previous.right==sibling){
							rotateLeft(sibling);
							sibling.colorRed = false;
							sibling.left.colorRed = true;
							killDoubleBlack(doubleBlack);
						}
						//i
						else {
							rotateRight(sibling);
							sibling.colorRed = false;
							sibling.right.colorRed = true;
							killDoubleBlack(doubleBlack);
						}
					}
					
					
				//these two are sometimes done when it is not needed...whatever
					
				//make sure root is the top element
					while(myTree.root.previous!=null){
						myTree.root = myTree.root.previous;
					}
					
					
					
					//doubleBlack is representing null, turn it to null
					if (doubleBlack.number==-100){
						if (doubleBlack.previous.left==doubleBlack){
							doubleBlack.previous.left=null;
						}
						else if (doubleBlack.previous.right==doubleBlack){
							doubleBlack.previous.right=null;
						}
					}
				}//end of double black nodes
	}
		
		
		
	void rotateRight(Node lower){
		
		Node temp  = lower.right;
		lower.right = lower.previous;
		lower.previous= lower.right.previous;
		
		lower.right.left = temp;
		if (temp!=null){
			lower.right.left.previous = lower.right;
		}
		lower.right.previous=lower;
		
		if (lower.previous!=null){
			if (lower.previous.right==lower.right){
				lower.previous.right=lower;
			}
			else if (lower.previous.left==lower.right){
				lower.previous.left=lower;
			}
		}
		
	}
	
	void rotateLeft(Node lower){
		
		Node temp  = lower.left;
		lower.left = lower.previous;
		lower.previous= lower.left.previous;
		
		lower.left.right = temp;
		if (temp!=null){
			lower.left.right.previous = lower.left;
		}
		lower.left.previous=lower;
		
		if (lower.previous!=null){
			if (lower.previous.right==lower.left){
				lower.previous.right=lower;
			}
			else if (lower.previous.left==lower.left){
				lower.previous.left=lower;
			}
		}
		
		
	}
		

	Node containsN(int num, Node current){
		
		if (current!=null){
			Node result = null;
			if (num<current.number){
				result = containsN(num, current.left);
			}
			else if (num>current.number){
				result = containsN(num, current.right);
			}
			else{
				return current;
			}
			return result;
		}
		else{
			return null;
		
		}
	}
	
	static void printAllNodes(Node current, Graphics2D g2d, int radius, int position){
		
		currDepth++;
		
		double distance = drawPanesWidth / (Math.pow(2,currDepth-1)+1);
		double centerX = distance*position;
		double centerY = currDepth*3*radius;
		
		if (current==null){
			currDepth--;
			//g2d.fill(new Ellipse2D.Double( centerX-15,centerY-15,30,30));
			
		}
		else{				
				printAllNodes(current.left, g2d, radius,position*2-1);
				
				//draw circle
				if (current.colorRed==true){
					g2d.setColor(Color.RED);
				}
				else{
					g2d.setColor(Color.BLACK);
				}
				
				Ellipse2D.Double ell = new Ellipse2D.Double( centerX-radius,centerY-radius,radius*2,radius*2);
				g2d.fill(ell);
				
				//draw number
				g2d.setColor(Color.WHITE);
			    Font font = new Font("Serif",Font.BOLD, 15);
			   
			    FontRenderContext frc = g2d.getFontRenderContext();
			    TextLayout layout = new TextLayout(current.number+"", font, frc);
			   
			    layout.draw(g2d,(float)(distance*position-layout.getAdvance()/2.0),(float) (currDepth*3*radius+5));
			    
			    //draw line
			    g2d.setColor(Color.BLUE);
			    double distanceNext = drawPanesWidth / (Math.pow(2,currDepth)+1);
			   
			    double centerNextLeftX = (position*2-1)*distanceNext;
			    double centerNextRightX = (position*2)*distanceNext;
			    double centerNextY = (currDepth+1)*3*radius;
			    
			    //for left link
			    double hypot = Math.sqrt(Math.pow(centerX-centerNextLeftX,2)+Math.pow(centerY-centerNextY,2));
			    double alpha = Math.asin((centerX-centerNextLeftX)/hypot);
			    
			    double yDiff = Math.cos(alpha)*radius; 
			    double xDiff = Math.sin(alpha)*radius;
			    g2d.draw(new Line2D.Double(centerX-xDiff, centerY+yDiff, centerNextLeftX+xDiff, centerNextY-yDiff));//to the left
				
			    //for right link
			     hypot = Math.sqrt(Math.pow(centerX-centerNextRightX,2)+Math.pow(centerY-centerNextY,2));
			     alpha = Math.asin((centerNextRightX-centerX)/hypot);
			    
			     yDiff = Math.cos(alpha)*radius; 
			     xDiff = Math.sin(alpha)*radius;
		
			    g2d.draw(new Line2D.Double(centerX+xDiff, centerY+yDiff,centerNextRightX-xDiff, centerNextY-yDiff));//to the right
				
				
				printAllNodes(current.right,g2d,  radius, position*2);
				currDepth--;
		}
	}//end of printAllNodes

}
