
public class MemoryItem {
	private String address;
	private String value;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getValue() {
		return Util.addZero(value, 16);
	}
	public void setValue(String value) {
		this.value = Util.addZero(value, 16);
	}
	
}
