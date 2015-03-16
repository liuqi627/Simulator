import java.util.HashMap;
import java.util.Map;


//only a hashmap where key is operation name and value the opcode
public class ISA {
	public static Map<String, String> ISAList = new HashMap<String, String>();

	public static void autoSetISA(){
		// lOAD AND STORE
		ISA.addISAList("LDR", "000001");
		ISA.addISAList("STR", "000010");
		ISA.addISAList("LDA", "000011");
		ISA.addISAList("LDX", "101001");
		ISA.addISAList("STX", "101010");
		// Arithmetic and Logical
		ISA.addISAList("AMR", "000100");
		ISA.addISAList("SMR", "000101");
		ISA.addISAList("AIR", "000110");
		ISA.addISAList("SIR", "000111");
		ISA.addISAList("MLT", "010100");
		ISA.addISAList("DVD", "010101");
		ISA.addISAList("TRR", "010110");
		ISA.addISAList("AND", "010111");
		ISA.addISAList("ORR", "011000");
		ISA.addISAList("NOT", "011001");
		// Transfer
		ISA.addISAList("JZ", "001010");
		ISA.addISAList("JNE", "001011");
		ISA.addISAList("JCC", "001100");
		ISA.addISAList("JMA", "001101");
		ISA.addISAList("JSR", "001110");
		ISA.addISAList("RFS", "001111");
		ISA.addISAList("SOB", "010000");
		ISA.addISAList("JGE", "010001");
		// IO
		ISA.addISAList("IN", "111101");
		ISA.addISAList("OUT", "111110");
		ISA.addISAList("CHK", "111111");
		// Shift and Rotate
		ISA.addISAList("SRC", "011111");//031
		ISA.addISAList("RRC", "100000");
	}
	public static void addISAList(String str, String str1) {
		ISAList.put(str, str1);
	}

	public static String getISAList(String str) {
		
		return ISAList.get(str);
	}

}
