
public class Boots {
	public static void main(String args[]){
		//init registers
		PC.value = "000000000110";//pc12位; 内存中0-5这六个地址被reserved了，所以要从第六位开始
		R0.value = "0000000000000000";
		R1.value = "0000000000000000";
		R2.value = "0000000000000000";
		R3.value = "0000000000000000";
		X1.value = "0000000000000000";
		X2.value = "0000000000000000";
		X3.value = "0000000000000000";
		MAR.value = "0000000000000000";
		MDR.value = "0000000000000000";
		
		//init memory
		Memory.memoryList.clear();
		for(Integer i=0; i<2048; i++)//0-2047的地址空间，大小位2048
		{
			MemoryItem mi = new MemoryItem();
			mi.setAddress(ALU.addZero(Integer.toBinaryString(i), 16));//memoryitem的地址存的是16位二进制数
			mi.setValue("0000000000000000");
			Memory.memoryList.add(i, mi);
		}
		//init ISA
		ISA.autoSetISA();//将指令集集成到系统中
		//init frame
		MainFrame mf = new MainFrame();
		mf.runGUI();
		//print
//		MainFrame.refresh();
//		MainFrame.txtrConsole.append("\nRegisters initiated.");
//		MainFrame.txtrConsole.append("\nMemory initiated.");
//		MainFrame.txtrConsole.append("\nISA initiated.");

		
		
	}
}
