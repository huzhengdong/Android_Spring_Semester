package com.domker.study.androidstudy;

import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MediaPlayerActivity extends AppCompatActivity {
    private SurfaceView surfaceView;
    private MediaPlayer player;
    private SurfaceHolder holder;
    private SeekBar seekBar;
    private Handler handler;
    private Runnable runnable;
    private TextView textViewTime;
    private TextView textViewCurrentPosition;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("MediaPlayer");

        setContentView(R.layout.layout_media_player);
        textViewCurrentPosition =findViewById(R.id.textViewCurrentPosition1);
        textViewTime =findViewById(R.id.textViewTime1);
        surfaceView = findViewById(R.id.surfaceView);
        seekBar = findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                try {
                    if (player.isPlaying()) {
                        int current = player.getCurrentPosition();
                        int duration = player.getDuration();
                        seekBar.setProgress(current * 100 / duration);
                        textViewTime.setText(time(player.getCurrentPosition()));
                        textViewCurrentPosition.setText(time(duration));
                    }
                }
                catch (IllegalStateException e)
                {
                    player =null;
                }
                catch (NullPointerException e)
                {
                    System.out.println("player is null");
                }
                handler.postDelayed(runnable,50);
            }
        };
        handler.post(runnable);


        player = new MediaPlayer();
        try {
            player.setDataSource(getResources().openRawResourceFd(R.raw.big_buck_bunny));
            holder = surfaceView.getHolder();
            holder.setFormat(PixelFormat.TRANSPARENT);
            holder.addCallback(new PlayerCallBack());
            player.prepare();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 自动播放
                    player.start();
                    player.setLooping(true);
                }
            });
            player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    System.out.println(percent);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        findViewById(R.id.buttonPlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.start();
            }
        });

        findViewById(R.id.buttonPause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.pause();
            }
        });


    }


    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        // 当进度条停止修改的时候触发
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // 取得当前进度条的刻度
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            int duration = player.getDuration();
            progress = seekBar.getProgress();
            if (player.isPlaying()&&fromUser) {
                // 设置当前播放的位置
                player.seekTo(duration*progress/100);
            }

        }
    };
    protected String time(long millionSeconds) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millionSeconds);
        return simpleDateFormat.format(c.getTime());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.stop();
            player.release();
        }
    }

    private class PlayerCallBack implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            player.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
