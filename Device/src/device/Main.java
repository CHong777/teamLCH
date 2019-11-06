package device;

public class Main {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 try {
	            (new Serial()).connect("COM8");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	}
}
