
public class HuffNode {
	byte character;
	int freq;
	HuffNode leftChild;
	HuffNode rightChild;

	
	public HuffNode() {
		character = 0;
		freq = 0;
		leftChild = null;
		rightChild = null;
	}
	
	public HuffNode(byte character, int freq) {
		this.character = character;
		this.freq = freq;
		leftChild = null;
		rightChild = null;
	}
	
	
	public HuffNode(int freq, HuffNode left, HuffNode right) {
		character = 0;
		this.freq = freq;
		leftChild = left;
		rightChild = right;
		
	}
	public HuffNode(byte character, int freq, HuffNode left, HuffNode right) {
		this.character = character;
		this.freq = freq;
		leftChild = left;
		rightChild = right;
	}
	public boolean isLeaf() {
		return leftChild==null && rightChild==null;
	}
	
}
