package Command;

import java.io.IOException;
import java.io.OutputStream;

public class Close {
	static final String AT = "AT*ICT*CLOSE";

	OutputStream out;

	int socket_descriptor;

	public Close(OutputStream out) throws IOException {
		/*
		 * AT*ICT*CLOSE=<socket_descriptor> <ip_addr> <rport>
		 * 
		 */

		// socket_descriptor = /* input descriptor value here */; 
		this.out = out;
	}
	
	public void run() throws IOException, InterruptedException {
		//String command = AT + "=" + socket_descriptor + " " + ip_addr + " " + rport + " " + size + " " + data;
		String cmmd ="AT*ICT*CLOSE=0";
		byte buf[] = (cmmd/*command*/ + "\r\n").getBytes();
		this.out.write(buf);
	}

}
