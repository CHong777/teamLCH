package com.example.tjddm.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;

public class LoginActivity extends AppCompatActivity {

    EditText idText;
    boolean isConnect = false;
    EditText passwdText;
    Button loginButton;
    ProgressDialog pro;
    LinearLayout container;
    ScrollView scroll;

    // 어플 종료시 스레드 중지를 위해...
    boolean isRunning = false;
    // 서버와 연결되어있는 소켓 객체
    Socket member_socket;
    // 사용자 닉네임( 내 닉넴과 일치하면 내가보낸 말풍선으로 설정 아니면 반대설정)
    String user_Password;
    String user_ID;

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        idText = (EditText) findViewById(R.id.idText);
        passwdText = (EditText) findViewById(R.id.passwordText);
        loginButton = findViewById(R.id.loginButton);
    }

    public void btnMethod(View v) {
        if (isConnect == false) {
            String userID = idText.getText().toString();
            String userPassword = passwdText.getText().toString();
            if (userPassword.length() > 0 && userPassword != null && userID.length() > 0 && userID != null) {
                pro = ProgressDialog.show(this, null, "접속중입니다.");
                ConnectionThread thread = new ConnectionThread();
                thread.start();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("빈칸없이 입력해주세요.");
                builder.setPositiveButton("확인", null);
                builder.show();
            }
        }
    }

    class ConnectionThread extends Thread {
        @Override
        public void run() {
            try {
                final Socket socket = new Socket("192.168.0.27", 3000);
                member_socket = socket;
                final String userPassword = passwdText.getText().toString();
                user_Password = userPassword;
                final String userID = idText.getText().toString();
                user_ID = userID;

                OutputStream os = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);

                InputStream is = socket.getInputStream();
                DataInputStream dis = new DataInputStream(is);

                Protocol protocol = new Protocol();

                protocol = new Protocol(Protocol.PT_RES_LOGIN);
                protocol.setId(userID);
                protocol.setPassword(userPassword);
                os.write(protocol.getPacket());

                byte[] buf = protocol.getPacket();

                is.read(buf);
                int packetType = buf[0];
                protocol.setPacket(packetType, buf);

                if (packetType == Protocol.PT_EXIT) {
                    System.out.println("클라이언트 종료");
                }

                switch (packetType) {
                    case Protocol.PT_LOGIN_RESULT:
                        System.out.println("서버가 로그인 결과 전송.");
                        String result = protocol.getLoginResult();
                        if (result.equals("1")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pro.dismiss();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                                    SocketHandler s = ((SocketHandler) getApplication());
                                    s.setSocket(member_socket);;

                                    intent.putExtra("USER_ID", user_ID);
                                    intent.putExtra("order","off");
                                    startActivity(intent);
                                    LoginActivity.this.finish();
                                }
                            });
                        } else if (result.equals("2")) {
                            Handler mHandler = new Handler(Looper.getMainLooper());
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    pro.dismiss();
                                    Toast.makeText(getApplicationContext(), "비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                                }
                            }, 0);

                            os.write(protocol.getPacket());
                        } else if (result.equals("3")) {
                            Handler mHandler = new Handler(Looper.getMainLooper());
                            mHandler.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    pro.dismiss();
                                    Toast.makeText(getApplicationContext(), "아이디를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                                }
                            }, 0);
                        }
                           /* protocol = new Protocol(Protocol.PT_EXIT);
                            System.out.println("종료 패킷 전송");
                            os.write(protocol.getPacket());*/
                }


              /*  dos.writeUTF(userPassword);

                String check = dis.readUTF();

                if (check.equals("sucess")) {
                    // ProgressDialog 를 제거한다.
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pro.dismiss();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            SocketHandler s = ((SocketHandler) getApplication());
                            s.setSocket(member_socket);

                            intent.putExtra("USER_PASSWORD", userPassword);
                            startActivity(intent);
                        }
                    });
                } else {
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            pro.dismiss();
                            Toast.makeText(getApplicationContext(), "인증번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();

                        }
                    }, 0);
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class MessageThread extends Thread {
        Socket socket;
        DataInputStream dis;

        public MessageThread(Socket socket) {
            try {
                this.socket = socket;
                InputStream is = socket.getInputStream();
                dis = new DataInputStream(is);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                while (isRunning) {
                    final String msg = dis.readUTF();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView tv = new TextView(LoginActivity.this);
                            tv.setTextColor(Color.BLACK);
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);

                            if (msg.startsWith(user_Password)) {

                            } else {

                            }
                            tv.setText(msg);
                            container.addView(tv);
                            scroll.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}