package device;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Command.*;


 class SerialTest implements Runnable {
	OutputStream out;
	InputStream in;
	int temperature = 26;
	int humidity = 60;

	public SerialTest(OutputStream out, InputStream in) {
		this.out = out;
	}
	
	public class SharedArea {
		int temperature;
		int humidity;
		int temperature2;
		int humidity2;
	}

	SharedArea sa = new SharedArea();
	
	public void run() {	

	}
}
