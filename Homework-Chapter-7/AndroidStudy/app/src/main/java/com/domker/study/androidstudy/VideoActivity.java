package com.domker.study.androidstudy;

import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.Log;
import android.widget.MediaController;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class VideoActivity extends AppCompatActivity {
    private static final String TAG = "Videoactivity.;";
    private Button buttonPlay;
    private Button buttonPause;
    private TextView textViewTime;
    private TextView textViewCurrentPosition;
    private VideoView videoView;
    private SeekBar seekBar;
    private Handler handler;
    private Runnable runnable;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_video);
        Log.d(TAG, "run: Runnable start");
        textViewCurrentPosition =findViewById(R.id.textViewCurrentPosition);
        textViewTime =findViewById(R.id.textViewTime);
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                Log.d(TAG, "run: Runnable");
                if (videoView.isPlaying()) {
                    int current = videoView.getCurrentPosition();
                    int duration = videoView.getDuration();
                    seekBar.setProgress(current*100/ duration);
                    textViewTime.setText(time(videoView.getCurrentPosition()));
                    textViewCurrentPosition.setText(time(duration));
                    Log.d(TAG, String.valueOf(current*100/duration));
                    Log.d(TAG, String.valueOf(duration));
                }
                handler.postDelayed(runnable,50);
            }
        };
        handler.post(runnable);



        buttonPause = findViewById(R.id.buttonPause);
        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.pause();
            }
        });

        buttonPlay = findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.start();
                Log.d(TAG, "run: Runnable play");
            }
        });

        seekBar =findViewById(R.id.seekbar1);
        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);

        videoView = findViewById(R.id.videoView);
        videoView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        videoView.setZOrderOnTop(true);
        videoView.setVideoPath(getVideoPath(R.raw.big_buck_bunny));


    }


    private String getVideoPath(int resId) {
        return "android.resource://" + this.getPackageName() + "/" + resId;
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
            int duration = videoView.getDuration();
            progress = seekBar.getProgress();
            if (videoView.isPlaying()&&fromUser) {
                // 设置当前播放的位置
                videoView.seekTo(duration*progress/100);
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
    protected void onDestroy() {

        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
