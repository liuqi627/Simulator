
public class Instruction {
	public static String command;//这是完整的指令，6+2+2+1+5=16位
	public static String opcode;//操作码，6位
	public static String RF;//GPRs的序号，2位
	public static String RX;//IndexRegister的序号，2位
	public static String RX1;//专门为了LDX和STX准备的，2位
	public static String ADDR;//地址，5位
	public static String I;//Indirect与否，1位
	public static String immed;//立即数，最多5位,放在ADDRESS的位置上，与Address位数一样
	public static String cc;//condition code，4位，overflow, underflow, division by zero, equal-or-not
	public static String rx;//2位
	public static String ry;//2位
	public static String DevID;//5位
	public static String AL;//1位
	public static String LR;//1位
	public static String Count;//4位
}
