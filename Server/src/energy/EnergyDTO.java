package energy;

public class EnergyDTO {

	private int Efficiency;
	private String Edate;

	public int getEfficiency() {
		return Efficiency;
	}

	public void setEfficiency(int efficiency) {
		Efficiency = efficiency;
	}

	public String getEdate() {
		return Edate;
	}

	public void setEdate(String edate) {
		Edate = edate;
	}

	public EnergyDTO() {
	}

	public EnergyDTO(int Efficiency, String Edate) {
		super();
		this.Efficiency = Efficiency;
		this.Edate = Edate;
	}
}