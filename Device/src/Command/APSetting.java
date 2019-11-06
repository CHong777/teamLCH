package Command;

import java.io.IOException;
import java.io.OutputStream;
/*192.168.0.27 255.255.255.0 192.168.0.254*/
public class APSetting {
	OutputStream out;

	public APSetting(OutputStream out) {
		this.out = out;
	}

	public void run() throws InterruptedException {
		byte buf[];
		try {

			buf = "AT*ICT*IPCONFIG=1\r\n".getBytes();
			this.out.write(buf);
			
			buf = "AT*ICT*CRYPTO=3 1 1\r\n".getBytes();
			this.out.write(buf);

			buf = "AT*ICT*PSK=0318041176600\r\n".getBytes();
			this.out.write(buf);

			buf = "AT*ICT*ASSOCIATE=EH717\r\n".getBytes();
			this.out.write(buf);

			
			/*buf = "AT*ICT*IPCONFIG=1\r\n".getBytes();
			this.out.write(buf);
	         
	        buf = "AT*ICT*SCONN=EH717 0318041176600\r\n".getBytes();
	        this.out.write(buf);
	        */
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
