package Command;

import java.io.IOException;
import java.io.OutputStream;

public class Socket {
	static final String AT = "AT*ICT*SOCKET";
	
	OutputStream out;
	
	/* socket_type */
	public static final int TCP_Socket = 1;
	public static final int UDP_Socket = 2;
	public static final int reserved = 3;
	public static final int TCP_SSL_Client_Socket = 4;

	/* parameter */
	int socket_type;

	public Socket(OutputStream out) throws IOException {
		
		/*
		 * Format
		 * 		AT*ICT*SOCKET=<socket_type> : This command is used to open socket.
		 * 
		 * Response
		 * 		OK<socket_descriptor> or Error
		 * 
		 * 		Valid socket descriptor
		 * 		0~5(max 6 socket descriptor, TCP: 3/UDP: 3)
		 * 		6(TCP SSL Client socket descriptor)
		 * 
		 */
		
		this.out = out;
	}
	
	public void run() throws IOException {
		
		socket_type = TCP_Socket;
		
		String command = AT + "=" + socket_type;

		byte buf[] = (command + "\r\n").getBytes();

		this.out.write(buf);
	}
}
