public class Helper{
	
	public static int immediate;
	
	public static boolean constructInstruction(String str){
		//拿到了用户的输入，先要转化成二进制的instruction
		
		String rawInstruction[] = str.split(" ");//用户输入的字符串
		System.out.println("这句指令split后有 "+rawInstruction.length+" 段");
		Instruction.opcode = ISA.getISAList(rawInstruction[0]);//字符串的开头必定是opcode，将获取到的操作转化成ISA里的二进制码并放到Instruction里
		//AIR,SIR
		if (rawInstruction[0].equals("AIR") || rawInstruction[0].equals("SIR")) //opcode R IMMED， IX 和 I 在这两条命令中不考虑
		{	
			if (!decodeRF(rawInstruction[1]))	return false;//这里注意，if括号里要用非，这样才能一旦发生错误才return，如果正确decode，应该继续执行下面的指令
			if (!decodeImmed(rawInstruction[2]))	return false;
			Instruction.command = Instruction.opcode + Instruction.RF + "000"
					+ Instruction.immed;
			return true;
		}
		//JCC
		else if(rawInstruction[0].equals("JCC")){		//opcode CC X ADDR [I]
			if(!decodeCC(rawInstruction[1])) 	return false;
			if(!decodeRX(rawInstruction[2])) 	return false;
			if(!decodeAddr(rawInstruction[3])) 	return false;
			if(rawInstruction.length == 5){//因为I位是可省略的，这里判断如果指明了I位那么rawInstruction会有5项
				if (!decodeI(rawInstruction[4]))	return false;
				}
			else {//如果没指明I，那么就是I=0
				Instruction.I = "0";
			}
			Instruction.command = Instruction.opcode + Instruction.cc 
					+Instruction.RX  + Instruction.I +  Instruction.ADDR;
			return true;
		}
		//AMR,SMR,LDR,STR,LDA,JZ,JNE,SOB,JGE
		else if (rawInstruction[0].equals("AMR") || rawInstruction[0].equals("SMR")		//opcode R X ADDR [I]
				|| rawInstruction[0].equals("LDR") || rawInstruction[0].equals("STR")|| rawInstruction[0].equals("LDA")
				|| rawInstruction[0].equals("JZ") || rawInstruction[0].equals("JNE")
				|| rawInstruction[0].equals("SOB") || rawInstruction[0].equals("JGE"))
		{
			if (!decodeRF(rawInstruction[1]))	return false;
			if (!decodeRX(rawInstruction[2]))	return false;
			if (!decodeAddr(rawInstruction[3]))	return false;
			if(rawInstruction.length == 5){//因为I位是可省略的，这里判断如果指明了I位那么rawInstruction会有5项
				if (!decodeI(rawInstruction[4]))	return false;
				}
			else {//如果没指明I，那么就是I=0
				Instruction.I = "0";
			}
			Instruction.command = Instruction.opcode + Instruction.RF
					+ Instruction.RX + Instruction.I + Instruction.ADDR;//注意这里不要把I和Address写颠倒了，机器指令中，I在前，Address在后
			return true;
		}
		//LDX,STX
		else if(rawInstruction[0].equals("LDX") || rawInstruction[0].equals("STX")){	//opcode X1 X2 ADDR [I]
			if(!decodeRX(rawInstruction[1])) 	return false;
			Instruction.RX1 = Instruction.RX;
			if(!decodeRX(rawInstruction[2])) return false;
			if(!decodeAddr(rawInstruction[3])) return false;
			if(rawInstruction.length == 5){//因为I位是可省略的，这里判断如果指明了I位那么rawInstruction会有4项
				if (!decodeI(rawInstruction[4]))	return false;
				}
			else {//如果没指明I，那么就是I=0
				Instruction.I = "0";
			}
			Instruction.command = Instruction.opcode + Instruction.RX1 + Instruction.RX + Instruction.I + Instruction.ADDR;
		}
		//JMA,JSR
		else if(rawInstruction[0].equals("JMA") || rawInstruction[0].equals("JSR")){ //opcode X address [i]
			if(!decodeRX(rawInstruction[1])) 	return false;
			if(!decodeAddr(rawInstruction[2])) return false;
			if(rawInstruction.length == 4){//因为I位是可省略的，这里判断如果指明了I位那么rawInstruction会有4项
				if (!decodeI(rawInstruction[3]))	return false;
				}
			else {//如果没指明I，那么就是I=0
				Instruction.I = "0";
			}
			Instruction.command = Instruction.opcode + "00" +Instruction.RX //补的这两个00用不着
					+ Instruction.I + Instruction.ADDR;
		}
		//RFS
		else if (rawInstruction[0].equals("RFS")){		//opcode Immed
			if(!decodeImmed(rawInstruction[1]))	return false;
			Instruction.command = Instruction.opcode + "00000" +Instruction.immed;//补的这几个00用不着，immed放在最后address的位置上
		}
		//IN,OUT
		else if(rawInstruction[0].equals("IN") || rawInstruction[0].equals("OUT")){	//opcode r devid
			if(!decodeRF(rawInstruction[1]))	return false;
			if(!decodeDevID(rawInstruction[2]))	return false;
			Instruction.command = Instruction.opcode  +Instruction.RF +"000" + Instruction.DevID;
		}
		//TRR,AND,ORR
		else if(rawInstruction[0].equals("TRR") || rawInstruction[0].equals("AND")		 	//opcode rx,ry
				|| rawInstruction[0].equals("ORR")){
			if((rawInstruction[1].equals("0") || rawInstruction[1].equals("2")) 
					&& (rawInstruction[2].equals("0") || rawInstruction[2].equals("2"))){
			if(!decodeRF(rawInstruction[1]))	return false;
			Instruction.rx = Instruction.RF;
			if(!decodeRF(rawInstruction[2])) 	return false;
			Instruction.ry = Instruction.RF;
			Instruction.command = Instruction.opcode +Instruction.rx + Instruction.ry + "000000";
		}
			else {
				MainFrame.txtrConsole.append("\nrx and ry should be 0 or 2!");
				return false;
			}
		}
		//MLT,DVD
		else if(rawInstruction[0].equals("MLT") || rawInstruction[0].equals("DVD")){	 	//opcode rx,ry
			if((rawInstruction[1].equals("0") || rawInstruction[1].equals("2")) 
					&& (rawInstruction[2].equals("0") || rawInstruction[2].equals("2"))){
				if(!decodeRF(rawInstruction[1]))	return false;
				Instruction.rx = Instruction.RF;
				if(!decodeRF(rawInstruction[2])) 	return false;
				Instruction.ry = Instruction.RF;
				Instruction.command = Instruction.opcode +Instruction.rx + Instruction.ry + "000000";
			}
			else {
				MainFrame.txtrConsole.append("\nrx and ry should be 0 or 2!");
				return false;
			}
		}
		//NOT
		else if(rawInstruction[0].equals("NOT")){		//op rx
			if(!decodeRF(rawInstruction[1])) 	return false;
			Instruction.command = Instruction.opcode + Instruction.RF + "00000000"; 
		}
		//SRC,RRC
		else if(rawInstruction[0].equals("SRC") || rawInstruction[0].equals("RRC")){		//opcode r count L/R A/L
			if(!decodeRF(rawInstruction[1])) 	return false;
			if(!decodeCount(rawInstruction[2])) 	return false;
			if(!decodeAL(rawInstruction[3])) 	return false;
			if(!decodeLR(rawInstruction[4])) 	return false;
			Instruction.command = Instruction.opcode + Instruction.RF +  Instruction.AL + Instruction.LR + "00" + Instruction.Count; 
		}
		return false;
		
	}
	
