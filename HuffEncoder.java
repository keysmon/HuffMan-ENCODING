/* HuffEncoder.java

   Starter code for compressed file encoder. You do not have to use this
   program as a starting point if you don't want to, but your implementation
   must have the same command line interface. Do not modify the HuffFileReader
   or HuffFileWriter classes (provided separately).
   
   B. Bird - 03/19/2019
   (Add your name/studentID/date here)
*/
import java.util.HashMap;
import java.io.*;
import java.util.TreeMap;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Map;
import java.util.LinkedList;
public class HuffEncoder{

    private BufferedInputStream inputFile;
    private HuffFileWriter outputWriter;
    public HuffEncoder(String inputFilename, String outputFilename) throws FileNotFoundException {
        inputFile = new BufferedInputStream(new FileInputStream(inputFilename));
        outputWriter = new HuffFileWriter(outputFilename);
    }


    static class HuffComparator implements Comparator <HuffNode>{			// comparator method for priority queue, 
    	public int compare (HuffNode x, HuffNode y) {
    		int diff =  x.freq - y.freq;
    		if (diff!=0) {			//return the difference of their frequency if it's non-zero
    			return diff;
    		}else {						// an else statement is inserted for cases where two nodes have the same frequency
    			return x.character - y.character;
    		}
    	}
    }
    
    
    public void encode() throws IOException{

        
        //You may want to start by reading the entire file into a list to make it easier
        //to navigate.
        LinkedList<Byte> input_bytes = new LinkedList<Byte>();
        for(int nextByte = inputFile.read(); nextByte != -1; nextByte = inputFile.read()){
            input_bytes.add((byte)nextByte);
        }
        
        //Suggested algorithm:

        //Compute the frequency of each input symbol. Since symbols are one character long,
        //you can simply iterate through input_bytes to see each symbol.
        
        TreeMap<Byte, Integer> freqTable = freqTableBuilder(input_bytes);	//take all bytes and store their frequency into a treeMap
        PriorityQueue<HuffNode> theQueue = pqBuilder(freqTable);	//using their frequncy to build a priority queue
        HuffNode root = HuffTreeBuilder(theQueue);	//using priorityQueue to build a tree and return the root of the tree
        HashMap<Byte,String> symbolTable = symbolTableBuilder(root); //build the symboltable from the huffman tree
        String s = compressor(symbolTable,input_bytes);
        
        for (int i=0;i<s.length();i++) {
        	outputWriter.writeStreamBit(Character.getNumericValue(s.charAt(i)));
        }
        outputWriter.close();
        //Build a prefix code for the encoding scheme (if using Huffman Coding, build a 
        //Huffman tree).
        
        //Write the symbol table to the output file

        //Call outputWriter.finalizeSymbols() to end the symbol table

        //Iterate through each input byte and determine its encode bitstring representation,
        //then write that to the output file with outputWriter.writeStreamBit()

        //Call outputWriter.close() to end the output file

    }
    
    //function takes Linkedlist of all bytes and store all all byte_key and their frequency and return a treeMap
    private TreeMap<Byte, Integer> freqTableBuilder(LinkedList<Byte> theList) {
    	TreeMap<Byte, Integer> freqMap = new TreeMap<Byte, Integer>();
    	for (Byte byte_key : theList) {
    		if(freqMap.get(byte_key)==null) {
    			freqMap.put(byte_key, 1);
    		}else {
    			freqMap.put(byte_key,freqMap.get(byte_key)+1);
    		}
    	}
    	return freqMap;
    	
    }
    // Convert frequency into a priority queue sorted by their frequency
    private PriorityQueue<HuffNode> pqBuilder(TreeMap<Byte, Integer> freqTree){
    	PriorityQueue<HuffNode> theQueue = new PriorityQueue<HuffNode>(new HuffComparator());
    	for (Map.Entry<Byte, Integer> item : freqTree.entrySet()) {
    		theQueue.add(new HuffNode(item.getKey(),item.getValue()));
    	}
    	
    	return theQueue;
    }
    //build branches then merge to get the HuffMan tree
    private HuffNode HuffTreeBuilder(PriorityQueue<HuffNode> theQueue) {
    	while (theQueue.size()>1) {
    		HuffNode x = theQueue.poll();
    		HuffNode y = theQueue.poll();
    		HuffNode currentNode = new HuffNode(x.freq + y.freq, x, y);
    		theQueue.add(currentNode);
    	}
    	
    	return theQueue.poll();
    }
    
    
    // build the symbol table by the Huffman tree
    
    
    
    private HashMap<Byte,String> symbolTableBuilder(HuffNode root){
    	
    	HashMap<Byte, String> symbolTable = new HashMap<Byte, String>();
    	tableBuilderHelper(root,symbolTable, "");
    	outputWriter.finalizeSymbols();
    	return symbolTable;
    }
    //helper method that uses recursion to find the path to each individual leaf nodes then append their path and byte into the symbol table
    private void tableBuilderHelper(HuffNode node,Map<Byte, String> symbolTable, String bin) {
    	if(!node.isLeaf()) {
    		tableBuilderHelper(node.leftChild, symbolTable, bin + "0");
    		tableBuilderHelper(node.rightChild, symbolTable, bin + "1");
    	}else {
    		symbolTable.put(node.character, bin);
    		int size = bin.length();
    		int[] bits = new int[size];
    		for (int i =0;i<size;i++) {
    			bits[i] = Character.getNumericValue(bin.charAt(i));
    		}
    	//	HuffFileSymbol symbol = new HuffFileSymbol(node.character, bits);
    		outputWriter.writeSymbol(new HuffFileSymbol(node.character, bits));
    		
    	}
    	return;
    }
    private static String compressor(HashMap<Byte,String> symbolTable, LinkedList<Byte> theList) {
    	StringBuilder s = new StringBuilder();
    	for (Byte byte_key : theList){
    		s.append(symbolTable.get(byte_key));
    	}
    	return s.toString();
    }
  
    
    
    public static void main(String[] args) throws IOException{
        if (args.length != 2){
            System.err.println("Usage: java HuffEncoder <input file> <output file>");
            return;
        }
        String inputFilename = args[0];
        String outputFilename = args[1];

        try{
            HuffEncoder encoder = new HuffEncoder(inputFilename, outputFilename);
            encoder.encode();
        } catch (FileNotFoundException e) {
            System.err.println("FileNotFoundException: "+e.getMessage());
        } catch (IOException e) {
            System.err.println("IOException: "+e.getMessage());
        }

    }
}

