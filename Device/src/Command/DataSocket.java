package Command;

import java.io.IOException;
import java.io.OutputStream;

public class DataSocket {

	static final String AT = "AT*ICT*DATA_SOCKET";

	/* socket_type */
	public static final int TCP_Client = 1;
	public static final int UDP_Client = 2;
	public static final int TCP_Server = 4;
	public static final int UDP_Server = 8;

	OutputStream out;

	/* parameters */
	int socket_type;
	String remote_ip;
	int remote_port;
	int local_port;

	public DataSocket(OutputStream out, int socket_type, String remote_ip, int remote_port, int local_port)
			throws IOException {

		/*
		 * AT*ICT*DATA_SOCKET=<socket_type> <remote_ip> <remote_port> <local_port>
		 *
		 * Example> 1)Create TCP client AT*ICT*DATA_SOCKET=1 192.168.0.2 60000 0<0x0D>
		 * 
		 * 2)Create TCP server AT*ICT*DATA_SOCKET=4 0 0 50000<0x0D>
		 * 
		 */

		this.out = out;
		this.socket_type = socket_type;
		this.remote_ip = remote_ip;
		this.remote_port = remote_port;
		this.local_port = local_port;

		/*
		 * socket_type = TCP_Client; remote_ip = "192.168.0.27"; remote_port = 3005;
		 * local_port = 0;
		 */
	}

	public void run() throws IOException {
		
		String DataMode ="AT*ICT*DATA_SOCKET=1 192.168.0.16 3005 0";
		byte buf2[] = (DataMode/*command*/ + "\r\n").getBytes();
		this.out.write(buf2);
	}

}
