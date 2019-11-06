package device;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import Command.Send;
import Command.Send2;
import device.SerialTest.SharedArea;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SerialReader extends JFrame implements ActionListener, KeyListener, Runnable {
	InputStream in;
	OutputStream out;
	int temperature = 26;
	int humidity = 60; // �쇊履� �솚�뭾湲�
	int temperature2 = 37; // �삤瑜몄そ �솚�뭾湲�
	int humidity2 = 85;

	int mltime = 0;
	int ttime = 0;

	int start = 0;
	int start2 = 0;
	
	public SerialReader(InputStream in, OutputStream out) {
		this.in = in;
		this.out = out;
	}

	public class SharedArea {
		int temperature;
		int humidity;
		int temperature2;
		int humidity2;
		String order;
		String order2;
	}

	SharedArea sa = new SharedArea();

	String uid;
	String dv1 = "IOT";
	String dv2 = "717";
	String initorder = "off";

	JFrame f = new JFrame();

	ImageIcon image1 = new ImageIcon("C:\\FAN_OFF.png"); // �씠誘몄� 寃쎈줈
	ImageIcon image2 = new ImageIcon("C:\\FAN_OFF.png"); // �씠誘몄� 寃쎈줈

	JTextField uMsg = new JTextField(20);

	JPanel panel = new JPanel();

	JLabel label = new JLabel(image1);
	JLabel label1 = new JLabel(image2);

	JLabel jLabel = new JLabel(" 온도 ");
	JLabel jLabel2 = new JLabel(String.valueOf(temperature));
	JLabel jLabel3 = new JLabel(" ℃");
	JLabel jLabel4 = new JLabel(" 습도 ");
	JLabel jLabel5 = new JLabel(String.valueOf(humidity));
	JLabel jLabel6 = new JLabel("  %");

	JLabel jLabel1 = new JLabel(" 온도 ");
	JLabel jLabel7 = new JLabel(String.valueOf(temperature2));
	JLabel jLabel8 = new JLabel(" ℃");

	JLabel jLabel9 = new JLabel(" 습도 ");
	JLabel jLabel10 = new JLabel(String.valueOf(humidity2));
	JLabel jLabel11 = new JLabel("  %");

	JLabel jLabel12 = new JLabel("ID : ");
	JLabel jLabel13 = new JLabel(dv1);

	JLabel jLabel14 = new JLabel("ID : ");
	JLabel jLabel15 = new JLabel(dv2);

	JLabel jLabel16 = new JLabel("time: ");
	JLabel jLabel17 = new JLabel("0");
	JLabel jLabel18 = new JLabel("  초");

	JLabel jLabel19 = new JLabel("ML-T: ");
	JLabel jLabel20 = new JLabel("0");
	JLabel jLabel21 = new JLabel("  초");

	JLabel jLabel22 = new JLabel("time: ");
	JLabel jLabel23 = new JLabel("0");
	JLabel jLabel24 = new JLabel("  초");

	JLabel jLabel25 = new JLabel("ML-T: ");
	JLabel jLabel26 = new JLabel("0");
	JLabel jLabel27 = new JLabel("  초");

	{
		panel.setLayout(null);
		// 踰꾪듉 �깮�꽦

		panel.add(label);
		panel.add(label1);

		panel.add(jLabel);
		panel.add(jLabel2);
		panel.add(jLabel3);
		panel.add(jLabel4);
		panel.add(jLabel5);
		panel.add(jLabel6);
		panel.add(jLabel1);
		panel.add(jLabel7);
		panel.add(jLabel8);
		panel.add(jLabel9);
		panel.add(jLabel10);
		panel.add(jLabel11);
		panel.add(jLabel12);
		panel.add(jLabel13);
		panel.add(jLabel14);
		panel.add(jLabel15);

		panel.add(jLabel16);
		panel.add(jLabel17);
		panel.add(jLabel18);
		panel.add(jLabel19);
		panel.add(jLabel20);
		panel.add(jLabel21);
		panel.add(jLabel22);
		panel.add(jLabel23);
		panel.add(jLabel24);
		panel.add(jLabel25);
		panel.add(jLabel26);
		panel.add(jLabel27);

		jLabel.setBounds(70, 350, 35, 15);
		jLabel2.setBounds(105, 350, 35, 15);
		jLabel3.setBounds(115, 350, 35, 15);
		// �씠誘몄� 1�쓽 �삩�룄

		jLabel4.setBounds(150, 350, 35, 15);
		jLabel5.setBounds(185, 350, 35, 15);
		jLabel6.setBounds(195, 350, 35, 15);
		// �씠誘몄� 1�쓽 �뒿�룄

		// �씠誘몄� 1�쓽 ID
		jLabel12.setBounds(130, 80, 35, 15);
		jLabel13.setBounds(150, 80, 35, 15);

		// �씠誘몄� 2�쓽 ID
		jLabel14.setBounds(755, 80, 35, 15);
		jLabel15.setBounds(775, 80, 35, 15);

		jLabel14.setBounds(755, 80, 35, 15);
		jLabel15.setBounds(775, 80, 35, 15);

		jLabel16.setBounds(80, 400, 40, 15);
		jLabel17.setBounds(115, 400, 40, 15);
		jLabel18.setBounds(120, 400, 40, 15);
		// 환풍기 1의 그냥 시간

		jLabel19.setBounds(150, 400, 40, 15);
		jLabel20.setBounds(190, 400, 40, 15);
		jLabel21.setBounds(195, 400, 40, 15);
		// 환풍기 1의 기계학습시간

		jLabel22.setBounds(700, 400, 40, 15);
		jLabel23.setBounds(735, 400, 40, 15);
		jLabel24.setBounds(740, 400, 40, 15);
		// 환풍기 2의 그냥 시간

		jLabel25.setBounds(770, 400, 40, 15);
		jLabel26.setBounds(810, 400, 40, 15);
		jLabel27.setBounds(815, 400, 40, 15);
		// 환풍기 2의 기계학습시간

		// jLabel.setBounds(r);
		label.setBounds(25, 90, 250, 250); // �씠誘몄�1
		label1.setBounds(650, 90, 250, 250); // �씠誘몄� 2

		jLabel1.setBounds(690, 350, 35, 15);
		jLabel7.setBounds(730, 350, 35, 15);
		jLabel8.setBounds(740, 350, 35, 15);
		// �씠誘몄� 2�쓽 �삩�룄
		jLabel9.setBounds(770, 350, 35, 15);
		jLabel10.setBounds(810, 350, 35, 15);
		jLabel11.setBounds(820, 350, 35, 15);
		// �씠誘몄� 2�쓽 �뒿�룄

		uMsg.addKeyListener(this);

		setSize(960, 500);
		this.add(panel);
		this.setVisible(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	// off 異붽�
	class off extends Thread {

		SharedArea sa = new SharedArea();

		OutputStream out;
		int humidity;
		int temperature;
		String order;
		InputStream in;
		
		TimerTest test;
		TimerTest2 test2;
		TimerTest3 test3;
		
		public off(OutputStream out, int humidity, int temperature, String order, InputStream in) {

			this.out = out;
			this.humidity = humidity;
			this.temperature = temperature;
			this.order = order;
			this.in = in;
		}

		public void run() {
			byte[] buffer = new byte[1024];
			int len = -1;
			String a, b, c, d, f, g, h;

			try {
				while ((len = in.read(buffer)) > -1) {

					a = (new String(buffer, 0, 3)); // sa.order
					b = (new String(buffer, 3, 3)); // 泥ル쾲吏� �젒�냽 id
					c = (new String(buffer, 6, 3)); // �몢踰덉㎏ �젒�냽 id
					d = (new String(buffer, 9, 3)); // sa.order2
					f = (new String(buffer, 12, 3)); // 留덉�留� �젒�냽 id
					g = (new String(buffer, 15, 3));
					h = (new String(buffer, 18, 3));

					System.out.println(a + " " + b + " " + c + " " + d + " " + f + " " + g + " " + h);

					// �쇊履� �솚�뭾湲곕옉 媛숈쓣�븣

					if (b.contentEquals(dv1) || c.contentEquals(dv1)) {
						if (a.contentEquals("onn")) {
							panel.remove(label);
							image1 = new ImageIcon("C:\\FAN_ON.gif"); // �씠誘몄� 寃쎈줈 label = new JLabel(image1);
							label = new JLabel(image1);
							panel.add(label);
							label.setBounds(25, 90, 250, 250);
							setVisible(true);
							
							if(h.contentEquals("200")) {
								test = new TimerTest(20);
								test.start();
								
								System.out.println("띵띵");
							}else{
								System.out.println("Timertest2");
								if(test2!=null) {
									test2.stop();
								test2 = new TimerTest2(ttime,0);
								test2.start();
								}
								else {
									test2 = new TimerTest2(ttime,0);
									test2.start();
								}
							}
							
						} else if (a.contentEquals("off")) {
							panel.remove(label);
							image1 = new ImageIcon("C:\\FAN_OFF.png");
							label = new JLabel(image1);
							panel.add(label);
							label.setBounds(25, 90, 250, 250);
							setVisible(true);
							if(test2!=null) {
							test2.stop();
							test2 = new TimerTest2(ttime,1);
							test2.start();
							}
						}
					}

					// �삤瑜몄そ �솚�뭾湲�
					if (b.contentEquals(dv2) || c.contentEquals(dv2)) {
						if (d.contentEquals("onn")) {
							panel.remove(label1);
							image2 = new ImageIcon("C:\\FAN_ON.gif"); // �씠誘몄� 寃쎈줈
							label1 = new JLabel(image2);
							panel.add(label1);
							label1.setBounds(650, 90, 250, 250);
							setVisible(true);
							if(h.contentEquals("200")) {
								
							}
							else{
								System.out.println("Timertest2");
								if(test3!=null) {
									test3.stop();
									test3 = new TimerTest3(ttime,0);
									test3.start();
								}
								else {
									test3 = new TimerTest3(ttime,0);
									test3.start();
								}
							}
						} else if (d.contentEquals("off")) {
							panel.remove(label1);
							image2 = new ImageIcon("C:\\\\FAN_OFF.png"); // �씠誘몄� 寃쎈줈
							label1 = new JLabel(image2);
							panel.add(label1);
							label1.setBounds(650, 90, 250, 250);
							setVisible(true);
							if(test3!=null) {
								test3.stop();
								System.out.println("ts");
								test3= new TimerTest3(ttime,1);
								test3.start();
								}
						}
					}

					System.out.println(a);

					if (a.contentEquals("off") || !a.contentEquals("onn") || !a.contentEquals("ml")) {

						if (humidity > 99)
							humidity = 99;

						if (humidity < 0)
							humidity = 0;

						if (humidity2 > 99)
							humidity2 = 99;

						if (humidity2 < 0)
							humidity2 = 0;

						if (temperature > 37)
							temperature = 37;

						if (temperature2 > 37)
							temperature2 = 37;

						sa.temperature = temperature;
						sa.humidity = humidity;

						sa.temperature2 = temperature2;
						sa.humidity2 = humidity2;

						Send sen = new Send(out, sa.temperature, sa.humidity, "off");
						sen.run();

						Thread.sleep(500);

						Send2 sen2 = new Send2(out, sa.temperature2, sa.humidity2, "off");
						sen2.run();

						//

						jLabel2.setText(String.valueOf(sa.temperature));
						jLabel5.setText(String.valueOf(sa.humidity));

						jLabel7.setText(String.valueOf(sa.temperature2));
						jLabel10.setText(String.valueOf(sa.humidity2));

						Thread.sleep(500);

						temperature++;
						humidity++;
						temperature2--;
						humidity2--;

						if (humidity > 99)
							humidity = 99;

						if (humidity < 0)
							humidity = 0;

						if (humidity2 > 99)
							humidity2 = 99;

						if (humidity2 < 0)
							humidity2 = 0;

						if (temperature > 37)
							temperature = 37;

						if (temperature2 > 37)
							temperature2 = 37;

						sa.temperature = temperature;
						sa.humidity = humidity;

						sa.temperature2 = temperature2;
						sa.humidity2 = humidity2;

						sen = new Send(out, sa.temperature, sa.humidity, "off");

						// sa.temperature = temperature;
						// sa.humidity = humidity;
						sen.run();
						Thread.sleep(500);
						sen2 = new Send2(out, sa.temperature2, sa.humidity2, "off");
						sen2.run();

						jLabel2.setText(String.valueOf(sa.temperature));
						jLabel5.setText(String.valueOf(sa.humidity));

						jLabel7.setText(String.valueOf(sa.temperature2));
						jLabel10.setText(String.valueOf(sa.humidity2));

						Thread.sleep(500);

						temperature++;
						humidity++;
						temperature2++;
						humidity2++;

						if (humidity > 99)
							humidity = 99;

						if (humidity < 0)
							humidity = 0;

						if (humidity2 > 99)
							humidity2 = 99;

						if (humidity2 < 0)
							humidity2 = 0;

						if (temperature > 37)
							temperature = 37;

						if (temperature2 > 37)
							temperature2 = 37;

						sa.temperature = temperature;
						sa.humidity = humidity;

						sa.temperature2 = temperature2;
						sa.humidity2 = humidity2;

						sen = new Send(out, sa.temperature, sa.humidity, "off");

						sen.run();
						Thread.sleep(500);
						sen2 = new Send2(out, sa.temperature2, sa.humidity2, "off");
						sen2.run();

						jLabel2.setText(String.valueOf(sa.temperature));
						jLabel5.setText(String.valueOf(sa.humidity));
						jLabel7.setText(String.valueOf(sa.temperature2));
						jLabel10.setText(String.valueOf(sa.humidity2));

						Thread.sleep(500);

						temperature--;
						humidity--;
						temperature2--;
						humidity2++;

						if (humidity > 99)
							humidity = 99;

						if (humidity < 0)
							humidity = 0;

						if (humidity2 > 99)
							humidity2 = 99;

						if (humidity2 < 0)
							humidity2 = 0;

						if (temperature > 37)
							temperature = 37;

						if (temperature2 > 37)
							temperature2 = 37;

						sa.temperature = temperature;
						sa.humidity = humidity;

						sa.temperature2 = temperature2;
						sa.humidity2 = humidity2;

						sen = new Send(out, sa.temperature, sa.humidity, "off");

						// sa.temperature = temperature;
						// sa.humidity = humidity;
						sen.run();
						Thread.sleep(500);
						sen2 = new Send2(out, sa.temperature2, sa.humidity2, "off");
						sen2.run();

						jLabel2.setText(String.valueOf(sa.temperature));
						jLabel5.setText(String.valueOf(sa.humidity));
						jLabel7.setText(String.valueOf(sa.temperature2));
						jLabel10.setText(String.valueOf(sa.humidity2));

						Thread.sleep(500);

						temperature--;
						humidity++;
						temperature2++;
						humidity2++;
					}

					if (a.contentEquals("onn")) {

						System.out.println("돌아간다");

						if (humidity > 99)
							humidity = 99;

						if (humidity < 0)
							humidity = 0;

						if (humidity2 > 99)
							humidity2 = 99;

						if (humidity2 < 0)
							humidity2 = 0;

						if (temperature > 37)
							temperature = 37;

						if (temperature2 > 37)
							temperature2 = 37;

						sa.temperature = temperature;
						sa.humidity = humidity;

						sa.temperature2 = temperature2;
						sa.humidity2 = humidity2;

						Send sen = new Send(out, sa.temperature, sa.humidity, "onn");
						sen.run();

						Send2 sen2 = new Send2(out, sa.temperature2, sa.humidity2, "onn");
						sen2.run();

						jLabel2.setText(String.valueOf(sa.temperature));
						jLabel5.setText(String.valueOf(sa.humidity));

						jLabel7.setText(String.valueOf(sa.temperature2));
						jLabel10.setText(String.valueOf(sa.humidity2));

						Thread.sleep(1000);

						temperature--;
						humidity--;
						temperature2++;
						humidity2++;

						if (humidity > 99)
							humidity = 99;

						if (humidity < 0)
							humidity = 0;

						if (humidity2 > 99)
							humidity2 = 99;

						if (humidity2 < 0)
							humidity2 = 0;

						if (temperature > 37)
							temperature = 37;

						if (temperature2 > 37)
							temperature2 = 37;

						sa.temperature = temperature;
						sa.humidity = humidity;

						sa.temperature2 = temperature2;
						sa.humidity2 = humidity2;

						sen = new Send(out, sa.temperature, sa.humidity, "onn");

						// sa.temperature = temperature;
						// sa.humidity = humidity;
						sen.run();

						sen2 = new Send2(out, sa.temperature2, sa.humidity2, "onn");
						sen2.run();

						jLabel2.setText(String.valueOf(sa.temperature));
						jLabel5.setText(String.valueOf(sa.humidity));
						jLabel7.setText(String.valueOf(sa.temperature2));
						jLabel10.setText(String.valueOf(sa.humidity2));

						Thread.sleep(1000);

						temperature--;
						humidity--;
						temperature2++;
						humidity2++;

						if (humidity > 99)
							humidity = 99;

						if (humidity < 0)
							humidity = 0;

						if (humidity2 > 99)
							humidity2 = 99;

						if (humidity2 < 0)
							humidity2 = 0;

						if (temperature > 37)
							temperature = 37;

						if (temperature2 > 37)
							temperature2 = 37;

						sa.temperature = temperature;
						sa.humidity = humidity;

						sa.temperature2 = temperature2;
						sa.humidity2 = humidity2;

						sen = new Send(out, sa.temperature, sa.humidity, "onn");

						// sa.temperature = temperature;
						// sa.humidity = humidity;
						sen.run();

						sen2 = new Send2(out, sa.temperature2, sa.humidity2, "onn");
						sen2.run();

						jLabel2.setText(String.valueOf(sa.temperature));
						jLabel5.setText(String.valueOf(sa.humidity));

						jLabel7.setText(String.valueOf(sa.temperature2));
						jLabel10.setText(String.valueOf(sa.humidity2));

						Thread.sleep(500);

						temperature--;
						humidity--;
						temperature2++;
						humidity2++;

						if (humidity > 99)
							humidity = 99;

						if (humidity < 0)
							humidity = 0;

						if (humidity2 > 99)
							humidity2 = 99;

						if (humidity2 < 0)
							humidity2 = 0;

						if (temperature > 37)
							temperature = 37;

						if (temperature2 > 37)
							temperature2 = 37;

						sa.temperature = temperature;
						sa.humidity = humidity;

						sa.temperature2 = temperature2;
						sa.humidity2 = humidity2;

						sen = new Send(out, sa.temperature, sa.humidity, "onn");

						// sa.temperature = temperature;
						// sa.humidity = humidity;
						sen.run();

						sen2 = new Send2(out, sa.temperature2, sa.humidity2, "onn");
						sen2.run();

						jLabel2.setText(String.valueOf(sa.temperature));
						jLabel5.setText(String.valueOf(sa.humidity));

						jLabel7.setText(String.valueOf(sa.temperature2));
						jLabel10.setText(String.valueOf(sa.humidity2));

						Thread.sleep(500);
						humidity--;
					}

					if (d.contentEquals("onn")) {

						System.out.println("d가동");

						if (humidity > 99)
							humidity = 99;

						if (humidity < 0)
							humidity = 0;

						if (humidity2 > 99)
							humidity2 = 99;

						if (humidity2 < 0)
							humidity2 = 0;

						if (temperature > 37)
							temperature = 37;

						if (temperature2 > 37)
							temperature2 = 37;

						sa.temperature = temperature;
						sa.humidity = humidity;

						sa.temperature2 = temperature2;
						sa.humidity2 = humidity2;

						Send sen = new Send(out, sa.temperature, sa.humidity, "onn");
						sen.run();

						Send2 sen2 = new Send2(out, sa.temperature2, sa.humidity2, "onn");
						sen2.run();

						jLabel2.setText(String.valueOf(sa.temperature));
						jLabel5.setText(String.valueOf(sa.humidity));

						jLabel7.setText(String.valueOf(sa.temperature2));
						jLabel10.setText(String.valueOf(sa.humidity2));

						Thread.sleep(1000);

						temperature++;
						humidity++;
						temperature2--;
						humidity2--;

						if (humidity > 99)
							humidity = 99;

						if (humidity < 0)
							humidity = 0;

						if (humidity2 > 99)
							humidity2 = 99;

						if (humidity2 < 0)
							humidity2 = 0;

						if (temperature > 37)
							temperature = 37;

						if (temperature2 > 37)
							temperature2 = 37;

						sa.temperature = temperature;
						sa.humidity = humidity;

						sa.temperature2 = temperature2;
						sa.humidity2 = humidity2;

						sen = new Send(out, sa.temperature, sa.humidity, "onn");

						// sa.temperature = temperature;
						// sa.humidity = humidity;
						sen.run();

						sen2 = new Send2(out, sa.temperature2, sa.humidity2, "onn");
						sen2.run();

						jLabel2.setText(String.valueOf(sa.temperature));
						jLabel5.setText(String.valueOf(sa.humidity));
						jLabel7.setText(String.valueOf(sa.temperature2));
						jLabel10.setText(String.valueOf(sa.humidity2));

						Thread.sleep(1000);

						temperature++;
						humidity++;
						temperature2--;
						humidity2--;

						if (humidity > 99)
							humidity = 99;

						if (humidity < 0)
							humidity = 0;

						if (humidity2 > 99)
							humidity2 = 99;

						if (humidity2 < 0)
							humidity2 = 0;

						if (temperature > 37)
							temperature = 37;

						if (temperature2 > 37)
							temperature2 = 37;

						sa.temperature = temperature;
						sa.humidity = humidity;

						sa.temperature2 = temperature2;
						sa.humidity2 = humidity2;

						sen = new Send(out, sa.temperature, sa.humidity, "onn");

						// sa.temperature = temperature;
						// sa.humidity = humidity;
						sen.run();

						sen2 = new Send2(out, sa.temperature2, sa.humidity2, "onn");
						sen2.run();

						jLabel2.setText(String.valueOf(sa.temperature));
						jLabel5.setText(String.valueOf(sa.humidity));

						jLabel7.setText(String.valueOf(sa.temperature2));
						jLabel10.setText(String.valueOf(sa.humidity2));

						Thread.sleep(500);

						temperature++;
						humidity++;
						temperature2--;
						humidity2--;

						if (humidity > 99)
							humidity = 99;

						if (humidity < 0)
							humidity = 0;

						if (humidity2 > 99)
							humidity2 = 99;

						if (humidity2 < 0)
							humidity2 = 0;

						if (temperature > 37)
							temperature = 37;

						if (temperature2 > 37)
							temperature2 = 37;

						sa.temperature = temperature;
						sa.humidity = humidity;

						sa.temperature2 = temperature2;
						sa.humidity2 = humidity2;

						sen = new Send(out, sa.temperature, sa.humidity, "onn");

						// sa.temperature = temperature;
						// sa.humidity = humidity;
						sen.run();

						sen2 = new Send2(out, sa.temperature2, sa.humidity2, "onn");
						sen2.run();

						jLabel2.setText(String.valueOf(sa.temperature));
						jLabel5.setText(String.valueOf(sa.humidity));

						jLabel7.setText(String.valueOf(sa.temperature2));
						jLabel10.setText(String.valueOf(sa.humidity2));

						Thread.sleep(500);

						humidity2--;

					}
					if (a.contentEquals("ml")) {

						System.out.println("湲곌퀎 �븰�뒿 �슂泥�");
					}

				}
			} catch (IOException | InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

		}
	}

	public class TimerTest extends Thread {
		//int start = 0;
		int end;

		public TimerTest(int end) {
			try {
				this.end = end;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void run() {
			while (true) {

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (end < start) {
					//System.out.println("나이따 성공 end는 :" + end + " start는 : " + start);
					break;
				} else {
					mltime = start;
					jLabel20.setText(String.valueOf(mltime));
					start++;
				}
			}
		}
	}
	public class TimerTest2 extends Thread{
		 int end;
		 int flag;
		 private boolean stop; // stop 플래그
			public TimerTest2(int end, int flag) {
				try {
					this.end = end;
					this.stop = false;
					this.flag = flag;

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		    public void run()
		    {	       	   
		        while(true)
		        {		          
		            try {
		                Thread.sleep(2000);
		            } catch (InterruptedException e) {
		                e.printStackTrace();
		            }     
		            
		            if(flag == 1) {
		            	//System.out.println("asd");
		            break;
		            }
		            else if(flag == 0) {
		            	//System.out.println("aaaaaa");
		            	ttime = start2;
		            	jLabel17.setText(String.valueOf(ttime));
		            	start2++;		   
		            }
		        }
		        jLabel17.setText(String.valueOf(ttime));
		    }
		}
	
	public class TimerTest3 extends Thread{
		 int end;
		 int flag;
		 private boolean stop; // stop 플래그
			public TimerTest3(int end, int flag) {
				try {
					this.end = end;
					this.stop = false;
					this.flag = flag;

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		    public void run()
		    {	       	   
		        while(true)
		        {		          
		            try {
		                Thread.sleep(2000);
		            } catch (InterruptedException e) {
		                e.printStackTrace();
		            }     
		            
		            if(flag == 1) {
		            	//System.out.println("asd");
		            break;
		            }
		            else if(flag == 0) {
		            	//System.out.println("aaaaaa");
		            	ttime = start2;
		            	jLabel23.setText(String.valueOf(ttime));
		            	start2++;		   
		            }
		        }
		        jLabel23.setText(String.valueOf(ttime));
		    }
		}

	public void run() {

		off sen = new off(out, temperature, humidity, "off", in);

		// sa.temperature = temperature;
		// sa.humidity = humidity;
		sen.run();

	}
}
