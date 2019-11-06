package Command;

import java.io.IOException;
import java.io.OutputStream;

public class ChangeDataMode {
	OutputStream out;
	
	public ChangeDataMode(OutputStream out) {
		this.out = out;
	}
	
	public void run() throws InterruptedException {
		byte buf[];
		try {
			
			String DataMode ="AT*ICT*DATA_SOCKET=1 192.168.0.27 3005 0";
    		byte buf2[] = (DataMode/*command*/ + "\r\n").getBytes();
    		this.out.write(buf2);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
