package com.example.dell.linkgame;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;



public class MusicService extends Service{
    private MyBinder mBinder = new MyBinder();
    public MediaPlayer mediaPlayer;//初始化MediaPlayer

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MyBinder extends Binder {

        /**
         * 播放音乐
         */
        public void playMusic() {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(MusicService.this, R.raw.aaa);
                mediaPlayer.setLooping(true);
            }
            if (!mediaPlayer.isPlaying()) {
                //如果还没开始播放，就开始
                mediaPlayer.start();
            }
        }

        /**
         * 暂停播放
         */
        public void pauseMusic() {
            if (mediaPlayer.isPlaying()) {
                //如果还没开始播放，就开始
                mediaPlayer.pause();
            }
        }

        /**
         * 关闭播放器
         */
        public void closeMedia() {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }

    }

}
