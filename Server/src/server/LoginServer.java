package server;

import java.net.*;
import java.text.DecimalFormat;
import java.util.stream.IntStream;
import device.DeviceDAO;
import user.UserDAO;
import java.io.*;

public class LoginServer {
	InputStream in;
	OutputStream out;
	private ServerSocket server;
	private ServerSocket virtualDevice;

	Socket socket; // 앱쪽
	Socket devsocket; // 가상장치쪽

	int connect = 0, k = 0;
	Boolean AutoMode = false;
	String[] userid = { "ini", "ini", "ini" };
	String useId;

	UserDAO userDAO = new UserDAO();
	DeviceDAO deviceDAO = new DeviceDAO();

	public class SharedArea {
		int temperature;
		int humidity;
		String order;
		int timeorder;

		int temperature2;
		int humidity2;
		String order2;
		double timeorder2;

	}

	public LoginServer() {
		SharedArea sa = new SharedArea();
		try {
			server = new ServerSocket(3000);
			virtualDevice = new ServerSocket(3001);
			// 초기 명령은 off
			sa.order = "off";
			sa.order2 = "off";
			sa.timeorder = 999;

			ConnectionThread thread = new ConnectionThread();
			thread.sa = sa;
			thread.start();

			DevConnectionThread devthread = new DevConnectionThread();
			devthread.sa = sa;
			devthread.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class ConnectionThread extends Thread {

		SharedArea sa = new SharedArea();

		public void run() {
			try {
				System.out.println("사용자 접속 대기");
				while (true) {

					socket = server.accept();

					// 사용자 닉네임을 처리하는 스레드 가동
					RequestThread thread = new RequestThread(socket);
					thread.sa = sa;
					thread.start();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class DevConnectionThread extends Thread {

		SharedArea sa = new SharedArea();

		public void run() {
			try {
				System.out.println("가상 장치 대기");

				devsocket = virtualDevice.accept();
				System.out.println("가상장치 정보:" + devsocket.toString());
				in = devsocket.getInputStream();
				out = devsocket.getOutputStream();
				System.out.println("Connecting with Wifi : " + devsocket);
				System.out.println(devsocket.getPort());
				// (new Thread(new SerialReader(in, out))).start();

				DeviceProtocol protocol = new DeviceProtocol(DeviceProtocol.PT_REQ_DEV_STATUS);
				out.write(protocol.getPacket());

				DeviceThread thread = new DeviceThread(devsocket, in, out);
				thread.sa = sa;
				thread.start();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class RequestThread extends Thread {
		private Socket socket;
		SharedArea sa = new SharedArea();

		public RequestThread(Socket socket) {
			this.socket = socket;
		}

		public void run() {

			try {

				System.out.println("클라이언트 접속");

				OutputStream os = socket.getOutputStream();
				InputStream is = socket.getInputStream();

				while (true) {
					Protocol protocol = new Protocol();

					byte[] buf = protocol.getPacket();
					is.read(buf);
					// 패킷 타입을 얻음
					int packetType = buf[0];
					protocol.setPacket(packetType, buf);
					if (packetType == Protocol.PT_EXIT) {
						protocol = new Protocol(Protocol.PT_EXIT);
						os.write(protocol.getPacket());
						System.out.println("서버 종료");
					}

					switch (packetType) {
					// 클라이언트가 로그인 정보 응답 패킷인 경우
					case Protocol.PT_RES_LOGIN:
						System.out.println("클라이언트가" + "로그인 정보를 보냈습니다.");
						String id = protocol.getId();
						String password = protocol.getPassword();
						System.out.println(id + " " + password);

						int result = userDAO.login(id, password);
						System.out.println("안커넥트 : " + connect);

						if (result == 1) // 로그인 성공
						{
							protocol = new Protocol(Protocol.PT_LOGIN_RESULT);
							protocol.setLoginResult("1");
							UserClass user = new UserClass(id, socket, protocol);
							user.sa = sa;
							user.start();
							System.out.println("로그인 성공");

							connect++;

							int i = 0;

							if (connect == 1)
								userid[i] = id;

							else {
								userid[k - 1] = userid[k - 1];
								userid[k] = id;
							}
							k++;

							System.out.println("커넥트 : " + connect);
						} else if (result == 0) {
							protocol = new Protocol(Protocol.PT_LOGIN_RESULT);
							protocol.setLoginResult("2");
							System.out.println("암호 틀림");
						} else if (result == -1) {
							// 아이디 존재 안 함
							protocol = new Protocol(Protocol.PT_LOGIN_RESULT);
							protocol.setLoginResult("3");
							System.out.println("아이디 존재 안 함");
						} else if (result == -2)
							System.out.println("db error");
						System.out.println("로그인 처리 결과 전송");
						os.write(protocol.getPacket());

					case Protocol.PT_REQ_DEV_STATUS:

						while (true) {

							if (socket != null) {
								if (userid[0].contentEquals("IOT") || (userid[1].contentEquals("IOT"))
										|| userid[2].contentEquals("IOT")) {
									Thread.sleep(2800);

									protocol = new Protocol(Protocol.PT_RES_DEV_STATUS);

									protocol.setTemperature(sa.temperature);
									protocol.setHumidity(sa.humidity);
									protocol.setOrder(sa.order + "-IOT");
									System.out.println("IOT로 송신하는 데이터");
									System.out.println(
											"온도 : " + sa.temperature + " 습도  :" + sa.humidity + "앱 명령 : " + sa.order);

									os.write(protocol.getPacket());
								}
								if (userid[0].contentEquals("717") || (userid[1].contentEquals("717"))
										|| userid[2].contentEquals("717")) {

									Thread.sleep(2800);

									protocol = new Protocol(Protocol.PT_RES_DEV_STATUS);

									protocol.setTemperature(sa.temperature2);
									protocol.setHumidity(sa.humidity2);
									protocol.setOrder(sa.order2 + "-717");
									System.out.println("717로 로그인");
									System.out.println("온도 : " + sa.temperature2 + " 습도  :" + sa.humidity2 + "앱 명령 : "
											+ sa.order2);

									os.write(protocol.getPacket());
								}
							}
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	class DeviceThread extends Thread {
		private Socket dsocket;
		InputStream is;
		OutputStream os;

		SharedArea sa = new SharedArea();

		DeviceProtocol protocol = new DeviceProtocol(DeviceProtocol.PT_REQ_DEV_STATUS);

		public DeviceThread(Socket dsocket, InputStream is, OutputStream os) {
			try {
				this.dsocket = dsocket;
				this.is = is;
				this.os = os;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void run() {
			try {

				os.write(protocol.getPacket());

				while (true) {
					protocol = new DeviceProtocol();
					// 기본적으로 1000개 잡혀있음
					byte[] buf = protocol.getPacket();
					is.read(buf);
					// 패킷 타입을 얻음

					int packetType = buf[0];
					protocol.setPacket(packetType, buf);

					if (packetType == DeviceProtocol.PT_EXIT) {
						protocol = new DeviceProtocol(DeviceProtocol.PT_EXIT);
						os.write(protocol.getPacket());
						System.out.println("서버 종료");
						break;
					}
					switch (packetType) {
					case DeviceProtocol.PT_RES_DEV_STATUS:

						int temperature = buf[2];
						int humidity = buf[1];

						sa.temperature = temperature;
						sa.humidity = humidity;
						// 자동모드랑 사용자가 온 누른게 같이 실행되면
						if (AutoMode == true && sa.order.contentEquals("onn")) {
							System.out.println("오토모드 온 : " + Math.round(sa.timeorder));
							TimerTest timer = new TimerTest((int) Math.round(sa.timeorder/10), out);
							
							timer.sa = sa;
							timer.start();

							byte bufF[] = sa.order.getBytes();
							out.write(bufF);
						}

						else {
							System.out.println("환풍기로부터 수신된 데이터");
							System.out.println("온도 :" + temperature + " 습도 :" + humidity + "명령: " + sa.order);
							// 장치에게 명령어 계속 전송
							String test = sa.order + userid[0] + userid[1] + sa.order2 + userid[2]+sa.timeorder;

							byte bufF[] = test.getBytes();
							out.write(bufF);
						}

						break;

					case DeviceProtocol.PT_RES_DEV_STATUS2:

						int temperature2 = buf[2];
						int humidity2 = buf[1];

						sa.temperature2 = temperature2;
						sa.humidity2 = humidity2;
						// 자동모드랑 사용자가 온 누른게 같이 실행되면
						if (AutoMode == true && sa.order2.contentEquals("onn")) {
							System.out.println("717온: " + Math.round(sa.timeorder));
							TimerTest timer = new TimerTest((int) Math.round(sa.timeorder), out);
							
							timer.sa = sa;
							timer.start();

							byte bufF[] = sa.order2.getBytes();
							out.write(bufF);
						}

						else {
							System.out.println("717환풍로부터 수신된 데이터");
							System.out.println("온도 :" + temperature2 + " 습도 :" + humidity2 + "명령: " + sa.order2);
							// 장치에게 명령어 계속 전송
							String tes = sa.order + userid[0] + userid[1] + sa.order2 + userid[2]+sa.timeorder;
							byte bu[] = tes.getBytes();
							out.write(bu);
						}

						break;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 타이머
	public class TimerTest extends Thread {
		int start = 0;
		int end;
		OutputStream out;
		SharedArea sa = new SharedArea();

		public TimerTest(int end, OutputStream out) {
			try {
				this.end = end;
				this.out = out;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void run() {
			while (true) {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (end < start) {
					sa.order = "off";
					break;
				} else {
					start++;
				}
			}
		}
	}

	// 사용자 정보를 관리하는 클래스
	class UserClass extends Thread {
		String id;
		Socket socket;
		Protocol protocol;
		DataInputStream dis;
		DataOutputStream dos;
		String order;

		SharedArea sa = new SharedArea();

		public UserClass(String id, Socket socket, Protocol protocol) {
			try {
				this.id = id;
				this.socket = socket;
				this.protocol = protocol;
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				dis = new DataInputStream(is);
				dos = new DataOutputStream(os);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void run() {
			try {

				OutputStream os = socket.getOutputStream();
				InputStream is = socket.getInputStream();
				// 기본적으로 1000개 잡혀있음
				while (true) {
					protocol = new Protocol();

					byte[] buf = protocol.getPacket();
					is.read(buf);
					// 패킷 타입을 얻음
					int packetType = buf[0];
					protocol.setPacket(packetType, buf);
					if (packetType == Protocol.PT_EXIT) {
						protocol = new Protocol(Protocol.PT_EXIT);
						os.write(protocol.getPacket());
						System.out.println("서버 종료");
					}

					switch (packetType) {
					case Protocol.PT_Ventilator_ORDER:

						order = protocol.getUserOrder();

						if (order.contentEquals("onn") && id.contentEquals("IOT")) {
							sa.order = "onn";
							System.out.println("사용자 아이디 : " + id + " 로부터  환풍기 ON 명령 수신...");
						} else if (order.contentEquals("onn") && id.contentEquals("717")) {
							sa.order2 = "onn";
							System.out.println("사용자 아이디 : " + id + " 로부터  환풍기 ON 명령 수신...");
						} else if (order.contentEquals("off") && id.contentEquals("IOT")) {
							sa.order = "off";
							System.out.println("사용자 아이디 : " + id + " 로부터  환풍기 OFF 명령 수신...");
						} else if (order.contentEquals("off") && id.contentEquals("717")) {
							sa.order2 = "off";
							System.out.println("사용자 아이디 : " + id + " 로부터  환풍기 OFF 명령 수신...");
						} else
							System.out.println("사용자 아이디 : '" + id + "' 로부터 ON/OFF 명령 오지 않음");
						break;
						
					case Protocol.PT_AUTO_ON_OFF:
						int auto = buf[3];

						if (auto % 2 == 0) {
							AutoMode = true;
							double t = handleCommandLine(Integer.toString(70));
							sa.timeorder = (int)t;
							sa.timeorder = sa.timeorder*10;
							System.out.println("자동모드 키기 :" + sa.timeorder);
							System.out.println("자동오토다" + auto);
						} else {
							AutoMode = false;
							System.out.println("자동모드 끄기");
							System.out.println("끄는오토다" + auto);
						}
						break;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private double handleCommandLine(String humidity) {

			double time = 0;

			double[][][] TRAINING_DATA = { { { 1.0, 48.9 }, { 13 } }, { { 1.0, 28.5 }, { 0 } },
					{ { 1.0, 37.5 }, { 0 } }, { { 1.0, 40.4 }, { 10 } }, { { 1.0, 35.8 }, { 0 } },
					{ { 1.0, 43.8 }, { 10 } }, { { 1.0, 44.9 }, { 12 } }, { { 1.0, 46.8 }, { 12 } },
					{ { 1.0, 93.0 }, { 40 } }, { { 1.0, 94.6 }, { 43 } }, { { 1.0, 95.4 }, { 45 } },
					{ { 1.0, 74.4 }, { 20 } }, { { 1.0, 69.6 }, { 17 } }, { { 1.0, 77.1 }, { 25 } },
					{ { 1.0, 52.4 }, { 13 } }, { { 1.0, 50.9 }, { 10 } }, { { 1.0, 50.5 }, { 10 } },
					{ { 1.0, 42.3 }, { 10 } }, { { 1.0, 50.1 }, { 10 } }, { { 1.0, 87.4 }, { 33 } },
					{ { 1.0, 74.9 }, { 20 } }, { { 1.0, 39.5 }, { 0 } }, { { 1.0, 80.5 }, { 30 } },
					{ { 1.0, 86.7 }, { 32 } }, { { 1.0, 75.2 }, { 20 } }, { { 1.0, 90.2 }, { 40 } },
					{ { 1.0, 91.3 }, { 40 } }, { { 1.0, 65.0 }, { 15 } }, { { 1.0, 59.5 }, { 14 } },
					{ { 1.0, 54.4 }, { 10 } }, { { 1.0, 60.0 }, { 14 } }, { { 1.0, 71.6 }, { 20 } },
					{ { 1.0, 73.0 }, { 20 } }, { { 1.0, 71.9 }, { 20 } }, { { 1.0, 60.6 }, { 10 } },
					{ { 1.0, 63.0 }, { 15 } }, { { 1.0, 71.9 }, { 20 } }, { { 1.0, 68.1 }, { 16 } },
					{ { 1.0, 67.1 }, { 15 } }, { { 1.0, 66.4 }, { 15 } }, { { 1.0, 89.9 }, { 38 } },
					{ { 1.0, 72.5 }, { 20 } }, { { 1.0, 60.3 }, { 10 } }, { { 1.0, 62.1 }, { 10 } },
					{ { 1.0, 67.5 }, { 15 } }, { { 1.0, 93.8 }, { 40 } }, { { 1.0, 80.5 }, { 30 } },
					{ { 1.0, 93.8 }, { 40 } }, { { 1.0, 80.5 }, { 30 } }, { { 1.0, 59.0 }, { 13 } },
					{ { 1.0, 57.5 }, { 14 } }, { { 1.0, 58.0 }, { 14 } }, { { 1.0, 68.8 }, { 16 } },
					{ { 1.0, 59.1 }, { 14 } }, { { 1.0, 70.4 }, { 20 } }, { { 1.0, 57.0 }, { 13 } },
					{ { 1.0, 43.3 }, { 10 } }, { { 1.0, 62.5 }, { 12 } }, { { 1.0, 54.5 }, { 10 } },
					{ { 1.0, 60.4 }, { 11 } }, { { 1.0, 85.3 }, { 30 } }, { { 1.0, 69.0 }, { 16 } },
					{ { 1.0, 84.1 }, { 30 } }, { { 1.0, 73.3 }, { 20 } }, { { 1.0, 58.9 }, { 13 } },
					{ { 1.0, 61.9 }, { 15 } }, { { 1.0, 59.3 }, { 12 } }, { { 1.0, 67.8 }, { 16 } },
					{ { 1.0, 66.4 }, { 16 } }, { { 1.0, 68.0 }, { 15 } }, { { 1.0, 60.5 }, { 12 } },
					{ { 1.0, 62.4 }, { 12 } }, { { 1.0, 61.8 }, { 11 } }, { { 1.0, 69.9 }, { 17 } },
					{ { 1.0, 62.8 }, { 12 } }, { { 1.0, 63.6 }, { 13 } }, { { 1.0, 67.9 }, { 15 } },
					{ { 1.0, 65.4 }, { 15 } }, { { 1.0, 64.1 }, { 14 } }, { { 1.0, 66.9 }, { 15 } },
					{ { 1.0, 68.3 }, { 16 } }, { { 1.0, 70.4 }, { 18 } }, { { 1.0, 68.6 }, { 16 } },
					{ { 1.0, 75.9 }, { 20 } }, { { 1.0, 67.8 }, { 15 } }, { { 1.0, 64.0 }, { 14 } },
					{ { 1.0, 75.5 }, { 20 } }, { { 1.0, 69.3 }, { 16 } }, { { 1.0, 69.8 }, { 20 } } };

			LinearRegression lr = null;

			double[][] xArray = new double[TRAINING_DATA.length][TRAINING_DATA[0][0].length];
			double[][] yArray = new double[TRAINING_DATA.length][1];
			IntStream.range(0, TRAINING_DATA.length).forEach(i -> {
				IntStream.range(0, TRAINING_DATA[0][0].length).forEach(j -> xArray[i][j] = TRAINING_DATA[i][0][j]);
				yArray[i][0] = TRAINING_DATA[i][1][0];
			});

			try {
				lr = new LinearRegression(xArray, yArray);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

			try {
				String entry = humidity;
				System.out.println("현재습도는 : " + entry);
				if (!entry.equals("exit")) {
					DecimalFormat format = new DecimalFormat();
					format.applyLocalizedPattern("0.00");
					System.out.println("Time minute : " + format.format(lr.estimateRent(entry)) + "분");
					time = lr.estimateRent(entry);
					System.out.println(id);
					return time;
				} else
					System.exit(0);
			} catch (Exception e) {
				System.out.println("invalid input");
			}
			return time;
		}

	}

	public static void main(String[] args) throws Exception {

		new LoginServer();

	}

}