
public class Util {
	public static String addZero(String binaryString, int length) {
		int l = binaryString.length();
		for (int i = 1; i <= (length - l); i++) {
			binaryString = "0" + binaryString;
		}
		return binaryString;
	}
}
