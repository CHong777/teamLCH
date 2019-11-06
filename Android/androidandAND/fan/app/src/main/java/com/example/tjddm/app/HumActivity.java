package com.example.tjddm.app;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static java.lang.Thread.*;

public class HumActivity extends AppCompatActivity {
    String Order;
    Toolbar myToolbar;
    Socket member_socket;
    String user_ID;
    boolean isConnect = false;
    boolean isRunning = false;
    String str = "0%";
    ImageView fan;

    String IOTid = "IOT";
    String dfid = "717";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hum);

        fan = (ImageView) findViewById(R.id.fan);

        // 추가 한 부분
        SocketHandler s = ((SocketHandler) getApplication());
        member_socket = s.getSocket();

        TextView humView = (TextView) findViewById(R.id.humview);
        humView.setText("···");
        //

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        setTitle("");

        final ImageButton okhumButton = (ImageButton) findViewById(R.id.okhumButton);

        Intent intent = getIntent();
        Order = intent.getStringExtra("order");
        user_ID = intent.getStringExtra("USER_ID");

        if (Order.equals("on")) {
            GlideDrawableImageViewTarget onfan = new GlideDrawableImageViewTarget(fan);
            Glide.with(this).load(R.drawable.on_fan).fitCenter().into(onfan);
        } else {
            fan.setImageResource(R.drawable.stop_fan);
        }

        Button mainbtn = (Button) findViewById(R.id.mainButton);
        Button infobtn = (Button) findViewById(R.id.infoButton);
        Button humbtn = (Button) findViewById(R.id.humButton);

        mainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Order.equals("on")) {
                    Intent intent = new Intent(HumActivity.this, MainActivity.class);
                    intent.putExtra("USER_ID", user_ID);
                    intent.putExtra("order", "on");
                    startActivity(intent);
                    HumActivity.this.finish();

                } else {
                    Intent intent = new Intent(HumActivity.this, MainActivity.class);
                    intent.putExtra("USER_ID", user_ID);
                    intent.putExtra("order", "off");
                    startActivity(intent);
                    HumActivity.this.finish();
                }
            }
        });
        infobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Order.equals("on")) {
                    Intent intent = new Intent(HumActivity.this, InfoActivity.class);
                    intent.putExtra("USER_ID", user_ID);
                    intent.putExtra("order", "on");
                    startActivity(intent);
                    HumActivity.this.finish();

                } else {
                    Intent intent = new Intent(HumActivity.this, InfoActivity.class);
                    intent.putExtra("USER_ID", user_ID);
                    intent.putExtra("order", "off");
                    startActivity(intent);
                    HumActivity.this.finish();
                }
            }
        });

        humbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        ReceiveHumidity receiveHumidity = new ReceiveHumidity(member_socket);
        receiveHumidity.start();


        if(user_ID.equals(IOTid)) {
            ReceiveOnOff receiveOnOff = new ReceiveOnOff(member_socket);
            receiveOnOff.start();
        }
        else {
            ReceiveOnOff2 receiveOnOff2 = new ReceiveOnOff2(member_socket);
            receiveOnOff2.start();
        }

    }

    public void okMethod(View v) {
        EditText humText = (EditText) findViewById(R.id.humEdit);
        if (humText.length() > 0 && humText != null) {
            //Toast.makeText(HumActivity.this, "습도 설정 완료!", Toast.LENGTH_SHORT).show();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    GlideDrawableImageViewTarget onfan = new GlideDrawableImageViewTarget(fan);
                    Glide.with(HumActivity.this).load(R.drawable.on_fan).fitCenter().into(onfan);
                    final Toast toast = Toast.makeText(HumActivity.this,"2222222", Toast.LENGTH_SHORT);
                    toast.show();
                    // 접속 상태를 true로 셋팅한다.
                    isConnect = true;
                    // 메세지 수신을 위한 스레드 가동
                    isRunning = true;
                }
            });

            HumSendThread thread = new HumSendThread();
            thread.start();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("빈칸없이 입력해주세요.");
            builder.setPositiveButton("확인", null);
            builder.show();
        }
    }

    class HumSendThread extends Thread {
        EditText humText = (EditText) findViewById(R.id.humEdit);

        @Override
        public void run() {
            try {
                str = humText.getText().toString();
                SocketHandler s = ((SocketHandler) getApplication());
                member_socket = s.getSocket();

                // 닉네임을 송신한다.
                OutputStream os = member_socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);

                InputStream is = member_socket.getInputStream();
                DataInputStream dis = new DataInputStream(is);

                Protocol protocol = new Protocol();

                //protocol = new Protocol(Protocol.PT_USERHUM_SET);
                protocol.setOrder(str);
                os.write(protocol.getPacket());

                // ProgressDialog 를 제거한다.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isConnect = true;
                        // 메세지 수신을 위한 스레드 가동
                        isRunning = true;

                        humText.setText(null);

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.weathermenu) {
            Uri gotoWeather = Uri.parse("http://www.weather.go.kr/weather/main.jsp");
            Intent weatherintent = new Intent(Intent.ACTION_VIEW, gotoWeather);
            startActivity(weatherintent);
        }
        return super.onOptionsItemSelected(item);
    }

    class ReceiveOnOff2 extends  Thread{

        Socket socket;

        Protocol protocol =  new Protocol();

        public ReceiveOnOff2(Socket socket) {
            try {
                this.socket = socket;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void run(){
            try {
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();

                while(true){

                    // Protocol protocol = new Protocol();
                    // 기본적으로 1000개 잡혀있음
                    byte[] buf = protocol.getPacket();
                    is.read(buf);
                    // 패킷 타입을 얻음
                    int packetType = buf[0];
                    protocol.setPacket(packetType, buf);

                    if (packetType == Protocol.PT_EXIT) {
                        protocol = new Protocol(Protocol.PT_EXIT);
                        os.write(protocol.getPacket());
                        System.out.println("서버 종료");
                        break;
                    }

                    switch (packetType) {
                        case Protocol.PT_RES_FAN:
                            final String order2 = protocol.getResfan();
                            final  String did2 = protocol.getResId();
                            // order = protocol.getOrder();

                            if(order2.contentEquals("off") && did2.contentEquals(user_ID)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        fan.setImageResource(R.drawable.stop_fan);
                                        final Toast toast = Toast.makeText(HumActivity.this, "2222222", Toast.LENGTH_SHORT);
                                        toast.show();
                                        isConnect = true;
                                        // 메세지 수신을 위한 스레드 가동
                                        isRunning = true;
                                    }
                                });
                            }
                            break;

                        case Protocol.PT_ID_ORDER:
                            final String order3 = protocol.getResfan();
                            final  String did3 = protocol.getResId();
                            // order = protocol.getOrder();

                            if(order3.contentEquals("off") && did3.contentEquals(user_ID)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        fan.setImageResource(R.drawable.stop_fan);
                                        final Toast toast = Toast.makeText(HumActivity.this, "2멈쳐제발", Toast.LENGTH_SHORT);
                                        toast.show();
                                        isConnect = true;
                                        // 메세지 수신을 위한 스레드 가동
                                        isRunning = true;
                                    }
                                });
                            }
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    class ReceiveOnOff extends  Thread{

        Socket socket;

        Protocol protocol =  new Protocol();

        public ReceiveOnOff(Socket socket) {
            try {
                this.socket = socket;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void run(){
            try {
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();

                while(true){

                    // Protocol protocol = new Protocol();
                    // 기본적으로 1000개 잡혀있음
                    byte[] buf = protocol.getPacket();
                    is.read(buf);
                    // 패킷 타입을 얻음
                    int packetType = buf[0];
                    protocol.setPacket(packetType, buf);

                    if (packetType == Protocol.PT_EXIT) {
                        protocol = new Protocol(Protocol.PT_EXIT);
                        os.write(protocol.getPacket());
                        System.out.println("서버 종료");
                        break;
                    }

                    switch (packetType) {
                        case Protocol.PT_RES_FAN:
                            final String order2 = protocol.getResfan();
                            final  String did2 = protocol.getResId();
                            // order = protocol.getOrder();

                            if(order2.contentEquals("off") && did2.contentEquals(user_ID)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        fan.setImageResource(R.drawable.stop_fan);
                                        final Toast toast = Toast.makeText(HumActivity.this, "777", Toast.LENGTH_SHORT);
                                        toast.show();
                                        isConnect = true;
                                        // 메세지 수신을 위한 스레드 가동
                                        isRunning = true;
                                    }
                                });
                            }
                            break;

                        case Protocol.PT_ID_ORDER:
                            final String order3 = protocol.getResfan();
                            final  String did3 = protocol.getResId();
                            // order = protocol.getOrder();

                            if(order3.contentEquals("off") && did3.contentEquals(user_ID)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        fan.setImageResource(R.drawable.stop_fan);
                                        final Toast toast = Toast.makeText(HumActivity.this, "717멈쳐제발", Toast.LENGTH_SHORT);
                                        toast.show();
                                        isConnect = true;
                                        // 메세지 수신을 위한 스레드 가동
                                        isRunning = true;
                                    }
                                });
                            }
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    // 추가한 클래스
    class ReceiveHumidity extends Thread {

        Socket socket;
        TextView humTextView = (TextView) findViewById(R.id.humview);
        Protocol protocol = new Protocol(Protocol.PT_REQ_DEV_STATUS);

        public ReceiveHumidity(Socket socket) {
            try {
                this.socket = socket;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void run(){
            try {
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();

                os.write(protocol.getPacket());
                while(true){

                    Protocol protocol = new Protocol();
                    // 기본적으로 1000개 잡혀있음
                    byte[] buf = protocol.getPacket();
                    is.read(buf);
                    // 패킷 타입을 얻음
                    int packetType = buf[0];
                    protocol.setPacket(packetType, buf);

                    if (packetType == Protocol.PT_EXIT) {
                        protocol = new Protocol(Protocol.PT_EXIT);
                        os.write(protocol.getPacket());
                        System.out.println("서버 종료");
                        break;
                    }

                    switch(packetType) {
                        case Protocol.PT_RES_DEV_STATUS:
                            while(true) {
                                final int humidity = protocol.getHumidity();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Stuff that updates the UI
                                        humTextView.setText(humidity);

                                        final Toast toast = Toast.makeText(HumActivity.this,humidity, Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                });
                                os.write(protocol.getPacket());
                                break;
                            }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}