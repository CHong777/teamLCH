package device;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import Command.*;
public class SerialWriter implements Runnable {

	OutputStream out;
	InputStream in;

	public SerialWriter(OutputStream out, InputStream in) {
		this.out = out;
		this.in = in;
	}

	public void run() {

		//byte buf[] = "AT*ICT*FACRESET=0\r\n".getBytes();	
		byte buf[] = "AT\r\n".getBytes();
		//this.out.write(buf);
		
		SerialTest ST = new SerialTest(out, in);
		ST.run();
	}
}
