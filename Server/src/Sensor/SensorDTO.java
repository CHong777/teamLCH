package Sensor;

public class SensorDTO {

	private int Temperature;
	private int Humidity;
	private String SDate;

	public int getTemperature() {
		return Temperature;
	}

	public void setTemperature(int temperature) {
		Temperature = temperature;
	}

	public int getHumidity() {
		return Humidity;
	}

	public void setHumidity(int humidity) {
		Humidity = humidity;
	}

	public String getSDate() {
		return SDate;
	}

	public void setSDate(String sDate) {
		SDate = sDate;
	}

	public SensorDTO() {
	}

	public SensorDTO(int Temperature, int Humidity, String SDate) {
		super();
		this.Temperature = Temperature;
		this.Humidity = Humidity;
		this.SDate = SDate;
	}
}