	public static boolean decodeCount(String Count){
		Instruction.Count = Integer.toBinaryString(Integer.parseInt(Count));
		return true;
	}
	
	public static boolean decodeAL(String AL){
		if(AL.equals("0"))
			Instruction.AL = "0";
		else
			Instruction.AL = "1";
		return true;
	}
	
	public static boolean decodeLR(String LR){
		if(LR.equals("0"))
			Instruction.LR = "0";
		else
			Instruction.LR = "1";
		return true;
	}
	
	public static boolean decodeDevID(String DevID) {
		int DevIDInt;
		String DevIDStr;

		try {
			DevIDInt = Integer.parseInt(DevID);
		} catch (Exception e) {
			return false;
		}
		if (DevIDInt >= 0 && DevIDInt <= 31) {
			DevIDStr = Integer.toBinaryString(DevIDInt);
			int l = DevIDStr.length();
			for (int i = 0; i < (5 - l); i++) {
				DevIDStr = "0" + DevIDStr;
			}
			Instruction.DevID = DevIDStr;
			return true;
		} else {
			//MainFrame.screen.append("DevID must be <=31 >=0\n");
			return false;
		}
	}
	
	public static boolean decodeCC(String cc) {
		int ccInt;
		try {
			 ccInt= Integer.parseInt(cc);
		} catch (Exception e) {
			return false;
		}
		if (ccInt<0 || ccInt>3) {
			//MainFrame.screen.append("cc must be <=3 >=0\n");
			return false;
		} else {
			String ccStr = Integer.toBinaryString(ccInt);
			int l = ccStr.length();
			for (int i = 0; i < (2 - l); i++) {
				ccStr = "0" + ccStr;
			}
			Instruction.cc = ccStr;
			return true;
		}
	}
	
