//liuqi
import java.util.HashMap;
import java.util.Map;

public class DeviceList {
	public Map<Integer, Object> deviceList = new HashMap<Integer, Object>();
	public static int currentDeviceId = 0;
	public int id;

	public static int getCurrentDeviceId() {
		return currentDeviceId;
	}

	public static void setCurrentDeviceId(int currentDeviceId) {
		DeviceList.currentDeviceId = currentDeviceId;
	}

	public Object getDevice(int id) {
		return deviceList.get(id);
	}

	public void addDevice(Object o) {
		deviceList.put(currentDeviceId, o);
		currentDeviceId++;
	}
}
