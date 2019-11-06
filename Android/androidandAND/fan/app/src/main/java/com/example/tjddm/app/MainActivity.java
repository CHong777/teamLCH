package com.example.tjddm.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    Toolbar myToolbar;
    String user_ID;
    String Order;
    boolean isConnect = false;
    Socket member_socket;
    boolean isRunning=false;

    String IOTid = "IOT";
    String dfid = "717";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SocketHandler s = ((SocketHandler) getApplication());
        member_socket = s.getSocket();

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        setTitle("");
        final Button mainButton = (Button) findViewById(R.id.mainButton);
        final ToggleButton humbtn = (ToggleButton)findViewById(R.id.humButton);
        final Button infobtn = (Button)findViewById(R.id.infoButton);

        ImageView fan = (ImageView) findViewById(R.id.fan);
        Switch aSwitch = (Switch) findViewById(R.id.onoffButton);

        Intent intent = getIntent(); //이 액티비티를 부른 인텐트를 받는다.

        user_ID = intent.getStringExtra("USER_ID");
        Order = intent.getStringExtra("order");

        if(Order.equals("on")){
            aSwitch.setChecked(true);
            GlideDrawableImageViewTarget onfan = new GlideDrawableImageViewTarget(fan);
            Glide.with(this).load(R.drawable.on_fan).fitCenter().into(onfan);
        }else{
            aSwitch.setChecked(false);
            fan.setImageResource(R.drawable.stop_fan);
        }

        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        infobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switch aSwitch = (Switch) findViewById(R.id.onoffButton);
                boolean ON = aSwitch.isChecked();
                ImageView fan = (ImageView) findViewById(R.id.fan);
                if(ON){
                    Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                    SocketHandler s = ((SocketHandler) getApplication());
                    s.setSocket(member_socket);
                    intent.putExtra("USER_ID", user_ID);
                    intent.putExtra("order","on");
                    startActivity(intent);
                    MainActivity.this.finish();

                }else {
                    Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                    SocketHandler s = ((SocketHandler) getApplication());
                    s.setSocket(member_socket);
                    intent.putExtra("USER_ID", user_ID);
                    intent.putExtra("order","off");
                    startActivity(intent);
                    MainActivity.this.finish();
                }
            }
        });

        humbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(humbtn.isChecked()){
                    humbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.pressed));
                }else{
                    humbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.basic));
                }

            }
        });

        if(user_ID.equals(IOTid)) {
            ClientReceiver thread = new ClientReceiver(member_socket);
            thread.start();
        }
        else{
            DfReceiver thread2 = new DfReceiver(member_socket);
            thread2.start();
        }

        StatusReceiver statusReceiver = new StatusReceiver(member_socket);
        statusReceiver.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.weathermenu){
            Uri gotoWeather = Uri.parse("http://www.weather.go.kr/weather/main.jsp");
            Intent weatherintent = new Intent(Intent.ACTION_VIEW, gotoWeather);
            startActivity(weatherintent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onoffMethod(View v){
        Switch aSwitch = (Switch) findViewById(R.id.onoffButton);
        boolean on = ((Switch) v).isChecked();
        ImageView fan = (ImageView) findViewById(R.id.fan);

        if(on){
            Toast.makeText(getApplicationContext(), "환풍기 ON", Toast.LENGTH_SHORT).show();
            GlideDrawableImageViewTarget onfan = new GlideDrawableImageViewTarget(fan);
            Glide.with(this).load(R.drawable.on_fan).fitCenter().into(onfan);
            On thread = new On();
            thread.start();


        }else {
            Toast.makeText(MainActivity.this, "환풍기 OFF", Toast.LENGTH_SHORT).show();
            fan.setImageResource(R.drawable.stop_fan);
            Off thread = new Off();
            thread.start();
        }

    }

    class On extends Thread {
        Switch aSwitch = (Switch) findViewById(R.id.onoffButton);
        boolean on = aSwitch.isChecked();
        String str;
        @Override
        public void run() {
            try {
                // 접속한다.
                SocketHandler s = ((SocketHandler) getApplication());
                member_socket = s.getSocket();

                // 닉네임을 송신한다.
                OutputStream os = member_socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);

                InputStream is = member_socket.getInputStream();
                DataInputStream dis=new DataInputStream(is);

                Protocol protocol = new Protocol();

                protocol = new Protocol(Protocol.PT_Ventilator_ORDER);
                protocol.setOrder("on");
                os.write(protocol.getPacket());

                // ProgressDialog 를 제거한다.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isConnect = true;
                        // 메세지 수신을 위한 스레드 가동
                        isRunning = true;
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class Off extends Thread {
        Switch aSwitch = (Switch) findViewById(R.id.onoffButton);
        boolean on = aSwitch.isChecked();
        String str;
        @Override
        public void run() {
            try {
                // 접속한다.
                SocketHandler s = ((SocketHandler) getApplication());
                member_socket = s.getSocket();

                // 닉네임을 송신한다.
                OutputStream os = member_socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);

                InputStream is = member_socket.getInputStream();
                DataInputStream dis=new DataInputStream(is);

                Protocol protocol = new Protocol();

                protocol = new Protocol(Protocol.PT_Ventilator_ORDER);
                protocol.setOrder("off");
                os.write(protocol.getPacket());

                // ProgressDialog 를 제거한다.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isConnect = true;
                        isRunning = true;
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ClientReceiver extends Thread {
        private Socket socket;
        String order;
        String dvid;

        Switch aSwitch = (Switch) findViewById(R.id.onoffButton);
        boolean on = aSwitch.isChecked();
        ImageView fan = (ImageView) findViewById(R.id.fan);
        TextView temTextView = (TextView) findViewById(R.id.temText);

        public ClientReceiver(Socket socket) {
            try {
                this.socket = socket;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public void run() {

            try {
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();

                while (true) {
                    Protocol protocol = new Protocol();
                    // 기본적으로 1000개 잡혀있음
                    byte[] buf = protocol.getPacket();
                    is.read(buf);
                    // 패킷 타입을 얻음
                    int packetType = buf[0];
                    protocol.setPacket(packetType, buf);

                    switch (packetType) {
                        case Protocol.PT_ID_ORDER:
                            order = protocol.getMyOrder();
                            dvid = protocol.getMyId();
                            if(order.equals("on") && dvid.equals(user_ID)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        aSwitch.setChecked(true);
                                        GlideDrawableImageViewTarget onfan = new GlideDrawableImageViewTarget(fan);
                                        Glide.with(MainActivity.this).load(R.drawable.on_fan).fitCenter().into(onfan);
                                        final Toast toast = Toast.makeText(MainActivity.this, user_ID, Toast.LENGTH_SHORT);
                                        toast.show();
                                        // 접속 상태를 true로 셋팅한다.
                                        isConnect = true;
                                        // 메세지 수신을 위한 스레드 가동
                                        isRunning = true;
                                    }
                                });
                            }
                            else if(order.contentEquals("off")&& dvid.contentEquals(user_ID)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        aSwitch.setChecked(false);
                                        fan.setImageResource(R.drawable.stop_fan);
                                        // 접속 상태를 true로 셋팅한다.
                                        final Toast toast = Toast.makeText(MainActivity.this, user_ID, Toast.LENGTH_SHORT);
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
            }catch(IOException e) {}
        }
    }

    class DfReceiver extends Thread {
        private Socket socket;
        String order;
        String dvid;

        Switch aSwitch = (Switch) findViewById(R.id.onoffButton);
        boolean on = aSwitch.isChecked();
        ImageView fan = (ImageView) findViewById(R.id.fan);
        TextView temTextView = (TextView) findViewById(R.id.temText);

        public DfReceiver(Socket socket) {
            try {
                this.socket = socket;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public void run() {

            try {
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();

                while (true) {
                    Protocol protocol = new Protocol();
                    // 기본적으로 1000개 잡혀있음
                    byte[] buf = protocol.getPacket();
                    is.read(buf);
                    // 패킷 타입을 얻음
                    int packetType = buf[0];
                    protocol.setPacket(packetType, buf);

                    switch (packetType) {
                        case Protocol.PT_TOML:
                            order = protocol.getUMLOrder();
                            dvid = protocol.getUMLId();
                            if(order.equals("on")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        aSwitch.setChecked(true);
                                        GlideDrawableImageViewTarget onfan = new GlideDrawableImageViewTarget(fan);
                                        Glide.with(MainActivity.this).load(R.drawable.on_fan).fitCenter().into(onfan);
                                        final Toast toast = Toast.makeText(MainActivity.this, user_ID, Toast.LENGTH_SHORT);
                                        toast.show();
                                        // 접속 상태를 true로 셋팅한다.
                                        isConnect = true;
                                        // 메세지 수신을 위한 스레드 가동
                                        isRunning = true;
                                    }
                                });
                            }
                            else if(order.contentEquals("off")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        aSwitch.setChecked(false);
                                        fan.setImageResource(R.drawable.stop_fan);
                                        // 접속 상태를 true로 셋팅한다.
                                        final Toast toast = Toast.makeText(MainActivity.this, user_ID, Toast.LENGTH_SHORT);
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
            }catch(IOException e) {}
        }
    }

    class StatusReceiver extends Thread {

        Socket socket;

        TextView temTextView = (TextView) findViewById(R.id.temText);
        TextView humTextView = (TextView) findViewById(R.id.humText);

        Protocol protocol = new Protocol(Protocol.PT_REQ_DEV_STATUS);

        public StatusReceiver(Socket socket) {
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

                protocol.setMyId(user_ID);
                System.out.println(user_ID);

                os.write(protocol.getPacket());
                while(true){
                    protocol = new Protocol();
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

                                final int temperature = protocol.getTemperature();
                                final int humidity = protocol.getHumidity();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Stuff that updates the UI
                                        temTextView.setText(Integer.toString(temperature));
                                        humTextView.setText(Integer.toString(humidity));
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