	public static boolean decodeRF(String RF){
		switch (RF) {
		case "0":
			Instruction.RF = "00";
			return  true;
		case "1":
			Instruction.RF = "01";
			return  true;
		case "2":
			Instruction.RF = "10";
			return  true;
		case "3":
			Instruction.RF = "11";
			return  true;
		default:
			return false;
		}
	}

	
	public static boolean decodeImmed(String immed) {
		try {
			immediate = Integer.parseInt(immed);//先把十进制数的string转变为int
		} catch (Exception e) {
			return false;
		}
		if (immediate < 0 || immediate > 31) {
			//MainFrame.txtrConsole.append("immediate must be <=31 >=0\n");
			return false;
		} else {
			String immedStr = Integer.toBinaryString(immediate);//再把int转变为二进制的string
			//immediate最多五位二进制数，最低0到最高31，位数不足的前面补零
			int l = immedStr.length();
			for (int i = 0; i < (5 - l); i++) {
				immedStr = "0" + immedStr;
			}
			Instruction.immed = immedStr;
			return true;
		}
	}
	
	public static boolean decodeRX(String rx) {
		switch (rx) {
		case "0":
			Instruction.RX = "00";
			return true;
		case "1":
			Instruction.RX = "01";
			return true;
		case "2":
			Instruction.RX = "10";
			return true;
		case "3":
			Instruction.RX = "11";
			return true;
		default:
			return false;
		}
	}
	
	public static boolean decodeI(String indirect) {
		switch (indirect) {
		case "0":
			Instruction.I = "0";
			return true;
		case "1":
			Instruction.I = "1";
			return true;
		default:
			return false;
		}
	}
	
	public static boolean decodeAddr(String addr) {
		int addrInt;
		try {
			addrInt = Integer.parseInt(addr);
		}catch(Exception e){
			return false;
		}
		if (addrInt <= 31 && addrInt >= 0) {
			String addr1 = Integer.toBinaryString(addrInt);
			int l = addr1.length();
			for(int i=0; i<(5-l);i++){
				addr1 = "0"+addr1;
			}
			Instruction.ADDR = addr1;
			return true;
		}else{
			//MainFrame.txtrConsole.append("addr must be <=31 >=0\n");
			return false;
		}
	}
}
