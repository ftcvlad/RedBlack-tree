
public class Node {

	Node left;
	Node right;
	Node previous;
	
	int number;
	boolean colorRed;
	
	public Node(int num, boolean color, Node previous){
		left=null;
		
		right=null;
		this.previous = previous;
		number=num;
		colorRed = color;
	}

}
