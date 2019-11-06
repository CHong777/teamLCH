package device;

public class DeviceDTO {

	private String deviceID;
	private String devuserID;
	private int mlTime;
	private int useruseTime;
	private int totalTime;
	private String Date;

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getDevuserID() {
		return devuserID;
	}

	public void setDevuserID(String devuserID) {
		this.devuserID = devuserID;
	}

	public int getMlTime() {
		return mlTime;
	}

	public void setMlTime(int mlTime) {
		this.mlTime = mlTime;
	}

	public int getUseruseTime() {
		return useruseTime;
	}

	public void setUseruseTime(int useruseTime) {
		this.useruseTime = useruseTime;
	}

	public int getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public DeviceDTO() {

	}

	public DeviceDTO(String deviceID, int mlTime, int useruseTime, int totalTime, String Date, String devuserID) {
		super();
		this.deviceID = deviceID;
		this.mlTime = mlTime;
		this.useruseTime = useruseTime;
		this.totalTime = totalTime;
		this.Date = Date;
		this.devuserID = devuserID;
	}
}