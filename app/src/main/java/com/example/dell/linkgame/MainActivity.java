package com.example.dell.linkgame;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dell.linkgame.utils.ExitApplication;


/**
 * 主题界面Activity
 */
public class MainActivity extends AppCompatActivity {
    private ImageButton musicButton; //音乐暂停按钮
    private Button animalButton; //动物主题按钮
    private Button fruitButton; //水果主题按钮
    private Button iceScreamButton; //风景主题按钮
    private Button flagButton; //国旗主题按钮
    private Button exitButton; //退出按钮
    private MusicService.MyBinder myBinder;
    private boolean isMusic;
    private boolean isNew;
    Intent MediaServiceIntent; //“绑定”服务的intent
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (MusicService.MyBinder) service;
            if(isNew){
                myBinder.playMusic();
                Log.i("MainActivity","connected2!");
                isMusic = true;
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myBinder = null;
        }

    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ExitApplication.getInstance().addActivity(this);
        Intent intent = getIntent();
        isNew = intent.getBooleanExtra("isNew",true);
        isMusic = intent.getBooleanExtra("isMusic",false);
        MediaServiceIntent = new Intent(this, MusicService.class);
        bindService(MediaServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);//与service建立通话，准备播放

        musicButton = (ImageButton) findViewById(R.id.imageButton);
        musicButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(isMusic){
                    myBinder.pauseMusic();
                    isMusic = false;
                }else{
                    myBinder.playMusic();
                    isMusic = true;
                }
            }

        });
        animalButton = (Button) findViewById(R.id.button_an);
        animalButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openGame("pan");
            }

        });
        fruitButton = (Button) findViewById(R.id.button_fr);
        fruitButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openGame("pfr");
            }

        });

        iceScreamButton = (Button) findViewById(R.id.button_ic);
        iceScreamButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openGame("pic");
            }

        });
        flagButton = (Button) findViewById(R.id.button_fl);
        flagButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openGame("pfc");
            }

        });

        exitButton = (Button) findViewById(R.id.button_ex);
        exitButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("确定退出?");
                builder.setTitle("提示");

                //添加AlertDialog.Builder对象的setPositiveButton()方法
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ExitApplication.getInstance().exit();
                    }
                });

                //添加AlertDialog.Builder对象的setNegativeButton()方法
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.create().show();
            }

        });
    }

    private void openGame(String theme){
        Intent config = new Intent(this,RoundActivity.class);
        config.putExtra("theme",theme);
        config.putExtra("isMusic",isMusic);
        startActivityForResult(config,1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isMusic){
            myBinder.closeMedia();
        }
        unbindService(mServiceConnection);
    }

}
