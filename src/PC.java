
public class PC {
	//十二位
	public static String value= "000000000000";
	
	public static void pcPlusOne(){
		Integer result_in_int_decimal = Integer.parseInt(value,2)+1;
		String result_in_string_bi = Integer.toBinaryString(result_in_int_decimal);
		value = addZero(result_in_string_bi, 12);
		MainFrame.txtrConsole.append("\nPC <- PC + 1");
	}
	public static String addZero(String binaryString, int length) {
		int l=binaryString.length();
		for(int i=1;i<=(length-l);i++)
		{
			binaryString="0"+binaryString;
		}
		return binaryString;
	}
}
