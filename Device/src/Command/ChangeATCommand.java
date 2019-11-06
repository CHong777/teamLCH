package Command;

import java.io.IOException;
import java.io.OutputStream;

public class ChangeATCommand {		//escape DataMode
	
static final String AT = "AT*ICT*SEND";
static final byte MAXIMUM = (byte) 1460;

OutputStream out;

/* parameter */
int socket_descriptor;
String ip_addr;
int rport;
byte size;
//char[] stream_data = new char[MAXIMUM];
String data;

public ChangeATCommand(OutputStream out) throws IOException {
	/*
	 * AT*ICT*SEND=<socket_descriptor> <ip_addr> <rport> <size> <stream_data>
	 *
	 * Example> AT*ICT*SEND=0 192.168.0.27 3005 5 Hello
	 * 
	 */
	this.out = out;

	// socket_descriptor = /* input descriptor value here*/;
	ip_addr = "192.168.0.16";
	rport = 3005;
	size = 10;
	data = "Hel";
	
}

public void run() throws IOException, InterruptedException {
		
	//String command = AT + "=" + socket_descriptor + " " + ip_addr + " " + rport + " " + size + " " + data;
	String cmmd ="+++";
	byte buf[] = (cmmd/*command*/ + "\r\n").getBytes();
	this.out.write(buf);
}
}
