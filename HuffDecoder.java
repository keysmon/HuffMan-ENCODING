/* HuffDecoder.java

   Starter code for compressed file decoder. You do not have to use this
   program as a starting point if you don't want to, but your implementation
   must have the same command line interface. Do not modify the HuffFileReader
   or HuffFileWriter classes (provided separately).
   
   B. Bird - 03/19/2019
   (Add your name/studentID/date here)
*/

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;



public class HuffDecoder{

    private HuffFileReader inputReader;
    private BufferedOutputStream outputFile;

    /* Basic constructor to open input and output files. */
    public HuffDecoder(String inputFilename, String outputFilename) throws FileNotFoundException {
        inputReader = new HuffFileReader(inputFilename);
        outputFile = new BufferedOutputStream(new FileOutputStream(outputFilename));
    }


    public void decode() throws IOException{
        // build the huffman tree from the symbol table
    	HuffNode root = HuffTreeReBuilder();
    	HuffNode node;
        int direction = inputReader.readStreamBit();        //fifth bug change it to the outside of the while loop
    	while (true) {
            node = root;
    	
            
    		if (direction == -1) {       //forth bug && instead of || (change to ==-1
    			break;
    		}
    		while (!node.isLeaf()) {
    			if(direction == 0) {
    				node = node.leftChild;
                    
    			}else {
    				node = node.rightChild;
                    
    			}
                direction = inputReader.readStreamBit();
                
    		}
            byte symbol = node.character;
           
    		outputFile.write(symbol);
    	}
        inputReader.close();
        outputFile.close(); //third bug!!!!!!! forgot to close
        /* This is where actual decoding should happen. */

        /* The outputFile.write() method can be used to write individual bytes to the output file.*/
    }
    
    private HuffNode HuffTreeReBuilder() {
    	HuffNode root = new HuffNode();
    	HuffNode node = root;
    	HuffFileSymbol symbol;
    	while (true) {
    		symbol = inputReader.readSymbol();
    		if (symbol==null) {
    			break;
    		}
    		int[] bits = symbol.symbolBits;
    		for (int i =0;i<bits.length;i++) {
    			if (bits[i]==0) {
    				if(node.leftChild==null){
    					node.leftChild = new HuffNode();
                        
    				}
    				node = node.leftChild;
    			}else {
    				if(node.rightChild==null) {
    					node.rightChild = new HuffNode();
                        
    				}
    				node = node.rightChild;
    			}
    			if (i ==bits.length-1) {
    				node.character = symbol.symbol[0];
                   
    				
    			}
    		}	
    		 node = root;
    	}
              //second bug!!!
	        //first bug!!!
    	return root;
    }
   
    
    
 
 
    public static void main(String[] args) throws IOException{
        if (args.length != 2){
            System.err.println("Usage: java HuffDecoder <input file> <output file>");
            return;
        }
        String inputFilename = args[0];
        String outputFilename = args[1];

        try {
            HuffDecoder decoder = new HuffDecoder(inputFilename, outputFilename);
            decoder.decode();
        } catch (FileNotFoundException e) {
            System.err.println("Error: "+e.getMessage());
        }
    }
}
