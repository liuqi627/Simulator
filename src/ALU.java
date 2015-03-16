




//liuqi
public class ALU {
	public static boolean execute(){
//		System.out.println("进来execute了");
		String rf = Instruction.RF;
		String rx = Instruction.RX;
		String i = Instruction.I;
		String addr = Instruction.ADDR;
		String immediate = Instruction.immed;
		String cc = Instruction.cc;
		String devid = Instruction.DevID;
		String regX = Instruction.rx;
		String regY = Instruction.ry;
		String rx1 = Instruction.RX1;
		String al = Instruction.AL;
		String count = Instruction.Count;
		String lr = Instruction.LR;
		
		switch (Instruction.opcode) {
		case "000001":
			return LDR(rf, rx, i, addr);
		case "000010":
			return STR(rf, rx, i, addr);
		case "000011":
			return LDA(rf, rx, i, addr);
		case "101001":
			return LDX(rx1, rx, i, addr);
		case "101010":
			return STX(rx1, rx, i, addr);
		case "000100":
			return AMR(rf, rx, i, addr);
		case "000101":
			return SMR(rf, rx, i, addr);
		case "000110":
			return AIR(rf, immediate);
		case "000111":
			return SIR(rf, immediate);
		case "010100":
			return MLT(regX, regY);
		case "010101":
			return DVD(regX, regY);
		case "010110":
			return TRR(regX, regY);
		case "010111":
			return AND(regX, regY);
		case "011000":
			return ORR(regX, regY);
		case "011001":
			return NOT(regX);
		case "001010":
			return JZ(rf, rx, i, addr);
		case "001011":
			return JNE(rf, rx, i, addr);
		case "001100":
			return JCC(cc, rx, i, addr);
		case "001101":
			return JMA(rx, i, addr);
		case "001110":
			return JSR(rx, i, addr);
		case "001111":
			return RFS(immediate);
		case "010000":
			return SOB(rf, rx, i, addr);
		case "010001":
			return JGE(rf, rx, i, addr);
		case "111101":
			return IN(rf, devid);
		case "111110":
			return OUT(rf, devid);
		case "011111":
			return SRC(rf,al,lr,count);
		case "100000":
			return RRC(rf,al,lr,count);
		default:
			break;
		}
		return false;
	}
	
