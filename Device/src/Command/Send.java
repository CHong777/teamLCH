package Command;

import java.io.IOException;
import java.io.OutputStream;

import device.Protocol;

public class Send {
	static final String AT = "AT*ICT*SEND";
	static final byte MAXIMUM = (byte) 1460;

	OutputStream out;
	int humidity;
	int temperature;
	String order;

	
	/* parameter */
	int socket_descriptor;
	String ip_addr;
	int rport;
	byte size;
	//char[] stream_data = new char[MAXIMUM];
	String data;

	public Send(OutputStream out, int humidity, int temperature, String order) throws IOException {
		/*
		 * AT*ICT*SEND=<socket_descriptor> <ip_addr> <rport> <size> <stream_data>
		 *
		 * Example> AT*ICT*SEND=0 192.168.0.27 3005 5 Hello
		 * 
		 */
		this.out = out;
		this.humidity = humidity;
		this.temperature = temperature;
		this.order = order;

		// socket_descriptor = /* input descriptor value here*/;
		ip_addr = "192.168.0.16";
		rport = 3005;
		size = 10;
		data = "Hel";
		
		
	}
	
	public void run() throws IOException, InterruptedException {
			
		//String command = AT + "=" + socket_descriptor + " " + ip_addr + " " + rport + " " + size + " " + data;
		//String cmmd ="AT*ICT*SEND=0 192.168.0.16 3005 5 HelLLO";
		//int a=25;
		
		//byte buf[] = (cmmd/*command*/+ "\r\n").getBytes();
		//this.out.write(buf);
		
		
		Protocol protocol = new Protocol();
		protocol = new Protocol(Protocol.PT_RES_DEV_STATUS);
	
		protocol.setTemperature(temperature);
		protocol.setHumidity(humidity);

		if (protocol.getPacket() != null) {
			// System.out.println("ø¿¥ıππ¿” " + Order);
			this.out.write(protocol.getPacket());
		}
		
	}
}
