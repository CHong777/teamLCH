package Command;

import java.io.IOException;
import java.io.OutputStream;

public class Disconnect {

	static final String AT = "AT*ICT*DISCONNECT";

	OutputStream out;

	/* parameter */
	int socket_descriptor;
	String ip_addr;
	int rport;

	public Disconnect(OutputStream out) throws IOException {
		/*
		 * AT*ICT*DISCONNECT=<socket_descriptor> <ip_addr> <rport>
		 * 
		 */

		this.out = out;

		String command = AT + "=" + socket_descriptor + " " + ip_addr + " " + rport;

		byte buf[] = (command + "\r\n").getBytes();

		this.out.write(buf);
	}

}