	private static boolean LDR(String RF, String RX, String I, String addr) {

		Integer ea = getEA(RX, I, addr);
		//System.out.println("内存ea中存的是：" + Memory.memoryList.get(ea).getValue());
		String mData = Memory.memoryList.get(ea).getValue();//内存里的东西都是二进制的string,寄存器里存的是二进制string

		setValueToRegisterById(RF, mData);
		// pc++
		PC.pcPlusOne();
		return true;
	}
	private static boolean STR(String RF, String RX, String I, String addr) {
		Integer ea = getEA(RX, I, addr);
		String rfData = getValueFromRegisterById(RF);
		Memory.memoryList.get(ea).setValue(rfData);
		// pc++
		PC.pcPlusOne();
		MainFrame.txtrConsole.append("\nPC <- PC + 1");
		return true;
	}
	private static boolean LDA(String RF, String RX, String I, String addr) {
		Integer ea = getEA(RX, I, addr);//获取ea，ea是十进制int
		String ea_str_bi = Integer.toBinaryString(ea);//把ea转化为二进制string
		String str = addZero(ea_str_bi,16);//把ea补满零
		setValueToRegisterById(RF, str);
		// pc++
		PC.pcPlusOne();
		return true;
	}
	private static boolean LDX(String RX1, String RX, String I, String addr) {
		Integer ea = getEA(RX, I, addr);
		String mData = Memory.memoryList.get(ea).getValue();//获取到了内存中的数据,内存16位，indexRegister12位
		setValueToIndexById(RX1, Integer.toString(Integer.valueOf(mData, 2)));//防止位数不同的后果，此处IndexRegister中的数并不一定填满12位
		// pc++
		PC.pcPlusOne();
		return true;
	}
	private static boolean STX(String RX1, String RX, String I, String addr) {//store index register to memory

		Integer ea = getEA(RX, I, addr);
		String rxData = addZero(getValueFromIndexById(RX1), 16);
		Memory.memoryList.get(ea).setValue(rxData);
		// pc++
		PC.pcPlusOne();
		return true;
	}
	private static boolean AMR(String RF, String RX, String I, String addr) {

		Integer ea = getEA(RX, I, addr);
		String mData = Memory.memoryList.get(ea).getValue();
		int result;
		result = Integer.parseInt(getValueFromRegisterById(RF)) + Integer.parseInt(mData, 2);
		setValueToRegisterById(RF, Integer.toBinaryString(result));
		PC.pcPlusOne();
		return true;
		
	}
	private static boolean SMR(String RF, String RX, String I, String addr) {
		Integer ea = getEA(RX, I, addr);
		String mData = Memory.memoryList.get(ea).getValue();
		int result;
		result = Integer.parseInt(getValueFromRegisterById(RF)) - Integer.parseInt(mData, 2);
		setValueToRegisterById(RF, Integer.toBinaryString(result));
		PC.pcPlusOne();
		return true;
	}
	private static boolean SIR(String RF, String immediate) {
		int result;
		result = Integer.parseInt(getValueFromRegisterById(RF)) - Integer.valueOf(immediate, 2);
		setValueToRegisterById(RF, addZero(Integer.toBinaryString(result), 16));
		PC.pcPlusOne();
		return true;
	}
	private static boolean AIR(String RF, String immediate) {//add immediate to register
		int result;
		result = Integer.parseInt(getValueFromRegisterById(RF)) + Integer.valueOf(immediate, 2);
		setValueToRegisterById(RF, addZero(Integer.toBinaryString(result), 16));
		PC.pcPlusOne();
		return true;
		
	}
	private static boolean MLT(String regX, String regY) {//乘积
		int rxInt = Integer.parseInt(getValueFromRegisterById(regX));
		int ryInt = Integer.parseInt(getValueFromRegisterById(regY));
		int result = rxInt * ryInt;
		if(result>2147483647 || result<-2147483648)
			CC.ccArray[0]=1;//overflow
		String result_binary = addZero(Integer.toBinaryString(result), 32);
		switch (regX) {
		case ("00"):
			setValueToRegisterById("00",result_binary.substring(0, 16));
			setValueToRegisterById("01",result_binary.substring(16, 32));
			break;
		case ("10"):
			setValueToRegisterById("10",result_binary.substring(0, 16));
			setValueToRegisterById("11",result_binary.substring(18, 36));
			break;
		default:
			return false;
		}
		PC.pcPlusOne();
		return true;
	}
	private static boolean DVD(String regX, String regY) {
		int x = Integer.parseInt(getValueFromRegisterById(regX));
		int y = Integer.parseInt(getValueFromRegisterById(regY));
		if (y != 0) {

			int quotient = x / y;
			int remainder = x % y;

			setValueToRegisterById(regX, Integer.toString(quotient));
			switch (regX) {
			case ("00"):
				setValueToRegisterById("01", Integer.toString(remainder));
				break;
			case ("10"):
				setValueToRegisterById("11", Integer.toString(remainder));
				break;
			default:
				return false;
			}
			CC.ccArray[2] = 0;
		} else
			CC.ccArray[2] = 1;
		// pc++
		PC.pcPlusOne();
		return true;
	}
	private static boolean TRR(String regX, String regY) {//寄存器两个数相等
		int x = Integer.parseInt(getValueFromRegisterById(regX));
		int y = Integer.parseInt(getValueFromRegisterById(regY));
		if (x == y)
			CC.ccArray[3] = 1;
		else
			CC.ccArray[3] = 0;
		// pc++
		PC.pcPlusOne();
		return true;
	}
	private static boolean AND(String regX, String regY) {
		int x = Integer.parseInt(getValueFromRegisterById(regX));
		int y = Integer.parseInt(getValueFromRegisterById(regY));
		setValueToRegisterById(regX, Integer.toString(x & y));
		PC.pcPlusOne();
		return true;
	}
	private static boolean ORR(String regX, String regY) {
		int x = Integer.parseInt(getValueFromRegisterById(regX));
		int y = Integer.parseInt(getValueFromRegisterById(regY));
		setValueToRegisterById(regX, Integer.toString(x | y));
		PC.pcPlusOne();
		return true;
	}
	private static boolean NOT(String regX) {
		int x = Integer.parseInt(getValueFromRegisterById(regX));
		String xStr = Integer.toBinaryString(x);
		String yStr = "";
		for (int i = 0; i < xStr.length(); i++) {
			if (xStr.substring(i, i + 1).equals("0"))
				yStr = yStr + "1";
			else
				yStr = yStr + "0";
		}
		setValueToRegisterById(regX, Integer.toString(Integer.valueOf(yStr, 2)));
		PC.pcPlusOne();
		return true;
	}
	private static boolean JZ(String RF, String RX, String I, String addr) {
		Integer EA;
		String rfData = getValueFromRegisterById(RF);
		if (rfData.equals("error"))
			return false;

		try {
			EA = getEA(RX, I, addr);
		} catch (Exception e) {
			//MainFrame.screen.append("illegal address\n");
			return false;
		}
		if (rfData.equals("0000000000000000")) {
			PC.value = PC.addZero(Integer.toBinaryString(EA), 12);
		} else
			// pc++
			PC.pcPlusOne();
		return true;
	}
	private static boolean JNE(String RF, String RX, String I, String addr) {
		long EALong;
		String rfData = getValueFromRegisterById(RF);
		if (rfData.equals("error"))
			return false;

		try {
			EALong = getEA(RX, I, addr);
		} catch (Exception e) {
			//MainFrame.screen.append("illegal address\n");
			return false;
		}
		if (!rfData.equals("0")) {
			PC.value = PC.addZero(Long.toBinaryString(EALong), 18);
		} else
			PC.pcPlusOne();
		return true;
	}
	private static boolean JCC(String cc, String RX, String I, String addr) {
		long EALong;

		try {
			EALong = getEA(RX, I, addr);
		} catch (Exception e) {
			//MainFrame.screen.append("illegal address\n");
			return false;
		}
		int ccrValue = CC.ccArray[Integer.parseInt(cc)];
		if (ccrValue == 1) {
			PC.value = PC.addZero(Long.toBinaryString(EALong), 16);
		} else
			PC.pcPlusOne();
		return true;
	}
	private static boolean JMA(String RX, String I, String addr) {
		long EALong;

		try {
			EALong = getEA(RX, I, addr);
		} catch (Exception e) {
			//MainFrame.screen.append("illegal address\n");
			return false;
		}
		PC.value = PC.addZero(Long.toBinaryString(EALong), 16);
		return true;
	}
	private static boolean JSR(String RX, String I, String addr) {
		Integer EA;

		try {
			EA = getEA(RX, I, addr);
		} catch (Exception e) {
			//MainFrame.screen.append("illegal address\n");
			return false;
		}
		R3.value = addZero(Integer.toBinaryString(Integer.parseInt(PC.value,2)+1), 16);
		PC.value = addZero(Integer.toBinaryString(EA), 12);
		return true;
	}
	private static boolean RFS(String Immed) {
		R0.value = addZero(Immed, 16);
		PC.value = addZero(Integer.toBinaryString(Integer.parseInt(R3.value,2)), 12);
		return true;
	}
	private static boolean SOB(String RF, String RX, String I, String addr) {
		Integer EA;
		int rValue = Integer.parseInt(getValueFromRegisterById(RF));

		try {
			EA = getEA(RX, I, addr);
		} catch (Exception e) {
			//MainFrame.screen.append("illegal address\n");
			return false;
		}
		setValueToRegisterById(RF, addZero(Integer.toBinaryString(rValue - 1), 16));
		if (rValue > 0) {
			PC.value = addZero(Integer.toBinaryString(EA), 12);
		} else
			// pc++
			PC.pcPlusOne();
		return true;
	}
	private static boolean JGE(String RF, String RX, String I, String addr) {
		Integer EA;
		int rValue = Integer.parseInt(getValueFromRegisterById(RF),2);

		try {
			EA = getEA(RX, I, addr);
		} catch (Exception e) {
			//MainFrame.screen.append("illegal address\n");
			return false;
		}
		if (rValue >= 0) {
			PC.value = PC.addZero(Integer.toBinaryString(EA), 12);
		} else
			// pc++
			PC.pcPlusOne();
		return true;
	}
	private static boolean IN(String RF, String devid){
		int devidInt = Integer.valueOf(devid, 2);
		switch (devidInt) {
		case 0://keyboard
			setValueToRegisterById(RF, ConsoleKeyboard.value);
			break;
		case 2://CardReader
			setValueToRegisterById(RF, CardReader.value);
			break;
		default:
			return false;
		}
		return true;
	}
	private static boolean OUT(String RF, String devid){
		int devidInt = Integer.valueOf(devid, 2);
		switch (devidInt) {
		case 1://printer
			ConsolePrinter.value = getValueFromRegisterById(RF);
			break;
		default:
			return false;
		}
		return true;
	}
	private static boolean SRC(String RF,String al,String lr,String count){
		return false;
	}
	private static boolean RRC(String RF,String al,String lr,String count){
		return false;
	}
	
