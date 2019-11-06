package Command;

import java.io.IOException;
import java.io.OutputStream;

public class Connect {
	static final String AT = "AT*ICT*CONNECT";

	OutputStream out;

	/* parameter */
	int socket_descriptor;
	String ip_addr;
	int remote_port;

	public Connect(OutputStream out, int socket_descriptor, String ip_addr, int remote_port) throws IOException {
		/*
		 * Format AT*ICT*CONNECT=<socket_descriptor> <ip_addr> <rport>
		 * 
		 */
		this.out = out;
		this.socket_descriptor = socket_descriptor;
		this.ip_addr = ip_addr;
		this.remote_port = remote_port;
	}

	public void run() throws IOException {
		
		String command = AT + "=" + socket_descriptor+" "+ip_addr + " " + remote_port + " ";

		byte buf[] = (command + "\r\n").getBytes();

		this.out.write(buf);
	}

}
