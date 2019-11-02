package com.example.dell.linkgame;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dell.linkgame.utils.ExitApplication;
import com.example.dell.linkgame.utils.GameConfig;

import java.util.ArrayList;
import java.util.List;


public class RoundActivity extends AppCompatActivity {
    private ImageButton backButton; //返回上级按钮
    private Button relaxBtn;//休闲模式按钮
    private Button classicBtn;//经典模式按钮
    private Button hellBtn;//地狱模式按钮
    private TextView textView;
    private String theme;//游戏主题
    private boolean isMusic;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);
        ExitApplication.getInstance().addActivity(this);
        Intent intent = getIntent();
        theme = intent.getStringExtra("theme");
        isMusic = intent.getBooleanExtra("isMusic",false);
        textView = (TextView)findViewById((R.id.textView));
        switch(theme){
            case "pan":
                textView.setText("动物派");
                break;
            case "pfr":
                textView.setText("水果派");
                break;
            case "pic":
                textView.setText("冰激凌派");
                break;
            case "pfc":
                textView.setText("面孔派");
                break;

        }
        backButton = (ImageButton) findViewById(R.id.imageButton);
        backButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent config = new Intent(RoundActivity.this,MainActivity.class);
                config.putExtra("isNew",false);
                config.putExtra("isMusic",isMusic);
                startActivityForResult(config,1);
            }
        });

        relaxBtn = (Button) findViewById(R.id.button1);
        relaxBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent config = new Intent(RoundActivity.this,GameActivity.class);
                config.putExtra("theme",theme);
                config.putExtra("mode",1);
                config.putExtra("isMusic",isMusic);
                startActivityForResult(config,1);
            }
        });

        classicBtn = (Button) findViewById(R.id.button2);
        classicBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent config = new Intent(RoundActivity.this,GameActivity.class);
                config.putExtra("theme",theme);
                config.putExtra("mode",2);
                config.putExtra("isMusic",isMusic);
                startActivityForResult(config,1);
            }
        });

        hellBtn = (Button) findViewById(R.id.button3);
        hellBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent config = new Intent(RoundActivity.this,GameActivity.class);
                config.putExtra("theme",theme);
                config.putExtra("mode",3);
                config.putExtra("isMusic",isMusic);
                startActivityForResult(config,1);
            }
        });

    }
}
