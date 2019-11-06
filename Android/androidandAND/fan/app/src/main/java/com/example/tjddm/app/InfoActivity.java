package com.example.tjddm.app;

import android.content.Intent;
import android.icu.text.IDNA;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;

public class InfoActivity extends AppCompatActivity {
    String Order;
    String user_ID;
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        setTitle("");

        Intent intent = getIntent();
        Order = intent.getStringExtra("order");
        user_ID = intent.getStringExtra("USER_ID");


        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        Button humbtn = (Button)findViewById(R.id.humButton);
        Button mainbtn = (Button)findViewById(R.id.mainButton);
        Button infobtn = (Button)findViewById(R.id.infoButton);


        mainbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(Order.equals("on")){
                    Intent intent = new Intent(InfoActivity.this, MainActivity.class);
                    intent.putExtra("USER_ID", user_ID);
                    intent.putExtra("order","on");
                    startActivity(intent);
                    InfoActivity.this.finish();

                }else {
                    Intent intent = new Intent(InfoActivity.this, MainActivity.class);
                    intent.putExtra("USER_ID", user_ID);
                    intent.putExtra("order","off");
                    startActivity(intent);
                    InfoActivity.this.finish();
                }
            }
        });

        humbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(Order.equals("on")){
                    Intent intent = new Intent(InfoActivity.this, HumActivity.class);
                    intent.putExtra("USER_ID", user_ID);
                    intent.putExtra("order","on");
                    startActivity(intent);
                    InfoActivity.this.finish();

                }else {
                    Intent intent = new Intent(InfoActivity.this, HumActivity.class);
                    intent.putExtra("USER_ID", user_ID);
                    intent.putExtra("order","off");
                    startActivity(intent);
                    InfoActivity.this.finish();
                }
            }
        });

        infobtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            }
        });

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

}
