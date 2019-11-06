package Command;

import java.io.IOException;
import java.io.OutputStream;
/* 192.168.0.27 255.255.255.0 192.168.0.254*/
public class TCPClient {
	OutputStream out;
	
	public TCPClient(OutputStream out) {
		this.out = out;
	}
	
	public void run() throws InterruptedException {
		byte buf[];
		try {
			
			buf = "AT*ICT*IPCONFIG=1\r\n".getBytes();
			//this.out.write(buf);

			buf = "AT*ICT*SCONN=EH717 0318041176600\r\n".getBytes();
			//this.out.write(buf);
			
			buf = "AT*ICT*SOCKET=1\r\n".getBytes();
			//this.out.write(buf);

			buf = "AT*ICT*CONNECT=0 192.168.0.16 3001\r\n".getBytes();
			//this.out.write(buf);
			
			buf = "AT*ICT*DATA_SOCKET=1 192.168.0.27 3001 0\r\n".getBytes();
			this.out.write(buf);
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
