import java.util.ArrayList;


public class Cache {
	public static ArrayList<CacheLine> listCacheLine = new ArrayList<CacheLine>();

	public static String[][] cache = new String[16][3];
	public static String[] cachePipe = new String[4];
	public static String tag;
	public static int index;
	public static int dirty;
	public static String value;
	public static String addr;

	public static String getValue(int address) {
		addr = addZero(String.valueOf(address), 12);
		tag = addZero(Long.toHexString(Long.valueOf(addr.substring(0, 16),2)), 4);
		index = Integer.valueOf(addr.substring(16, 12),2);

		try {
			int flag = 1;
			for (int i = 0; i < 16; i += 4) {
				if (cache[i][0] != null) {
					if (cache[i][0].equals(tag)) {
						value = cache[i + index][2];
						flag = 0;
					}
				}
			}
			if (flag == 1) {
				value = Memory.memoryList.get(address).getValue();
				Cache.addValueByADDR(address, value);
			}
		} catch (java.lang.NullPointerException e) {
			value = Memory.memoryList.get(address).getValue();
			Cache.addValueByADDR(address, value);
		}
		return value;
	}

	public static void addValueByADDR(int address, String value) {
		addr = addZero(String.valueOf(address), 12);
		tag = addZero(Long.toHexString(Long.valueOf(addr.substring(0, 16),2)), 4);
		index = Integer.valueOf(addr.substring(16, 12),2);

		try {
			int flag = 1;
			// update, cache have address
			for (int i = 0; i < 16; i += 4) {
				
				if (cache[i][0] != null) {
					if (cache[i][0].equals(tag)) {
						cache[i + index][1] = "1";
						cache[i + index][2] = addZero(value, 12);
						flag = 0;
					}
				}
			}
			// update, cache do not have address
			if (flag == 1) {
				Memory.memoryList.get(address).setValue(value);
				int flag1 = 1;
				for (int k = 0; k < 16; k += 4) {

					// cache available
					if (cache[k][0] == null) {
						cachePipe[k / 4] = tag;
						cache[k][0] = tag;
						cache[k][1] = "0";
						cache[k][2] = Memory.memoryList.get(Integer.valueOf(addr.substring(0, 14) + "00")).getValue();
						cache[k + 1][0] = tag;
						cache[k + 1][1] = "0";
						cache[k + 1][2] = Memory.memoryList.get(Integer.valueOf(addr.substring(0, 14) + "01")).getValue();
						cache[k + 2][0] = tag;
						cache[k + 2][1] = "0";
						cache[k + 2][2] = Memory.memoryList.get(Integer.valueOf(addr.substring(0, 14) + "10")).getValue();
						cache[k + 3][0] = tag;
						cache[k + 3][1] = "0";
						cache[k + 3][2] = Memory.memoryList.get(Integer.valueOf(addr.substring(0, 14) + "11")).getValue();
						flag1 = 0;
						break;

					}
				}
				// cache not available
				if (flag1 == 1) {
					for (int k = 0; k < 16; k += 4) {
						if (cache[k][0].equals(cachePipe[0])) {
							// manage cache pipe
							for (int j = 0; j < 3; j++) {
								cachePipe[j] = cachePipe[j + 1];
							}
							cachePipe[3] = tag;
							// manage cache
							for (int j = 0; j < 4; j++) {
								if (cache[k + j][1].equals("1")) {
									Memory.memoryList.get(Integer.valueOf(Long.toBinaryString(Integer.valueOf((cache[k+ j][0]),16))
													+ addZero(Long.toBinaryString(j),2))).setValue(cache[k + j][2]);
								}
							}
							cache[k][0] = tag;
							cache[k][1] = "0";
							cache[k][2] = Memory.memoryList.get(Integer.valueOf(addr.substring(0, 14) + "00")).getValue();
							cache[k + 1][0] = tag;
							cache[k + 1][1] = "0";
							cache[k + 1][2] = Memory.memoryList.get(Integer.valueOf(addr.substring(0, 14) + "01")).getValue();
							cache[k + 2][0] = tag;
							cache[k + 2][1] = "0";
							cache[k + 2][2] = Memory.memoryList.get(Integer.valueOf(addr.substring(0, 14) + "10")).getValue();
							cache[k + 3][0] = tag;
							cache[k + 3][0] = tag;
							cache[k + 3][1] = "0";
							cache[k + 3][2] = Memory.memoryList.get(Integer.valueOf(addr.substring(0, 14) + "11")).getValue();
							break;
						}
					}
				}
			}

		} catch (java.lang.NullPointerException e) {

		}
	}

	private static String addZero(String binaryString, int length) {
		int l = binaryString.length();
		for (int i = 1; i <= (length - l); i++) {
			binaryString = "0" + binaryString;
		}
		return binaryString;
	}

	
}