	private static Integer getEA(String RX, String I, String addr) {//这里返回的是int，参数中的addr是二进制string
		Integer ea = 0;//把有效地址存到l里，这里注意用的是十进制radix
		switch (I) {
		case "0"://直接寻址
			switch (RX) {
			case "00"://不使用indexRegister
				ea = Integer.valueOf(addr, 2);
				break;
			case "01"://一号indexRegister
				ea = Integer.parseInt(X1.value) + Integer.valueOf(addr, 2);//后面这个2表示第一个参数是二进制数(radix=2)，
				break;
			case "10"://二号indexRegister
				ea = Integer.parseInt(X2.value) + Integer.valueOf(addr, 2);
				break;
			case "11"://三号indexRegister
				ea = Integer.parseInt(X3.value) + Integer.valueOf(addr, 2);
				break;
			default:
				System.out.println("ALU ERROR");
				break;
			}
			break;//case"0"的break
		case "1"://间接寻址
			switch (RX) {
			case "00":
				Integer pre_ea = Integer.valueOf(addr,2);
				String in_pre_ea = Memory.memoryList.get(pre_ea).getValue();
				ea = Integer.parseInt(in_pre_ea, 2);
				break;
			case "01":
				Integer pre_ea1 = Integer.parseInt(X1.value) + Integer.valueOf(addr, 2);//先把ix和addr相加，得到pre_ea
				String in_pre_ea1 = Memory.memoryList.get(pre_ea1).getValue();//在内存中找到pre_ea对应的值，是个string，得转化成int
				ea = Integer.parseInt(in_pre_ea1, 2);//内存中存的是2进制的数，但是是以string出现的
				//ea = Integer.valueOf(in_pre_ea, 2);
				//Java源码中显示Integer.valueof(str,radix)调用的Integer.parseInt(str,radix),所以此处选择用parseInt,都一样
				break;
			case "10":
				Integer pre_ea2 = Integer.parseInt(X2.value) + Integer.valueOf(addr, 2);
				String in_pre_ea2 = Memory.memoryList.get(pre_ea2).getValue();//在内存中找到pre_ea对应的值，是个string，得转化成int
				ea = Integer.parseInt(in_pre_ea2, 2);//内存中存的是2进制的数，但是是以string出现的
				break;
			case "11":
				Integer pre_ea3 = Integer.parseInt(X3.value) + Integer.valueOf(addr, 2);
				String in_pre_ea3 = Memory.memoryList.get(pre_ea3).getValue();//在内存中找到pre_ea对应的值，是个string，得转化成int
				ea = Integer.parseInt(in_pre_ea3, 2);//内存中存的是2进制的数，但是是以string出现的
				break;
			default:
				System.out.println("ALU ERROR");
				break;
			}
			break;//case"1"的break
		default:
			System.out.println("ALU ERROR");
			break;
		}
		MAR.value = addZero(Integer.toBinaryString(ea), 16);
		MainFrame.txtrConsole.append("\nMAR <- EA");
		
		MDR.value = Memory.memoryList.get(Integer.parseInt(MAR.value, 2)).getValue();
		MainFrame.txtrConsole.append("\nMDR <- M(MAR)");
		
		return ea;
	}
	private static String getValueFromRegisterById(String id) {
		switch (id) {
		case "00":
			return R0.value;
		case "01":
			return R1.value;
		case "10":
			return R2.value;
		case "11":
			return R3.value;
		default:
			return "error";
		}
	}

	private static boolean setValueToRegisterById(String id, String value) {
		MainFrame.txtrConsole.append("\nRF[RFI] <- MDR");
		switch (id) {
		case "00":
			R0.value = value;
			return true;
		case "01":
			R1.value = value;
			return true;
		case "10":
			R2.value = value;
			return true;
		case "11":
			R3.value = value;
			return true;
		default:
			return false;
		}
	}

	private static boolean setValueToIndexById(String id, String value) {
		MainFrame.txtrConsole.append("\nXF[IX] <- MDR");
		switch (id) {
		case "01":
			X1.value = value;
			return true;
		case "10":
			X2.value = value;
			return true;
		case "11":
			X3.value = value;
			return true;
		default:
			return false;
		}
	}

	private static String getValueFromIndexById(String id) {
		switch (id) {
		case "01":
			return X1.value;
		case "10":
			return X2.value;
		case "11":
			return X3.value;
		default:
			return "error";
		}
	}
	
	public static String addZero(String binaryString, int length) {
		int l = binaryString.length();
		for (int i = 1; i <= (length - l); i++) {
			binaryString = "0" + binaryString;
		}
		return binaryString;
	}
}
